package com.nb.duckhunt.ui;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.nb.duckhunt.R;
import com.nb.duckhunt.Session.SessionManager;
import com.nb.duckhunt.models.Player;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    private EditText etNick;
    private Button btnStart;
    private TextView tvNick;

    private String nick, id;

    // User Session Manager Class
    private SessionManager session;

    // get user data from session
    HashMap<String, String> user;

    /**
     * Conexion a la base de datos
     */
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Instanciar conexion a db
        db = FirebaseFirestore.getInstance();

        session = new SessionManager(getApplicationContext());


        etNick = findViewById(R.id.etNick);
        btnStart = findViewById(R.id.btnStart);
        tvNick = findViewById(R.id.tvNick);

        //cambiar tipo de fuente
        Typeface typeface = Typeface.createFromAsset(getAssets(), "pixel.ttf");
        etNick.setTypeface(typeface);
        btnStart.setTypeface(typeface);
        tvNick.setTypeface(typeface);
        TextView tv = findViewById(R.id.textView);
        tv.setTypeface(typeface);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!session.isLogin()) {
                    nick = etNick.getText().toString();
                    if (nick.isEmpty()) {
                        etNick.setError("Coloque un nombre valido");
                    } else
                        addNickAndStart();
                } else
                    addNickAndStart();
            }
        });

        if (session.isLogin()) {
            user = session.getUserDetails();
            nick = user.get(SessionManager.KEY_NICK);
            id = user.get(SessionManager.KEY_ID);
            etNick.setVisibility(View.GONE);
            tvNick.setVisibility(View.VISIBLE);
            tvNick.setText(nick);
            tv.setVisibility(View.VISIBLE);
        }
    }

    private void addNickAndStart() {
        if (session.isLogin()) {
            startGame();
        } else {
            //Consulta si el nick ya existe
            db.collection("Players").whereEqualTo("nickname", nick).get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if (queryDocumentSnapshots.size() > 0) {
                                etNick.setError(getString(R.string.nick_error));
                            } else {
                                addNickToFirestore();
                            }
                        }
                    });

        }
    }

    private void addNickToFirestore() {

        Player player = new Player(nick, 0);

        db.collection("Players")
                .add(player)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        etNick.setText("");

                        String id = documentReference.getId();
                        session.createUserSession(id, nick, 0);


                    }
                });
    }

    private void startGame() {
        Intent i = new Intent(LoginActivity.this, GameActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }
}
