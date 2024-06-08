package com.example.mymessage;

import static android.content.ContentValues.TAG;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "messages.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_MESSAGES = "messages";
    private static final String COLUMN_ID = "id";
    public static final String COLUMN_PHONE_NUMBER = "phone_number";
    public static final String COLUMN_MESSAGE = "message";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_MESSAGES_TABLE = "CREATE TABLE " + TABLE_MESSAGES + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_PHONE_NUMBER + " TEXT,"
                + COLUMN_MESSAGE + " TEXT" + ")";
        db.execSQL(CREATE_MESSAGES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGES);
        onCreate(db);
    }
    public void deleteMessage(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            int deletedRows = db.delete(TABLE_MESSAGES, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
            if (deletedRows > 0) {
                Log.d(TAG, "Message deleted from SQLite database, ID: " + id);
            } else {
                Log.d(TAG, "Message with ID " + id + " not found in the database.");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error deleting message from database: " + e.getMessage());
        } finally {
            db.close();
        }
    }





    public List<Message> getAllMessages() {
        List<Message> messages = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_MESSAGES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        int idIndex = cursor.getColumnIndex(COLUMN_ID);
        int phoneNumberIndex = cursor.getColumnIndex(COLUMN_PHONE_NUMBER);
        int messageIndex = cursor.getColumnIndex(COLUMN_MESSAGE);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(idIndex);
                String phoneNumber = cursor.getString(phoneNumberIndex);
                String message = cursor.getString(messageIndex);
                messages.add(new Message(id, phoneNumber, message));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return messages;
    }



}
