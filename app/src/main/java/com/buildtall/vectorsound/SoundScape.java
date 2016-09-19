package com.buildtall.vectorsound;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

public class SoundScape {
    AudioTrack at;
    Thread noiseThread;
    boolean isStopped = true;
    int iterations;

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

    public SoundScape(int iterationsPerRun) {
        this.iterations = iterationsPerRun;
        at = new AudioTrack(AudioManager.STREAM_MUSIC, 44100, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_8BIT, 44100, AudioTrack.MODE_STREAM);
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
}
