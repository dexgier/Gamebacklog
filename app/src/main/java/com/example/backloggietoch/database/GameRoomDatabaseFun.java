package com.example.backloggietoch.database;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import com.example.backloggietoch.model.Game;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

//Class that helps us with all the database commands
public class GameRoomDatabaseFun {

    private GameRoomDatabase gameRoomDatabase;
    private GameDao gameDao;
    private LiveData<List<Game>> gamesList;
    private Executor executor = Executors.newSingleThreadExecutor();

    public GameRoomDatabaseFun(Context context) {
        gameRoomDatabase = GameRoomDatabase.getDatabase(context);
        gameDao = gameRoomDatabase.gameDao();
        gamesList = gameDao.getAllGames();
    }

    public LiveData<List<Game>> getAllGames() {return gamesList;}

    public void insert(final Game game) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                gameDao.insert(game);
            }
        });
    }

    public void insertAll(final List<Game> games) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                gameDao.insert(games);
            }
        });
    }

    public void update(final Game game) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                gameDao.update(game);
            }
        });
    }

    public void delete(final Game game) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                gameDao.delete(game);
            }
        });
    }

    public void deleteAll(final List<Game> games) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                gameDao.delete(games);
            }
        });
    }
}
