package com.example.weatherapp_miniprojet.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.text.InputType;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weatherapp_miniprojet.Adapters.VilleAdapter;
import com.example.weatherapp_miniprojet.DAO.VilleDAO;
import com.example.weatherapp_miniprojet.Entities.Ville;
import com.example.weatherapp_miniprojet.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class VilleFavorie extends AppCompatActivity {
    private VilleAdapter villeAdapter;
    VilleDAO villeDAO;
    ListView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ville_favorie);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        villeDAO = new VilleDAO(this);
        view = findViewById(R.id.listvilles_id);
        villeAdapter = new VilleAdapter(villeDAO.villeList(), this, R.layout.listville_custom);
        view.setAdapter(villeAdapter);
        view.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        view.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                // Capture total checked items
                final int checkedCount = view.getCheckedItemCount();
                // Set the CAB title according to total checked items
                if (checkedCount == 1) {
                    mode.setTitle(checkedCount + " élément selectionné");
                } else {
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

    public void addVille(View view) throws JSONException {
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        View customVille_view = inflater.inflate(R.layout.addville_custom_alert, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Nouvelle Ville");
        alertDialogBuilder.setView(customVille_view);
        final AutoCompleteTextView nomVilleTxt = customVille_view.findViewById(R.id.nomVilleTxt);
        //AutoComplete text
            ArrayList<String> list = fetch();
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.select_dialog_item, list);
            nomVilleTxt.setThreshold(1);
            nomVilleTxt.setAdapter(arrayAdapter);

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

    private String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getApplicationContext().getAssets().open("city.list.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    private ArrayList<String> fetch() throws JSONException {
        ArrayList<String> listdata = new ArrayList<String>();
        JSONArray json = new JSONArray(loadJSONFromAsset());
        for (int i = 0; i < json.length(); i++) {
            final JSONObject e = json.getJSONObject(i);
            if(e.getString("country").equals("MA")) {
                listdata.add(e.getString("name"));
            }
        }
        return listdata;
    }
}