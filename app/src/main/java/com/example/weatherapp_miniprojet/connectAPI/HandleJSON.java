package com.example.weatherapp_miniprojet.connectAPI;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HandleJSON {
    private static final String OPEN_WEATHER_MAP_URL = "http://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&units=metric";
    private static final String OPEN_WEATHER_MAP_API = "88f399bec2c33cb721b859e49d93b9dd";
    Context context;

    public HandleJSON(Context context) {
        this.context = context;
    }

    public static String[] getJSONResponse(String latitude, String longitude) {
        String[] jsonData = new String[10];
        JSONObject jsonWeather = null;
        try {
            jsonWeather = getWeatherJSON(latitude, longitude);
        } catch (Exception e) {
            Log.d("Error", "impossible de traiter le résultat json", e);
        }

        try {
            if (jsonWeather != null) {
                //Recuperation des données
                JSONObject details = jsonWeather.getJSONArray("weather").getJSONObject(0);
                JSONObject main = jsonWeather.getJSONObject("main");
                JSONObject wind = jsonWeather.getJSONObject("wind");
                DateFormat df = DateFormat.getDateInstance();

                String city = jsonWeather.getString("name") + ", " + jsonWeather.getJSONObject("sys").getString("country");
                String description = details.getString("description").toLowerCase(Locale.US);
                String temperature = String.format("%.0f", main.getDouble("temp")) + "°";
                String humidity = main.getString("humidity") + "%";
                String pressure = main.getString("pressure") + " hPa";
                long dt = jsonWeather.getLong("dt");
                System.out.println("----------------dt : " + dt);
                String date = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new java.util.Date((long) dt * 1000));
                Date date1 = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").parse(date);
                DateFormat format2 = new SimpleDateFormat("HH:mm");
                String heure = format2.format(date1);
                DateFormat format3 = new SimpleDateFormat("EEEE dd MMM yyyy HH:mm");
                String updateOn = format3.format(date1);
                //String iconeText = setWeatherIcon(details.getInt("id"),jsonWeather.getJSONObject("sys").getLong("sunrise")*1000, jsonWeather.getJSONObject("sys").getLong("sunset")*1000);
                String iconeText = details.getString("icon");
                int visibility = jsonWeather.getInt("visibility") / 1000;
                System.out.println("---------visibilite : " + visibility + "km");
                double vent = wind.getDouble("speed");
                System.out.println("-------------vent : " + vent + " km/h");
                jsonData[0] = city;
                jsonData[1] = description;
                jsonData[2] = temperature;
                jsonData[3] = humidity;
                jsonData[4] = pressure;
                jsonData[5] = updateOn;
                jsonData[6] = iconeText;
                jsonData[7] = heure;
                jsonData[8] = visibility + " km";
                jsonData[9] = vent + " km/h";
            }
        } catch (Exception e) {

        }
        return jsonData;
    }
    public static JSONObject getWeatherJSON(String lat, String lon) {
        try {
            URL url = new URL(String.format(OPEN_WEATHER_MAP_URL, lat, lon));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // openConnection() : Renvoie une instance URLConnection qui représente une connexion à l'objet distant référencé par l'URL.
            connection.addRequestProperty("x-api-key", OPEN_WEATHER_MAP_API);
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuffer json = new StringBuffer(1024);
            String tmp = "";
            while ((tmp = reader.readLine()) != null) {
                json.append(tmp).append("\n");
            }
            reader.close();
            JSONObject data = new JSONObject(json.toString());
            if (data.getInt("cod") != 200) {
                return null;
            }
            return data;
        } catch (Exception e) {
            return null;
        }
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = context.getAssets().open("city.list.min.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}