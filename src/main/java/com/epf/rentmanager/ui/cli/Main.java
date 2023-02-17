package com.epf.rentmanager.ui.cli;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        while(true){
            System.out.println("Que souhaitez vous faire? \n " +
                    "1 : Créer un client \n " +
                    "2 : Afficher la liste de tous les clients \n" +
                    "3 : Ajouter un véhicule \n" +
                    "4 : Afficher la liste de tous les véhicules \n" +
                    "5 : Supprimer un client \n " +
                    "6 : Supprimer un véhicule");
            Scanner scanner = new Scanner(System.in);
            switch(Integer.parseInt(scanner.nextLine())){
                case 1:
                    System.out.println("Ajout d'un nouveau client...");

                case 2:
                    System.out.println("Liste des clients :");

                case 3:
                    System.out.println("Ajout d'un véhicule");
            }
        }
    }
}
