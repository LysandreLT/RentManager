package com.epf.rentmanager.configuration;

import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.model.Reservation;
import com.epf.rentmanager.model.Vehicle;
import com.epf.rentmanager.persistence.ConnectionManager;
import com.epf.rentmanager.service.ClientService;
import com.epf.rentmanager.service.ReservationService;
import com.epf.rentmanager.service.ServiceException;
import com.epf.rentmanager.service.VehicleService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class CommandLine {

    private ClientService clientService;
    private VehicleService vehicleService;
    private ReservationService reservationService;

    private CommandLine() {


    }

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfiguration.class);
        ClientService clientService = context.getBean(ClientService.class);
        ReservationService reservationService = context.getBean(ReservationService.class);
        VehicleService vehicleService = context.getBean(VehicleService.class);
        while(true){
            System.out.println("Que souhaitez vous faire? \n " +
                    "1 : Créer un client \n " +
                    "2 : Afficher la liste de tous les clients \n" +
                    "3 : Supprimer un client \n " +
                    "4 : Ajouter un véhicule \n" +
                    "5 : Afficher la liste de tous les véhicules \n" +
                    "6 : Supprimer un véhicule \n" +
                    "7 : Ajouter une réservation \n" +
                    "8 : Afficher la liste de toutes les réservations \n" +
                    "9 : Supprimer une réservation \n" +
                    "10 : Ajouter une colonne à la base de données \n");

            Scanner scanner = new Scanner(System.in);
            switch(Integer.parseInt(scanner.nextLine())){
                case 1:
                    System.out.println("Ajout d'un nouveau client...");
                    System.out.println("Nom du client : ");
                    String nomClient = scanner.nextLine();
                    System.out.println("Prénom du client : ");
                    String prenomClient = scanner.nextLine();
                    Client client = new Client(nomClient, prenomClient);
                    System.out.println(client);
                    break;

                case 2:
                    System.out.println("Liste des clients :");
                    try {
                        System.out.println(clientService.findAll());
                    } catch (ServiceException e) {
                        throw new RuntimeException(e);
                    }
                    break;

                case 3:
                    System.out.println("Ajout d'un nouveau client...");
                    System.out.println("Id du client : ");
                    int idClient = Integer.parseInt(scanner.nextLine());

                case 4:
                    System.out.println("Ajout d'un véhicule : ");
                    System.out.println("Constructeur du véhicule : ");
                    String constructeurVehicule = scanner.nextLine();
                    System.out.println("Modèle du véhicule : ");
                    String modeleVehicule = scanner.nextLine();
                    System.out.println("Nombre du places dans le véhicule : ");
                    int nbDePlacesVehicule = Integer.parseInt(scanner.nextLine());
                    Vehicle nouveauVehicule = new Vehicle(constructeurVehicule, modeleVehicule, nbDePlacesVehicule);
                    try {
                        long result = vehicleService.create(nouveauVehicule);
                    } catch (ServiceException e) {
                        throw new RuntimeException(e);
                    }
                    break;

                case 5:
                    System.out.println("Affichage de la liste de véhicules : ");
                    try {
                        System.out.println(vehicleService.findAll());
                    } catch (ServiceException e) {
                        throw new RuntimeException(e);
                    }
                    break;

                case 7:
                    System.out.println("Ajout d'une réservation");
                    System.out.println("Id du client : ");
                    int idClientReservation = Integer.parseInt(scanner.nextLine());
                    System.out.println("Id du véhicule : ");
                    int idVehicle = Integer.parseInt(scanner.nextLine());
                    System.out.println("Date du début de la location (JJ/MM/AAAA) : ");
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    LocalDate debut = LocalDate.parse(scanner.nextLine(), formatter);
                    System.out.println("Date de fin de la location (JJ/MM/AAAA) : ");
                    LocalDate fin = LocalDate.parse(scanner.nextLine(), formatter);

                    Reservation nouvelleReservation = new Reservation(idClientReservation, idVehicle,   debut, fin);

                    try {
                        long result = reservationService.create(nouvelleReservation);
                    } catch (ServiceException e) {
                        throw new RuntimeException(e);
                    }
                    break;

                case 8:
                    System.out.println("Affichage de la liste des réservations : ");
                    try {
                        System.out.println(reservationService.findAll());
                    } catch (ServiceException e) {
                        throw new RuntimeException(e);
                    }
                    break;

                case 10:
                    System.out.println("Ajout d'une colonne à la base de données : ");
                    System.out.println("Nom de la table à modifier : ");
                    String nomTable = scanner.nextLine();
                    System.out.println("Nom de la colonne à ajouter : ");
                    String nomColonne = scanner.nextLine();
                    Connection connection = null;
                    try {
                        connection = ConnectionManager.getConnection();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    PreparedStatement statement = null;
                    try {
                        statement = connection.prepareStatement("ALTER TABLE " + nomTable + " ADD " + nomColonne + " VARCHAR(255)");
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        statement.executeUpdate();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
            }

        }
    }
}
