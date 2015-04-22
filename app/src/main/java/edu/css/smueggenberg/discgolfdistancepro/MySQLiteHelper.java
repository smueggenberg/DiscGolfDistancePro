package edu.css.smueggenberg.discgolfdistancepro;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by smueggenberg on 4/22/2015.
 */
public class MySQLiteHelper extends SQLiteOpenHelper {
    //declare constant for table name
    public static final String TABLE_THROWS = "throws";

    //declare field constants for the throw table
    public static final String THROW_ID = "_id";
    public static final String THROW_DISTANCE = "distance";
    public static final String THROW_TYPE = "type";
    public static final String THROW_COURSE = "course";
    public static final String THROW_DATE = "date";

    public static final String DATABASE_NAME = "dgdp.db";
    public static final int DATABASE_VERSION = 1;

    public MySQLiteHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String THROW_TABLE_CREATE = "create table " + TABLE_THROWS + "(" + THROW_ID + " integer primary key autoincrement," +
                THROW_DISTANCE + " real not null," +
                THROW_TYPE + " text," +
                THROW_COURSE + " text," +
                THROW_DATE + " text);";

        db.execSQL(THROW_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_THROWS);
        onCreate(db);
    }
}
