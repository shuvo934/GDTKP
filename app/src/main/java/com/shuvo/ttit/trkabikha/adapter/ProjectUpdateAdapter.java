package com.shuvo.ttit.trkabikha.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.shuvo.ttit.trkabikha.R;
import com.shuvo.ttit.trkabikha.arraylist.LocationLists;
import com.shuvo.ttit.trkabikha.arraylist.ProjectUpdateLists;
import com.shuvo.ttit.trkabikha.projectUpdate.editProject.ProjectEdit;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ProjectUpdateAdapter extends RecyclerView.Adapter<ProjectUpdateAdapter.PUAHolder> {

    private ArrayList<ProjectUpdateLists> mCategoryItem;
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
    public String P_SUB_TYPE = "";
    public String F_NAME = "";
    public String SANC_CAT = "";
    public String PIC_DET = "";
    public String EVAL = "";
    public String PCM_ID = "";
    public String S_TYPE = "";
    public String S_TYPE_ID = "";
    public String PSC_SANCTION_CAT_ID = "";
    public String PCM_CATEGORY_ID = "";
    public Boolean IMAGE_DATA = false;
    public String DISTANCE_METER = "";
    public static ArrayList<LocationLists> locationListsAdapterPU;

    int selectedPosition = -1 ;

    public ProjectUpdateAdapter(ArrayList<ProjectUpdateLists> mCategoryItem, Context myContext, ClickedItem myClickedItem) {
        this.mCategoryItem = mCategoryItem;
        this.myContext = myContext;
        this.myClickedItem = myClickedItem;
    }

    @NonNull
    @Override
    public PUAHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.project_details_view, parent, false);
        return new PUAHolder(view, myClickedItem);
    }

    @Override
    public void onBindViewHolder(@NonNull PUAHolder holder, int position) {

        ProjectUpdateLists categoryItem = mCategoryItem.get(position);

        holder.projectCount.setText(categoryItem.getCount());
        holder.projectName.setText(categoryItem.getPcmProjectName());
        holder.projectNo.setText(categoryItem.getPcmProjectNo());
        holder.projectCode.setText(categoryItem.getPcmProjectCode());
//        String dateC = categoryItem.getPcmProjectDate().substring(0, 10);
//        System.out.println(dateC);
//
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
//        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy", Locale.getDefault());
//
//        String formattedDate = "";
//        Date date = null;
//
//        try {
//            date = df.parse(dateC);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        if (date != null) {
//            formattedDate = sdf.format(date);
//        }
        holder.projectDate.setText(categoryItem.getPcmProjectDate());
        String stype = categoryItem.getSanctionType();
        if (stype.contains("Taka")) {
            DecimalFormat formatter = new DecimalFormat("##,##,##,###");
            double val = 0.0;
            if (categoryItem.getPcmEstimateProjectValue() != null) {
                val = Double.parseDouble(categoryItem.getPcmEstimateProjectValue());
            }
            String formatted = formatter.format(val);
            String totalVal = formatted + " " + stype;
            holder.estimateValue.setText(totalVal);
        }
        else {
            String totalVal = categoryItem.getPcmEstimateProjectValue() + " " + stype;
            holder.estimateValue.setText(totalVal);
        }

        String type = categoryItem.getProjectTypeName()+" > " + categoryItem.getProjectSubTypeName();
        holder.projectType.setText(type);
        holder.fundName.setText(categoryItem.getFsmFundName());
        holder.finanYear.setText(categoryItem.getFyFinancialYearName());
        holder.internalNo.setText(categoryItem.getPcmInternalNo());

        if (categoryItem.isImageData()) {
            holder.imageDataImage.setBackgroundResource(R.drawable.check_circle_24);
        }
        else {
            holder.imageDataImage.setBackgroundResource(R.drawable.horizontal_rule_24);
        }

        if (categoryItem.isMapData()) {
            holder.mapDataImage.setBackgroundResource(R.drawable.check_circle_24);
        }
        else {
            holder.mapDataImage.setBackgroundResource(R.drawable.horizontal_rule_24);
        }

        if (selectedPosition == position) {
            holder.linearLayout.setBackgroundColor(Color.parseColor("#dfe6e9"));
        } else {
            holder.linearLayout.setBackgroundColor(Color.WHITE);
        }
    }

    @Override
    public int getItemCount() {
        return mCategoryItem.size();
    }

    public class PUAHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView projectCount;
        public TextView projectName;
        public TextView projectNo;
        public TextView projectCode;
        public TextView projectDate;
        public TextView estimateValue;
        public TextView projectType;
        public TextView fundName;
        public TextView finanYear;
        public TextView internalNo;
        public ImageView mapDataImage;
        public ImageView imageDataImage;

        LinearLayout linearLayout;
        ClickedItem mClickedItem;

        public PUAHolder(@NonNull View itemView, ClickedItem ci) {
            super(itemView);
            projectCount = itemView.findViewById(R.id.project_count);
            projectName = itemView.findViewById(R.id.project_name);
            projectNo = itemView.findViewById(R.id.project_no);
            projectCode = itemView.findViewById(R.id.project_code);
            projectDate = itemView.findViewById(R.id.project_date);
            estimateValue = itemView.findViewById(R.id.estimated_value);
            projectType = itemView.findViewById(R.id.project_type);
            fundName = itemView.findViewById(R.id.fund_name);
            finanYear = itemView.findViewById(R.id.financial_year);
            internalNo = itemView.findViewById(R.id.project_internal_no);
            linearLayout = itemView.findViewById(R.id.background_of_p_d);
            mapDataImage = itemView.findViewById(R.id.map_data_checked_pic);
            imageDataImage = itemView.findViewById(R.id.image_data_checked_pic);

            this.mClickedItem = ci;
            itemView.setOnClickListener(this);


            projectName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AppCompatActivity activity = (AppCompatActivity) view.getContext();

                    locationListsAdapterPU = new ArrayList<>();

                    INTERNAL_NO = mCategoryItem.get(getAdapterPosition()).getPcmInternalNo();
                    P_NO = mCategoryItem.get(getAdapterPosition()).getPcmProjectNo();
                    P_CODE = mCategoryItem.get(getAdapterPosition()).getPcmProjectCode();
                    P_NAME = mCategoryItem.get(getAdapterPosition()).getPcmProjectName();
                    P_DETAILS = mCategoryItem.get(getAdapterPosition()).getProjectDetails();
                    ENTRY_DATE = mCategoryItem.get(getAdapterPosition()).getPcmEntryDate();
                    START_DATE = mCategoryItem.get(getAdapterPosition()).getProjectStartDate();
                    END_DATE = mCategoryItem.get(getAdapterPosition()).getProjectEndDate();
                    SUBMITTER = mCategoryItem.get(getAdapterPosition()).getPcmUser();
//                    String dateC = mCategoryItem.get(getAdapterPosition()).getPcmProjectDate().substring(0, 10);
//                    System.out.println(dateC);
//
//                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
//                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy", Locale.getDefault());
//
//                    String formattedDate = "";
//                    Date date = null;
//
//                    try {
//                        date = df.parse(dateC);
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//                    if (date != null) {
//                        formattedDate = sdf.format(date);
//                    }
                    P_DATE = mCategoryItem.get(getAdapterPosition()).getPcmProjectDate();
                    F_YEAR = mCategoryItem.get(getAdapterPosition()).getFyFinancialYearName();
                    S_TYPE= mCategoryItem.get(getAdapterPosition()).getSanctionType();
                    S_TYPE_ID = mCategoryItem.get(getAdapterPosition()).getSanctionType_id();

                    //                    if (stype.contains("Taka")) {
//                        DecimalFormat formatter = new DecimalFormat("##,##,##,###");
//                        double val = 0.0;
//                        if (mCategoryItem.get(getAdapterPosition()).getPcmEstimateProjectValue() != null) {
//                            val = Double.parseDouble(mCategoryItem.get(getAdapterPosition()).getPcmEstimateProjectValue());
//                        }
//                        String formatted = formatter.format(val);
//                        totalVal = stype + " " + formatted;
//                    }
//                    else {
//                        totalVal = stype + " " + mCategoryItem.get(getAdapterPosition()).getPcmEstimateProjectValue();
//
//                    }
                    ES_VAL = mCategoryItem.get(getAdapterPosition()).getPcmEstimateProjectValue();
                    CATEGORY = mCategoryItem.get(getAdapterPosition()).getPcmCategoryName();
                    P_TYPE = mCategoryItem.get(getAdapterPosition()).getProjectTypeName();
                    P_SUB_TYPE = mCategoryItem.get(getAdapterPosition()).getProjectSubTypeName();
                    F_NAME = mCategoryItem.get(getAdapterPosition()).getFsmFundName();
                    SANC_CAT = mCategoryItem.get(getAdapterPosition()).getPscSanctionCatName();
                    PIC_DET = mCategoryItem.get(getAdapterPosition()).getPcmPicChairmanDetails();
                    EVAL = mCategoryItem.get(getAdapterPosition()).getProjEvaluationRem();
                    PCM_ID = mCategoryItem.get(getAdapterPosition()).getPcmId();
                    locationListsAdapterPU = mCategoryItem.get(getAdapterPosition()).getLocationLists();
                    PSC_SANCTION_CAT_ID = mCategoryItem.get(getAdapterPosition()).getPscSanctionCatId();
                    PCM_CATEGORY_ID = mCategoryItem.get(getAdapterPosition()).getPcmCategoryId();
                    IMAGE_DATA = mCategoryItem.get(getAdapterPosition()).isImageData();
                    DISTANCE_METER = mCategoryItem.get(getAdapterPosition()).getDistanceMeter();

                    Intent intent = new Intent(myContext, ProjectEdit.class);
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
                    intent.putExtra("P_SUB_TYPE",P_SUB_TYPE);
                    intent.putExtra("F_NAME",F_NAME);
                    intent.putExtra("SANC_CAT",SANC_CAT);
                    intent.putExtra("PIC_DET",PIC_DET);
                    intent.putExtra("EVAL",EVAL);
                    intent.putExtra("PCM_ID",PCM_ID);
                    intent.putExtra("S_TYPE",S_TYPE);
                    intent.putExtra("S_TYPE_ID",S_TYPE_ID);
                    intent.putExtra("PSC_SANCTION_CAT_ID",PSC_SANCTION_CAT_ID);
                    intent.putExtra("PCM_CATEGORY_ID",PCM_CATEGORY_ID);
                    intent.putExtra("IMAGE_DATA",IMAGE_DATA);
                    intent.putExtra("DISTANCE_METER",DISTANCE_METER);
                    intent.putExtra("FROM_MAP",false);
                    activity.startActivity(intent);

                    selectedPosition = getAdapterPosition();
                    notifyDataSetChanged();
//                    detailsDialogue.show(activity.getSupportFragmentManager(),"DD");
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

    public void filterList(ArrayList<ProjectUpdateLists> filteredList) {
        mCategoryItem = filteredList;
        notifyDataSetChanged();
    }
}
