package com.shuvo.ttit.trkabikha.projectUpdate.editProject.showPicture;

import static com.shuvo.ttit.trkabikha.connection.OracleConnection.createConnection;
import static com.shuvo.ttit.trkabikha.projectUpdate.editProject.ProjectEdit.PCM_ID_PE;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.shuvo.ttit.trkabikha.R;
import com.shuvo.ttit.trkabikha.adapter.PhotoAdapter;
import com.shuvo.ttit.trkabikha.adapter.PhotoAdapterPE;
import com.shuvo.ttit.trkabikha.arraylist.PhotoList;
import com.shuvo.ttit.trkabikha.arraylist.PhotoListFromPE;
import com.shuvo.ttit.trkabikha.progressbar.WaitProgress;
import com.shuvo.ttit.trkabikha.projectPicture.ProjectPicture;

import java.io.IOException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class ShowImage extends AppCompatActivity {

    WaitProgress waitProgress = new WaitProgress();
    private Boolean conn = false;
    private Boolean connected = false;
    private Connection connection;
    private Boolean queryFailed = false;

    String pcm = "";

    RecyclerView recyclerView;
    PhotoAdapterPE photoAdapter;
    RecyclerView.LayoutManager layoutManager;

    TextView noPhotoMsg;

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

        new CheckFORLISt().execute();
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

    public class CheckFORLISt extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            waitProgress.show(getSupportFragmentManager(),"WaitBar");
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

                if (photoLists.size() == 0) {
                    noPhotoMsg.setVisibility(View.VISIBLE);
                    String text = "No Photos Found";
                    noPhotoMsg.setText(text);
                } else {
                    noPhotoMsg.setVisibility(View.GONE);
                }
                photoAdapter = new PhotoAdapterPE(photoLists,ShowImage.this);
                recyclerView.setAdapter(photoAdapter);
                //photoAdapter.notifyDataSetChanged();

                waitProgress.dismiss();

            }else {
                waitProgress.dismiss();
                if (queryFailed) {
//                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.loading_error);
//                    photoLists.add(new PhotoListFromPE(bitmap,"N/A","N/A"));
//                    photoAdapter = new PhotoAdapterPE(photoLists,ShowImage.this);
//                    recyclerView.setAdapter(photoAdapter);
                    noPhotoMsg.setVisibility(View.VISIBLE);
                    String text = "Photo Loading Failed From Server";
                    noPhotoMsg.setText(text);

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

                            new CheckFORLISt().execute();
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

    }

    public void ItemData() {
        try {
            this.connection = createConnection();

            photoLists = new ArrayList<>();
            queryFailed = false;

            Statement stmt = connection.createStatement();

            ResultSet resultSet1 = stmt.executeQuery("select loadBlobFromFile(UD_DB_GENERATED_FILE_NAME) AS image_name,TO_CHAR(UD_DATE, 'DD-MON-RR'),UD_DOC_UPLOAD_STAGE from uploaded_docs WHERE UD_PCM_ID = "+pcm+"");

            while (resultSet1.next()) {

                String stype = "";
                if (resultSet1.getString(3) != null) {
                    switch (resultSet1.getString(3)) {
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
                }

                Blob b=resultSet1.getBlob(1);
                byte[] barr =b.getBytes(1,(int)b.length());
                Bitmap bitmap = BitmapFactory.decodeByteArray(barr,0,barr.length);

                photoLists.add(new PhotoListFromPE(bitmap, resultSet1.getString(2), stype));

            }

            resultSet1.close();
            stmt.close();

            connected = true;


            connection.close();

        }
        catch (Exception e) {

            queryFailed = true;
            //   Toast.makeText(MainActivity.this, ""+e,Toast.LENGTH_LONG).show();
            Log.i("ERRRRR", e.getLocalizedMessage());
            e.printStackTrace();
        }
    }
}