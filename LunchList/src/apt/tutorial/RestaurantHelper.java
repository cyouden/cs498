package apt.tutorial;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RestaurantHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "lunchlist.db";
	private static final int SCHEMA_VERSION = 2;
	
	// SQL
	private static final String GET_ALL_QUERY = "SELECT _id, name, address, type, notes, feed FROM restaurants ORDER BY ";
	private static final String GET_BY_ID_QUERY = "SELECT _id, name, address, type, notes, feed FROM restaurants WHERE _ID=?";
	private static final String CREATE_TABLE = "CREATE TABLE restaurants (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, address TEXT, type TEXT, notes TEXT, feed TEXT)";
	private static final String ADD_FEED_COLUMN = "ALTER TABLE restaurants ADD COLUMN feed TEXT";
	
	
	public RestaurantHelper(Context context) {
		super(context, DATABASE_NAME, null, SCHEMA_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		/*
		 * 1 => 2: Add feed column
		 */
		
		if (oldVersion < 2) {
			db.execSQL(ADD_FEED_COLUMN);
		}
	}
	
	public void insert(String name, String address, String type, String notes, String feed) {
		ContentValues cv = new ContentValues();
		
		cv.put("name", name);
		cv.put("address", address);
		cv.put("type", type);
		cv.put("notes", notes);
		cv.put("feed", feed);
		
		getWritableDatabase().insert("restaurants", "name", cv);
	}
	
	public void update(String id, String name, String address, String type, String notes, String feed) {
		ContentValues cv = new ContentValues();
		String[] args = { id };
		
		cv.put("name", name);
		cv.put("address", address);
		cv.put("type", type);
		cv.put("notes", notes);
		cv.put("feed", feed);
		
		getWritableDatabase().update("restaurants", cv,  "_ID=?", args);
	}
	
	public Cursor getAll(String orderBy) {
		return getReadableDatabase().rawQuery(GET_ALL_QUERY + orderBy, null);
	}
	
	public Cursor getById(String id) {
		String[] args = { id };
		
		return getReadableDatabase().rawQuery(GET_BY_ID_QUERY, args);
	}
	
	public String getName(Cursor c) {
		return c.getString(1);
	}
	
	public String getAddress(Cursor c) {
		return c.getString(2);
	}
	
	public Restaurant.Type getType(Cursor c) {
		return Restaurant.Type.valueOf(c.getString(3));
	}
	
	public String getNotes(Cursor c) {
		return c.getString(4);
	}
	
	public String getFeed(Cursor c) {
		return c.getString(5);
	}
}
