package com.nb.duckhunt;

import android.content.DialogInterface;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.nb.duckhunt.Common.Constantes;

import java.util.Random;

public class GameActivity extends AppCompatActivity {

    private TextView tvCounter, tvTimer, tvNick;
    private ImageView ivDuck;
    private int counter;
    private int anchoPantalla, altoPantalla;

    private Random random;

    private boolean gameOver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        initGame();
    }

    private void initGame() {
        gameOver = false;
        initViewComponents();
        initPantalla();
        initTimer();
        eventos();
    }

    private void initTimer() {
        new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                long segundosRestantes = millisUntilFinished / 1000;
                tvTimer.setText(segundosRestantes + "s");
            }

            public void onFinish() {
                tvTimer.setText("0s");
                gameOver = true;
                showGameOver();
            }
        }.start();

    }

    private void showGameOver() {
        // 1. Instantiate an <code><a href="/reference/android/app/AlertDialog.Builder.html">AlertDialog.Builder</a></code> with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);

        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage(R.string.detalles_game_over + counter + "patos")
                .setTitle(R.string.game_over);

        builder.setPositiveButton("Reiniciar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        //3. Get AlerDialog
        AlertDialog dialog = builder.create();

        //4. Show
        dialog.show();
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
                if (!gameOver) {
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
