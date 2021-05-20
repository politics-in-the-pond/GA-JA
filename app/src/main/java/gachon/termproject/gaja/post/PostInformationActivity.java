package gachon.termproject.gaja.post;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import gachon.termproject.gaja.Info.MemberInfo;
import gachon.termproject.gaja.Info.PostInfo;

import gachon.termproject.gaja.R;
import gachon.termproject.gaja.adapter.postAdapter;

import static gachon.termproject.gaja.Util.isStorageUrl;
import static gachon.termproject.gaja.Util.showToast;

public class PostInformationActivity extends AppCompatActivity {

    private final String TAG = "게시글 정보";
    //데이터베이스 선언
    private FirebaseFirestore firebaseFirestore;
    //리사이클러 뷰 선언
    private RecyclerView another_Post;
    //게시글 정보 가져오기 위함
    private PostInfo postInfo;
    //신고 버튼
    private Button reportBtn;
    //참여 버튼
    private Button enrollmentBtn;

    private DocumentReference dr;

    //파이어베이스에서 유저 정보 가져오기위해 선언.
    FirebaseUser firebaseUser;
    //유저 아이디
    String user;
    //게시글 아이디
    String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

    }

    //게시글 참여인원수가 증가했거나 감소했을경우, 또는 게시글이 작성되었을 경우 바로바로 업데이트 해주기 위해 resume함수에 넣어 관리.
    @Override
    protected void onResume() {
        super.onResume();

        firebaseFirestore= FirebaseFirestore.getInstance();//데이터베이스 선언

        //유저가 이미 추천을 했는지 하지않았는지 알려주기 위함.
        enrollmentBtn = findViewById(R.id.btn_enrollment);
        enrollmentBtn.setOnClickListener(onClickListener);
        findViewById(R.id.btn_report).setOnClickListener(onClickListener);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser(); //파이어베이스 유저 선언
        user = firebaseUser.getUid();

        //게시글 data 가져옴
        postInfo = (PostInfo) getIntent().getSerializableExtra("PostInfo");
        //추천한 유저 명단
        ArrayList<String> participatingUser = postInfo.getParticipatingUserId();
        //만약 현재 유저가 게시글에 이미 참가를 눌렀다면
        if(participatingUser.contains(user))
        {
            enrollmentBtn.setText("참여 취소");
        }
        //누르지 않았다면
        else{
            enrollmentBtn.setText("참여");
        }



        //게시글 정보 띄우기 위한 코드
        //시작
        String id = getIntent().getStringExtra("id");
        Log.d("로그: ", "" + getIntent().getStringExtra("id"));

        ImageView postInfoTitleImage = findViewById(R.id.post_image);

        String infoTitleImagePath = postInfo.getTitleImage();
        if(isStorageUrl(infoTitleImagePath)){
            Glide.with(this).load(infoTitleImagePath).override(1000).thumbnail(0.1f).into(postInfoTitleImage);
        }
        Log.d("로그","" + postInfo.getTitleImage());

        TextView postInfoTitle = findViewById(R.id.post_title);
        postInfoTitle.setText(postInfo.getTitle());
        Log.d("로그","" + postInfo.getTitle());

        TextView postPeople = findViewById(R.id.post_enrollment);
        postPeople.setText(Integer.toString((int) postInfo.getCurrentNumOfPeople()) + " / " + Integer.toString((int) postInfo.getPeopleNeed()));
        Log.d("로그","" + postInfo.getCurrentNumOfPeople() + " / " + postInfo.getPeopleNeed());

        TextView postCreatedAt = findViewById(R.id.post_uploadtime);
        postCreatedAt.setText(new SimpleDateFormat("MM-dd hh:mm", Locale.KOREA).format(postInfo.getCreatedAt()));
        Log.d("로그","" + postInfo.getCreatedAt());

        TextView postContent = findViewById(R.id.post_content);
        postContent.setText(postInfo.getContent());

        //끝

        //다른 추천레시피 띄우기 위한 코드
        //시작

        another_Post = findViewById(R.id.another_post);
        another_Post.setHasFixedSize(true);
        another_Post.setLayoutManager(new LinearLayoutManager(PostInformationActivity.this, LinearLayoutManager.HORIZONTAL, false));
        CollectionReference collectionReference = firebaseFirestore.collection("posts");
        collectionReference
                .whereEqualTo("category", postInfo.getCategory())
                .orderBy("createdAt", Query.Direction.DESCENDING).limit(4)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
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

                            RecyclerView.Adapter mAdapter = new postAdapter(PostInformationActivity.this, postList);
                            another_Post.setAdapter(mAdapter);
                        } else {
                            Log.d("로그: ", "Error getting documents: ", task.getException());

                        }
                    }
                });

        //끝
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_enrollment:
                    ArrayList<String> newParticipatingUserId = new ArrayList<>();
                    firebaseUser = FirebaseAuth.getInstance().getCurrentUser(); //파이어베이스 유저 선언
                    user = firebaseUser.getUid();
                    id = postInfo.getPostId();

                    firebaseFirestore.collection("users").document(user).get()
                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    Log.d(TAG, "다큐먼트 실행");
                                    if (task.isSuccessful()) {
                                        ArrayList<String> participatingPost = new ArrayList<>();
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            MemberInfo memberInfo = new MemberInfo(
                                                    document.getData().get("name").toString(),
                                                    document.getData().get("nickname").toString(),
                                                    (ArrayList<String>) document.getData().get("participatingPost")
                                            );
                                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                            if(memberInfo.getParticipatingPost()==null){
                                                participatingPost.add(id);
                                                memberInfo.setParticipatingPost(participatingPost);
                                                if(user == null){
                                                    dr = firebaseFirestore.collection("users").document();

                                                }else{
                                                    dr =firebaseFirestore.collection("users").document(user);
                                                }
                                                Log.d(TAG, "유저 아이디 : " + user);
                                                dbUploader(dr, memberInfo, 0);
                                            }

                                            else if(memberInfo.getParticipatingPost().contains(id))
                                            {
                                                participatingPost = memberInfo.getParticipatingPost();
                                                participatingPost.remove(id);
                                                memberInfo.setParticipatingPost(participatingPost);
                                                if(user == null){
                                                    dr = firebaseFirestore.collection("users").document();

                                                }else{
                                                    dr =firebaseFirestore.collection("users").document(user);

                                                }
                                                Log.d(TAG, "유저 아이디 : " + user);
                                                dbUploader(dr, memberInfo, 1);
                                            }
                                            else{
                                                participatingPost = memberInfo.getParticipatingPost();
                                                participatingPost.add(id);
                                                memberInfo.setParticipatingPost(participatingPost);
                                                if(user == null){
                                                    dr = firebaseFirestore.collection("users").document();

                                                }else{
                                                    dr =firebaseFirestore.collection("users").document(user);

                                                }
                                                Log.d(TAG, "유저 아이디 : " + user);
                                                dbUploader(dr, memberInfo, 0);
                                            }
                                        } else {
                                            Log.d(TAG, "No such document");
                                        }
                                    } else {
                                        Log.d(TAG, "get failed with ", task.getException());
                                    }
                                }
                            });

                    if(postInfo.getParticipatingUserId().contains(user))
                    {
                        postInfo.setCurrentNumOfPeople(postInfo.getCurrentNumOfPeople() - 1);
                        newParticipatingUserId = postInfo.getParticipatingUserId();
                        newParticipatingUserId.remove(user);
                        postInfo.setParticipatingUserId(newParticipatingUserId);
                        if(id == null){
                            dr = firebaseFirestore.collection("posts").document();

                        }else{
                            dr =firebaseFirestore.collection("posts").document(id);

                        }
                        dbUploader(dr, postInfo, 0);
                        break;
                    }
                    else{
                        postInfo.setCurrentNumOfPeople(postInfo.getCurrentNumOfPeople() + 1);
                        newParticipatingUserId = postInfo.getParticipatingUserId();
                        newParticipatingUserId.add(user);
                        postInfo.setParticipatingUserId(newParticipatingUserId);
                        if(id == null){
                            dr = firebaseFirestore.collection("posts").document();

                        }else{
                            dr =firebaseFirestore.collection("posts").document(id);

                        }
                        dbUploader(dr, postInfo, 1);
                        break;
                    }

                case R.id.btn_report:
                    break;
            }
        }
    };

    //참여, 참여 취소시 바로바로 데이터베이스에 업로드하여 반영해줌.(게시글)
    private void dbUploader(DocumentReference documentReference , PostInfo postInfo, int requestCode) {
        //참여취소 하는 경우
        if (requestCode == 0) {
            documentReference.set(postInfo)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            showToast(PostInformationActivity.this, "참여를 취소했어요!");
                            Log.w(TAG, "Success writing document" + documentReference.getId());
                            onResume();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    showToast(PostInformationActivity.this, "참여취소에 실패했어요!");
                    Log.w(TAG, "Error writing document", e);
                }
            });
        }
        //추천할 경우
        else {
            documentReference.set(postInfo)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            showToast(PostInformationActivity.this, "게시글에 참여했어요!");
                            Log.w(TAG, "Success writing document" + documentReference.getId());
                            onResume();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    showToast(PostInformationActivity.this, "게시글 참여에 실패했어요!");
                    Log.w(TAG, "Error writing document", e);
                }
            });
        }
    }

        //참여, 참여 취소시 바로바로 데이터베이스에 업로드하여 반영해줌.(유저)
    private void dbUploader(DocumentReference documentReference , MemberInfo memberInfo, int requestCode){
            //참여취소 하는 경우
            if(requestCode == 0){
                documentReference.set(memberInfo)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                showToast(PostInformationActivity.this ,"참여를 취소했어요!");
                                Log.w(TAG,"Success writing document" + documentReference.getId());
                                onResume();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showToast(PostInformationActivity.this ,"참여취소에 실패했어요!");
                        Log.w(TAG,"Error writing document", e);
                    }
                });
            }
            //추천할 경우
            else{
                documentReference.set(memberInfo)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                showToast(PostInformationActivity.this ,"게시글에 참여했어요!");
                                Log.w(TAG,"Success writing document" + documentReference.getId());
                                onResume();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showToast(PostInformationActivity.this ,"게시글 참여에 실패했어요!");
                        Log.w(TAG,"Error writing document", e);
                    }
                });
            }

    }
}