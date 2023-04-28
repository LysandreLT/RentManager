package com.epf.rentmanager.servlet;

import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.model.Reservation;
import com.epf.rentmanager.model.Vehicle;
import com.epf.rentmanager.service.ReservationService;
import com.epf.rentmanager.service.ServiceException;
import com.epf.rentmanager.service.ClientService;
import com.epf.rentmanager.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/rents/create")
public class RentsCreateServlet extends HttpServlet {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private ReservationService reservationService;

    @Override
    public void init() throws ServletException {
        super.init();
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            List<Vehicle> vehicules= vehicleService.findAll();
            List<String> constructeursEtModeles = vehicules.stream().map(Vehicle::getConstructeurEtModele).collect(Collectors.toList());
            request.setAttribute("constructeursEtModeles", constructeursEtModeles);
            request.setAttribute("vehicules", vehicules);
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }

        try {
            List<Client> clients = clientService.findAll();
            List<String> nomsPrenoms = clients.stream().map(Client::getNomPrenom).collect(Collectors.toList());
            request.setAttribute("nomsPrenoms", nomsPrenoms);
            request.setAttribute("clients", clients);
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }

        this.getServletContext().getRequestDispatcher("/WEB-INF/views/rents/create.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse
            response) throws ServletException, IOException {

        int vehicle_id = Integer.parseInt(request.getParameter("car"));
        int client_id = Integer.parseInt(request.getParameter("client"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate debut = LocalDate.parse(request.getParameter("begin"), formatter);
        LocalDate fin = LocalDate.parse(request.getParameter("end"), formatter);
        Reservation nouvelleReservation = new Reservation(client_id, vehicle_id, debut, fin);
        try {
            long result = reservationService.create(nouvelleReservation);
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }

        response.sendRedirect("create");
    }

}
