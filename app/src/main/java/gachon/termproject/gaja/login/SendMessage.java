package gachon.termproject.gaja.login;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import gachon.termproject.gaja.Info.PostInfo;

public class SendMessage {
    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;
    HttpConn httpConn;
    ArrayList<String> users;
    String postid;
    String title;
    String text;

    public void SendFull(PostInfo postInfo){
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        httpConn = new HttpConn();
        users = postInfo.getParticipatingUserId();
        postid = postInfo.getPostId();
        title = postInfo.getTitle();
        text = postInfo.getContent();
        if(text.length()>20){
            text = text.substring(0,5) + "..." + text.substring(text.length()-5, text.length());
        }

        firebaseFirestore.collection("users").document(postInfo.getPublisher()).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                DocumentSnapshot document = task.getResult();
                if(document.exists()){
                    String token = document.getData().get("fcmtoken").toString();
                    httpConn.sendData(token, title, text, postid);
                }
            } else{
                //SendFull(postInfo);
            }
        });

        for(int i = 0; i < users.size(); i++){
            firebaseFirestore.collection("users").document(users.get(i)).get().addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        String token = document.getData().get("fcmtoken").toString();
                        httpConn.sendData(token, title, text, postid);
                    }
                } else{
                    //SendFull(postInfo);
                }
            });
        }
    }
}
