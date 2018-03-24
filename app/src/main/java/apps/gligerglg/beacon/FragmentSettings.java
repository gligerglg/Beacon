package apps.gligerglg.beacon;


import android.app.AlarmManager;
import android.support.v4.app.DialogFragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Switch;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentSettings extends Fragment{

    private Button btn_reset, btn_reset_all;
    private Switch switch_date, switch_unit;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private int units, cycle_days;
    private FrameLayout layout;
    private Context context;
    private DailyDBHelper dailyDBHelper;
    private MonthlyDBHelper monthlyDBHelper;
    private String category;

    String[]monthName={"January","February","March", "April", "May", "June", "July",
            "August", "September", "October", "November",
            "December"};

    public FragmentSettings() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View rootView = inflater.inflate(R.layout.fragment_fragment_settings, container, false);
       context = getActivity();
       btn_reset = rootView.findViewById(R.id.btn_setting_reset);
       btn_reset_all = rootView.findViewById(R.id.btn_setting_reset_all);
       switch_date = rootView.findViewById(R.id.switch_settings_date);
       switch_unit = rootView.findViewById(R.id.switch_settings_unit);
       layout = rootView.findViewById(R.id.frame_settings);
       Init();

       //Threshold Button Click Event
       switch_unit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
               if(switch_unit.isChecked()) {
                   getUnits();
               }else{
                   editor.putInt("threshold",0);
                   editor.commit();
               }
           }
       });

       //Reset Daily Readings
       btn_reset.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               AlertDialog.Builder builder = new AlertDialog.Builder(context);
               builder.setTitle("Warning")
                       .setMessage("You are going to Reset you daily reading database\nContinue?")
                       .setCancelable(false)
                       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialogInterface, int i) {
                               resetMonth();
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
       });

       //Set Bill Cycle
        switch_date.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(switch_date.isChecked()){
                    getCycleDays();
                }
                else {
                    editor.putInt("cycle_days",0);
                    editor.commit();
                }
            }
        });

        //Reset All Data
        btn_reset_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Warning")
                        .setMessage("You are going to Reset all the Data\nContinue?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dailyDBHelper.deleteAllRecords();
                                monthlyDBHelper.deleteAllRecords();
                                setMessage("All Data Erased Successfully!");
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
        });

       return rootView;
    }

    private void Init(){

        //Get Threshold Units
        sharedPreferences = getActivity().getSharedPreferences("beacon_settings",0);
        editor = sharedPreferences.edit();
        units = sharedPreferences.getInt("threshold",0);
        category = sharedPreferences.getString("category","");

        //Initialize DB helpers
        dailyDBHelper = new DailyDBHelper(context);
        monthlyDBHelper = new MonthlyDBHelper(context);

        //Set CycleDay Switch
        cycle_days = sharedPreferences.getInt("cycle_days",0);
        if(cycle_days==0)
        {
            switch_date.setChecked(false);
        }
        else {
            switch_date.setChecked(true);
        }

        if(units==0)
        {
            switch_unit.setChecked(false);
        }
        else
            switch_unit.setChecked(true);
    }

    private void getUnits(){
        LayoutInflater li = LayoutInflater.from(context);
        View unitInputView = li.inflate(R.layout.unit_inputbox,null);

        AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.MyDialogTheme);
        builder.setView(unitInputView);
        final EditText inputUnits = unitInputView.findViewById(R.id.txt_settings_unit_input);
        inputUnits.setText(units + "");

        builder.setCancelable(false)
                .setPositiveButton("Set", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        units = Integer.parseInt(inputUnits.getText().toString());
                        if(units!=0){
                        editor.putInt("threshold",units);
                        editor.commit();
                        }
                        else
                            switch_unit.setChecked(false);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch_unit.setChecked(false);
                        dialogInterface.cancel();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void getCycleDays(){
        LayoutInflater li = LayoutInflater.from(context);
        View unitInputView = li.inflate(R.layout.day_inputbox,null);

        AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.MyDialogTheme);
        builder.setView(unitInputView);
        final EditText inputDays = unitInputView.findViewById(R.id.txt_settings_days_input);
        inputDays.setText(cycle_days + "");

        builder.setCancelable(false)
                .setPositiveButton("Set", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        cycle_days = Integer.parseInt(inputDays.getText().toString());
                        if(cycle_days!=0) {
                            editor.putInt("cycle_days", cycle_days);
                            editor.commit();
                        }else
                            switch_date.setChecked(false);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch_date.setChecked(false);
                        dialogInterface.cancel();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void setMessage(String message){
        Snackbar snackbar = Snackbar.make(layout,message,Snackbar.LENGTH_LONG);
        snackbar.show();
    }
    private void resetMonth()
    {
        if(dailyDBHelper.getRecordCount()!=0) {
            List<DailyRecord> dailyRecordList = dailyDBHelper.getAllRecords();
            int units = 0;
            int days = dailyDBHelper.getRecordCount();
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

            MonthlyRecord monthlyRecord = new MonthlyRecord(month, units, charge);
            monthlyDBHelper.addNewRecord(monthlyRecord);
            dailyDBHelper.deleteAllRecords();
        }
        else
            setMessage("There is no Data in Database!");
    }


}
