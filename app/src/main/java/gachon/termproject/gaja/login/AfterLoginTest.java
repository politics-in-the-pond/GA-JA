package gachon.termproject.gaja.login;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import gachon.termproject.gaja.R;

public class AfterLoginTest extends AppCompatActivity {

    public FirebaseUser user;
    TextView test;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afterlogintest);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        test = (TextView) findViewById(R.id.test1);

        test.setText(user.getEmail());
    }
}