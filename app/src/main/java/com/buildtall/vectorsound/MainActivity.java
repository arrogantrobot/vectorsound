package com.buildtall.vectorsound;

import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.logging.Logger;

// heavy borrowing from https://stackoverflow.com/questions/6179392/audiotrack-in-streaming-mode-mode-streaming


public class MainActivity extends AppCompatActivity {
    Logger logger;
    SoundScape soundScape;

    private void togglePlay(View view){
        if (soundScape.isStopped()) {
            start();
        } else {
            stop();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        logger = Logger.getLogger("vectorSound");
        logger.info("startup");
        Button startStopButton = (Button) findViewById(R.id.startStop);
        startStopButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                togglePlay(v);
            }
        });
    }

    private void start() {
        TextView text = (TextView)findViewById(R.id.startStop);
        text.setText("pause");
        soundScape.play();
        logger.info("play");
    }

    private void stop() {
        TextView text = (TextView)findViewById(R.id.startStop);
        text.setText("play");
        soundScape.pause();
        logger.info("pause");
    }

}
