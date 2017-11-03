package com.projetomobile.jpm.healthunits.telas.chat;


import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.projetomobile.jpm.healthunits.R;
import com.projetomobile.jpm.healthunits.adaptadores.MyAdapterChat;
import com.projetomobile.jpm.healthunits.dao.ConfiguracaoFirebase;
import com.projetomobile.jpm.healthunits.valueobject.ChatMessage;

import java.util.ArrayList;
import java.util.List;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;

import static com.projetomobile.jpm.healthunits.telas.chat.TelaLocaisPreChat.clicou;

public class TelaChat extends AppCompatActivity {

    private String imageB64;
    private String temImagem;
    private static int SIGN_IN_REQUEST_CODE = 1;
    RelativeLayout activity_main;

    //Add Emojicon
    EmojiconEditText emojiconEditText;
    ImageView emojiButton,submitButton;
    EmojIconActions emojIconActions;

    private List<ChatMessage> items = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_chat);

        if(getIntent().hasExtra("ImageB64")){
            Bundle extras = getIntent().getExtras();
            imageB64 = (String) extras.get("ImageB64");
        }

        if(clicou == true){
            temImagem = "1";
        }else{
            temImagem = "2";
        }

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

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
                if(FirebaseAuth.getInstance().getCurrentUser().getEmail() == null) {
                    FirebaseDatabase.getInstance().getReference().push().setValue(new ChatMessage(emojiconEditText.getText().toString(),
                            FirebaseAuth.getInstance().getCurrentUser().getDisplayName(), imageB64, temImagem));
                }else {
                    FirebaseDatabase.getInstance().getReference().push().setValue(new ChatMessage(emojiconEditText.getText().toString(),
                            FirebaseAuth.getInstance().getCurrentUser().getEmail(), imageB64, temImagem));// Base de dados do firebase recebe a mensagem e o usuário que enviou
                }
                clicou = false;
                imageB64 = null;
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

        DatabaseReference ref = ConfiguracaoFirebase.getFirebase();
        ref.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {
                items.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    ChatMessage chatMessage = postSnapshot.getValue(ChatMessage.class);
                    items.add(chatMessage);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(TelaChat.this,"Não foi possivel conectar a base de dados! ",Toast.LENGTH_LONG).show();
            }


        });

        adapter = new MyAdapterChat(items);
        listOfMessage.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
