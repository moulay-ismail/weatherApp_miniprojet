package com.example.weatherapp_miniprojet;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class VilleFavorie extends AppCompatActivity {
    TextView txtNameVille;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ville_favorie);

        txtNameVille = findViewById(R.id.txt_name_ville);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // setFlags() : Définissez les indicateurs de la fenêtre.
        getSupportActionBar().hide();
        //cacher la bare des taches
    }

    public void addVille(View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("nom ville : ");
        final EditText nomVilleTxt = new EditText(VilleFavorie.this);
        nomVilleTxt.setInputType(InputType.TYPE_CLASS_TEXT);
        alertDialogBuilder.setView(nomVilleTxt);
        alertDialogBuilder.setPositiveButton("ajouter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String txtVille = nomVilleTxt.getText().toString();
                Toast.makeText(VilleFavorie.this, "nom ville : "+txtVille,Toast.LENGTH_LONG).show();
                txtNameVille.setText("ville : "+txtVille);
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