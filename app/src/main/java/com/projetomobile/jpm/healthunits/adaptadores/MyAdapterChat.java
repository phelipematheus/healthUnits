package com.projetomobile.jpm.healthunits.adaptadores;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.projetomobile.jpm.healthunits.R;
import com.projetomobile.jpm.healthunits.valueobject.ChatMessage;

import java.util.List;

import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;


public class MyAdapterChat extends RecyclerView.Adapter<MyAdapterChat.ViewHolder>{

    private List<ChatMessage> values;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView messageText, messageUser, messageTime;
        public View layout;
        public ImageView messageBitmap;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            messageText = (EmojiconTextView) v.findViewById(R.id.message_text);
            messageUser = (TextView) v.findViewById(R.id.message_user);
            messageTime = (TextView) v.findViewById(R.id.message_time);
            messageBitmap = (ImageView) v.findViewById(R.id.message_bitmap);
        }
    }
    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapterChat(List<ChatMessage> myDataset) {
        values = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapterChat.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View v = inflater.inflate(R.layout.row_chat, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        final ChatMessage chatMessage = values.get(position);
        //get(position);
        int num = Integer.parseInt(chatMessage.getTemImagem());
        holder.messageText.setText(chatMessage.getMessageText());
        holder.messageUser.setText(chatMessage.getMessageUser());
        holder.messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", chatMessage.getMessageTime()));
        try {//converte o dado string para bitmap
            if(num==1){
                byte [] encodeByte= Base64.decode(chatMessage.getMessageBitmap(),Base64.DEFAULT);
                Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                holder.messageBitmap.setVisibility(View.VISIBLE);
                holder.messageBitmap.setImageBitmap(bitmap);

                holder.messageText.setTextColor(Color.RED);

            }else if(num==2){
                holder.messageBitmap.setImageBitmap(null);
                holder.messageBitmap.setVisibility(View.GONE);

                holder.messageText.setTextColor(Color.BLACK);
            }

        } catch(Exception e) {
            e.getMessage();
        }


    }

    @Override
    public int getItemCount() {
        return values.size();
    }
}
