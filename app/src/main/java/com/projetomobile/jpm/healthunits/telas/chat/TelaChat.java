package com.projetomobile.jpm.healthunits.telas.chat;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.projetomobile.jpm.healthunits.R;
import com.projetomobile.jpm.healthunits.adaptadores.MyAdapterChat;
import com.projetomobile.jpm.healthunits.valueobject.ChatMessage;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;
import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;

public class TelaChat extends AppCompatActivity {

    private Bitmap bm;
    private static int SIGN_IN_REQUEST_CODE = 1;
    private FirebaseListAdapter<ChatMessage> firebaseListAdapter;
    RelativeLayout activity_main;

    //Add Emojicon
    EmojiconEditText emojiconEditText;
    ImageView emojiButton,submitButton,imageViewRow;
    EmojIconActions emojIconActions;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_chat);

        if(getIntent().hasExtra("BITMAP")){
            Bundle extras = getIntent().getExtras();
            bm = (Bitmap) extras.get("BITMAP");
            imageViewRow.setVisibility(View.VISIBLE);

        }

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        activity_main = (RelativeLayout)findViewById(R.id.activity_main);

        //Add Emoji
        imageViewRow = (ImageView) findViewById(R.id.image_user);
        emojiButton = (ImageView)findViewById(R.id.emoji_button);
        submitButton = (ImageView)findViewById(R.id.submit_button);
        emojiconEditText = (EmojiconEditText)findViewById(R.id.emojicon_edit_text);
        emojIconActions = new EmojIconActions(getApplicationContext(),activity_main,emojiButton,emojiconEditText);
        emojIconActions.ShowEmojicon();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(FirebaseAuth.getInstance().getCurrentUser().getEmail() == null) {
                    FirebaseDatabase.getInstance().getReference().push().setValue(new ChatMessage(emojiconEditText.getText().toString(),
                            FirebaseAuth.getInstance().getCurrentUser().getDisplayName(), bm));
                }else {
                    FirebaseDatabase.getInstance().getReference().push().setValue(new ChatMessage(emojiconEditText.getText().toString(),
                            FirebaseAuth.getInstance().getCurrentUser().getEmail(), bm));// Base de dados do firebase recebe a mensagem e o usuário que enviou
                }
                emojiconEditText.setText("");// Zero o campo mensagem
                emojiconEditText.requestFocus();// Volto o cursor para o campo de mensagem
            }
        });

        //Checa se não está logado e vai para página de login
        if(FirebaseAuth.getInstance().getCurrentUser() == null)
        {
            Toast.makeText(TelaChat.this,"Não conseguiu pegar o user do firebase", Toast.LENGTH_SHORT).show();
            displayChatMessage();
        }else{
            if(FirebaseAuth.getInstance().getCurrentUser().getEmail() == null) {
                Snackbar.make(activity_main,"Welcome "+FirebaseAuth.getInstance().getCurrentUser().getDisplayName(),Snackbar.LENGTH_SHORT).show();
            }else{
                Snackbar.make(activity_main,"Welcome "+FirebaseAuth.getInstance().getCurrentUser().getEmail(),Snackbar.LENGTH_SHORT).show();
            }
            //Load content
            displayChatMessage();
        }
    }

    private void displayChatMessage() {

        RecyclerView listOfMessage = (RecyclerView)findViewById(R.id.list_of_message);
        RecyclerView.Adapter adapter;

        listOfMessage.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        listOfMessage.setLayoutManager(mLayoutManager);

        firebaseListAdapter = new FirebaseListAdapter<ChatMessage>(this,ChatMessage.class,R.layout.row_chat, FirebaseDatabase.getInstance().getReference())
        {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {

                //Get references to the views of row_chat.xml
                TextView messageText, messageUser, messageTime;
                ImageView messageImage;
                messageText = (EmojiconTextView) v.findViewById(R.id.message_text);
                messageUser = (TextView) v.findViewById(R.id.message_user);
                messageTime = (TextView) v.findViewById(R.id.message_time);
                messageImage = (ImageView) v.findViewById(R.id.image_user);

                messageImage.setImageBitmap(model.getBm());
                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser());
                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", model.getMessageTime()));

            }
        };
        adapter = new MyAdapterChat(firebaseListAdapter);
        listOfMessage.setAdapter(adapter);
    }
}
