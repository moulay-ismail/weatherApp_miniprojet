package com.example.weatherapp_miniprojet.ConnectAPI;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public class HandleJSON {
    private static final String OPEN_WEATHER_MAP_URL = "http://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&units=metric";
    private static final String OPEN_WEATHER_MAP_API = "88f399bec2c33cb721b859e49d93b9dd";
    public static String[] getJSONResponse(String latitude, String longitude){
        String[] jsonData =new String[7];
        JSONObject jsonWeather = null;
        try {
            jsonWeather = getWeatherJSON(latitude,longitude);
        }
        catch (Exception e){
            Log.d("Error", "impossible de traiter le résultat json", e);
        }

        try {
            if(jsonWeather !=null){
                //Recuperation des données
                JSONObject details =jsonWeather.getJSONArray("weather").getJSONObject(0);
                JSONObject main = jsonWeather.getJSONObject("main");
                DateFormat df = DateFormat.getDateInstance();

                String city = jsonWeather.getString("name")+", "+jsonWeather.getJSONObject("sys").getString("country");
                String description = details.getString("description").toLowerCase(Locale.US);
                String temperature = String.format("%.0f", main.getDouble("temp"))+"°";
                String humidity = main.getString("humidity")+"%";
                String pressure = main.getString("pressure")+" hPa";
                String updateOn = df.format(new Date(jsonWeather.getLong("dt")*1000));
                //String iconeText = setWeatherIcon(details.getInt("id"),jsonWeather.getJSONObject("sys").getLong("sunrise")*1000, jsonWeather.getJSONObject("sys").getLong("sunset")*1000);
                String iconeText = details.getString("icon");

                jsonData[0] = city;
                jsonData[1] = description;
                jsonData[2] = temperature;
                jsonData[3] = humidity;
                jsonData[4] = pressure;
                jsonData[5] = updateOn;
                jsonData[6] = iconeText;
            }
        }
        catch (Exception e ){

        }
        return jsonData;
    }

    public static String setWeatherIcon(int actualId, long sunrise, long sunset){
        int id = actualId/100;
        String icon = "";
        if(actualId==800){
            long currentTime = new Date().getTime();
            if(currentTime>=sunrise && currentTime<sunset){
                icon = "&#xf00d;";
            }else {
                icon = "&#xf02e;";
            }
        }else {
            switch (id){
                case 2:
                    icon = "&#xf01e;";
                    break;
                case 3:
                    icon = "&#xfc01c;";
                    break;
                case 7:
                    icon = "&#xf014";
                    break;
                case 8:
                    icon = "&#xf013;";
                    break;
                case 6:
                    icon = "&#xf01b;";
                    break;
                case 5:
                    icon = "&#xf019;";
                    break;
            }
        }
        return icon;
    }

    public static JSONObject getWeatherJSON(String lat,String lon){
        try{
            URL url = new URL(String.format(OPEN_WEATHER_MAP_URL, lat, lon));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // openConnection() : Renvoie une instance URLConnection qui représente une connexion à l'objet distant référencé par l'URL.
            connection.addRequestProperty("x-api-key", OPEN_WEATHER_MAP_API);
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuffer json = new StringBuffer(1024);
            String tmp = "";
            while ((tmp=reader.readLine()) != null){
                json.append(tmp).append("\n");
            }
            reader.close();
            JSONObject data = new JSONObject(json.toString());
            if (data.getInt("cod") != 200){
                return null;
            }
            return data;
        }
        catch (Exception e){
            return null;
        }
    }
}
