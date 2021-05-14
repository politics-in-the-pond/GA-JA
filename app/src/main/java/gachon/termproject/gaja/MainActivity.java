package gachon.termproject.gaja;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import gachon.termproject.gaja.ui.alarm.AlarmFragment;
import gachon.termproject.gaja.ui.home.HomeFragment;
import gachon.termproject.gaja.ui.mypage.MypageFragment;

public class MainActivity extends AppCompatActivity {
   /* FragmentManager manager;
    FragmentTransaction ft;*/

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private HomeFragment homeFragment = new HomeFragment();
    private MypageFragment MyPageFragment = new MypageFragment();
    private AlarmFragment AlarmFragment=new AlarmFragment();

    Button btn_goP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       // manager = getSupportFragmentManager();

       /* fragmentBuy = new BuyFragment();
        fragmentEat= new EatFragment();
        fragmentTotal = new TotalFragment();*/

       // ft = manager.beginTransaction();


        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.nav_host_fragment, homeFragment).commitAllowingStateLoss();
        BottomNavigationView bottomNavigationView  = findViewById(R.id.nav_view);
        bottomNavigationView .setOnNavigationItemSelectedListener(new ItemSelectedListener());

        // ft.add(R.id.fragment_container, fragmentTotal).addToBackStack(null).commit();

    }

   /* // spinner 이용해서 게시판 바꾸게 되면 fragment 이동
    public void onButtonClicked(View view) {
        ft = manager.beginTransaction();

        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        if(String.valueOf(spinner.getSelectedItem()).equalsIgnoreCase("전체")){
            ft.replace(R.id.fragment_container, fragmentTotal).commit();
           // Toast.makeText(this, String.valueOf(spinner.getSelectedItem()), Toast.LENGTH_LONG).show();
        }
        else if(String.valueOf(spinner.getSelectedItem()).equalsIgnoreCase("같이 먹어요")){
            ft.replace(R.id.fragment_container, fragmentEat).commit();
            //Toast.makeText(this, String.valueOf(spinner.getSelectedItem()), Toast.LENGTH_LONG).show();
        }
        else if((String.valueOf(spinner.getSelectedItem()).equalsIgnoreCase("같이 사요"))){
            ft.replace(R.id.fragment_container, fragmentBuy).commit();
           // Toast.makeText(this, String.valueOf(spinner.getSelectedItem()), Toast.LENGTH_LONG).show();
        }
    }*/

    private class ItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            switch(menuItem.getItemId())
            {
                case R.id.navigation_home:
                    transaction.replace(R.id.nav_host_fragment, homeFragment).commitAllowingStateLoss();
                    break;
                case R.id.navigation_alarm:
                    transaction.replace(R.id.nav_host_fragment,AlarmFragment).commitAllowingStateLoss();
                    break;

                case R.id.navigation_mypage:
                    transaction.replace(R.id.nav_host_fragment, MyPageFragment).commitAllowingStateLoss();
                    break;
            }
            return true;
        }
    }
}