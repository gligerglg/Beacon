package apps.gligerglg.beacon;


import android.arch.persistence.room.Room;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MonthlyGraph extends Fragment {

    private BarChart barChart;
    private BeaconDB beaconDB;
    private Context context;
    private int iterator;
    private BarData barData;
    private ArrayList<String> months;
    private ArrayList<Double> costs;
    private FrameLayout layout;

    public MonthlyGraph() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_monthly_graph, container, false);

        barChart = root.findViewById(R.id.chart_monthly);
        context = getActivity();
        layout = root.findViewById(R.id.layout_monthly_graph);
        beaconDB = Room.databaseBuilder(context,BeaconDB.class,"BeaconDB").fallbackToDestructiveMigration()
                .allowMainThreadQueries().build();

        if(beaconDB.monthlyDAO().getRecordCount()>0)
            drawChart();

        barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                setMessage(e.getY() + " Units have Consumed in " + months.get((int)e.getX()) + "\nTotal Charge is " +
                costs.get((int)e.getX()) + " Rupees");
            }

            @Override
            public void onNothingSelected() {

            }
        });
        return root;
    }

    private void generateNormalData(){
        iterator = 0;
        List<BarEntry> entries = new ArrayList<>();
        List<MonthlyRecord> recordList = beaconDB.monthlyDAO().getAllRecords();
        for(MonthlyRecord record : recordList) {
            entries.add(new BarEntry(iterator, record.getUnits()));
            iterator++;
        }

        BarDataSet dataSet = new BarDataSet(entries,"Monthly Electricity Consumption");

        dataSet.setColor(Color.YELLOW);
        dataSet.setValueTextColor(Color.YELLOW);

        barData = new BarData(dataSet);
    }

    private void drawChart() {
        updateUI();
        generateNormalData();
        barChart.setDrawGridBackground(false);
        barChart.getXAxis().setEnabled(false);
        barChart.getAxisLeft().setEnabled(false);
        barChart.getAxisRight().setEnabled(false);

        barChart.getDescription().setEnabled(false);
        barChart.getLegend().setTextColor(Color.WHITE);
        barChart.getLegend().setWordWrapEnabled(true);
        barChart.setData(barData);
        barChart.animateY(2000, Easing.EasingOption.EaseOutCirc);
        barChart.invalidate();
    }

    private void updateUI()
    {
        months = new ArrayList<>();
        costs = new ArrayList<>();

        List<MonthlyRecord> recordList = beaconDB.monthlyDAO().getAllRecords();
        for(MonthlyRecord record : recordList) {
            months.add(record.getMonth());
            costs.add(record.getCharge());
        }
    }

    private void setMessage(String message){
        Snackbar snackbar = Snackbar.make(layout,message,Snackbar.LENGTH_LONG);
        snackbar.show();
    }
}
