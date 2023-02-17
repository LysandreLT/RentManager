package com.epf.rentmanager.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import com.epf.rentmanager.dao.ClientDao;
import com.epf.rentmanager.dao.DaoException;
import com.epf.rentmanager.model.Client;

public class ClientService {

	private ClientDao clientDao;
	public static ClientService instance;
	
	private ClientService() {
		this.clientDao = ClientDao.getInstance();
	}
	
	public static ClientService getInstance() {
		if (instance == null) {
			instance = new ClientService();
		}
		
		return instance;
	}
	
	
	public long create(Client client) throws ServiceException {
		LocalDate now = java.time.LocalDate.now();
		if (Period.between(client.getNaissance(), now).getYears() < 18) {
			throw new ServiceException("le client doit être majeur");
		}

		if (client.getPrenom() == null || client.getPrenom().isBlank()){
			throw new ServiceException("Le client doit avoir un prénom");
		}

		if (client.getNom() == null || client.getNom().isBlank()){
			throw new ServiceException("Le client doit avoir un nom");
		}

		try {
			return clientDao.create(client);
		} catch (DaoException e) {
			throw new ServiceException("erreur durant la création du client");
		}
	}

	public Client findById(long id) throws ServiceException {
		try {
			Client client = clientDao.findById(id);
			return new Client(client);
		} catch (DaoException e) {
			throw new ServiceException("Erreur pendant la récupération du client");
		}
	}

	public List<Client> findAll() throws ServiceException {
		try {
			return new ArrayList<Client>(clientDao.findAll());
		} catch (DaoException e) {
			throw new ServiceException("Erreur pendant la récupération de la liste de clients");
		}
	}

	public int delete(Client client) throws ServiceException{
		try{
			clientDao.delete(client);
		} catch (DaoException e) {
			throw new ServiceException("Erreur pendant la suppression du client");
		}
		return 0;
	}
	
}
