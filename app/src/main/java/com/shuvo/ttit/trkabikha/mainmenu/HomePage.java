package com.shuvo.ttit.trkabikha.mainmenu;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
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
import com.shuvo.ttit.trkabikha.projectUpdate.ProjectUpdate;
import com.shuvo.ttit.trkabikha.projects.Projects;
import com.shuvo.ttit.trkabikha.projectsWithMap.ProjectsMaps;


import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import static com.shuvo.ttit.trkabikha.connection.OracleConnection.createConnection;
import static com.shuvo.ttit.trkabikha.login.Login.userInfoLists;

public class HomePage extends AppCompatActivity {

    AmazingSpinner financialYearStart;
    AmazingSpinner financialYearEnd;
    LinearLayout afterYearSelection;
    AmazingSpinner division;

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

    WaitProgress waitProgress = new WaitProgress();
    private String message = null;
    private Boolean conn = false;
    private Boolean connected = false;

    private Connection connection;

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

    ImageView logOut;


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

        logOut = findViewById(R.id.log_out_icon_main_menu);

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

        if (userType.equals("GUEST")) {
            userName.setText(userType);
            searchMap.setVisibility(View.GONE);
            searchUpdateProject.setVisibility(View.GONE);
            logOut.setVisibility(View.GONE);
        } else if (userType.equals("ADMIN")) {
            String n = userInfoLists.get(0).getUserName();
            userName.setText(n);
            searchMap.setVisibility(View.VISIBLE);
            searchUpdateProject.setVisibility(View.VISIBLE);
            logOut.setVisibility(View.VISIBLE);
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
                    new DistrictCheck().execute();
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

                new UpazilaCheck().execute();
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

                new UnionCheck().execute();

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
                    new ProjectSubTypeCheck().execute();
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

                    new ProjectDataCheck().execute();
                }
            }
        });

        searchMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!fys_id.isEmpty() && !fye_id.isEmpty()) {
                    new ProjectMapDataCheck().execute();
                }
            }
        });

        searchUpdateProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!fys_id.isEmpty() && !fye_id.isEmpty()) {

                    new ProjectUpdateDataCheck().execute();
                }
            }
        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userType.equals("GUEST")) {
                    finish();
                } else if (userType.equals("ADMIN")) {
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
                }
            }
        });

        new Check().execute();

    }

    @Override
    public void onBackPressed() {
        if (userType.equals("GUEST")) {
            super.onBackPressed();
        } else if (userType.equals("ADMIN")) {
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
        }
    }

    public boolean isConnected () {
        boolean connected = false;
        boolean isMobile = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        } catch (Exception e) {
            Log.e("Connectivity Exception", e.getMessage());
        }
        return connected;
    }

    public boolean isOnline () {

        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return false;
    }

    public class Check extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            waitProgress.show(getSupportFragmentManager(), "WaitBar");
            waitProgress.setCancelable(false);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (isConnected() && isOnline()) {

                Query();
                if (connected) {
                    conn = true;
                    message = "Internet Connected";
                }

            } else {
                conn = false;
                message = "Not Connected";
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

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
                
                
                conn = false;
                connected = false;



            } else {
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

                        new Check().execute();
                        dialog.dismiss();
                    }
                });

                Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                negative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        if (userType.equals("GUEST")) {
                            finish();
                        } else if (userType.equals("ADMIN")) {
                            userInfoLists.clear();
                            userInfoLists = new ArrayList<>();
                            finish();
                        }
                    }
                });
            }
        }
    }

    public class DistrictCheck extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            waitProgress.show(getSupportFragmentManager(), "WaitBar");
            waitProgress.setCancelable(false);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (isConnected() && isOnline()) {

                DistrictQuery();
                if (connected) {
                    conn = true;
                    message = "Internet Connected";
                }

            } else {
                conn = false;
                message = "Not Connected";
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

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
                connected = false;



            } else {
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

                        new DistrictCheck().execute();
                        dialog.dismiss();
                    }
                });
            }
        }
    }

    public class UpazilaCheck extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            waitProgress.show(getSupportFragmentManager(), "WaitBar");
            waitProgress.setCancelable(false);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (isConnected() && isOnline()) {

                UpazilaQuery();
                if (connected) {
                    conn = true;
                    message = "Internet Connected";
                }

            } else {
                conn = false;
                message = "Not Connected";
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

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
                connected = false;



            } else {
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

                        new UpazilaCheck().execute();
                        dialog.dismiss();
                    }
                });
            }
        }
    }

    public class UnionCheck extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            waitProgress.show(getSupportFragmentManager(), "WaitBar");
            waitProgress.setCancelable(false);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (isConnected() && isOnline()) {

                UnionQuery();
                if (connected) {
                    conn = true;
                    message = "Internet Connected";
                }

            } else {
                conn = false;
                message = "Not Connected";
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

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
                connected = false;

            } else {
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

                        new UnionCheck().execute();
                        dialog.dismiss();
                    }
                });
            }
        }
    }

    public class ProjectSubTypeCheck extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            waitProgress.show(getSupportFragmentManager(), "WaitBar");
            waitProgress.setCancelable(false);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (isConnected() && isOnline()) {

                ProjectSubTypeQuery();
                if (connected) {
                    conn = true;
                    message = "Internet Connected";
                }

            } else {
                conn = false;
                message = "Not Connected";
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

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
                connected = false;



            } else {
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

                        new ProjectSubTypeCheck().execute();
                        dialog.dismiss();
                    }
                });
            }
        }
    }

    public class ProjectDataCheck extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            waitProgress.show(getSupportFragmentManager(), "WaitBar");
            waitProgress.setCancelable(false);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (isConnected() && isOnline()) {

                ProjectDataQuery();
                if (connected) {
                    conn = true;
                    message = "Internet Connected";
                }

            } else {
                conn = false;
                message = "Not Connected";
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            waitProgress.dismiss();
            if (conn) {



                if (projectlists.size() != 0) {
                    System.out.println(projectlists.size());
                    Intent intent = new Intent(HomePage.this, Projects.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getApplicationContext(),"No Results Found",Toast.LENGTH_SHORT).show();
                }



                conn = false;
                connected = false;



            } else {
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

                        new ProjectDataCheck().execute();
                        dialog.dismiss();
                    }
                });
            }
        }
    }

    public class ProjectMapDataCheck extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            waitProgress.show(getSupportFragmentManager(), "WaitBar");
            waitProgress.setCancelable(false);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (isConnected() && isOnline()) {

                ProjectMapDataQuery();
                if (connected) {
                    conn = true;
                    message = "Internet Connected";
                }

            } else {
                conn = false;
                message = "Not Connected";
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            waitProgress.dismiss();
            if (conn) {



                if (projectMapsLists.size() != 0) {
                    System.out.println(projectMapsLists.size());
                    Intent intent = new Intent(HomePage.this, ProjectsMaps.class);
                    intent.putExtra("DIST_ID",dist_id);
                    intent.putExtra("DD_ID",dd_id);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getApplicationContext(),"No Results Found",Toast.LENGTH_SHORT).show();
                }



                conn = false;
                connected = false;



            } else {
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

                        new ProjectMapDataCheck().execute();
                        dialog.dismiss();
                    }
                });
            }
        }
    }

    public class ProjectUpdateDataCheck extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            waitProgress.show(getSupportFragmentManager(), "WaitBar");
            waitProgress.setCancelable(false);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (isConnected() && isOnline()) {

                ProjectUpdateDataQuery();
                if (connected) {
                    conn = true;
                    message = "Internet Connected";
                }

            } else {
                conn = false;
                message = "Not Connected";
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            waitProgress.dismiss();
            if (conn) {

                if (projectUpdateLists.size() != 0) {
                    System.out.println(projectUpdateLists.size());
                    Intent intent = new Intent(HomePage.this, ProjectUpdate.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getApplicationContext(),"No Results Found",Toast.LENGTH_SHORT).show();
                }

                conn = false;
                connected = false;

            } else {
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

                        new ProjectDataCheck().execute();
                        dialog.dismiss();
                    }
                });
            }
        }
    }

    public void Query () {

        try {
            this.connection = createConnection();

            fysLists = new ArrayList<>();
            fyeLists = new ArrayList<>();
            divisionLists = new ArrayList<>();
            sourceFundLists = new ArrayList<>();
            projectTypeLists = new ArrayList<>();

            Statement stmt = connection.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT FINANCIAL_YEAR.FY_ID P_FY_ID, FINANCIAL_YEAR.FY_FINANCIAL_YEAR_NAME, FINANCIAL_YEAR.FY_FROM_YEAR, FINANCIAL_YEAR.FY_TO_YEAR, FINANCIAL_YEAR.FY_DETAILS, FINANCIAL_YEAR.FY_ACTIVE_FLAG " +
                    "FROM FINANCIAL_YEAR " +
                    "WHERE FINANCIAL_YEAR.FY_ACTIVE_FLAG = 1 " +
                    "ORDER BY FINANCIAL_YEAR.FY_FROM_YEAR ASC");


            while (rs.next()) {
                fysLists.add(new FinancialYearLists(rs.getString(1),rs.getString(2),rs.getString(3),
                        rs.getString(4),rs.getString(5),rs.getString(6)));
                fyeLists.add(new FinancialYearLists(rs.getString(1),rs.getString(2),rs.getString(3),
                        rs.getString(4),rs.getString(5),rs.getString(6)));
            }

            rs.close();

            ResultSet rs1 = stmt.executeQuery("SELECT DIVISION.DIV_ID P_DIV_ID, DIVISION.DIV_NAME FROM DIVISION WHERE DIV_ACTIVE_FLAG=1 ORDER BY DIV_NAME");

            divisionLists.add(new DivisionLists("","..."));
            while (rs1.next()) {
                divisionLists.add(new DivisionLists(rs1.getString(1),rs1.getString(2)));
            }

            rs1.close();

            ResultSet rs2 = stmt.executeQuery("SELECT FSM_ID P_FSM_ID, FSM_FUND_NAME FROM FUND_SOURCE_MST WHERE FSM_FUND_SOURCE_ACTIVE_FLAG = 1");

            sourceFundLists.add(new SourceFundLists("","..."));
            while (rs2.next()) {
                sourceFundLists.add(new SourceFundLists(rs2.getString(1),rs2.getString(2)));
            }

            rs2.close();

            ResultSet rs3 = stmt.executeQuery("SELECT PTM_ID P_PTM_ID, ptm_project_type_name FROM PROJECT_TYPE_MST");

            projectTypeLists.add(new ProjectTypeLists("","..."));
            while (rs3.next()) {
                projectTypeLists.add(new ProjectTypeLists(rs3.getString(1),rs3.getString(2)));
            }

            rs3.close();

            stmt.close();

            connected = true;

            connection.close();

        } catch (Exception e) {

            //   Toast.makeText(MainActivity.this, ""+e,Toast.LENGTH_LONG).show();
            Log.i("ERRRRR", e.getLocalizedMessage());
            e.printStackTrace();
        }

    }

    public void DistrictQuery () {

        try {
            this.connection = createConnection();

            districtLists = new ArrayList<>();

            Statement stmt = connection.createStatement();

            if (div_id != null) {
                if (div_id.isEmpty()) {
                    div_id = null;
                }
            }

            ResultSet rs = stmt.executeQuery("SELECT DIST_ID P_DIST_ID, DIST_NAME FROM DISTRICT WHERE DIST_ACTIVE_FLAG=1 AND DIST_DIV_ID= "+div_id+" ORDER BY DIST_NAME");

            while (rs.next()) {
                districtLists.add(new DistrictLists(rs.getString(1),rs.getString(2)));
            }

            if (div_id == null) {
                div_id = "";
            }

            rs.close();

            stmt.close();

            connected = true;

            connection.close();

        } catch (Exception e) {

            //   Toast.makeText(MainActivity.this, ""+e,Toast.LENGTH_LONG).show();
            Log.i("ERRRRR", e.getLocalizedMessage());
            e.printStackTrace();
        }

    }

    public void UpazilaQuery () {

        try {
            this.connection = createConnection();

            upazilaLists = new ArrayList<>();

            Statement stmt = connection.createStatement();

            if (dist_id != null) {
                if (dist_id.isEmpty()) {
                    dist_id = null;
                }
            }

            ResultSet rs = stmt.executeQuery("SELECT DISTRICT_DTL.DD_ID PCU_DD_ID, DISTRICT_DTL.DD_THANA_NAME THANA_UPOZILLA \n" +
                    "FROM DISTRICT_DTL \n" +
                    "WHERE DD_DIST_ID = "+dist_id+" \n" +
                    "AND NVL(DD_ACTIVE_FLAG,0)=1 \n" +
                    "ORDER BY DISTRICT_DTL.DD_THANA_NAME ASC");

            while (rs.next()) {
                upazilaLists.add(new UpazilaLists(rs.getString(1),rs.getString(2)));
            }

            if (dist_id == null) {
                dist_id = "";
            }

            rs.close();

            stmt.close();

            connected = true;

            connection.close();

        } catch (Exception e) {

            //   Toast.makeText(MainActivity.this, ""+e,Toast.LENGTH_LONG).show();
            Log.i("ERRRRR", e.getLocalizedMessage());
            e.printStackTrace();
        }

    }

    public void UnionQuery () {

        try {
            this.connection = createConnection();

            unionLists = new ArrayList<>();

            Statement stmt = connection.createStatement();

            if (dd_id != null) {
                if (dd_id.isEmpty()) {
                    dd_id = null;
                }
            }

            ResultSet rs = stmt.executeQuery("SELECT DISTRICT_DTL_UNION.DDU_ID PCUN_DDU_ID, DISTRICT_DTL_UNION.DDU_UNION_NAME \n" +
                    "FROM DISTRICT_DTL_UNION \n" +
                    "WHERE DISTRICT_DTL_UNION.DDU_DD_ID = "+dd_id+"\n" +
                    "ORDER BY DISTRICT_DTL_UNION.DDU_UNION_NAME ASC");

            while (rs.next()) {
                unionLists.add(new UnionLists(rs.getString(1),rs.getString(2)));
            }

            if (dd_id == null) {
                dd_id = "";
            }

            rs.close();

            stmt.close();

            connected = true;

            connection.close();

        } catch (Exception e) {

            //   Toast.makeText(MainActivity.this, ""+e,Toast.LENGTH_LONG).show();
            Log.i("ERRRRR", e.getLocalizedMessage());
            e.printStackTrace();
        }

    }

    public void ProjectSubTypeQuery () {

        try {
            this.connection = createConnection();

            projectSubTypeLists = new ArrayList<>();

            Statement stmt = connection.createStatement();

            if (ptm_id != null) {
                if (ptm_id.isEmpty()) {
                    ptm_id = null;
                }
            }

            ResultSet rs = stmt.executeQuery("SELECT PTD_ID P_PTD_ID, ptd_project_subtype_name FROM PROJECT_TYPE_DTL WHERE PTD_PTM_ID = "+ptm_id+" ORDER BY P_PTD_ID");

            while (rs.next()) {
                projectSubTypeLists.add(new ProjectSubTypeLists(rs.getString(1),rs.getString(2)));
            }

            if (ptm_id == null) {
                ptm_id = "";
            }

            rs.close();

            stmt.close();

            connected = true;

            connection.close();

        } catch (Exception e) {

            //   Toast.makeText(MainActivity.this, ""+e,Toast.LENGTH_LONG).show();
            Log.i("ERRRRR", e.getLocalizedMessage());
            e.printStackTrace();
        }

    }

    public void ProjectDataQuery() {
        try {
            this.connection = createConnection();

            projectlists = new ArrayList<>();

            Statement stmt = connection.createStatement();

            if (ptm_id != null) {
                if (ptm_id.isEmpty()) {
                    ptm_id = null;
                }
            }
            if (fsm_id != null) {
                if (fsm_id.isEmpty()) {
                    fsm_id = null;
                }
            }

            if (ddu_id != null) {
                if (ddu_id.isEmpty()) {
                    ddu_id = null;
                }
            }

            if (dist_id != null) {
                if (dist_id.isEmpty()) {
                    dist_id = null;
                }
            }

            if (dd_id != null) {
                if (dd_id.isEmpty()) {
                    dd_id = null;
                }
            }
            if (ptd_Id != null) {
                if (ptd_Id.isEmpty()) {
                    ptd_Id = null;
                }
            }

            if (div_id != null) {
                if (div_id.isEmpty()) {
                    div_id = null;
                }
            }

            int count = 0;
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM (\n" +
                    "            SELECT p.*, ROW_NUMBER() OVER (ORDER BY p.PCM_PROJECT_DATE DESC, p.PCM_ID DESC) as ROWNUMBER_ FROM ( \n" +
                    "        SELECT DISTINCT PROJECT_CREATION_MST.PCM_ID,\n" +
                    "        TO_CHAR(PROJECT_CREATION_MST.PCM_ENTRY_DATE,'DD-MON-RR') ENTRY_DATE,\n" +
                    "        PROJECT_CREATION_MST.PCM_INTERNAL_NO,\n" +
                    "        PROJECT_CREATION_MST.PCM_PROJECT_CODE,\n" +
                    "        PROJECT_CREATION_MST.PCM_USER,\n" +
                    "        PROJECT_CREATION_MST.PCM_PROJECT_NAME,\n" +
                    "        PROJECT_CREATION_MST.PCM_PROJECT_NO,\n" +
                    "        PROJECT_CREATION_MST.PCM_PROJECT_DATE,\n" +
                    "        PROJECT_CREATION_MST.PCM_PIC_CHAIRMAN_NAME,\n" +
                    "        PROJECT_CREATION_MST.PCM_PIC_CHAIRMAN_DETAILS,\n" +
                    "        PROJECT_CREATION_MST.PCM_ESTIMATE_PROJECT_VALUE,\n" +
                    "        FINANCIAL_YEAR.FY_FINANCIAL_YEAR_NAME,\n" +
                    "        FUND_SOURCE_MST.FSM_FUND_NAME,\n" +
                    "        PROJECT_TYPE_MST.PTM_PROJECT_TYPE_NAME,\n" +
                    "        PROJECT_TYPE_DTL.PTD_PROJECT_SUBTYPE_NAME,\n" +
                    "        PROJECT_SANCTION_CATEGORY.PSC_SANCTION_CAT_NAME,\n" +
                    "        PROJECT_CATEGORY_MST.PCM_CATEGORY_NAME,\n" +
                    "        PROJECT_CREATION_UNION.PCUN_DDU_ID,\n" +
                    "        PROJECT_CREATION_UPOZILA.PCU_DD_ID,\n" +
                    "        PROJECT_CREATION_MST.PCM_PROJ_EVALUATION_REMARKS,\n" +
                    "        PROJECT_CREATION_MST_GPS_DTL.PCMGD_TYPE_FLAG,PROJECT_CREATION_MST.PCM_PROJECT_DETAILS,\n" +
                    "        TO_CHAR(PROJECT_CREATION_MST.PCM_ESTIMATE_START_DATE,'DD-MON-RR') START_DATE,\n" +
                    "        TO_CHAR(PROJECT_CREATION_MST.PCM_ESTIMATE_END_DATE,'DD-MON-RR') END_DATE,PROJECT_CREATION_MST.PCM_PROJECT_SANCTION_TYPE\n" +
                    "    FROM\n" +
                    "        PROJECT_CREATION_MST,\n" +
                    "        PROJECT_CREATION_UPOZILA,\n" +
                    "        PROJECT_CREATION_UNION,\n" +
                    "        PROJECT_CREATION_VILLAGE,\n" +
                    "        PROJECT_CREATION_WARD,\n" +
                    "        FINANCIAL_YEAR,\n" +
                    "        FUND_SOURCE_MST,\n" +
                    "        PROJECT_TYPE_MST,\n" +
                    "        PROJECT_TYPE_DTL,\n" +
                    "        PROJECT_SANCTION_CATEGORY,\n" +
                    "        PROJECT_CATEGORY_MST,\n" +
                    "        PROJECT_CREATION_MST_GPS_DTL\n" +
                    "    WHERE FINANCIAL_YEAR.FY_ID = PROJECT_CREATION_MST.PCM_FY_ID\n" +
                    "        AND FUND_SOURCE_MST.FSM_ID = PROJECT_CREATION_MST.PCM_FSM_ID\n" +
                    "        AND PROJECT_TYPE_MST.PTM_ID = PROJECT_CREATION_MST.PCM_PTM_ID\n" +
                    "        AND PROJECT_TYPE_DTL.PTD_ID = PROJECT_CREATION_MST.PCM_PTD_ID\n" +
                    "        AND PROJECT_CREATION_MST.PCM_PSC_ID = PROJECT_SANCTION_CATEGORY.PSC_ID\n" +
                    "        AND PROJECT_CREATION_MST.PCM_PCM_ID = PROJECT_CATEGORY_MST.PCM_ID\n" +
                    "        AND PROJECT_CREATION_MST.PCM_ID = PROJECT_CREATION_UPOZILA.PCU_PCM_ID\n" +
                    "        AND PROJECT_CREATION_MST.PCM_ID = PROJECT_CREATION_MST_GPS_DTL.PCMGD_PCM_ID\n" +
                    "        AND PROJECT_CREATION_UNION.PCUN_PCU_ID = PROJECT_CREATION_UPOZILA.PCU_ID\n" +
                    "        AND PROJECT_CREATION_UNION.PCUN_ID = PROJECT_CREATION_WARD.PCW_PCUN_ID (+)\n" +
                    "        AND PROJECT_CREATION_WARD.PCW_ID = PROJECT_CREATION_VILLAGE.PCV_PCW_ID (+)\n" +
                    "        AND PROJECT_CREATION_MST.PCM_PROJ_EVALUATION_FLAG = 1 \n" +
                    "        AND (PROJECT_CREATION_MST.PCM_PTD_ID = "+ptd_Id+" OR "+ptd_Id+" IS NULL )\n" +
                    "        AND (PROJECT_CREATION_MST.PCM_PTM_ID = "+ptm_id+" OR "+ptm_id+" IS NULL )\n" +
                    "        AND (PROJECT_CREATION_MST.PCM_FSM_ID = "+fsm_id+" OR "+fsm_id+" IS NULL )\n" +
                    "        AND (PROJECT_CREATION_UNION.PCUN_DDU_ID = "+ddu_id+" OR "+ddu_id+" IS NULL )\n" +
                    "        AND (PROJECT_CREATION_UPOZILA.PCU_DD_ID = "+dd_id+" OR "+dd_id+" IS NULL )\n" +
                    "        AND (PROJECT_CREATION_UPOZILA.PCU_DIST_ID = "+dist_id+" OR "+dist_id+" IS NULL )\n" +
                    "        AND (PROJECT_CREATION_UPOZILA.PCU_DIV_ID = "+div_id+" OR "+div_id+" IS NULL )\n" +
                    "        AND PROJECT_CREATION_MST.PCM_FY_ID BETWEEN "+fys_id+" AND "+fye_id+" ) p\n" +
                    "    ORDER BY p.PCM_PROJECT_DATE DESC, p.PCM_ID DESC )\n");

            while (resultSet.next()) {
                count++;
                String stype = "";
                switch (resultSet.getString(25)) {
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

                String pCount = "#"+count;

                projectlists.add(new Projectlists(resultSet.getString(1),resultSet.getString(2),resultSet.getString(3),
                        resultSet.getString(4),resultSet.getString(5),resultSet.getString(6),
                        resultSet.getString(7),resultSet.getString(8),resultSet.getString(9),
                        resultSet.getString(10),resultSet.getString(11),resultSet.getString(12),
                        resultSet.getString(13),resultSet.getString(14),resultSet.getString(15),
                        resultSet.getString(16),resultSet.getString(17),resultSet.getString(18),
                        resultSet.getString(19),resultSet.getString(20),resultSet.getString(21),
                        resultSet.getString(22),resultSet.getString(23),resultSet.getString(24),
                        stype,resultSet.getString(26),pCount,new ArrayList<>()));

//                projectlists.add(new Projectlists(resultSet.getString(1),resultSet.getString(2),resultSet.getString(3),
//                        resultSet.getString(4),resultSet.getString(5),resultSet.getString(6),
//                        resultSet.getString(7),resultSet.getString(8),resultSet.getString(9),
//                        resultSet.getString(10),resultSet.getString(11),resultSet.getString(12),
//                        resultSet.getString(13),resultSet.getString(14),resultSet.getString(15),
//                        resultSet.getString(16),resultSet.getString(17),null,
//                        resultSet.getString(18),resultSet.getString(19),resultSet.getString(20),
//                        resultSet.getString(21), resultSet.getString(22),resultSet.getString(23),
//                        stype,resultSet.getString(25), new ArrayList<>()));
            }

            resultSet.close();

            for (int i = 0 ; i < projectlists.size(); i++) {
                String pcmid = projectlists.get(i).getPcmId();
                ArrayList<LocationLists> locationLists = new ArrayList<>();
                ResultSet resultSet1 = stmt.executeQuery("SELECT \n" +
                        "PCMGD_LATITUDE,\n" +
                        "PCMGD_LONGITUDE,\n" +
                        "PCMGD_LATITUDE_NUM,\n" +
                        "PCMGD_LONGITUDE_NUM,\n" +
                        "NVL(PCMGD_SEGMENT,0)\n"+
                        "FROM project_creation_mst_gps_dtl WHERE PCMGD_PCM_ID = "+pcmid+" AND PCMGD_ACTIVE_FLAG=1 order by pcmgd_id ASC");

                while (resultSet1.next()) {
                    locationLists.add(new LocationLists(resultSet1.getString(1),resultSet1.getString(2),resultSet1.getInt(5)));
                }

                resultSet1.close();

                projectlists.get(i).setLocationLists(locationLists);

            }

            stmt.close();

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

            connected = true;

            connection.close();


        } catch (Exception e) {
            Log.i("ERRRRR", e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    public void ProjectMapDataQuery() {
        try {
            this.connection = createConnection();

            projectMapsLists = new ArrayList<>();

            Statement stmt = connection.createStatement();

            if (ptm_id != null) {
                if (ptm_id.isEmpty()) {
                    ptm_id = null;
                }
            }
            if (fsm_id != null) {
                if (fsm_id.isEmpty()) {
                    fsm_id = null;
                }
            }

            if (ddu_id != null) {
                if (ddu_id.isEmpty()) {
                    ddu_id = null;
                }
            }

            if (dist_id != null) {
                if (dist_id.isEmpty()) {
                    dist_id = null;
                }
            }

            if (dd_id != null) {
                if (dd_id.isEmpty()) {
                    dd_id = null;
                }
            }
            if (ptd_Id != null) {
                if (ptd_Id.isEmpty()) {
                    ptd_Id = null;
                }
            }

            if (div_id != null) {
                if (div_id.isEmpty()) {
                    div_id = null;
                }
            }

            int count = 0;
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM (\n" +
                    "            SELECT p.*, ROW_NUMBER() OVER (ORDER BY p.PCM_PROJECT_DATE DESC, p.PCM_ID DESC) as ROWNUMBER_ FROM ( \n" +
                    "        SELECT DISTINCT PROJECT_CREATION_MST.PCM_ID,\n" +
                    "        TO_CHAR(PROJECT_CREATION_MST.PCM_ENTRY_DATE,'DD-MON-RR') ENTRY_DATE,\n" +
                    "        PROJECT_CREATION_MST.PCM_INTERNAL_NO,\n" +
                    "        PROJECT_CREATION_MST.PCM_PROJECT_CODE,\n" +
                    "        PROJECT_CREATION_MST.PCM_USER,\n" +
                    "        PROJECT_CREATION_MST.PCM_PROJECT_NAME,\n" +
                    "        PROJECT_CREATION_MST.PCM_PROJECT_NO,\n" +
                    "        PROJECT_CREATION_MST.PCM_PROJECT_DATE,\n" +
                    "        PROJECT_CREATION_MST.PCM_PIC_CHAIRMAN_NAME,\n" +
                    "        PROJECT_CREATION_MST.PCM_PIC_CHAIRMAN_DETAILS,\n" +
                    "        PROJECT_CREATION_MST.PCM_ESTIMATE_PROJECT_VALUE,\n" +
                    "        FINANCIAL_YEAR.FY_FINANCIAL_YEAR_NAME,\n" +
                    "        FUND_SOURCE_MST.FSM_FUND_NAME,\n" +
                    "        PROJECT_TYPE_MST.PTM_PROJECT_TYPE_NAME,\n" +
                    "        PROJECT_TYPE_DTL.PTD_PROJECT_SUBTYPE_NAME,\n" +
                    "        PROJECT_SANCTION_CATEGORY.PSC_SANCTION_CAT_NAME,\n" +
                    "        PROJECT_CATEGORY_MST.PCM_CATEGORY_NAME,\n" +
                    "        PROJECT_CREATION_UNION.PCUN_DDU_ID,\n" +
                    "        PROJECT_CREATION_UPOZILA.PCU_DD_ID,\n" +
                    "        PROJECT_CREATION_MST.PCM_PROJ_EVALUATION_REMARKS,\n" +
                    "        PROJECT_CREATION_MST_GPS_DTL.PCMGD_TYPE_FLAG,PROJECT_CREATION_MST.PCM_PROJECT_DETAILS,\n" +
                    "        TO_CHAR(PROJECT_CREATION_MST.PCM_ESTIMATE_START_DATE,'DD-MON-RR') START_DATE,\n" +
                    "        TO_CHAR(PROJECT_CREATION_MST.PCM_ESTIMATE_END_DATE,'DD-MON-RR') END_DATE,PROJECT_CREATION_MST.PCM_PROJECT_SANCTION_TYPE\n" +
                    "    FROM\n" +
                    "        PROJECT_CREATION_MST,\n" +
                    "        PROJECT_CREATION_UPOZILA,\n" +
                    "        PROJECT_CREATION_UNION,\n" +
                    "        PROJECT_CREATION_VILLAGE,\n" +
                    "        PROJECT_CREATION_WARD,\n" +
                    "        FINANCIAL_YEAR,\n" +
                    "        FUND_SOURCE_MST,\n" +
                    "        PROJECT_TYPE_MST,\n" +
                    "        PROJECT_TYPE_DTL,\n" +
                    "        PROJECT_SANCTION_CATEGORY,\n" +
                    "        PROJECT_CATEGORY_MST,\n" +
                    "        PROJECT_CREATION_MST_GPS_DTL\n" +
                    "    WHERE FINANCIAL_YEAR.FY_ID = PROJECT_CREATION_MST.PCM_FY_ID\n" +
                    "        AND FUND_SOURCE_MST.FSM_ID = PROJECT_CREATION_MST.PCM_FSM_ID\n" +
                    "        AND PROJECT_TYPE_MST.PTM_ID = PROJECT_CREATION_MST.PCM_PTM_ID\n" +
                    "        AND PROJECT_TYPE_DTL.PTD_ID = PROJECT_CREATION_MST.PCM_PTD_ID\n" +
                    "        AND PROJECT_CREATION_MST.PCM_PSC_ID = PROJECT_SANCTION_CATEGORY.PSC_ID\n" +
                    "        AND PROJECT_CREATION_MST.PCM_PCM_ID = PROJECT_CATEGORY_MST.PCM_ID\n" +
                    "        AND PROJECT_CREATION_MST.PCM_ID = PROJECT_CREATION_UPOZILA.PCU_PCM_ID\n" +
                    "        AND PROJECT_CREATION_MST.PCM_ID = PROJECT_CREATION_MST_GPS_DTL.PCMGD_PCM_ID\n" +
                    "        AND PROJECT_CREATION_UNION.PCUN_PCU_ID = PROJECT_CREATION_UPOZILA.PCU_ID\n" +
                    "        AND PROJECT_CREATION_UNION.PCUN_ID = PROJECT_CREATION_WARD.PCW_PCUN_ID (+)\n" +
                    "        AND PROJECT_CREATION_WARD.PCW_ID = PROJECT_CREATION_VILLAGE.PCV_PCW_ID (+)\n" +
                    "        AND PROJECT_CREATION_MST.PCM_PROJ_EVALUATION_FLAG = 1 \n" +
                    "        AND (PROJECT_CREATION_MST.PCM_PTD_ID = "+ptd_Id+" OR "+ptd_Id+" IS NULL )\n" +
                    "        AND (PROJECT_CREATION_MST.PCM_PTM_ID = "+ptm_id+" OR "+ptm_id+" IS NULL )\n" +
                    "        AND (PROJECT_CREATION_MST.PCM_FSM_ID = "+fsm_id+" OR "+fsm_id+" IS NULL )\n" +
                    "        AND (PROJECT_CREATION_UNION.PCUN_DDU_ID = "+ddu_id+" OR "+ddu_id+" IS NULL )\n" +
                    "        AND (PROJECT_CREATION_UPOZILA.PCU_DD_ID = "+dd_id+" OR "+dd_id+" IS NULL )\n" +
                    "        AND (PROJECT_CREATION_UPOZILA.PCU_DIST_ID = "+dist_id+" OR "+dist_id+" IS NULL )\n" +
                    "        AND (PROJECT_CREATION_UPOZILA.PCU_DIV_ID = "+div_id+" OR "+div_id+" IS NULL )\n" +
                    "        AND PROJECT_CREATION_MST.PCM_FY_ID BETWEEN "+fys_id+" AND "+fye_id+" ) p\n" +
                    "    ORDER BY p.PCM_PROJECT_DATE DESC, p.PCM_ID DESC )\n");

            while (resultSet.next()) {

                count++;
                String stype = "";
                switch (resultSet.getString(25)) {
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

                String pCount = "#"+count;

                projectMapsLists.add(new ProjectMapsLists(resultSet.getString(1),resultSet.getString(2),resultSet.getString(3),
                        resultSet.getString(4),resultSet.getString(5),resultSet.getString(6),
                        resultSet.getString(7),resultSet.getString(8),resultSet.getString(9),
                        resultSet.getString(10),resultSet.getString(11),resultSet.getString(12),
                        resultSet.getString(13),resultSet.getString(14),resultSet.getString(15),
                        resultSet.getString(16),resultSet.getString(17),resultSet.getString(18),
                        resultSet.getString(19),resultSet.getString(20),resultSet.getString(21),
                        resultSet.getString(22), resultSet.getString(23),resultSet.getString(24),
                        stype,resultSet.getString(26),pCount, new ArrayList<>()));

//                projectMapsLists.add(new ProjectMapsLists(resultSet.getString(1),resultSet.getString(2),resultSet.getString(3),
//                        resultSet.getString(4),resultSet.getString(5),resultSet.getString(6),
//                        resultSet.getString(7),resultSet.getString(8),resultSet.getString(9),
//                        resultSet.getString(10),resultSet.getString(11),resultSet.getString(12),
//                        resultSet.getString(13),resultSet.getString(14),resultSet.getString(15),
//                        resultSet.getString(16),resultSet.getString(17),null,
//                        resultSet.getString(18),resultSet.getString(19),resultSet.getString(20),
//                        resultSet.getString(21), resultSet.getString(22),resultSet.getString(23),
//                        stype,resultSet.getString(25), new ArrayList<>()));
            }

            resultSet.close();

            for (int i = 0 ; i < projectMapsLists.size(); i++) {
                String pcmid = projectMapsLists.get(i).getPcmId();
                ArrayList<LocationLists> locationLists = new ArrayList<>();
                ResultSet resultSet1 = stmt.executeQuery("SELECT \n" +
                        "PCMGD_LATITUDE,\n" +
                        "PCMGD_LONGITUDE,\n" +
                        "PCMGD_LATITUDE_NUM,\n" +
                        "PCMGD_LONGITUDE_NUM,\n" +
                        "NVL(PCMGD_SEGMENT,0)\n"+
                        "FROM project_creation_mst_gps_dtl WHERE PCMGD_PCM_ID = "+pcmid+" AND PCMGD_ACTIVE_FLAG=1 order by pcmgd_id ASC");

                while (resultSet1.next()) {
                    locationLists.add(new LocationLists(resultSet1.getString(1),resultSet1.getString(2),resultSet1.getInt(5)));
                }

                resultSet1.close();

                projectMapsLists.get(i).setLocationLists(locationLists);

            }

            stmt.close();

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

            connected = true;

            connection.close();


        } catch (Exception e) {
            Log.i("ERRRRR", e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    public void ProjectUpdateDataQuery() {
        try {
            this.connection = createConnection();

            projectUpdateLists = new ArrayList<>();

            Statement stmt = connection.createStatement();

            if (ptm_id != null) {
                if (ptm_id.isEmpty()) {
                    ptm_id = null;
                }
            }
            if (fsm_id != null) {
                if (fsm_id.isEmpty()) {
                    fsm_id = null;
                }
            }

            if (ddu_id != null) {
                if (ddu_id.isEmpty()) {
                    ddu_id = null;
                }
            }

            if (dist_id != null) {
                if (dist_id.isEmpty()) {
                    dist_id = null;
                }
            }

            if (dd_id != null) {
                if (dd_id.isEmpty()) {
                    dd_id = null;
                }
            }
            if (ptd_Id != null) {
                if (ptd_Id.isEmpty()) {
                    ptd_Id = null;
                }
            }

            if (div_id != null) {
                if (div_id.isEmpty()) {
                    div_id = null;
                }
            }

            int count = 0;
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM (\n" +
                    "                                SELECT p.*, ROW_NUMBER() OVER (ORDER BY p.PCM_PROJECT_DATE DESC, p.PCM_ID DESC) as ROWNUMBER_ FROM ( \n" +
                    "                            SELECT DISTINCT PROJECT_CREATION_MST.PCM_ID,\n" +
                    "                            TO_CHAR(PROJECT_CREATION_MST.PCM_ENTRY_DATE,'DD-MON-RR') ENTRY_DATE,\n" +
                    "                            PROJECT_CREATION_MST.PCM_INTERNAL_NO,\n" +
                    "                            PROJECT_CREATION_MST.PCM_PROJECT_CODE,\n" +
                    "                            PROJECT_CREATION_MST.PCM_USER,\n" +
                    "                            PROJECT_CREATION_MST.PCM_PROJECT_NAME,\n" +
                    "                            PROJECT_CREATION_MST.PCM_PROJECT_NO,\n" +
                    "                            TO_CHAR(PROJECT_CREATION_MST.PCM_PROJECT_DATE,'DD-MON-RR') PCM_PROJECT_DATE,\n" +
                    "                            PROJECT_CREATION_MST.PCM_PIC_CHAIRMAN_NAME,\n" +
                    "                            PROJECT_CREATION_MST.PCM_PIC_CHAIRMAN_DETAILS,\n" +
                    "                            PROJECT_CREATION_MST.PCM_ESTIMATE_PROJECT_VALUE,\n" +
                    "                            FINANCIAL_YEAR.FY_FINANCIAL_YEAR_NAME,\n" +
                    "                            FUND_SOURCE_MST.FSM_FUND_NAME,\n" +
                    "                            PROJECT_TYPE_MST.PTM_PROJECT_TYPE_NAME,\n" +
                    "                            PROJECT_TYPE_DTL.PTD_PROJECT_SUBTYPE_NAME,\n" +
                    "                            PROJECT_SANCTION_CATEGORY.PSC_SANCTION_CAT_NAME,\n" +
                    "                            PROJECT_CATEGORY_MST.PCM_CATEGORY_NAME,\n" +
                    "                            PROJECT_CREATION_UNION.PCUN_DDU_ID,\n" +
                    "                            PROJECT_CREATION_UPOZILA.PCU_DD_ID,\n" +
                    "                            PROJECT_CREATION_MST.PCM_PROJ_EVALUATION_REMARKS,\n" +
                    "                            PROJECT_CREATION_MST_GPS_DTL.PCMGD_TYPE_FLAG,PROJECT_CREATION_MST.PCM_PROJECT_DETAILS,\n" +
                    "                            TO_CHAR(PROJECT_CREATION_MST.PCM_ESTIMATE_START_DATE,'DD-MON-RR') START_DATE,\n" +
                    "                            TO_CHAR(PROJECT_CREATION_MST.PCM_ESTIMATE_END_DATE,'DD-MON-RR') END_DATE,PROJECT_CREATION_MST.PCM_PROJECT_SANCTION_TYPE,\n" +
                    "                            PROJECT_CREATION_MST.PCM_PSC_ID, PROJECT_CREATION_MST.PCM_PCM_ID\n" +
                    "                        FROM\n" +
                    "                            PROJECT_CREATION_MST,\n" +
                    "                            PROJECT_CREATION_UPOZILA,\n" +
                    "                            PROJECT_CREATION_UNION,\n" +
                    "                            PROJECT_CREATION_VILLAGE,\n" +
                    "                            PROJECT_CREATION_WARD,\n" +
                    "                            FINANCIAL_YEAR,\n" +
                    "                            FUND_SOURCE_MST,\n" +
                    "                            PROJECT_TYPE_MST,\n" +
                    "                            PROJECT_TYPE_DTL,\n" +
                    "                            PROJECT_SANCTION_CATEGORY,\n" +
                    "                            PROJECT_CATEGORY_MST,\n" +
                    "                            PROJECT_CREATION_MST_GPS_DTL\n" +
                    "                        WHERE FINANCIAL_YEAR.FY_ID = PROJECT_CREATION_MST.PCM_FY_ID\n" +
                    "                            AND FUND_SOURCE_MST.FSM_ID = PROJECT_CREATION_MST.PCM_FSM_ID\n" +
                    "                            AND PROJECT_TYPE_MST.PTM_ID = PROJECT_CREATION_MST.PCM_PTM_ID\n" +
                    "                            AND PROJECT_TYPE_DTL.PTD_ID = PROJECT_CREATION_MST.PCM_PTD_ID\n" +
                    "                            AND PROJECT_CREATION_MST.PCM_PSC_ID = PROJECT_SANCTION_CATEGORY.PSC_ID\n" +
                    "                            AND PROJECT_CREATION_MST.PCM_PCM_ID = PROJECT_CATEGORY_MST.PCM_ID\n" +
                    "                            AND PROJECT_CREATION_MST.PCM_ID = PROJECT_CREATION_UPOZILA.PCU_PCM_ID\n" +
                    "                            AND PROJECT_CREATION_MST.PCM_ID = PROJECT_CREATION_MST_GPS_DTL.PCMGD_PCM_ID (+)\n" +
                    "                            AND PROJECT_CREATION_UNION.PCUN_PCU_ID = PROJECT_CREATION_UPOZILA.PCU_ID\n" +
                    "                            AND PROJECT_CREATION_UNION.PCUN_ID = PROJECT_CREATION_WARD.PCW_PCUN_ID (+)\n" +
                    "                            AND PROJECT_CREATION_WARD.PCW_ID = PROJECT_CREATION_VILLAGE.PCV_PCW_ID (+)\n" +
                    "                            AND PROJECT_CREATION_MST.PCM_PROJ_EVALUATION_FLAG IS NULL\n" +
                    "                            AND (PROJECT_CREATION_MST.PCM_PTD_ID = "+ptd_Id+" OR "+ptd_Id+" IS NULL )\n" +
                    "                            AND (PROJECT_CREATION_MST.PCM_PTM_ID = "+ptm_id+" OR "+ptm_id+" IS NULL )\n" +
                    "                            AND (PROJECT_CREATION_MST.PCM_FSM_ID = "+fsm_id+" OR "+fsm_id+" IS NULL )\n" +
                    "                            AND (PROJECT_CREATION_UNION.PCUN_DDU_ID = "+ddu_id+" OR "+ddu_id+" IS NULL )\n" +
                    "                            AND (PROJECT_CREATION_UPOZILA.PCU_DD_ID = "+dd_id+" OR "+dd_id+" IS NULL )\n" +
                    "                            AND (PROJECT_CREATION_UPOZILA.PCU_DIST_ID = "+dist_id+" OR "+dist_id+" IS NULL )\n" +
                    "                            AND (PROJECT_CREATION_UPOZILA.PCU_DIV_ID = "+div_id+" OR "+div_id+" IS NULL )\n" +
                    "                            AND PROJECT_CREATION_MST.PCM_FY_ID BETWEEN "+fys_id+" AND "+fye_id+" ) p\n" +
                    "                        ORDER BY p.PCM_ID DESC )");

            while (resultSet.next()) {
                count++;
                String stype = "";
                switch (resultSet.getString(25)) {
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

                String pCount = "#"+count;

                projectUpdateLists.add(new ProjectUpdateLists(resultSet.getString(1),resultSet.getString(2),resultSet.getString(3),
                        resultSet.getString(4),resultSet.getString(5),resultSet.getString(6),
                        resultSet.getString(7),resultSet.getString(8),resultSet.getString(9),
                        resultSet.getString(10),resultSet.getString(11),resultSet.getString(12),
                        resultSet.getString(13),resultSet.getString(14),resultSet.getString(15),
                        resultSet.getString(16),resultSet.getString(17),resultSet.getString(18),
                        resultSet.getString(19),resultSet.getString(20),resultSet.getString(21),
                        resultSet.getString(22),resultSet.getString(23),resultSet.getString(24),
                        stype,resultSet.getString(25),resultSet.getString(26),resultSet.getString(27),
                        resultSet.getString(28),pCount,new ArrayList<>()));

            }

            resultSet.close();

            for (int i = 0 ; i < projectUpdateLists.size(); i++) {
                String pcmid = projectUpdateLists.get(i).getPcmId();
                ArrayList<LocationLists> locationLists = new ArrayList<>();
                ResultSet resultSet1 = stmt.executeQuery("SELECT \n" +
                        "PCMGD_LATITUDE,\n" +
                        "PCMGD_LONGITUDE,\n" +
                        "PCMGD_LATITUDE_NUM,\n" +
                        "PCMGD_LONGITUDE_NUM,\n" +
                        "NVL(PCMGD_SEGMENT,0)\n"+
                        "FROM project_creation_mst_gps_dtl WHERE PCMGD_PCM_ID = "+pcmid+" AND PCMGD_ACTIVE_FLAG=1 order by pcmgd_id ASC");

                while (resultSet1.next()) {
                    locationLists.add(new LocationLists(resultSet1.getString(1),resultSet1.getString(2),resultSet1.getInt(5)));
                }

                resultSet1.close();

                projectUpdateLists.get(i).setLocationLists(locationLists);

            }

            stmt.close();

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

            connected = true;

            connection.close();


        } catch (Exception e) {
            Log.i("ERRRRR", e.getLocalizedMessage());
            e.printStackTrace();
        }
    }
}