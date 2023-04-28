package com.epf.rentmanager.servlet;

import com.epf.rentmanager.model.Vehicle;
import com.epf.rentmanager.service.ServiceException;
import com.epf.rentmanager.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/cars/modify")
public class VehicleModifyServlet extends HttpServlet {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Autowired
    private VehicleService vehicleService;

    @Override
    public void init() throws ServletException {
        super.init();
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        this.getServletContext().getRequestDispatcher("/WEB-INF/views/vehicles/modify.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse
            response) throws ServletException, IOException {
        int idVehiculeAModifier = Integer.parseInt(request.getParameter("id"));
        String constructeur = request.getParameter("constructeur");
        String modele = request.getParameter("modele");
        int nbDePlaces = Integer.parseInt(request.getParameter("nbDePlaces"));
        Vehicle modificationVehicule = new Vehicle(idVehiculeAModifier, constructeur, modele, nbDePlaces);
        try {
            long result = vehicleService.modify(modificationVehicule, idVehiculeAModifier);
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }

        response.sendRedirect("/rentmanager/cars");
    }

}
