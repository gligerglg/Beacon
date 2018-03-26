package apps.gligerglg.beacon;


import android.app.AlertDialog;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private TextView txt_units, txt_total_days, txt_total_charge;
    private ImageView img_beacon;
    private CardView btn_meter_reader, btn_summery, btn_calc, btn_board;
    private Context context;
    private SharedPreferences sharedPreferences;
    private int total_units=0, total_days=0, threshold_unit=0, cycle_date=0;
    private double total_charge=0;
    private BeaconDB beaconDB;
    private String category;
    private FrameLayout layout;
    private SharedPreferences.Editor editor;

    private boolean isBeaconActive = false;
    private double critical_ratio = 0;
    private String bracon_message = "";
    String[]monthName={"January","February","March", "April", "May", "June", "July",
            "August", "September", "October", "November",
            "December"};

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
        txt_total_charge = rootView.findViewById(R.id.txt_total_charge);
        txt_total_days = rootView.findViewById(R.id.txt_total_days);
        layout = rootView.findViewById(R.id.layout_home);

        beaconDB = Room.databaseBuilder(context,BeaconDB.class,"BeaconDB").fallbackToDestructiveMigration()
                .allowMainThreadQueries().build();


        Init();

        btn_meter_reader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isReadingMarked())
                    setMessage("You have already read the Meter!");
                else {
                    startActivity(new Intent(context, TodayReading.class));
                    getActivity().finish();
                }
            }
        });

        btn_calc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context,Calculator.class));
            }
        });

        btn_board.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBoard();
            }
        });

        btn_summery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context,GraphViewer.class));
            }
        });

        img_beacon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setMessage(bracon_message);
            }
        });

        return rootView;
    }

    private void Init()
    {
        sharedPreferences = getActivity().getSharedPreferences("beacon_settings", 0);
        editor = sharedPreferences.edit();
        category = sharedPreferences.getString("category","");
        threshold_unit = sharedPreferences.getInt("threshold",0);
        cycle_date = sharedPreferences.getInt("cycle_days",0);
        updateUI();
    }

    private void updateUI(){

        total_days = 0;
        total_units = 0;
        total_charge = 0;

        total_days = beaconDB.dailyDAO().getRecordCount();
        if(total_days==0){
            txt_units.setText("00");
            txt_total_charge.setText("00");
            txt_total_days.setText("00");
        }
        else {
            List<DailyRecord> recordList = beaconDB.dailyDAO().getAllRecords();
            for(DailyRecord record : recordList){
                total_units += record.getUnits();
            }

            total_charge = calcCharge();

            txt_units.setText("" + total_units);
            txt_total_days.setText("" + total_days);
            txt_total_charge.setText(String.format("%.2f",total_charge));
        }

        //Handle Settings
        if(threshold_unit!=0 && cycle_date!=0){
            if(total_days==cycle_date){
                resetMonth();
                txt_units.setText("00");
                txt_total_charge.setText("00");
                txt_total_days.setText("00");
            }
            try {
                critical_ratio = (total_units / total_days) * cycle_date;
                if (critical_ratio >= threshold_unit) {
                    if (total_units >= threshold_unit)
                        bracon_message = "You have already exceeded threshold unit limit";
                    else
                        bracon_message = "Your threshold limit is can be exceeded. Reduce your power consumption";
                    Glide.with(getActivity()).asGif().load(R.drawable.beacon).into(img_beacon);
                    isBeaconActive = true;
                } else {
                    Glide.with(getActivity()).asBitmap().load(R.drawable.beacon_green).into(img_beacon);
                    isBeaconActive = false;
                    bracon_message = "Your power consumption is normal";
                }
            }catch (Exception e){
                Glide.with(getActivity()).asBitmap().load(R.drawable.beacon_green).into(img_beacon);
                isBeaconActive = false;
                bracon_message = "Your power consumption is normal";
            }
        }
        else if(threshold_unit!=0 && cycle_date==0){
            if(total_units>=threshold_unit){
                Glide.with(getActivity()).asGif().load(R.drawable.beacon).into(img_beacon);
                isBeaconActive = true;
                bracon_message = "You have already exceeded threshold unit limit";
            }
            else {
                Glide.with(getActivity()).asBitmap().load(R.drawable.beacon_green).into(img_beacon);
                isBeaconActive = false;
                bracon_message = "Your power consumption is less than threshold limit";
            }
        }
        else if(threshold_unit==0 && cycle_date!=0){
            if(total_days==cycle_date){
                resetMonth();
                txt_units.setText("00");
                txt_total_charge.setText("00");
                txt_total_days.setText("00");
            }
            Glide.with(getActivity()).asBitmap().load(R.drawable.beacon_yellow).into(img_beacon);
            bracon_message = "Your bill will be automatically refreshed";
        }
        else {
            Glide.with(getActivity()).asBitmap().load(R.drawable.beacon_deactivate).into(img_beacon);
            bracon_message = "Beacon is currently deactivated";
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

    private boolean isReadingMarked(){
        boolean chacked = false;
        Calendar nowcalender = Calendar.getInstance();
        Calendar taskcalendar = Calendar.getInstance();
        if(beaconDB.dailyDAO().getRecordCount()==0)
            chacked = false;
        else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            try {
                Date dbDate = sdf.parse(getLastRecord().getDate());
                taskcalendar.setTime(dbDate);
                if ((nowcalender.get(Calendar.YEAR)==taskcalendar.get(Calendar.YEAR))
                        && (nowcalender.get(Calendar.MONTH)==taskcalendar.get(Calendar.MONTH))
                && (nowcalender.get(Calendar.DATE)==taskcalendar.get(Calendar.DATE))){
                    chacked = true;
               }
                else if(nowcalender.before(taskcalendar))
                    chacked = true;
                else
                    chacked = false;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return chacked;
    }

    private void setMessage(String message){
        Snackbar snackbar = Snackbar.make(layout,message,Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    private void callBoard(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Contact Electricity Board")
                .setMessage("You are going to Contact Sri Lanka Electricity Board\nContinue?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:1987"));
                        startActivity(intent);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void resetMonth()
    {
        if(beaconDB.dailyDAO().getRecordCount()!=0) {
            List<DailyRecord> dailyRecordList = beaconDB.dailyDAO().getAllRecords();
            int units = 0;
            int days = beaconDB.dailyDAO().getRecordCount();
            for (DailyRecord record : dailyRecordList)
                units += record.getUnits();

            Calendar c = Calendar.getInstance();
            String month = monthName[c.get(Calendar.MONTH)];
            double charge = 0;
            switch (category) {
                case "Domestic":
                    charge = BillCalculator.calcDomesticBill(units, days);
                    break;
                case "Religious":
                    charge = BillCalculator.calcReligiousBill(units, days);
                    break;
                case "General Purpose":
                    charge = BillCalculator.calcGeneralBill(units, days);
                    break;
                case "Hotel":
                    charge = BillCalculator.calcHotelBill(units);
                    break;
                case "Industrial":
                    charge = BillCalculator.calcIndustrialBill(units, days);
                    break;
                default:
                    break;
            }

            editor.putInt("reading_data",getLastRecord().getReading());
            editor.commit();
            MonthlyRecord monthlyRecord = new MonthlyRecord(month, units, charge);
            beaconDB.monthlyDAO().addNewRecord(monthlyRecord);
            beaconDB.dailyDAO().deleteAllRecords();
            setMessage("Your daily records are refreshed.\nCurrent month data added to database successfully");
        }
        else
            setMessage("There is no Data in Database!");
    }

    private DailyRecord getLastRecord(){
        DailyRecord record = null;
        List<DailyRecord> recordList = beaconDB.dailyDAO().getAllRecords();
        for(DailyRecord dailyRecord : recordList)
            record = dailyRecord;
        return record;
    }
}
