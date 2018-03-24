package apps.gligerglg.beacon;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class DailyGraph extends Fragment {
    private final Handler mHandler = new Handler();
    private Runnable mTimer1;
    private Runnable mTimer2;
    private GraphView graph;
    private Context context;
    private DailyDBHelper dailyDBHelper;
    private LineGraphSeries<DataPoint> dataSeries;

    public DailyGraph() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_daily_graph, container, false);
        context = getActivity();
        dailyDBHelper = new DailyDBHelper(context);
        graph = root.findViewById(R.id.graphview_daily);
        dataSeries = new LineGraphSeries<>(getData());
        dataSeries.setColor(Color.YELLOW);
        dataSeries.setBackgroundColor(Color.YELLOW);
        dataSeries.setDrawDataPoints(true);
        dataSeries.setDataPointsRadius(12);

        dataSeries.setDrawBackground(true);

        graph.getViewport().setScalable(true);
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity()));
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getGridLabelRenderer().setHumanRounding(false);
        graph.addSeries(dataSeries);

        return root;
    }

    private DataPoint[] getData(){
        int count = dailyDBHelper.getRecordCount();
        List<DailyRecord> recordList = dailyDBHelper.getAllRecords();
        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        String[] dates = new String[count];
        DataPoint[] values = new DataPoint[count];
        int i=0;
        for(DailyRecord record : recordList){
                values[i] = new DataPoint(record.getReading(),record.getUnits());
                dates[i] = record.getDate();
                i++;
        }
        //staticLabelsFormatter.setHorizontalLabels(dates);
        //graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
        return values;
    }

   /* @Override
    public void onResume() {
        super.onResume();
        mTimer1 = new Runnable() {
            @Override
            public void run() {
                dataSeries.resetData(getData());
                mHandler.postDelayed(this, 300);
            }
        };
        mHandler.postDelayed(mTimer1, 300);

    }

    @Override
    public void onPause() {
        mHandler.removeCallbacks(mTimer1);
        super.onPause();
    }*/
}
