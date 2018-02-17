package com.example.michael.team3speechtherapy;
import java.util.*;
/**
 * Created by James Tam on 2/16/2018.
 */

public class FormantData {

    final String[] gender = {"M", "W", "CH"};
    final String[] vowels = {"ee", "i", "e", "ae", "ah", "aw", "^u", "oo", "u", "er"};

    static HashMap<String, HashMap<String, double[]>> table;
    private static void init()
    {
        if(table!=null)
            return;
        //Create Hashtables
       table = new HashMap<String, HashMap<String, double[]>>();

        //Inserting the values into the HashMap.
        // Males
        createSubMap("M", "ee",270,2290);
        createSubMap("M", "i",390,1990);
        createSubMap("M", "e",530,1840);
        createSubMap("M", "ae",660,1720);
        createSubMap("M", "ah",730,1090);
        createSubMap("M", "aw",570,840);
        createSubMap("M", "^u",440,1020);
        createSubMap("M", "oo",300,870);
        createSubMap("M", "u",640,1190);
        createSubMap("M", "er",490,1350);

        // Women
        createSubMap("W", "ee",310,2790);
        createSubMap("W", "i",430,2480);
        createSubMap("W", "e",610,2330);
        createSubMap("W", "ae",860,2050);
        createSubMap("W", "ah",850,1220);
        createSubMap("W", "aw",590,920);
        createSubMap("W", "^u",470,1160);
        createSubMap("W", "oo",370,950);
        createSubMap("W", "u",760,1400);
        createSubMap("W", "er",500,1640);

        // Children
        createSubMap("CH", "ee",370,3200);
        createSubMap("CH", "i",530,2730);
        createSubMap("CH", "e",690,2610);
        createSubMap("CH", "ae",1010,2320);
        createSubMap("CH", "ah",1030,1370);
        createSubMap("CH", "aw",680,1060);
        createSubMap("CH", "^u",560,1410);
        createSubMap("CH", "oo",430,1170);
        createSubMap("CH", "u",850,1590);
        createSubMap("CH", "er",560,1820);

    }

    private static void createSubMap(String g, String v, double f1, double f2)
    {
        HashMap<String, double[]> temp;
        if(table.containsKey(g)){
            temp = table.get(g);
        }
        else {
            //Males
            temp = new HashMap<String, double[]>();
            table.put(g, temp);
        }
        temp.put(v, new double[]{f1, f2});
    }
    public static double[] getExpectedFormants(String g, String v){
        init();
        return table.get(g).get(v);
    }
}
