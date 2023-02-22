package com.shuvo.ttit.trkabikha.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.shuvo.ttit.trkabikha.R;
import com.shuvo.ttit.trkabikha.arraylist.PhotoList;
import com.shuvo.ttit.trkabikha.dialogue.PicDialogue;

import java.util.ArrayList;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PHOTOHOLDER> {

    private final ArrayList<PhotoList> mCategoryItem;
    private final Context myContext;

    public static String urlFromPhotoAdapter = "";
    public static Bitmap bitmapFromCompletedPhotoAdapter = null;

    public PhotoAdapter(ArrayList<PhotoList> mCategoryItem, Context myContext) {
        this.mCategoryItem = mCategoryItem;
        this.myContext = myContext;
    }

    public class PHOTOHOLDER extends RecyclerView.ViewHolder {


        public ImageView imageView;
        public TextView uploadDate;
        public TextView stage;

        public PHOTOHOLDER(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_from);
            uploadDate = itemView.findViewById(R.id.upload_date_text);
            stage = itemView.findViewById(R.id.working_stage_text);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                    urlFromPhotoAdapter = mCategoryItem.get(getAdapterPosition()).getPhotoName();
                    bitmapFromCompletedPhotoAdapter = mCategoryItem.get(getAdapterPosition()).getImage();
                    PicDialogue picDialogue = new PicDialogue();
                    picDialogue.show(activity.getSupportFragmentManager(),"PICTURE");
                }
            });

        }
    }

    @NonNull
    @Override
    public PHOTOHOLDER onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.photo_list_view, parent, false);
        return new PHOTOHOLDER(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PHOTOHOLDER holder, int position) {
        PhotoList categoryItem = mCategoryItem.get(position);

        holder.uploadDate.setText(categoryItem.getUploadDate());
        holder.stage.setText(categoryItem.getStage());
//        Glide.with(myContext)
//                .load(categoryItem.getImage())
//                .error(R.drawable.loading_error)
//                .placeholder(R.drawable.loading)
//                .into(holder.imageView);
        Glide.with(myContext).load(categoryItem.getPhotoName()).error(categoryItem.getImage()).placeholder(R.drawable.loading).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return mCategoryItem.size();
    }
}
