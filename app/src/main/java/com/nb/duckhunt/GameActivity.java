package com.nb.duckhunt;

import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.nb.duckhunt.Common.Constantes;

import java.util.Random;

public class GameActivity extends AppCompatActivity {

    private TextView tvCounter, tvTimer, tvNick;
    private ImageView ivDuck;
    private int counter;
    private int anchoPantalla, altoPantalla;

    Random random;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        initViewComponents();
        initPantalla();
        eventos();
    }

    private void initPantalla() {
        // Obtiene tamaño de pantall
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        anchoPantalla = size.x;
        altoPantalla = size.y;

        //objeto para generar numeros random
        random = new Random();
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
                ivDuck.setImageResource(R.drawable.duck_clicked);
                ivDuck.setClickable(false);

                // Ejecutar codigo segundos despues
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ivDuck.setImageResource(R.drawable.duck);
                        moveDuck();
                    }
                }, 500);
            }
        });
    }

    private void moveDuck() {
        int min = 0;
        int maximoX = anchoPantalla - ivDuck.getWidth();
        int maxY = altoPantalla - ivDuck.getHeight();

        //numeros aleatorios
        int randomX = random.nextInt(((maximoX - min) + 1) + min);
        int randomY = random.nextInt(((maxY - min) + 1) + min);

        ivDuck.setX(randomX);
        ivDuck.setY(randomY);
        ivDuck.setClickable(true);
    }
}
