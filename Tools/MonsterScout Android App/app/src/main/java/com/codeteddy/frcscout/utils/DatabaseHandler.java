package com.codeteddy.frcscout.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

/**
 * @author Alex
 * Created by Alex on 09.03.2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;

    private static final String DATABASE_NAME = "monsterScoutDB";

    private static final String TABLE_NAME = "qrcodes";
    private static final String KEY_ID = "id";
    private static final String KEY_TYPE = "type";
    private static final String KEY_QRCODE = "qrcode";
    private static final String KEY_UPLOADED = "uploaded";

    public DatabaseHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_QRCODE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_TYPE + " TEXT, "
                + KEY_QRCODE + " TEXT, " + KEY_UPLOADED + " INTEGER " + ")";

        sqLiteDatabase.execSQL(CREATE_QRCODE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // Drop older table if existed
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // Create tables again
        onCreate(sqLiteDatabase);

    }

    public void addQRCode(QRCode qrCode){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TYPE, qrCode.getType());
        values.put(KEY_QRCODE, qrCode.getQrcode());
        int uploaded;
        if(qrCode.isUploaded())
            uploaded = 1;
        else
            uploaded = 0;

        values.put(KEY_UPLOADED, uploaded);

        // Inserting Row
        db.insert(TABLE_NAME, null, values);
        db.close(); // Closing database connection
    }

    public QRCode getQRCode(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[] { KEY_ID,
                        KEY_TYPE, KEY_QRCODE, KEY_UPLOADED }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        boolean isUploaded;
        if(cursor.getInt(3) == 1)
            isUploaded = true;
        else
            isUploaded = false;

        QRCode qrCode = new QRCode(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), isUploaded);
        // return contact
        return qrCode;
    }

    public ArrayList<QRCode> getAllQRCodes() {
        ArrayList<QRCode> contactList = new ArrayList<QRCode>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                boolean isUploaded;
                if(cursor.getInt(3) == 1)
                    isUploaded = true;
                else
                    isUploaded = false;
                QRCode code = new QRCode(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), isUploaded);
                // Adding contact to list
                contactList.add(code);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }

    public int getQRCodeCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

    public int updateQRCode(QRCode code) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TYPE, code.getType());
        values.put(KEY_QRCODE, code.getQrcode());
        int uploaded;
        if(code.isUploaded())
            uploaded = 1;
        else
            uploaded = 0;

        values.put(KEY_UPLOADED, uploaded);
        // updating row
        return db.update(TABLE_NAME, values, KEY_ID + " = ?",
                new String[] { String.valueOf(code.getId()) });
    }

    public void deleteQRCode(QRCode code) {
        SQLiteDatabase db = this.getWritableDatabase();

        //db.delete(TABLE_NAME, KEY_ID + " = ?", new String[] { String.valueOf(code.getId()) });
        db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + KEY_ID + "='" + code.getId() + "'");
        db.close();
    }
}
