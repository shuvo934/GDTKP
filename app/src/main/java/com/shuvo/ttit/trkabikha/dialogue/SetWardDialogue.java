package com.shuvo.ttit.trkabikha.dialogue;

import static com.shuvo.ttit.trkabikha.projectCreation.CreateProject.ddu_id;
import static com.shuvo.ttit.trkabikha.projectCreation.CreateProject.selectedWardAdapter;
import static com.shuvo.ttit.trkabikha.projectCreation.CreateProject.selectedWardLists;
import static com.shuvo.ttit.trkabikha.projectCreation.CreateProject.villageLayout;

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
import com.rosemaryapp.amazingspinner.AmazingSpinner;
import com.shuvo.ttit.trkabikha.R;
import com.shuvo.ttit.trkabikha.arraylist.ChoiceList;
import com.shuvo.ttit.trkabikha.arraylist.SelectedWardList;
import com.shuvo.ttit.trkabikha.progressbar.WaitProgress;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class SetWardDialogue extends AppCompatDialogFragment {

    AppCompatActivity activity;
    AlertDialog alertDialog;

    ImageView close;
    AmazingSpinner projectWard;
    TextView wardMissing;
    Button setWard;

    ArrayList<ChoiceList> wardLists;
    String ward_id = "";
    String ward_name = "";

    WaitProgress waitProgress = new WaitProgress();
    private Boolean conn = false;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.ward_selection_dialogue_layout, null);
        activity = (AppCompatActivity) view.getContext();

        close = view.findViewById(R.id.close_set_ward_dialogue);
        projectWard = view.findViewById(R.id.project_ward_spinner_project_creation);
        wardMissing = view.findViewById(R.id.project_ward_missing);
        wardMissing.setVisibility(View.GONE);
        setWard = view.findViewById(R.id.set_ward_button);

        wardLists = new ArrayList<>();

        builder.setView(view);

        alertDialog = builder.create();

        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        projectWard.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                wardMissing.setVisibility(View.GONE);
                String name = adapterView.getItemAtPosition(i).toString();
                for (int j = 0; j < wardLists.size(); j++) {
                    if (name.equals(wardLists.get(j).getName())) {
                        ward_id = (wardLists.get(j).getId());
                        ward_name = wardLists.get(j).getName();
                    }
                }
                System.out.println(ward_name);
                System.out.println(ward_id);

            }
        });

        setWard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ward_id.isEmpty()) {
                    wardMissing.setVisibility(View.VISIBLE);
                }
                else {
                    wardMissing.setVisibility(View.GONE);
                    boolean found = false;
                    for (int i = 0; i < selectedWardLists.size(); i++) {
                        if (ward_id.equals(selectedWardLists.get(i).getDdw_id())) {
                            found = true;
                            break;
                        }
                    }
                    if (found) {
                        Toast.makeText(getContext(),"This Ward has been selected before.",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        selectedWardLists.add(new SelectedWardList(ward_id,ward_name));
                        System.out.println(selectedWardLists.size());
                        selectedWardAdapter.notifyDataSetChanged();
                        villageLayout.setVisibility(View.VISIBLE);
                        alertDialog.dismiss();

                    }
                }
            }
        });

//        new Check().execute();
        getWards();
        return alertDialog;
    }

    public void getWards() {
        waitProgress.show(activity.getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        conn = false;

        wardLists = new ArrayList<>();

        String dist_url = "http://103.56.208.123:8086/terrain/tr_kabikha/utility_data/ward_lists?ddu_id="+ddu_id;

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, dist_url, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray jsonArray = new JSONArray(items);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject wardObject = jsonArray.getJSONObject(i);
                        String ddw_id = wardObject.getString("ddw_id");
                        String ddw_ward_name = wardObject.getString("ddw_ward_name");

                        ddw_ward_name = transformText(ddw_ward_name);

                        wardLists.add(new ChoiceList(ddw_id,ddw_ward_name));
                    }
                }
                conn = true;
                updateWards();

            } catch (JSONException e) {
                e.printStackTrace();
                conn = false;
                updateWards();
            }
        }, error -> {
            conn = false;
            updateWards();
        });

        requestQueue.add(stringRequest);

    }

    public void updateWards() {
        if (conn) {

            ArrayList<String> type = new ArrayList<>();
            for(int i = 0; i < wardLists.size(); i++) {
                type.add(wardLists.get(i).getName());
            }
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(),R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type);

            projectWard.setAdapter(arrayAdapter);

            conn = false;
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

                    getWards();
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
