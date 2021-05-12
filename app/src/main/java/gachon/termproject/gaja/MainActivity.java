package gachon.termproject.gaja;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    FragmentManager manager;
    FragmentTransaction ft;

    BuyFragment fragmentBuy;
    EatFragment fragmentEat;
    TotalFragment fragmentTotal;

    Button btn_goP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        manager = getSupportFragmentManager();

        fragmentBuy = new BuyFragment();
        fragmentEat= new EatFragment();
        fragmentTotal = new TotalFragment();

        ft = manager.beginTransaction();
        ft.add(R.id.fragment_container, fragmentTotal).addToBackStack(null).commit();

    }

    // spinner 이용해서 게시판 바꾸게 되면 fragment 이동
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
    }
}