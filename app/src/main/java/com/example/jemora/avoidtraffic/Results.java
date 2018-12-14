package com.example.jemora.avoidtraffic;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static java.util.Calendar.HOUR;

//import butterknife.BindView;
//import butterknife.ButterKnife;


public class Results extends AppCompatActivity implements GeoTask.Geo{

    private TextView tvTime;
    private TextView tvDestination;
    private TextView tvAddress;
    private Button returnButton;
    private int minutes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results);
        final Intent intentR = new Intent(Results.this, ActivityUsingLocationAPI.class);
        Calendar cal = Calendar.getInstance();
        Intent intent=getIntent();
        tvDestination = (TextView)findViewById(R.id.tvDestination);
        tvTime = (TextView)findViewById(R.id.tvTime);
        tvAddress= (TextView)findViewById(R.id.tvAddress);
        String tvAddressString= intent.getStringExtra("tvAddress");
        String tvDestinationString= intent.getStringExtra("tvDestination");
        String tvTimeString= intent.getStringExtra("tvTime");
        //int tvTimeInt= Integer.parseInt(tvTimeString);
        tvDestination.setText(tvDestinationString);
        //tvTime.setText(tvTimeString);
        tvAddress.setText(tvAddressString);
        returnButton = findViewById(R.id.reset);
        //cal.add(HOUR,tvTimeInt);
        String url = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=" + tvAddressString + "&destinations=" + tvDestinationString +"&key=AIzaSyB5vVtXqrMHE_YNLoG_Sa91qUyDgEK7PsU";
        new GeoTask(Results.this).execute(url);
        GraphView graph = (GraphView) findViewById(R.id.graph);
        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(new DataPoint[] {
                new DataPoint(11, 46),
                new DataPoint(12, 50),
                new DataPoint(13, 47),
                new DataPoint(14, 48),
                new DataPoint(15, 51)
        });
        graph.addSeries(series);

// styling
        series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb((int) data.getX()*255/4, (int) Math.abs(data.getY()*255/6), 100);
            }
        });

        series.setSpacing(50);

// draw values on top
        series.setDrawValuesOnTop(true);
        series.setValuesOnTopColor(Color.RED);

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(intentR);

            }
        });


    }

    @Override
    public void setDouble(String result) {
        String res[]=result.split(",");
        Double min=Double.parseDouble(res[0])/60;
        tvTime.setText(  (int) (min / 60) + " hr " + (int) (min % 60) + " mins");

    }
}
