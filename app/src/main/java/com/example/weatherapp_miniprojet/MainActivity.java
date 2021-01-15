package com.example.weatherapp_miniprojet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Html;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
public class MainActivity extends AppCompatActivity {

    private static final String OPEN_WEATHER_MAP_URL = "http://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&units=metric";
    private static final String OPEN_WEATHER_MAP_API = "88f399bec2c33cb721b859e49d93b9dd";

    TextView cityField, detailsFields, currentTemperatureField, humidityField, pressureField, weatherIcon, updatedField;
    Typeface weatherFont;
    static String latitude;
    static String longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // setFlags() : Définissez les indicateurs de la fenêtre.
        getSupportActionBar().hide();

        StrictMode.ThreadPolicy policy= new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        requestPermissions();

        FusedLocationProviderClient mFusedLocationProviderClient;
        // FusedLocationProviderClient : Le principal point d'entrée pour interagir avec le fournisseur d'emplacement fusionné
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if(ActivityCompat.checkSelfPermission(MainActivity.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            // PackageManager.PERMISSION_GRANTED : Résultat du contrôle des autorisations: il est renvoyé par checkPermission si l'autorisation a été accordée au package donné
            return;
        }
        mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(MainActivity.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null){
                    latitude = String.valueOf(location.getLatitude());
                    longitude = String.valueOf(location.getLongitude());

                    //erikflowers / weather-icons --- GitHub
                    weatherFont = Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/weathericons-regular-webfont.ttf");

                    cityField = findViewById(R.id.city_field);
                    currentTemperatureField = findViewById(R.id.currnet_temperature_field);
                    updatedField = findViewById(R.id.updated_field);
                    detailsFields = findViewById(R.id.details_field);
                    humidityField = findViewById(R.id.humidity_field);
                    pressureField = findViewById(R.id.pressure_field);
                    weatherIcon = findViewById(R.id.weather_icon);

                    String[] jsonData = getJSONResponse();
                    cityField.setText(jsonData[0]);
                    detailsFields.setText(jsonData[1]);
                    currentTemperatureField.setText(jsonData[2]);
                    humidityField.setText("Humidité : "+jsonData[3]);
                    pressureField.setText("Pression : "+jsonData[4]);
                    updatedField.setText(jsonData[5]);
                    weatherIcon.setText(Html.fromHtml(jsonData[6]));

                    System.out.println("-------------"+jsonData[0]);
                    System.out.println("-------------"+jsonData[1]);
                    System.out.println("-------------"+jsonData[2]);
                    System.out.println("-------------"+"Humidity : "+jsonData[3]);
                    System.out.println("-------------"+"Pressure : "+jsonData[4]);
                    System.out.println("-------------"+jsonData[5]);
                    System.out.println("-------------"+jsonData[6]);

                }
            }
        });
    }

    public String[] getJSONResponse(){
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
                String iconeText = setWeatherIcon(details.getInt("id"),jsonWeather.getJSONObject("sys").getLong("sunrise")*1000, jsonWeather.getJSONObject("sys").getLong("sunset")*1000);

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

    //demande des autorisations
    private void requestPermissions(){
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, 1);
    }
}