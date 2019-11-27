package com.nb.duckhunt;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.nb.duckhunt.Common.Constantes;
import com.nb.duckhunt.models.Player;

public class LoginActivity extends AppCompatActivity {

    private EditText etNick;
    private Button btnStart;

    private String nick;

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

        etNick = findViewById(R.id.etNick);
        btnStart = findViewById(R.id.btnStart);

        //cambiar tipo de fuente
        Typeface typeface = Typeface.createFromAsset(getAssets(), "pixel.ttf");
        etNick.setTypeface(typeface);
        btnStart.setTypeface(typeface);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nick = etNick.getText().toString();
                if (nick.isEmpty()) {
                    etNick.setError("Coloque un nombre valido");
                } else {
                    addNickAndStart();
                }
            }
        });
    }

    private void addNickAndStart() {

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

    private void addNickToFirestore() {

        Player player = new Player(nick, 0);

        db.collection("Players")
                .add(player)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        etNick.setText("");
                        Intent i = new Intent(LoginActivity.this, GameActivity.class);
                        i.putExtra(Constantes.EXTRA_NICK, nick);
                        i.putExtra(Constantes.EXTRA_ID, documentReference.getId());
                        startActivity(i);
                    }
                });
    }
}
