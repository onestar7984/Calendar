package onestar.calendar;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TabHost;

public class CalendarActivity extends AppCompatActivity implements View.OnClickListener{
    private TabLayout tabLayout;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private Add add;
    private Button bt_add;
    private int dlgWidth;
    private int dlgHight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        DBHelper dbHelper = new DBHelper(this, "Schedule", null, 1);

        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
        dlgWidth = dm.widthPixels/4;
        dlgHight = dm.heightPixels/4;

        bt_add = (Button)findViewById(R.id.bt_add);
        bt_add.setOnClickListener(this);

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_view, new Month());
        fragmentTransaction.commit();

        tabLayout = (TabLayout)findViewById(R.id.tap_container);
        tabLayout.addTab(tabLayout.newTab().setText("Monthly"));
        tabLayout.addTab(tabLayout.newTab().setText("Weekly"));
        tabLayout.addTab(tabLayout.newTab().setText("Daily"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        tabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        fragmentManager = getSupportFragmentManager();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.fragment_view, new Month());
                        fragmentTransaction.commit();
                        break;
                    case 1:
                        fragmentManager = getSupportFragmentManager();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.fragment_view, new Week());
                        fragmentTransaction.commit();
                        break;
                    case 2:
                        fragmentManager = getSupportFragmentManager();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.fragment_view, new Day());
                        fragmentTransaction.commit();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_add:
                add = new Add(this);
                WindowManager.LayoutParams wm = add.getWindow().getAttributes();
                wm.copyFrom(add.getWindow().getAttributes());
                wm.width = dlgWidth*3;
                wm.height = dlgHight*3;
                add.show();
                Log.i("dialog log", "종료");

        }
    }

    public int getTabNumber(){
        return tabLayout.getSelectedTabPosition();
    }

    public void refreshFragment(){
        getSupportFragmentManager().beginTransaction().detach(fragmentManager.findFragmentById(R.id.fragment_view)).attach(fragmentManager.findFragmentById(R.id.fragment_view)).commit();
    }
}
