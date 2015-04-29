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

    public void saveThrow(Throw newThrow){
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.THROW_DISTANCE, newThrow.getDistance());
        values.put(MySQLiteHelper.THROW_TYPE, newThrow.getType());
        values.put(MySQLiteHelper.THROW_COURSE, newThrow.getCourse());
        values.put(MySQLiteHelper.THROW_DATE, newThrow.getDate());

        long insertId = database.insert(MySQLiteHelper.TABLE_THROWS, null, values);
    }

    public void saveThrow(long distance, String type, String course, String date){
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.THROW_DISTANCE, distance);
        values.put(MySQLiteHelper.THROW_TYPE, type);
        values.put(MySQLiteHelper.THROW_COURSE, course);
        values.put(MySQLiteHelper.THROW_DATE, date);

        long insertId = database.insert(MySQLiteHelper.TABLE_THROWS, null, values);
    }

    // TODO: make appropriate database queries to get putt list and drive list
    public List<Throw> getThrowList(String whereParameter){
        List<Throw> throwList = new ArrayList<Throw>();

        //TODO: change this query to complete above "TODO"
        //This query will get all the throws: the third parameter establishes the where clause
        //Ex "type=putt" / MySQLiteHelper.THROW_TYPE + "=" + desiredType (if desiredType is a parameter saying putt or drive)
        //Or build a string and enter the built string into the third query parameter

        // Executes a query that either gets all putts or all drives depending on the parameter
        // Then sorts the throws by course and distance
        Cursor cursor = database.query(MySQLiteHelper.TABLE_THROWS,
                null, MySQLiteHelper.THROW_TYPE + "=" + "'" + whereParameter + "'", null, null, null, "course, distance");

        //MySQLiteHelper.THROW_TYPE + "=" + whereParameter

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            // Skips getting the ID
            long distance = cursor.getLong(1);
            String type = cursor.getString(2);
            String course = cursor.getString(3);
            String date = cursor.getString(4);

            Throw myThrow = new Throw(distance, type, course, date);
            throwList.add(myThrow);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return throwList;
    }
}
