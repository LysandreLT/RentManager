package com.epf.rentmanager.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.model.Reservation;
import com.epf.rentmanager.persistence.ConnectionManager;

public class ReservationDao {

	private static ReservationDao instance = null;
	private ReservationDao() {}
	public static ReservationDao getInstance() {
		if(instance == null) {
			instance = new ReservationDao();
		}
		return instance;
	}
	
	private static final String CREATE_RESERVATION_QUERY = "INSERT INTO Reservation(client_id, vehicle_id, debut, fin) VALUES(?, ?, ?, ?);";
	private static final String DELETE_RESERVATION_QUERY = "DELETE FROM Reservation WHERE id=?;";
	private static final String FIND_RESERVATIONS_BY_CLIENT_QUERY = "SELECT id, vehicle_id, debut, fin FROM Reservation WHERE client_id=?;";
	private static final String FIND_RESERVATIONS_BY_VEHICLE_QUERY = "SELECT id, client_id, debut, fin FROM Reservation WHERE vehicle_id=?;";
	private static final String FIND_RESERVATIONS_QUERY = "SELECT id, client_id, vehicle_id, debut, fin FROM Reservation;";
		
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
			throw new DaoException("Erreur lors de la création du client");
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
			statement.executeUpdate();
			ResultSet resultSet = statement.getResultSet();
			List<Reservation> reservations = new ArrayList<Reservation>();
			while(resultSet.next()) {
				resultSet.next();
				reservations.add((Reservation) resultSet.getObject(1));
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
			statement.executeUpdate();
			ResultSet resultSet = statement.getResultSet();
			List<Reservation> reservations = new ArrayList<Reservation>();
			while(resultSet.next()) {
				resultSet.next();
				reservations.add((Reservation) resultSet.getObject(1));
			}
			return new ArrayList<Reservation>(reservations);

		} catch (SQLException e) {
			throw new DaoException("Erreur lors de la recherche de la réservation par vehicule");
		}
	}

	public List<Reservation> findAll() throws DaoException {
		try(Connection connection = ConnectionManager.getConnection()){

			PreparedStatement statement = connection.prepareStatement(FIND_RESERVATIONS_QUERY);
			statement.executeUpdate();
			ResultSet resultSet = statement.getResultSet();
			List<Reservation> reservations = new ArrayList<Reservation>();
			while(resultSet.next()) {
				resultSet.next();
				reservations.add((Reservation) resultSet.getObject(1));
			}
			return new ArrayList<Reservation>(reservations);

		} catch (SQLException e) {
			throw new DaoException("Erreur lors de la recherche des réservations");
		}
	}
}
