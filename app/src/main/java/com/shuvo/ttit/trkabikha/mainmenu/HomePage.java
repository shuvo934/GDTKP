package com.shuvo.ttit.trkabikha.mainmenu;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;
import com.rosemaryapp.amazingspinner.AmazingSpinner;
import com.shuvo.ttit.trkabikha.R;
import com.shuvo.ttit.trkabikha.arraylist.DistrictLists;
import com.shuvo.ttit.trkabikha.arraylist.DivisionLists;
import com.shuvo.ttit.trkabikha.arraylist.FinancialYearLists;
import com.shuvo.ttit.trkabikha.arraylist.LocationLists;
import com.shuvo.ttit.trkabikha.arraylist.ProjectMapsLists;
import com.shuvo.ttit.trkabikha.arraylist.ProjectSubTypeLists;
import com.shuvo.ttit.trkabikha.arraylist.ProjectTypeLists;
import com.shuvo.ttit.trkabikha.arraylist.ProjectUpdateLists;
import com.shuvo.ttit.trkabikha.arraylist.Projectlists;
import com.shuvo.ttit.trkabikha.arraylist.SourceFundLists;
import com.shuvo.ttit.trkabikha.arraylist.UnionLists;
import com.shuvo.ttit.trkabikha.arraylist.UpazilaLists;
import com.shuvo.ttit.trkabikha.progressbar.WaitProgress;
import com.shuvo.ttit.trkabikha.projectCreation.CreateProject;
import com.shuvo.ttit.trkabikha.projectUpdate.ProjectUpdate;
import com.shuvo.ttit.trkabikha.projects.Projects;
import com.shuvo.ttit.trkabikha.projectsWithMap.ProjectsMaps;


import java.io.IOException;
import java.util.ArrayList;

import static com.shuvo.ttit.trkabikha.login.Login.userInfoLists;
import static com.shuvo.ttit.trkabikha.login.PICLogin.picUserDetails;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_8;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HomePage extends AppCompatActivity {

    AmazingSpinner financialYearStart;
    AmazingSpinner financialYearEnd;
    LinearLayout afterYearSelection;
    AmazingSpinner division;
    TextInputLayout divisionLay;

    AmazingSpinner district;
    TextInputLayout districtLay;

    AmazingSpinner upazila;
    TextInputLayout upazilaLay;

    AmazingSpinner union;
    TextInputLayout unionLay;

    AmazingSpinner sourceOfFund;

    AmazingSpinner projectType;

    AmazingSpinner projectSubType;
    TextInputLayout projectSubTypeLay;

    Button search;
    Button searchMap;
    Button searchUpdateProject;
    Button createProject;

    WaitProgress waitProgress = new WaitProgress();
    private Boolean conn = false;

    ArrayList<FinancialYearLists> fysLists;
    ArrayList<FinancialYearLists> fyeLists;

    String fys_id = "";
    String fye_id = "";
    String fys_name = "";
    String fye_name = "";

    ArrayList<DivisionLists> divisionLists;
    String div_id = "";
    String div_name = "";

    ArrayList<DistrictLists> districtLists;
    String dist_id = "";
    String dist_name = "";

    ArrayList<UpazilaLists> upazilaLists;
    String dd_id = "";
    String thana_name = "";

    ArrayList<UnionLists> unionLists;
    String ddu_id = "";
    String union_name = "";

    ArrayList<SourceFundLists> sourceFundLists;
    String fsm_id = "";
    String fund_name = "";

    ArrayList<ProjectTypeLists> projectTypeLists;
    String ptm_id = "";
    String project_type_name = "";

    ArrayList<ProjectSubTypeLists> projectSubTypeLists;
    String ptd_Id = "";
    String project_sub_type_name = "";

    public static ArrayList<Projectlists> projectlists;
    public static ArrayList<ProjectMapsLists> projectMapsLists;
    public static ArrayList<ProjectUpdateLists> projectUpdateLists;

    TextView  userName;

    String userType = "";
    String pcmUser = "";

    ImageView logOut;
    TextView citizenPortalLink;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        userName = findViewById(R.id.user_full_name);

        Intent intent = getIntent();
        userType = intent.getStringExtra("USER");

        financialYearStart = findViewById(R.id.fy_s_spinner);
        financialYearEnd = findViewById(R.id.fy_e_spinner);
        afterYearSelection = findViewById(R.id.after_selecting_year);
        afterYearSelection.setVisibility(View.GONE);

        division = findViewById(R.id.division_spinner);
        divisionLay = findViewById(R.id.spinner_layout_division);

        district = findViewById(R.id.district_spinner);
        districtLay = findViewById(R.id.spinner_layout_district);
        districtLay.setEnabled(false);

        upazila = findViewById(R.id.upazila_spinner);
        upazilaLay = findViewById(R.id.spinner_layout_upazila);
        upazilaLay.setEnabled(false);

        union = findViewById(R.id.union_spinner);
        unionLay = findViewById(R.id.spinner_layout_union);
        unionLay.setEnabled(false);

        sourceOfFund = findViewById(R.id.source_fund_spinner);

        projectType = findViewById(R.id.project_type_spinner);

        projectSubType = findViewById(R.id.project_sub_type_spinner);
        projectSubTypeLay = findViewById(R.id.spinner_layout_project_sub_type);
        projectSubTypeLay.setEnabled(false);

        search = findViewById(R.id.search_project_button);
        search.setEnabled(false);
        searchMap = findViewById(R.id.search_map_button);
        searchMap.setEnabled(false);
        searchUpdateProject = findViewById(R.id.search_to_update_project_button);
        searchUpdateProject.setEnabled(false);
        createProject = findViewById(R.id.create_project_button);
        createProject.setEnabled(false);

        logOut = findViewById(R.id.log_out_icon_main_menu);
        citizenPortalLink = findViewById(R.id.citizen_portal_web_link);

        fysLists = new ArrayList<>();
        fyeLists = new ArrayList<>();
        divisionLists = new ArrayList<>();
        districtLists = new ArrayList<>();
        upazilaLists = new ArrayList<>();
        sourceFundLists = new ArrayList<>();
        projectTypeLists = new ArrayList<>();
        projectSubTypeLists = new ArrayList<>();
        unionLists = new ArrayList<>();
        projectlists = new ArrayList<>();
        projectMapsLists = new ArrayList<>();
        projectUpdateLists = new ArrayList<>();

        switch (userType) {
            case "GUEST":
                userName.setText(userType);
                searchMap.setVisibility(View.GONE);
                searchUpdateProject.setVisibility(View.GONE);
                logOut.setVisibility(View.GONE);
                createProject.setVisibility(View.GONE);
                break;
            case "ADMIN": {
                String n = userInfoLists.get(0).getUserName();
                userName.setText(n);
                searchMap.setVisibility(View.VISIBLE);
                searchUpdateProject.setVisibility(View.GONE);
                logOut.setVisibility(View.VISIBLE);
                createProject.setVisibility(View.GONE);
                break;
            }
            case "PIC_USER": {
                String n = picUserDetails.get(0).getUser_fname() + " " + picUserDetails.get(0).getUser_lname();
                userName.setText(n);
                searchMap.setVisibility(View.VISIBLE);
                searchUpdateProject.setVisibility(View.VISIBLE);
                logOut.setVisibility(View.VISIBLE);
                createProject.setVisibility(View.VISIBLE);
                if (picUserDetails.get(0).getDd_id() != null) {
                    createProject.setEnabled(!picUserDetails.get(0).getDd_id().isEmpty());
                }
                else {
                    createProject.setEnabled(false);
                }
                div_id = picUserDetails.get(0).getDiv_id();
                dist_id = picUserDetails.get(0).getDist_id();
                dd_id = picUserDetails.get(0).getDd_id();
                pcmUser = picUserDetails.get(0).getUserName();
                if (div_id != null) {
                    if (!div_id.isEmpty()) {
                        division.setText(picUserDetails.get(0).getDiv_name());
                        divisionLay.setEnabled(false);
                        districtLay.setEnabled(true);
                        if (dist_id != null) {
                            if (!dist_id.isEmpty()) {
                                district.setText(picUserDetails.get(0).getDist_name());
                                districtLay.setEnabled(false);
                                upazilaLay.setEnabled(true);
                                if (dd_id != null) {
                                    if (!dd_id.isEmpty()) {
                                        upazila.setText(picUserDetails.get(0).getDd_name());
                                        upazilaLay.setEnabled(false);
                                        unionLay.setEnabled(true);
                                    }
                                }
                            }
                        }
                    }
                }
                break;
            }
        }



        financialYearStart.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String name = adapterView.getItemAtPosition(i).toString();
                for (int j = 0; j < fysLists.size(); j++) {
                    if (name.equals(fysLists.get(j).getFinancialYearName())) {
                        fys_id = (fysLists.get(j).getFyId());
                    }
                }
                System.out.println(fys_id);
                if (!fye_id.isEmpty()) {
                    afterYearSelection.setVisibility(View.VISIBLE);
                    if (!div_id.isEmpty() && !fys_id.isEmpty() && !fye_id.isEmpty() && !dist_id.isEmpty()) {
                        search.setEnabled(true);
                        searchMap.setEnabled(true);
                        searchUpdateProject.setEnabled(true);
                    } else {
                        searchMap.setEnabled(false);
                        search.setEnabled(false);
                        searchUpdateProject.setEnabled(false);
                    }
//                    search.setEnabled(true);
//                    searchMap.setEnabled(true);
                }
            }
        });

        financialYearEnd.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String name = adapterView.getItemAtPosition(i).toString();
                for (int j = 0; j < fyeLists.size(); j++) {
                    if (name.equals(fyeLists.get(j).getFinancialYearName())) {
                        fye_id = (fyeLists.get(j).getFyId());
                    }
                }
                System.out.println(fye_id);
                if (!fys_id.isEmpty()) {
                    afterYearSelection.setVisibility(View.VISIBLE);
                    if (!div_id.isEmpty() && !fys_id.isEmpty() && !fye_id.isEmpty() && !dist_id.isEmpty()) {
                        search.setEnabled(true);
                        searchMap.setEnabled(true);
                        searchUpdateProject.setEnabled(true);
                    } else {
                        searchMap.setEnabled(false);
                        search.setEnabled(false);
                        searchUpdateProject.setEnabled(false);
                    }
//                    search.setEnabled(true);
//                    searchMap.setEnabled(true);
                }
            }
        });

        division.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                districtLay.setEnabled(false);
                district.setText("");
                upazilaLay.setEnabled(false);
                upazila.setText("");
                unionLay.setEnabled(false);
                union.setText("");
                div_id = "";
                dist_id = "";
                dd_id = "";
                ddu_id = "";
                String name = adapterView.getItemAtPosition(i).toString();
                for (int j = 0; j < divisionLists.size(); j++) {
                    if (name.equals(divisionLists.get(j).getDivName())) {
                        div_id = (divisionLists.get(j).getDivId());
                    }
                }
                if (name.equals("...")) {
                    division.setText("");
                }
                System.out.println(name);
                System.out.println(div_id);

                if (!div_id.isEmpty()) {
//                    new DistrictCheck().execute();
                    getDistricts();
                }

                if (!div_id.isEmpty() && !fys_id.isEmpty() && !fye_id.isEmpty() && !dist_id.isEmpty()) {
                    search.setEnabled(true);
                    searchMap.setEnabled(true);
                    searchUpdateProject.setEnabled(true);
                } else {
                    searchMap.setEnabled(false);
                    search.setEnabled(false);
                    searchUpdateProject.setEnabled(false);
                }

            }
        });

        district.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                upazilaLay.setEnabled(false);
                upazila.setText("");
                unionLay.setEnabled(false);
                union.setText("");
                dist_id = "";
                dd_id = "";
                ddu_id = "";


                String name = adapterView.getItemAtPosition(i).toString();
                for (int j = 0; j < districtLists.size(); j++) {
                    if (name.equals(districtLists.get(j).getDistName())) {
                        dist_id = (districtLists.get(j).getDistId());
                    }
                }

                System.out.println(dist_id);

                if (!div_id.isEmpty() && !fys_id.isEmpty() && !fye_id.isEmpty() && !dist_id.isEmpty()) {
                    search.setEnabled(true);
                    searchMap.setEnabled(true);
                    searchUpdateProject.setEnabled(true);
                } else {
                    searchMap.setEnabled(false);
                    search.setEnabled(false);
                    searchUpdateProject.setEnabled(false);
                }

//                new UpazilaCheck().execute();
                getUpazilas();
            }
        });

        upazila.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                unionLay.setEnabled(false);
                union.setText("");
                dd_id = "";
                ddu_id = "";

                String name = adapterView.getItemAtPosition(i).toString();
                for (int j = 0; j < upazilaLists.size(); j++) {
                    if (name.equals(upazilaLists.get(j).getThanaName())) {
                        dd_id = (upazilaLists.get(j).getDdId());
                    }
                }

                System.out.println(dd_id);

//                new UnionCheck().execute();
                getUnions();

            }
        });

        union.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ddu_id = "";

                String name = adapterView.getItemAtPosition(i).toString();
                for (int j = 0; j < unionLists.size(); j++) {
                    if (name.equals(unionLists.get(j).getUnionName())) {
                        ddu_id = (unionLists.get(j).getDduId());
                    }
                }

                System.out.println(ddu_id);
            }
        });

        sourceOfFund.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                fsm_id = "";

                String name = adapterView.getItemAtPosition(i).toString();
                for (int j = 0; j < sourceFundLists.size(); j++) {
                    if (name.equals(sourceFundLists.get(j).getFundName())) {
                        fsm_id = (sourceFundLists.get(j).getFsmId());
                    }
                }

                if (name.equals("...")) {
                    sourceOfFund.setText("");
                }

                System.out.println(fsm_id);
            }
        });

        projectType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                projectSubTypeLay.setEnabled(false);
                projectSubType.setText("");
                ptm_id = "";
                ptd_Id = "";
                String name = adapterView.getItemAtPosition(i).toString();
                for (int j = 0; j < projectTypeLists.size(); j++) {
                    if (name.equals(projectTypeLists.get(j).getProjectTypeName())) {
                        ptm_id = (projectTypeLists.get(j).getPtmId());
                    }
                }
                System.out.println(name);
                System.out.println(ptm_id);
                if (name.equals("...")) {
                    projectType.setText("");
                }

                if (!ptm_id.isEmpty()) {
//                    new ProjectSubTypeCheck().execute();
                    getProjectSubType();
                }


            }
        });

        projectSubType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ptd_Id = "";
                String name = adapterView.getItemAtPosition(i).toString();
                for (int j = 0; j < projectSubTypeLists.size(); j++) {
                    if (name.equals(projectSubTypeLists.get(j).getProjectSubTypeName())) {
                        ptd_Id = (projectSubTypeLists.get(j).getPtdId());
                    }
                }
                System.out.println(ptd_Id);
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!fys_id.isEmpty() && !fye_id.isEmpty()) {

//                    new ProjectDataCheck().execute();
                    getProjectData();
                }
            }
        });

        searchMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!fys_id.isEmpty() && !fye_id.isEmpty()) {
//                    new ProjectMapDataCheck().execute();
                    getProjectMapData();
                }
            }
        });

        searchUpdateProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!fys_id.isEmpty() && !fye_id.isEmpty()) {

//                    new ProjectUpdateDataCheck().execute();
                    getProjectUpdateData();
                }
            }
        });

        createProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(HomePage.this, CreateProject.class);
                intent1.putExtra("UPAZILLA",picUserDetails.get(0).getDd_id());
                intent1.putExtra("UPAZILLA_NAME", picUserDetails.get(0).getDd_name());
                startActivity(intent1);
            }
        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (userType) {
                    case "GUEST":
                        finish();
                        break;
                    case "ADMIN": {
                        AlertDialog.Builder builder = new AlertDialog.Builder(HomePage.this);
                        builder.setTitle("LOG OUT!")
                                .setMessage("Do you want to Log Out?")
                                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {


                                        userInfoLists.clear();
                                        userInfoLists = new ArrayList<>();


//                        Intent intent = new Intent(HomePage.this, Login.class);
//                        startActivity(intent);
                                        finish();
                                        //System.exit(0);
                                    }
                                })
                                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                        break;
                    }
                    case "PIC_USER": {
                        AlertDialog.Builder builder = new AlertDialog.Builder(HomePage.this);
                        builder.setTitle("LOG OUT!")
                                .setMessage("Do you want to Log Out?")
                                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {


                                        picUserDetails.clear();
                                        picUserDetails = new ArrayList<>();

                                        finish();

                                    }
                                })
                                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                        break;
                    }
                }
            }
        });

        citizenPortalLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String site_link = "http://tr-kabikha.techterrain-it.com:8869/";
                Uri uri = Uri.parse(site_link); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

//        new Check().execute();
        getQuery();

    }

    @Override
    public void onBackPressed() {
        switch (userType) {
            case "GUEST":
                super.onBackPressed();
                break;
            case "ADMIN": {
                AlertDialog.Builder builder = new AlertDialog.Builder(HomePage.this);
                builder.setTitle("LOG OUT!")
                        .setMessage("Do you want to Log Out?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                userInfoLists.clear();
                                userInfoLists = new ArrayList<>();


//                        Intent intent = new Intent(HomePage.this, Login.class);
//                        startActivity(intent);
                                finish();
                                //System.exit(0);
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
                break;
            }
            case "PIC_USER": {
                AlertDialog.Builder builder = new AlertDialog.Builder(HomePage.this);
                builder.setTitle("LOG OUT!")
                        .setMessage("Do you want to Log Out?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                picUserDetails.clear();
                                picUserDetails = new ArrayList<>();

                                finish();

                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
                break;
            }
        }
    }

    //    --------------------------Updating UI with Necessary Data-----------------------------
    public void getQuery() {
        waitProgress.show(getSupportFragmentManager(), "WaitBar");
        waitProgress.setCancelable(false);

        fysLists = new ArrayList<>();
        fyeLists = new ArrayList<>();
        divisionLists = new ArrayList<>();
        sourceFundLists = new ArrayList<>();
        projectTypeLists = new ArrayList<>();
        districtLists = new ArrayList<>();
        upazilaLists = new ArrayList<>();
        unionLists = new ArrayList<>();

        conn = false;

        String fy_url = "http://103.56.208.123:8086/terrain/tr_kabikha/utility_data/fy_lists";
        String div_url = "http://103.56.208.123:8086/terrain/tr_kabikha/utility_data/division_lists";
        String fund_url = "http://103.56.208.123:8086/terrain/tr_kabikha/utility_data/source_of_fund_lists";
        String p_type_url = "http://103.56.208.123:8086/terrain/tr_kabikha/utility_data/project_type_lists";
        String dist_url = "http://103.56.208.123:8086/terrain/tr_kabikha/utility_data/dist_lists?div_id="+div_id;
        String upa_url = "http://103.56.208.123:8086/terrain/tr_kabikha/utility_data/upazila_lists?dist_id="+dist_id;
        String union_url = "http://103.56.208.123:8086/terrain/tr_kabikha/utility_data/union_lists?dd_id="+dd_id;

        RequestQueue requestQueue = Volley.newRequestQueue(HomePage.this);

        StringRequest unionRequest = new StringRequest(Request.Method.GET, union_url, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray jsonArray = new JSONArray(items);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject unionObject = jsonArray.getJSONObject(i);
                        String pcun_ddu_id = unionObject.getString("pcun_ddu_id");
                        String ddu_union_name = unionObject.getString("ddu_union_name");

                        ddu_union_name = transformText(ddu_union_name);

                        unionLists.add(new UnionLists(pcun_ddu_id,ddu_union_name));

                    }
                }
                conn = true;
                updateUI();

            } catch (JSONException e) {
                e.printStackTrace();
                conn = false;
                updateUI();
            }
        }, error -> {

        });

        StringRequest upazilaRequest = new StringRequest(Request.Method.GET, upa_url, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray jsonArray = new JSONArray(items);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject upazilaObject = jsonArray.getJSONObject(i);
                        String pcu_dd_id = upazilaObject.getString("pcu_dd_id");
                        String thana_upozilla = upazilaObject.getString("thana_upozilla");

                        thana_upozilla = transformText(thana_upozilla);

                        upazilaLists.add(new UpazilaLists(pcu_dd_id,thana_upozilla));

                    }

                    if (dd_id != null) {
                        if (!dd_id.isEmpty()) {
                            requestQueue.add(unionRequest);
                        }
                        else {
                            conn = true;
                            updateUI();
                        }
                    }
                    else {
                        conn = true;
                        updateUI();
                    }
                }
                else {
                    conn = true;
                    updateUI();
                }

            } catch (JSONException e) {
                e.printStackTrace();
                conn = false;
                updateUI();
            }
        }, error -> {
            conn = false;
            updateUI();
        });

        StringRequest districtRequest = new StringRequest(Request.Method.GET, dist_url, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray jsonArray = new JSONArray(items);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject distObject = jsonArray.getJSONObject(i);
                        String p_dist_id = distObject.getString("p_dist_id");
                        String dist_name = distObject.getString("dist_name");

                        dist_name = transformText(dist_name);

                        districtLists.add(new DistrictLists(p_dist_id,dist_name));
                    }
                    if (dist_id != null) {
                        if (!dist_id.isEmpty()) {
                            requestQueue.add(upazilaRequest);
                        }
                        else {
                            conn = true;
                            updateUI();
                        }
                    }
                    else {
                        conn = true;
                        updateUI();
                    }
                }
                else {
                    conn = true;
                    updateUI();
                }

            } catch (JSONException e) {
                e.printStackTrace();
                conn = false;
                updateUI();
            }
        }, error -> {
            conn = false;
            updateUI();
        });

        StringRequest projectTypeRequest = new StringRequest(Request.Method.GET, p_type_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String items = jsonObject.getString("items");
                    String count = jsonObject.getString("count");
                    projectTypeLists.add(new ProjectTypeLists("","..."));
                    if (!count.equals("0")) {
                        JSONArray jsonArray = new JSONArray(items);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject pTypeObject = jsonArray.getJSONObject(i);
                            String p_ptm_id = pTypeObject.getString("p_ptm_id");
                            String ptm_project_type_name = pTypeObject.getString("ptm_project_type_name");

                            ptm_project_type_name = transformText(ptm_project_type_name);

                            projectTypeLists.add(new ProjectTypeLists(p_ptm_id,ptm_project_type_name));
                        }
                    }
                    if (div_id != null) {
                        if (!div_id.isEmpty()) {
                            requestQueue.add(districtRequest);
                        }
                        else {
                            conn = true;
                            updateUI();
                        }
                    }
                    else {
                        conn = true;
                        updateUI();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    conn = false;
                    updateUI();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                conn = false;
                updateUI();
            }
        });

        StringRequest fundRequest = new StringRequest(Request.Method.GET, fund_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String items = jsonObject.getString("items");
                    String count = jsonObject.getString("count");
                    sourceFundLists.add(new SourceFundLists("","..."));
                    if (!count.equals("0")) {
                        JSONArray jsonArray = new JSONArray(items);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject fundObject = jsonArray.getJSONObject(i);
                            String p_fsm_id = fundObject.getString("p_fsm_id");
                            String fsm_fund_name = fundObject.getString("fsm_fund_name");

                            fsm_fund_name = transformText(fsm_fund_name);

                            sourceFundLists.add(new SourceFundLists(p_fsm_id,fsm_fund_name));
                        }
                    }
                    requestQueue.add(projectTypeRequest);

                } catch (JSONException e) {
                    e.printStackTrace();
                    conn = false;
                    updateUI();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                conn = false;
                updateUI();
            }
        });

        StringRequest divRequest = new StringRequest(Request.Method.GET, div_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String items = jsonObject.getString("items");
                    String count = jsonObject.getString("count");
                    if (!count.equals("0")) {
                        divisionLists.add(new DivisionLists("","..."));
                        JSONArray jsonArray = new JSONArray(items);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject divObject = jsonArray.getJSONObject(i);
                            String p_div_id = divObject.getString("p_div_id");
                            String div_name = divObject.getString("div_name");

                            div_name = transformText(div_name);

                            divisionLists.add(new DivisionLists(p_div_id,div_name));
                        }
                        requestQueue.add(fundRequest);
                    }
                    else {
                        conn = false;
                        updateUI();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    conn = false;
                    updateUI();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                conn = false;
                updateUI();
            }
        });

        StringRequest fyRequest = new StringRequest(Request.Method.GET, fy_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String items = jsonObject.getString("items");
                    String count = jsonObject.getString("count");
                    if (!count.equals("0")) {
                        JSONArray jsonArray = new JSONArray(items);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject fyObject = jsonArray.getJSONObject(i);
                            String p_fy_id = fyObject.getString("p_fy_id");
                            String fy_financial_year_name = fyObject.getString("fy_financial_year_name");
                            String fy_from_year = fyObject.getString("fy_from_year");
                            String fy_to_year = fyObject.getString("fy_to_year");
                            String fy_details = fyObject.getString("fy_details");
                            String fy_active_flag = fyObject.getString("fy_active_flag");

                            fysLists.add(new FinancialYearLists(p_fy_id,fy_financial_year_name,fy_from_year,
                                    fy_to_year,fy_details,fy_active_flag));
                            fyeLists.add(new FinancialYearLists(p_fy_id,fy_financial_year_name,fy_from_year,
                                    fy_to_year,fy_details,fy_active_flag));
                        }
                        requestQueue.add(divRequest);
                    }
                    else {
                        conn = false;
                        updateUI();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    conn = false;
                    updateUI();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                conn = false;
                updateUI();
            }
        });

        requestQueue.add(fyRequest);
    }

    public void updateUI() {
        waitProgress.dismiss();
        if (conn) {

            ArrayList<String> type = new ArrayList<>();
            for(int i = 0; i < fysLists.size(); i++) {
                type.add(fysLists.get(i).getFinancialYearName());
            }
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type);

            financialYearStart.setAdapter(arrayAdapter);

            ArrayList<String> type1 = new ArrayList<>();
            for(int i = 0; i < fyeLists.size(); i++) {
                type1.add(fyeLists.get(i).getFinancialYearName());
            }
            ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(getApplicationContext(),R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type1);

            financialYearEnd.setAdapter(arrayAdapter1);

            ArrayList<String> type2 = new ArrayList<>();
            for(int i = 0; i < divisionLists.size(); i++) {
                type2.add(divisionLists.get(i).getDivName());
            }
            ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(getApplicationContext(),R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type2);

            division.setAdapter(arrayAdapter2);

            ArrayList<String> type3 = new ArrayList<>();
            for(int i = 0; i < sourceFundLists.size(); i++) {
                type3.add(sourceFundLists.get(i).getFundName());
            }
            ArrayAdapter<String> arrayAdapter3 = new ArrayAdapter<String>(getApplicationContext(),R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type3);

            sourceOfFund.setAdapter(arrayAdapter3);

            ArrayList<String> type4 = new ArrayList<>();
            for(int i = 0; i < projectTypeLists.size(); i++) {
                type4.add(projectTypeLists.get(i).getProjectTypeName());
            }
            ArrayAdapter<String> arrayAdapter4 = new ArrayAdapter<String>(getApplicationContext(),R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type4);

            projectType.setAdapter(arrayAdapter4);

            if (div_id != null) {
                if (!div_id.isEmpty()) {
                    ArrayList<String> type5 = new ArrayList<>();
                    for(int i = 0; i < districtLists.size(); i++) {
                        type5.add(districtLists.get(i).getDistName());
                    }
                    ArrayAdapter<String> arrayAdapter5 = new ArrayAdapter<String>(getApplicationContext(),R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type5);

                    district.setAdapter(arrayAdapter5);
                }
            }

            if (dist_id != null) {
                if (!dist_id.isEmpty()) {
                    ArrayList<String> type6 = new ArrayList<>();
                    for(int i = 0; i < upazilaLists.size(); i++) {
                        type6.add(upazilaLists.get(i).getThanaName());
                    }
                    ArrayAdapter<String> arrayAdapter6 = new ArrayAdapter<String>(getApplicationContext(),R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type6);

                    upazila.setAdapter(arrayAdapter6);
                }
            }

            if (dd_id != null) {
                if (!dd_id.isEmpty()) {
                    ArrayList<String> type7 = new ArrayList<>();
                    for(int i = 0; i < unionLists.size(); i++) {
                        type7.add(unionLists.get(i).getUnionName());
                    }
                    ArrayAdapter<String> arrayAdapter7 = new ArrayAdapter<String>(getApplicationContext(),R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type7);

                    union.setAdapter(arrayAdapter7);
                }
            }

            conn = false;

        }
        else {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
            AlertDialog dialog = new AlertDialog.Builder(HomePage.this)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("EXIT",null)
                    .show();

            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    getQuery();
                    dialog.dismiss();
                }
            });

            Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            negative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    switch (userType) {
                        case "GUEST":
                            finish();
                            break;
                        case "ADMIN":
                            userInfoLists.clear();
                            userInfoLists = new ArrayList<>();
                            finish();
                            break;
                        case "PIC_USER":
                            picUserDetails.clear();
                            picUserDetails = new ArrayList<>();
                            finish();
                            break;
                    }
                }
            });
        }
    }

    //    --------------------------Getting Districts and updating UI-----------------------------
    public void getDistricts() {
        waitProgress.show(getSupportFragmentManager(), "WaitBar");
        waitProgress.setCancelable(false);
        conn = false;

        districtLists = new ArrayList<>();

        if (div_id == null) {
            div_id = "";
        }
        String dist_url = "http://103.56.208.123:8086/terrain/tr_kabikha/utility_data/dist_lists?div_id="+div_id;

        RequestQueue requestQueue = Volley.newRequestQueue(HomePage.this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, dist_url, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray jsonArray = new JSONArray(items);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject distObject = jsonArray.getJSONObject(i);
                        String p_dist_id = distObject.getString("p_dist_id");
                        String dist_name = distObject.getString("dist_name");

                        dist_name = transformText(dist_name);

                        districtLists.add(new DistrictLists(p_dist_id,dist_name));
                    }
                }
                conn = true;
                updateDistricts();

            } catch (JSONException e) {
                e.printStackTrace();
                conn = false;
                updateDistricts();
            }
        }, error -> {
            conn = false;
            updateDistricts();
        });

        requestQueue.add(stringRequest);

    }

    public void updateDistricts() {
        waitProgress.dismiss();
        if (conn) {

            districtLay.setEnabled(true);

            ArrayList<String> type = new ArrayList<>();
            for(int i = 0; i < districtLists.size(); i++) {
                type.add(districtLists.get(i).getDistName());
            }
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type);

            district.setAdapter(arrayAdapter);

            conn = false;

        }
        else {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
            AlertDialog dialog = new AlertDialog.Builder(HomePage.this)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .show();

//                dialog.setCancelable(false);
//                dialog.setCanceledOnTouchOutside(false);
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    getDistricts();
                    dialog.dismiss();
                }
            });
        }
    }

    //    --------------------------Getting Upazillas and updating UI-----------------------------
    public void getUpazilas() {
        waitProgress.show(getSupportFragmentManager(), "WaitBar");
        waitProgress.setCancelable(false);
        conn = false;

        upazilaLists = new ArrayList<>();

        if (dist_id == null) {
            dist_id = "";
        }
        String upazila_url = "http://103.56.208.123:8086/terrain/tr_kabikha/utility_data/upazila_lists?dist_id="+dist_id;

        RequestQueue requestQueue = Volley.newRequestQueue(HomePage.this);

        StringRequest upazilaRequest = new StringRequest(Request.Method.GET, upazila_url, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray jsonArray = new JSONArray(items);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject upazilaObject = jsonArray.getJSONObject(i);
                        String pcu_dd_id = upazilaObject.getString("pcu_dd_id");
                        String thana_upozilla = upazilaObject.getString("thana_upozilla");

                        thana_upozilla = transformText(thana_upozilla);

                        upazilaLists.add(new UpazilaLists(pcu_dd_id,thana_upozilla));

                    }
                }
                conn = true;
                updateUpazila();

            } catch (JSONException e) {
                e.printStackTrace();
                conn = false;
                updateUpazila();
            }
        }, error -> {
            conn = false;
            updateUpazila();
        });

        requestQueue.add(upazilaRequest);
    }

    public void updateUpazila() {
        waitProgress.dismiss();
        if (conn) {

            upazilaLay.setEnabled(true);

            ArrayList<String> type = new ArrayList<>();
            for(int i = 0; i < upazilaLists.size(); i++) {
                type.add(upazilaLists.get(i).getThanaName());
            }
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type);

            upazila.setAdapter(arrayAdapter);

            conn = false;

        }
        else {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
            AlertDialog dialog = new AlertDialog.Builder(HomePage.this)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .show();

//                dialog.setCancelable(false);
//                dialog.setCanceledOnTouchOutside(false);
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    getUpazilas();
                    dialog.dismiss();
                }
            });
        }
    }

    //    --------------------------Getting Unions and updating UI-----------------------------
    public void getUnions() {
        waitProgress.show(getSupportFragmentManager(), "WaitBar");
        waitProgress.setCancelable(false);
        conn = false;

        unionLists = new ArrayList<>();

        if (dd_id == null) {
            dd_id = "";
        }

        String union_url = "http://103.56.208.123:8086/terrain/tr_kabikha/utility_data/union_lists?dd_id="+dd_id;

        RequestQueue requestQueue = Volley.newRequestQueue(HomePage.this);

        StringRequest unionRequest = new StringRequest(Request.Method.GET, union_url, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray jsonArray = new JSONArray(items);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject unionObject = jsonArray.getJSONObject(i);
                        String pcun_ddu_id = unionObject.getString("pcun_ddu_id");
                        String ddu_union_name = unionObject.getString("ddu_union_name");

                        ddu_union_name = transformText(ddu_union_name);

                        unionLists.add(new UnionLists(pcun_ddu_id,ddu_union_name));

                    }
                }
                conn = true;
                updateUnion();

            } catch (JSONException e) {
                e.printStackTrace();
                conn = false;
                updateUnion();
            }
        }, error -> {
            conn = false;
            updateUnion();
        });

        requestQueue.add(unionRequest);
    }

    public void updateUnion() {
        waitProgress.dismiss();
        if (conn) {

            unionLay.setEnabled(true);

            ArrayList<String> type = new ArrayList<>();
            for(int i = 0; i < unionLists.size(); i++) {
                type.add(unionLists.get(i).getUnionName());
            }
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type);

            union.setAdapter(arrayAdapter);

            conn = false;

        }
        else {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
            AlertDialog dialog = new AlertDialog.Builder(HomePage.this)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .show();

//                dialog.setCancelable(false);
//                dialog.setCanceledOnTouchOutside(false);
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    getUnions();
                    dialog.dismiss();
                }
            });
        }
    }

    //    --------------------------Getting Project Sub Types and updating UI-----------------------------
    public void getProjectSubType() {
        waitProgress.show(getSupportFragmentManager(), "WaitBar");
        waitProgress.setCancelable(false);
        conn = false;

        projectSubTypeLists = new ArrayList<>();

        if (ptm_id == null) {
            ptm_id = "";
        }

        String pr_sub_type_url = "http://103.56.208.123:8086/terrain/tr_kabikha/utility_data/project_sub_type_lists?ptm_id="+ptm_id;

        RequestQueue requestQueue = Volley.newRequestQueue(HomePage.this);

        StringRequest prSubTypeRequest = new StringRequest(Request.Method.GET, pr_sub_type_url, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray jsonArray = new JSONArray(items);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject prSubTypeObject = jsonArray.getJSONObject(i);
                        String p_ptd_id = prSubTypeObject.getString("p_ptd_id");
                        String ptd_project_subtype_name = prSubTypeObject.getString("ptd_project_subtype_name");

                        ptd_project_subtype_name = transformText(ptd_project_subtype_name);

                        projectSubTypeLists.add(new ProjectSubTypeLists(p_ptd_id,ptd_project_subtype_name));

                    }
                }
                conn = true;
                updateProjectSubTypes();

            } catch (JSONException e) {
                e.printStackTrace();
                conn = false;
                updateProjectSubTypes();
            }
        }, error -> {
            conn = false;
            updateProjectSubTypes();
        });

        requestQueue.add(prSubTypeRequest);
    }

    public void updateProjectSubTypes() {
        waitProgress.dismiss();
        if (conn) {

            projectSubTypeLay.setEnabled(true);

            ArrayList<String> type = new ArrayList<>();
            for(int i = 0; i < projectSubTypeLists.size(); i++) {
                type.add(projectSubTypeLists.get(i).getProjectSubTypeName());
            }
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type);

            projectSubType.setAdapter(arrayAdapter);

            conn = false;

        }
        else {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
            AlertDialog dialog = new AlertDialog.Builder(HomePage.this)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .show();

//                dialog.setCancelable(false);
//                dialog.setCanceledOnTouchOutside(false);
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    getProjectSubType();
                    dialog.dismiss();
                }
            });
        }
    }

    //    --------------------------Getting Project Data and going to project lists-----------------------------
    public void getProjectData() {
        waitProgress.show(getSupportFragmentManager(), "WaitBar");
        waitProgress.setCancelable(false);
        conn = false;

        projectlists = new ArrayList<>();

        if (ptm_id == null) {
            ptm_id = "";
        }

        if (fsm_id == null) {
            fsm_id = "";
        }

        if (ddu_id == null) {
            ddu_id = "";
        }

        if (dist_id == null) {
            dist_id = "";
        }

        if (dd_id == null) {
            dd_id = "";
        }

        if (ptd_Id == null) {
            ptd_Id = "";
        }

        if (div_id == null) {
            div_id = "";
        }

        final int[] countingNum = {0};

        String projectDataURL = "";
        if (userType.equals("PIC_USER")) {
            if (pcmUser.equals("admin")) {
                projectDataURL = "http://103.56.208.123:8086/terrain/tr_kabikha/projects/projectData_admin?ptd_Id="+ptd_Id+"&ptm_id="+ptm_id+
                        "&fsm_id="+fsm_id+"&ddu_id="+ddu_id+"&dd_id="+dd_id+"&dist_id="+dist_id+"&div_id="+div_id+"&fys_id="+fys_id+"&fye_id="+fye_id;
            }
            else {
                projectDataURL = "http://103.56.208.123:8086/terrain/tr_kabikha/projects/projectData_pic?ptd_Id="+ptd_Id+"&ptm_id="+ptm_id+
                        "&fsm_id="+fsm_id+"&ddu_id="+ddu_id+"&dd_id="+dd_id+"&dist_id="+dist_id+"&div_id="+div_id+"&fys_id="+fys_id+
                        "&fye_id="+fye_id+"&pcm_user="+pcmUser;
            }
        }
        else {
            projectDataURL = "http://103.56.208.123:8086/terrain/tr_kabikha/projects/projectData_admin?ptd_Id="+ptd_Id+"&ptm_id="+ptm_id+
                    "&fsm_id="+fsm_id+"&ddu_id="+ddu_id+"&dd_id="+dd_id+"&dist_id="+dist_id+"&div_id="+div_id+"&fys_id="+fys_id+"&fye_id="+fye_id;
        }

        RequestQueue requestQueue = Volley.newRequestQueue(HomePage.this);

        StringRequest projectDataRequest = new StringRequest(Request.Method.GET, projectDataURL, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray jsonArray = new JSONArray(items);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject projectDataObject = jsonArray.getJSONObject(i);

                        String pcm_id = projectDataObject.getString("pcm_id");
                        String entry_date = projectDataObject.getString("entry_date");
                        String pcm_internal_no = projectDataObject.getString("pcm_internal_no");

                        String pcm_project_code = projectDataObject.getString("pcm_project_code");
                        pcm_project_code = transformText(pcm_project_code);

                        String pcm_user = projectDataObject.getString("pcm_user");

                        String pcm_project_name = projectDataObject.getString("pcm_project_name");
                        pcm_project_name = transformText(pcm_project_name);

                        String pcm_project_no = projectDataObject.getString("pcm_project_no");
                        pcm_project_no = transformText(pcm_project_no);

                        String pcm_project_date = projectDataObject.getString("pcm_project_date");

                        String pcm_pic_chairman_name = projectDataObject.getString("pcm_pic_chairman_name");
                        pcm_pic_chairman_name = transformText(pcm_pic_chairman_name);

                        String pcm_pic_chairman_details = projectDataObject.getString("pcm_pic_chairman_details");
                        pcm_pic_chairman_details = transformText(pcm_pic_chairman_details);

                        String pcm_estimate_project_value = projectDataObject.getString("pcm_estimate_project_value");
                        String fy_financial_year_name = projectDataObject.getString("fy_financial_year_name");

                        String fsm_fund_name = projectDataObject.getString("fsm_fund_name");
                        fsm_fund_name = transformText(fsm_fund_name);

                        String ptm_project_type_name = projectDataObject.getString("ptm_project_type_name");
                        ptm_project_type_name = transformText(ptm_project_type_name);

                        String ptd_project_subtype_name = projectDataObject.getString("ptd_project_subtype_name");
                        ptd_project_subtype_name = transformText(ptd_project_subtype_name);

                        String psc_sanction_cat_name = projectDataObject.getString("psc_sanction_cat_name");
                        psc_sanction_cat_name = transformText(psc_sanction_cat_name);

                        String pcm_category_name = projectDataObject.getString("pcm_category_name");
                        pcm_category_name = transformText(pcm_category_name);

                        String pcun_ddu_id = projectDataObject.getString("pcun_ddu_id");
                        String pcu_dd_id = projectDataObject.getString("pcu_dd_id");
                        String pcm_proj_evaluation_remarks = projectDataObject.getString("pcm_proj_evaluation_remarks");
                        pcm_proj_evaluation_remarks = transformText(pcm_proj_evaluation_remarks);

                        String pcm_project_details = projectDataObject.getString("pcm_project_details");
                        pcm_project_details = transformText(pcm_project_details);

                        String start_date = projectDataObject.getString("start_date");
                        String end_date = projectDataObject.getString("end_date");
                        String pcm_project_sanction_type = projectDataObject.getString("pcm_project_sanction_type");
                        String rownumber_ = projectDataObject.getString("rownumber_");
                        String map_data_available = projectDataObject.getString("map_data_available");

                        boolean map_data = false;
                        map_data = !map_data_available.equals("0");

                        String image_data_available = projectDataObject.getString("image_data_available");

                        boolean image_data = false;
                        image_data = !image_data_available.equals("0");

                        countingNum[0]++;
                        String stype = "";
                        switch (pcm_project_sanction_type) {
                            case "0":
                                stype = "Taka(টাকা)";
                                break;
                            case "1":
                                stype = "Rice(চাল) (MT)";
                                break;
                            case "2":
                                stype = "Wheat(গম) (MT)";
                                break;
                        }

                        String pCount = "#"+countingNum[0];

                        projectlists.add(new Projectlists(pcm_id,entry_date,pcm_internal_no,
                                pcm_project_code,pcm_user,pcm_project_name,
                                pcm_project_no,pcm_project_date,pcm_pic_chairman_name,
                                pcm_pic_chairman_details,pcm_estimate_project_value,fy_financial_year_name,
                                fsm_fund_name,ptm_project_type_name,ptd_project_subtype_name,
                                psc_sanction_cat_name,pcm_category_name,pcun_ddu_id,
                                pcu_dd_id,pcm_proj_evaluation_remarks,null,
                                pcm_project_details,start_date,end_date,
                                stype,map_data,image_data,rownumber_,pCount,new ArrayList<>()));

//                        numberOfRequestsToMake++;
//                        System.out.println(projectlists.size() +", index: "+ i + "number of requests: "+ numberOfRequestsToMake);
//                        getLocations(pcm_id,i,projectlists);


                    }
                    conn = true;
                    goToProjectLists();
                }
                else {
                    conn = true;
                    goToProjectLists();
                }

            } catch (JSONException e) {
                e.printStackTrace();
                conn = false;
                goToProjectLists();
            }
        }, error -> {
            conn = false;
            goToProjectLists();
        });

        requestQueue.add(projectDataRequest);
    }

    public void goToProjectLists() {
        waitProgress.dismiss();
        if (conn) {
            if (projectlists.size() != 0) {
                System.out.println(projectlists.size());
                Intent intent = new Intent(HomePage.this, Projects.class);
                startActivity(intent);
            }
            else {
                Toast.makeText(getApplicationContext(),"No Project Found",Toast.LENGTH_SHORT).show();
            }
            conn = false;

        }
        else {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
            AlertDialog dialog = new AlertDialog.Builder(HomePage.this)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .show();

//                dialog.setCancelable(false);
//                dialog.setCanceledOnTouchOutside(false);
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    getProjectData();
                    dialog.dismiss();
                }
            });
        }
    }

    //    --------------------------Getting Project Map Data and going to project lists-----------------------------
    public void getProjectMapData() {
        waitProgress.show(getSupportFragmentManager(), "WaitBar");
        waitProgress.setCancelable(false);
        conn = false;

        projectMapsLists = new ArrayList<>();

        if (ptm_id == null) {
            ptm_id = "";
        }

        if (fsm_id == null) {
            fsm_id = "";
        }

        if (ddu_id == null) {
            ddu_id = "";
        }

        if (dist_id == null) {
            dist_id = "";
        }

        if (dd_id == null) {
            dd_id = "";
        }

        if (ptd_Id == null) {
            ptd_Id = "";
        }

        if (div_id == null) {
            div_id = "";
        }

        final int[] countingNum = {0};

        String projectMapDataURL = "";

        if (userType.equals("PIC_USER")) {
            if (pcmUser.equals("admin")) {
                projectMapDataURL = "http://103.56.208.123:8086/terrain/tr_kabikha/projects/projectMapData_admin?ptd_Id="+ptd_Id+"&ptm_id="+ptm_id+
                        "&fsm_id="+fsm_id+"&ddu_id="+ddu_id+"&dd_id="+dd_id+"&dist_id="+dist_id+"&div_id="+div_id+"&fys_id="+fys_id+"&fye_id="+fye_id;
            }
            else {
                projectMapDataURL = "http://103.56.208.123:8086/terrain/tr_kabikha/projects/projectMapData_pic?ptd_Id="+ptd_Id+"&ptm_id="+ptm_id+
                        "&fsm_id="+fsm_id+"&ddu_id="+ddu_id+"&dd_id="+dd_id+"&dist_id="+dist_id+"&div_id="+div_id+"&fys_id="+fys_id+
                        "&fye_id="+fye_id+"&pcm_user="+pcmUser;
            }
        }
        else {
            projectMapDataURL = "http://103.56.208.123:8086/terrain/tr_kabikha/projects/projectMapData_admin?ptd_Id="+ptd_Id+"&ptm_id="+ptm_id+
                    "&fsm_id="+fsm_id+"&ddu_id="+ddu_id+"&dd_id="+dd_id+"&dist_id="+dist_id+"&div_id="+div_id+"&fys_id="+fys_id+"&fye_id="+fye_id;
        }

        RequestQueue requestQueue = Volley.newRequestQueue(HomePage.this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, projectMapDataURL, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray jsonArray = new JSONArray(items);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject projectMapDataObject = jsonArray.getJSONObject(i);

                        String pcm_id = projectMapDataObject.getString("pcm_id");
                        String entry_date = projectMapDataObject.getString("entry_date");
                        String pcm_internal_no = projectMapDataObject.getString("pcm_internal_no");

                        String pcm_project_code = projectMapDataObject.getString("pcm_project_code");
                        pcm_project_code = transformText(pcm_project_code);

                        String pcm_user = projectMapDataObject.getString("pcm_user");

                        String pcm_project_name = projectMapDataObject.getString("pcm_project_name");
                        pcm_project_name = transformText(pcm_project_name);

                        String pcm_project_no = projectMapDataObject.getString("pcm_project_no");
                        pcm_project_no = transformText(pcm_project_no);

                        String pcm_project_date = projectMapDataObject.getString("pcm_project_date");

                        String pcm_pic_chairman_name = projectMapDataObject.getString("pcm_pic_chairman_name");
                        pcm_pic_chairman_name = transformText(pcm_pic_chairman_name);

                        String pcm_pic_chairman_details = projectMapDataObject.getString("pcm_pic_chairman_details");
                        pcm_pic_chairman_details = transformText(pcm_pic_chairman_details);

                        String pcm_estimate_project_value = projectMapDataObject.getString("pcm_estimate_project_value");
                        String fy_financial_year_name = projectMapDataObject.getString("fy_financial_year_name");

                        String fsm_fund_name = projectMapDataObject.getString("fsm_fund_name");
                        fsm_fund_name = transformText(fsm_fund_name);

                        String ptm_project_type_name = projectMapDataObject.getString("ptm_project_type_name");
                        ptm_project_type_name = transformText(ptm_project_type_name);

                        String ptd_project_subtype_name = projectMapDataObject.getString("ptd_project_subtype_name");
                        ptd_project_subtype_name = transformText(ptd_project_subtype_name);

                        String psc_sanction_cat_name = projectMapDataObject.getString("psc_sanction_cat_name");
                        psc_sanction_cat_name = transformText(psc_sanction_cat_name);

                        String pcm_category_name = projectMapDataObject.getString("pcm_category_name");
                        pcm_category_name = transformText(pcm_category_name);

                        String pcun_ddu_id = projectMapDataObject.getString("pcun_ddu_id");
                        String pcu_dd_id = projectMapDataObject.getString("pcu_dd_id");
                        String pcm_proj_evaluation_remarks = projectMapDataObject.getString("pcm_proj_evaluation_remarks");
                        pcm_proj_evaluation_remarks = transformText(pcm_proj_evaluation_remarks);

                        String pcm_project_details = projectMapDataObject.getString("pcm_project_details");
                        pcm_project_details = transformText(pcm_project_details);

                        String start_date = projectMapDataObject.getString("start_date");
                        String end_date = projectMapDataObject.getString("end_date");
                        String pcm_project_sanction_type = projectMapDataObject.getString("pcm_project_sanction_type");
                        String rownumber_ = projectMapDataObject.getString("rownumber_");

                        countingNum[0]++;
                        String stype = "";
                        switch (pcm_project_sanction_type) {
                            case "0":
                                stype = "Taka(টাকা)";
                                break;
                            case "1":
                                stype = "Rice(চাল) (MT)";
                                break;
                            case "2":
                                stype = "Wheat(গম) (MT)";
                                break;
                        }

                        String pCount = "#"+countingNum[0];

                        projectMapsLists.add(new ProjectMapsLists(pcm_id,entry_date,pcm_internal_no,
                                pcm_project_code,pcm_user,pcm_project_name,
                                pcm_project_no,pcm_project_date,pcm_pic_chairman_name,
                                pcm_pic_chairman_details,pcm_estimate_project_value,fy_financial_year_name,
                                fsm_fund_name,ptm_project_type_name,ptd_project_subtype_name,
                                psc_sanction_cat_name,pcm_category_name,pcun_ddu_id,
                                pcu_dd_id,pcm_proj_evaluation_remarks,null,
                                pcm_project_details,start_date,end_date,
                                stype,rownumber_,pCount,new ArrayList<>()));

//                        System.out.println(projectMapsLists.size() +", index: "+ i + "number of requests: "+ numberOfRequestsToMake);

                    }
                    getMapLocations();
                }
                else {
                    conn = true;
                    goToProjectMapLists();
                }

            } catch (JSONException e) {
                e.printStackTrace();
                conn = false;
                goToProjectMapLists();
            }
        }, error -> {
            conn = false;
            goToProjectMapLists();
        });

        requestQueue.add(stringRequest);

    }

    public void getMapLocations() {
        String url = "http://103.56.208.123:8086/terrain/tr_kabikha/all_locations/project_locations";

        RequestQueue requestQueue = Volley.newRequestQueue(HomePage.this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");

                if (!count.equals("0")) {
                    JSONArray jsonArray = new JSONArray(items);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject locationObject = jsonArray.getJSONObject(i);
                        String pcmgd_latitude = locationObject.getString("pcmgd_latitude");
                        String pcmgd_longitude = locationObject.getString("pcmgd_longitude");
                        int segment = locationObject.getInt("segment");
                        String pcmgd_pcm_id = locationObject.getString("pcmgd_pcm_id");

                        for (int j = 0; j < projectMapsLists.size(); j++) {
                            if (pcmgd_pcm_id.equals(projectMapsLists.get(j).getPcmId())) {
                                ArrayList<LocationLists> locationLists = projectMapsLists.get(j).getLocationLists();
                                locationLists.add(new LocationLists(pcmgd_latitude,pcmgd_longitude,segment));
                                projectMapsLists.get(j).setLocationLists(locationLists);
                            }
                        }
                    }
                }
                conn = true;
                goToProjectMapLists();

            } catch (JSONException e) {
                e.printStackTrace();
                conn = false;
                goToProjectMapLists();
            }
        }, error -> {
            conn = false;
            goToProjectMapLists();
        });

        requestQueue.add(stringRequest);
    }

    public void goToProjectMapLists() {
        waitProgress.dismiss();
        if (conn) {
            if (projectMapsLists.size() != 0) {
                System.out.println(projectMapsLists.size());
                Intent intent = new Intent(HomePage.this, ProjectsMaps.class);
                intent.putExtra("DIST_ID",dist_id);
                intent.putExtra("DD_ID",dd_id);
                intent.putExtra("DDU_ID",ddu_id);
                startActivity(intent);
            }
            else {
                Toast.makeText(getApplicationContext(),"No Project Found",Toast.LENGTH_SHORT).show();
            }

            conn = false;

        }
        else {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
            AlertDialog dialog = new AlertDialog.Builder(HomePage.this)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .show();

//                dialog.setCancelable(false);
//                dialog.setCanceledOnTouchOutside(false);
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    getProjectMapData();
                    dialog.dismiss();
                }
            });
        }
    }

    //    --------------------------Getting Project Data to Update and going to project lists-----------------------------
    public void getProjectUpdateData() {
        waitProgress.show(getSupportFragmentManager(), "WaitBar");
        waitProgress.setCancelable(false);
        conn = false;

        projectUpdateLists = new ArrayList<>();

        if (ptm_id == null) {
            ptm_id = "";
        }

        if (fsm_id == null) {
            fsm_id = "";
        }

        if (ddu_id == null) {
            ddu_id = "";
        }

        if (dist_id == null) {
            dist_id = "";
        }

        if (dd_id == null) {
            dd_id = "";
        }

        if (ptd_Id == null) {
            ptd_Id = "";
        }

        if (div_id == null) {
            div_id = "";
        }

        final int[] countingNum = {0};

        String projectUpdateDataURL = "";

        if (userType.equals("PIC_USER")) {
            if (pcmUser.equals("admin")) {
                projectUpdateDataURL = "http://103.56.208.123:8086/terrain/tr_kabikha/projects/projectToUpdate_Data_admin?ptd_Id="+ptd_Id+"&ptm_id="+ptm_id+
                        "&fsm_id="+fsm_id+"&ddu_id="+ddu_id+"&dd_id="+dd_id+"&dist_id="+dist_id+"&div_id="+div_id+"&fys_id="+fys_id+"&fye_id="+fye_id;
            }
            else {
                projectUpdateDataURL = "http://103.56.208.123:8086/terrain/tr_kabikha/projects/projectToUpdate_Data_pic?ptd_Id="+ptd_Id+"&ptm_id="+ptm_id+
                        "&fsm_id="+fsm_id+"&ddu_id="+ddu_id+"&dd_id="+dd_id+"&dist_id="+dist_id+"&div_id="+div_id+"&fys_id="+fys_id+
                        "&fye_id="+fye_id+"&pcm_user="+pcmUser;
            }
        }
        else {
            projectUpdateDataURL = "http://103.56.208.123:8086/terrain/tr_kabikha/projects/projectToUpdate_Data_admin?ptd_Id="+ptd_Id+"&ptm_id="+ptm_id+
                    "&fsm_id="+fsm_id+"&ddu_id="+ddu_id+"&dd_id="+dd_id+"&dist_id="+dist_id+"&div_id="+div_id+"&fys_id="+fys_id+"&fye_id="+fye_id;
        }

        RequestQueue requestQueue = Volley.newRequestQueue(HomePage.this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, projectUpdateDataURL, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray jsonArray = new JSONArray(items);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject projectUpdateDataObject = jsonArray.getJSONObject(i);

                        String pcm_id = projectUpdateDataObject.getString("pcm_id");
                        String entry_date = projectUpdateDataObject.getString("entry_date");
                        String pcm_internal_no = projectUpdateDataObject.getString("pcm_internal_no");

                        String pcm_project_code = projectUpdateDataObject.getString("pcm_project_code");
                        pcm_project_code = transformText(pcm_project_code);

                        String pcm_user = projectUpdateDataObject.getString("pcm_user");

                        String pcm_project_name = projectUpdateDataObject.getString("pcm_project_name");
                        pcm_project_name = transformText(pcm_project_name);

                        String pcm_project_no = projectUpdateDataObject.getString("pcm_project_no");
                        pcm_project_no = transformText(pcm_project_no);

                        String pcm_project_date = projectUpdateDataObject.getString("pcm_project_date");

                        String pcm_pic_chairman_name = projectUpdateDataObject.getString("pcm_pic_chairman_name");
                        pcm_pic_chairman_name = transformText(pcm_pic_chairman_name);

                        String pcm_pic_chairman_details = projectUpdateDataObject.getString("pcm_pic_chairman_details");
                        pcm_pic_chairman_details = transformText(pcm_pic_chairman_details);

                        String pcm_estimate_project_value = projectUpdateDataObject.getString("pcm_estimate_project_value");
                        String fy_financial_year_name = projectUpdateDataObject.getString("fy_financial_year_name");

                        String fsm_fund_name = projectUpdateDataObject.getString("fsm_fund_name");
                        fsm_fund_name = transformText(fsm_fund_name);

                        String ptm_project_type_name = projectUpdateDataObject.getString("ptm_project_type_name");
                        ptm_project_type_name = transformText(ptm_project_type_name);

                        String ptd_project_subtype_name = projectUpdateDataObject.getString("ptd_project_subtype_name");
                        ptd_project_subtype_name = transformText(ptd_project_subtype_name);

                        String psc_sanction_cat_name = projectUpdateDataObject.getString("psc_sanction_cat_name");
                        psc_sanction_cat_name = transformText(psc_sanction_cat_name);

                        String pcm_category_name = projectUpdateDataObject.getString("pcm_category_name");
                        pcm_category_name = transformText(pcm_category_name);

                        String pcun_ddu_id = projectUpdateDataObject.getString("pcun_ddu_id");
                        String pcu_dd_id = projectUpdateDataObject.getString("pcu_dd_id");
                        String pcm_proj_evaluation_remarks = projectUpdateDataObject.getString("pcm_proj_evaluation_remarks");
                        pcm_proj_evaluation_remarks = transformText(pcm_proj_evaluation_remarks);

                        String pcm_project_details = projectUpdateDataObject.getString("pcm_project_details");
                        pcm_project_details = transformText(pcm_project_details);

                        String start_date = projectUpdateDataObject.getString("start_date");
                        String end_date = projectUpdateDataObject.getString("end_date");
                        String pcm_project_sanction_type = projectUpdateDataObject.getString("pcm_project_sanction_type");
                        String pcm_psc_id = projectUpdateDataObject.getString("pcm_psc_id");
                        String pcm_pcm_id = projectUpdateDataObject.getString("pcm_pcm_id");
                        String rownumber_ = projectUpdateDataObject.getString("rownumber_");

                        String map_data_available = projectUpdateDataObject.getString("map_data_available");

                        boolean map_data = false;
                        map_data = !map_data_available.equals("0");

                        String image_data_available = projectUpdateDataObject.getString("image_data_available");

                        String ptd_distance_limit = projectUpdateDataObject.getString("ptd_distance_limit").equals("null") ? "" : projectUpdateDataObject.getString("ptd_distance_limit");;

                        boolean image_data = false;
                        image_data = !image_data_available.equals("0");

                        countingNum[0]++;
                        String stype = "";
                        switch (pcm_project_sanction_type) {
                            case "0":
                                stype = "Taka(টাকা)";
                                break;
                            case "1":
                                stype = "Rice(চাল) (MT)";
                                break;
                            case "2":
                                stype = "Wheat(গম) (MT)";
                                break;
                        }

                        String pCount = "#"+countingNum[0];

                        projectUpdateLists.add(new ProjectUpdateLists(pcm_id,entry_date,pcm_internal_no,
                                pcm_project_code,pcm_user,pcm_project_name,
                                pcm_project_no,pcm_project_date,pcm_pic_chairman_name,
                                pcm_pic_chairman_details,pcm_estimate_project_value,fy_financial_year_name,
                                fsm_fund_name,ptm_project_type_name,ptd_project_subtype_name,
                                psc_sanction_cat_name,pcm_category_name,pcun_ddu_id,
                                pcu_dd_id,pcm_proj_evaluation_remarks,null,
                                pcm_project_details,start_date,end_date,
                                stype,pcm_project_sanction_type,pcm_psc_id,pcm_pcm_id,map_data,image_data,
                                ptd_distance_limit,rownumber_,pCount,new ArrayList<>()));


                    }
                    getLocationsForUpdate();
                }
                else {
                    conn = true;
                    goToProjectUpdateLists();
                }

            } catch (JSONException e) {
                e.printStackTrace();
                conn = false;
                goToProjectUpdateLists();
            }
        }, error -> {
            conn = false;
            goToProjectUpdateLists();
        });

        requestQueue.add(stringRequest);
    }

    public void getLocationsForUpdate() {
        String url = "http://103.56.208.123:8086/terrain/tr_kabikha/all_locations/project_locations";

        RequestQueue requestQueue = Volley.newRequestQueue(HomePage.this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");

                if (!count.equals("0")) {
                    JSONArray jsonArray = new JSONArray(items);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject locationObject = jsonArray.getJSONObject(i);
                        String pcmgd_latitude = locationObject.getString("pcmgd_latitude");
                        String pcmgd_longitude = locationObject.getString("pcmgd_longitude");
                        int segment = locationObject.getInt("segment");
                        String pcmgd_pcm_id = locationObject.getString("pcmgd_pcm_id");

                        for (int j = 0; j < projectUpdateLists.size(); j++) {
                            if (pcmgd_pcm_id.equals(projectUpdateLists.get(j).getPcmId())) {
                                ArrayList<LocationLists> locationLists = projectUpdateLists.get(j).getLocationLists();
                                locationLists.add(new LocationLists(pcmgd_latitude,pcmgd_longitude,segment));
                                projectUpdateLists.get(j).setLocationLists(locationLists);
                            }
                        }
                    }
                }
                conn = true;
                goToProjectUpdateLists();

            } catch (JSONException e) {
                e.printStackTrace();
                conn = false;
                goToProjectUpdateLists();
            }
        }, error -> {
            conn = false;
            goToProjectUpdateLists();
        });

        requestQueue.add(stringRequest);
    }

    public void goToProjectUpdateLists() {
        waitProgress.dismiss();
        if (conn) {

            if (projectUpdateLists.size() != 0) {
                System.out.println(projectUpdateLists.size());
                Intent intent = new Intent(HomePage.this, ProjectUpdate.class);
                startActivity(intent);
            }
            else {
                Toast.makeText(getApplicationContext(),"No Project Found",Toast.LENGTH_SHORT).show();
            }

            conn = false;

        }
        else {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
            AlertDialog dialog = new AlertDialog.Builder(HomePage.this)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .show();

//                dialog.setCancelable(false);
//                dialog.setCanceledOnTouchOutside(false);
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    getProjectUpdateData();
                    dialog.dismiss();
                }
            });
        }
    }

    //    --------------------------Transforming Bangla Text-----------------------------
    private String transformText(String text) {
        byte[] bytes = text.getBytes(ISO_8859_1);
        return new String(bytes, UTF_8);
    }
}