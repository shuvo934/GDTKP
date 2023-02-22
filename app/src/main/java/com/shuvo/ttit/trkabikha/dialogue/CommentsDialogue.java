package com.shuvo.ttit.trkabikha.dialogue;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.shuvo.ttit.trkabikha.R;
import com.shuvo.ttit.trkabikha.progressbar.WaitProgress;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.shuvo.ttit.trkabikha.projectDetails.ProjectDetails.PCM_ID_PD;

import org.json.JSONException;
import org.json.JSONObject;

public class CommentsDialogue extends AppCompatDialogFragment {

    AppCompatActivity activity;
    AlertDialog alertDialog;
    TextInputEditText name;
    TextInputEditText email;
    TextInputEditText comment;

    TextView emailMiss;
    TextView nameMiss;
    TextView commentMiss;

    Button submit;
    TextView cancel;

    WaitProgress waitProgress = new WaitProgress();
    private Boolean conn = false;
    private Boolean connected = false;
//    private Connection connection;

    String pcm = "";

    String nameOfUser = "";
    String emailOfUser = "";
    String commentOfUser = "";

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.comment_dialogue_view, null);
        activity = (AppCompatActivity) view.getContext();

        name = view.findViewById(R.id.name_edit);
        comment = view.findViewById(R.id.comment_edit);
        email = view.findViewById(R.id.email_edit);

        nameMiss = view.findViewById(R.id.name_edit_miss);
        emailMiss = view.findViewById(R.id.email_edit_miss);
        commentMiss = view.findViewById(R.id.comment_edit_miss);

        submit = view.findViewById(R.id.submit_button_dial);
        cancel = view.findViewById(R.id.cancel_comments_dial);

        pcm = PCM_ID_PD;

        builder.setView(view);

        alertDialog = builder.create();

        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("ASHE POSITIVE");


                nameOfUser = name.getText().toString();
                emailOfUser = email.getText().toString();
                commentOfUser = comment.getText().toString();


                if (!nameOfUser.isEmpty()) {
                    System.out.println("NAME");
                    nameMiss.setVisibility(View.GONE);

                    if (!emailOfUser.isEmpty()) {
                        System.out.println("EMAIL");
                        emailMiss.setVisibility(View.GONE);

                        if (!commentOfUser.isEmpty()) {
                            System.out.println("COMMENT");
                            commentMiss.setVisibility(View.GONE);
                            AlertDialog dialog;
                            dialog = new AlertDialog.Builder(getContext())
                                    .setMessage("Do you want to submit your Comment?")
                                    .setPositiveButton("Yes", null)
                                    .setNegativeButton("No",null)
                                    .show();

                            dialog.setCancelable(false);
                            dialog.setCanceledOnTouchOutside(false);
                            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                            positive.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

//                                    new Check().execute();
                                    postComment();
                                    dialog.dismiss();
                                }
                            });

                            Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                            negative.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                   dialog.dismiss();

                                }
                            });
                        }
                        else {
                            commentMiss.setVisibility(View.VISIBLE);
                        }
                    }
                    else {
                        emailMiss.setVisibility(View.VISIBLE);
                    }
                } else {
                    nameMiss.setVisibility(View.VISIBLE);
                }
            }
        });

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (nameMiss.getVisibility() == View.VISIBLE) {
                    if (!editable.toString().isEmpty()) {
                        nameMiss.setVisibility(View.GONE);
                    }
                }

            }
        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (emailMiss.getVisibility() == View.VISIBLE) {
                    if (!editable.toString().isEmpty()) {
                        emailMiss.setVisibility(View.GONE);
                    }
                }

            }
        });

        comment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (commentMiss.getVisibility() == View.VISIBLE ) {
                    if (!editable.toString().isEmpty()) {
                        commentMiss.setVisibility(View.GONE);
                    }
                }

            }
        });

        return alertDialog;
    }

    public void postComment() {
        waitProgress.show(activity.getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        conn = false;
        connected = false;

        String post_url = "http://103.56.208.123:8086/terrain/tr_kabikha/comments/uploadComments";

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, post_url, response -> {
            try {
                conn = true;
                JSONObject jsonObject = new JSONObject(response);
                String string_out = jsonObject.getString("string_out");
                if (string_out.equals("Successfully Created")) {
                    connected = true;
                    updateUI();
                }
                else {
                    connected = false;
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
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("pcm_id",pcm);
                headers.put("submitter_name",nameOfUser);
                headers.put("submiiter_email",emailOfUser);
                headers.put("submitter_message",commentOfUser);
                return headers;
            }
        };

        requestQueue.add(stringRequest);

    }

    public void updateUI() {
        if (conn) {
            waitProgress.dismiss();
            if (connected) {
                Toast.makeText(getContext(), "Comment Submitted Successfully!", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }
            else {
                Toast.makeText(getContext(), "Comment Failed to Submit", Toast.LENGTH_SHORT).show();
                AlertDialog dialog;
                dialog = new AlertDialog.Builder(getContext())
                        .setMessage("Comment Submission Failed")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel",null)
                        .show();

//            dialog.setCancelable(false);
//            dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        conn = false;
                        connected = false;
                        postComment();
                        dialog.dismiss();
                    }
                });
            }

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

//            dialog.setCancelable(false);
//            dialog.setCanceledOnTouchOutside(false);
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    postComment();
                    dialog.dismiss();
                }
            });
        }
    }
}
