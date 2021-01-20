package com.example.weatherapp_miniprojet.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
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

import com.example.weatherapp_miniprojet.R;
import com.example.weatherapp_miniprojet.ConnectAPI.HandleJSON;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.squareup.picasso.Picasso;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    public static final String IMG_URL = "https://openweathermap.org/img/w/";
    public static String iconeUrl;

    TextView cityField, detailsFields, currentTemperatureField, humidityField, pressureField, updatedField;
    ImageView weatherIcon;
    Typeface weatherFont;
    Button prev,btnVille;
    static String latitude;
    static String longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prev = findViewById(R.id.btn_prev);
        btnVille = findViewById(R.id.btn_ville);

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent prevIntent = new Intent(getApplicationContext(),PrevisionActivity.class);
                startActivity(prevIntent);
            }
        });
        btnVille.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent villeIntent = new Intent(getApplicationContext(),VilleFavorie.class);
                startActivity(villeIntent);
            }
        });
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
        mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(MainActivity.this, location -> {
            if (location != null) {
                latitude = String.valueOf(location.getLatitude());
                longitude = String.valueOf(location.getLongitude());
            }
                //erikflowers / weather-icons --- GitHub
                //weatherFont = Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/weathericons-regular-webfont.ttf");

                cityField = findViewById(R.id.city_field);
                currentTemperatureField = findViewById(R.id.currnet_temperature_field);
                updatedField = findViewById(R.id.updated_field);
                detailsFields = findViewById(R.id.details_field);
                humidityField = findViewById(R.id.humidity_field);
                pressureField = findViewById(R.id.pressure_field);
                weatherIcon = findViewById(R.id.weather_icon);

                String[] jsonData = HandleJSON.getJSONResponse(latitude, longitude);
                cityField.setText(jsonData[0]);
                detailsFields.setText(jsonData[1]);
                currentTemperatureField.setText(jsonData[2]);
                humidityField.setText("Humidité : "+jsonData[3]);
                pressureField.setText("Pression : "+jsonData[4]);
                updatedField.setText(jsonData[5]);
                iconeUrl = IMG_URL+jsonData[6]+".png";
                System.out.println("---------------------------"+iconeUrl);
                Picasso.get().load(iconeUrl).resize(250,250).into(weatherIcon);

        });
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.setting_temperature:
                Toast.makeText(this, "test temperature", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.setting_vent:
                Toast.makeText(this, "test vent", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.Apropos:
                Toast.makeText(this, "test apropos", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }*/

    //demande des autorisations
    private void requestPermissions(){
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, 1);
    }

    public void showMenu(View view) {

        PopupMenu popupMenu = new PopupMenu(this,view);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.menu);
        popupMenu.show();
    }
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
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
}