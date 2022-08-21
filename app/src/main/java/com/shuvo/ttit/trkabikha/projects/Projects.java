package com.shuvo.ttit.trkabikha.projects;

import android.os.Bundle;
import android.view.animation.AccelerateDecelerateInterpolator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shuvo.ttit.trkabikha.R;
import com.shuvo.ttit.trkabikha.adapter.ProjectAdapter;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

import static com.shuvo.ttit.trkabikha.mainmenu.HomePage.projectlists;

public class Projects extends AppCompatActivity implements ProjectAdapter.ClickedItem{

    RecyclerView itemView;
    ProjectAdapter projectAdapter;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects);

        itemView = findViewById(R.id.project_details_report_view);

        itemView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        itemView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(itemView.getContext(),DividerItemDecoration.VERTICAL);
        itemView.addItemDecoration(dividerItemDecoration);
        //AppCompatActivity activity1 = (AppCompatActivity) Projects.this;



        projectAdapter = new ProjectAdapter(projectlists, Projects.this, Projects.this);
        ScaleInAnimationAdapter animationAdapter = new ScaleInAnimationAdapter(projectAdapter);
        animationAdapter.setDuration(500);
        animationAdapter.setInterpolator(new AccelerateDecelerateInterpolator());
        animationAdapter.setFirstOnly(false);
        itemView.setAdapter(animationAdapter);

    }

    @Override
    public void onCategoryClicked(int CategoryPosition) {

    }
}