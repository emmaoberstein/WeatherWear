package weatherwear.weatherwear.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import weatherwear.weatherwear.AlarmModel;

/**
 * Created by Emily Lin on 2/27/16.
 */
public class AlarmDatabaseHelper extends SQLiteOpenHelper {

    // Database Strings
    private static String CREATE_TABLE_ITEMS = "" +
            "CREATE TABLE IF NOT EXISTS " +
            "ITEMS (" +
            "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "sunday TEXT, " +
            "monday TEXT, " +
            "tuesday TEXT, " +
            "wednesday TEXT, " +
            "thursday TEXT, " +
            "friday TEXT, " +
            "saturday TEXT, " +
            "repeat TEXT, " +
            "datetime DATETIME " +
            ");";
    private static String DATABASE_NAME = "WeatherWearAlarmDB";
    private static int DATABASE_VERSION = 1;
    private static String TABLE_NAME = "Items";
    private String[] ALL_COLUMNS = {KEY_ID, KEY_SUNDAY, KEY_MONDAY, KEY_TUESDAY, KEY_WEDNESDAY,
            KEY_THURSDAY, KEY_FRIDAY, KEY_SATURDAY, KEY_REPEAT, KEY_DATETIME};

    // Value keys
    public static final String KEY_ID = "_id";
    public static final String KEY_SUNDAY= "sunday";
    public static final String KEY_MONDAY = "monday";
    public static final String KEY_TUESDAY = "tuesday";
    public static final String KEY_WEDNESDAY = "wednesday";
    public static final String KEY_THURSDAY = "thursday";
    public static final String KEY_FRIDAY = "friday";
    public static final String KEY_SATURDAY = "saturday";
    public static final String KEY_REPEAT = "repeat";
    public static final String KEY_DATETIME = "datetime";


    // Constructor
    public AlarmDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creates database if doesn't exist
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_ITEMS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // to properly extend abstract
    }

    // Insert a item given each column value
    public long insertAlarm(AlarmModel item) {
        // Create ContentValues and fill with values
        ContentValues values = new ContentValues();
        values.put(KEY_SUNDAY, item.getSun()? "T":"F");
        values.put(KEY_MONDAY, item.getMon()? "T":"F");
        values.put(KEY_TUESDAY, item.getTues()? "T":"F");
        values.put(KEY_WEDNESDAY, item.getWed()? "T":"F");
        values.put(KEY_THURSDAY, item.getThurs()? "T":"F");
        values.put(KEY_FRIDAY, item.getFri()? "T":"F");
        values.put(KEY_SATURDAY, item.getSat()? "T":"F");
        values.put(KEY_REPEAT, item.getRepeat()? "T":"F");
        values.put(KEY_DATETIME, item.getTimeInMillis());

        // Create a database, insert into table, and close
        SQLiteDatabase db = getWritableDatabase();
        long id = db.insert(TABLE_NAME, null, values);
        db.close();
        return id;
    }

    // Remove an entry by giving its index (on a thread!)
    public void removeEntry(long rowIndex) {
        final long row = rowIndex;
        new Thread() {
            public void run() {
                SQLiteDatabase db = getWritableDatabase();
                db.delete(TABLE_NAME, KEY_ID + " = " + row, null);
                db.close();
            }
        }.start();
    }

    // Query a specific entry by its index.
    public AlarmModel fetchEntryByIndex(long rowId) {
        // Create and query database
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(TABLE_NAME, ALL_COLUMNS, null, null, null, null, null);
        cursor.moveToFirst();
        // Convert cursor to ClothingItem, and close db/cursor
        AlarmModel alarm = cursorToAlarm(cursor);
        cursor.close();
        db.close();

        return alarm;
    }

    // Query the entire table, return all items
    public ArrayList<AlarmModel> fetchEntries() {
        // Create and query db, create array list
        SQLiteDatabase db = getWritableDatabase();
        ArrayList<AlarmModel> alarms = new ArrayList<AlarmModel>();
        Cursor cursor = db.query(TABLE_NAME, ALL_COLUMNS, null, null, null, null, null);
        cursor.moveToFirst();
        // Process through all returned, creating entries and adding to list
        while (!cursor.isAfterLast()) {
            AlarmModel alarm = cursorToAlarm(cursor);
            alarms.add(alarm);
            cursor.moveToNext();
        }
        // Close everything up
        cursor.close();
        db.close();

        return alarms;
    }

    private AlarmModel cursorToAlarm(Cursor c) {
        AlarmModel alarm = new AlarmModel();

        alarm.setId(c.getLong(c.getColumnIndex(KEY_ID)));
        alarm.setSun((c.getString(c.getColumnIndex(KEY_SUNDAY))).equals("T"));
        alarm.setMon((c.getString(c.getColumnIndex(KEY_MONDAY))).equals("T"));
        alarm.setTues((c.getString(c.getColumnIndex(KEY_TUESDAY)).equals("T")));
        alarm.setWed((c.getString(c.getColumnIndex(KEY_WEDNESDAY))).equals("T"));
        alarm.setThurs((c.getString(c.getColumnIndex(KEY_SUNDAY))).equals("T"));
        alarm.setFri((c.getString(c.getColumnIndex(KEY_FRIDAY))).equals("T"));
        alarm.setSat((c.getString(c.getColumnIndex(KEY_SATURDAY))).equals("T"));
        alarm.setRepeat((c.getString(c.getColumnIndex(KEY_REPEAT))).equals("T"));
        alarm.setTime((c.getLong(c.getColumnIndex(KEY_DATETIME))));
        return alarm;
    }
}
