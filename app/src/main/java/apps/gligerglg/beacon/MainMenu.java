package apps.gligerglg.beacon;

import android.app.Fragment;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.FrameLayout;

public class MainMenu extends AppCompatActivity {

    private FrameLayout mainFrame;
    private BottomNavigationView navigationView;
    private AboutFragment aboutFragment;
    private FragmentSettings settingsFragment;
    private HomeFragment homeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mainFrame = findViewById(R.id.main_frame);
        navigationView = findViewById(R.id.navigation_view);
        aboutFragment = new AboutFragment();
        settingsFragment = new FragmentSettings();
        homeFragment = new HomeFragment();

        setFragment(homeFragment);

        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.mnu_home:
                        setFragment(homeFragment);
                        return true;

                    case R.id.mnu_about:
                        setFragment(aboutFragment);
                        return true;

                    case R.id.mnu_settings:
                        setFragment(settingsFragment);
                        return true;

                    default:
                        return false;
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1)
            updateUI();
    }

    private void setFragment(android.support.v4.app.Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame,fragment);
        fragmentTransaction.commit();
    }

    private void updateUI(){
        setFragment(homeFragment);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
