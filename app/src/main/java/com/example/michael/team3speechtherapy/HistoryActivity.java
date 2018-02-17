package com.example.michael.team3speechtherapy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class HistoryActivity extends AppCompatActivity {

    private String history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setTitle("History");
        super.onCreate(savedInstanceState);
        history = "220,2290,M,ee\n220,2290,M,o\n225,2290,M,ee\n220,2290,M,o";
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

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    //Create a send button
    public void sendButton()
    {

    }
}
