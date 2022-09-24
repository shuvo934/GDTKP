package com.shuvo.ttit.trkabikha.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.shuvo.ttit.trkabikha.R;
import com.shuvo.ttit.trkabikha.arraylist.ImageCapturedList;

import java.util.ArrayList;

public class ImageCapturedAdapter extends RecyclerView.Adapter<ImageCapturedAdapter.ICAHolder> {

    private final Context context;
    private ArrayList<ImageCapturedList> categoryLists;
    private final ClickedItem myClickedItem;

    public ImageCapturedAdapter(Context context, ArrayList<ImageCapturedList> categoryLists, ClickedItem myClickedItem) {
        this.context = context;
        this.categoryLists = categoryLists;
        this.myClickedItem = myClickedItem;
    }

    @NonNull
    @Override
    public ICAHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.image_captured_view, parent, false);
        return new ICAHolder(view, myClickedItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ICAHolder holder, int position) {
        ImageCapturedList imageCapturedList = categoryLists.get(position);

        Glide.with(context)
                .load(imageCapturedList.getBitmap())
                .fitCenter()
                .into(holder.imageCapture);
        //holder.imageCapture.setBackgroundResource(R.drawable.image_transparent);
    }

    @Override
    public int getItemCount() {
        return categoryLists.size();
    }

    public class ICAHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        public ImageView imageCapture;
        public ImageView deleteImage;

        ClickedItem mClickedItem;

        public ICAHolder(@NonNull View itemView, ClickedItem ci) {
            super(itemView);
            imageCapture = itemView.findViewById(R.id.image_captured_from_project_edit);
            deleteImage = itemView.findViewById(R.id.delete_captured_image);

            this.mClickedItem = ci;
            itemView.setOnClickListener(this);

            deleteImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    categoryLists.remove(getAdapterPosition());
                    notifyDataSetChanged();
                }
            });

        }

        @Override
        public void onClick(View v) {
            mClickedItem.onCategoryClicked(getAdapterPosition());
        }

    }
    public interface ClickedItem {
        void onCategoryClicked(int CategoryPosition);
    }
}
