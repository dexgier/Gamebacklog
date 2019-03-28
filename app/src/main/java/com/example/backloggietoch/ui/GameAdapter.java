package com.example.backloggietoch.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.backloggietoch.R;
import com.example.backloggietoch.model.Game;

import java.util.List;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.ViewHolder> {

    private List<Game> gamesList;
    public GameAdapter(List<Game> gamesList) {this.gamesList = gamesList;}

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.activity_game_cardview, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Game game = gamesList.get(i);
        viewHolder.title.setText(game.getTitle());
        viewHolder.platform.setText(game.getPlatform());
        viewHolder.status.setText(game.getStatus());
        viewHolder.date.setText(game.getDate());
    }

    @Override
    public int getItemCount() {
        return gamesList.size();
    }

    public void updateGamesList(List<Game> newGamesList) {
        gamesList = newGamesList;
        if(newGamesList != null) {
            this.notifyDataSetChanged();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView platform;
        private TextView status;
        private TextView date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.titleLabel);
            platform = itemView.findViewById(R.id.platformLabel);
            status = itemView.findViewById(R.id.statusLabel);
            date = itemView.findViewById(R.id.dateLabel);
        }
    }
}
