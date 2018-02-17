package com.example.michael.team3speechtherapy;

import android.content.Context;

import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.Map;
import java.io.OutputStream;
/**
 * Created by j on 2/16/18.
 */

public class Feedback {




    public static void saveToFile(){
        /*String FILENAME = "hello_file";
        String string = "hello world!";

        FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
        fos.write(string.getBytes());
        fos.close();*/


    }

    //private static FileOutputStream openFileOutput(String filename, int modePrivate) {
    //openFileOutput(filename);
    //}

    public static double score(double f1, double f2, String profile, String v) {


        double mag = magnitude(f1, f2, profile, v);
        return mag;
        //return 5;
        //saveToFile();
    }

    public static double magnitude(double f1, double f2, String profile, String  v) {
        double[] ExpectedFormances = FormantData.getExpectedFormants(profile, v);
        System.out.println(Arrays.toString(ExpectedFormances));
        double F1, F2;
        F1 = ExpectedFormances[0];
        F2 = ExpectedFormances[1];
        return Math.sqrt(Math.pow(F1 - f1, 2) + Math.pow(F2 - f2, 2));


    }

}


