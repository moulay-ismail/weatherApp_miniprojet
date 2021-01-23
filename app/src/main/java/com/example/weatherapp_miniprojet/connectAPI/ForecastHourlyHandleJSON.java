package com.example.weatherapp_miniprojet.connectAPI;

import android.util.Log;

import com.example.weatherapp_miniprojet.Entities.Prevision;
import com.example.weatherapp_miniprojet.Entities.PrevisionHoraire;

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

public class ForecastHourlyHandleJSON {
    private static final String OPEN_WEATHER_MAP_URL_FORECAST = "http://api.openweathermap.org/data/2.5/forecast?lat=%s&lon=%s&units=metric";
    private static final String OPEN_WEATHER_MAP_API = "88f399bec2c33cb721b859e49d93b9dd";

    public static ArrayList<PrevisionHoraire> getJSONResponse(String latitude, String longitude){
        ArrayList<PrevisionHoraire> listP = new ArrayList<>();
        JSONObject jsonWeather = null;
        try {
            jsonWeather = getWeatherJSON(latitude,longitude);
        }
        catch (Exception e){
            Log.d("Error", "impossible de traiter le résultat json", e);
        }
        try {
            if(jsonWeather !=null){
                System.out.println("-----------sucess----------");

                JSONArray list = jsonWeather.getJSONArray("list");
                System.out.println("----------length : "+list.length());

                for (int i=0;i<list.length();i++){

                    //System.out.println("------------"+i);
                    int dt= list.getJSONObject(i).getInt("dt");
                    //System.out.println("----------- dt : "+dt);
                    String date = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new java.util.Date ((long)dt*1000));
                    //System.out.println("------------- date : "+date);
                    Date date1=new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").parse(date);
                    DateFormat format2=new SimpleDateFormat("HH:mm");
                    String heure=format2.format(date1);
                    //System.out.println("--------------heure : "+heure);

                    JSONObject main = list.getJSONObject(i).getJSONObject("main");
                    int temp = (int) Math.round(main.getDouble("temp"));
                    //System.out.println("-------------temperature : "+temp);

                    JSONObject weather =jsonWeather.getJSONArray("list").getJSONObject(i).getJSONArray("weather").getJSONObject(0);
                    String icone = weather.getString("icon");
                    //System.out.println("------------ icone : "+icone);
                   // System.out.println("***********************");
                    PrevisionHoraire p = new PrevisionHoraire(heure, icone, temp);
                    listP.add(p);
                }

            }else{
                System.out.println("-------------weather null-------------");
            }
        }catch (Exception e ){

        }
        return  listP;
    }

    public static JSONObject getWeatherJSON(String lat, String lon){
        try {
            URL url = new URL(String.format(OPEN_WEATHER_MAP_URL_FORECAST, lat, lon));
            // System.out.println("----------url :"+url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // openConnection() : Renvoie une instance URLConnection qui représente une connexion à l'objet distant référencé par l'URL.
            connection.addRequestProperty("x-api-key", OPEN_WEATHER_MAP_API);
            System.out.println("----------url prevision horaire :"+connection.getURL());
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            StringBuffer json = new StringBuffer(1024);

            String tmp = "";
            while ((tmp=reader.readLine()) != null){
                json.append(tmp).append("\n");
            }

            reader.close();

            JSONObject data = new JSONObject(json.toString());

            if (data.getInt("cod") != 200){

                System.out.println("----------echec---------");
                return null;
            }
            System.out.println("----------okkkkkkkk---------");
            return data;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  null;
    }
}
