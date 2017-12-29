package com.example.bunfei.location_project;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

public class AnalysisActivity extends AppCompatActivity {

    TextView responseView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);

        responseView = (TextView) findViewById(R.id.responseView);

        final double[] latlong = getIntent().getExtras().getDoubleArray("latlong");
        final double lat = latlong[0];
        final double lng = latlong[1];
        final String address = getIntent().getExtras().getString("address");

        String result = address;

        responseView.setText(result);

        GraphView graph = (GraphView) findViewById(R.id.graph);
        String title = "Annual CO concentration";
        graph.setTitle(title);
        graph.setTitleTextSize(50);

        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 0),
                new DataPoint(2, 0.1),
                new DataPoint(4, 0.0),
                new DataPoint(6, 0.2),
                new DataPoint(8, 0.2),
                new DataPoint(10, 0.3),
                new DataPoint(12, 0.2),
                new DataPoint(14, 0.0),
                new DataPoint(16, 0.1),
                new DataPoint(18, 0.3),
                new DataPoint(20, 0.4),
                new DataPoint(22, 0.1),
                new DataPoint(24, 0.2)
        });
        graph.addSeries(series);

        graph.getViewport().setMinX(0.0);
        graph.getViewport().setMaxX(26);
        graph.getViewport().setMinY(0.0);
        graph.getViewport().setMaxY(1.0);

        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setXAxisBoundsManual(true);


        // use static labels for horizontal and vertical labels
        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        staticLabelsFormatter.setHorizontalLabels(new String[] {"","Jan", "Feb", "Mar","Apr", "May", "Jun","Jul", "Aug", "Sep","Oct", "Nov", "Dec",""});
        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);


        // styling
        series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb((int) data.getX()*255/4, (int) Math.abs(data.getY()*255/6), 100);
            }
        });

        series.setSpacing(20);

        // draw values on top
        series.setDrawValuesOnTop(false);
        series.setValuesOnTopColor(Color.RED);
        //series.setValuesOnTopSize(50);






        GraphView graph1 = (GraphView) findViewById(R.id.graph1);
        String title1 = "Annual NO2 concentration";
        graph1.setTitle(title1);
        graph1.setTitleTextSize(50);

        BarGraphSeries<DataPoint> series1 = new BarGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 0),
                new DataPoint(2, 0.011),
                new DataPoint(4, 0.034),
                new DataPoint(6, 0.017),
                new DataPoint(8, 0.017),
                new DataPoint(10, 0.017),
                new DataPoint(12, 0.014),
                new DataPoint(14, 0.017),
                new DataPoint(16, 0.011),
                new DataPoint(18, 0.018),
                new DataPoint(20, 0.031),
                new DataPoint(22, 0.026),
                new DataPoint(24, 0.018)
        });
        graph1.addSeries(series1);

        graph1.getViewport().setMinX(0);
        graph1.getViewport().setMaxX(26);
        graph1.getViewport().setMinY(0.0);
        graph1.getViewport().setMaxY(0.050);

        graph1.getViewport().setYAxisBoundsManual(true);
        graph1.getViewport().setXAxisBoundsManual(true);


        // use static labels for horizontal and vertical labels
        StaticLabelsFormatter staticLabelsFormatter1 = new StaticLabelsFormatter(graph1);
        staticLabelsFormatter1.setHorizontalLabels(new String[] {"","Jan", "Feb", "Mar","Apr", "May", "Jun","Jul", "Aug", "Sep","Oct", "Nov", "Dec",""});
        graph1.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter1);


        // styling
        series1.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb((int) data.getX()*255/4, (int) Math.abs(data.getY()*255/6), 100);
            }
        });

        series1.setSpacing(20);

        // draw values on top
        series1.setDrawValuesOnTop(false);
        series1.setValuesOnTopColor(Color.RED);
        //series.setValuesOnTopSize(50);



        GraphView graph2 = (GraphView) findViewById(R.id.graph2);
        String title2 = "Annual SO2 concentration";
        graph2.setTitle(title2);
        graph2.setTitleTextSize(50);

        BarGraphSeries<DataPoint> series2 = new BarGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 0),
                new DataPoint(2, 0.007),
                new DataPoint(4, 0.009),
                new DataPoint(6, 0.006),
                new DataPoint(8, 0.006),
                new DataPoint(10, 0.011),
                new DataPoint(12, 0.011),
                new DataPoint(14, 0.006),
                new DataPoint(16, 0.009),
                new DataPoint(18, 0.006),
                new DataPoint(20, 0.007),
                new DataPoint(22, 0.006),
                new DataPoint(24, 0.008)
        });
        graph2.addSeries(series2);

        graph2.getViewport().setMinX(0);
        graph2.getViewport().setMaxX(26);
        graph2.getViewport().setMinY(0.0);
        graph2.getViewport().setMaxY(0.02);

        graph2.getViewport().setYAxisBoundsManual(true);
        graph2.getViewport().setXAxisBoundsManual(true);


        // use static labels for horizontal and vertical labels
        StaticLabelsFormatter staticLabelsFormatter2 = new StaticLabelsFormatter(graph2);
        staticLabelsFormatter2.setHorizontalLabels(new String[] {"","Jan", "Feb", "Mar","Apr", "May", "Jun","Jul", "Aug", "Sep","Oct", "Nov", "Dec",""});
        graph2.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter2);


        // styling
        series2.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb((int) data.getX()*255/4, (int) Math.abs(data.getY()*255/6), 100);
            }
        });

        series2.setSpacing(20);

        // draw values on top
        series2.setDrawValuesOnTop(false);
        series2.setValuesOnTopColor(Color.RED);
        //series.setValuesOnTopSize(50);






        GraphView graph3 = (GraphView) findViewById(R.id.graph3);
        String title3 = "Annual PM2.5 concentration";
        graph3.setTitle(title3);
        graph3.setTitleTextSize(50);

        BarGraphSeries<DataPoint> series3 = new BarGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 0),
                new DataPoint(2, 21.0),
                new DataPoint(4, 25.0),
                new DataPoint(6, 18.0),
                new DataPoint(8, 25.0),
                new DataPoint(10, 26.0),
                new DataPoint(12, 19.0),
                new DataPoint(14, 24.0),
                new DataPoint(16, 13.0),
                new DataPoint(18, 18.0),
                new DataPoint(20, 22.0),
                new DataPoint(22, 23.0),
                new DataPoint(24, 24.0)
        });
        graph3.addSeries(series3);

        graph3.getViewport().setMinX(0);
        graph3.getViewport().setMaxX(26);
        graph3.getViewport().setMinY(0.0);
        graph3.getViewport().setMaxY(60.0);

        graph3.getViewport().setYAxisBoundsManual(true);
        graph3.getViewport().setXAxisBoundsManual(true);


        // use static labels for horizontal and vertical labels
        StaticLabelsFormatter staticLabelsFormatter3 = new StaticLabelsFormatter(graph3);
        staticLabelsFormatter3.setHorizontalLabels(new String[] {"","Jan", "Feb", "Mar","Apr", "May", "Jun","Jul", "Aug", "Sep","Oct", "Nov", "Dec",""});
        graph3.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter3);


        // styling

        series3.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb((int) data.getX()*255/4, (int) Math.abs(data.getY()*255/6), 100);
            }
        });


        series3.setSpacing(20);

        // draw values on top
        series3.setDrawValuesOnTop(false);
        series3.setValuesOnTopColor(Color.RED);
        //series.setValuesOnTopSize(50);




        GraphView graph4 = (GraphView) findViewById(R.id.graph4);
        String title4 = "Annual PM10 concentration";
        graph4.setTitle(title4);
        graph4.setTitleTextSize(50);

        BarGraphSeries<DataPoint> series4 = new BarGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 0),
                new DataPoint(2, 43),
                new DataPoint(4, 51),
                new DataPoint(6, 36),
                new DataPoint(8, 50),
                new DataPoint(10, 52),
                new DataPoint(12, 38),
                new DataPoint(14, 49),
                new DataPoint(16, 27),
                new DataPoint(18, 37),
                new DataPoint(20, 45),
                new DataPoint(22, 47),
                new DataPoint(24, 46)
        });
        graph4.addSeries(series4);

        graph4.getViewport().setMinX(0);
        graph4.getViewport().setMaxX(26);
        graph4.getViewport().setMinY(0.0);
        graph4.getViewport().setMaxY(80.0);

        graph4.getViewport().setYAxisBoundsManual(true);
        graph4.getViewport().setXAxisBoundsManual(true);


        // use static labels for horizontal and vertical labels
        StaticLabelsFormatter staticLabelsFormatter4 = new StaticLabelsFormatter(graph4);
        staticLabelsFormatter4.setHorizontalLabels(new String[] {"","Jan", "Feb", "Mar","Apr", "May", "Jun","Jul", "Aug", "Sep","Oct", "Nov", "Dec",""});
        graph4.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter4);


        // styling
        series4.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb((int) data.getX()*255/4, (int) Math.abs(data.getY()*255/6), 100);
            }
        });

        series4.setSpacing(20);

        // draw values on top
        series4.setDrawValuesOnTop(false);
        series4.setValuesOnTopColor(Color.RED);
        //series.setValuesOnTopSize(50);





        GraphView graph5 = (GraphView) findViewById(R.id.graph5);
        String title5 = "Annual Humidity concentration";
        graph5.setTitle(title5);
        graph5.setTitleTextSize(50);

        BarGraphSeries<DataPoint> series5 = new BarGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 0),
                new DataPoint(2, 63),
                new DataPoint(4, 55),
                new DataPoint(6, 59),
                new DataPoint(8, 67),
                new DataPoint(10, 72),
                new DataPoint(12, 81),
                new DataPoint(14, 86),
                new DataPoint(16, 80),
                new DataPoint(18, 87),
                new DataPoint(20, 84),
                new DataPoint(22, 73),
                new DataPoint(24, 67)
        });
        graph5.addSeries(series5);

        graph5.getViewport().setMinX(0);
        graph5.getViewport().setMaxX(26);
        graph5.getViewport().setMinY(0.0);
        graph5.getViewport().setMaxY(100.0);

        graph5.getViewport().setYAxisBoundsManual(true);
        graph5.getViewport().setXAxisBoundsManual(true);


        // use static labels for horizontal and vertical labels
        StaticLabelsFormatter staticLabelsFormatter5 = new StaticLabelsFormatter(graph5);
        staticLabelsFormatter5.setHorizontalLabels(new String[] {"","Jan", "Feb", "Mar","Apr", "May", "Jun","Jul", "Aug", "Sep","Oct", "Nov", "Dec",""});
        graph5.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter5);


        // styling
        series5.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb((int) data.getX()*255/4, (int) Math.abs(data.getY()*255/6), 100);
            }
        });

        series5.setSpacing(20);

        // draw values on top
        series5.setDrawValuesOnTop(false);
        series5.setValuesOnTopColor(Color.RED);
        //series.setValuesOnTopSize(50);






        GraphView graph6 = (GraphView) findViewById(R.id.graph6);
        String title6 = "Annual Noise concentration";
        graph6.setTitle(title6);
        graph6.setTitleTextSize(50);

        BarGraphSeries<DataPoint> series6 = new BarGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 0),
                new DataPoint(2, 60),
                new DataPoint(4, 57),
                new DataPoint(6, 63),
                new DataPoint(8, 63),
                new DataPoint(10, 64),
                new DataPoint(12, 62),
                new DataPoint(14, 70),
                new DataPoint(16, 83),
                new DataPoint(18, 82),
                new DataPoint(20, 75),
                new DataPoint(22, 64),
                new DataPoint(24, 63)
        });
        graph6.addSeries(series6);

        graph6.getViewport().setMinX(0);
        graph6.getViewport().setMaxX(26);
        graph6.getViewport().setMinY(0.0);
        graph6.getViewport().setMaxY(100.0);

        graph6.getViewport().setYAxisBoundsManual(true);
        graph6.getViewport().setXAxisBoundsManual(true);


        // use static labels for horizontal and vertical labels
        StaticLabelsFormatter staticLabelsFormatter6 = new StaticLabelsFormatter(graph6);
        staticLabelsFormatter6.setHorizontalLabels(new String[] {"","Jan", "Feb", "Mar","Apr", "May", "Jun","Jul", "Aug", "Sep","Oct", "Nov", "Dec",""});
        graph6.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter6);


        // styling
        series6.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb((int) data.getX()*255/4, (int) Math.abs(data.getY()*255/6), 100);
            }
        });

        series6.setSpacing(20);

        // draw values on top
        series6.setDrawValuesOnTop(false);
        series6.setValuesOnTopColor(Color.RED);
        //series.setValuesOnTopSize(50);

        final Button Searchbutton = (Button) findViewById(R.id.Choose);
        Searchbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nextIntent = new Intent(AnalysisActivity.this, ThankYouActivity.class);
                startActivity(nextIntent);
            }
        });
    }
}


//<com.jjoe64.graphview.GraphView
//        android:layout_width="match_parent"
//        android:layout_height="200dip"
//        android:id="@+id/graph"
//        xmlns:android="http://schemas.android.com/apk/res/android" />