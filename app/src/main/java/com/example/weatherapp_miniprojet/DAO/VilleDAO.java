package com.example.weatherapp_miniprojet.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.weatherapp_miniprojet.Entities.Ville;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class VilleDAO extends SQLiteOpenHelper {

    private final static String DB_NAME = "DB_Weather";
    private final static int DB_VERSION = 1;

    private final static String TABLE_NAME = "Ville";
    private final static String IdVille = "id_ville";
    private final static String NomVille = "nom_ville";

    public VilleDAO(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Ville( " + " id_ville INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nom_ville TEXT" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Ville");
        this.onCreate(db);
    }

    public void ajouterVille(Ville ville) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NomVille, ville.getNomVille());
        database.insert(TABLE_NAME, null, values);
        database.close();
    }

    public void deleteVille(Ville ville) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_NAME + " where nom_ville='" + ville.getNomVille() + "'");
        db.close();
    }

    public ArrayList<Ville> villeList() {
        ArrayList<Ville> villes = new ArrayList<>();
        String req = "select * from " + TABLE_NAME;
        SQLiteDatabase liteDatabase = this.getReadableDatabase();
        Cursor cursor = liteDatabase.rawQuery(req, null);
        Ville ville;
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                ville = new Ville();
                ville.setNomVille(cursor.getString(1));
                villes.add(ville);
                cursor.moveToNext();
            }
        }
        return villes;
    }
}