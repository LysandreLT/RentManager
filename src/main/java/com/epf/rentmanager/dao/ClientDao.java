package com.epf.rentmanager.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.persistence.ConnectionManager;

public class ClientDao {
	
	private static ClientDao instance = null;
	private ClientDao() {}
	public static ClientDao getInstance() {
		if(instance == null) {
			instance = new ClientDao();
		}
		return instance;
	}
	
	private static final String CREATE_CLIENT_QUERY = "INSERT INTO Client(nom, prenom, email, naissance) VALUES(?, ?, ?, ?);";
	private static final String DELETE_CLIENT_QUERY = "DELETE FROM Client WHERE id=?;";
	private static final String FIND_CLIENT_QUERY = "SELECT nom, prenom, email, naissance FROM Client WHERE id=?;";
	private static final String FIND_CLIENTS_QUERY = "SELECT id, nom, prenom, email, naissance FROM Client;";

	public long create(Client client) throws DaoException {
		try(Connection connection = ConnectionManager.getConnection()) {
			PreparedStatement statement = connection.prepareStatement(CREATE_CLIENT_QUERY, Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, client.getNom().toUpperCase());
			statement.setString(2, client.getPrenom());
			statement.setString(3, client.getEmail());
			statement.setString(4, client.getNaissance().toString());

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
	
	public long delete(Client client) throws DaoException {
		try(Connection connection = ConnectionManager.getConnection()){

			PreparedStatement statement = connection.prepareStatement(DELETE_CLIENT_QUERY);
			statement.setString(1, Integer.toString(client.getId()));
			statement.executeUpdate();


		} catch (SQLException e) {
			throw new DaoException("Erreur lors de la suppression du client");
		}
		return 0;
	}

	public Client findById(long id) throws DaoException {
		try(Connection connection = ConnectionManager.getConnection()){

			PreparedStatement statement = connection.prepareStatement(FIND_CLIENT_QUERY);
			statement.setString(1, Integer.toString((int) id));
			statement.executeUpdate();
			ResultSet resultSet = statement.getResultSet();
			if (resultSet.next()) {
				Client client = (Client)resultSet.getObject(1);
				return new Client(client);
			}

		} catch (SQLException e) {
			throw new DaoException("Erreur lors de la recherche du client par identifiant");
		}
		return null;
	}

	public List<Client> findAll() throws DaoException {
		try(Connection connection = ConnectionManager.getConnection()){

			PreparedStatement statement = connection.prepareStatement(FIND_CLIENTS_QUERY);
			statement.executeUpdate();
			ResultSet resultSet = statement.getResultSet();
			List<Client> clients = new ArrayList<Client>();
			while(resultSet.next()) {
				resultSet.next();
				clients.add((Client)resultSet.getObject(1));
			}
			return clients;

		} catch (SQLException e) {
			throw new DaoException("Erreur lors de la recherche du client par identifiant");
		}
	}

}
