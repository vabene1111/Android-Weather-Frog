package de.droidenschmiede.droidenbase;

import java.util.Random;

public class NumberOps {

    /**
     * Get a randomized integer value between min and max
     * @param min
     * @param max
     * @return
     */
    public static int getRandomIntBetween(int min, int max){
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }
}
