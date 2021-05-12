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


        // db 연동 해서 formatArgs 부분에 넣으면 될,,듯...?
        TextView text_title = (TextView)findViewById(R.id.post_title);
        text_title.setText(getString(R.string.title,"엽떡 같이 먹을 사람 구해요"));

        TextView text_upload = (TextView)findViewById(R.id.post_uploadtime);
        text_upload.setText(getString(R.string.upload_time,"13:18"));

        TextView text_enroll = (TextView)findViewById(R.id.post_enrollment);
        text_enroll.setText(getString(R.string.enroll_person,2)
                            +" / "
                            +getString(R.string.total_person,3)
                            +" 명");


        // 스크롤뷰 동작하는지 보려고 임의로 만들었음.
        TextView text = (TextView)findViewById(R.id.post_content);
        String txt="";
        for(int i=0;i<50;i++){
            txt+=i+ "\n";
            text.setText(txt);
        }

        // 화살표 버튼 이미지로 변경하기 (디자인 부분 )
        //뒤로 가기 눌렀을때 main 말고 이전에 있었던 게시판으로 돌아가기
        // 지금은 어디에 있던 전체 게시판으로 돌아가짐.
        btn_goM = (Button)findViewById(R.id.go_to_main);
        btn_goM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                Intent intent = new Intent (getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }

        });

        // 신고 버튼
        btn_r = (Button)findViewById(R.id.btn_report);
        btn_r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {

                // 신고 하기 팝업창 구현 dialog View 이용 예정
            }

        });

    }

}