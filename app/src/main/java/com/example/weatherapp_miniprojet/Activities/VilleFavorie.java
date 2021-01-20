package com.example.weatherapp_miniprojet.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
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
        villeAdapter = new VilleAdapter(villeDAO.villeList(), this, R.layout.listville_custom);
        view.setAdapter(villeAdapter);
        view.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        view.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                // Capture total checked items
                final int checkedCount = view.getCheckedItemCount();
                // Set the CAB title according to total checked items
                if(checkedCount == 1){
                    mode.setTitle(checkedCount + " élément selectionné");
                }
                else {
                    mode.setTitle(checkedCount + " éléments selectionnés");
                }
                // Calls toggleSelection method from ListViewAdapter Class
                villeAdapter.toggleSelection(position);
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.ville_menu, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.delete:
                        // Calls getSelectedIds method from ListViewAdapter Class
                        SparseBooleanArray selected = villeAdapter
                                .getSelectedIds();
                        // Captures all selected ids with a loop
                        for (int i = (selected.size() - 1); i >= 0; i--) {
                            if (selected.valueAt(i)) {
                                Ville selecteditem = villeAdapter
                                        .getItem(selected.keyAt(i));
                                // Remove selected items following the ids
                                villeDAO.deleteVille(selecteditem);
                            }
                        }
                        // Close CAB
                        mode.finish();
                        refresh();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                villeAdapter.removeSelection();
            }
        });
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
                    refresh();

                    Toast.makeText(VilleFavorie.this, "Bien Ajouter", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.getStackTrace();
                }
            }
        });

        alertDialogBuilder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void refresh() {
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }
}