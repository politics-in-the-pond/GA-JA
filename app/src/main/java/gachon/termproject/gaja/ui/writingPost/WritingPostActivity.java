package gachon.termproject.gaja.ui.writingPost;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
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
    private Spinner numberOfPeopleSpinner;
    private String numberOfPeople;
    private Spinner categorySpinner;
    private String category;
    private TextView Date;
    private TextView time;
    private EditText chatlink;
    private boolean datepic = false;
    private boolean timepic = false;
    private boolean dateToday = false;


    //선택시간
    private int y=0, m=0, d=0, h=0, mi=0;

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
        Date = findViewById(R.id.finishDate_Text);
        time = findViewById(R.id.finishTime_Text);
        chatlink = findViewById(R.id.openchat_link);

        //날짜/시간 설정 picker
        Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDate();
            }
        });

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTime();

            }
        });





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


    //data, time picker
    void showDate() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                y = year;
                m = month+1;
                d = dayOfMonth;

                datepic = true;

                @SuppressLint("SimpleDateFormat") String format = new SimpleDateFormat("yyyyMMdd").format(System.currentTimeMillis());
                String sm = Integer.toString(m);
                String sd = Integer.toString(d);
                if(sm.length() == 1) sm = "0" + sm;
                if(sd.length() == 1) sd = "0" + sd;

                String selectDate = Integer.toString(y) + sm + sd;

                if(format.trim().equals(selectDate.trim())){
                    dateToday = true;
                }

                Date.setText(y + "년 " + m + "월 " + d + "일");

            }
        },2019, 1, 11);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    void showTime() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                if(datepic == false){
                    showToast(WritingPostActivity.this , "날짜를 먼저 선택해 주세요");
                }
                else if (dateToday == true){
                    @SuppressLint("SimpleDateFormat") String format = new SimpleDateFormat("HHMM").format(System.currentTimeMillis());

                    String sm = Integer.toString(hourOfDay);
                    String sd = Integer.toString(minute);
                    if(sm.length() == 1) sm = "0" + sm;
                    if(sd.length() == 1) sd = "0" + sd;

                    String selectTime = sm + sd;

                    if(Integer.parseInt(format) > Integer.parseInt(selectTime)){
                        showToast(WritingPostActivity.this , "현재 시각 이후를 선택해 주세요!");
                    }
                    else{
                        h = hourOfDay;
                        mi = minute;
                        timepic = true;

                        time.setText(h + "시 " + mi + "분 까지");
                    }
                }
                else{
                    h = hourOfDay;
                    mi = minute;
                    timepic = true;

                    time.setText(h + "시 " + mi + "분 까지");
                }
            }
        }, 21, 12, true);

        timePickerDialog.show();
    }




    //파이어베이스에 데이터 업로드 하기 위함.
    private void postUpload(){
        //제목 저장.
        final String title = ((EditText)findViewById(R.id.editTitle)).getText().toString();
        final String content = ((EditText)findViewById(R.id.editContent)).getText().toString();
        final String chatlink = ((EditText)findViewById(R.id.openchat_link)).getText().toString();

        final long number = Long.parseLong(numberOfPeople);

        //만약 제목, 내용 모두 공백이 아닐경우 업로드 실행.
        if(title.trim().length() == 0 || content.trim().length() == 0) {
            showToast(WritingPostActivity.this , "제목과 내용을 채워주세요");
        }
        else if(titleImagePath == null) {
            showToast(WritingPostActivity.this , "관련 사진을 등록해 주세요");
        }
        else if(chatlink.trim().length() == 0) {
            showToast(WritingPostActivity.this , "유효한 오픈채팅 링크를 입력해 주세요");
        }
        else if(!datepic || !timepic) {
            showToast(WritingPostActivity.this , "마감 시간을 선택해 주세요");
        }
        else{
            //타이틀이미지 경로 저장.
            String[] titleArray = titleImagePath.split("\\.");

            //업로드를 하고있는 것을 보여주기 위한 로딩창 띄움.
            loaderLayout.setVisibility(View.VISIBLE);

            Date uploadTime = new Date();
            Date finishDate = new Date();

            finishDate.setHours(h);
            finishDate.setMinutes(mi);
            finishDate.setSeconds(0);
            finishDate.setMonth(m - 1);
            finishDate.setYear(y - 1900);
            System.out.println("why" + m + "///" + y + "///" + finishDate);


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
                                                                document.getData().get("id").toString(),
                                                                document.getData().get("nickName").toString(),
                                                                (ArrayList<String>) document.getData().get("participatingPost"),
                                                                document.getData().get("fcmtoken").toString(),
                                                                (ArrayList<String>) document.getData().get("alarmPost")
                                                        );
                                                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                                        DocumentReference documentReference = firebaseFirestore.collection("posts").document();
                                                        //recipepostinfo 형식으로 저장.
                                                        PostInfo postInfo = new PostInfo(titleImagePath, title, content,
                                                                user.getUid(), memberInfo.getNickName(), uploadTime , number ,1, documentReference.getId(), participatingUserId, category, finishDate, chatlink);
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
