package com.example.backloggietoch.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.example.backloggietoch.R;
import com.example.backloggietoch.model.Game;
import com.example.backloggietoch.model.MainViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecyclerView.OnItemTouchListener {

    private List<Game> gamesList;
    private GameAdapter adapter;
    private RecyclerView gamesRv;
    private GestureDetector gDetector;
    private MainViewModel mvModel;

    public static final String NEW = "New";
    public static final int NEWGAMECODE = 1234;

    public static final String UPDATE = "Update";
    public static final int UPDATEGAMECODE = 4321;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gamesList = new ArrayList<>();
        initMainViewModel();
        initTools();
        initFabButton();
        initRV();

    }

    private void initMainViewModel() {
        mvModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mvModel.getGamesList().observe(this, new Observer<List<Game>>() {
            @Override
            public void onChanged(@Nullable List<Game> games) {
                gamesList = games;
                updateUI();
            }
        });
    }

    private void initRV() {
        adapter = new GameAdapter(gamesList);
        gamesRv = findViewById(R.id.backlogView);
        gamesRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        gamesRv.setAdapter(adapter);
        gamesRv.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));

        gDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });

        touchHelperHandler();
        gamesRv.addOnItemTouchListener(this);
    }

    private void touchHelperHandler() {
        ItemTouchHelper touchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                int position = (viewHolder.getAdapterPosition());
                final Game STORE_GAME = gamesList.get(position);
                mvModel.delete(gamesList.get(position));
                gamesList.remove(position);
                adapter.notifyItemRemoved(position);

                Snackbar undoSnackBar = Snackbar.make(viewHolder.itemView, "Deleted: " + STORE_GAME.getTitle(), Snackbar.LENGTH_LONG);
                undoSnackBar.setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mvModel.insert(STORE_GAME);
                    }
                });
                undoSnackBar.show();
            }
        });
        touchHelper.attachToRecyclerView(gamesRv);
    }

    private void initTools() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.deleteList) {
            deleteAll(gamesList);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteAll(List<Game> gamesList) {
        if(gamesList.size() > 0) {
            final List<Game> TEMPLIST = gamesList;
            mvModel.deleteAll(gamesList);

            Snackbar undoAll = Snackbar.make(gamesRv, "Deleted ALL", Snackbar.LENGTH_LONG);
            undoAll.setAction("Undo", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mvModel.insertAll(TEMPLIST);
                }
            });
            undoAll.show();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
        View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
        if (child != null){
            int adapterPosition = recyclerView.getChildAdapterPosition(child);
            if (gDetector.onTouchEvent(motionEvent)){
                Intent intent = new Intent(MainActivity.this, AddNewGame.class);
                intent.putExtra(UPDATE, gamesList.get(adapterPosition));
                startActivityForResult(intent, UPDATEGAMECODE);
            }
        }
        return false;
    }

    @Override
    public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean b) {

    }

    private void updateUI() {
        if (adapter == null) {
            adapter = new GameAdapter(gamesList);
            gamesRv.setAdapter(adapter);
        } else {
            adapter.updateGamesList(gamesList);
        }
    }

    private void initFabButton() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddNewGame.class);
                startActivityForResult(intent, NEWGAMECODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == NEWGAMECODE) {
            if(resultCode == RESULT_OK) {
                Game newGame = data.getParcelableExtra(MainActivity.NEW);
                newGame.setDate(getDate());
                mvModel.insert(newGame);
            }
        } else if(requestCode == UPDATEGAMECODE){
            if(resultCode == RESULT_OK) {
                Game updateGame = data.getParcelableExtra(MainActivity.UPDATE);
                updateGame.setDate(getDate());
                mvModel.update(updateGame);
            }
        }
    }

    private String getDate(){
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        return format.format(Calendar.getInstance().getTime());
    }
}
