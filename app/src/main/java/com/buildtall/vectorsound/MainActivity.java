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
    AudioTrack at;
    Thread noiseThread;
    boolean isStopped = true;
    int iterations = 100;


    Runnable noiseGenerator = new Runnable() {
        public void run() {
            Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
            int initialValue = 128;
            while(!isStopped) {
                byte[] brownNoise = WeinerProcess.getWeinerBytes(iterations, initialValue);
                at.write(brownNoise, 0, iterations);
                byte newStart = brownNoise[iterations - 1];
                initialValue = newStart < 0 ? 256 + newStart : newStart;
            }
        }
    };

    private void togglePlay(View view){
        if (isStopped) {
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
        at = new AudioTrack(AudioManager.STREAM_MUSIC, 44100, AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_8BIT, 44100, AudioTrack.MODE_STREAM);
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
        play();
    }

    private void stop() {
        TextView text = (TextView)findViewById(R.id.startStop);
        text.setText("play");
        pause();
    }

    private void pause() {
        isStopped = true;
        at.stop();
        logger.info("stopped");
    }

    private void play() {
        isStopped = false;
        at.play();
        noiseThread = new Thread(noiseGenerator);
        noiseThread.start();
        logger.info("playing");
    }
}
