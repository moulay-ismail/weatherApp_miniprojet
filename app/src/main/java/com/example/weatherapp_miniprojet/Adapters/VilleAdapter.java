package com.example.weatherapp_miniprojet.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.weatherapp_miniprojet.Entities.Ville;
import com.example.weatherapp_miniprojet.R;

import java.util.ArrayList;

public class VilleAdapter extends BaseAdapter {

    private ArrayList<Ville> villes;
    private LayoutInflater layoutInflater;
    private Context context;

    public VilleAdapter(ArrayList<Ville> villes, Context context) {
        this.villes = villes;
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public int getCount() {
        return this.villes.size();
    }

    @Override
    public Object getItem(int position) {
        return this.villes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
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
}