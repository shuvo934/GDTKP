package com.shuvo.ttit.trkabikha.adapter;

import static com.shuvo.ttit.trkabikha.projectCreation.CreateProject.selectedVillageAdapter;
import static com.shuvo.ttit.trkabikha.projectCreation.CreateProject.selectedVillageLists;
import static com.shuvo.ttit.trkabikha.projectCreation.CreateProject.villageLayout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shuvo.ttit.trkabikha.R;
import com.shuvo.ttit.trkabikha.arraylist.SelectedWardList;

import java.util.ArrayList;

public class SelectedWardAdapter extends RecyclerView.Adapter<SelectedWardAdapter.SWHolder> {
    private final ArrayList<SelectedWardList> mCategoryItem;
    private final Context myContext;

    public SelectedWardAdapter(ArrayList<SelectedWardList> mCategoryItem, Context myContext) {
        this.mCategoryItem = mCategoryItem;
        this.myContext = myContext;
    }

    @NonNull
    @Override
    public SWHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.ward_village_name_selected_layout, parent, false);
        return new SWHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SWHolder holder, int position) {

        SelectedWardList selectedWardList = mCategoryItem.get(position);
        holder.wardName.setText(selectedWardList.getWard_name());
    }

    @Override
    public int getItemCount() {
        return mCategoryItem.size();
    }

    public class SWHolder extends RecyclerView.ViewHolder {
        public ImageView deleteWard;
        public TextView wardName;


        public SWHolder(@NonNull View itemView) {
            super(itemView);
            wardName = itemView.findViewById(R.id.ward_village_name_p_crtn);
            deleteWard = itemView.findViewById(R.id.delete_ward_villa_name_p_crtn);

            deleteWard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String ward_id = mCategoryItem.get(getAdapterPosition()).getDdw_id();
                    boolean found = true;
                    int index = 0;
                    while (found) {
                        if (selectedVillageLists.size() != 0) {
                            for (int i = 0; i < selectedVillageLists.size(); i++) {
                                if (ward_id.equals(selectedVillageLists.get(i).getDdv_ddw_id())) {
                                    index = i;
                                    found = true;
                                    break;
                                }
                                else {
                                    found = false;
                                }
                            }
                            if (found) {
                                selectedVillageLists.remove(index);
                            }
                        }
                        else {
                            found = false;
                        }
                    }

                    mCategoryItem.remove(getAdapterPosition());
                    if (mCategoryItem.size() == 0) {
                        villageLayout.setVisibility(View.GONE);
                    }
                    selectedVillageAdapter.notifyDataSetChanged();
                    notifyDataSetChanged();
                }
            });
        }
    }
}
