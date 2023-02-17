package com.epf.rentmanager.service;

import java.util.ArrayList;
import java.util.List;

import com.epf.rentmanager.dao.DaoException;
import com.epf.rentmanager.service.ServiceException;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.model.Vehicle;
import com.epf.rentmanager.dao.ClientDao;
import com.epf.rentmanager.dao.VehicleDao;

public class VehicleService {

	private VehicleDao vehicleDao;
	public static VehicleService instance;
	
	private VehicleService() {
		this.vehicleDao = VehicleDao.getInstance();
	}
	
	public static VehicleService getInstance() {
		if (instance == null) {
			instance = new VehicleService();
		}
		
		return instance;
	}
	
	
	public long create(Vehicle vehicle) throws ServiceException {
		if (vehicle.getConstructeur() == null || vehicle.getConstructeur().isBlank()){
			throw new ServiceException("Le véhicule doit avoir son constructeur défini");
		}

		if (vehicle.getNb_places() < 2){
			throw new ServiceException("Le véhicule doit avoir au moins 2 places");
		}

		try {
			vehicleDao.create(vehicle);
		} catch (DaoException e) {
			throw new ServiceException("Erreur pendant la création du véhicule");
		}
		return 0;
	}

	public Vehicle findById(long id) throws ServiceException {
		try {
			Vehicle vehicle = vehicleDao.findById(id);
			return new Vehicle(vehicle);
		} catch (DaoException e) {
			throw new ServiceException("Erreur pendant la récupération du véhicule avec l'id");
		}
		
	}

	public List<Vehicle> findAll() throws ServiceException {
		try {
			return new ArrayList<Vehicle>(vehicleDao.findAll());
		} catch (DaoException e) {
			throw new ServiceException("Erreur pendant la récupération de la liste de véhicules");
		}
	}
	
}
