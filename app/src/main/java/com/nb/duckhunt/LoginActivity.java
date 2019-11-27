package com.nb.duckhunt;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.nb.duckhunt.Common.Constantes;

public class LoginActivity extends AppCompatActivity {

    private EditText etNick;
    private Button btnStart;

    private String nick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etNick = findViewById(R.id.etNick);
        btnStart = findViewById(R.id.btnStart);

        //cambiar tipo de fuente
        Typeface typeface = Typeface.createFromAsset(getAssets(),"pixel.ttf");
        etNick.setTypeface(typeface);
        btnStart.setTypeface(typeface);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nick = etNick.getText().toString();
                if (nick.isEmpty()) {
                    etNick.setError("Coloque un nombre valido");
                } else {
                    Intent i = new Intent(LoginActivity.this, GameActivity.class);
                    i.putExtra(Constantes.EXTRA_NICK, nick);
                    startActivity(i);
                }
            }
        });
    }
}
