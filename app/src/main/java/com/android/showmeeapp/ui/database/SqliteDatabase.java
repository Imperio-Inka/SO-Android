package com.android.showmeeapp.ui.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class SqliteDatabase {

	static final String DATABASE_NAME = "ENFO.db";
	static final int DATABASE_VERSION = 1;
	static final String DATABASE_SM_CONTACT = "create table " + "TABLE_SM_CONTACT" +
			"( " + "SNO text " + "integer primary key" + ",Name text,Contact text,Email text,Image_uri text); ";
	static final String DATABASE_MY_CONTACT = "create table " + "TABLE_MY_CONTACT" +
			"( " + "SNO text " + "integer primary key" + ",Name text,Contact text,Email text,Image_uri text); ";
	// Context of the application using the database.
	private final Context context;
	public SQLiteDatabase db;
	// Database open/upgrade helper
	private DatabaseHelper dbHelper;

	public SqliteDatabase(Context _context) {
		context = _context;
		dbHelper = new DatabaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public SqliteDatabase open() throws SQLException {
		db = dbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		db.close();
	}

	public SQLiteDatabase getDatabaseInstance() {
		return db;
	}

	public long insertSMContact(String name, String contact, String email, String image_uri) {
		long result = 0;
		ContentValues value = new ContentValues();
		value.put("Name", name);
		value.put("Contact", contact);
		value.put("Email", email);
		value.put("Image_uri", image_uri);
		result = db.insert("TABLE_SM_CONTACT", null, value);

		return result;
	}

	public long insertMYContact(String name, String contact, String email, String image_uri) {
		long result = 0;
		ContentValues value = new ContentValues();
		value.put("Name", name);
		value.put("Contact", contact);
		value.put("Email", email);
		value.put("Image_uri", image_uri);
		result = db.insert("TABLE_MY_CONTACT", null, value);

		return result;
	}

	public Cursor getSMContact() {
		return db.query("TABLE_SM_CONTACT", null, null, null, null, null, null);
	}

	public Cursor getMYContact() {
		return db.query("TABLE_MY_CONTACT", null, null, null, null, null, null);
	}

	public void deleteAllContact() {
		db.delete("TABLE_SM_CONTACT", null, null);
		db.delete("TABLE_MY_CONTACT", null, null);
	}
}
