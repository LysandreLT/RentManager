package com.epf.rentmanager.service;

import java.util.ArrayList;
import java.util.List;

import com.epf.rentmanager.dao.DaoException;
import com.epf.rentmanager.model.Vehicle;
import com.epf.rentmanager.dao.VehicleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class VehicleService {

	private VehicleDao vehicleDao;
	public static VehicleService instance;

	@Autowired
	private ReservationService reservationService;

	private VehicleService(VehicleDao vehicleDao){
		this.vehicleDao = vehicleDao;
	}
	
	
	public long create(Vehicle vehicle) throws ServiceException {
		if (vehicle.getConstructeur() == null || vehicle.getConstructeur().isBlank()){
			throw new ServiceException("Le véhicule doit avoir son constructeur défini");
		}

		if (vehicle.getModele() == null || vehicle.getModele().isBlank()){
			throw new ServiceException("Le véhicule doit avoir son modèle défini");
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

	public int delete(Vehicle vehicle) throws ServiceException, DaoException {
		System.out.println(reservationService.findResaByVehicleId(vehicle.getId()));
		if (reservationService.findResaByVehicleId(vehicle.getId()) != null && !reservationService.findResaByVehicleId(vehicle.getId()).isEmpty()){
			throw new ServiceException("Le véhicule ne doit pas avoir de réservation à son nom");
		}
		try{
			vehicleDao.delete(vehicle);
		} catch (DaoException e) {
			throw new ServiceException("Erreur pendant la suppression du véhicule");
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

	public int count() throws ServiceException {
		try {
			return (vehicleDao.count());
		} catch (DaoException e) {
			throw new ServiceException("Erreur pendant la récupération du nombre de véhicules");
		}
	}

	public long modify(Vehicle vehicle, int id) throws ServiceException {
		if (vehicle.getConstructeur() == null || vehicle.getConstructeur().isBlank()){
			throw new ServiceException("Le véhicule doit avoir un constructeur");
		}

		if (vehicle.getModele() == null || vehicle.getModele().isBlank()){
			throw new ServiceException("Le véhicule doit avoir un modèle");
		}

		try {
			return vehicleDao.modify(vehicle, id);
		} catch (DaoException e) {
			throw new ServiceException("erreur durant la modification du véhicule");
		}
	}
	
}
