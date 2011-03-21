package com.underhilllabs.knitting;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbAdapter {
	public static final String KEY_NAME = "name";
	public static final String KEY_SIZE = "size";
	public static final String KEY_SIZE_I = "size_i";
	public static final String KEY_NOTES = "notes";
	public static final String KEY_IS_METRIC = "is_metric";
	public static final String KEY_IS_CROCHET = "is_crochet";
	public static final String KEY_TYPE = "type";
	public static final String KEY_MATERIAL = "material";
	public static final String KEY_LENGTH = "length";
	public static final String KEY_LENGTH_I = "length_i";
	public static final String KEY_ROWID = "_id";

	public static final String KEY_START = "start_date";
	public static final String KEY_LASTMOD = "lastmod_date";
	public static final String KEY_STATUS = "status";
	public static final String KEY_STATUS_I = "status_i";
	public static final String KEY_PICTURE = "picture_uri";

	public static final String KEY_CURRENT_VAL = "current_val";
	public static final String KEY_PROJECT_ID = "project_id";
	public static final String KEY_PROJECT_NAME = "project_name";
	public static final String KEY_UP_OR_DOWN = "up_or_down";
	public static final String KEY_MULTIPLE = "multiple";
	public static final String KEY_IN_USE = "in_use";
	public static final String KEY_NEEDED_SHOPPING = "needed_shopping";

	private static final String TAG = "DbAdapter";

	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;

	/**
	 * Database creation sql statement
	 */
	private static final String N_DATABASE_CREATE = "create table needles (_id integer primary key autoincrement, "
			+ "size text not null, "
			+ "size_i int, "
			+ "length text, "
			+ "length_i int, "
			+ "type text, "
			+ "material text, "
			+ "is_metric integer, "
			+ "is_crochet integer, "
			+ "notes text,"
			+ "in_use integer, " + "project_id integer);";

	private static final String H_DATABASE_CREATE = "create table hooks (_id integer primary key autoincrement, "
			+ "size text not null, "
			+ "size_i int, "
			+ "material text, "
			+ "notes text);";

	private static final String P_DATABASE_CREATE = "create table projects (_id integer primary key autoincrement, "
			+ "name text not null, "
			+ "start_date text, "
			+ "lastmod_date text, "
			+ "status text, "
			+ "status_i int,"
			+ "picture_uri text, " + "notes text," + "needed_shopping text);";

	private static final String C_DATABASE_CREATE = "create table counters (_id integer primary key autoincrement, "
			+ "name text not null, "
			+ "current_val integer, "
			+ "project_name text, "
			+ "project_id integer, "
			+ "up_or_down integer, "
			+ "type text, "
			+ "multiple integer, "
			+ "notes text,"
			+ "is_complex integer, "
			+ "counter_x integer, "
			+ "counter_y integer);";

	private static final String DATABASE_NAME = "knittingstash";
	private static final String N_DATABASE_TABLE = "needles";
	private static final String H_DATABASE_TABLE = "hooks";
	private static final String P_DATABASE_TABLE = "projects";
	private static final String C_DATABASE_TABLE = "counters";

	private static final int DATABASE_VERSION = 13;

	private final Context mCtx;

	private static class DatabaseHelper extends SQLiteOpenHelper {

		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {

			db.execSQL(H_DATABASE_CREATE);
			db.execSQL(N_DATABASE_CREATE);
			db.execSQL(P_DATABASE_CREATE);
			db.execSQL(C_DATABASE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");
			if (oldVersion == 11) {
				db.execSQL("ALTER TABLE needles ADD COLUMN in_use integer");
				db.execSQL("ALTER TABLE needles ADD COLUMN project_id integer");
				char quote_char = '"';
				// adding two new straight and dpns needles lengths, so have to
				// update length_i on straight and dpns needles
				db
						.execSQL("UPDATE needles set length_i = 2 where type = 'dpns' and length = '6"
								+ quote_char + "'");
				db
						.execSQL("UPDATE needles set length_i = 3 where type = 'dpns' and length = '8"
								+ quote_char + "'");
				db
						.execSQL("UPDATE needles set length_i = 1 where type = 'straight' and length = '10"
								+ quote_char + "'");
				db
						.execSQL("UPDATE needles set length_i = 3 where type = 'straight' and length = '14"
								+ quote_char + "'");

				// add new complex counter
				db
						.execSQL("ALTER TABLE counters ADD COLUMN is_complex integer");
				db.execSQL("ALTER TABLE counters ADD COLUMN counter_x integer");
				db.execSQL("ALTER TABLE counters ADD COLUMN counter_y integer");

				// add a shopping needed column, so user can bring up a list of
				// projects that need shopping to be
				// complete
				db
						.execSQL("ALTER TABLE projects ADD COLUMN needed_shopping text");

			} else if (oldVersion == 12) {
				char quote_char = '"';
				// adding two new straight and dpns needles lengths, so have to
				// update length_i on straight and dpns needles
				db
						.execSQL("UPDATE needles set length_i = 2 where type = 'dpns' and length = '6"
								+ quote_char + "'");
				db
						.execSQL("UPDATE needles set length_i = 3 where type = 'dpns' and length = '8"
								+ quote_char + "'");
				db
						.execSQL("UPDATE needles set length_i = 1 where type = 'straight' and length = '10"
								+ quote_char + "'");
				db
						.execSQL("UPDATE needles set length_i = 3 where type = 'straight' and length = '14"
								+ quote_char + "'");

				// add new complex counter
				db
						.execSQL("ALTER TABLE counters ADD COLUMN is_complex integer");
				db.execSQL("ALTER TABLE counters ADD COLUMN counter_x integer");
				db.execSQL("ALTER TABLE counters ADD COLUMN counter_y integer");

				// add a shopping needed column, so user can bring up a list of
				// projects that need shopping to be
				// complete
				db
						.execSQL("ALTER TABLE projects ADD COLUMN needed_shopping text");

			} else {
				db.execSQL("DROP TABLE IF EXISTS needles");
				db.execSQL("DROP TABLE IF EXISTS hooks");
				db.execSQL("DROP TABLE IF EXISTS projects");
				db.execSQL("DROP TABLE IF EXISTS counters");
				onCreate(db);
			}
		}
	}

	/**
	 * Constructor - takes the context to allow the database to be
	 * opened/created
	 * 
	 * @param ctx
	 *            the Context within which to work
	 */
	public DbAdapter(Context ctx) {
		this.mCtx = ctx;
	}

	/**
	 * Open the notes database. If it cannot be opened, try to create a new
	 * instance of the database. If it cannot be created, throw an exception to
	 * signal the failure
	 * 
	 * @return this (self reference, allowing this to be chained in an
	 *         initialization call)
	 * @throws SQLException
	 *             if the database could be neither opened or created
	 */
	public DbAdapter open() throws SQLException {
		mDbHelper = new DatabaseHelper(mCtx);
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		mDbHelper.close();
	}

	/**
	 * Create a new note using the title and body provided. If the note is
	 * successfully created return the new rowId for that note, otherwise return
	 * a -1 to indicate failure.
	 * 
	 * @param title
	 *            the title of the note
	 * @param body
	 *            the body of the note
	 * @return rowId or -1 if failed
	 */
	public long createNeedle(String size, int size_i, String type,
			String material, String length, int length_i, int is_metric,
			int is_crochet, String notes, int in_use) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_SIZE, size);
		initialValues.put(KEY_SIZE_I, size_i);
		initialValues.put(KEY_TYPE, type);
		initialValues.put(KEY_MATERIAL, material);
		initialValues.put(KEY_NOTES, notes);
		initialValues.put(KEY_LENGTH, length);
		initialValues.put(KEY_LENGTH_I, length_i);
		initialValues.put(KEY_IS_METRIC, is_metric);
		initialValues.put(KEY_IS_CROCHET, is_crochet);
		initialValues.put(KEY_IN_USE, in_use);

		return mDb.insert(N_DATABASE_TABLE, null, initialValues);
	}

	/**
	 * Delete the note with the given rowId
	 * 
	 * @param rowId
	 *            id of note to delete
	 * @return true if deleted, false otherwise
	 */
	public boolean deleteNeedle(long rowId) {

		return mDb.delete(N_DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
	}

	/**
	 * Return a Cursor over the list of all notes in the database
	 * 
	 * @return Cursor over all notes
	 */
	public Cursor fetchAllNeedles() {

		return mDb.query(N_DATABASE_TABLE, new String[] { KEY_ROWID, KEY_SIZE,
				KEY_SIZE_I, KEY_LENGTH, KEY_LENGTH_I, KEY_TYPE, KEY_MATERIAL,
				KEY_NOTES, KEY_IS_METRIC, KEY_IS_CROCHET, KEY_IN_USE }, null,
				null, null, null, null);
	}

	public Cursor fetchAllAvailableNeedles() {

		return mDb.query(N_DATABASE_TABLE, new String[] { KEY_ROWID, KEY_SIZE,
				KEY_SIZE_I, KEY_LENGTH, KEY_LENGTH_I, KEY_TYPE, KEY_MATERIAL,
				KEY_NOTES, KEY_IS_METRIC, KEY_IS_CROCHET, KEY_IN_USE },
				" in_use<1 or in_use is null ", null, null, null, null);
	}

	public Cursor fetchAllNeedles_simple(String sort_criteria) {
		if (sort_criteria.equals("KEY_TYPE")) {
			return mDb.query(N_DATABASE_TABLE, new String[] { KEY_ROWID,
					KEY_SIZE, KEY_SIZE_I, KEY_LENGTH, KEY_LENGTH_I, KEY_TYPE,
					KEY_MATERIAL, KEY_IN_USE }, null, null, null, null,
					"type,size_i");
		} else {
			return mDb.query(N_DATABASE_TABLE, new String[] { KEY_ROWID,
					KEY_SIZE, KEY_SIZE_I, KEY_LENGTH, KEY_LENGTH_I, KEY_TYPE,
					KEY_MATERIAL, KEY_IN_USE }, null, null, null, null,
					KEY_SIZE_I);
		}

	}

	/**
	 * Return a Cursor positioned at the note that matches the given rowId
	 * 
	 * @param rowId
	 *            id of note to retrieve
	 * @return Cursor positioned to matching note, if found
	 * @throws SQLException
	 *             if note could not be found/retrieved
	 */
	public Cursor fetchNeedle(long rowId) throws SQLException {

		Cursor mCursor =

		mDb.query(true, N_DATABASE_TABLE, new String[] { KEY_ROWID, KEY_SIZE,
				KEY_SIZE_I, KEY_LENGTH, KEY_LENGTH_I, KEY_TYPE, KEY_MATERIAL,
				KEY_NOTES, KEY_IS_METRIC, KEY_IS_CROCHET, KEY_IN_USE },
				KEY_ROWID + "=" + rowId, null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;

	}

	/**
	 * Update the note using the details provided. The note to be updated is
	 * specified using the rowId, and it is altered to use the title and body
	 * values passed in
	 * 
	 * @param rowId
	 *            id of note to update
	 * @param title
	 *            value to set note title to
	 * @param body
	 *            value to set note body to
	 * @return true if the note was successfully updated, false otherwise
	 */
	public boolean updateNeedle(long rowId, String size, int size_i,
			String type, String material, String length, int length_i,
			int is_metric, int is_crochet, String notes, int in_use) {
		ContentValues args = new ContentValues();
		args.put(KEY_SIZE, size);
		args.put(KEY_SIZE_I, size_i);
		args.put(KEY_TYPE, type);
		args.put(KEY_LENGTH, length);
		args.put(KEY_LENGTH_I, length_i);
		args.put(KEY_MATERIAL, material);
		args.put(KEY_NOTES, notes);
		args.put(KEY_IS_METRIC, is_metric);
		args.put(KEY_IS_CROCHET, is_crochet);
		args.put(KEY_IN_USE, in_use);
		return mDb
				.update(N_DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
	}

	/**
	 * Hook functions
	 * 
	 */

	public long createHook(String size, int size_i, String material,
			String notes) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_SIZE, size);
		initialValues.put(KEY_SIZE_I, size_i);
		initialValues.put(KEY_MATERIAL, material);
		initialValues.put(KEY_NOTES, notes);

		return mDb.insert(H_DATABASE_TABLE, null, initialValues);
	}

	public boolean deleteHook(long rowId) {

		return mDb.delete(H_DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
	}

	public Cursor fetchAllHooks() {

		return mDb.query(H_DATABASE_TABLE, new String[] { KEY_ROWID, KEY_SIZE,
				KEY_SIZE_I, KEY_MATERIAL, KEY_NOTES }, null, null, null, null,
				null);
	}

	public Cursor fetchAllHooks_simple(String sort_criteria) {
		if (sort_criteria.equals("KEY_TYPE")) {
			return mDb.query(H_DATABASE_TABLE, new String[] { KEY_ROWID,
					KEY_SIZE, KEY_SIZE_I, KEY_MATERIAL, KEY_NOTES }, null,
					null, null, null, "material,size_i");
		} else {
			return mDb.query(H_DATABASE_TABLE, new String[] { KEY_ROWID,
					KEY_SIZE, KEY_SIZE_I, KEY_MATERIAL, KEY_NOTES }, null,
					null, null, null, KEY_SIZE_I);
		}
	}

	public Cursor fetchHook(long rowId) throws SQLException {

		Cursor mCursor =

		mDb.query(true, H_DATABASE_TABLE, new String[] { KEY_ROWID, KEY_SIZE,
				KEY_SIZE_I, KEY_MATERIAL, KEY_NOTES }, KEY_ROWID + "=" + rowId,
				null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;

	}

	public boolean updateHook(long rowId, String size, int size_i,
			String material, String notes) {
		ContentValues args = new ContentValues();
		args.put(KEY_SIZE, size);
		args.put(KEY_SIZE_I, size_i);
		args.put(KEY_MATERIAL, material);
		args.put(KEY_NOTES, notes);

		return mDb
				.update(H_DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
	}

	/*
	 * Project Table functions
	 */

	public long createProject(String name, String start, String lastmod,
			String status, int status_i, String notes, String needed_shopping) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_NAME, name);
		initialValues.put(KEY_START, start);
		initialValues.put(KEY_LASTMOD, lastmod);
		initialValues.put(KEY_STATUS, status);
		initialValues.put(KEY_STATUS_I, status_i);
		initialValues.put(KEY_NOTES, notes);
		initialValues.put(KEY_NEEDED_SHOPPING, needed_shopping);

		return mDb.insert(P_DATABASE_TABLE, null, initialValues);
	}

	public boolean deleteProject(long rowId) {

		return mDb.delete(P_DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
	}

	public Cursor fetchAllProjects() {

		return mDb.query(P_DATABASE_TABLE, new String[] { KEY_ROWID, KEY_NAME,
				KEY_START, KEY_LASTMOD, KEY_STATUS, KEY_STATUS_I, KEY_PICTURE,
				KEY_NOTES, KEY_NEEDED_SHOPPING }, null, null, null, null, null);
	}

	public Cursor fetchAllWipProjects() {

		return mDb.query(P_DATABASE_TABLE, new String[] { KEY_ROWID, KEY_NAME,
				KEY_START, KEY_LASTMOD, KEY_STATUS, KEY_STATUS_I, KEY_PICTURE,
				KEY_NOTES, KEY_NEEDED_SHOPPING }, "status<>'Finished'", null,
				null, null, null);
	}

	public Cursor fetchAllShoppingProjects() {

		return mDb.query(P_DATABASE_TABLE, new String[] { KEY_ROWID, KEY_NAME,
				KEY_START, KEY_LASTMOD, KEY_STATUS, KEY_STATUS_I, KEY_PICTURE,
				KEY_NOTES, KEY_NEEDED_SHOPPING }, "needed_shopping<>''", null,
				null, null, null);
	}

	public Cursor fetchAllProjects_simple(String sort_criteria) {
		if (sort_criteria.equals("KEY_LASTMOD")) {
			return mDb.query(P_DATABASE_TABLE, new String[] { KEY_ROWID,
					KEY_NAME, KEY_START, KEY_LASTMOD, KEY_STATUS, KEY_STATUS_I,
					KEY_PICTURE, KEY_NOTES, KEY_NEEDED_SHOPPING }, null, null,
					null, null, KEY_LASTMOD);
		} else if (sort_criteria.equals("KEY_STATUS")) {
			return mDb.query(P_DATABASE_TABLE, new String[] { KEY_ROWID,
					KEY_NAME, KEY_START, KEY_LASTMOD, KEY_STATUS, KEY_STATUS_I,
					KEY_PICTURE, KEY_NOTES, KEY_NEEDED_SHOPPING }, null, null,
					null, null, KEY_STATUS);
		} else {
			return mDb.query(P_DATABASE_TABLE, new String[] { KEY_ROWID,
					KEY_NAME, KEY_START, KEY_LASTMOD, KEY_STATUS, KEY_STATUS_I,
					KEY_PICTURE, KEY_NOTES, KEY_NEEDED_SHOPPING }, null, null,
					null, null, KEY_NAME);
		}

	}

	public Cursor fetchProject(long rowId) throws SQLException {

		Cursor mCursor =

		mDb.query(true, P_DATABASE_TABLE, new String[] { KEY_ROWID, KEY_NAME,
				KEY_START, KEY_LASTMOD, KEY_STATUS, KEY_STATUS_I, KEY_PICTURE,
				KEY_NOTES, KEY_NEEDED_SHOPPING }, KEY_ROWID + "=" + rowId,
				null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;

	}

	public boolean updateProject(long rowId, String name, String lastmod,
			String status, int status_i, String pic_uri, String notes,
			String needed_shopping) {
		ContentValues args = new ContentValues();
		args.put(KEY_NAME, name);
		args.put(KEY_LASTMOD, lastmod);
		args.put(KEY_STATUS, status);
		args.put(KEY_STATUS_I, status_i);
		args.put(KEY_PICTURE, pic_uri);
		args.put(KEY_NOTES, notes);
		args.put(KEY_NEEDED_SHOPPING, needed_shopping);
		return mDb
				.update(P_DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
	}

	/*
	 * Counters Table functions
	 */

	public long createCounter(String name, int current_val, int project_id,
			String project_name, int up_or_down, String type, int multiple,
			String notes) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_NAME, name);
		initialValues.put(KEY_CURRENT_VAL, current_val);
		initialValues.put(KEY_PROJECT_ID, project_id);
		initialValues.put(KEY_PROJECT_NAME, project_name);
		initialValues.put(KEY_UP_OR_DOWN, up_or_down);
		initialValues.put(KEY_TYPE, type);
		initialValues.put(KEY_MULTIPLE, multiple);
		initialValues.put(KEY_NOTES, notes);

		return mDb.insert(C_DATABASE_TABLE, null, initialValues);
	}

	public boolean deleteCounter(long rowId) {

		return mDb.delete(C_DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
	}

	public Cursor fetchAllCounters() {

		return mDb.query(C_DATABASE_TABLE, new String[] { KEY_ROWID, KEY_NAME,
				KEY_CURRENT_VAL, KEY_PROJECT_ID, KEY_PROJECT_NAME,
				KEY_UP_OR_DOWN, KEY_TYPE, KEY_MULTIPLE, KEY_NOTES }, null,
				null, null, null, null);
	}

	public Cursor fetchAllCounters_simple(String sort_criteria) {
		if (sort_criteria.equals("KEY_TYPE")) {
			return mDb.query(C_DATABASE_TABLE, new String[] { KEY_ROWID,
					KEY_NAME, KEY_CURRENT_VAL, KEY_PROJECT_ID,
					KEY_PROJECT_NAME, KEY_UP_OR_DOWN, KEY_TYPE, KEY_MULTIPLE,
					KEY_NOTES }, null, null, null, null, "type,name");
		} else {
			return mDb.query(C_DATABASE_TABLE, new String[] { KEY_ROWID,
					KEY_NAME, KEY_CURRENT_VAL, KEY_PROJECT_ID,
					KEY_PROJECT_NAME, KEY_UP_OR_DOWN, KEY_TYPE, KEY_MULTIPLE,
					KEY_NOTES }, null, null, null, null, KEY_NAME);
		}

	}

	public Cursor fetchCounter(long rowId) throws SQLException {

		Cursor mCursor =

		mDb.query(true, C_DATABASE_TABLE, new String[] { KEY_ROWID, KEY_NAME,
				KEY_CURRENT_VAL, KEY_PROJECT_ID, KEY_PROJECT_NAME,
				KEY_UP_OR_DOWN, KEY_TYPE, KEY_MULTIPLE, KEY_NOTES }, KEY_ROWID
				+ "=" + rowId, null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;

	}

	public boolean incrementCounter(long rowId, int current_val, int inc_amt) {
		ContentValues args = new ContentValues();
		args.put(KEY_CURRENT_VAL, current_val + inc_amt);

		return mDb
				.update(C_DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
	}

	public boolean setCounter(long rowId, int current_val) {
		ContentValues args = new ContentValues();
		args.put(KEY_CURRENT_VAL, current_val);

		return mDb
				.update(C_DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;

	}

	public boolean updateCounter(long rowId, String name, int current_val,
			int project_id, String project_name, int up_or_down, String type,
			int multiple, String notes) {
		ContentValues args = new ContentValues();
		args.put(KEY_NAME, name);
		args.put(KEY_CURRENT_VAL, current_val);
		args.put(KEY_PROJECT_ID, project_id);
		args.put(KEY_PROJECT_NAME, project_name);
		args.put(KEY_UP_OR_DOWN, up_or_down);
		args.put(KEY_TYPE, type);
		args.put(KEY_MULTIPLE, multiple);
		args.put(KEY_NOTES, notes);

		return mDb
				.update(C_DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
	}
	/*
	 * public Cursor fetchAllAvailableNeedles() { // TODO Auto-generated method
	 * stub return null; }
	 */

}
