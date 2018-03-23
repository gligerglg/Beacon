package apps.gligerglg.beacon;


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


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private TextView txt_units;
    private ImageView img_beacon;
    private CardView btn_meter_reader, btn_summery, btn_calc, btn_board;
    private LinearLayout btn_status_unit, btn_status_days;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        txt_units = rootView.findViewById(R.id.txt_no_unit);
        img_beacon = rootView.findViewById(R.id.img_beacon);
        btn_meter_reader = rootView.findViewById(R.id.btn_meter_reader);
        btn_summery = rootView.findViewById(R.id.btn_summery_usage);
        btn_calc = rootView.findViewById(R.id.btn_bill_calculator);
        btn_board = rootView.findViewById(R.id.btn_contact_board);
        btn_status_days = rootView.findViewById(R.id.btn_status_days);
        btn_status_unit = rootView.findViewById(R.id.btn_status_unit);

        Init();
        return rootView;
    }

    private void Init()
    {
        Glide.with(getActivity()).asGif().load(R.drawable.beacon).into(img_beacon);
        Glide.with(getActivity()).asBitmap().load(R.drawable.beacon_green).into(img_beacon);
    }

}
