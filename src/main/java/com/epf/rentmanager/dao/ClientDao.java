package com.epf.rentmanager.dao;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.model.Vehicle;
import com.epf.rentmanager.persistence.ConnectionManager;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Repository;

@Repository
public class ClientDao {

	private static ClientDao instance = null;

	private ClientDao() {}

	private static final String CREATE_CLIENT_QUERY = "INSERT INTO Client(nom, prenom, email, naissance) VALUES(?, ?, ?, ?);";
	private static final String DELETE_CLIENT_QUERY = "DELETE FROM Client WHERE id=?;";
	private static final String FIND_CLIENT_QUERY = "SELECT nom, prenom, email, naissance FROM Client WHERE id=?;";
	private static final String FIND_CLIENTS_QUERY = "SELECT id, nom, prenom, email, naissance FROM Client;";
	private static final String COUNT_CLIENTS_QUERY = "SELECT COUNT(*) FROM Client;";
	private static final String MODIFY_CLIENT_QUERY = "UPDATE Client SET nom=?, prenom=?, email=? WHERE id=?;";

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
			statement.executeQuery();
			ResultSet resultSet = statement.getResultSet();
			if (resultSet.next()) {
				String nom = resultSet.getString(1);
				String prenom = resultSet.getString(2);
				String email = resultSet.getString(3);
				Timestamp naissanceBrut = (Timestamp) resultSet.getObject(4);
				LocalDate naissance = naissanceBrut.toLocalDateTime().toLocalDate();
				return new Client((int) id, nom, prenom, email, naissance);
			}
		} catch (SQLException e) {
			throw new DaoException("Erreur lors de la recherche du client par identifiant");
		}
		return null;
	}

	public List<Client> findAll() throws DaoException {
		try(Connection connection = ConnectionManager.getConnection()){

			//"SELECT id, nom, prenom, email, naissance FROM Client;"
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(FIND_CLIENTS_QUERY);
			List<Client> clients = new ArrayList<Client>();
			while(resultSet.next()) {
				int idClient = resultSet.getInt(1);
				String nomClient = resultSet.getString(2);
				String prenomClient = resultSet.getString(3);
				String email = resultSet.getString(4);
				LocalDate naissanceClient = resultSet.getTimestamp(5).toLocalDateTime().toLocalDate();
				Client bufferClient = new Client(idClient, nomClient, prenomClient, email, naissanceClient);
				clients.add(bufferClient);
			}
			return clients;

		} catch (SQLException e) {
			throw new DaoException("Erreur lors de la recherche du client par identifiant");
		}
	}

	public int count() throws DaoException {
		try (Connection connection = ConnectionManager.getConnection()) {

			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(COUNT_CLIENTS_QUERY);
			if (resultSet.next()) {
				return (resultSet.getInt(1));
			}

		} catch (SQLException e) {
			throw new DaoException("Erreur lors de la recherche du nombre de clients");
		}
		return 0;
	}

	public long modify(Client client, int id) throws DaoException {
		try(Connection connection = ConnectionManager.getConnection()) {
			PreparedStatement statement = connection.prepareStatement(MODIFY_CLIENT_QUERY);
			statement.setString(1, client.getNom().toUpperCase());
			statement.setString(2, client.getPrenom());
			statement.setString(3, client.getEmail());
			statement.setString(4, String.valueOf(id));
			statement.executeUpdate();

		} catch (SQLException e) {
			throw new DaoException("Erreur lors de la modification du client");
		}
		return 0;
	}
}
