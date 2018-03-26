package apps.gligerglg.beacon;


import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.EntryXComparator;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class DailyGraph extends Fragment {

    private Context context;
    private BeaconDB beaconDB;
    private LineChart lineChart;
    private int total_units, threshold_unit, iterator;
    private SharedPreferences sharedPreferences;
    private LineData lineData;
    private FrameLayout layout;
    private ArrayList<String> dates;

    public DailyGraph() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_daily_graph, container, false);
        context = getActivity();

        lineChart = root.findViewById(R.id.chart_daily);
        layout = root.findViewById(R.id.layout_daily_graph);
        beaconDB = Room.databaseBuilder(context,BeaconDB.class,"BeaconDB").fallbackToDestructiveMigration()
                .allowMainThreadQueries().build();
        sharedPreferences = getActivity().getSharedPreferences("beacon_settings", 0);
        threshold_unit = sharedPreferences.getInt("threshold",0);

        if(beaconDB.dailyDAO().getRecordCount()>0)
            drawChart();

        lineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                setMessage(e.getY() + " Units have Consumed on " + dates.get((int)e.getX()));
            }

            @Override
            public void onNothingSelected() {

            }
        });

        return root;
    }

    private void generateNormalData(){
        iterator=0;
        List<Entry> entries = new ArrayList<>();
        List<DailyRecord> recordList = beaconDB.dailyDAO().getAllRecords();
        for(DailyRecord record : recordList) {
            entries.add(new Entry(iterator, record.getUnits()));
            iterator++;
        }

        LineDataSet dataSet = new LineDataSet(entries,"Daily Electricity Consumption");

        dataSet.setColor(Color.YELLOW);
        dataSet.setDrawFilled(true);
        dataSet.setFillColor(Color.YELLOW);
        dataSet.setValueTextColor(Color.YELLOW);
        dataSet.setCircleColor(Color.YELLOW);
        dataSet.setCircleColorHole(Color.TRANSPARENT);
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        lineData = new LineData(dataSet);
    }

    private void thresholdDataGenerator(){
        total_units = 0;
        iterator=0;

        List<Entry> entries_lower = new ArrayList<>();
        List<Entry> entries_higher = new ArrayList<>();
        Entry extra_entry = null;
        List<DailyRecord> recordList = beaconDB.dailyDAO().getAllRecords();

            for (DailyRecord record : recordList) {
                total_units += record.getUnits();
                iterator++;

                if (total_units >= threshold_unit) {
                    if (extra_entry != null) {
                        entries_higher.add(extra_entry);
                        extra_entry = null;
                    }
                    entries_higher.add(new Entry(iterator, record.getUnits()));
                } else {
                    extra_entry = new Entry(iterator, record.getUnits());
                    entries_lower.add(extra_entry);
                }
            }


        LineDataSet dataSet_lower = new LineDataSet(entries_lower,"Electricity Consumption Under Threshold Limit");
        LineDataSet dataSet_higer = new LineDataSet(entries_higher,"Electricity Consumption Over Threshold Limit");

        //Lower Data Style
        dataSet_lower.setColor(Color.YELLOW);
        dataSet_lower.setDrawFilled(true);
        dataSet_lower.setFillColor(Color.YELLOW);
        dataSet_lower.setValueTextColor(Color.YELLOW);
        dataSet_lower.setCircleColor(Color.YELLOW);
        dataSet_lower.setCircleColorHole(Color.TRANSPARENT);
        dataSet_lower.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        //Higher Data Style
        dataSet_higer.setColor(Color.RED);
        dataSet_higer.setDrawFilled(true);
        dataSet_higer.setFillColor(Color.RED);
        dataSet_higer.setValueTextColor(Color.RED);
        dataSet_higer.setCircleColor(Color.RED);
        dataSet_higer.setCircleColorHole(Color.TRANSPARENT);
        dataSet_higer.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        lineData = new LineData(dataSet_lower,dataSet_higer);
    }

    private void setMessage(String message){
        Snackbar snackbar = Snackbar.make(layout,message,Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    private void updateUI()
    {
        dates = new ArrayList<>();
        List<DailyRecord> recordList = beaconDB.dailyDAO().getAllRecords();
        for(DailyRecord record : recordList)
            dates.add(record.getDate());
    }

    private void drawChart(){
        updateUI();

        if(threshold_unit==0){
            generateNormalData();
        }
        else {
            //thresholdDataGenerator();
            generateNormalData();
        }

        lineChart.setDrawGridBackground(false);
        lineChart.getXAxis().setEnabled(false);
        lineChart.getAxisLeft().setEnabled(false);
        lineChart.getAxisRight().setEnabled(false);

        lineChart.getDescription().setEnabled(false);
        lineChart.getLegend().setTextColor(Color.WHITE);
        lineChart.getLegend().setWordWrapEnabled(true);
        lineChart.setData(lineData);
        lineChart.animateY(3000, Easing.EasingOption.EaseOutCirc);
        lineChart.invalidate();
    }
}
