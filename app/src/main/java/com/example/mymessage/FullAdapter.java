package com.example.mymessage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FullAdapter extends RecyclerView.Adapter<FullAdapter.ViewHolder> {

    private List<String> messages;
    private Context context;
    private DatabaseHelper dbHelper;
    private String phoneNumber;

    public FullAdapter(Context context, List<String> messages, String phoneNumber) {
        this.context = context;
        this.messages = messages;
        this.dbHelper = new DatabaseHelper(context);
        this.phoneNumber = phoneNumber;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String message = messages.get(position);
        holder.messageTextView.setText(message);

        holder.itemView.setOnLongClickListener(v -> {
            showDeleteConfirmationDialog(message, position);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    private void showDeleteConfirmationDialog(String message, int position) {
        new AlertDialog.Builder(context)
                .setTitle("Delete Message")
                .setMessage("Are you sure you want to delete this message?")
                .setPositiveButton("Delete", (dialog, which) -> deleteMessage(message, position))
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deleteMessage(String message, int position) {
        // Assuming each message is unique, find and delete the message from the database
        List<Message> allMessages = dbHelper.getAllMessages();
        for (Message dbMessage : allMessages) {
            if (dbMessage.getPhoneNumber().equals(phoneNumber) && dbMessage.getMessageText().equals(message)) {
                dbHelper.deleteMessage(dbMessage.getId());
                messages.remove(position);
                notifyItemRemoved(position);
                break;
            }
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView messageTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.fullMessage);
        }
    }
}
