package com.syd.tank;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.syd.tank.domain.GameView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GameView game = findViewById(R.id.game);
        Thread thread = new Thread(game);
        thread.start();
    }
}
