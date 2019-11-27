package com.nb.duckhunt.ui;

import android.content.DialogInterface;
import android.content.Intent;
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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nb.duckhunt.Common.Constantes;
import com.nb.duckhunt.R;

import java.util.Random;

public class GameActivity extends AppCompatActivity {

    private TextView tvCounter, tvTimer, tvNick;
    private ImageView ivDuck;
    private int counter;
    private int anchoPantalla, altoPantalla;

    private Random random;

    private boolean gameOver;

    private String id, nick;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        db = FirebaseFirestore.getInstance();

        initGame();
    }

    private void initGame() {
        gameOver = false;
        initViewComponents();
        initPantalla();
        initTimer();
        eventos();
        moveDuck();
    }

    private void initTimer() {
        new CountDownTimer(6000, 1000) {

            public void onTick(long millisUntilFinished) {
                long segundosRestantes = millisUntilFinished / 1000;
                tvTimer.setText(segundosRestantes + "s");
            }

            public void onFinish() {
                tvTimer.setText("0s");
                gameOver = true;
                saveResultFirestore();
                //showGameOver();
            }
        }.start();

    }

    private void saveResultFirestore() {
        db.collection("Players")
                .document(id)
                .update("patos", counter).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                showGameOver();
            }
        });
    }

    private void showGameOver() {
        // 1. Instantiate an <code><a href="/reference/android/app/AlertDialog.Builder.html">AlertDialog.Builder</a></code> with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);

        // 2. Chain together various setter methods to set the dialog characteristics
        String detalles = getString(R.string.detalles_game_over, counter);
        builder.setMessage(detalles)
                .setTitle(R.string.game_over);

        builder.setPositiveButton("Reiniciar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                initGame();
            }
        });

        builder.setNegativeButton("Ver Ranking", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(GameActivity.this, RankingActivity.class);
                startActivity(intent);
            }
        });

        builder.setCancelable(false);

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
        counter = 0;

        tvCounter = findViewById(R.id.tvCounter);
        tvTimer = findViewById(R.id.tvTimer);
        tvNick = findViewById(R.id.tvNick);
        ivDuck = findViewById(R.id.ivDuck);

        //cambiar tipo de fuente
        Typeface typeface = Typeface.createFromAsset(getAssets(), "pixel.ttf");
        tvCounter.setTypeface(typeface);
        tvTimer.setTypeface(typeface);
        tvNick.setTypeface(typeface);

        tvCounter.setText("0");

        // Obtiene el nick del usuario
        Bundle extras = getIntent().getExtras();
        nick = extras.getString(Constantes.EXTRA_NICK);
        id = extras.getString(Constantes.EXTRA_ID);
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
                    }, 300);
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
