package com.shuvo.ttit.trkabikha.projectPicture;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
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
import com.shuvo.ttit.trkabikha.arraylist.PhotoList;
import com.shuvo.ttit.trkabikha.progressbar.WaitProgress;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import static com.shuvo.ttit.trkabikha.connection.OracleConnection.createConnection;
import static com.shuvo.ttit.trkabikha.projectDetails.ProjectDetails.PCM_ID_PD;

public class ProjectPicture extends AppCompatActivity {

    WaitProgress waitProgress = new WaitProgress();
    private Boolean conn = false;
    private Boolean connected = false;
    private Connection connection;

    String pcm = "";

    RecyclerView recyclerView;
    PhotoAdapter photoAdapter;
    RecyclerView.LayoutManager layoutManager;

    TextView noPhotoMsg;

    ArrayList<PhotoList> photoLists;

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
                } else {
                    noPhotoMsg.setVisibility(View.GONE);
                }
                photoAdapter = new PhotoAdapter(photoLists,ProjectPicture.this);
                recyclerView.setAdapter(photoAdapter);
                photoAdapter.notifyDataSetChanged();


                waitProgress.dismiss();

            }else {
                waitProgress.dismiss();
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                AlertDialog dialog;
                dialog = new AlertDialog.Builder(getApplicationContext())
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
                    }
                });
            }
        }

    }

    public void ItemData() {
        try {
            this.connection = createConnection();
            //    Toast.makeText(MainActivity.this, "Connected",Toast.LENGTH_SHORT).show();

            photoLists = new ArrayList<>();

            Statement stmt = connection.createStatement();

//            if (USERNAME_CONNECTION.equals("TR_KABIKHA")) {

            ResultSet resultSet1 = stmt.executeQuery("SELECT UD_DB_GENERATED_FILE_NAME, TO_CHAR(UD_DATE, 'DD-MON-RR'),UD_DOC_UPLOAD_STAGE \n" +
                    "FROM UPLOADED_DOCS\n" +
                    "WHERE UD_PCM_ID = "+pcm+"");

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

                System.out.println(resultSet1.getString(1));
                String url = "";
                url = "http://103.56.208.123:8869/assets/project_image/"+resultSet1.getString(1);

                photoLists.add(new PhotoList(url, resultSet1.getString(2), stype));

            }

            resultSet1.close();
            stmt.close();

//            } else {
//
//                ResultSet resultSet1 = stmt.executeQuery("SELECT UD_DB_GENERATED_FILE_NAME, TO_CHAR(UD_DATE, 'DD-MON-RR'),UD_DOC_UPLOAD_STAGE \n" +
//                        "FROM UPLOADED_DOCS WHERE NVL(UD_THREESIXTY_FLAG,0) = 0\n" +
//                        "AND UD_PCM_ID = "+pcm+"");
//
//                while (resultSet1.next()) {
//
//                    String stype = "";
//                    if (resultSet1.getString(3) != null) {
//                        switch (resultSet1.getString(3)) {
//                            case "1":
//                                stype = "Pre-Work";
//                                break;
//                            case "2":
//                                stype = "On-Working";
//                                break;
//                            case "3":
//                                stype = "Finish-Work";
//                                break;
//                        }
//                    }
//
//                    System.out.println(resultSet1.getString(1));
//                    String url = "";
//                    url = "http://103.56.208.123:8863/assets/project_image/" +resultSet1.getString(1);
//
//                    photoLists.add(new PhotoList(url, resultSet1.getString(2), stype));
//
//                }
//
//                resultSet1.close();
//                stmt.close();
//
//            }



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