package gachon.termproject.gaja;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lakue.lakuepopupactivity.PopupActivity;
import com.lakue.lakuepopupactivity.PopupGravity;
import com.lakue.lakuepopupactivity.PopupType;

public class PostActivity extends AppCompatActivity {

    Button btn_goM;
    Button btn_r;
    EditText editName;
    LinearLayout diaglogView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);


        // 스크롤뷰 동작하는지 보려고 임의로 만들었음.
        TextView text = (TextView)findViewById(R.id.post_content);
        String txt="";
        for(int i=0;i<50;i++){
            txt+=i+ "\n";
            text.setText(txt);
        }

        btn_goM = (Button)findViewById(R.id.go_to_main);
        btn_goM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                Intent intent = new Intent (getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }

        });


        btn_r = (Button)findViewById(R.id.btn_report);
        btn_r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {




            }

        });

    }

}