package com.nb.duckhunt;

import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.nb.duckhunt.Common.Constantes;

public class GameActivity extends AppCompatActivity {

    private TextView tvCounter, tvTimer, tvNick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        tvCounter = findViewById(R.id.tvCounter);
        tvTimer = findViewById(R.id.tvTimer);
        tvNick = findViewById(R.id.tvNick);

        //cambiar tipo de fuente
        Typeface typeface = Typeface.createFromAsset(getAssets(),"pixel.ttf");
        tvCounter.setTypeface(typeface);
        tvTimer.setTypeface(typeface);
        tvNick.setTypeface(typeface);

        // Obtiene el nick del usuario
        Bundle extras = getIntent().getExtras();
        String nick = extras.getString(Constantes.EXTRA_NICK);
        tvNick.setText(nick);

    }
}
