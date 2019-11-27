package com.nb.duckhunt.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.nb.duckhunt.R;
import com.nb.duckhunt.models.Player;

import java.util.ArrayList;
import java.util.List;


public class PlayerRankingFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;

    private RecyclerView recyclerView;

    private List<Player> players;
    private MyPlayerRankingRecyclerViewAdapter adapter;

    private FirebaseFirestore db;

    public PlayerRankingFragment() {
    }

    @SuppressWarnings("unused")
    public static PlayerRankingFragment newInstance(int columnCount) {
        PlayerRankingFragment fragment = new PlayerRankingFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = FirebaseFirestore.getInstance();

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playerranking_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }


            /**
             * Obtiene coleccion de db y ordena de forma descendente con un limite de 10 elementos
             * Luego instancia y llena la lista combirtiendo cada document en un objeto Player
             */
            db.collection("Players")
                    .orderBy("patos", Query.Direction.DESCENDING)
                    .limit(10)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            players = new ArrayList<>();
                            for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                //Player playerItem = documentSnapshot.toObject(Player.class);
                                String nick = documentSnapshot.get("nickname").toString();
                                int patos = Integer.parseInt(documentSnapshot.get("patos").toString());
                                Player playerItem = new Player(nick, patos);
                                players.add(playerItem);
                                adapter = new MyPlayerRankingRecyclerViewAdapter(players);
                                recyclerView.setAdapter(adapter);
                            }
                        }
                    });


        }
        return view;
    }
}
