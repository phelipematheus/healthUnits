package com.projetomobile.jpm.healthunits.adaptadores;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.projetomobile.jpm.healthunits.R;
import com.projetomobile.jpm.healthunits.telas.TelaMaps;
import com.projetomobile.jpm.healthunits.valueobject.ChatMessage;

import java.util.List;

import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;


public class MyAdapterChat extends RecyclerView.Adapter<MyAdapterChat.ViewHolder>{

    private List<ChatMessage> values;
    private LatLng origem;
    private Context contextTelaChat;
    private Bitmap bm;

    private FirebaseStorage storage;
    private StorageReference storageRef;
    private StorageReference mountainsRef;

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
    public MyAdapterChat(Context contextTelaChat, List<ChatMessage> myDataset, LatLng origem) {
        this.contextTelaChat = contextTelaChat;
        this.origem = origem;
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
    public void onBindViewHolder(final ViewHolder holder, final int position) {


        final ChatMessage chatMessage = values.get(position);
        //get(position);
        int num = Integer.parseInt(chatMessage.getTemImagem());
        holder.messageText.setText(chatMessage.getMessageText());
        holder.messageUser.setText(chatMessage.getMessageUser());
        holder.messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", chatMessage.getMessageTime()));
        try {//converte o dado string para bitmap
            if(num==1){
                //byte [] encodeByte= Base64.decode(chatMessage.getMessageBitmap(),Base64.DEFAULT);
                //Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                holder.messageBitmap.setVisibility(View.VISIBLE);
                //holder.messageBitmap.setImageBitmap(bitmap);

                storage = FirebaseStorage.getInstance();
                storageRef = storage.getReferenceFromUrl("gs://health-units.appspot.com/");
                mountainsRef = storageRef.child(chatMessage.getMessageBitmap());
                Task<Uri> httpRef = mountainsRef.getDownloadUrl();
                
                final long ONE_MEGABYTE = 64 * 64;
                mountainsRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bitmap= BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        bm = bitmap;
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                    }
                });
                holder.messageBitmap.setImageBitmap(bm);

                //Picasso.with(contextTelaChat).load("AQUI VAI A URL HTTP://...").into(holder.messageBitmap);



                holder.messageBitmap.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent abreMapaComRota = new Intent(contextTelaChat, TelaMaps.class);
                        abreMapaComRota.putExtra("MinhaLocalizacao", origem);
                        abreMapaComRota.putExtra("LatitudeDestino", chatMessage.getLatitude());
                        abreMapaComRota.putExtra("LongitudeDestino", chatMessage.getLongitude());
                        contextTelaChat.startActivity(abreMapaComRota);
                        ((Activity) contextTelaChat).finish();
                    }
                });

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
