package com.nb.duckhunt.ui;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.nb.duckhunt.R;
import com.nb.duckhunt.models.Player;

import java.util.List;

public class MyPlayerRankingRecyclerViewAdapter extends RecyclerView.Adapter<MyPlayerRankingRecyclerViewAdapter.ViewHolder> {

    private final List<Player> mValues;
    private Typeface typeface;

    public MyPlayerRankingRecyclerViewAdapter(List<Player> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_playerranking, parent, false);

        typeface = Typeface.createFromAsset(view.getContext().getAssets(), "pixel.ttf");

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        int pos = position + 1;

        holder.mItem = mValues.get(position);
        holder.tvPosition.setText(pos + "º");
        holder.tvDucks.setText(mValues.get(position).getDucksHunted() + "");
        holder.tvNick.setText(mValues.get(position).getNickname());

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView tvPosition;
        public final TextView tvDucks;
        public final TextView tvNick;
        public Player mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            tvPosition = view.findViewById(R.id.tvPosition);
            tvDucks = view.findViewById(R.id.tvDucks);
            tvNick = view.findViewById(R.id.tvNick);

            tvPosition.setTypeface(typeface);
            tvDucks.setTypeface(typeface);
            tvNick.setTypeface(typeface);

        }

    }
}
