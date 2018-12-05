package com.example.jemora.avoidtraffic;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;

public class MainActivity extends AppCompatActivity {

    private EditText destination;
    private EditText departTime;
    private TextView departTimeTextView;
    private TextView destinationTextView;
    private Button saveButton;
     private GraphView graphView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        departTimeTextView = findViewById(R.id.depart_time);
        departTime = findViewById(R.id.depart_time_edit_text);
        destination = findViewById(R.id.destination_edit_text);
        destinationTextView = findViewById(R.id.destination);
        graphView = findViewById(R.id.graph);
        saveButton = findViewById(R.id.save);


    }




}
