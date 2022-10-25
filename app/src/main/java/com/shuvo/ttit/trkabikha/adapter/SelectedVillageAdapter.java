package com.shuvo.ttit.trkabikha.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shuvo.ttit.trkabikha.R;
import com.shuvo.ttit.trkabikha.arraylist.SelectedVillageList;

import java.util.ArrayList;

public class SelectedVillageAdapter extends RecyclerView.Adapter<SelectedVillageAdapter.SVHolder> {

    private final ArrayList<SelectedVillageList> mCategoryItem;
    private final Context myContext;

    public SelectedVillageAdapter(ArrayList<SelectedVillageList> mCategoryItem, Context myContext) {
        this.mCategoryItem = mCategoryItem;
        this.myContext = myContext;
    }

    @NonNull
    @Override
    public SVHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.ward_village_name_selected_layout, parent, false);
        return new SVHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SVHolder holder, int position) {
        SelectedVillageList selectedVillageList = mCategoryItem.get(position);
        holder.villageName.setText(selectedVillageList.getVillage_name());
    }

    @Override
    public int getItemCount() {
        return mCategoryItem.size();
    }

    public class SVHolder extends RecyclerView.ViewHolder {
        public ImageView deleteVillage;
        public TextView villageName;


        public SVHolder(@NonNull View itemView) {
            super(itemView);
            villageName = itemView.findViewById(R.id.ward_village_name_p_crtn);
            deleteVillage = itemView.findViewById(R.id.delete_ward_villa_name_p_crtn);

            deleteVillage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mCategoryItem.remove(getAdapterPosition());
                    notifyDataSetChanged();
                }
            });
        }
    }
}
