package com.example.backloggietoch.model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.backloggietoch.database.GameRoomDatabaseFun;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private GameRoomDatabaseFun gameRoomDatabaseFun;
    private LiveData<List<Game>> gamesList;

    public MainViewModel(@NonNull Application application) {
        super(application);
        gameRoomDatabaseFun = new GameRoomDatabaseFun(application.getApplicationContext());
        gamesList = gameRoomDatabaseFun.getAllGames();
    }

    public LiveData<List<Game>> getGamesList() {return gamesList;}

    public void insert(Game game) {gameRoomDatabaseFun.insert(game);}
    public void update(Game game) {gameRoomDatabaseFun.update(game);}
    public void delete(Game game) {gameRoomDatabaseFun.delete(game);}
    public void deleteAll(List<Game> gamesList) {gameRoomDatabaseFun.deleteAll(gamesList);}
    public void insertAll(List<Game> gamesList) {gameRoomDatabaseFun.insertAll(gamesList);}

}
