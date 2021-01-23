package com.example.weatherapp_miniprojet.connectAPI;

import android.util.Log;

import com.example.weatherapp_miniprojet.Entities.Prevision;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ForecastHandleJSON {
    private static final String OPEN_WEATHER_MAP_URL_FORECAST = "https://api.openweathermap.org/data/2.5/onecall?lat=%s&lon=%s&exclude=hourly,current,minutely,alerts&units=metric";
    private static final String OPEN_WEATHER_MAP_API = "88f399bec2c33cb721b859e49d93b9dd";

    public static ArrayList<Prevision> getJSONResponse(String latitude, String longitude) {
        //String[] jsonData = new String[2];
        ArrayList<Prevision> listP = new ArrayList<>();
        JSONObject jsonWeather = null;
        try {
            jsonWeather = getWeatherJSON(latitude, longitude);
        } catch (Exception e) {
            Log.d("Error", "impossible de traiter le résultat json", e);
        }
        try {

            if (jsonWeather != null) {
                //System.out.println("--------okkkkkkkkk-----------");
                //Recuperation des données

                JSONArray daily = jsonWeather.getJSONArray("daily");
                System.out.println("----------length : " + daily.length());
                for (int i = 0; i < daily.length(); i++) {
                    int dt = daily.getJSONObject(i).getInt("dt");
                    String date = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new java.util.Date((long) dt * 1000));
                    //System.out.println("------------- date : "+date);
                    Date date1 = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").parse(date);
                    DateFormat format2 = new SimpleDateFormat("EEEE");
                    String finalDay = format2.format(date1);
                    //System.out.println("--------------finalDay : "+finalDay);

                    JSONObject temp = daily.getJSONObject(i).getJSONObject("temp");
                    int tempMin = (int) temp.getDouble("min");
                    //System.out.println("-------------tempMin : "+tempMin);
                    int tempMax = (int) Math.round(temp.getDouble("max"));
                    //System.out.println("--------------tempMax : "+tempMax);

                    JSONArray weather = daily.getJSONObject(i).getJSONArray("weather");
                    String icone = weather.getJSONObject(0).getString("icon");
                    //System.out.println("------------icone : "+icone);
                    //System.out.println("******************************************************");
                    Prevision p = new Prevision(finalDay, icone, tempMax, tempMin);
                    listP.add(p);
                }

            } else {
                System.out.println("-------------weather null-------------");
            }
        } catch (Exception e) {

        }
        return listP;
    }

    public static JSONObject getWeatherJSON(String lat, String lon) {
        try {
            URL url = new URL(String.format(OPEN_WEATHER_MAP_URL_FORECAST, lat, lon));
            // System.out.println("----------url :"+url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // openConnection() : Renvoie une instance URLConnection qui représente une connexion à l'objet distant référencé par l'URL.
            connection.addRequestProperty("x-api-key", OPEN_WEATHER_MAP_API);
            System.out.println("----------url :" + connection.getURL());
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            StringBuffer json = new StringBuffer(1024);

            String tmp = "";
            while ((tmp = reader.readLine()) != null) {
                json.append(tmp).append("\n");
            }

            reader.close();

            JSONObject data = new JSONObject(json.toString());

            /*if (data.getInt("cod") != 200){

                System.out.println("----------echec---------");
                return null;
            }
            System.out.println("----------okkkkkkkk---------");*/
            return data;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
