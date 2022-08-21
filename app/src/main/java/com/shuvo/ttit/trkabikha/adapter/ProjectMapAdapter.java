package com.shuvo.ttit.trkabikha.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.shuvo.ttit.trkabikha.R;
import com.shuvo.ttit.trkabikha.arraylist.LocationLists;
import com.shuvo.ttit.trkabikha.arraylist.ProjectMapsLists;
import com.shuvo.ttit.trkabikha.projectDetails.ProjectDetails;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static com.shuvo.ttit.trkabikha.projectsWithMap.ProjectsMaps.selectedAdapterPosition;


public class ProjectMapAdapter extends RecyclerView.Adapter<ProjectMapAdapter.PMHolder> {

    private final ArrayList<ProjectMapsLists> mCategoryItem;
    private final Context myContext;
    private final ClickedItem myClickedItem;

    public String INTERNAL_NO = "";
    public String P_NO = "";
    public String P_CODE = "";
    public String P_NAME = "";
    public String P_DETAILS = "";
    public String ENTRY_DATE = "";
    public String START_DATE = "";
    public String END_DATE = "";
    public String SUBMITTER = "";
    public String P_DATE = "";
    public String F_YEAR = "";
    public String ES_VAL = "";
    public String CATEGORY = "";
    public String P_TYPE = "";
    public String F_NAME = "";
    public String SANC_CAT = "";
    public String PIC_DET = "";
    public String EVAL = "";
    public String PCM_ID = "";

    public static ArrayList<LocationLists> locationListsMapAdapter;

    public ProjectMapAdapter(ArrayList<ProjectMapsLists> categoryItems, Context context, ClickedItem myClickedItem) {
        this.mCategoryItem = categoryItems;
        this.myContext = context;
        this.myClickedItem = myClickedItem;
    }

    public class PMHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        public TextView projectName;
        public TextView internalNo;
        public TextView pCount;
        Button details;
        public LinearLayout linearLayout;

        ClickedItem mClickedItem;

        public PMHolder(@NonNull View itemView, ClickedItem ci) {
            super(itemView);
            projectName = itemView.findViewById(R.id.project_name_map);

            internalNo = itemView.findViewById(R.id.internal_no_map);

            details = itemView.findViewById(R.id.go_to_details_button);

            pCount = itemView.findViewById(R.id.project_count_map);

            linearLayout = itemView.findViewById(R.id.background_of_p_d_m);

            this.mClickedItem = ci;
            itemView.setOnClickListener(this);

            details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AppCompatActivity activity = (AppCompatActivity) view.getContext();


                    locationListsMapAdapter = new ArrayList<>();

                    INTERNAL_NO = mCategoryItem.get(getAdapterPosition()).getPcmInternalNo();
                    P_NO = mCategoryItem.get(getAdapterPosition()).getPcmProjectNo();
                    P_CODE = mCategoryItem.get(getAdapterPosition()).getPcmProjectCode();
                    P_NAME = mCategoryItem.get(getAdapterPosition()).getPcmProjectName();
                    P_DETAILS = mCategoryItem.get(getAdapterPosition()).getProjectDetails();
                    ENTRY_DATE = mCategoryItem.get(getAdapterPosition()).getPcmEntryDate();
                    START_DATE = mCategoryItem.get(getAdapterPosition()).getProjectStartDate();
                    END_DATE = mCategoryItem.get(getAdapterPosition()).getProjectEndDate();
                    SUBMITTER = mCategoryItem.get(getAdapterPosition()).getPcmUser();
                    String dateC = mCategoryItem.get(getAdapterPosition()).getPcmProjectDate().substring(0, 10);
                    System.out.println(dateC);

                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy", Locale.getDefault());

                    String formattedDate = "";
                    Date date = null;

                    try {
                        date = df.parse(dateC);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (date != null) {
                        formattedDate = sdf.format(date);
                    }
                    P_DATE = formattedDate;
                    F_YEAR = mCategoryItem.get(getAdapterPosition()).getFyFinancialYearName();
                    String stype = mCategoryItem.get(getAdapterPosition()).getSanctionType();
                    String totalVal = "";
                    if (stype.contains("Taka")) {
                        DecimalFormat formatter = new DecimalFormat("##,##,##,###");
                        double val = 0.0;
                        if (mCategoryItem.get(getAdapterPosition()).getPcmEstimateProjectValue() != null) {
                            val = Double.parseDouble(mCategoryItem.get(getAdapterPosition()).getPcmEstimateProjectValue());
                        }
                        String formatted = formatter.format(val);
                        totalVal = stype + " " + formatted;
                    }
                    else {
                        totalVal = stype + " " + mCategoryItem.get(getAdapterPosition()).getPcmEstimateProjectValue();

                    }
                    ES_VAL = totalVal;
                    CATEGORY = mCategoryItem.get(getAdapterPosition()).getPcmCategoryName();
                    P_TYPE = mCategoryItem.get(getAdapterPosition()).getProjectTypeName() +" > " + mCategoryItem.get(getAdapterPosition()).getProjectSubTypeName();
                    F_NAME = mCategoryItem.get(getAdapterPosition()).getFsmFundName();
                    SANC_CAT = mCategoryItem.get(getAdapterPosition()).getPscSanctionCatName();
                    PIC_DET = mCategoryItem.get(getAdapterPosition()).getPcmPicChairmanName() + mCategoryItem.get(getAdapterPosition()).getPcmPicChairmanDetails();
                    EVAL = mCategoryItem.get(getAdapterPosition()).getProjEvaluationRem();
                    PCM_ID = mCategoryItem.get(getAdapterPosition()).getPcmId();
                    locationListsMapAdapter = mCategoryItem.get(getAdapterPosition()).getLocationLists();

                    Intent intent = new Intent(myContext, ProjectDetails.class);
                    intent.putExtra("INTERNAL_NO", INTERNAL_NO);
                    intent.putExtra("P_NO",P_NO);
                    intent.putExtra("P_CODE",P_CODE);
                    intent.putExtra("P_NAME",P_NAME);
                    intent.putExtra("P_DETAILS",P_DETAILS);
                    intent.putExtra("ENTRY_DATE",ENTRY_DATE);
                    intent.putExtra("START_DATE",START_DATE);
                    intent.putExtra("END_DATE",END_DATE);
                    intent.putExtra("SUBMITTER",SUBMITTER);
                    intent.putExtra("P_DATE",P_DATE);
                    intent.putExtra("F_YEAR",F_YEAR);
                    intent.putExtra("ES_VAL",ES_VAL);
                    intent.putExtra("CATEGORY",CATEGORY);
                    intent.putExtra("P_TYPE",P_TYPE);
                    intent.putExtra("F_NAME",F_NAME);
                    intent.putExtra("SANC_CAT",SANC_CAT);
                    intent.putExtra("PIC_DET",PIC_DET);
                    intent.putExtra("EVAL",EVAL);
                    intent.putExtra("PCM_ID",PCM_ID);
                    intent.putExtra("FROM_MAP",true);

                    activity.startActivity(intent);
                }
            });

        }

        @Override
        public void onClick(View v) {
            mClickedItem.onCategoryClicked(getAdapterPosition());
            //Log.i("Client Name", mCategoryItem.get(getAdapterPosition()).getvDate());
        }

    }
    public interface ClickedItem {
        void onCategoryClicked(int CategoryPosition);
    }

    @NonNull
    @Override
    public PMHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.project_details_map_view, parent, false);
        return new PMHolder(view, myClickedItem);
    }

    @Override
    public void onBindViewHolder(@NonNull PMHolder holder, int position) {

        ProjectMapsLists categoryItem = mCategoryItem.get(position);

        holder.projectName.setText(categoryItem.getPcmProjectName());
        holder.internalNo.setText(categoryItem.getPcmInternalNo());
        holder.pCount.setText(categoryItem.getCount());

        if(selectedAdapterPosition == position) {
            holder.linearLayout.setBackgroundColor(Color.parseColor("#dfe6e9"));
            holder.details.setVisibility(View.VISIBLE);
        } else {
            holder.linearLayout.setBackgroundColor(Color.WHITE);
            holder.details.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return mCategoryItem.size();
    }
}
