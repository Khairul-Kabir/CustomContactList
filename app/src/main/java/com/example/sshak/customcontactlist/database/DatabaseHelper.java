package com.example.sshak.customcontactlist.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.sshak.customcontactlist.model.ContactVO;

import java.util.ArrayList;

/**
 * Created by sshak on 3/31/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "contactdb.db";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        TableAttributes tableAttr = new TableAttributes();
        String query = tableAttr.contactTableCreateQuery();
        try {
            db.execSQL(query);
            Log.i("Create", "Complete");
        } catch (SQLException e) {
            Log.e("Create Error", e.toString());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertContact(ContactVO contactVO) {

        SQLiteDatabase dbInsert = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(TableAttributes.NAME, contactVO.getContactName());
        values.put(TableAttributes.PHONE, contactVO.getContactNumber());

        try {
            dbInsert.insert(TableAttributes.CONTACT_TABLE_NAME, null, values);
            Log.i("Data", values.toString());
        } catch (SQLException e) {
            Log.e("Insert Error", e.toString());
        }
    }

    public ArrayList<ContactVO> getAllContacts() {
        ArrayList<ContactVO> arrayList = new ArrayList<ContactVO>();
        SQLiteDatabase dbFetch = this.getReadableDatabase();
        String query = "SELECT * FROM " + TableAttributes.CONTACT_TABLE_NAME;
        Cursor cur = dbFetch.rawQuery(query, null);
        cur.moveToFirst();
        while(!cur.isAfterLast()){
            ContactVO cntObj = new ContactVO();
            cntObj.setContactName(cur.getString(cur.getColumnIndex(TableAttributes.NAME)));
            cntObj.setContactNumber(cur.getString(cur.getColumnIndex(TableAttributes.PHONE)));

            arrayList.add(cntObj);
            cur.moveToNext();
        }
        return arrayList;
    }
}
