package com.example.mymessage;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SmsReceiver extends BroadcastReceiver {

    private static final String TAG = "SmsReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Object[] pdus = (Object[]) bundle.get("pdus");
            if (pdus != null) {
                for (Object pdu : pdus) {
                    SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu);
                    String senderPhoneNumber = smsMessage.getDisplayOriginatingAddress();
                    String messageBody = smsMessage.getMessageBody();
                    Log.d(TAG, "Received SMS: " + messageBody + ", from: " + senderPhoneNumber);

                    // Save the message to the SQLite database
                    saveMessageToDatabase(context, senderPhoneNumber, messageBody);

                    // Optionally, show a toast notification
                    Toast.makeText(context, "Received SMS from: " + senderPhoneNumber, Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void saveMessageToDatabase(Context context, String senderPhoneNumber, String messageBody) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_PHONE_NUMBER, senderPhoneNumber);
        values.put(DatabaseHelper.COLUMN_MESSAGE, messageBody);
        long newRowId = db.insert(DatabaseHelper.TABLE_MESSAGES, null, values);
        Log.d(TAG, "Message saved to SQLite database, row ID: " + newRowId);
        db.close();
    }
}
