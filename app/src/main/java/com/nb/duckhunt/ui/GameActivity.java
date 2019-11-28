package com.nb.duckhunt.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nb.duckhunt.R;
import com.nb.duckhunt.Session.SessionManager;

import java.util.HashMap;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    private TextView tvCounter, tvTimer, tvNick;
    private ImageView ivDuck;
    private int counter;
    private int anchoPantalla, altoPantalla;

    private Random random;

    private boolean gameOver;

    private String id, nick, patos;
    private FirebaseFirestore db;

    // User Session Manager Class
    private SessionManager session;

    // get user data from session
    HashMap<String, String> user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        db = FirebaseFirestore.getInstance();

        session = new SessionManager(getApplicationContext());
        user = session.getUserDetails();

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
        new CountDownTimer(60000, 1000) {

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
        // Si el puntaje supera al anterior registrado, se actualiza el puntaje en firebase
        if (counter > Integer.parseInt(patos)) {
            //Actualiza puntaje en bd
            db.collection("Players")
                    .document(id)
                    .update("patos", counter).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    showGameOver();
                }
            });

            //Sobrescribe los datos de session
            session.createUserSession(id, nick, counter);
        } else
            showGameOver();
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
                finish();
            }
        });

        builder.setCancelable(false);

        //3. Get AlerDialog
        AlertDialog dialog = builder.create();

        //4. Show
        try {
            dialog.show();
        } catch (Exception e) {
            Log.e("GameActivity", "Activity cerrado, no se puede mostrar dialogo");
        }

    }

    private void initPantalla() {
        // Obtiene tamaño de pantalla
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

        // Obtiene el nick del usuario a traves de shared pref
        nick = user.get(SessionManager.KEY_NICK);
        id = user.get(SessionManager.KEY_ID);
        patos = user.get(SessionManager.KEY_DUCKS_HUNTED);
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
        int minX = 0;
        int maximoX = (anchoPantalla - (ivDuck.getWidth() + minX));
        int minY = 100;
        int maxY = (altoPantalla - (ivDuck.getHeight() + minY));


        //numeros aleatorios
        int randomX = random.nextInt(maximoX);
        int randomY = random.nextInt(maxY);

        ivDuck.setX(randomX);
        ivDuck.setY(randomY);
        ivDuck.setClickable(true);
    }
}
