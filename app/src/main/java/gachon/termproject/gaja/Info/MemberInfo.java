package gachon.termproject.gaja.Info;

import java.io.Serializable;
import java.util.ArrayList;

public class MemberInfo implements Serializable {
    String id;
    String nickName;
    ArrayList<String> participatingPost;
    String FCMtoken;

    public MemberInfo(String id, String nickName, ArrayList<String> participatingPost, String FCMtoken) {
        this.id = id;
        this.nickName = nickName;
        this.participatingPost = participatingPost;
        this.FCMtoken = FCMtoken;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickName() {
        return this.nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public ArrayList<String> getParticipatingPost() {
        return this.participatingPost;
    }

    public void setParticipatingPost(ArrayList<String> participatingPost) {
        this.participatingPost = participatingPost;
    }

    public String getFCMtoken() {
        return this.FCMtoken;
    }

    public void setFCMtoken(String id) {
        this.FCMtoken = FCMtoken;
    }
}
