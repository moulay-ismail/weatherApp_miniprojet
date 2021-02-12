package com.example.weatherapp_miniprojet.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.weatherapp_miniprojet.R;

public class UpdateUniteActivity extends AppCompatActivity {
    RadioButton metricRadio, imperialRadio;
    String lati;
    String longti;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_unite);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        // setFlags() : Définissez les indicateurs de la fenêtre.
        getSupportActionBar().hide();

        metricRadio = findViewById(R.id.metric_radio);
        imperialRadio = findViewById(R.id.imperial_radio);

        Intent inten = getIntent();
        String unit = inten.getStringExtra("unit");
        lati = inten.getStringExtra("lat");
        longti = inten.getStringExtra("long");
        //Toast.makeText(this, "latitude : "+lati+" longitud : "+longti, Toast.LENGTH_SHORT).show();
        if (unit.equals("metric")){
            metricRadio.setChecked(true);
            imperialRadio.setChecked(false);
        }else if(unit.equals("imperial")){
            metricRadio.setChecked(false);
            imperialRadio.setChecked(true);
        }
    }

    public void imperialRadioClicked(View view) {
        //Toast.makeText(this, "unite : imperial", Toast.LENGTH_SHORT).show();
        String unitee = "imperial";
        startActivityAcceuil(unitee);
    }

    public void metricRadioClicked(View view) {
        //Toast.makeText(this, "unite : metric", Toast.LENGTH_SHORT).show();
        String unitee = "metric";
        startActivityAcceuil(unitee);
    }

    void startActivityAcceuil(String unitee){
        System.out.println("---------latituude : "+lati+" mongitude : "+longti);
        finish();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("unite", unitee);

        intent.putExtra("latit", lati);
        intent.putExtra("longti", longti);
        startActivity(intent);
    }
}