package com.epf.rentmanager.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.epf.rentmanager.dao.ClientDao;
import com.epf.rentmanager.dao.DaoException;
import com.epf.rentmanager.model.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ClientService {

	private ClientDao clientDao;
	public static ClientService instance;

	@Autowired
	private ReservationService reservationService;

	private ClientService(ClientDao clientDao){
		this.clientDao = clientDao;
	}
	
	
	public long create(Client client) throws ServiceException {
		LocalDate now = java.time.LocalDate.now();
		client.setNaissance(now);

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

	public int delete(Client client) throws ServiceException, DaoException {
		if (reservationService.findResaByClientId(client.getId()) != null && !reservationService.findResaByClientId(client.getId()).isEmpty()){
			throw new ServiceException("Le client ne doit pas avoir de réservation à son nom");
		}
		try{
			clientDao.delete(client);
		} catch (DaoException e) {
			throw new ServiceException("Erreur pendant la suppression du client");
		}
		return 0;
	}

	public int count() throws ServiceException {
		try {
			return (clientDao.count());
		} catch (DaoException e) {
			throw new ServiceException("Erreur pendant la récupération du nombre de clients");
		}
	}

	public long modify(Client client, int id) throws ServiceException {
		if (client.getPrenom() == null || client.getPrenom().isBlank()){
			throw new ServiceException("Le client doit avoir un prénom");
		}

		if (client.getNom() == null || client.getNom().isBlank()){
			throw new ServiceException("Le client doit avoir un nom");
		}

		try {
			return clientDao.modify(client, id);
		} catch (DaoException e) {
			throw new ServiceException("erreur durant la modification du client");
		}
	}
	
}
