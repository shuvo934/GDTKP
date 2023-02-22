package com.shuvo.ttit.trkabikha.projectUpdate.editProject.showPicture;

import static com.shuvo.ttit.trkabikha.projectUpdate.editProject.ProjectEdit.PCM_ID_PE;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.shuvo.ttit.trkabikha.R;
import com.shuvo.ttit.trkabikha.adapter.PhotoAdapterPE;
import com.shuvo.ttit.trkabikha.arraylist.PhotoListFromPE;
import com.shuvo.ttit.trkabikha.connection.retrofit.ApiClient;
import com.shuvo.ttit.trkabikha.progressbar.WaitProgress;
import com.shuvo.ttit.trkabikha.projectPicture.PictureResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowImage extends AppCompatActivity {

    WaitProgress waitProgress = new WaitProgress();
    private Boolean conn = false;

    String pcm = "";

    RecyclerView recyclerView;
    PhotoAdapterPE photoAdapter;
    RecyclerView.LayoutManager layoutManager;

    TextView noPhotoMsg;
    boolean imageFailedToLoad = false;

    ArrayList<PhotoListFromPE> photoLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);

        recyclerView = findViewById(R.id.photo_list_recyclerView_pe);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        noPhotoMsg = findViewById(R.id.no_photo_msg_pe);
        noPhotoMsg.setVisibility(View.GONE);

        System.out.println(PCM_ID_PE);
        pcm = PCM_ID_PE;

//        new CheckFORLISt().execute();
        getImages();
    }

    public void getImages() {
        waitProgress.show(getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        conn = false;
        imageFailedToLoad = false;

        photoLists = new ArrayList<>();

//        String image_url = "http://103.56.208.123:8086/terrain/tr_kabikha/images/getImages?pcm_id="+pcm;

        Call<PictureResponse> pictureResponseCall = ApiClient.getService().getPictures(pcm);
        pictureResponseCall.enqueue(new Callback<PictureResponse>() {
            @Override
            public void onResponse(Call<PictureResponse> call, Response<PictureResponse> response) {
                System.out.println("RESPONSE 1");
                if (response.isSuccessful()) {
                    System.out.println("RESPONSE 2");
                    if (response.body() != null) {
                        String count = response.body().getCount();
                        if (!count.equals("0")) {
                            List<PictureResponse.PicInfo> items = response.body().getItems();
                            for (int i = 0 ; i < items.size(); i++) {
                                String image_name = items.get(i).getImage_name();
                                String ud_date = items.get(i).getUd_date();
                                String ud_db_generated_file_name = items.get(i).getUd_db_generated_file_name();
                                String ud_doc_upload_stage = items.get(i).getUd_doc_upload_stage();

                                if (image_name.equals("null") || image_name.equals("") ) {
                                    System.out.println("NULL IMAGE");
                                    imageFailedToLoad = true;
                                }
                                else {
                                    byte[] decodedString = Base64.decode(image_name,Base64.DEFAULT);
                                    Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString,0,decodedString.length);
                                    if (bitmap != null) {
                                        System.out.println("OK");

                                        String stype = "";
                                        switch (ud_doc_upload_stage) {
                                            case "1":
                                                stype = "Pre-Work";
                                                break;
                                            case "2":
                                                stype = "On-Working";
                                                break;
                                            case "3":
                                                stype = "Finish-Work";
                                                break;
                                        }

                                        photoLists.add(new PhotoListFromPE(bitmap, ud_date, stype));
                                    }
                                    else {
                                        System.out.println("NOT OK");
                                        imageFailedToLoad = true;
                                    }
                                }
                            }
                        }
                    }
                    conn = true;
                }
                else {
                    conn = false;
                }
                loadImages();
            }

            @Override
            public void onFailure(Call<PictureResponse> call, Throwable t) {
                conn = false;
                loadImages();
            }
        });
    }

    public void loadImages() {
        waitProgress.dismiss();
        if (conn) {
            if (!imageFailedToLoad) {
                if (photoLists.size() == 0) {
                    noPhotoMsg.setVisibility(View.VISIBLE);
                    String text = "No Photos Found";
                    noPhotoMsg.setText(text);
                } else {
                    noPhotoMsg.setVisibility(View.GONE);
                }
                photoAdapter = new PhotoAdapterPE(photoLists,ShowImage.this);
                recyclerView.setAdapter(photoAdapter);
            }
            else {
                noPhotoMsg.setVisibility(View.VISIBLE);
                String text = "Photo Loading Failed From Server";
                noPhotoMsg.setText(text);
            }
            conn = false;

        }
        else {
            Toast.makeText(ShowImage.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            AlertDialog dialog;
            dialog = new AlertDialog.Builder(ShowImage.this)
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

                    getImages();
                    dialog.dismiss();
                }
            });

            Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            negative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    finish();
                }
            });
        }
    }
}