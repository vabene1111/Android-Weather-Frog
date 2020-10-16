package de.droidenschmiede.weather;

import java.util.Random;

public class Hilfsklassen {

    public static int getRandomIntBetween(int min, int max){
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }
}
