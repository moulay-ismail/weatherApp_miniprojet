package com.example.weatherapp_miniprojet.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weatherapp_miniprojet.Adapters.VilleAdapter;
import com.example.weatherapp_miniprojet.DAO.VilleDAO;
import com.example.weatherapp_miniprojet.Entities.Ville;
import com.example.weatherapp_miniprojet.R;

import java.util.ArrayList;

public class VilleFavorie extends AppCompatActivity {
    private VilleAdapter villeAdapter;
    VilleDAO villeDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ville_favorie);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        villeDAO = new VilleDAO(this);
        ListView view = findViewById(R.id.listvilles_id);
        villeAdapter = new VilleAdapter(villeDAO.villeList(), this);
        view.setAdapter(villeAdapter);
    }

    public void addVille(View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Nom Ville : ");
        final EditText nomVilleTxt = new EditText(VilleFavorie.this);
        nomVilleTxt.setInputType(InputType.TYPE_CLASS_TEXT);
        alertDialogBuilder.setView(nomVilleTxt);
        alertDialogBuilder.setPositiveButton("Ajouter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    Ville ville = new Ville(nomVilleTxt.getText().toString());
                    villeDAO.ajouterVille(ville);
                    finish();
                    Intent i1 = new Intent(getApplicationContext(), VilleFavorie.class);
                    startActivity(i1);
                    //villeDAO.villeList();
                    Toast.makeText(VilleFavorie.this, "Bien Ajouter", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.getStackTrace();
                }
            }
        });

        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}