package com.shuvo.ttit.trkabikha.projectPicture;

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
import com.shuvo.ttit.trkabikha.adapter.PhotoAdapter;
import com.shuvo.ttit.trkabikha.arraylist.PhotoList;
import com.shuvo.ttit.trkabikha.connection.retrofit.ApiClient;
import com.shuvo.ttit.trkabikha.progressbar.WaitProgress;

import java.util.ArrayList;
import java.util.List;

import static com.shuvo.ttit.trkabikha.projectDetails.ProjectDetails.PCM_ID_PD;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProjectPicture extends AppCompatActivity {

    WaitProgress waitProgress = new WaitProgress();
    private Boolean conn = false;

    String pcm = "";

    RecyclerView recyclerView;
    PhotoAdapter photoAdapter;
    RecyclerView.LayoutManager layoutManager;

    TextView noPhotoMsg;

    ArrayList<PhotoList> photoLists;
    boolean imageFailedToLoad = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_picture);

        recyclerView = findViewById(R.id.photo_list_recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        noPhotoMsg = findViewById(R.id.no_photo_msg);
        noPhotoMsg.setVisibility(View.GONE);

        pcm = PCM_ID_PD;

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
                if (response.isSuccessful()) {
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

//                                    Uri uri = getImageUri(ProjectPicture.this,bitmap);
//                                    Bitmap rotatedBitmap = picRotation(uri,bitmap);
//                                    Bitmap test = null;
//                                    try {
//                                        test = getCorrectlyOrientedImage(ProjectPicture.this,uri);
//                                    } catch (IOException e) {
//                                        e.printStackTrace();
//                                    }
//                                    Matrix matrix = new Matrix();
//                                    matrix.postRotate(90);
//                                    Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
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

                                        String url = "";
                                        url = "http://103.56.208.123:8869/assets/project_image/"+ud_db_generated_file_name;
                                        photoLists.add(new PhotoList(url, ud_date, stype,bitmap));
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

        if (conn) {
            System.out.println("1");
            if (!imageFailedToLoad) {
                System.out.println("2");
                if (photoLists.size() == 0) {
                    noPhotoMsg.setVisibility(View.VISIBLE);
                    String text = "No Photos Found";
                    noPhotoMsg.setText(text);
                } else {
                    noPhotoMsg.setVisibility(View.GONE);
                }

                System.out.println("3");
                photoAdapter = new PhotoAdapter(photoLists,ProjectPicture.this);
                recyclerView.setAdapter(photoAdapter);
//                photoAdapter.notifyDataSetChanged();
                System.out.println("4");

            }
            else {
                noPhotoMsg.setVisibility(View.VISIBLE);
                String text = "Photo Loading Failed From Server";
                noPhotoMsg.setText(text);
            }

            conn = false;
            waitProgress.dismiss();
            System.out.println("5");

        }
        else {
            waitProgress.dismiss();
            Toast.makeText(ProjectPicture.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            AlertDialog dialog;
            dialog = new AlertDialog.Builder(ProjectPicture.this)
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

//    public static Bitmap picRotation(Uri uri, Bitmap source) {
//        Bitmap adjustedBitmap = null;
//        try {
//            ExifInterface exif = new ExifInterface(uri.getPath());
//            int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
//            int rotationInDegrees = exifToDegrees(rotation);
//            Matrix matrix = new Matrix();
//            System.out.println("Rotation: "+rotation);
//            System.out.println("Rotation Degress: "+rotationInDegrees);
//            if (rotation != 0) {
//                matrix.preRotate(rotationInDegrees);
//            }
//            adjustedBitmap = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return adjustedBitmap;
//    }
//
//    private static int exifToDegrees(int exifOrientation) {
//        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) { return 90; }
//        else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {  return 180; }
//        else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {  return 270; }
//        return 0;
//    }
//
//    public Uri getImageUri(Context inContext, Bitmap inImage) {
////        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
////        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
////        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
////        return Uri.parse(path);
//
//        File tempDir= Environment.getExternalStorageDirectory();
//        tempDir=new File(tempDir.getAbsolutePath()+"/.temp/");
//        tempDir.mkdir();
//        File tempFile = null;
//        try {
//            tempFile = File.createTempFile("inImage", ".jpg", tempDir);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
//        byte[] bitmapData = bytes.toByteArray();
//
//        //write the bytes in file
//        FileOutputStream fos = null;
//        try {
//            fos = new FileOutputStream(tempFile);
//            fos.write(bitmapData);
//            fos.flush();
//            fos.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        System.out.println("URI: " + Uri.fromFile(tempFile));
//        return Uri.fromFile(tempFile);
//    }
}