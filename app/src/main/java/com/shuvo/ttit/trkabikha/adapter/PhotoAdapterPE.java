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
import com.shuvo.ttit.trkabikha.arraylist.PhotoListFromPE;
import com.shuvo.ttit.trkabikha.dialogue.PhotoDialoguePE;

import java.util.ArrayList;

public class PhotoAdapterPE extends RecyclerView.Adapter<PhotoAdapterPE.PHOTOPEHOLDER>{

    private final ArrayList<PhotoListFromPE> mCategoryItem;
    private final Context myContext;

    public static Bitmap bitmapFromPhotoAdapter = null;

    public PhotoAdapterPE(ArrayList<PhotoListFromPE> mCategoryItem, Context myContext) {
        this.mCategoryItem = mCategoryItem;
        this.myContext = myContext;
    }

    public class PHOTOPEHOLDER extends RecyclerView.ViewHolder {


        public ImageView imageView;
        public TextView uploadDate;
        public TextView stage;

        public PHOTOPEHOLDER(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_from);
            uploadDate = itemView.findViewById(R.id.upload_date_text);
            stage = itemView.findViewById(R.id.working_stage_text);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                    bitmapFromPhotoAdapter = mCategoryItem.get(getAdapterPosition()).getPhotoName();
                    PhotoDialoguePE photoDialoguePE = new PhotoDialoguePE();
                    photoDialoguePE.show(activity.getSupportFragmentManager(),"PICTURE_1");
                }
            });

        }
    }

    @NonNull
    @Override
    public PHOTOPEHOLDER onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.photo_list_view, parent, false);
        return new PHOTOPEHOLDER(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PHOTOPEHOLDER holder, int position) {
        PhotoListFromPE categoryItem = mCategoryItem.get(position);

        holder.uploadDate.setText(categoryItem.getUploadDate());
        holder.stage.setText(categoryItem.getStage());
        Glide.with(myContext)
                .load(categoryItem.getPhotoName())
                .error(R.drawable.loading_error)
                .placeholder(R.drawable.loading)
                .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return mCategoryItem.size();
    }
}
