package apps.gligerglg.beacon;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

public class SplashScreen extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private boolean isFirsttime = true;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        sharedPreferences = getSharedPreferences("beacon_settings",0);
        editor= sharedPreferences.edit();
        isFirsttime = sharedPreferences.getBoolean("intro", true);

        Thread timerThread = new Thread()
        {
            public  void run()
            {
                try
                {
                    sleep(2000);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                finally
                {
                    if(isFirsttime) {
                        intent = new Intent(SplashScreen.this, Intro.class);
                        editor.putBoolean("intro",false);
                        editor.commit();
                    }
                    else
                        intent = new Intent(SplashScreen.this,MainMenu.class);
                    startActivity(intent);
                }
            }
        };

        timerThread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
