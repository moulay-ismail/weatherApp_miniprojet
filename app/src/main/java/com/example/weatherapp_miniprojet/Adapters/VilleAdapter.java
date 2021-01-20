package com.example.weatherapp_miniprojet.Adapters;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.weatherapp_miniprojet.Entities.Ville;
import com.example.weatherapp_miniprojet.R;

import java.util.ArrayList;
import java.util.List;

public class VilleAdapter extends ArrayAdapter<Ville> {

    private ArrayList<Ville> villes;
    private LayoutInflater layoutInflater;
    private Context context;
    private SparseBooleanArray mSelectedItemsIds;

    public VilleAdapter(ArrayList<Ville> villes, Context context, int resourceId) {
        super(context,resourceId,villes);
        this.villes = villes;
        mSelectedItemsIds = new SparseBooleanArray();
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }

    public static class VilleView {
        TextView villeName;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        VilleView villeView = null;
        if (convertView == null) {
            layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.listville_custom, null);
            villeView = new VilleView();
            villeView.villeName = convertView.findViewById(R.id.nomVille_txt);
            convertView.setTag(villeView);
        } else {
            villeView = (VilleView) convertView.getTag();
        }
        Ville ville = villes.get(position);
        villeView.villeName.setText(ville.getNomVille());
        return convertView;
    }
    @Override
    public void remove(Ville object) {
        villes.remove(object);
        notifyDataSetChanged();
    }

    public List<Ville> getWorldPopulation() {
        return villes;
    }

    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));
    }

    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    public void selectView(int position, boolean value) {
        if (value)
            mSelectedItemsIds.put(position, value);
        else
            mSelectedItemsIds.delete(position);
        notifyDataSetChanged();
    }

    public int getSelectedCount() {
        return mSelectedItemsIds.size();
    }

    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }
}