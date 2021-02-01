package com.example.weatherapp_miniprojet.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.weatherapp_miniprojet.R;

public class UpdateUniteActivity extends AppCompatActivity {
    RadioButton metricRadio, imperialRadio;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_unite);

        metricRadio = findViewById(R.id.metric_radio);
        imperialRadio = findViewById(R.id.imperial_radio);

        Intent inten = getIntent();
        String unit = inten.getStringExtra("unit");
        Toast.makeText(this, "unite : "+unit, Toast.LENGTH_SHORT).show();
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
        finish();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("unite", unitee);
        startActivity(intent);
    }
}