package com.buildtall.vectorsound;

import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioTrack;

public class SoundScape {
    AudioTrack at;
    Thread noiseThread;
    boolean isStopped = true;
    int iterations;
    int SAMPLE_RATE = 8000;

    WeinerProcess wp;

    Runnable noiseGenerator = new Runnable() {

        public void run() {
            Thread.currentThread().setPriority(Thread.NORM_PRIORITY);
            float initialValue = 0f;
            wp = new WeinerProcess();
            while(!isStopped) {
                float[] brownNoise = wp.getWeinerFloats(iterations, initialValue);
                at.write(brownNoise, 0, iterations, AudioTrack.WRITE_BLOCKING);
                initialValue = brownNoise[iterations - 1];
            }
        }
    };

    public SoundScape(int iterationsPerRun) {
        this.iterations = iterationsPerRun;
        at = new AudioTrack.Builder()
            .setAudioAttributes(new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ALARM)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build())
            .setAudioFormat(new AudioFormat.Builder()
                .setEncoding(AudioFormat.ENCODING_PCM_FLOAT)
                .setSampleRate(SAMPLE_RATE)
                .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                .build())
            .setBufferSizeInBytes(iterations*2)
            .setTransferMode(AudioTrack.MODE_STREAM)
            .build();
    }

    public void setIterations(int iterations) { this.iterations = iterations; }

    public boolean isStopped() { return isStopped; }

    public void pause() {
        isStopped = true;
        at.stop();

    }

    public void play() {
        isStopped = false;
        at.play();
        noiseThread = new Thread(noiseGenerator);
        noiseThread.start();
    }

    public void setStep(float step) {
        wp.setStep(step);
    }
}
