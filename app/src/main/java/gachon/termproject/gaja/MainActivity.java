package gachon.termproject.gaja;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import gachon.termproject.gaja.login.LoginBridge;
import gachon.termproject.gaja.login.SplashActivity;
import gachon.termproject.gaja.ui.alarm.AlarmFragment;
import gachon.termproject.gaja.ui.home.HomeFragment;
import gachon.termproject.gaja.ui.mypage.MypageFragment;

public class MainActivity extends AppCompatActivity{
   /* FragmentManager manager;
    FragmentTransaction ft;*/

    Button btn_goP;
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private HomeFragment homeFragment = new HomeFragment();
    private MypageFragment MyPageFragment = new MypageFragment();
    private AlarmFragment AlarmFragment = new AlarmFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.nav_host_fragment, homeFragment).commitAllowingStateLoss();
        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(new ItemSelectedListener());

    }




    private class ItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            switch (menuItem.getItemId()) {
                case R.id.navigation_home:
                    transaction.replace(R.id.nav_host_fragment, homeFragment).commitAllowingStateLoss();
                    break;
                case R.id.navigation_alarm:
                    transaction.replace(R.id.nav_host_fragment, AlarmFragment).commitAllowingStateLoss();
                    break;

                case R.id.navigation_mypage:
                    transaction.replace(R.id.nav_host_fragment, MyPageFragment).commitAllowingStateLoss();
                    break;
            }
            return true;
        }
    }

    // 마지막으로 뒤로 가기 버튼을 눌렀던 시간 저장
    private long backKeyPressedTime = 0;
    // 첫 번째 뒤로 가기 버튼을 누를 때 표시
    private Toast toast;


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        // 기존 뒤로 가기 버튼의 기능을 막기 위해 주석 처리 또는 삭제

        // 마지막으로 뒤로 가기 버튼을 눌렀던 시간에 2.5초를 더해 현재 시간과 비교 후
        // 마지막으로 뒤로 가기 버튼을 눌렀던 시간이 2.5초가 지났으면 Toast 출력
        // 2500 milliseconds = 2.5 seconds
        if (System.currentTimeMillis() > backKeyPressedTime + 2500) {
            backKeyPressedTime = System.currentTimeMillis();
            toast = Toast.makeText(this, "뒤로 가기 버튼을 한 번 더 누르시면 로그인 화면으로 돌아갑니다.", Toast.LENGTH_LONG);
            toast.show();
            return;
        }
        // 마지막으로 뒤로 가기 버튼을 눌렀던 시간에 2.5초를 더해 현재 시간과 비교 후
        // 마지막으로 뒤로 가기 버튼을 눌렀던 시간이 2.5초가 지나지 않았으면 종료
        if (System.currentTimeMillis() <= backKeyPressedTime + 2500) {
            Intent finishIntent = new Intent(getApplicationContext(), SplashActivity.class);
            startActivity(finishIntent);
            finish();
            toast.cancel();
            toast.show();
        }
    }


}