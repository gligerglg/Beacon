package apps.gligerglg.beacon;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class MainActivity extends AppCompatActivity {

    private TextView txt_units;
    private ImageView img_beacon;
    private CardView btn_meter_reader, btn_summery, btn_calc, btn_board;
    private LinearLayout btn_status_unit, btn_status_days;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Init();
    }

    private void Init()
    {
        txt_units = findViewById(R.id.txt_no_unit);
        img_beacon = findViewById(R.id.img_beacon);
        btn_meter_reader = findViewById(R.id.btn_meter_reader);
        btn_summery = findViewById(R.id.btn_summery_usage);
        btn_calc = findViewById(R.id.btn_bill_calculator);
        btn_board = findViewById(R.id.btn_contact_board);
        btn_status_days = findViewById(R.id.btn_status_days);
        btn_status_unit = findViewById(R.id.btn_status_unit);

        Glide.with(getApplicationContext()).asGif().load(R.drawable.beacon).into(img_beacon);
        Glide.with(getApplicationContext()).asBitmap().load(R.drawable.beacon_green).into(img_beacon);
    }
}
