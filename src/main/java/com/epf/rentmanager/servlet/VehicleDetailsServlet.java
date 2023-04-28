package com.epf.rentmanager.servlet;
import com.epf.rentmanager.model.Client;
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

@WebServlet("/cars/details")
public class VehicleDetailsServlet extends HttpServlet {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Autowired
    private ClientService clientService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private VehicleService vehicleService;

    @Override
    public void init() throws ServletException {
        super.init();
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            long id = Integer.parseInt(request.getParameter("id"));
            Vehicle vehicule = vehicleService.findById(id);
            List<Reservation> reservations = reservationService.findResaByVehicleId(id);
            List<Client> clients = reservations.stream().map(reservation -> {
                try {
                    return clientService.findById(reservation.getClient_id());
                } catch (ServiceException e) {
                    throw new RuntimeException(e);
                }
            }).collect(Collectors.toList());
            List<Client> clientsSansDoublons = clients.stream().distinct().collect(Collectors.toList());
            request.setAttribute("clients", clients);
            request.setAttribute("clientsSansDoublons", clientsSansDoublons);
            request.setAttribute("vehicule", vehicule);
            request.setAttribute("reservations", reservations);


        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }
        this.getServletContext().getRequestDispatcher("/WEB-INF/views/vehicles/details.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse
            response) throws ServletException, IOException {

    }
}