package apps.gligerglg.beacon;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class Calculator extends AppCompatActivity {

    private RadioGroup radioGroup;
    private RadioButton radio_button;
    private EditText txt_units, txt_days;
    private TextView txt_bill;
    private Button btn_calc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Init();

        btn_calc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radio_button = findViewById(radioGroup.getCheckedRadioButtonId());
                if(txt_units.getText().toString().isEmpty())
                    txt_units.setError("Please Insert Number of Units");
                if(txt_days.getText().toString().isEmpty())
                    txt_days.setError("Please Insert Number of Days");

                if(!txt_days.getText().toString().isEmpty() && !txt_units.getText().toString().isEmpty()){
                    double bill = calcCharge(radio_button.getText().toString(),Integer.parseInt(txt_units.getText().toString()),Integer.parseInt(txt_days.getText().toString()));
                    txt_bill.setText(String.format("%.2f",bill) + " Rupees including Fixed Charges");
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),MainMenu.class));
    }

    private void Init(){
        radioGroup = findViewById(R.id.calc_radio_category);
        radio_button = findViewById(R.id.calc_radio_domestic);
        txt_bill = findViewById(R.id.calc_txt_bill);
        txt_days = findViewById(R.id.calc_txt_days);
        txt_units = findViewById(R.id.calc_txt_units);
        btn_calc = findViewById(R.id.calc_btn_bill);
        txt_bill.setText("");
        radio_button.setChecked(true);
    }

    private double calcCharge(String category, int total_units, int total_days)
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
