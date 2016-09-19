package com.buildtall.vectorsound;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.Math.exp;
import static java.lang.Math.sqrt;

public class WeinerProcess {
    private static double getWeinerDouble(double t) {
        return ((new Random()).nextGaussian() + t) % 256;
    }

    public static byte[] getWeinerBytes(int numBytes, int initialValue) {
        byte[] answer = new byte[numBytes];
        double value = (float)initialValue;
        for(int i = 0; i < numBytes; i++) {
            value = getWeinerDouble(value);
            answer[i] = (byte)Math.round(value);
        }
        return answer;
    }
}

