package com.example.michael.team3speechtherapy;

import android.content.Context;

import java.io.FileOutputStream;
import java.util.Map;
import java.io.OutputStream;
/**
 * Created by j on 2/16/18.
 */

public class Feedback {
    enum gender {M, F, Ch}

    enum vowel {a, e, i, o, u}

    private static Map<vowel, Map<gender, double[]>> formantTable;
    /*public static void initTable(){
       if(!formantTable.isEmpty()){
           return;

       }
       else{
           formantTable.put(vowel.a, (new HashMap<gender, double[]>()).put(gender.M, new double[]{730,1090}));
       }
    }*/

    public static double[] getExpectedFormance(gender g, vowel v) {
        return new double[]{0, 0};
        //return formantTable.get(v).get(g);
    }
    public static void saveToFile(){
        String FILENAME = "hello_file";
        String string = "hello world!";

       // FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
      //  fos.write(string.getBytes());
    //    fos.close();
    }

    //private static FileOutputStream openFileOutput(String filename, int modePrivate) {
    //openFileOutput(filename);
    //}

    public static double score(double f1, double f2, gender profile, vowel v) {

        profile = gender.M;
        v = vowel.a;
        double mag = magnitude(f1, f2, profile, v);
        return mag;
        //return 5;
        //saveToFile();
    }

    public static double magnitude(double f1, double f2, gender profile, vowel v) {
        double[] ExpectedFormances = getExpectedFormance(profile, v);
        double F1, F2;
        F1 = ExpectedFormances[0];
        F2 = ExpectedFormances[1];
        return Math.sqrt(Math.pow(F1 - f1, 2) + Math.pow(F2 - f2, 2));


    }
}


