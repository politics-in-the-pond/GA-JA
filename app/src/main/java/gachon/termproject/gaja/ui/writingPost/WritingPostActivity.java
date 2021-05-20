package gachon.termproject.gaja.ui.writingPost;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;

import gachon.termproject.gaja.Info.PostInfo;
import gachon.termproject.gaja.Info.MemberInfo;
import gachon.termproject.gaja.MainActivity;
import gachon.termproject.gaja.R;
import gachon.termproject.gaja.gallery.GalleryActivity;

import static gachon.termproject.gaja.Util.showToast;

public class WritingPostActivity extends AppCompatActivity {

    private static final String TAG ="게시글 작성 화면";
    //유저 선언
    private FirebaseUser user;

    private ImageButton addTitleImageBtn;
    private EditText editTitle;
    private EditText editContent;
    private Spinner numberOfPeopleSpinner;
    private String numberOfPeople;
    private Spinner categorySpinner;
    private String category;

    private RelativeLayout loaderLayout;
    //타이틀 이미지 경로값
    private String titleImagePath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writingpost);

        loaderLayout = findViewById(R.id.loaderLayout);
        addTitleImageBtn = findViewById(R.id.addTitleImageBtn);
        addTitleImageBtn.setOnClickListener(onClickListener);
        findViewById(R.id.confirmBtn).setOnClickListener(onClickListener);
        findViewById(R.id.goBackBtn).setOnClickListener(onClickListener);


        numberOfPeopleSpinner = (Spinner) findViewById(R.id.numberOfPeopleSpinner);
        //태그 카테고리 스피너에서 값을 얻어옴
        numberOfPeopleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                numberOfPeople = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        categorySpinner = (Spinner) findViewById(R.id.categorySpinner);
        //태그 카테고리 스피너에서 값을 얻어옴
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.addTitleImageBtn:
                    myStartActivity(GalleryActivity.class,"image", 0);
                    break;
                case R.id.confirmBtn:
                    postUpload();
                    break;
                case R.id.goBackBtn:
                    myStartActivity(MainActivity.class);
                    break;
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        switch (requestCode){
            case 0:
                //타이틀 이미지 버튼 눌렀을때
                if(resultCode == Activity.RESULT_OK){
                    //타이틀 이미지 관련 코드
                    //타이틀 이미지를 이미지 버튼에 출력
                    String profilePath = data.getStringExtra("profilePath");
                    titleImagePath = profilePath;
                    Glide.with(this).load(profilePath).override(1000).into(addTitleImageBtn);
                }
                break;
        }
    }


    //파이어베이스에 데이터 업로드 하기 위함.
    private void postUpload(){
        //제목 저장.
        final String title = ((EditText)findViewById(R.id.editTitle)).getText().toString();
        //레시피 재료 저장.
        final String content = ((EditText)findViewById(R.id.editContent)).getText().toString();
        //타이틀이미지 경로 저장.
        String[] titleArray = titleImagePath.split("\\.");

        final long number = Long.parseLong(numberOfPeople);
        //만약 제목, 내용 모두 공백이 아닐경우 업로드 실행.
        if(title.length() > 0 && content.length() > 0 && titleArray.length > 0 && number > 1){
            //업로드를 하고있는 것을 보여주기 위한 로딩창 띄움.
            loaderLayout.setVisibility(View.VISIBLE);

            //현재 유저 데이터 가져옴
            user = FirebaseAuth.getInstance().getCurrentUser();
            //Storage 선언
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            //파이어베이스에 있는 데이터베이스 가져옴
            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
            //저장할 위치 선언
            final DocumentReference documentReference = firebaseFirestore.collection("posts").document();


            final StorageReference titleImagesRef = storageRef.child("posts/" + documentReference.getId() + "/title" +titleArray[titleArray.length - 1]);

            try{
                //타이틀 이미지를 storage에 저장함
                InputStream stream = new FileInputStream(new File(titleImagePath));
                StorageMetadata metadata = new StorageMetadata.Builder().setCustomMetadata("title", "title").build();
                UploadTask uploadTask = titleImagesRef.putStream(stream,metadata);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("로그 : " , "실패 " + titleImagePath);

                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        titleImagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                //이미지 경로를 uri로 바꿔 다시 저장. -> uri로 바꾸지 않으면 app에서 보이지 않음.
                                titleImagePath = uri.toString();
                                Log.d("로그 : " , "titleImagePath " + titleImagePath);

                                ArrayList<String> participatingUserId = new ArrayList<>();
                                //유저 아이디를 통해 데이터베이스에 접근하여 이름을 가져옴.
                                firebaseFirestore.collection("users").document(user.getUid()).get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                Log.d(TAG, "다큐먼트 실행");
                                                if (task.isSuccessful()) {
                                                    DocumentSnapshot document = task.getResult();
                                                    if (document.exists()) {
                                                        MemberInfo memberInfo = new MemberInfo(
                                                                document.getData().get("name").toString(),
                                                                document.getData().get("nickname").toString(),
                                                                (ArrayList<String>) document.getData().get("participatingPost")
                                                        );
                                                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                                        DocumentReference documentReference = firebaseFirestore.collection("recipePost").document();
                                                        //recipepostinfo 형식으로 저장.
                                                        PostInfo postInfo = new PostInfo(titleImagePath, title, content,
                                                                user.getUid(), memberInfo.getNickName(), new Date(), number ,1, documentReference.getId(), participatingUserId, category);
                                                        //업로드 실행
                                                        dbUploader(documentReference, postInfo);
                                                    } else {
                                                        Log.d(TAG, "No such document");
                                                    }
                                                } else {
                                                    Log.d(TAG, "get failed with ", task.getException());
                                                }
                                            }
                                        });


                            }
                        });
                    }
                });
            } catch (FileNotFoundException e) {
                Log.e("로그","에러:" + e.toString());
            }



        }
        //만약 레시피의 제목, 내용 재료 가격중 하나라도 공백일경우 실행 X
        else{
            showToast(WritingPostActivity.this , "레시피를 정확히 입력해주세요!");
            showToast(WritingPostActivity.this ,"레시피에는 최소 한개 이상의 이미지가 들어가야 해요!");
        }
    }

    //파이어베이스에 업로드 하기 위함.
    private void dbUploader(DocumentReference documentReference , PostInfo postInfo){
        documentReference.set(postInfo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //등록이 끝나면 로딩창 보이지 않게 함.
                        loaderLayout.setVisibility(View.GONE);
                        showToast(WritingPostActivity.this ,"게시글 등록 성공!");
                        Log.w(TAG,"Success writing document" + documentReference.getId());
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loaderLayout.setVisibility(View.GONE);
                showToast(WritingPostActivity.this ,"게시글 등록 실패.");
                Log.w(TAG,"Error writing document", e);
            }
        });
    }


    private void myStartActivity(Class c){
        Intent intent=new Intent( this, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }



    private void myStartActivity(Class c, String media, int requestCode){
        Intent intent=new Intent( this, c);
        intent.putExtra("media",media);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(intent, requestCode);
    }
}
