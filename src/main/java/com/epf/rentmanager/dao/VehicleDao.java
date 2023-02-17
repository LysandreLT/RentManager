package com.epf.rentmanager.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.model.Vehicle;
import com.epf.rentmanager.persistence.ConnectionManager;

public class VehicleDao {
	
	private static VehicleDao instance = null;
	private VehicleDao() {}
	public static VehicleDao getInstance() {
		if(instance == null) {
			instance = new VehicleDao();
		}
		return instance;
	}
	
	private static final String CREATE_VEHICLE_QUERY = "INSERT INTO Vehicle(constructeur, nb_places) VALUES(?, ?);";
	private static final String DELETE_VEHICLE_QUERY = "DELETE FROM Vehicle WHERE id=?;";
	private static final String FIND_VEHICLE_QUERY = "SELECT id, constructeur, nb_places FROM Vehicle WHERE id=?;";
	private static final String FIND_VEHICLES_QUERY = "SELECT id, constructeur, nb_places FROM Vehicle;";
	
	public long create(Vehicle vehicle) throws DaoException {
		try(Connection connection = ConnectionManager.getConnection()) {
			PreparedStatement statement = connection.prepareStatement(CREATE_VEHICLE_QUERY, Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, vehicle.getConstructeur());
			statement.setString(2, Integer.toString(vehicle.getNb_places()));

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
		try(Connection connection = ConnectionManager.getConnection()){

			PreparedStatement statement = connection.prepareStatement(DELETE_VEHICLE_QUERY);
			statement.setString(1, Integer.toString(vehicle.getId()));
			statement.executeUpdate();


		} catch (SQLException e) {
			throw new DaoException("Erreur lors de la suppression du véhicule");
		}
		return 0;
	}

	public Vehicle findById(long id) throws DaoException {
		try(Connection connection = ConnectionManager.getConnection()){

			PreparedStatement statement = connection.prepareStatement(FIND_VEHICLE_QUERY);
			statement.setString(1, Integer.toString((int) id));
			statement.executeUpdate();
			ResultSet resultSet = statement.getResultSet();
			if (resultSet.next()) {
				Vehicle vehicle = (Vehicle) resultSet.getObject(1);
				return new Vehicle(vehicle);
			}

		} catch (SQLException e) {
			throw new DaoException("Erreur lors de la recherche du véhicule par identifiant");
		}
		return null;
	}

	public List<Vehicle> findAll() throws DaoException {
		try(Connection connection = ConnectionManager.getConnection()){

			PreparedStatement statement = connection.prepareStatement(FIND_VEHICLES_QUERY);
			statement.executeUpdate();
			ResultSet resultSet = statement.getResultSet();
			List<Vehicle> vehicles = new ArrayList<Vehicle>();
			while(resultSet.next()) {
				resultSet.next();
				vehicles.add((Vehicle) resultSet.getObject(1));
			}
			return vehicles;

		} catch (SQLException e) {
			throw new DaoException("Erreur lors de la recherche du client par identifiant");
		}
	}
	

}
