package com.nb.duckhunt;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.nb.duckhunt.Common.Constantes;

public class GameActivity extends AppCompatActivity {

    private TextView tvCounter, tvTimer, tvNick;
    private ImageView ivDuck;
    private int counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        initViewComponents();
        eventos();
    }

    private void initViewComponents() {
        tvCounter = findViewById(R.id.tvCounter);
        tvTimer = findViewById(R.id.tvTimer);
        tvNick = findViewById(R.id.tvNick);
        ivDuck = findViewById(R.id.ivDuck);

        //cambiar tipo de fuente
        Typeface typeface = Typeface.createFromAsset(getAssets(), "pixel.ttf");
        tvCounter.setTypeface(typeface);
        tvTimer.setTypeface(typeface);
        tvNick.setTypeface(typeface);

        // Obtiene el nick del usuario
        Bundle extras = getIntent().getExtras();
        String nick = extras.getString(Constantes.EXTRA_NICK);
        tvNick.setText(nick);

    }

    private void eventos() {
        ivDuck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counter++;
                tvCounter.setText(counter + "");
            }
        });
    }
}
