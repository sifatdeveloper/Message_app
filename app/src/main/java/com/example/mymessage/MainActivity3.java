package com.example.mymessage;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        TextView phoneNumberTextView = findViewById(R.id.phoneNumberTextView);
        RecyclerView fullMessageRecyclerView = findViewById(R.id.fullMessageTextView);

        Intent intent = getIntent();
        String phoneNumber = intent.getStringExtra("PHONE_NUMBER");
        String messageText = intent.getStringExtra("MESSAGE_TEXT");

        if (phoneNumber != null && messageText != null) {
            phoneNumberTextView.setText(phoneNumber);

            List<String> messages = getMessageList(messageText);
            FullAdapter adapter = new FullAdapter(this, messages, phoneNumber);
            fullMessageRecyclerView.setAdapter(adapter);
            fullMessageRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
    }

    private List<String> getMessageList(String messageText) {
        // Split the messageText into a list of messages (assuming messages are separated by new lines)
        String[] messagesArray = messageText.split("\n");
        List<String> messagesList = new ArrayList<>();
        for (String message : messagesArray) {
            messagesList.add(message);
        }
        return messagesList;
    }
}
