package com.epf.rentmanager.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.epf.rentmanager.dao.ReservationDao;
import com.epf.rentmanager.dao.DaoException;
import com.epf.rentmanager.model.Reservation;
import org.springframework.stereotype.Service;

@Service
public class ReservationService {

    private ReservationDao reservationDao;
    public static ReservationService instance;

    private ReservationService(ReservationDao reservationDao){
        this.reservationDao = reservationDao;
    }


    public long create(Reservation reservation) throws ServiceException {
        LocalDate now = java.time.LocalDate.now();

        if (reservation.getClient_id() == 0){
            throw new ServiceException("L'ID client ne doit pas être nul");
        }

        if (reservation.getVehicle_id() == 0){
            throw new ServiceException("L'ID du véhicule ne doit pas être nul");
        }

        if (reservation.getDebut().isAfter(reservation.getFin())){
            throw new ServiceException("La début de la réservation doit être antérieure à la fin de la réservation");
        }

        try {
            List<Reservation> reservationsVehicule = reservationDao.findResaByVehicleId(reservation.getVehicle_id());
            for (Reservation res: reservationsVehicule
                 ) {
                if (!(reservation.getDebut().isBefore(res.getDebut()) || reservation.getFin().isAfter(res.getFin()))){
                    throw new ServiceException("Le véhicule n'est pas disponible");
                };
            }
        } catch (DaoException e) {
            throw new RuntimeException(e);
        }

        try {
            return reservationDao.create(reservation);
        } catch (DaoException e) {
            throw new ServiceException("erreur durant la création de la réservation");
        }
    }

    public Reservation findById(long id) throws ServiceException {
        try {
            Reservation reservation = reservationDao.findById(id);
            return new Reservation(reservation);
        } catch (DaoException e) {
            throw new ServiceException("Erreur pendant la récupération de la réservation");
        }
    }

    public List<Reservation> findAll() throws ServiceException {
        try {
            return new ArrayList<Reservation>(reservationDao.findAll());
        } catch (DaoException e) {
            throw new ServiceException("Erreur pendant la récupération de la liste de réservations");
        }
    }

    public int delete(Reservation reservation) throws ServiceException{
        try{
            reservationDao.delete(reservation);
        } catch (DaoException e) {
            throw new ServiceException("Erreur pendant la suppression de la réservation");
        }
        return 0;
    }

    public List<Reservation> findResaByClientId(long clientId) throws ServiceException{
        try{
            return new ArrayList<Reservation>(reservationDao.findResaByClientId(clientId));
        } catch (DaoException e) {
            throw new ServiceException("Erreur pendant la récupération de la liste de réservations");
        }
    }

    public List<Reservation> findResaByVehicleId(long vehicleId) throws ServiceException{
        try{
            return new ArrayList<Reservation>(reservationDao.findResaByVehicleId(vehicleId));
        } catch (DaoException e) {
            throw new ServiceException("Erreur pendant la récupération de la liste de réservations");
        }
    }

    public int count() throws ServiceException {
        try {
            return (reservationDao.count());
        } catch (DaoException e) {
            throw new ServiceException("Erreur pendant la récupération du nombre de réservations");
        }
    }

    public long modify(Reservation reservation, int id) throws ServiceException {

        if (reservation.getClient_id() == 0){
            throw new ServiceException("L'ID client ne doit pas être nul");
        }

        if (reservation.getVehicle_id() == 0){
            throw new ServiceException("L'ID du véhicule ne doit pas être nul");
        }

        if (reservation.getDebut().isAfter(reservation.getFin())){
            throw new ServiceException("La début de la réservation doit être antérieure à la fin de la réservation");
        }

        try {
            List<Reservation> reservationsVehicule = reservationDao.findResaByVehicleId(reservation.getVehicle_id());
            for (Reservation res: reservationsVehicule
            ) {
                if (!(reservation.getDebut().isBefore(res.getDebut()) || reservation.getFin().isAfter(res.getFin()))){
                    throw new ServiceException("Le véhicule n'est pas disponible");
                };
            }
        } catch (DaoException e) {
            throw new RuntimeException(e);
        }

        try {
            return reservationDao.modify(reservation, id);
        } catch (DaoException e) {
            throw new ServiceException("erreur durant la modification de la réservation");
        }
    }

}