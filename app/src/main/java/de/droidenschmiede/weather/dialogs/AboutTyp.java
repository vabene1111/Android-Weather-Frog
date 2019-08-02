package de.droidenschmiede.weather.dialogs;

public enum AboutTyp {

    INFO, IMPRESSUM;

    public static AboutTyp fromInteger(int x) {
        switch(x) {
            case 0:
                return INFO;
            case 1:
                return IMPRESSUM;
        }
        return null;
    }
}
