package gachon.termproject.gaja.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;

import gachon.termproject.gaja.MainActivity;
import gachon.termproject.gaja.R;

public class LoginActivity extends AppCompatActivity {

    EditText email;
    EditText pw;
    Button login;
    TextView register;
    CheckBox isAutoLogin;
    AutoLoginProvider autoLoginProvider = new AutoLoginProvider();
    private FirebaseAuth mAuth;
    public FirebaseUser user = null;
    File loginFile;
    private RelativeLayout loaderLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBar actionbar = getSupportActionBar();

        ActivityCompat.requestPermissions(LoginActivity.this,
                new String[]{"android.permission.INTERNET"}, 0);
        ActivityCompat.requestPermissions(LoginActivity.this,
                new String[]{"Manifest.permission.READ_EXTERNAL_STORAGE"}, MODE_PRIVATE);
        ActivityCompat.requestPermissions(LoginActivity.this,
                new String[]{"Manifest.permission.WRITE_EXTERNAL_STORAGE"}, MODE_PRIVATE);

        email = (EditText) findViewById(R.id.email);
        pw = (EditText) findViewById(R.id.pw);
        login = (Button) findViewById(R.id.login);
        register = (TextView) findViewById(R.id.register);
        mAuth = FirebaseAuth.getInstance();
        isAutoLogin = (CheckBox) findViewById(R.id.isAutoLogin);
        loginFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/logininfo", "login.dat");
        loaderLayout = findViewById(R.id.loaderLayout);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!email.getText().toString().equals("") && !pw.getText().toString().equals("")) {
                    loaderLayout.setVisibility(View.VISIBLE);
                    loginUser(email.getText().toString(), pw.getText().toString());
                    if (user != null) {

                    }
                } else {
                    Toast.makeText(getApplicationContext(), "이메일 혹은 비밀번호가 공백입니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));//액티비티 이동
            }
        });
    }

    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            user = mAuth.getCurrentUser();
                            user.updateEmail(email);
                            Toast.makeText(getApplicationContext(), "로그인 성공", Toast.LENGTH_SHORT).show();

                            if (isAutoLogin.isChecked()) {
                                autoLoginProvider.AutoLoginWriter(email, password); //자동로그인 설정
                            } else {
                                autoLoginProvider.AutoLoginRemover();
                            }
                            loaderLayout.setVisibility(View.GONE);
                            Intent intent = new Intent(getApplicationContext(), LoginBridge.class);
                            startActivity(intent); //메인으로 이동
                            finish();
                        } else {
                            loaderLayout.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "로그인 실패", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            //reload();
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
            toast = Toast.makeText(this, "뒤로 가기 버튼을 한 번 더 누르시면 종료됩니다.", Toast.LENGTH_LONG);
            toast.show();
            return;
        }
        // 마지막으로 뒤로 가기 버튼을 눌렀던 시간에 2.5초를 더해 현재 시간과 비교 후
        // 마지막으로 뒤로 가기 버튼을 눌렀던 시간이 2.5초가 지나지 않았으면 종료
        if (System.currentTimeMillis() <= backKeyPressedTime + 2500) {
            finish();
            toast.cancel();
            toast = Toast.makeText(this,"이용해 주셔서 감사합니다.",Toast.LENGTH_LONG);
            toast.show();
        }
    }
}