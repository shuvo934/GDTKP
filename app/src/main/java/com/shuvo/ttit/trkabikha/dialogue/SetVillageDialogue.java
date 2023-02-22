package com.shuvo.ttit.trkabikha.dialogue;

import static com.shuvo.ttit.trkabikha.projectCreation.CreateProject.selectedVillageAdapter;
import static com.shuvo.ttit.trkabikha.projectCreation.CreateProject.selectedVillageLists;
import static com.shuvo.ttit.trkabikha.projectCreation.CreateProject.selectedWardLists;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_8;

import android.app.Dialog;
import android.content.Context;

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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;
import com.rosemaryapp.amazingspinner.AmazingSpinner;
import com.shuvo.ttit.trkabikha.R;
import com.shuvo.ttit.trkabikha.arraylist.ChoiceList;
import com.shuvo.ttit.trkabikha.arraylist.SelectedVillageList;
import com.shuvo.ttit.trkabikha.arraylist.SelectedWardList;
import com.shuvo.ttit.trkabikha.progressbar.WaitProgress;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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

                getVillages();

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

    public void getVillages() {
        waitProgress.show(activity.getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        conn = false;

        villageLists = new ArrayList<>();


        String dist_url = "http://103.56.208.123:8086/terrain/tr_kabikha/utility_data/village_lists?ward_id="+ward_id;

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, dist_url, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray jsonArray = new JSONArray(items);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject villaObject = jsonArray.getJSONObject(i);
                        String ddv_id = villaObject.getString("ddv_id");
                        String ddw_village_name = villaObject.getString("ddw_village_name");

                        ddw_village_name = transformText(ddw_village_name);

                        villageLists.add(new ChoiceList(ddv_id,ddw_village_name));
                    }
                }
                conn = true;
                updateVillages();

            } catch (JSONException e) {
                e.printStackTrace();
                conn = false;
                updateVillages();
            }
        }, error -> {
            conn = false;
            updateVillages();
        });

        requestQueue.add(stringRequest);

    }

    public void updateVillages() {
        if (conn) {
            conn = false;

            projectVillageLay.setEnabled(true);

            ArrayList<String> type = new ArrayList<>();
            for(int i = 0; i < villageLists.size(); i++) {
                type.add(villageLists.get(i).getName());
            }
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(),R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type);

            projectVillage.setAdapter(arrayAdapter);

            waitProgress.dismiss();

        }
        else {
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

                    getVillages();
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

    //    --------------------------Transforming Bangla Text-----------------------------
    private String transformText(String text) {
        byte[] bytes = text.getBytes(ISO_8859_1);
        return new String(bytes, UTF_8);
    }
}
