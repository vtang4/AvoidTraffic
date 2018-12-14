package com.example.jemora.avoidtraffic;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

//import butterknife.BindView;
//import butterknife.ButterKnife;


public class Results extends AppCompatActivity{

    private TextView tvTime;
    private TextView tvDestination;
    private TextView tvAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results);
        Intent intent=getIntent();
        tvDestination = (TextView)findViewById(R.id.tvDestination);
        tvTime = (TextView)findViewById(R.id.tvTime);
        tvAddress= (TextView)findViewById(R.id.tvAddress);
        String tvAddressString= intent.getStringExtra("tvAddress");
        String tvDestinationString= intent.getStringExtra("tvDestination");
        String tvTimeString= intent.getStringExtra("tvTime");
        tvDestination.setText(tvDestinationString);
        tvTime.setText(tvTimeString);
        tvAddress.setText(tvAddressString);


        GraphView graph = (GraphView) findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6)
        });
        graph.addSeries(series);


    }

}
