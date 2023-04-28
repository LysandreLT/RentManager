package com.epf.rentmanager.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.epf.rentmanager.model.Reservation;
import com.epf.rentmanager.persistence.ConnectionManager;
import org.springframework.stereotype.Repository;


@Repository
public class ReservationDao {

	private static ReservationDao instance = null;
	private ReservationDao() {}
	
	private static final String CREATE_RESERVATION_QUERY = "INSERT INTO Reservation(client_id, vehicle_id, debut, fin) VALUES(?, ?, ?, ?);";
	private static final String DELETE_RESERVATION_QUERY = "DELETE FROM Reservation WHERE id=?;";
	private static final String FIND_RESERVATIONS_BY_CLIENT_QUERY = "SELECT id, vehicle_id, debut, fin FROM Reservation WHERE client_id=?;";
	private static final String FIND_RESERVATIONS_BY_VEHICLE_QUERY = "SELECT id, client_id, debut, fin FROM Reservation WHERE vehicle_id=?;";
	private static final String FIND_RESERVATIONS_QUERY = "SELECT id, client_id, vehicle_id, debut, fin FROM Reservation;";
	private static final String FIND_RESERVATION_QUERY = "SELECT id, client_id, vehicle_id, debut, fin FROM Reservation WHERE id=?;";
	private static final String COUNT_RESERVATIONS_QUERY = "SELECT COUNT(*) FROM Reservation;";
	private static final String MODIFY_RESERVATION_QUERY = "UPDATE Reservation SET client_id=?, vehicle_id=?, debut=?, fin=? WHERE id=?;";
		
	public long create(Reservation reservation) throws DaoException {
		try(Connection connection = ConnectionManager.getConnection()) {
			PreparedStatement statement = connection.prepareStatement(CREATE_RESERVATION_QUERY, Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, Integer.toString(reservation.getClient_id()));
			statement.setString(2, Integer.toString(reservation.getVehicle_id()));
			statement.setString(3, reservation.getDebut().toString());
			statement.setString(4, reservation.getFin().toString());

			statement.executeUpdate();
			ResultSet resultSet = statement.getGeneratedKeys();
			if (resultSet.next()) {
				int id = resultSet.getInt(1);
				return id;
			}

		} catch (SQLException e) {
			throw new DaoException("Erreur lors de la création de la réservation");
		}
		return 0;
	}
	
	public long delete(Reservation reservation) throws DaoException {
		try(Connection connection = ConnectionManager.getConnection()){

			PreparedStatement statement = connection.prepareStatement(DELETE_RESERVATION_QUERY);
			statement.setString(1, Integer.toString(reservation.getId()));
			statement.executeUpdate();


		} catch (SQLException e) {
			throw new DaoException("Erreur lors de la suppression de la réservation");
		}
		return 0;
	}

	
	public List<Reservation> findResaByClientId(long clientId) throws DaoException {
		try(Connection connection = ConnectionManager.getConnection()){

			PreparedStatement statement = connection.prepareStatement(FIND_RESERVATIONS_BY_CLIENT_QUERY);
			statement.setString(1, Integer.toString((int) clientId));
			statement.executeQuery();
			ResultSet resultSet = statement.getResultSet();
			List<Reservation> reservations = new ArrayList<Reservation>();
			while(resultSet.next()) {
				int reservationId = resultSet.getInt(1);
				int vehicleId = resultSet.getInt(2);
				LocalDate debut = resultSet.getDate(3).toLocalDate();
				LocalDate fin = resultSet.getDate(4).toLocalDate();
				reservations.add(new Reservation(reservationId, (int) clientId, vehicleId, debut, fin));
			}
			return new ArrayList<Reservation>(reservations);

		} catch (SQLException e) {
			throw new DaoException("Erreur lors de la recherche de la réservation par identifiant");
		}
	}
	
	public List<Reservation> findResaByVehicleId(long vehicleId) throws DaoException {
		try(Connection connection = ConnectionManager.getConnection()){

			PreparedStatement statement = connection.prepareStatement(FIND_RESERVATIONS_BY_VEHICLE_QUERY);
			statement.setString(1, Integer.toString((int) vehicleId));
			statement.executeQuery();
			ResultSet resultSet = statement.getResultSet();
			List<Reservation> reservations = new ArrayList<Reservation>();
			while(resultSet.next()) {
				int reservationId = resultSet.getInt(1);
				int clientId = resultSet.getInt(2);
				LocalDate debut = resultSet.getDate(3).toLocalDate();
				LocalDate fin = resultSet.getDate(4).toLocalDate();
				reservations.add(new Reservation(reservationId, clientId, (int) vehicleId, debut, fin));
			}
			return new ArrayList<Reservation>(reservations);

		} catch (SQLException e) {
			throw new DaoException("Erreur lors de la recherche de la réservation par vehicule");
		}
	}

	public List<Reservation> findAll() throws DaoException {
		try(Connection connection = ConnectionManager.getConnection()){

			PreparedStatement statement = connection.prepareStatement(FIND_RESERVATIONS_QUERY);
			statement.executeQuery();
			ResultSet resultSet = statement.getResultSet();
			List<Reservation> reservations = new ArrayList<Reservation>();
			while(resultSet.next()) {
				int id = resultSet.getInt(1);
				int client_id = resultSet.getInt(2);
				int vehicle_id = resultSet.getInt(3);
				LocalDate debut = resultSet.getDate(4).toLocalDate();
				LocalDate fin = resultSet.getDate(5).toLocalDate();
				Reservation bufferReservation = new Reservation(id, client_id, vehicle_id, debut, fin);
				reservations.add(bufferReservation);
			}
			return new ArrayList<Reservation>(reservations);

		} catch (SQLException e) {
			throw new DaoException("Erreur lors de la recherche des réservations");
		}
	}


	public Reservation findById(long id) throws DaoException {
		try(Connection connection = ConnectionManager.getConnection()){

			PreparedStatement statement = connection.prepareStatement(FIND_RESERVATION_QUERY);
			statement.setString(1, Integer.toString((int) id));
			statement.executeQuery();
			ResultSet resultSet = statement.getResultSet();
			if (resultSet.next()) {
				int idClient = resultSet.getInt(2);
				int idVehicule = resultSet.getInt(3);
				LocalDate debut = resultSet.getDate(4).toLocalDate();
				LocalDate fin = resultSet.getDate(5).toLocalDate();

				return new Reservation((int) id, idClient, idVehicule, debut, fin);
			}

		} catch (SQLException e) {
			throw new DaoException("Erreur lors de la recherche de la réservation par identifiant");
		}
		return null;
	}

	public int count() throws DaoException {
		try (Connection connection = ConnectionManager.getConnection()) {

			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(COUNT_RESERVATIONS_QUERY);
			if (resultSet.next()) {
				return (resultSet.getInt(1));
			}

		} catch (SQLException e) {
			throw new DaoException("Erreur lors de la recherche du nombre de réservations");
		}
		return 0;
	}

	public long modify(Reservation reservation, int id) throws DaoException {
		try(Connection connection = ConnectionManager.getConnection()) {
			PreparedStatement statement = connection.prepareStatement(MODIFY_RESERVATION_QUERY);
			statement.setString(1, String.valueOf(reservation.getClient_id()));
			statement.setString(2, String.valueOf(reservation.getVehicle_id()));
			statement.setString(3, String.valueOf(reservation.getDebut()));
			statement.setString(4, String.valueOf(reservation.getFin()));
			statement.setString(5, String.valueOf(id));
			statement.executeUpdate();

		} catch (SQLException e) {
			throw new DaoException("Erreur lors de la modification de la réservation");
		}
		return 0;
	}
}
