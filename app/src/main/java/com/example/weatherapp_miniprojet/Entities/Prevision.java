package com.example.weatherapp_miniprojet.Entities;

public class Prevision {
    private String jour;
    private String icon;
    private int tempMax;
    private int tempMin;

    public Prevision() {
    }

    public Prevision(String jour, String icon, int tempMax, int tempMin) {
        this.jour = jour;
        this.icon = icon;
        this.tempMax = tempMax;
        this.tempMin = tempMin;
    }

    public Prevision(String jour, String icon) {
        this.jour = jour;
        this.icon = icon;
    }

    public String getJour() {
        return jour;
    }

    public void setJour(String jour) {
        this.jour = jour;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getTempMax() {
        return tempMax;
    }

    public void setTempMax(int tempMax) {
        this.tempMax = tempMax;
    }

    public int getTempMin() {
        return tempMin;
    }

    public void setTempMin(int tempMin) {
        this.tempMin = tempMin;
    }
}
