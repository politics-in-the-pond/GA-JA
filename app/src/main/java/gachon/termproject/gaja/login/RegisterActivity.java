package gachon.termproject.gaja.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import gachon.termproject.gaja.Info.MemberInfo;
import gachon.termproject.gaja.MainActivity;
import gachon.termproject.gaja.R;

import static gachon.termproject.gaja.Util.showToast;

public class RegisterActivity extends AppCompatActivity {
    private static String TAG = "회원가입";
    EditText email;
    EditText pw;
    EditText nickName;
    TextView login;
    Button register;
    private FirebaseAuth mAuth;
    public FirebaseUser user = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ActionBar actionbar = getSupportActionBar();
        actionbar.hide();

        email = (EditText) findViewById(R.id.email);
        pw = (EditText) findViewById(R.id.pw);
        login = (TextView) findViewById(R.id.gotologin);
        register = (Button) findViewById(R.id.register);
        nickName = (EditText) findViewById(R.id.nickname);
        mAuth = FirebaseAuth.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!email.getText().toString().equals("") && !pw.getText().toString().equals("") && !nickName.getText().toString().equals("")) {
                    registerUser(email.getText().toString(), pw.getText().toString());
/*                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    ArrayList<String> participatingPost = new ArrayList<>();
                    MemberInfo memberInfo = new MemberInfo(user.getUid(), nickName.getText().toString(), participatingPost);
                    db.collection("users").document(user.getUid()).set(memberInfo)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    showToast(RegisterActivity.this, "회원정보 등록을 성공하였습니다.");
                                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    showToast(RegisterActivity.this, "회원정보 등록에 실패하였습니다.");
                                    Log.w(TAG, "Error writing document", e);
                                }
                            });

                    finish(); //로그인창으로 돌아가기*/
                } else {
                    Toast.makeText(getApplicationContext(), "이메일 혹은 비밀번호 혹은 이름이 공백입니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); //로그인창으로 돌아가기
            }
        });
    }

    private void registerUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            user = mAuth.getCurrentUser();
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            ArrayList<String> participatingPost = new ArrayList<>();
                            MemberInfo memberInfo = new MemberInfo(user.getUid(), nickName.getText().toString(), participatingPost, "");
                            db.collection("users").document(user.getUid()).set(memberInfo)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            showToast(RegisterActivity.this, "회원정보 등록을 성공하였습니다. 다시 로그인 해주세요.");
                                            mAuth.signOut();
                                            finish();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            showToast(RegisterActivity.this, "회원정보 등록에 실패하였습니다.");
                                            Log.w(TAG, "Error writing document", e);
                                        }
                                    });
                            finish();
                            //액티비티 이동
                        } else {
                            Toast.makeText(getApplicationContext(), "회원가입 실패", Toast.LENGTH_SHORT).show();
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
}