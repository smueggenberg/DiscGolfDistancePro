package edu.css.smueggenberg.discgolfdistancepro;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by smueggenberg on 4/22/2015.
 * Allows the app to communicate with the database
 */
public class ThrowsDAO {
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;

    public ThrowsDAO(Context context){
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException{
        database = dbHelper.getWritableDatabase();
    }

    /**
     * Saves a throw to the database
     * @param newThrow An object of the Throw class whose attributes will be saved to the database
     */
    public void saveThrow(Throw newThrow){
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.THROW_DISTANCE, newThrow.getDistance());
        values.put(MySQLiteHelper.THROW_TYPE, newThrow.getType());
        values.put(MySQLiteHelper.THROW_COURSE, newThrow.getCourse());
        values.put(MySQLiteHelper.THROW_DATE, newThrow.getDate());

        long insertId = database.insert(MySQLiteHelper.TABLE_THROWS, null, values);
    }

    /**
     * Saves a throw to the databse as individual attributes
     * @param distance The throw's distance
     * @param type The type of throw, either putt or drive
     * @param course The location or course where the throw was made
     * @param date The date of the throw
     */
    public void saveThrow(long distance, String type, String course, String date){
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.THROW_DISTANCE, distance);
        values.put(MySQLiteHelper.THROW_TYPE, type);
        values.put(MySQLiteHelper.THROW_COURSE, course);
        values.put(MySQLiteHelper.THROW_DATE, date);

        long insertId = database.insert(MySQLiteHelper.TABLE_THROWS, null, values);
    }

    /**
     * Deletes a throw from the database
     * @param selectedThrow The throw to be deleted
     */
    public void deleteThrow(Throw selectedThrow){
        int id = selectedThrow.getId();
        database.delete(MySQLiteHelper.TABLE_THROWS, MySQLiteHelper.THROW_ID + "=" + id, null);
    }

    /**
     * Queries the database to retrieve a list of throws
     * @param whereParameter Identifies the type of throw to be accessed: either "putt" or "drive"
     * @return The list of throws retrieved in the query
     */
    public List<Throw> getThrowList(String whereParameter){
        List<Throw> throwList = new ArrayList<Throw>();

        // Executes a query that either gets all putts or all drives depending on the parameter
        // Then sorts the throws by course and distance
        Cursor cursor = database.query(MySQLiteHelper.TABLE_THROWS,
                null, MySQLiteHelper.THROW_TYPE + "=" + "'" + whereParameter + "'", null, null, null, "course, distance");

        // Goes through each result in the query and adds it to the List of throws
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(0);
            long distance = cursor.getLong(1);
            String type = cursor.getString(2);
            String course = cursor.getString(3);
            String date = cursor.getString(4);

            Throw myThrow = new Throw(id, distance, type, course, date);
            throwList.add(myThrow);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();

        return throwList;
    }
}
