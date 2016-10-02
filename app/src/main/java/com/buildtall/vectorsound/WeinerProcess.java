package com.buildtall.vectorsound;

import java.util.Random;

public class WeinerProcess {
    private Random r = new Random();
    private float step = 0.1f;

    public void setStep(float step) {this.step = clamp(step);}

    private float clamp(float d) {
        if (d > 1.0) return 1.0f;
        if (d < -1.0) return -1.0f;
        return d;
    }

    private float getWeinerFloat(float t) {
        return clamp((((float)r.nextGaussian() * step) + t));
    }

    public float[] getWeinerFloats(int numBytes, float initialValue) {
        if (numBytes <= 0) throw new RuntimeException("cannot produce zero or fewer bytes");
        float[] answer = new float[numBytes];
        float value = clamp(initialValue);
        for(int i = 0; i < numBytes; i++) {
            value = getWeinerFloat(value);
            answer[i] = value;
        }
        return answer;
    }
}

