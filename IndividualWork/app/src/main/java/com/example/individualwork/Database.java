package com.example.individualwork;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import entity.Detail;
import entity.Trip;


public class Database extends SQLiteOpenHelper
{
    private static final String DATABASE_NAME = "TripInformation";
    private static final String Table_Trip = "Trips";
    private static final String Table_Detail = "Details";

    public static final String TRIP_ID = "trip_id";
    public static final String TRIP_NAME = "trip_name";
    public static final String DATE = "date";
    public static final String DESCRIPTION = "description";
    public static final String PIC = "picture";
    public static final String DESTINATION = "destination";
    public static final String RISK = "risk";

    public static final String DETAIL_ID = "detail_id";
    public static final String DETAIL_NAME = "detail_name";
    public static final String DETAIL_COST = "detail_cost";
    private SQLiteDatabase database;

    private static final String TABLE_TRIP_CREATE = String.format(
            "CREATE TABLE %s (" +
                    "   %s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "   %s TEXT, " +
                    "   %s TEXT, " +
                    "   %s TEXT, " +
                    "   %s TEXT, " +
                    "   %s TEXT, " +
                    "   %s NUMERIC)",
            Table_Trip, TRIP_ID, TRIP_NAME, DATE, DESCRIPTION, PIC, DESTINATION, RISK);

    private static final String TABLE_DETAIL_CREATE = String.format(
            "CREATE TABLE %s (" +
                    "   %s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "   %s INTEGER, " +
                    "   %s TEXT, " +
                    "   %s TEXT)",
            Table_Detail, DETAIL_ID, TRIP_ID, DETAIL_NAME, DETAIL_COST);

    public Database(Context context){
        super(context, DATABASE_NAME, null, 1);
        database = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(TABLE_TRIP_CREATE);
        sqLiteDatabase.execSQL(TABLE_DETAIL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Table_Detail);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Table_Trip);

        Log.v(this.getClass().getName(), Table_Trip + " database upgrade to version " +
                i1 + " - old data lost");
        onCreate(sqLiteDatabase);
    }

    public long insertTrip(String name, String date, String description, String pic, String des, boolean risk){
        ContentValues row = new ContentValues();

        row.put(TRIP_NAME, name);
        row.put(DATE, date);
        row.put(DESCRIPTION, description);
        row.put(PIC, pic);
        row.put(DESTINATION, des);
        row.put(RISK, risk);

        return database.insertOrThrow(Table_Trip, null, row);
    }

    public long insertDetail(int trip_id, String detail_name, String detail_cost){
        ContentValues row = new ContentValues();

        row.put(TRIP_ID, trip_id);
        row.put(DETAIL_NAME, detail_name);
        row.put(DETAIL_COST, detail_cost);
        return database.insertOrThrow(Table_Detail, null, row);
    }

    public ArrayList<Trip> getTrip() {
        Cursor cursor = database.query(Table_Trip, new String[]{TRIP_ID,TRIP_NAME, DATE,DESCRIPTION,PIC,DESTINATION, RISK}
                ,null,null,null,null,"trip_name");

        ArrayList<Trip> results = new ArrayList<>();
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String date = cursor.getString(2);
            String description = cursor.getString(3);
            String destination = cursor.getString(5);
            String pic = cursor.getString(4);
            int risk = cursor.getInt(6);
            Trip trip = new Trip();

            trip.setId(id);
            trip.setName(name);
            trip.setDate(date);
            trip.setDescription(description);
            trip.setPic(pic);
            trip.setDestination(destination);
            trip.setRisk(risk == 0 ? false : true);
            results.add(trip);
            cursor.moveToNext();
        }

        return results;
    }

    public ArrayList<String> searchTrip(String name){
        Cursor cursor = database.query(Table_Trip, new String[] { TRIP_ID, TRIP_NAME, DESTINATION, DATE, DESCRIPTION, RISK,PIC },
                TRIP_NAME + " LIKE " + "'%" + name + "%'", null, null, null,  TRIP_ID);
        cursor.moveToFirst();
        ArrayList<String> results = new ArrayList<>();
        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(0);
            String name1 = cursor.getString(1);
            String destination = cursor.getString(2);
            String date = cursor.getString(3);
            String des = cursor.getString(4);
            int risk = cursor.getInt(5);
            String pic = cursor.getString(6);
            Trip trip = new Trip();
            trip.setId(id);
            trip.setName(name1);
            trip.setDestination(destination);
            trip.setDate(date);
            trip.setDescription(des);
            trip.setRisk(risk == 0 ? false : true);
            trip.setPic(pic);
            results.add(trip.toString());
            cursor.moveToNext();
        }
        return results;
    }

    public void DeleteTrip(int id){
        database.execSQL("DELETE FROM " + Table_Trip + " WHERE " + TRIP_ID + " = " + id);
    }

    public void DeleteDetail(int id){
        database.execSQL("DELETE FROM " + Table_Detail + " WHERE " + DETAIL_ID + " = " + id);
    }


    public void EditTrip(int id, String name, String date, String description, String pic, String des, boolean risk){
        ContentValues trip = new ContentValues();
        trip.put(TRIP_NAME, name);
        trip.put(DESTINATION, des);
        trip.put(DATE, date);
        trip.put(DESCRIPTION, description);
        trip.put(PIC, pic);
        trip.put(RISK, risk);

        database.update(Table_Trip, trip, TRIP_ID + " = " + id, null);
    }

    public ArrayList<Detail> getDetail(int Trip_Id){
        String query = "SELECT a.detail_id, a.detail_name, a.detail_cost, b.trip_id FROM " + Table_Detail
                + " a INNER JOIN " + Table_Trip + " b ON a.trip_id = b.trip_id WHERE b.trip_id =?";
        Cursor cursor = database.rawQuery(query, new String[]{String.valueOf(Trip_Id)});

        ArrayList<Detail> details = new ArrayList<>();
        ArrayList<String> results = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            int trip_id = cursor.getInt(3);
            int detail_id = cursor.getInt(0);
            String detail_name = cursor.getString(1);
            String detail_cost = cursor.getString(2);

            Detail detail = new Detail();
            detail.setTrip_Id(trip_id);
            detail.setCost_Id(detail_id);
            detail.setCost_name(detail_name);
            detail.setCost(detail_cost);

            details.add(detail);
            results.add(detail.toString());
            cursor.moveToNext();
        }
        return details;
    }
        public void ResetData(){
        database.execSQL("DELETE FROM " + Table_Trip);
        database.execSQL("VACUUM;");
        database.execSQL("DELETE FROM " + Table_Detail);
        database.execSQL("VACUUM;");
    }

}
