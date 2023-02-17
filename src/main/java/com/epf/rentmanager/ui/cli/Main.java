package com.epf.rentmanager.ui.cli;

import java.io.IOException;

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
            System.in.read();
        }
    }
}
