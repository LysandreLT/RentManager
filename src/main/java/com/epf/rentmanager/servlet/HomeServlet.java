package com.epf.rentmanager.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.epf.rentmanager.service.ClientService;
import com.epf.rentmanager.service.ServiceException;
import com.epf.rentmanager.service.VehicleService;
import com.epf.rentmanager.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {

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
			int nbDeVehicules = vehicleService.count();
			request.setAttribute("nbDeVehicules", nbDeVehicules);
		} catch (ServiceException e) {
			throw new RuntimeException(e);
		}

		try {
			int nbDeClients = clientService.count();
			request.setAttribute("nbDeClients", nbDeClients);
		} catch (ServiceException e) {
			throw new RuntimeException(e);
		}

		try {
			int nbDeReservations = reservationService.count();
			request.setAttribute("nbDeReservations", nbDeReservations);
		} catch (ServiceException e) {
			throw new RuntimeException(e);
		}

		this.getServletContext().getRequestDispatcher("/WEB-INF/views/home.jsp").forward(request, response);
	}

}
