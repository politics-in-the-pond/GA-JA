package gachon.termproject.gaja.ui.mypage;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;

import gachon.termproject.gaja.Info.MemberInfo;
import gachon.termproject.gaja.Info.PostInfo;
import gachon.termproject.gaja.MainActivity;
import gachon.termproject.gaja.R;
import gachon.termproject.gaja.adapter.FragmentAdapter;
import gachon.termproject.gaja.ui.home.BuyFragment;
import gachon.termproject.gaja.ui.home.EatFragment;
import gachon.termproject.gaja.ui.home.TotalFragment;

public class MypageFragment extends Fragment {
    final private String TAG = "마이페이지";
    private ViewPager vp;
    FragmentAdapter adapter;

    private TextView nickname;
    private TextView email;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser user;

    View view;
    public View onCreateView(@NonNull LayoutInflater inflater,
       ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mypage, container, false);
        firebaseFirestore = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        return view;
    }

    @Override
    public void onResume(){
        super.onResume();

        nickname = view.findViewById(R.id.nickname);
        email = view.findViewById(R.id.email);

        firebaseFirestore.collection("users").document(user.getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        Log.d(TAG, "다큐먼트 실행");
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            if (document.exists()) {
                                MemberInfo memberInfo = new MemberInfo(
                                        document.getData().get("id").toString(),
                                        document.getData().get("nickName").toString(),
                                        (ArrayList<String>) document.getData().get("participatingPost"),
                                        document.getData().get("fcmtoken").toString()
                                );
                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                nickname.setText(memberInfo.getNickName());
                                email.setText(user.getEmail());
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
        adapter =  new FragmentAdapter(getChildFragmentManager(), 1);
        vp=(ViewPager)view.findViewById(R.id.myPage_container);
        setupViewPager(vp);
        TabLayout tab=view.findViewById(R.id.mypage_tabLayout);
        tab.setupWithViewPager(vp);


    }

    private void setupViewPager(ViewPager vp) {
        adapter.addFragment(new MyPostFragment(), "내가 쓴 글");
        adapter.addFragment(new MyEnrollmentFragment(), "신청한 글");
        vp.setAdapter(adapter);
    }
}