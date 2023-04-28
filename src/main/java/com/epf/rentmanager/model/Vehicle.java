package com.epf.rentmanager.model;

public class Vehicle {
    private int id;
    private String constructeur;
    private String modele;
    private int nb_places;

    public Vehicle(int id, String constructeur, String modele, int nb_places) {
        this.id = id;
        this.constructeur = constructeur;
        this.modele = modele;
        this.nb_places = nb_places;
    }

    public Vehicle(String constructeur, String modele, int nb_places) {
        this.constructeur = constructeur;
        this.modele = modele;
        this.nb_places = nb_places;
    }

    public Vehicle(int id, String constructeur, int nb_places) {
        this.id = id;
        this.constructeur = constructeur;
        this.nb_places = nb_places;
    }

    public Vehicle(Vehicle vehicle) {
        this.id = vehicle.id;
        this.constructeur = vehicle.constructeur;
        this.modele = vehicle.modele;
        this.nb_places = vehicle.nb_places;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getConstructeur() {
        return constructeur;
    }

    public void setConstructeur(String constructeur) {
        this.constructeur = constructeur;
    }

    public String getModele() {
        return modele;
    }

    public void setModele(String modele) {
        this.modele = modele;
    }

    public int getNb_places() {
        return nb_places;
    }

    public void setNb_places(int nb_places) {
        this.nb_places = nb_places;
    }

    public String getConstructeurEtModele(){return constructeur + " " + modele;}

    @Override
    public String toString() {
        return "Vehicle{" +
                "id=" + id +
                ", constructeur='" + constructeur + '\'' +
                ", modele='" + modele + '\'' +
                ", nb_places=" + nb_places +
                '}';
    }
}
