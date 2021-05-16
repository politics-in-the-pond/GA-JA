package gachon.termproject.gaja.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import gachon.termproject.gaja.R;
import gachon.termproject.gaja.adapter.FragmentAdapter;
import gachon.termproject.gaja.ui.writingPost.WritingPostActivity;

public class HomeFragment extends Fragment {
    private ViewPager vp;
    FragmentAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        adapter =  new FragmentAdapter(getChildFragmentManager(), 1);
        root.findViewById(R.id.writingPostBtn).setOnClickListener(onClickListener);
        vp=(ViewPager)root.findViewById(R.id.share_need_container);
        adapter.addFragment(new TotalFragment(), "전체");
        adapter.addFragment(new BuyFragment(), "같이 먹어요");
        adapter.addFragment(new EatFragment(), "같이 사요");
        vp.setAdapter(adapter);
        TabLayout tab=root.findViewById(R.id.tabLayout);
        tab.setupWithViewPager(vp);

        return root;
    }

    //만약 게시글 작성 버튼을 누르면 작성 화면으로 넘어가게 하기 위한 리스너
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.writingPostBtn:
                    myStartActivity(WritingPostActivity.class);
                    break;
            }
        }
    };

    //activity를 실행하기 위한 함수.
    private void myStartActivity(Class c){
        Intent intent=new Intent(getActivity(), c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

}