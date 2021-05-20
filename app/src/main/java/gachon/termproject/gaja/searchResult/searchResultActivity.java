package gachon.termproject.gaja.searchResult;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
import gachon.termproject.gaja.adapter.SearchAdapter;
public class searchResultActivity extends AppCompatActivity{

    private String TAG = "검색결과화면";
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<PostInfo> arrayList;
    private FirebaseFirestore database;
    private CollectionReference collectionReference ;
    private String search_content;

    @Nullable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_search_result);

        search_content = (String) getIntent().getSerializableExtra("Query");
        Log.d(TAG, "" + search_content);

        recyclerView = (RecyclerView) findViewById(R.id.searchRecyclerView);
        recyclerView.setHasFixedSize(true); // 리사이클러뷰 기존성능 강화
        layoutManager = new GridLayoutManager(searchResultActivity.this, 2);
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>(); // User 객체를 담을 어레이 리스트 (어댑터쪽으로)

        database = FirebaseFirestore.getInstance(); //데이터베이스 선언 // 파이어베이스 데이터베이스 연동
        collectionReference = database.collection("posts"); // DB 테이블 연결
        Log.d(TAG, "검색 시작");

        collectionReference
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("로그: ", document.getId() + " => " + document.getData());
                                PostInfo postInfo = new PostInfo(
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
                                        document.getData().get("category").toString(),
                                        new Date(document.getDate("finishTime").getTime()),
                                        document.getData().get("talkLink").toString()
                                );

                                long gap = postInfo.getFinishTime().getTime() - new Date().getTime();
                                if (gap < 0) {
                                    //마감
                                } else if (postInfo.getCurrentNumOfPeople() == postInfo.getPeopleNeed()) {
                                    //인원 꽉참
                                } else {
                                    if (postInfo.getTitle().contains(search_content)) {
                                        arrayList.add(postInfo);
                                    }
                                }
                            }
                            Log.d(TAG, "검색 중");
                            adapter = new SearchAdapter(searchResultActivity.this, arrayList);
                            recyclerView.setAdapter(adapter); // 리사이클러뷰에 어댑터 연결

                        } else {
                            Log.d("로그: ", "Error getting documents: ", task.getException());

                        }
                    }
                });

        Log.d(TAG, "검색 끝");

    }

}