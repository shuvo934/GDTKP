package com.shuvo.ttit.trkabikha.dialogue;


import static com.shuvo.ttit.trkabikha.adapter.PhotoAdapterPE.bitmapFromPhotoAdapter;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.shuvo.ttit.trkabikha.R;

public class PhotoDialoguePE extends AppCompatDialogFragment {

    AppCompatActivity activity;
    AlertDialog alertDialog;

    PhotoView photoView;
    ImageView clear;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.pic_dial_view, null);
        activity = (AppCompatActivity) view.getContext();
        photoView=  view.findViewById(R.id.photo_view);
        clear = view.findViewById(R.id.clear_image);

        Glide.with(getContext())
                .load(bitmapFromPhotoAdapter)
                .error(R.drawable.loading_error)
                .placeholder(R.drawable.loading)
                .into(photoView);

        builder.setView(view);

        alertDialog = builder.create();

        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        return alertDialog;
    }
}
