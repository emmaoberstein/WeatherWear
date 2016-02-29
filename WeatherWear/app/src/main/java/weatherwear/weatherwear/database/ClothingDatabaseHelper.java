package weatherwear.weatherwear.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by alexbeals on 2/27/16.
 */
public class ClothingDatabaseHelper extends SQLiteOpenHelper {
    // Database Strings
    private static String CREATE_TABLE_ITEMS = "" +
            "CREATE TABLE IF NOT EXISTS " +
            "ITEMS (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "type TEXT, " +
                "cycle_length INTEGER NOT NULL, " +
                "last_used INTEGER NOT NULL, " +
                "seasons TEXT, " +
                "image BLOB" +
            ");";
    public static String DATABASE_NAME = "WeatherWearDB";
    private static int DATABASE_VERSION = 1;
    private static String TABLE_NAME = "Items";
    private String[] ALL_COLUMNS = {KEY_ID, KEY_TYPE, KEY_CYCLE_LENGTH, KEY_LAST_USED, KEY_SEASONS, KEY_IMAGE};

    // Value keys
    public static String KEY_ID = "_id";
    public static String KEY_TYPE = "type";
    public static String KEY_CYCLE_LENGTH = "cycle_length";
    public static String KEY_LAST_USED = "last_used";
    public static String KEY_SEASONS = "seasons";
    public static String KEY_IMAGE = "image";

    // Constructor
    public ClothingDatabaseHelper(Context context) {
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
    public long insertItem(ClothingItem item) {
        // Create ContentValues and fill with values
        ContentValues newItem = new ContentValues();
        newItem.put(KEY_TYPE, item.getType());
        newItem.put(KEY_CYCLE_LENGTH, item.getCycleLength());
        newItem.put(KEY_LAST_USED, item.getLastUsed());
        newItem.put(KEY_SEASONS, item.getSeasonsString());

        byte[] imageByteArray = item.getImageByteArray();
        newItem.put(KEY_IMAGE, imageByteArray);

        // Create a database, insert into table, and close
        SQLiteDatabase db = getWritableDatabase();
        long id = db.insert(TABLE_NAME, null, newItem);
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
    public ClothingItem fetchEntryByIndex(long rowId) {
        // Create and query database
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(TABLE_NAME, ALL_COLUMNS, KEY_ID + " = " + rowId, null, null, null, null);
        cursor.moveToFirst();
        // Convert cursor to ClothingItem, and close db/cursor
        ClothingItem item = cursorToItem(cursor);
        cursor.close();
        db.close();

        return item;
    }

    // Query the entire table, return all items
    public ArrayList<ClothingItem> fetchEntries() {
        // Create and query db, create array list
        SQLiteDatabase db = getWritableDatabase();
        ArrayList<ClothingItem> items = new ArrayList<ClothingItem>();
        Cursor cursor = db.query(TABLE_NAME, ALL_COLUMNS, null, null, null, null, null);
        cursor.moveToFirst();
        // Process through all returned, creating entries and adding to list
        while (!cursor.isAfterLast()) {
            ClothingItem item = cursorToItem(cursor);
            items.add(item);
            cursor.moveToNext();
        }
        // Close everything up
        cursor.close();
        db.close();

        return items;
    }

    public ArrayList<ClothingItem> fetchEntriesInCategory(String category) {
        // Create and query db, create array list
        SQLiteDatabase db = getWritableDatabase();
        ArrayList<ClothingItem> items = new ArrayList<ClothingItem>();
        Cursor cursor = null;
        try {
            cursor = db.query(TABLE_NAME, ALL_COLUMNS, KEY_TYPE + " = '" + category + "'", null, null, null, null);
            cursor.moveToFirst();
            // Process through all returned, creating entries and adding to list
            while (!cursor.isAfterLast()) {
                ClothingItem item = cursorToItem(cursor);
                items.add(item);
                cursor.moveToNext();
            }
        } catch (Exception e) {
            if(cursor != null)
                cursor.close();
        }
        // Close everything up
        if(cursor != null)
            cursor.close();
        db.close();

        return items;
    }

    private ClothingItem cursorToItem(Cursor c) {
        ClothingItem item = new ClothingItem();

        item.setId(c.getLong(c.getColumnIndex(KEY_ID)));
        item.setType(c.getString(c.getColumnIndex(KEY_TYPE)));
        item.setCycleLength(c.getInt(c.getColumnIndex(KEY_CYCLE_LENGTH)));
        item.setLastUsed(c.getInt(c.getColumnIndex(KEY_LAST_USED)));
        item.setSeasonsFromString(c.getString(c.getColumnIndex(KEY_SEASONS)));
        item.setImageFromByteArray(c.getBlob(c.getColumnIndex(KEY_IMAGE)));

        return item;
    }
}
