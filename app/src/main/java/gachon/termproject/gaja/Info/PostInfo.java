package gachon.termproject.gaja.Info;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class PostInfo implements Serializable {
    private String titleImage;
    private String title;
    private String content;
    private String publisher;
    private String userName;
    private Date createdAt;
    private long peopleNeed;
    private String postId;
    private ArrayList<String> participatingUserId;
    private String category;

    public PostInfo (String titleImage, String title, String content, String publisher, String userName,
    Date createdAt , long peopleNeed, String postId, ArrayList<String> participatingUserId, String category){
        this.titleImage = titleImage;
        this.title = title;
        this.content = content;
        this.publisher = publisher;
        this.userName = userName;
        this.createdAt = createdAt;
        this.peopleNeed = peopleNeed;
        this.postId = postId;
        this.participatingUserId = participatingUserId;
        this.category = category;
    }

    public String getTitleImage(){ return this.titleImage;}
    public void setTitleImage(String titleImage){this.titleImage = titleImage;}
    public String getTitle(){ return this.title;}
    public void setTitle(String title){this.title = title;}
    public String getContent(){ return this.content;}
    public void setContent(String content){this.content = content;}
    public String getPublisher(){return this.publisher;}
    public void setPublisher(String publisher){this.publisher = publisher;}
    public String getUserName(){return this.userName;}
    public void setUserName(String userName){this.userName = userName;}
    public Date getCreatedAt(){return this.createdAt;}
    public void setCreatedAt(Date publisher){this.createdAt = createdAt;}
    public long getPeopleNeed(){ return this.peopleNeed;}
    public void setPeopleNeed(long peopleNeed){this.peopleNeed = peopleNeed;}
    public String getPostId(){ return this.postId;}
    public void setPostId(String title){this.postId = postId;}
    public ArrayList<String> getParticipatingUserId(){ return  this.participatingUserId;}
    public void setParticipatingUserId(ArrayList<String> participatingUserId){this.participatingUserId = participatingUserId;}
    public String getCategory(){ return this.category;}
    public void setCategory(String category){this.category = category;}
}

