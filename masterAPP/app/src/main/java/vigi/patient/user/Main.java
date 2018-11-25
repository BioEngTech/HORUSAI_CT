package vigi.patient.user;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.FrameLayout;

import vigi.patient.R;
import vigi.patient.utils.BottomNavigationViewHelper;

public class Main extends AppCompatActivity {

    private FrameLayout mainFrame;
    private HealthFragment ratingFragment;
    private HomeFragment HomeFragment;
    private EventsFragment EventsFragment;
    private AccountFragment AccountFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_main);

        mainFrame = findViewById(R.id.userMain_MainFrame);
        BottomNavigationView bottomNavigationView = findViewById(R.id.userMain_BottomNav);

        // disable shiftMode bottom navigation

        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        HomeFragment = new HomeFragment();
        ratingFragment = new HealthFragment();
        EventsFragment = new EventsFragment();
        AccountFragment = new AccountFragment();

        setFragment(HomeFragment);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){

                    case R.id.navigation_home:

                        setFragment(HomeFragment);

                        return true;

                    case R.id.navigation_health:

                        setFragment(ratingFragment);

                        return true;

                    case R.id.navigation_events:

                        setFragment(EventsFragment);

                        return true;

                    case R.id.navigation_timeline:

                        setFragment(AccountFragment);

                        return true;

                    default:

                        return false;
                }
            }
        });


    }

    private void setFragment(Fragment fragment) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(mainFrame.getId(),fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}
