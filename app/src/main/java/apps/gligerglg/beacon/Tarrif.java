package apps.gligerglg.beacon;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class Tarrif extends AppCompatActivity {

    private EditText txt_units;
    private Button btn_done;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private ConstraintLayout layout;
    private RadioGroup radioGroup;
    private RadioButton radioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarrif);

        Init();
        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Verification
               radioButton = findViewById(radioGroup.getCheckedRadioButtonId());
                if(txt_units.getText().toString().isEmpty())
                    setMessage("Set a Power Consumption Threshold Limit");

                //Save in Preferences
                if(!txt_units.getText().toString().isEmpty()){
                    editor.putString("category",radioButton.getText().toString());
                    editor.putInt("threshold",Integer.parseInt(txt_units.getText().toString()));
                    editor.putBoolean("teriff_set",true);
                    editor.commit();
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                }

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    private void setMessage(String message)
    {
        Snackbar snackbar = Snackbar.make(layout,message,Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    private void Init(){
        getSupportActionBar().hide();
        txt_units = findViewById(R.id.txt_units);
        btn_done = findViewById(R.id.btn_done);
        layout = findViewById(R.id.layout_teriff);
        radioGroup = findViewById(R.id.radio_category);
        radioButton = findViewById(R.id.radio_domestic);
        sharedPreferences = getSharedPreferences("beacon_settings",0);
        editor = sharedPreferences.edit();

        radioButton.setChecked(true);
    }
}
