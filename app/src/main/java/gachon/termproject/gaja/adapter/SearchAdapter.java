package gachon.termproject.gaja.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import gachon.termproject.gaja.Info.PostInfo;
import gachon.termproject.gaja.R;
import gachon.termproject.gaja.post.PostInformationActivity;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    private ArrayList<PostInfo> arrayList;
    private Context context;
    private Activity activity;
    //어댑터에서 액티비티 액션을 가져올 때 context가 필요한데 어댑터에는 context가 없다.
    //선택한 액티비티에 대한 context를 가져올 때 필요하다.

    public SearchAdapter(Activity activity,ArrayList<PostInfo> arrayList) {
        this.arrayList = arrayList;
        this.activity = activity;
    }

    @NonNull
    @Override
    //실제 리스트뷰가 어댑터에 연결된 다음에 뷰 홀더를 최초로 만들어낸다.
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        SearchViewHolder holder = new SearchViewHolder(view);

        //카드뷰를 클릭할경우, 그 게시글로 activity가 넘어감.
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(activity, PostInformationActivity.class);
                intent.putExtra("PostInfo", arrayList.get(holder.getAdapterPosition()));
                activity.startActivity(intent);

            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        Glide.with(holder.itemView)
                .load(arrayList.get(position).getTitleImage())
                .into(holder.tn_image);
        holder.tn_postTitle.setText(arrayList.get(position).getTitle());
        holder.tn_uploadTime.setText(new SimpleDateFormat("MM월dd일 hh시mm분", Locale.KOREA).format(arrayList.get(position).getCreatedAt()));
        holder.tn_numberOfPeople.setText(arrayList.get(position).getCurrentNumOfPeople() + " / " + (int) arrayList.get(position).getPeopleNeed());
        holder.tn_publisher.setText(arrayList.get(position).getUserName());
    }

    @Override
    public int getItemCount() {
        // 삼항 연산자
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder {
        ImageView tn_image;
        TextView tn_postTitle;
        TextView tn_uploadTime;
        TextView tn_numberOfPeople;
        TextView tn_publisher;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tn_image = itemView.findViewById(R.id.post_thumbnail);
            this.tn_postTitle = itemView.findViewById(R.id.tn_postTitle);
            this.tn_uploadTime = itemView.findViewById(R.id.tn_uploadTime);
            this.tn_numberOfPeople = itemView.findViewById(R.id.tn_numberOfPeople);
            this.tn_publisher = itemView.findViewById(R.id.tn_publisher);
        }
    }
}