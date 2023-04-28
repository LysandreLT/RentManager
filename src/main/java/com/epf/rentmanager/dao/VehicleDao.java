package com.epf.rentmanager.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.epf.rentmanager.model.Vehicle;
import com.epf.rentmanager.persistence.ConnectionManager;
import org.springframework.stereotype.Repository;


@Repository
public class VehicleDao {

	private static VehicleDao instance = null;

	private VehicleDao() {
	}

	private static final String CREATE_VEHICLE_QUERY = "INSERT INTO Vehicle(constructeur, modele, nb_places) VALUES(?, ?, ?);";
	private static final String DELETE_VEHICLE_QUERY = "DELETE FROM Vehicle WHERE id=?;";
	private static final String FIND_VEHICLE_QUERY = "SELECT id, constructeur, modele, nb_places FROM Vehicle WHERE id=?;";
	private static final String FIND_VEHICLES_QUERY = "SELECT id, constructeur, modele, nb_places FROM Vehicle;";
	private static final String COUNT_VEHICLES_QUERY = "SELECT COUNT(*) FROM Vehicle;";
	private static final String MODIFY_VEHICLE_QUERY = "UPDATE Vehicle SET constructeur=?, modele=?, nb_places=? WHERE id=?;";

	public int create(Vehicle vehicle) throws DaoException {
		try (Connection connection = ConnectionManager.getConnection()) {
			PreparedStatement statement = connection.prepareStatement(CREATE_VEHICLE_QUERY, Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, vehicle.getConstructeur());
			statement.setString(2, vehicle.getModele());
			statement.setString(3, Integer.toString(vehicle.getNb_places()));
			statement.executeUpdate();
			ResultSet resultSet = statement.getGeneratedKeys();
			if (resultSet.next()) {
				int id = resultSet.getInt(1);
				return id;
			}

		} catch (SQLException e) {
			throw new DaoException("Erreur lors de la création du véhicule");
		}
		return 0;
	}

	public long delete(Vehicle vehicle) throws DaoException {
		try (Connection connection = ConnectionManager.getConnection()) {

			PreparedStatement statement = connection.prepareStatement(DELETE_VEHICLE_QUERY);
			statement.setString(1, Integer.toString(vehicle.getId()));
			statement.executeUpdate();


		} catch (SQLException e) {
			throw new DaoException("Erreur lors de la suppression du véhicule");
		}
		return 0;
	}

	public Vehicle findById(long id) throws DaoException {
		try (Connection connection = ConnectionManager.getConnection()) {

			PreparedStatement statement = connection.prepareStatement(FIND_VEHICLE_QUERY);
			statement.setString(1, Integer.toString((int) id));
			statement.executeQuery();
			ResultSet resultSet = statement.getResultSet();
			if (resultSet.next()) {
				int idVehicule = resultSet.getInt(1);
				String constructeur = resultSet.getString(2);
				String modele = resultSet.getString(3);
				int nbPlaces = resultSet.getInt(4);
				return new Vehicle(idVehicule, constructeur, modele, nbPlaces);
			}

		} catch (SQLException e) {
			throw new DaoException("Erreur lors de la recherche du véhicule par identifiant");
		}
		return null;
	}

	public List<Vehicle> findAll() throws DaoException {
		try (Connection connection = ConnectionManager.getConnection()) {

			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(FIND_VEHICLES_QUERY);
			List<Vehicle> vehicles = new ArrayList<Vehicle>();
			while (resultSet.next()) {
				int idVehicule = resultSet.getInt(1);
				String constructeurVehicule = resultSet.getString(2);
				String modeleVehicule = resultSet.getString(3);
				int nbDePlaceVehicule = resultSet.getInt(4);
				Vehicle bufferVehicule = new Vehicle(idVehicule, constructeurVehicule, modeleVehicule, nbDePlaceVehicule);
				vehicles.add(bufferVehicule);
			}
			return vehicles;

		} catch (SQLException e) {
			throw new DaoException("Erreur lors de la recherche du client par identifiant");
		}
	}

	public int count() throws DaoException {
		try (Connection connection = ConnectionManager.getConnection()) {

			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(COUNT_VEHICLES_QUERY);
			if (resultSet.next()) {
				return (resultSet.getInt(1));
			}

		} catch (SQLException e) {
			throw new DaoException("Erreur lors de la recherche du nombre de véhicules");
		}
		return 0;
	}

	public long modify(Vehicle vehicle, int id) throws DaoException {
		try(Connection connection = ConnectionManager.getConnection()) {
			PreparedStatement statement = connection.prepareStatement(MODIFY_VEHICLE_QUERY);
			statement.setString(1, vehicle.getConstructeur());
			statement.setString(2, vehicle.getModele());
			statement.setString(3, String.valueOf(vehicle.getNb_places()));
			statement.setString(4, String.valueOf(id));
			statement.executeUpdate();

		} catch (SQLException e) {
			throw new DaoException("Erreur lors de la modification du véhicule");
		}
		return 0;
	}
}
