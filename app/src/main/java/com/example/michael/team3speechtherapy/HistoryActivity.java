package com.example.michael.team3speechtherapy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Spinner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.String;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private ArrayList<String[]> history;
    InputStream inputStream;
    private String selectedVowel = "ee";

    public ArrayList<String[]> filterHistory(String vowel)
    {
        return history;
    }

    //Filter history, set vowel, update UI.
    private void updateUi()
    {
        ArrayList<String[]> toDisplay= filterHistory(selectedVowel);
        GridView list = (GridView) findViewById(R.id.history_list);

        ArrayList<String> dataToDisplay = new ArrayList<String>();
        int i = 0;
        for (String[] item:toDisplay
             ) {
            dataToDisplay.addAll(Arrays.asList(item));
        }
        String[] finalarray = dataToDisplay.toArray(new String[0]);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, finalarray);

        list.setAdapter(adapter);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setTitle("History");
        super.onCreate(savedInstanceState);
        try {
            history = readUserFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //history = "220,2290,M,ee\n220,2290,M,o\n225,2290,M,ee\n220,2290,M,o";
        setContentView(R.layout.activity_history);

        //DropDown Menu
        Spinner spinner = (Spinner) findViewById(R.id.history_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.vowels, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        // create listener and add to spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // put code which recognize a selected element
                String[] options = getResources().getStringArray(R.array.vowels);
                selectedVowel = options[position];
                updateUi();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        updateUi();
    }

    //will be ported to James file
    public ArrayList<String[]> readUserFile() throws IOException {
        ArrayList<String[]> returnValues = new ArrayList<String[]>();
        FileInputStream x = openFileInput("alice.csv");
        InputStreamReader isr = new InputStreamReader(x);

        BufferedReader fileReader = new BufferedReader(isr);
        String line = "";
        String data[][];
        String[] tokens;
        while ((line = fileReader.readLine()) != null) {
            //Get all tokens available in line
            tokens = line.split(",");

            if (tokens.length > 0) {
                //How am I going to filter each token?
                returnValues.add(tokens);

            }
        }
        return returnValues;
    }

    //Create a send button
    public void sendButton()
    {

    }
}
