package com.example.weatherapp_miniprojet.Entities;

public class Ville {
    int idVille;
    String nomVille;

    public Ville(){

    }
    public Ville(String nomVille) {
        this.nomVille = nomVille;
    }
    public int getIdVille() {
        return idVille;
    }

    public void setIdVille(int idVille) {
        this.idVille = idVille;
    }

    public String getNomVille() {
        return nomVille;
    }

    public void setNomVille(String nomVille) {
        this.nomVille = nomVille;
    }
}