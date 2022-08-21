package com.shuvo.ttit.trkabikha.dialogue;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shuvo.ttit.trkabikha.R;
import com.shuvo.ttit.trkabikha.adapter.CommentAdapter;
import com.shuvo.ttit.trkabikha.arraylist.CommentList;

import java.util.ArrayList;

import static com.shuvo.ttit.trkabikha.projectDetails.ProjectDetails.commentLists;

public class ShowCommentsDialogue extends AppCompatDialogFragment {

    RecyclerView recyclerView;
    CommentAdapter commentAdapter;
    RecyclerView.LayoutManager layoutManager;

    ArrayList<CommentList> commentListsDial;
    AppCompatActivity activity;
    AlertDialog alertDialog;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.show_comments_view, null);
        activity = (AppCompatActivity) view.getContext();

        commentListsDial = new ArrayList<>();

        recyclerView = view.findViewById(R.id.comments_list_recyclerView);

        builder.setView(view);

        alertDialog = builder.create();

        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        commentListsDial = commentLists;

        System.out.println(commentListsDial.size());
        System.out.println(commentListsDial.toString());

        commentAdapter = new CommentAdapter(commentListsDial, getContext());
        recyclerView.setAdapter(commentAdapter);





        alertDialog.setButton(Dialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        return alertDialog;
    }
}
