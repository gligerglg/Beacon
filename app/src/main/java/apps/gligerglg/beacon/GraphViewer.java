package apps.gligerglg.beacon;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.FrameLayout;

public class GraphViewer extends AppCompatActivity {

    private FrameLayout mainFrame;
    private BottomNavigationView navigationView;
    private DailyGraph dailyGraph;
    private MonthlyGraph monthlyGraph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_viewer);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mainFrame = findViewById(R.id.graph_frame);
        navigationView = findViewById(R.id.graph_navigation_view);
        dailyGraph = new DailyGraph();
        monthlyGraph = new MonthlyGraph();

        setFragment(dailyGraph);

        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.mnu_daily_summery:
                        setFragment(dailyGraph);
                        return true;

                    case R.id.mnu_monthly_summery:
                        setFragment(monthlyGraph);
                        return true;

                    default:
                        return false;
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    private void setFragment(android.support.v4.app.Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.graph_frame,fragment);
        fragmentTransaction.commit();
    }
}
