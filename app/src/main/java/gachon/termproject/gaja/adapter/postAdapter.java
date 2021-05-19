package gachon.termproject.gaja.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;


import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import gachon.termproject.gaja.Info.PostInfo;
import gachon.termproject.gaja.post.PostInformationActivity;
import gachon.termproject.gaja.R;

import static gachon.termproject.gaja.Util.isStorageUrl;


//레시피 게시판의 글을 카드뷰로 보여주기 위한 어댑터
public class postAdapter extends RecyclerView.Adapter<postAdapter.postViewHolder> {
    //레시피게시판 글 데이터
    private ArrayList<PostInfo> mDataset;
    private Activity activity;

    static class postViewHolder extends RecyclerView.ViewHolder{
        public CardView cardView;
        postViewHolder(Activity activity, CardView v, PostInfo postInfo){
            super(v);
            cardView = v;
        }
    }

    public postAdapter(Activity activity, ArrayList<PostInfo> postDataset){
        mDataset = postDataset;
        this.activity = activity;
    }

    @Override
    public int getItemViewType(int position){
        return position;
    }

    //카드뷰를 생성하여 그곳에 데이터를 집어넣어 완성시킴
    @NotNull
    @Override
    public postAdapter.postViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType){
        CardView cardView =(CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent,false);
        final postViewHolder postViewHolder = new postViewHolder(activity, cardView, mDataset.get(viewType));
        //카드뷰를 클릭할경우, 그 게시글로 activity가 넘어감.
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(activity, PostInformationActivity.class);
                intent.putExtra("PostInfo", mDataset.get(postViewHolder.getAdapterPosition()));
                activity.startActivity(intent);
            }
        });
        return postViewHolder;
    }

    //카드뷰 안에 들어갈 목록
    //레시피게시판 게시글 카드뷰에는 제목, 타이틀 이미지 , 작성자, 작성 날짜, 추천수가 저장되어 띄워짐.
    @Override
    public void onBindViewHolder(@NotNull final postViewHolder holder, int position){
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        CardView cardView = holder.cardView;
        cardView.setLayoutParams(layoutParams);
        ImageView titleImage = cardView.findViewById(R.id.post_thumbnail);
        String titleImagePath = mDataset.get(position).getTitleImage();
        if(isStorageUrl(titleImagePath)){
            Glide.with(activity).load(titleImagePath).override(1000).thumbnail(0.1f).into(titleImage);
        }
        TextView title = cardView.findViewById(R.id.tn_postTitle);
        title.setText(mDataset.get(position).getTitle());

        TextView userName = cardView.findViewById(R.id.tn_publisher);
        userName.setText(mDataset.get(position).getUserName());

        TextView createdAt = cardView.findViewById(R.id.tn_uploadTime);
        createdAt.setText(new SimpleDateFormat("MM-dd hh:mm", Locale.KOREA).format(mDataset.get(position).getCreatedAt()));

        TextView numberOfPeople = cardView.findViewById(R.id.tn_numberOfPeople);
        int currentPeople = (int) mDataset.get(position).getCurrentNumOfPeople();
        numberOfPeople.setText(currentPeople + " / " + (int) mDataset.get(position).getPeopleNeed());

    }

    @Override
    public int getItemCount(){
        return mDataset.size();
    }

}
