package de.droidenschmiede.droidenbase;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class StringOps {

    public static String getFormatedTime(Calendar calendar, Context context){
        Date date = new Date(calendar.getTimeInMillis());
        return android.text.format.DateFormat.getTimeFormat(context).format(date);
    }

    public static String getFormatedDate(Calendar calendar, Context context){
        Date date = new Date(calendar.getTimeInMillis());
        return android.text.format.DateFormat.getDateFormat(context).format(date);
    }

    public static String getFormatedLongDate(Calendar calendar, Context context, ArrayList<String> weekdays){

        return getWochentag(calendar, context, weekdays) + ", " + getFormatedDate(calendar, context);
    }

    public static String getFormatedLongXLDate(Calendar calendar, Context context, ArrayList<String> weekdays){

        Date date = new Date(calendar.getTimeInMillis());
        return getWochentag(calendar, context, weekdays) + ", " + android.text.format.DateFormat.getLongDateFormat(context).format(date);
    }

    /**
     * Returns String of weekday
     * @param calendar
     * @param context
     * @param weekdays a string-array of weekdays starting from monday to sunday (0-6)
     * @return
     */
    public static String getWochentag(Calendar calendar, Context context, ArrayList<String> weekdays){

        String wochentag = "";
        Date date = new Date(calendar.getTimeInMillis());
        int weekday = calendar.get(Calendar.DAY_OF_WEEK);

        switch (weekday){

            case Calendar.SUNDAY:
                wochentag = weekdays.get(6);
                break;

            case Calendar.MONDAY:
                wochentag = weekdays.get(0);
                break;

            case Calendar.TUESDAY:
                wochentag = weekdays.get(1);
                break;

            case Calendar.WEDNESDAY:
                wochentag = weekdays.get(2);
                break;

            case Calendar.THURSDAY:
                wochentag = weekdays.get(3);
                break;

            case Calendar.FRIDAY:
                wochentag = weekdays.get(4);
                break;

            case Calendar.SATURDAY:
                wochentag = weekdays.get(5);
                break;
        }

        return wochentag;
    }



    public static float roundFloat(float wert, int nachkommastellen){

        float rueckgabe = 0;
        int rueckgabeInt = 0;
        if(nachkommastellen > 0){

            rueckgabe = wert * (float) Math.pow(10f, nachkommastellen);
            rueckgabeInt = Math.round(rueckgabe);
            return ((float) rueckgabeInt) / (float) Math.pow(10f, nachkommastellen);
        }
        else{

            rueckgabe = wert;
            rueckgabeInt = Math.round(rueckgabe);
            return rueckgabeInt;
        }
    }


    public static String roundString(float wert, int nachkommastellen){

        float gerundet = roundFloat(wert, nachkommastellen);

        if(nachkommastellen > 0){
            int realeStellen = getNachkommastellen(gerundet);
            if(realeStellen == nachkommastellen){
                return "" + gerundet;
            }
            else{   //Wenn weniger echte Nachkommastellen als gefordert, mit Nullen ergaenzen
                String stringWert = "" + gerundet;
                for(int i=realeStellen; i < nachkommastellen; i++){
                    stringWert = stringWert + "0";
                }
                return stringWert;
            }
        }
        else{
            return "" + ((int)gerundet);
        }
    }

    public static int getNachkommastellen(float f) {
        String[] asplit =  Float.toString(f).split("\\.");
        return asplit[1].length();
    }
}

