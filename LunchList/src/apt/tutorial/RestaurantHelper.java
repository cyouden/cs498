package apt.tutorial;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RestaurantHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "lunchlist.db";
	private static final int SCHEMA_VERSION = 1;
	
	// Database queries
	private static final String GET_ALL_QUERY = "SELECT _id, name, address, type, notes FROM restaurants ORDER BY name;";
	private static final String GET_BY_ID_QUERY = "SELECT _id, name, address, type, notes FROM restaurants WHERE _ID=?;";
	
	public RestaurantHelper(Context context) {
		super(context, DATABASE_NAME, null, SCHEMA_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE restaurants (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, address TEXT, type TEXT, notes TEXT);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// no upgrade to perform yet
	}
	
	public void insert(String name, String address, String type, String notes) {
		ContentValues cv = new ContentValues();
		
		cv.put("name", name);
		cv.put("address", address);
		cv.put("type", type);
		cv.put("notes", notes);
		
		getWritableDatabase().insert("restaurants", "name", cv);
	}
	
	public void update(String id, String name, String address, String type, String notes) {
		ContentValues cv = new ContentValues();
		String[] args = { id };
		
		cv.put("name", name);
		cv.put("address", address);
		cv.put("type", type);
		cv.put("notes", notes);
		
		getWritableDatabase().update("restaurants", cv,  "_ID=?", args);
	}
	
	public Cursor getAll() {
		return getReadableDatabase().rawQuery(GET_ALL_QUERY, null);
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
}
