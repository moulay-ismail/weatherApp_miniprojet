package com.example.weatherapp_miniprojet.Entities;

public class PrevisionHoraire {
    private String heure;
    private String icon;
    private int temperature;

    public PrevisionHoraire() {
    }

    public PrevisionHoraire(String heure, String icon, int temperature) {
        this.heure = heure;
        this.icon = icon;
        this.temperature = temperature;
    }

    public String getHeure() {
        return heure;
    }

    public void setHeure(String heure) {
        this.heure = heure;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }
}
