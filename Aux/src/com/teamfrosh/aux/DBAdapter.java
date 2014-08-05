package com.teamfrosh.aux;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBAdapter {

	private static final String DBADAPTER_TAG = "DBADAPTER_TAG";

	/* General Keys */
	public static String KEY_ROWID;

	/* Context Keys */
	public static String KEY_ACTIVITY;
	public static String KEY_PLACE;
	public static String KEY_TIME;
	public static String KEY_DAY;
	public static String KEY_LOCATION;

	/* Settings Keys */
	public static String KEY_BLUETOOTH;
	public static String KEY_WIFI;
	public static String KEY_VOLUME;
	public static String KEY_BRIGHTNESS;
	public static String KEY_LOCATION_SERVICES;
	public static String KEY_GPS;
	public static String KEY_AIRPLANE;

    /* ALL Keys */
    public static String[] ALL_KEYS;

	/* Misc Variables */
	private static final String DATABASE_NAME = "PROFILES_DB";
	private static final String DATABASE_TABLE = "PROFILES_TABLE";
    private static String DATABASE_CREATE;
	/*
	 * The version number must be incremented each time a change
	 * to DB structure occurs.
	 */
	private static final int DATABASE_VERSION = 1;
	private final Context context;
	private DatabaseHelper DBHelper;
	private SQLiteDatabase db;

	public DBAdapter(Context ctx) {
		this.context = ctx;
		Resources res = context.getResources();

		/* General Key */
        KEY_ROWID = res.getString(R.string.db_key_row_id);

        /* Context Keys */
        KEY_ACTIVITY = res.getString(R.string.db_key_activity);
        KEY_PLACE = res.getString(R.string.db_key_place);
        KEY_TIME = res.getString(R.string.db_key_time);
        KEY_DAY = res.getString(R.string.db_key_day);
        KEY_LOCATION = res.getString(R.string.db_key_location);

        /* Settings Keys */
        KEY_BLUETOOTH = res.getString(R.string.db_key_bluetooth);
        KEY_WIFI = res.getString(R.string.db_key_wifi);
        KEY_VOLUME = res.getString(R.string.db_key_volume);
        KEY_BRIGHTNESS = res.getString(R.string.db_key_brightness);
        KEY_LOCATION_SERVICES = res.getString(R.string.db_key_location_services);
        KEY_GPS = res.getString(R.string.db_key_gps);
        KEY_AIRPLANE = res.getString(R.string.db_key_airplane);

        ALL_KEYS = new String[]{ KEY_ROWID, KEY_ACTIVITY, KEY_PLACE, KEY_TIME,
            KEY_DAY, KEY_LOCATION, KEY_BLUETOOTH, KEY_WIFI, KEY_VOLUME,
            KEY_BRIGHTNESS, KEY_LOCATION_SERVICES, KEY_GPS, KEY_AIRPLANE };

        DATABASE_CREATE =
            "create table " + DATABASE_TABLE + "(_id integer primary key autoincrement, "
            + KEY_ACTIVITY + " TEXT, "
            + KEY_PLACE + " TEXT, "
            + KEY_TIME + " TEXT, "
            + KEY_DAY + " TEXT, "
            + KEY_LOCATION + " TEXT, "
            + KEY_BLUETOOTH + " TEXT, "
            + KEY_WIFI + " TEXT, "
            + KEY_VOLUME + " TEXT, "
            + KEY_BRIGHTNESS + " TEXT, "
            + KEY_LOCATION_SERVICES + " TEXT, "
            + KEY_GPS + " TEXT, "
            + KEY_AIRPLANE + " TEXT);";

        DBHelper = new DatabaseHelper(context);
	}

    // Opens the database
    public DBAdapter open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    // Closes the database
    public void close() {
        DBHelper.close();
    }

    // Insert a new profile into the database
    public long insertProfile(String[] columns, String[] values) {
        ContentValues initialValues = new ContentValues();

        if (columns.length != values.length) {
            return -1;
        } else {
            for (int i = 0; i < columns.length; i++) {
                initialValues.put(columns[i], values[i]);
            }
        }

        // Ensure that profiles with the same row id are not inserted twice
        try {
            return db.insertOrThrow(DATABASE_TABLE, null, initialValues);
        } catch (SQLException exception) {
            // Do nothing
        }
        return -1;
    }

    // Delete a profile
    public boolean deleteProfile(long rowId) {
        return db.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) != 0;
    }

    // Get a profile
    public Cursor getProfile(long rowId) {
        String select = KEY_ROWID + "=" + rowId;
        Cursor c = db.query(true, DATABASE_TABLE, ALL_KEYS, select,
                null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    // Get ALL profiles
    public Cursor getAllProfiles() {
        String select = null;
        Cursor c = db.query(true, DATABASE_TABLE, ALL_KEYS, select,
                null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
            onCreate(db);
        }
    }

}
