package com.shuvo.ttit.trkabikha.projectUpdate;

import static com.shuvo.ttit.trkabikha.mainmenu.HomePage.projectUpdateLists;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.shuvo.ttit.trkabikha.R;
import com.shuvo.ttit.trkabikha.adapter.ProjectAdapter;
import com.shuvo.ttit.trkabikha.adapter.ProjectUpdateAdapter;
import com.shuvo.ttit.trkabikha.arraylist.ProjectUpdateLists;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

public class ProjectUpdate extends AppCompatActivity implements ProjectUpdateAdapter.ClickedItem {

    RecyclerView itemView;
    ProjectUpdateAdapter projectUpdateAdapter;
    RecyclerView.LayoutManager layoutManager;

    TextInputEditText searchItem;
    TextInputLayout searchItemLay;

    String searchingName = "";
    ArrayList<ProjectUpdateLists> filteredList;
    boolean isfiltered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_update);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        itemView = findViewById(R.id.project_update_details_report_view);

        searchItem = findViewById(R.id.search_project_name_by_internal);
        searchItemLay = findViewById(R.id.search_project_name_by_internal_lay);

        itemView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        itemView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(itemView.getContext(),DividerItemDecoration.VERTICAL);
        itemView.addItemDecoration(dividerItemDecoration);

        projectUpdateAdapter = new ProjectUpdateAdapter(projectUpdateLists, ProjectUpdate.this, ProjectUpdate.this);
        ScaleInAnimationAdapter animationAdapter = new ScaleInAnimationAdapter(projectUpdateAdapter);
        animationAdapter.setDuration(500);
        animationAdapter.setInterpolator(new AccelerateDecelerateInterpolator());
        animationAdapter.setFirstOnly(false);
        itemView.setAdapter(animationAdapter);

        searchItem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                searchingName = s.toString();
                filter(s.toString());
            }
        });

        searchItem.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT || event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER &&
                    event.getKeyCode() == KeyEvent.KEYCODE_NAVIGATE_NEXT) {
                if (event == null || !event.isShiftPressed()) {
                    // the user is done typing.
                    Log.i("Let see", "Come here");
                    closeKeyBoard();



                    return false; // consume.
                }
            }
            return false;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        projectUpdateAdapter.notifyDataSetChanged();

    }

    private void closeKeyBoard() {
        View view = getCurrentFocus();
        if (view != null) {
            view.clearFocus();
            InputMethodManager mgr = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void filter(String text) {

        filteredList = new ArrayList<>();
        for (ProjectUpdateLists item : projectUpdateLists) {
            if (item.getPcmInternalNo().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add((item));
                isfiltered = true;
            }
        }
        projectUpdateAdapter.filterList(filteredList);
    }

    @Override
    public void onCategoryClicked(int CategoryPosition) {

    }
}