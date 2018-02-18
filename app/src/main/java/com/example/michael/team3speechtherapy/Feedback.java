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

    public static String simluationFeedback(double f1,double f2,String profile, String v){
        double[] ExpectedFormances = FormantData.getExpectedFormants(profile,v);
        double F1, F2;
        F1 = ExpectedFormances[0];
        F2 = ExpectedFormances[1];
        //means f1 is too high
        String[] recommendation=new String[3];
        int i=0;
        if(f1-F1>15){
            //close mouth and higher tongue
            recommendation[i]="Close mouth a little and higher tongue.";
            i++;
            if(f2-F2>15) {
                recommendation[i] = "Advance tongue less.";
                i++;
            }
        }
        //means f2 is too high
        else if(f2-F2>15){
            //advance tongue less
            recommendation[i]="Advance tongue less.";
            i++;
        }
        //means f1 is too low
       else if(f1-F1<-15){
            //open mouth wider and lower tongue
            recommendation[i]="Open mouth wider and lower tongue.";
            i++;
            if(f2-F2<-15){
                recommendation[i]="Advance tongue more.";
                i++;
            }
        }
        //means f2 is too low
        else if(f2-F2<-15){
            //advance tongue more
            recommendation[i]="Advance tongue more.";
            i++;
        }
        else{
            recommendation[i]="Wow you're fantastic!";
            i++;
        }
        i=0;
        String appended="";
        while(recommendation[i]!=null) {
            appended=appended+" "+recommendation[i];
            i++;
        }
            return appended;
    }
}


