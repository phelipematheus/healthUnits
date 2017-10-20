package com.projetomobile.jpm.healthunits.telas.chat;


import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.View;
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

    private static int SIGN_IN_REQUEST_CODE = 1;
    private FirebaseListAdapter<ChatMessage> firebaseListAdapter;
    RelativeLayout activity_main;

    //Add Emojicon
    EmojiconEditText emojiconEditText;
    ImageView emojiButton,submitButton;
    EmojIconActions emojIconActions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_chat);

        activity_main = (RelativeLayout)findViewById(R.id.activity_main);

        //Add Emoji
        emojiButton = (ImageView)findViewById(R.id.emoji_button);
        submitButton = (ImageView)findViewById(R.id.submit_button);
        emojiconEditText = (EmojiconEditText)findViewById(R.id.emojicon_edit_text);
        emojIconActions = new EmojIconActions(getApplicationContext(),activity_main,emojiButton,emojiconEditText);
        emojIconActions.ShowEmojicon();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference().push().setValue(new ChatMessage(emojiconEditText.getText().toString(),
                        FirebaseAuth.getInstance().getCurrentUser().getEmail()));// Base de dados do firebase recebe a mensagem e o usuário que enviou
                emojiconEditText.setText("");// Zero o campo mensagem
                emojiconEditText.requestFocus();// Volto o cursor para o campo de mensagem
            }
        });

        //Checa se não está logado e vai para página de login
        if(FirebaseAuth.getInstance().getCurrentUser() == null)
        {
            Toast.makeText(TelaChat.this,"Não conseguiu pegar o user do firebase", Toast.LENGTH_SHORT).show();
            //startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(),SIGN_IN_REQUEST_CODE);
        }// startActivityForResult chama uma nova atividade e fica no aguardo do retorno(variaveis, valores) dessa nova atividade
        else
        {
            Snackbar.make(activity_main,"Welcome "+FirebaseAuth.getInstance().getCurrentUser().getEmail(),Snackbar.LENGTH_SHORT).show();
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

                //Get references to the views of list_item.xml
                TextView messageText, messageUser, messageTime;
                messageText = (EmojiconTextView) v.findViewById(R.id.message_text);
                messageUser = (TextView) v.findViewById(R.id.message_user);
                messageTime = (TextView) v.findViewById(R.id.message_time);

                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser());
                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", model.getMessageTime()));

            }
        };
        adapter = new MyAdapterChat(firebaseListAdapter);
        listOfMessage.setAdapter(adapter);
    }
}
