package com.shuvo.ttit.trkabikha.dialogue;

import static com.shuvo.ttit.trkabikha.connection.OracleConnection.createConnection;
import static com.shuvo.ttit.trkabikha.projectCreation.CreateProject.selectedVillageAdapter;
import static com.shuvo.ttit.trkabikha.projectCreation.CreateProject.selectedVillageLists;
import static com.shuvo.ttit.trkabikha.projectCreation.CreateProject.selectedWardLists;

import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.material.textfield.TextInputLayout;
import com.rosemaryapp.amazingspinner.AmazingSpinner;
import com.shuvo.ttit.trkabikha.R;
import com.shuvo.ttit.trkabikha.arraylist.ChoiceList;
import com.shuvo.ttit.trkabikha.arraylist.SelectedVillageList;
import com.shuvo.ttit.trkabikha.arraylist.SelectedWardList;
import com.shuvo.ttit.trkabikha.progressbar.WaitProgress;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class SetVillageDialogue extends AppCompatDialogFragment {
    AppCompatActivity activity;
    AlertDialog alertDialog;

    ImageView close;
    AmazingSpinner projectWard;
    AmazingSpinner projectVillage;
    TextInputLayout projectVillageLay;
    TextView villageMissing;
    Button setVillage;

    ArrayList<ChoiceList> wardLists;
    String ward_id = "";

    ArrayList<ChoiceList> villageLists;
    String village_id = "";
    String village_name = "";

    WaitProgress waitProgress = new WaitProgress();
    private Boolean conn = false;
    private Boolean connected = false;
    private Connection connection;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.village_selection_dialogue_layout, null);
        activity = (AppCompatActivity) view.getContext();

        close = view.findViewById(R.id.close_set_village_dialogue);
        projectWard = view.findViewById(R.id.project_ward_for_village_spinner_project_creation);
        projectVillage = view.findViewById(R.id.project_village_spinner_project_creation);
        projectVillageLay = view.findViewById(R.id.project_village_spinner_layout_project_creation);
        projectVillageLay.setEnabled(false);
        villageMissing = view.findViewById(R.id.project_village_missing);
        villageMissing.setVisibility(View.GONE);
        setVillage = view.findViewById(R.id.set_village_button);

        wardLists = new ArrayList<>();
        villageLists = new ArrayList<>();

        builder.setView(view);

        alertDialog = builder.create();

        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);

        for (int i = 0;i < selectedWardLists.size(); i++) {
            String id = selectedWardLists.get(i).getDdw_id();
            String name = selectedWardLists.get(i).getWard_name();
            wardLists.add(new ChoiceList(id,name));
        }

        ArrayList<String> type = new ArrayList<>();
        for(int i = 0; i < wardLists.size(); i++) {
            type.add(wardLists.get(i).getName());
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(),R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type);

        projectWard.setAdapter(arrayAdapter);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        projectWard.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                village_id = "";
                projectVillageLay.setEnabled(false);
                projectVillage.setText("");
                String name = adapterView.getItemAtPosition(i).toString();
                for (int j = 0; j < wardLists.size(); j++) {
                    if (name.equals(wardLists.get(j).getName())) {
                        ward_id = (wardLists.get(j).getId());
                    }
                }
                System.out.println(ward_id);

                new Check().execute();

            }
        });

        projectVillage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                villageMissing.setVisibility(View.GONE);
                String name = adapterView.getItemAtPosition(i).toString();
                for (int j = 0; j < villageLists.size(); j++) {
                    if (name.equals(villageLists.get(j).getName())) {
                        village_id = (villageLists.get(j).getId());
                        village_name = villageLists.get(j).getName();
                    }
                }
                System.out.println(village_name);
                System.out.println(village_id);

            }
        });

        setVillage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (village_id.isEmpty()) {
                    villageMissing.setVisibility(View.VISIBLE);
                }
                else {
                    villageMissing.setVisibility(View.GONE);
                    boolean found = false;
                    for (int i = 0; i < selectedVillageLists.size(); i++) {
                        if (village_id.equals(selectedVillageLists.get(i).getDdv_id())) {
                            found = true;
                            break;
                        }
                    }
                    if (found) {
                        Toast.makeText(getContext(),"This Village has been selected before.",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        selectedVillageLists.add(new SelectedVillageList(village_id,village_name,ward_id));
                        System.out.println(selectedVillageLists.size());
                        selectedVillageAdapter.notifyDataSetChanged();
                        alertDialog.dismiss();

                    }
                }
            }
        });

        return alertDialog;
    }

    public boolean isConnected() {
        boolean connected = false;
        boolean isMobile = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        } catch (Exception e) {
            Log.e("Connectivity Exception", e.getMessage());
        }
        return connected;
    }

    public boolean isOnline() {

        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        }
        catch (IOException | InterruptedException e)          { e.printStackTrace(); }

        return false;
    }

    public class Check extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            waitProgress.show(activity.getSupportFragmentManager(),"WaitBar");
            waitProgress.setCancelable(false);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (isConnected() && isOnline()) {

                ItemData();
                if (connected) {
                    conn = true;
                }

            } else {
                conn = false;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


            if (conn) {

                conn = false;
                connected = false;

                projectVillageLay.setEnabled(true);

                ArrayList<String> type = new ArrayList<>();
                for(int i = 0; i < villageLists.size(); i++) {
                    type.add(villageLists.get(i).getName());
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(),R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type);

                projectVillage.setAdapter(arrayAdapter);

                waitProgress.dismiss();

            }else {
                waitProgress.dismiss();
                Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                AlertDialog dialog;
                dialog = new AlertDialog.Builder(getContext())
                        .setMessage("Please Check Your Internet Connection")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel",null)
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
                        alertDialog.dismiss();

                    }
                });
            }
        }

    }

    public void ItemData() {
        try {
            this.connection = createConnection();
            //    Toast.makeText(MainActivity.this, "Connected",Toast.LENGTH_SHORT).show();

            Statement stmt = connection.createStatement();
            villageLists = new ArrayList<>();

            ResultSet resultSet = stmt.executeQuery("Select DDV_ID, DDW_VILLAGE_NAME from DISTRICT_DTL_VILLAGE WHERE DDV_DDW_ID = "+ward_id+"");

            while (resultSet.next()) {
                villageLists.add(new ChoiceList(resultSet.getString(1),resultSet.getString(2)));
            }

            resultSet.close();
            stmt.close();

            connected = true;

            connection.close();

        }
        catch (Exception e) {

            //   Toast.makeText(MainActivity.this, ""+e,Toast.LENGTH_LONG).show();
            Log.i("ERRRRR", e.getLocalizedMessage());
            e.printStackTrace();
        }
    }
}
