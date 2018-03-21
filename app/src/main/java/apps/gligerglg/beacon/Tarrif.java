package apps.gligerglg.beacon;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

public class Tarrif extends AppCompatActivity {

    String[] categories = {"Domestic","Religious","General","Hotel","Industrial"};
    private MaterialBetterSpinner spinner;
    private EditText txt_units;
    private Button btn_done;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private ConstraintLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarrif);

        spinner = findViewById(R.id.spin_categories);
        txt_units = findViewById(R.id.txt_units);
        btn_done = findViewById(R.id.btn_done);
        layout = findViewById(R.id.layout_teriff);
        sharedPreferences = getSharedPreferences("beacon_settings",0);
        editor = sharedPreferences.edit();

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,categories);
        spinner.setAdapter(arrayAdapter);


        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Verification
                if(spinner.getText().toString().isEmpty())
                    setMessage("Select a Consumer Category");
                if(txt_units.getText().toString().isEmpty())
                    setMessage("Set a Limit");

                //Save in Preferences
                if(!spinner.getText().toString().isEmpty() && !txt_units.getText().toString().isEmpty()){
                    editor.putString("category",spinner.getText().toString());
                    editor.putInt("threshold",Integer.parseInt(txt_units.getText().toString()));
                    editor.commit();
                }

                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });
    }

    private void setMessage(String message)
    {
        Snackbar snackbar = Snackbar.make(layout,message,Snackbar.LENGTH_SHORT);
        snackbar.show();
    }
}
