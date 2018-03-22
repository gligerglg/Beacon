package apps.gligerglg.beacon;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class MainActivity extends AppCompatActivity {

    private TextView txt_units;
    private ImageView img_beacon;

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
        Glide.with(getApplicationContext()).asGif().load(R.drawable.beacon).into(img_beacon);
    }
}
