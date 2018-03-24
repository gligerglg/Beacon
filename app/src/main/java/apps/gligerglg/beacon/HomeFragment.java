package apps.gligerglg.beacon;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private TextView txt_units, txt_total_days, txt_total_charge;
    private ImageView img_beacon;
    private CardView btn_meter_reader, btn_summery, btn_calc, btn_board;
    private LinearLayout btn_status_unit, btn_status_days;
    private Context context;

    private int total_units=0, total_days=0;
    private double total_charge=0;
    private DailyDBHelper dailyDBHelper;
    private String category;
    private SharedPreferences sharedPreferences;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        context = getActivity();
        txt_units = rootView.findViewById(R.id.txt_no_unit);
        img_beacon = rootView.findViewById(R.id.img_beacon);
        btn_meter_reader = rootView.findViewById(R.id.btn_meter_reader);
        btn_summery = rootView.findViewById(R.id.btn_summery_usage);
        btn_calc = rootView.findViewById(R.id.btn_bill_calculator);
        btn_board = rootView.findViewById(R.id.btn_contact_board);
        btn_status_days = rootView.findViewById(R.id.btn_status_days);
        btn_status_unit = rootView.findViewById(R.id.btn_status_unit);
        txt_total_charge = rootView.findViewById(R.id.txt_total_charge);
        txt_total_days = rootView.findViewById(R.id.txt_total_days);

        Init();

        btn_meter_reader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context,TodayReading.class));
            }
        });


        return rootView;
    }

    private void Init()
    {
        Glide.with(getActivity()).asGif().load(R.drawable.beacon).into(img_beacon);
        Glide.with(getActivity()).asBitmap().load(R.drawable.beacon_green).into(img_beacon);
        sharedPreferences = getActivity().getSharedPreferences("beacon_settings",0);
        category = sharedPreferences.getString("category","");

        updateUI();
    }

    private void updateUI(){

        DailyDBHelper dailyDBHelper = new DailyDBHelper(context);
        total_days = dailyDBHelper.getRecordCount();
        if(total_days==0){
            txt_units.setText("00");
            txt_total_charge.setText("00");
            txt_total_days.setText("00");
        }
        else if(total_days==1){
            txt_units.setText("--");
            txt_total_charge.setText("" + calcCharge());
            txt_total_days.setText("1");
        }
        else {
            List<DailyRecord> recordList = dailyDBHelper.getAllRecords();
            for(DailyRecord record : recordList){
                total_units += record.getUnits();
            }

            total_charge = calcCharge();

            txt_units.setText("" + total_units);
            txt_total_days.setText("" + total_days);
            txt_total_charge.setText(String.format("%.2f",total_charge));
        }
    }

    private double calcCharge()
    {
        double total_charge=0;

        switch (category) {
            case "Domestic":
                total_charge = BillCalculator.calcDomesticBill(total_units, total_days);
                break;
            case "Religious":
                total_charge = BillCalculator.calcReligiousBill(total_units, total_days);
                break;
            case "General Purpose":
                total_charge = BillCalculator.calcGeneralBill(total_units, total_days);
                break;
            case "Hotel":
                total_charge = BillCalculator.calcHotelBill(total_units);
                break;
            case "Industrial":
                total_charge = BillCalculator.calcIndustrialBill(total_units, total_days);
                break;
            default:
                break;
        }

        return total_charge;
    }


}
