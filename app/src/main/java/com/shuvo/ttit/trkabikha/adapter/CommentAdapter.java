package com.shuvo.ttit.trkabikha.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shuvo.ttit.trkabikha.R;
import com.shuvo.ttit.trkabikha.arraylist.CommentList;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentHolder> {

    private final ArrayList<CommentList> mCategoryItem;
    private final Context myContext;

    public CommentAdapter(ArrayList<CommentList> mCategoryItem, Context myContext) {
        this.mCategoryItem = mCategoryItem;
        this.myContext = myContext;
    }


    public static class CommentHolder extends RecyclerView.ViewHolder {


        public TextView commentator;
        public TextView comment;
        public TextView time;

        public CommentHolder(@NonNull View itemView) {
            super(itemView);
            commentator = itemView.findViewById(R.id.name_of_commentor_list);
            comment = itemView.findViewById(R.id.comments_of_commentor_list);
            time = itemView.findViewById(R.id.time_of_commentor_list);

        }
    }

    @NonNull
    @Override
    public CommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.comments_list_view, parent, false);
        return new CommentHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentHolder holder, int position) {
        CommentList categoryItem = mCategoryItem.get(position);

        holder.commentator.setText(categoryItem.getCommentator());
        holder.comment.setText(categoryItem.getComment());
        holder.time.setText(categoryItem.getComment_time());
    }


    @Override
    public int getItemCount() {
        return mCategoryItem.size();
    }
}
