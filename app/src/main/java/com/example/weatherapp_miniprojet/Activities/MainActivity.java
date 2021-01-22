package com.example.weatherapp_miniprojet.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weatherapp_miniprojet.ConnectAPI.ForecastHandleJSON;
import com.example.weatherapp_miniprojet.Entities.Prevision;
import com.example.weatherapp_miniprojet.R;
import com.example.weatherapp_miniprojet.ConnectAPI.HandleJSON;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    public static final String IMG_URL = "https://openweathermap.org/img/w/";
    public static String iconeUrl;
    HandleJSON handleJSON;

    TextView cityField, detailsFields, currentTemperatureField, humidityField, pressureField, updatedField,
            pre_meteo, pre_meteo1, pre_meteo2, pre_meteo3, pre_meteo4, pre_meteo5,
            temp_max, temp_max1, temp_max2, temp_max3, temp_max4, temp_max5, temp_min, temp_min1,
            temp_min2, temp_min3, temp_min4, temp_min5;
    ImageView weatherIcon, img_meteo, img_meteo1,
            img_meteo2, img_meteo3,
            img_meteo4, img_meteo5;
    Button btnVille;
    static String latitude;
    static String longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Get from Layout...
        btnVille = findViewById(R.id.btn_ville);
        cityField = findViewById(R.id.city_field);
        currentTemperatureField = findViewById(R.id.currnet_temperature_field);
        updatedField = findViewById(R.id.updated_field);
        detailsFields = findViewById(R.id.details_field);
        humidityField = findViewById(R.id.humidity_field);
        pressureField = findViewById(R.id.pressure_field);
        weatherIcon = findViewById(R.id.weather_icon);
        img_meteo = findViewById(R.id.imgMeteo);
        img_meteo1 = findViewById(R.id.imgMeteo1);
        img_meteo2 = findViewById(R.id.imgMeteo2);
        img_meteo3 = findViewById(R.id.imgMeteo3);
        img_meteo4 = findViewById(R.id.imgMeteo4);
        img_meteo5 = findViewById(R.id.imgMeteo5);
        pre_meteo = findViewById(R.id.preMeteo);
        pre_meteo1 = findViewById(R.id.preMeteo1);
        pre_meteo2 = findViewById(R.id.preMeteo2);
        pre_meteo3 = findViewById(R.id.preMeteo3);
        pre_meteo4 = findViewById(R.id.preMeteo4);
        pre_meteo5 = findViewById(R.id.preMeteo5);
        temp_max = findViewById(R.id.tempMax);
        temp_max1 = findViewById(R.id.tempMax1);
        temp_max2 = findViewById(R.id.tempMax2);
        temp_max3 = findViewById(R.id.tempMax3);
        temp_max4 = findViewById(R.id.tempMax4);
        temp_max5 = findViewById(R.id.tempMax5);
        temp_min = findViewById(R.id.tempMin);
        temp_min1 = findViewById(R.id.tempMin1);
        temp_min2 = findViewById(R.id.tempMin2);
        temp_min3 = findViewById(R.id.tempMin3);
        temp_min4 = findViewById(R.id.tempMin4);
        temp_min5 = findViewById(R.id.tempMin5);

        btnVille.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent villeIntent = new Intent(getApplicationContext(), VilleFavorie.class);
                startActivity(villeIntent);
            }
        });
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // setFlags() : Définissez les indicateurs de la fenêtre.
        getSupportActionBar().hide();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        requestPermissions();

        //Value Gotten From VilleFavouie Activity
        Intent intent = getIntent();
        String getVille = intent.getStringExtra("ville");
        if (getVille != null) {
            cityField.setText(getVille);
            try {
                ArrayList<String> infos = findByVille(getVille);
                if (!infos.equals(null)) {
                    longitude = infos.get(0);
                    latitude = infos.get(1);
                    results(latitude, longitude);
                    infos.clear();
                }
            } catch (JSONException exception) {
                exception.printStackTrace();
            }
        }
        else {
            FusedLocationProviderClient mFusedLocationProviderClient;
            // FusedLocationProviderClient : Le principal point d'entrée pour interagir avec le fournisseur d'emplacement fusionné
            mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            if (ActivityCompat.checkSelfPermission(MainActivity.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // PackageManager.PERMISSION_GRANTED : Résultat du contrôle des autorisations: il est renvoyé par checkPermission si l'autorisation a été accordée au package donné
                return;
            }
            mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(MainActivity.this, location -> {
                if (location != null) {
                    latitude = String.valueOf(location.getLatitude());
                    longitude = String.valueOf(location.getLongitude());
                }
                results(latitude, longitude);
            });
        }
    }

    private void results(String latitude, String longitude) {
        String[] jsonData = HandleJSON.getJSONResponse(latitude, longitude);
        cityField.setText(jsonData[0]);
        detailsFields.setText(jsonData[1]);
        currentTemperatureField.setText(jsonData[2]);
        humidityField.setText("Humidité : " + jsonData[3]);
        pressureField.setText("Pression : " + jsonData[4]);
        updatedField.setText(jsonData[5]);
        iconeUrl = IMG_URL + jsonData[6] + ".png";
        System.out.println("---------------------------" + iconeUrl);
        Picasso.get().load(iconeUrl).resize(250, 250).into(weatherIcon);
        ArrayList<Prevision> jsonDataForecast = ForecastHandleJSON.getJSONResponse(latitude, longitude);
        Picasso.get().load(IMG_URL + jsonDataForecast.get(0).getIcon() + ".png").resize(100, 100).into(img_meteo);
        pre_meteo.setText(jsonDataForecast.get(0).getJour());
        Picasso.get().load(IMG_URL + jsonDataForecast.get(1).getIcon() + ".png").resize(100, 100).into(img_meteo1);
        pre_meteo1.setText(jsonDataForecast.get(1).getJour());
        Picasso.get().load(IMG_URL + jsonDataForecast.get(2).getIcon() + ".png").resize(100, 100).into(img_meteo2);
        pre_meteo2.setText(jsonDataForecast.get(2).getJour());
        Picasso.get().load(IMG_URL + jsonDataForecast.get(3).getIcon() + ".png").resize(100, 100).into(img_meteo3);
        pre_meteo3.setText(jsonDataForecast.get(3).getJour());
        Picasso.get().load(IMG_URL + jsonDataForecast.get(4).getIcon() + ".png").resize(100, 100).into(img_meteo4);
        pre_meteo4.setText(jsonDataForecast.get(4).getJour());
        Picasso.get().load(IMG_URL + jsonDataForecast.get(5).getIcon() + ".png").resize(100, 100).into(img_meteo5);
        pre_meteo5.setText(jsonDataForecast.get(5).getJour());
        temp_max.setText(jsonDataForecast.get(0).getTempMax() + "°");
        temp_min.setText(jsonDataForecast.get(0).getTempMin() + "°");
        temp_max1.setText(jsonDataForecast.get(1).getTempMax() + "°");
        temp_min1.setText(jsonDataForecast.get(1).getTempMin() + "°");
        temp_max2.setText(jsonDataForecast.get(2).getTempMax() + "°");
        temp_min2.setText(jsonDataForecast.get(2).getTempMin() + "°");
        temp_max3.setText(jsonDataForecast.get(3).getTempMax() + "°");
        temp_min3.setText(jsonDataForecast.get(3).getTempMin() + "°");
        temp_max4.setText(jsonDataForecast.get(4).getTempMax() + "°");
        temp_min4.setText(jsonDataForecast.get(4).getTempMin() + "°");
        temp_max5.setText(jsonDataForecast.get(5).getTempMax() + "°");
        temp_min5.setText(jsonDataForecast.get(5).getTempMin() + "°");
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, 1);
    }

    public void showMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.menu);
        popupMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.temperaure_setting:
                Toast.makeText(this, "changer unite de temperature", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.vent_setting:
                Toast.makeText(this, "changer unite de vent", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.apropos:
                Toast.makeText(this, "layout apropos", Toast.LENGTH_SHORT).show();
                return true;
        }
        return false;
    }

    private ArrayList<String> findByVille(String ville) throws JSONException {
        handleJSON = new HandleJSON(MainActivity.this);
        ArrayList<String> listdata = new ArrayList<String>();
        JSONArray json = new JSONArray(handleJSON.loadJSONFromAsset());
        for (int i = 0; i < json.length(); i++) {
            final JSONObject e = json.getJSONObject(i);
            if (e.getString("country").equals("MA")) {
                if (e.getString("name").equals(ville)) {
                    JSONObject coordObj = new JSONObject(e.getString("coord"));
                    listdata.add(String.valueOf(coordObj.getDouble("lon")));
                    listdata.add(String.valueOf(coordObj.getDouble("lat")));
                }
            }
        }
        return listdata;
    }
}