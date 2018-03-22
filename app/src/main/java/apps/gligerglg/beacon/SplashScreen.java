package apps.gligerglg.beacon;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

public class SplashScreen extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private boolean isTeriffSet;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        sharedPreferences = getSharedPreferences("beacon_settings",0);
        isTeriffSet = sharedPreferences.getBoolean("teriff_set",false);

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
                    if(isTeriffSet)
                        intent = new Intent(SplashScreen.this,MainActivity.class);
                    else
                        intent = new Intent(SplashScreen.this,Tarrif.class);
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
