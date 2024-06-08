package com.example.mymessage;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.content.DialogInterface;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private Context context;
    private List<Message> originalMessageList;
    private List<Message> groupedMessageList;
    private DatabaseHelper dbHelper;

    public MessageAdapter(Context context, List<Message> messageList) {
        this.context = context;
        this.originalMessageList = messageList;
        this.groupedMessageList = groupMessagesByPhoneNumber(messageList);
        dbHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = groupedMessageList.get(position);
        holder.phoneNumberTextView.setText(message.getPhoneNumber());
        holder.messageTextView.setText(message.getMessageText());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, MainActivity3.class);
            intent.putExtra("PHONE_NUMBER", message.getPhoneNumber());
            intent.putExtra("MESSAGE_TEXT", message.getMessageText());
            context.startActivity(intent);
        });

        holder.itemView.setOnLongClickListener(v -> {
            showDeleteConfirmationDialog(message.getId(), position);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return groupedMessageList.size();
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {

        TextView phoneNumberTextView;
        TextView messageTextView;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            phoneNumberTextView = itemView.findViewById(R.id.phoneNumberTextView);
            messageTextView = itemView.findViewById(R.id.messageTextView);
        }
    }

    private List<Message> groupMessagesByPhoneNumber(List<Message> messageList) {
        Map<String, String> messageMap = new HashMap<>();
        for (Message message : messageList) {
            String phoneNumber = message.getPhoneNumber();
            String messageText = message.getMessageText();
            if (messageMap.containsKey(phoneNumber)) {
                String existingMessages = messageMap.get(phoneNumber);
                messageMap.put(phoneNumber, existingMessages + "\n" + messageText);
            } else {
                messageMap.put(phoneNumber, messageText);
            }
        }

        List<Message> groupedMessages = new ArrayList<>();
        for (Map.Entry<String, String> entry : messageMap.entrySet()) {
            // Create a Message object with a dummy ID of -1
            groupedMessages.add(new Message(-1, entry.getKey(), entry.getValue()));
        }
        return groupedMessages;
    }

    private void showDeleteConfirmationDialog(int messageId, int position) {
        new AlertDialog.Builder(context)
                .setTitle("Delete Message")
                .setMessage("Are you sure you want to delete this message?")
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    dbHelper.deleteMessage(messageId);
                    groupedMessageList.remove(position);
                    notifyItemRemoved(position);
                })
                .setNegativeButton(android.R.string.no, null)
                .show();
    }
}
