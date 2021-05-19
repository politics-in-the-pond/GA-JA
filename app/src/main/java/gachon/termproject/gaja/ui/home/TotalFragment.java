package gachon.termproject.gaja.ui.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;

import gachon.termproject.gaja.Info.PostInfo;
import gachon.termproject.gaja.R;
import gachon.termproject.gaja.adapter.postAdapter;
import gachon.termproject.gaja.post.PostInformationActivity;

public class TotalFragment extends Fragment {
    //파이어베이스 선언
    private FirebaseFirestore firebaseFirestore;
    //레시피 글을 카드뷰로 띄워주기 위한 리사이클러 뷰 선언
    private RecyclerView totalRecyclerView;
    View rootView;

    public static TotalFragment newInstance(){
        TotalFragment TotalFragment=new TotalFragment();
        return TotalFragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_total, container, false);

        firebaseFirestore= FirebaseFirestore.getInstance();

        //리사이클러뷰 작성
        totalRecyclerView = (RecyclerView)rootView.findViewById(R.id.total_List);
        totalRecyclerView.setHasFixedSize(true);
        totalRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        return rootView;
    }

    @Override
    public void onResume(){
        super.onResume();
        //recipePost에 있는 data를 가져오기 위함.
        CollectionReference collectionReference = firebaseFirestore.collection("posts");
        collectionReference
                //추천 높은 순으로 정렬
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            //각 게시글의 정보를 가져와 arrayList에 저장.
                            ArrayList<PostInfo> postList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("로그: ", document.getId() + " => " + document.getData());
                                postList.add(new PostInfo(
                                        document.getData().get("titleImage").toString(),
                                        document.getData().get("title").toString(),
                                        document.getData().get("content").toString(),
                                        document.getData().get("publisher").toString(),
                                        document.getData().get("userName").toString(),
                                        new Date(document.getDate("createdAt").getTime()),
                                        (Long) document.getData().get("peopleNeed"),
                                        (Long) document.getData().get("currentNumOfPeople"),
                                        document.getData().get("postId").toString(),
                                        (ArrayList<String>) document.getData().get("participatingUserId"),
                                        document.getData().get("category").toString()
                                ));
                            }

                            //recipeAdapter를 이용하여 리사이클러 뷰로 내용 띄움.
                            RecyclerView.Adapter mAdapter = new postAdapter(getActivity(), postList);
                            totalRecyclerView.setAdapter(mAdapter);
                        } else {
                            Log.d("로그: ", "Error getting documents: ", task.getException());

                        }
                    }
                });
    }

}