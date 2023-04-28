package com.epf.rentmanager.servlet;
import com.epf.rentmanager.dao.DaoException;
import com.epf.rentmanager.model.Reservation;
import com.epf.rentmanager.model.Vehicle;
import com.epf.rentmanager.service.ClientService;
import com.epf.rentmanager.service.ReservationService;
import com.epf.rentmanager.service.ServiceException;
import com.epf.rentmanager.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/rents")
public class RentsServlet extends HttpServlet {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private ClientService clientService;

    @Override
    public void init() throws ServletException {
        super.init();
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }



    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (request.getParameter("delete") != null){
            int id = Integer.parseInt(request.getParameter("delete"));
            try {
                Reservation reservationASupprimer = reservationService.findById(id);
                int sortie = reservationService.delete(reservationASupprimer);
            } catch (ServiceException e) {
                throw new RuntimeException(e);
            }
        }

        List<Reservation> reservations = null;
        try {
            reservations = reservationService.findAll();
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }
        List<String> vehicules = reservations.stream().map(reservation -> {
            try {
                return vehicleService.findById(reservation.getVehicle_id()).getConstructeurEtModele();
            } catch (ServiceException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());

        List<String> clients = reservations.stream().map(reservation -> {
            try {
                return clientService.findById(reservation.getClient_id()).getNomPrenom();
            } catch (ServiceException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
        request.setAttribute("reservations", reservations);
        request.setAttribute("vehicules", vehicules);
        request.setAttribute("clients", clients);
        this.getServletContext().getRequestDispatcher("/WEB-INF/views/rents/list.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse
            response) throws ServletException, IOException {

    }
}