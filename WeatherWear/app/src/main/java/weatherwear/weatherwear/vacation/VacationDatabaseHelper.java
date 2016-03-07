package weatherwear.weatherwear.vacation;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;


/**
 * Created by emilylin27 on 3/1/16.
 */
public class VacationDatabaseHelper extends SQLiteOpenHelper {
    // Database Strings
    private static String CREATE_TABLE_ITEMS = "" +
            "CREATE TABLE IF NOT EXISTS " +
            "ITEMS ( " +
            "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "zipcode TEXT, " +
            "start DATETIME, " +
            "end DATETIME, " +
            "name TEXT, " +
            "dayoneid INTEGER, " +
            "daytwoid INTEGER, " +
            "daythreeid INTEGER, " +
            "dayfourid INTEGER, " +
            "dayfiveid INTEGER " +
            ");";

    private static String DATABASE_NAME = "WeatherWearVacationDB";
    private static int DATABASE_VERSION = 1;
    private static String TABLE_NAME = "Items";
    private String[] ALL_COLUMNS = {KEY_ID, KEY_ZIPCODE, KEY_START, KEY_END, KEY_NAME, KEY_DAY_ONE,
            KEY_DAY_TWO, KEY_DAY_THREE, KEY_DAY_FOUR, KEY_DAY_FIVE};

    // Value keys
    public static final String KEY_ID = "_id";
    public static final String KEY_ZIPCODE= "zipcode";
    public static final String KEY_START = "start";
    public static final String KEY_END = "end";
    public static final String KEY_NAME = "name";
    public static final String KEY_DAY_ONE = "dayoneid";
    public static final String KEY_DAY_TWO = "daytwoid";
    public static final String KEY_DAY_THREE = "daythreeid";
    public static final String KEY_DAY_FOUR = "dayfourid";
    public static final String KEY_DAY_FIVE = "dayfiveid";


    // Constructor
    public VacationDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creates database if doesn't exist
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_ITEMS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    //Updates vacation info after editing or on/off
    public void onUpdate(VacationModel vacation) {
        ContentValues values = new ContentValues();
        values.put(KEY_ZIPCODE, vacation.getZipCode());
        values.put(KEY_START, vacation.getStartInMillis());
        values.put(KEY_END, vacation.getEndInMillis());
        values.put(KEY_NAME, vacation.getName());
        values.put(KEY_DAY_ONE, vacation.getDayOne());
        values.put(KEY_DAY_TWO, vacation.getDayTwo());
        values.put(KEY_DAY_THREE,vacation.getDayThree());
        values.put(KEY_DAY_FOUR, vacation.getDayFour());
        values.put(KEY_DAY_FIVE, vacation.getDayFive());

        String[] whereArgs = new String[] {Long.toString(vacation.getId())};
        SQLiteDatabase db = getWritableDatabase();
        db.update(TABLE_NAME, values, KEY_ID + " = ?", whereArgs);
        db.close();
    }

    // Insert a item given each column value
    public long insertVacation(VacationModel vacation) {
        // Create ContentValues and fill with values
        ContentValues values = new ContentValues();
        values.put(KEY_ZIPCODE, vacation.getZipCode());
        values.put(KEY_START, vacation.getStartInMillis());
        values.put(KEY_END, vacation.getEndInMillis());
        values.put(KEY_NAME, vacation.getName());
        values.put(KEY_DAY_ONE, vacation.getDayOne());
        values.put(KEY_DAY_TWO, vacation.getDayTwo());
        values.put(KEY_DAY_THREE,vacation.getDayThree());
        values.put(KEY_DAY_FOUR, vacation.getDayFour());
        values.put(KEY_DAY_FIVE, vacation.getDayFive());

        // Create a database, insert into table, and close
        SQLiteDatabase db = getWritableDatabase();
        long id = db.insert(TABLE_NAME, null, values);
        db.close();
        return id;
    }

    // Remove a vacation by giving its index (on a thread!)
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

    // Query the entire table, return all vacations
    public ArrayList<VacationModel> fetchEntries() {
        // Create and query db, create array list
        SQLiteDatabase db = getWritableDatabase();
        ArrayList<VacationModel> vacations = new ArrayList<VacationModel>();
        Cursor cursor = db.query(TABLE_NAME, ALL_COLUMNS, null, null, null, null, null);
        cursor.moveToFirst();
        // Process through all returned, creating vacations and adding to list
        while (!cursor.isAfterLast()) {
            VacationModel vacation = cursorToVacation(cursor);
            vacations.add(vacation);
            cursor.moveToNext();
        }
        // Close everything up
        cursor.close();
        db.close();

        return vacations;
    }

    private VacationModel cursorToVacation(Cursor c) {
        VacationModel vacation = new VacationModel();

        vacation.setId(c.getLong(c.getColumnIndex(KEY_ID)));
        vacation.setZipCode((c.getString(c.getColumnIndex(KEY_ZIPCODE))));
        vacation.setStartDate(c.getLong(c.getColumnIndex(KEY_START)));
        vacation.setEndDate(c.getLong(c.getColumnIndex(KEY_END)));
        vacation.setName(c.getString(c.getColumnIndex(KEY_NAME)));
        vacation.setDayOne(c.getInt(c.getColumnIndex(KEY_DAY_ONE)));
        vacation.setDayTwo(c.getInt(c.getColumnIndex(KEY_DAY_TWO)));
        vacation.setDayThree(c.getInt(c.getColumnIndex(KEY_DAY_THREE)));
        vacation.setDayFour(c.getInt(c.getColumnIndex(KEY_DAY_FOUR)));
        vacation.setDayFive(c.getInt(c.getColumnIndex(KEY_DAY_FIVE)));

        return vacation;
    }
}
