package apps.gligerglg.beacon;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.WindowManager;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

/**
 * Created by Gayan Lakshitha on 3/26/2018.
 */

public class Intro extends AppIntro {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setSlideOverAnimation();

        addSlide(AppIntroFragment.newInstance("Beacon",
                "Daily Electricity Consumption Monitor and Analyzer\nAutomated Prediction" +
                        "Generator to Conserve Electricity",
                R.drawable.beacon_icon,
                Color.parseColor("#212121")));

        addSlide(AppIntroFragment.newInstance("Threshold Limit & Bill Cycle Refresher",
                "Set 'Power Consumption Threshold Unit' to get alert before increasing your threshold limit\n" +
                        "Set 'Auto Refresh Bill Cycle' to refresh your monthly bill automatically",
                R.drawable.setting_intro,
                Color.parseColor("#76a20b")));

        addSlide(AppIntroFragment.newInstance("Set of Beacons",
                "Alerts will be given with different Beacons",
                R.drawable.beacon_intro,
                Color.parseColor("#ec8826")));

        addSlide(AppIntroFragment.newInstance("Analyze Using Graphs",
                "Daily Electricity Consumption & Monthly Electricity Consumption can be analyzed using Graphs",
                R.drawable.graph_intro,
                Color.parseColor("#059ddc")));

        addSlide(AppIntroFragment.newInstance("Calculate Any Bill @ Any Time",
                "Calculate any bill @ anytime using 'Electricity Bill Calculator'",
                R.drawable.calc_intro,
                Color.parseColor("#b4061b")));

        addSlide(AppIntroFragment.newInstance("All Set!",
                "Let's Start to Save Electricity!",
                R.drawable.logo_icon_dark_horizontal,
                Color.parseColor("#424242")));
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        startActivity(new Intent(Intro.this,Tarrif.class));
        finish();
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        startActivity(new Intent(Intro.this,Tarrif.class));
        finish();
    }
}
