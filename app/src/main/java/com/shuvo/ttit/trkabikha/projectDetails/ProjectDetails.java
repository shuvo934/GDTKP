package com.shuvo.ttit.trkabikha.projectDetails;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.shuvo.ttit.trkabikha.R;
import com.shuvo.ttit.trkabikha.arraylist.CommentList;
import com.shuvo.ttit.trkabikha.arraylist.LocationLists;
import com.shuvo.ttit.trkabikha.dialogue.CommentsDialogue;
import com.shuvo.ttit.trkabikha.dialogue.ShowCommentsDialogue;
import com.shuvo.ttit.trkabikha.progressbar.WaitProgress;
import com.shuvo.ttit.trkabikha.projectPicture.ProjectPicture;
import com.shuvo.ttit.trkabikha.threesixtyimage.ThreeSixtyImage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static com.shuvo.ttit.trkabikha.adapter.ProjectAdapter.locationListsAdapter;
import static com.shuvo.ttit.trkabikha.adapter.ProjectMapAdapter.locationListsMapAdapter;
import static com.shuvo.ttit.trkabikha.connection.OracleConnection.createConnection;

public class ProjectDetails extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    TextView internalNo;
    TextView projectNo;
    TextView projectCode;
    TextView projectName;
    TextView projectDetails;
    TextView entryDate;
    TextView startDate;
    TextView endDate;
    TextView submittedBy;
    TextView projectDate;
    TextView financialYear;
    TextView estimatedValue;
    TextView category;
    TextView projectType;
    TextView fundName;
    TextView sancCategory;
    TextView picDetails;
    TextView remarks;

    Button postComment;

    public String INTERNAL_NO = "";
    public String P_NO = "";
    public String P_CODE = "";
    public String P_NAME = "";
    public String P_DETAILS = "";
    public String ENTRY_DATE = "";
    public String START_DATE = "";
    public String END_DATE = "";
    public String SUBMITTER = "";
    public String P_DATE = "";
    public String F_YEAR = "";
    public String ES_VAL = "";
    public String CATEGORY = "";
    public String P_TYPE = "";
    public String F_NAME = "";
    public String SANC_CAT = "";
    public String PIC_DET = "";
    public String EVAL = "";
    public static String PCM_ID_PD = "";
    public Boolean FROM_MAP = false;

    ScrollView scrollView;
    ImageView imageView;

    ArrayList<LocationLists> locationListsDial;

    private Spinner selection;

    Boolean fullScreen = false;

    WaitProgress waitProgress = new WaitProgress();
    private String message = null;
    private Boolean conn = false;
    private Boolean connected = false;

    private Connection connection;

    public static ArrayList<CommentList> commentLists;

    TextView noCommentMsg;
    LinearLayout commLay;
    TextView nameOfCommentator;
    TextView msgOfCommentator;
    TextView timeOfCommentator;
    Button showAllComm;

    Button projectPic;
    Button pic360;

    Boolean available360 = false;

    public static String URL_360 = "";

    LinearLayout noMap;

//    boolean enable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_details);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.single_map);
        mapFragment.getMapAsync(this);

        selection = findViewById(R.id.spinnnnn_multi2);

        locationListsDial = new ArrayList<>();

        internalNo = findViewById(R.id.internal_no_pr_details);
        projectNo = findViewById(R.id.project_no_pr_details);
        projectCode = findViewById(R.id.project_code_pr_details);
        projectName = findViewById(R.id.project_name_pr_details);
        projectDetails = findViewById(R.id.project_details_pr_details);
        entryDate = findViewById(R.id.project_entry_date_pr_details);
        startDate = findViewById(R.id.project_start_date_pr_details);
        endDate = findViewById(R.id.project_end_date_pr_details);
        submittedBy = findViewById(R.id.project_submitted_by_pr_details);
        projectDate = findViewById(R.id.project_date_pr_details);
        financialYear = findViewById(R.id.project_fin_year_pr_details);
        estimatedValue = findViewById(R.id.project_value_pr_details);
        category = findViewById(R.id.project_category_pr_details);
        projectType = findViewById(R.id.project_type_pr_details);
        fundName = findViewById(R.id.project_fund_name_pr_details);
        sancCategory = findViewById(R.id.project_sanction_cat_pr_details);
        picDetails = findViewById(R.id.project_pic_det_pr_details);
        remarks = findViewById(R.id.project_evaluation_notes_pr_details);
        scrollView = findViewById(R.id.scrollview_project_details);
        imageView = findViewById(R.id.full_screen_changer);

        postComment = findViewById(R.id.comment_button);

        noCommentMsg = findViewById(R.id.no_comment_msg);
        commLay = findViewById(R.id.comments_layout);
        nameOfCommentator = findViewById(R.id.name_of_commentor);
        timeOfCommentator = findViewById(R.id.time_of_commentor);
        msgOfCommentator = findViewById(R.id.comments_of_commentor);
        showAllComm = findViewById(R.id.show_comment_button);

        projectPic = findViewById(R.id.picture_show_button);
        pic360 = findViewById(R.id.three_sixty_image_show_button);
        pic360.setVisibility(View.GONE);

        noMap = findViewById(R.id.no_map_layout);
        noMap.setVisibility(View.GONE);

        commentLists = new ArrayList<>();


        Intent intent = getIntent();


        FROM_MAP = intent.getBooleanExtra("FROM_MAP",false);

        if (FROM_MAP) {
            locationListsDial = locationListsMapAdapter;
        } else {
            locationListsDial = locationListsAdapter;
        }

        INTERNAL_NO = intent.getStringExtra("INTERNAL_NO");
        P_NO = intent.getStringExtra("P_NO");
        P_CODE = intent.getStringExtra("P_CODE");
        P_NAME = intent.getStringExtra("P_NAME");
        P_DETAILS = intent.getStringExtra("P_DETAILS");
        ENTRY_DATE = intent.getStringExtra("ENTRY_DATE");
        START_DATE = intent.getStringExtra("START_DATE");
        END_DATE = intent.getStringExtra("END_DATE");
        SUBMITTER = intent.getStringExtra("SUBMITTER");
        P_DATE = intent.getStringExtra("P_DATE");
        F_YEAR = intent.getStringExtra("F_YEAR");
        ES_VAL = intent.getStringExtra("ES_VAL");
        CATEGORY = intent.getStringExtra("CATEGORY");
        P_TYPE = intent.getStringExtra("P_TYPE");
        F_NAME = intent.getStringExtra("F_NAME");
        SANC_CAT = intent.getStringExtra("SANC_CAT");
        EVAL = intent.getStringExtra("EVAL");
        PIC_DET = intent.getStringExtra("PIC_DET");
        PCM_ID_PD = intent.getStringExtra("PCM_ID");


        internalNo.setText(INTERNAL_NO);
        projectNo.setText(P_NO);
        projectCode.setText(P_CODE);
        projectName.setText(P_NAME);
        projectDetails.setText(P_DETAILS);
        entryDate.setText(ENTRY_DATE);
        startDate.setText(START_DATE);
        endDate.setText(END_DATE);
        submittedBy.setText(SUBMITTER);
        projectDate.setText(P_DATE);
        financialYear.setText(F_YEAR);
        estimatedValue.setText(ES_VAL);
        category.setText(CATEGORY);
        projectType.setText(P_TYPE);
        fundName.setText(F_NAME);
        sancCategory.setText(SANC_CAT);
        remarks.setText(EVAL);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            picDetails.setText(Html.fromHtml(PIC_DET,Html.FROM_HTML_MODE_COMPACT));
        } else {
            picDetails.setText(Html.fromHtml(PIC_DET));
        }



        List<String> categories = new ArrayList<String>();
        categories.add("NORMAL");
        categories.add("SATELLITE");
        categories.add("TERRAIN");
        categories.add("HYBRID");
        categories.add("NO LANDMARK");

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, categories);

        spinnerAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        selection.setAdapter(spinnerAdapter);

//        scrollView.setOnTouchListener(new View.OnTouchListener()
//        {
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event)
//            {
//
//                return !enable;
//            }
//        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!fullScreen) {
                    scrollView.setVisibility(View.GONE);
                    imageView.setImageResource(R.drawable.fullscreen_exit);
                    fullScreen = true;
                } else {
                    scrollView.setVisibility(View.VISIBLE);
                    imageView.setImageResource(R.drawable.fullscreen);
                    fullScreen = false;
                }


            }
        });

        postComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommentsDialogue commentsDialogue = new CommentsDialogue();
                commentsDialogue.show(getSupportFragmentManager(),"COMMENT");
            }
        });

        showAllComm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowCommentsDialogue showCommentsDialogue = new ShowCommentsDialogue();
                showCommentsDialogue.show(getSupportFragmentManager(),"SHOWCOMMENT");
            }
        });

        projectPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(ProjectDetails.this, ProjectPicture.class);
                startActivity(intent1);
//                PicDialogue picDialogue = new PicDialogue();
//                picDialogue.show(getSupportFragmentManager(),"PICTURE");
            }
        });

        pic360.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(ProjectDetails.this, ThreeSixtyImage.class);
                startActivity(intent1);
            }
        });

        new CommentCheck().execute();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.getUiSettings().setZoomControlsEnabled(true);

        selection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String name = parent.getItemAtPosition(position).toString();
                switch (name) {
                    case "NORMAL":
                        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        try {
                            // Customise the styling of the base map using a JSON object defined
                            // in a raw resource file.
                            boolean success = googleMap.setMapStyle(
                                    MapStyleOptions.loadRawResourceStyle(
                                            ProjectDetails.this, R.raw.normal));

                            if (!success) {
                                Log.i("Failed ", "Style parsing failed.");
                            }
                        } catch (Resources.NotFoundException e) {
                            Log.e("Style ", "Can't find style. Error: ", e);
                        }
                        break;
                    case "SATELLITE":
                        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                        break;
                    case "TERRAIN":
                        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                        break;
                    case "HYBRID":
                        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                        break;
                    case "NO LANDMARK":
                        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        try {
                            // Customise the styling of the base map using a JSON object defined
                            // in a raw resource file.
                            boolean success = googleMap.setMapStyle(
                                    MapStyleOptions.loadRawResourceStyle(
                                            ProjectDetails.this, R.raw.no_landmark));

                            if (!success) {
                                Log.i("Failed ", "Style parsing failed.");
                            }
                        } catch (Resources.NotFoundException e) {
                            Log.e("Style ", "Can't find style. Error: ", e);
                        }
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

                LinearLayout info = new LinearLayout(getApplicationContext());
                info.setOrientation(LinearLayout.VERTICAL);

                TextView title = new TextView(getApplicationContext());
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                TextView snippet = new TextView(getApplicationContext());
                snippet.setTextColor(Color.GRAY);
                snippet.setText(marker.getSnippet());

                info.addView(title);
                info.addView(snippet);

                return info;
            }
        });

        if (locationListsDial.size() != 0) {
            noMap.setVisibility(View.GONE);
            if (locationListsDial.size() == 1 ) {
                LatLng latLng = new LatLng(Double.parseDouble(locationListsDial.get(0).getLatitude()),Double.parseDouble(locationListsDial.get(0).getLongitude()));
                Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).title(P_NAME)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_micro_36_2))
                        .snippet("Project No (প্রকল্প নং): "+ P_NO+"\nProject Code (প্রকল্প কোড): "+P_CODE+"\nProject Date: "+P_DATE+
                                "\nEstimated Value: " + ES_VAL + "\nFinancial Year: " + F_YEAR));
                //markerData.add(new MarkerData(marker,pcmId,false));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
            }
            else {


//                PolylineOptions option = new PolylineOptions().width(16)
//                        .color(Color.BLACK)
//                        .geodesic(true).clickable(true);
//                PolylineOptions option1 = new PolylineOptions().width(10)
//                        //.color(Color.parseColor("#00cec9"))
//                        .color(Color.CYAN)
//                        .geodesic(true).clickable(true);
//                for (int j = 0 ; j < locationListsDial.size(); j++ ) {
//                    LatLng point = new LatLng(Double.parseDouble(locationListsDial.get(j).getLatitude()), Double.parseDouble(locationListsDial.get(j).getLongitude()));
//                    option.add(point);
//                    option1.add(point);
//                }
//
//                Polyline polyline = mMap.addPolyline(option);
//                Polyline polyline1 = mMap.addPolyline(option1);
//
//                int a = locationListsDial.size()/2;
//
//                LatLng latLng = new LatLng(Double.parseDouble(locationListsDial.get(a).getLatitude()),Double.parseDouble(locationListsDial.get(a).getLongitude()));
//                Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).title(P_NAME)
//                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.transparent_circle))
//                        .anchor((float) 0.5,(float) 0.5)
//                        .snippet("Project No (প্রকল্প নং): "+ P_NO+"\nProject Code (প্রকল্প কোড): "+P_CODE+"\nProject Date: "+P_DATE));
//                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

                int segment = 0;
                for (int j = 0; j < locationListsDial.size(); j++) {
                    if (locationListsDial.get(j).getSegment() > segment) {
                        segment = locationListsDial.get(j).getSegment();
                    }
                }

                for (int s = 0; s <= segment; s++) {
                    int pointNumber = 0;
                    LatLng point = null;
                    PolylineOptions option = new PolylineOptions().width(16)
                            .color(Color.BLACK)
                            .geodesic(true).clickable(true);
                    PolylineOptions option1 = new PolylineOptions().width(10)
                            .color(Color.CYAN)
                            .geodesic(true).clickable(true);

                    for (int j = 0; j < locationListsDial.size(); j++) {

                        if (s == locationListsDial.get(j).getSegment()) {
                            pointNumber++;
                            point = new LatLng(Double.parseDouble(locationListsDial.get(j).getLatitude()), Double.parseDouble(locationListsDial.get(j).getLongitude()));
                            option.add(point);
                            option1.add(point);
                        }
                    }

                    if (pointNumber == 1) {
                        Marker marker = mMap.addMarker(new MarkerOptions().position(point).title(P_NAME)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_micro_36_2))
                                .snippet("Project No (প্রকল্প নং): "+ P_NO+"\nProject Code (প্রকল্প কোড): "+P_CODE+"\nProject Date: "+P_DATE+
                                        "\nEstimated Value: " + ES_VAL + "\nFinancial Year: " + F_YEAR));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 17));
                    }
                    else if (pointNumber > 1) {

                        Polyline polyline = mMap.addPolyline(option);
                        Polyline polyline1 = mMap.addPolyline(option1);

                        int a = polyline.getPoints().size()/2;

                        LatLng latLng = new LatLng(polyline.getPoints().get(a).latitude,polyline.getPoints().get(a).longitude);
                        Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).title(P_NAME)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.transparent_circle))
                                .anchor((float) 0.5,(float) 0.5)
                                .snippet("Project No (প্রকল্প নং): "+ P_NO+"\nProject Code (প্রকল্প কোড): "+P_CODE+"\nProject Date: "+P_DATE+
                                        "\nEstimated Value: " + ES_VAL + "\nFinancial Year: " + F_YEAR));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                    }

                }

            }
        }
        else {
            noMap.setVisibility(View.VISIBLE);
        }
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

    public class CommentCheck extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            waitProgress.show(getSupportFragmentManager(), "WaitBar");
            waitProgress.setCancelable(false);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (isConnected() && isOnline()) {

                CommentQuery();
                if (connected) {
                    conn = true;
                    message = "Internet Connected";
                }

            } else {
                conn = false;
                message = "Not Connected";
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            waitProgress.dismiss();
            if (conn) {



                if (commentLists.size() == 0) {

                    noCommentMsg.setVisibility(View.VISIBLE);
                    commLay.setVisibility(View.GONE);
                    showAllComm.setVisibility(View.GONE);
                }
                else if (commentLists.size() == 1) {
                    noCommentMsg.setVisibility(View.GONE);
                    commLay.setVisibility(View.VISIBLE);
                    int index = commentLists.size() - 1;
                    nameOfCommentator.setText(commentLists.get(index).getCommentator());
                    timeOfCommentator.setText(commentLists.get(index).getComment_time());
                    msgOfCommentator.setText(commentLists.get(index).getComment());
                    showAllComm.setVisibility(View.GONE);
                }
                else if (commentLists.size() > 1) {
                    int index = commentLists.size() - 1;
                    noCommentMsg.setVisibility(View.GONE);
                    commLay.setVisibility(View.VISIBLE);
                    nameOfCommentator.setText(commentLists.get(index).getCommentator());
                    timeOfCommentator.setText(commentLists.get(index).getComment_time());
                    msgOfCommentator.setText(commentLists.get(index).getComment());
                    showAllComm.setVisibility(View.VISIBLE);
                }

                if (available360) {
                    pic360.setVisibility(View.VISIBLE);
                } else {
                    pic360.setVisibility(View.GONE);
                }



                conn = false;
                connected = false;



            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                AlertDialog dialog = new AlertDialog.Builder(ProjectDetails.this)
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

                        new CommentCheck().execute();
                        dialog.dismiss();
                    }
                });

                Button negative = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                negative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        finish();
                    }
                });
            }
        }
    }

    public void CommentQuery() {
        try {
            this.connection = createConnection();


            Statement stmt = connection.createStatement();

            commentLists = new ArrayList<>();

            ResultSet resultSet = stmt.executeQuery("select pcof_id, PCOF_PCM_ID, PCOF_SUBMITTER_NAME,PCOF_SUBMITTER_EMAIL,PCOF_SUBMITTER_MESSAGE,\n" +
                    "TO_CHAR(PCOF_TIME, 'DD-MON-RR') as C_Date\n" +
                    "from project_creation_online_feed \n" +
                    "where pcof_pcm_id = "+PCM_ID_PD+"\n" +
                    "and PCOF_APPROVAL_FLAG = 1\n" +
                    "order by pcof_id --desc");

            while (resultSet.next()) {


                commentLists.add(new CommentList(resultSet.getString(1),resultSet.getString(2),resultSet.getString(3),
                        resultSet.getString(4),resultSet.getString(5),resultSet.getString(6)));
            }

            resultSet.close();

            available360 = false;
//            if (USERNAME_CONNECTION.equals("TR_KABIKHA")) {
//
//                available360 = false;
//            } else {
//                ResultSet resultSet1 = stmt.executeQuery("SELECT UD_DB_GENERATED_FILE_NAME, TO_CHAR(UD_DATE, 'DD-MON-RR'),UD_DOC_UPLOAD_STAGE \n" +
//                        "                        FROM UPLOADED_DOCS WHERE NVL(UD_THREESIXTY_FLAG,0) = 1\n" +
//                        "                        AND UD_PCM_ID = "+PCM_ID_PD+"");
//
//                while (resultSet1.next()) {
//
//                    URL_360 = "http://103.56.208.123:8863/assets/project_image/" +resultSet1.getString(1);
//
//                    if (URL_360.isEmpty()) {
//                        available360 = false;
//                    } else {
//                        available360 = true;
//                    }
//                }
//
//                resultSet1.close();
//            }


            stmt.close();


            connected = true;

            connection.close();


        } catch (Exception e) {
            Log.i("ERRRRR", e.getLocalizedMessage());
            e.printStackTrace();
        }
    }
}