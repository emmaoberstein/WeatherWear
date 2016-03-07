package weatherwear.weatherwear.vacation;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by emilylin27 on 3/6/16.
 */
public class OutfitDatabaseHelper extends SQLiteOpenHelper{
    // Database Strings
    private static String CREATE_TABLE_ITEMS = "" +
            "CREATE TABLE IF NOT EXISTS " +
            "OUTFIT (" +
            "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "top INTEGER NOT NULL, " +
            "bottom INTEGER NOT NULL, " +
            "shoes INTEGER NOT NULL, " +
            "outerwear INTEGER NOT NULL, " +
            "gloves INTEGER NOT NULL, " +
            "hats INTEGER NOT NULL, " +
            "scarves INTEGER NOT NULL, " +
            "date DATETIME, " +
            "location TEXT, " +
            "high INTEGER NOT NULL, " +
            "low INTEGER NOT NULL, " +
            "condition TEXT, " +
            "day TEXT " +
            ");";
    public static String DATABASE_NAME = "WeatherWearOutfitDB";
    private static int DATABASE_VERSION = 1;
    private static String TABLE_NAME = "Outfit";
    private String[] ALL_COLUMNS = {KEY_ID, KEY_TOP, KEY_BOTTOM, KEY_SHOES, KEY_OUTERWEAR,
            KEY_GLOVES, KEY_HATS, KEY_SCARVES, KEY_DATE, KEY_LOCATION, KEY_HIGH, KEY_LOW, KEY_CONDITION,
            KEY_DAY};

    // Value keys
    public static String KEY_ID = "_id";
    public static String KEY_TOP = "top";
    public static String KEY_BOTTOM = "bottom";
    public static String KEY_SHOES = "shoes";
    public static String KEY_OUTERWEAR = "outerwear";
    public static String KEY_GLOVES = "gloves";
    public static String KEY_HATS = "hats";
    public static String KEY_SCARVES = "scarves";
    public static String KEY_DATE = "date";
    public static String KEY_LOCATION = "location";
    public static String KEY_HIGH = "high";
    public static String KEY_LOW = "low";
    public static String KEY_CONDITION = "condition";
    public static String KEY_DAY = "day";

    // Constructor
    public OutfitDatabaseHelper(Context context) {
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

    // Insert an outfit given each column value
    public long insertOutfit(OutfitModel outfit) {
        // Create ContentValues and fill with values
        ContentValues values = new ContentValues();
        values.put(KEY_TOP, outfit.getmTop());
        values.put(KEY_BOTTOM, outfit.getmBottom());
        values.put(KEY_SHOES, outfit.getmShoes());
        values.put(KEY_OUTERWEAR, outfit.getmOuterwear());
        values.put(KEY_GLOVES, outfit.getmGloves());
        values.put(KEY_HATS, outfit.getmHat());
        values.put(KEY_HIGH, outfit.getmHigh());
        values.put(KEY_LOW, outfit.getmLow());
        values.put(KEY_SCARVES, outfit.getmScarves());
        values.put(KEY_DATE, outfit.getmDate());
        values.put(KEY_LOCATION, outfit.getmLocation());
        values.put(KEY_CONDITION, outfit.getmCondition());
        values.put(KEY_DAY, outfit.getmDay());

        // Create a database, insert into table, and close
        SQLiteDatabase db = getWritableDatabase();
        long id = db.insert(TABLE_NAME, null, values);
        db.close();

        return id;
    }

    // Updates an outfit
    public void updateOutfit(OutfitModel outfit) {
        ContentValues values = new ContentValues();
        values.put(KEY_TOP, outfit.getmTop());
        values.put(KEY_BOTTOM, outfit.getmBottom());
        values.put(KEY_SHOES, outfit.getmShoes());
        values.put(KEY_OUTERWEAR, outfit.getmOuterwear());
        values.put(KEY_GLOVES, outfit.getmGloves());
        values.put(KEY_HATS, outfit.getmHat());
        values.put(KEY_HIGH, outfit.getmHigh());
        values.put(KEY_LOW, outfit.getmLow());
        values.put(KEY_SCARVES, outfit.getmScarves());
        values.put(KEY_DATE, outfit.getmDate());
        values.put(KEY_LOCATION, outfit.getmLocation());
        values.put(KEY_CONDITION, outfit.getmCondition());
        values.put(KEY_DAY, outfit.getmDay());


        SQLiteDatabase db = getWritableDatabase();
        db.update(TABLE_NAME, values, KEY_ID + " = " + outfit.getmId(), null);
        db.close();
    }

    // Remove an outfit by giving its index (on a thread!)
    public void removeOutfit(long rowIndex) {
        final long row = rowIndex;
        new Thread() {
            public void run() {
                SQLiteDatabase db = getWritableDatabase();
                db.delete(TABLE_NAME, KEY_ID + " = " + row, null);
                db.close();
            }
        }.start();
    }

    // Query a specific outfit by its index.
    public OutfitModel fetchOutfitByIndex(long rowId) {
        // Create and query database
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(TABLE_NAME, ALL_COLUMNS, KEY_ID + " = " + rowId, null, null, null, null);
        cursor.moveToFirst();
        // Convert cursor to ClothingItem, and close db/cursor
        OutfitModel outfit = cursorToOutfit(cursor);
        cursor.close();
        db.close();

        return outfit;
    }

    private OutfitModel cursorToOutfit(Cursor c) {
        OutfitModel outfit = new OutfitModel();

        outfit.setmId(c.getLong(c.getColumnIndex(KEY_ID)));
        outfit.setmTop(c.getInt(c.getColumnIndex(KEY_TOP)));
        outfit.setmBottom(c.getInt(c.getColumnIndex(KEY_BOTTOM)));
        outfit.setmShoes(c.getInt(c.getColumnIndex(KEY_SHOES)));
        outfit.setmOuterwear(c.getInt(c.getColumnIndex(KEY_OUTERWEAR)));
        outfit.setmScarves(c.getInt(c.getColumnIndex(KEY_SCARVES)));
        outfit.setmHat(c.getInt(c.getColumnIndex(KEY_HATS)));
        outfit.setmGloves(c.getInt(c.getColumnIndex(KEY_GLOVES)));
        outfit.setmHigh(c.getInt(c.getColumnIndex(KEY_HIGH)));
        outfit.setmLow(c.getInt(c.getColumnIndex(KEY_LOW)));
        outfit.setmLocation(c.getString(c.getColumnIndex(KEY_LOCATION)));
        outfit.setmDate(c.getLong(c.getColumnIndex(KEY_DATE)));
        outfit.setmLocation(c.getString(c.getColumnIndex(KEY_LOCATION)));
        outfit.setmDay(c.getString(c.getColumnIndex(KEY_DAY)));
        outfit.setmCondition(c.getString(c.getColumnIndex(KEY_CONDITION)));

        return outfit;
    }
}
