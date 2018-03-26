package apps.gligerglg.beacon;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

public class TodayReading extends AppCompatActivity {

    private TextView txt_prev_reading;
    private EditText txt_today_reading;
    private Button btn_set_readong;
    private BeaconDB beaconDB;
    private ConstraintLayout layout;
    private SharedPreferences sharedPreferences;
    private int today_reading, last_reading, unit, days, reading_data;
    private String date;
    private boolean isFirst = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_reading);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Init();

        btn_set_readong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(txt_today_reading.getText().toString()))
                    setMessage("Please Insert Today Reading!");
                else {
                    today_reading = Integer.parseInt(txt_today_reading.getText().toString());

                    if(isFirst)
                        unit = 0;
                    else
                        unit = today_reading - last_reading;

                    if(unit<0)
                        setMessage("Invalid Meter Reading");
                    else {
                        Calendar calendar = Calendar.getInstance();
                        date = calendar.get(Calendar.YEAR) + "/" + (calendar.get(Calendar.MONTH) +1) + "/" + calendar.get(Calendar.DATE);
                        beaconDB.dailyDAO().addNewRecord(new DailyRecord(today_reading,unit,date));
                        startActivityForResult(new Intent(getApplicationContext(),MainMenu.class),1);
                        finish();
                    }

                }
            }
        });
    }

    private void updateUI()
    {
        beaconDB = Room.databaseBuilder(getApplicationContext(),BeaconDB.class,"BeaconDB").fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
        DailyRecord lastRecord = getLastRecord();
        days = beaconDB.dailyDAO().getRecordCount();
        if(lastRecord!=null) {
            last_reading = lastRecord.getReading();
            isFirst = false;
            txt_prev_reading.setText("" + last_reading);
        }
        else if(reading_data!=0){
            last_reading = reading_data;
            isFirst = false;
            txt_prev_reading.setText("" + last_reading);
        }
        else {
            isFirst = true;
            txt_prev_reading.setText("-------");
        }
    }

    private void Init(){
        txt_prev_reading = findViewById(R.id.txt_prev_reading);
        txt_today_reading = findViewById(R.id.txt_today_reading);
        btn_set_readong = findViewById(R.id.btn_today_reading);
        layout = findViewById(R.id.layout_today);

        sharedPreferences = getSharedPreferences("beacon_settings",0);
        reading_data = sharedPreferences.getInt("reading_data",0);
        updateUI();
    }

    private void setMessage(String message){
        Snackbar snackbar = Snackbar.make(layout,message,Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivityForResult(new Intent(getApplicationContext(),MainMenu.class),1);
    }

    private DailyRecord getLastRecord(){
        DailyRecord record = null;
        List<DailyRecord> recordList = beaconDB.dailyDAO().getAllRecords();
        for(DailyRecord dailyRecord : recordList)
            record = dailyRecord;
        return record;
    }
}
