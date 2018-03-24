package apps.gligerglg.beacon;

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

public class TodayReading extends AppCompatActivity {

    private TextView txt_prev_reading;
    private EditText txt_today_reading;
    private Button btn_set_readong;
    private DailyDBHelper dailyDBHelper;
    private ConstraintLayout layout;

    private int today_reading, last_reading, unit, days;
    private String date;

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

                    if(days==0)
                        unit=0;
                    else
                        unit = today_reading - last_reading;

                    if(unit<0)
                        setMessage("Invalid Meter Reading");
                    else {
                        Calendar calendar = Calendar.getInstance();
                        date = calendar.get(Calendar.YEAR) + "/" + (calendar.get(Calendar.MONTH) +1) + "/" + calendar.get(Calendar.DATE);
                        dailyDBHelper.addNewRecord(new DailyRecord(today_reading,unit,date));
                        startActivityForResult(new Intent(getApplicationContext(),MainMenu.class),1);
                        finish();
                    }



                }
            }
        });
    }

    private void updateUI()
    {
        dailyDBHelper = new DailyDBHelper(getApplicationContext());
        DailyRecord lastRecord = dailyDBHelper.getLastRecord();
        days = dailyDBHelper.getRecordCount();
        if(lastRecord.getReading()!=0) {
            last_reading = lastRecord.getReading();
            txt_prev_reading.setText("" + last_reading);
        }
        else
            txt_prev_reading.setText("-------");
    }

    private void Init(){
        txt_prev_reading = findViewById(R.id.txt_prev_reading);
        txt_today_reading = findViewById(R.id.txt_today_reading);
        btn_set_readong = findViewById(R.id.btn_today_reading);
        layout = findViewById(R.id.layout_today);
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
}
