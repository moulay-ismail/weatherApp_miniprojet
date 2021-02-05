package com.example.weatherapp_miniprojet.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.weatherapp_miniprojet.Entities.Prevision;
import com.example.weatherapp_miniprojet.Entities.PrevisionHoraire;
import com.example.weatherapp_miniprojet.R;
import com.example.weatherapp_miniprojet.connectAPI.ForecastHandleJSON;
import com.example.weatherapp_miniprojet.connectAPI.ForecastHourlyHandleJSON;
import com.example.weatherapp_miniprojet.connectAPI.HandleJSON;
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
    public String unit = "metric";

    TextView cityField, detailsFields, currentTemperatureField, humidityField, pressureField, ventField, visibiliteField, updatedField, pre_meteo, pre_meteo1, pre_meteo2, pre_meteo3, pre_meteo4, pre_meteo5, temp_max, temp_max1, temp_max2, temp_max3, temp_max4, temp_max5, temp_min, temp_min1, temp_min2, temp_min3, temp_min4, temp_min5, pre_hor, pre_hor1, pre_hor2, pre_hor3, pre_hor4, pre_hor5, pre_hor6, temp_hor, temp_hor1, temp_hor2, temp_hor3, temp_hor4, temp_hor5, temp_hor6;
    ImageView weatherIcon, img_meteo, img_meteo1, img_meteo2, img_meteo3, img_meteo4, img_meteo5, img_hor, img_hor1, img_hor2, img_hor3, img_hor4, img_hor5, img_hor6;
    Button btnVille;
    ImageButton btn_back_listFav;
    static String latitude;
    static String longitude;

    RelativeLayout previsionHoriare, previsionDay, headerLayout, errorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Get from Layout...
        btnVille = findViewById(R.id.btn_ville);
        btn_back_listFav = findViewById(R.id.btn_back_listFav);
        previsionHoriare = findViewById(R.id.previsionHoriare);
        previsionDay = findViewById(R.id.previsionDays);
        errorLayout = findViewById(R.id.errorLayout);
        headerLayout = findViewById(R.id.header);
        cityField = findViewById(R.id.city_field);
        currentTemperatureField = findViewById(R.id.currnet_temperature_field);
        updatedField = findViewById(R.id.updated_field);
        detailsFields = findViewById(R.id.details_field);
        humidityField = findViewById(R.id.humidity_field);
        pressureField = findViewById(R.id.pressure_field);
        ventField = findViewById(R.id.vent_field);
        visibiliteField = findViewById(R.id.visibilite_field);
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
        pre_hor = findViewById(R.id.preHor);
        pre_hor1 = findViewById(R.id.preHor1);
        pre_hor2 = findViewById(R.id.preHor2);
        pre_hor3 = findViewById(R.id.preHor3);
        pre_hor4 = findViewById(R.id.preHor4);
        pre_hor5 = findViewById(R.id.preHor5);
        pre_hor6 = findViewById(R.id.preHor6);
        img_hor = findViewById(R.id.imgHor);
        img_hor1 = findViewById(R.id.imgHor1);
        img_hor2 = findViewById(R.id.imgHor2);
        img_hor3 = findViewById(R.id.imgHor3);
        img_hor4 = findViewById(R.id.imgHor4);
        img_hor5 = findViewById(R.id.imgHor5);
        img_hor6 = findViewById(R.id.imgHor6);
        temp_hor = findViewById(R.id.tempHor);
        temp_hor1 = findViewById(R.id.tempHor1);
        temp_hor2 = findViewById(R.id.tempHor2);
        temp_hor3 = findViewById(R.id.tempHor3);
        temp_hor4 = findViewById(R.id.tempHor4);
        temp_hor5 = findViewById(R.id.tempHor5);
        temp_hor6 = findViewById(R.id.tempHor6);

        btnVille.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent villeIntent = new Intent(getApplicationContext(), VilleFavorie.class);
                startActivity(villeIntent);
            }
        });
        btn_back_listFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                //overridePendingTransition(0, 0);
            }
        });
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // setFlags() : Définissez les indicateurs de la fenêtre.
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getSupportActionBar().hide();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        requestPermissions();

        //Value Gotten From VilleFavouie Activity
        Intent intent = getIntent();
        String unitee = intent.getStringExtra("unite");
        if (unitee != null) {
            unit = unitee;
            Toast.makeText(this, "Unité : " + unitee, Toast.LENGTH_LONG).show();
        }
        String getVille = intent.getStringExtra("ville");
        if (getVille != null) {
            previsionDay.setVisibility(View.VISIBLE);
            previsionHoriare.setVisibility(View.VISIBLE);
            headerLayout.setVisibility(View.VISIBLE);
            btnVille.setVisibility(View.GONE);
            btn_back_listFav.setVisibility(View.VISIBLE);
            cityField.setText(getVille);
            try {
                ArrayList<String> infos = findByVille(getVille);
                if (!infos.equals(null)) {
                    longitude = infos.get(0);
                    latitude = infos.get(1);
                    results(latitude, longitude, unit);
                    infos.clear();
                }
            } catch (JSONException exception) {
                exception.printStackTrace();
            }
        } else {
            boolean connected = checkInternet();
            if (connected) {
                previsionDay.setVisibility(View.VISIBLE);
                previsionHoriare.setVisibility(View.VISIBLE);
                headerLayout.setVisibility(View.VISIBLE);
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
                        results(latitude, longitude, unit);
                    } else {
                        Toast.makeText(this, "Desolé(e), vous devez changer votre position pour vous detecter automatiquement",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                errorLayout.setVisibility(View.VISIBLE);
                Toast.makeText(this, "Vous devez avoir une connection internet pour acceder à la météo.",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void results(String latitude, String longitude, String unit) {
        String[] jsonData = HandleJSON.getJSONResponse(latitude, longitude, unit);
        cityField.setText(jsonData[0]);
        detailsFields.setText(jsonData[1]);
        currentTemperatureField.setText(jsonData[2]);
        humidityField.setText("Humidité : " + jsonData[3]);
        pressureField.setText("Pression : " + jsonData[4]);
        ventField.setText("Visibilité : " + jsonData[8]);
        visibiliteField.setText("Vent: " + jsonData[9]);
        updatedField.setText(jsonData[5]);
        iconeUrl = IMG_URL + jsonData[6] + ".png";
        System.out.println("---------------------------" + iconeUrl);
        Picasso.get().load(iconeUrl).resize(250, 250).into(weatherIcon);

        String uniteChal = " °C";
        if (unit.equals("imperial")) {
            uniteChal = " °F";
        }

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
        temp_max.setText(jsonDataForecast.get(0).getTempMax() + uniteChal);
        temp_min.setText(jsonDataForecast.get(0).getTempMin() + uniteChal);
        temp_max1.setText(jsonDataForecast.get(1).getTempMax() + uniteChal);
        temp_min1.setText(jsonDataForecast.get(1).getTempMin() + uniteChal);
        temp_max2.setText(jsonDataForecast.get(2).getTempMax() + uniteChal);
        temp_min2.setText(jsonDataForecast.get(2).getTempMin() + uniteChal);
        temp_max3.setText(jsonDataForecast.get(3).getTempMax() + uniteChal);
        temp_min3.setText(jsonDataForecast.get(3).getTempMin() + uniteChal);
        temp_max4.setText(jsonDataForecast.get(4).getTempMax() + uniteChal);
        temp_min4.setText(jsonDataForecast.get(4).getTempMin() + uniteChal);
        temp_max5.setText(jsonDataForecast.get(5).getTempMax() + uniteChal);
        temp_min5.setText(jsonDataForecast.get(5).getTempMin() + uniteChal);

        ArrayList<PrevisionHoraire> jsonDataForecastHourly = ForecastHourlyHandleJSON.getJSONResponse(latitude, longitude);
        pre_hor.setText(jsonDataForecastHourly.get(0).getHeure());
        Picasso.get().load(IMG_URL + jsonDataForecastHourly.get(0).getIcon() + ".png").resize(100, 100).into(img_hor);
        temp_hor.setText(jsonDataForecastHourly.get(0).getTemperature() + uniteChal);
        pre_hor1.setText(jsonDataForecastHourly.get(1).getHeure());
        Picasso.get().load(IMG_URL + jsonDataForecastHourly.get(1).getIcon() + ".png").resize(100, 100).into(img_hor1);
        temp_hor1.setText(jsonDataForecastHourly.get(1).getTemperature() + uniteChal);
        pre_hor2.setText(jsonDataForecastHourly.get(2).getHeure());
        Picasso.get().load(IMG_URL + jsonDataForecastHourly.get(1).getIcon() + ".png").resize(100, 100).into(img_hor2);
        temp_hor2.setText(jsonDataForecastHourly.get(2).getTemperature() + uniteChal);
        pre_hor3.setText(jsonDataForecastHourly.get(3).getHeure());
        Picasso.get().load(IMG_URL + jsonDataForecastHourly.get(1).getIcon() + ".png").resize(100, 100).into(img_hor3);
        temp_hor3.setText(jsonDataForecastHourly.get(3).getTemperature() + uniteChal);
        pre_hor4.setText(jsonDataForecastHourly.get(4).getHeure());
        Picasso.get().load(IMG_URL + jsonDataForecastHourly.get(1).getIcon() + ".png").resize(100, 100).into(img_hor4);
        temp_hor4.setText(jsonDataForecastHourly.get(4).getTemperature() + uniteChal);
        pre_hor5.setText(jsonDataForecastHourly.get(5).getHeure());
        Picasso.get().load(IMG_URL + jsonDataForecastHourly.get(5).getIcon() + ".png").resize(100, 100).into(img_hor5);
        temp_hor5.setText(jsonDataForecastHourly.get(5).getTemperature() + uniteChal);
        pre_hor6.setText(jsonDataForecastHourly.get(6).getHeure());
        Picasso.get().load(IMG_URL + jsonDataForecastHourly.get(6).getIcon() + ".png").resize(100, 100).into(img_hor6);
        temp_hor6.setText(jsonDataForecastHourly.get(6).getTemperature() + uniteChal);
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
                finish();
                Intent updateUnitIntent = new Intent(getApplicationContext(), UpdateUniteActivity.class);
                updateUnitIntent.putExtra("unit", unit);
                startActivity(updateUnitIntent);
                //Toast.makeText(this, "changer unité", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.apropos:
                Intent intent = new Intent(getApplicationContext(),aboutUsActivity.class);
                startActivity(intent);
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
            if (e.getString("name").equals(ville)) {
                JSONObject coordObj = new JSONObject(e.getString("coord"));
                listdata.add(String.valueOf(coordObj.getDouble("lon")));
                listdata.add(String.valueOf(coordObj.getDouble("lat")));
            }
        }
        return listdata;
    }

    private boolean checkInternet() {
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            // connected to the internet
            /*if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
            }*/
            return true;
        } else {
            // not connected to the internet
            return false;
        }
    }
}