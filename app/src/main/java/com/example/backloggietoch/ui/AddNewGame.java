package com.example.backloggietoch.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;


import com.example.backloggietoch.R;
import com.example.backloggietoch.model.Game;

public class AddNewGame extends AppCompatActivity {

    private EditText title;
    private EditText platform;
    private Spinner statusSelector;
    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_game);
        initFabButton();
        initTools();

        title = findViewById(R.id.titleEdit);
        platform = findViewById(R.id.platformEdit);
        statusSelector = findViewById(R.id.statusSpinner);

        game = getIntent().getParcelableExtra(MainActivity.UPDATE);
        if (game != null) {
            title.setText(game.getTitle());
            platform.setText(game.getPlatform());
            statusSelector.setSelection(((ArrayAdapter) statusSelector.getAdapter()).getPosition(game.getStatus()));
            this.setTitle(R.string.title_activity_add_game_update);
        } else {
            this.setTitle(R.string.title_activity_add_game_new);
        }
    }

    private void initFabButton() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String titleText = title.getText().toString();
                String platformText = platform.getText().toString();
                String statusText = statusSelector.getSelectedItem().toString();

                if (game != null) {
                    if (!TextUtils.isEmpty(titleText) && !TextUtils.isEmpty(platformText)) {
                        game.setTitle(titleText);
                        game.setPlatform(platformText);
                        game.setStatus(statusText);

                        Intent resultIntent = new Intent();
                        resultIntent.putExtra(MainActivity.UPDATE, game);
                        setResult(Activity.RESULT_OK, resultIntent);
                        finish();
                    } else {
                        Snackbar.make(v, "Please fill in all the fields", Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    if (!TextUtils.isEmpty(titleText) && !TextUtils.isEmpty(platformText)) {
                        game = new Game(titleText, platformText, statusText);

                        Intent resultIntent = new Intent();
                        resultIntent.putExtra(MainActivity.NEW, game);
                        setResult(Activity.RESULT_OK, resultIntent);
                        finish();
                    } else
                        Snackbar.make(v, "Please fill in all the fields!", Snackbar.LENGTH_LONG).show();
                }
            }

        });
    }
    private void initTools() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }
}
