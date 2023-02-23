package com.shuvo.ttit.trkabikha.projectUpdate.editProject;

import static com.shuvo.ttit.trkabikha.adapter.ProjectUpdateAdapter.locationListsAdapterPU;
import static com.shuvo.ttit.trkabikha.login.PICLogin.picUserDetails;
import static com.shuvo.ttit.trkabikha.mainmenu.HomePage.projectUpdateLists;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_8;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.ExifInterface;

import android.net.Uri;

import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.Html;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.rosemaryapp.amazingspinner.AmazingSpinner;
import com.shuvo.ttit.trkabikha.R;
import com.shuvo.ttit.trkabikha.adapter.ImageCapturedAdapter;
import com.shuvo.ttit.trkabikha.arraylist.ChoiceList;
import com.shuvo.ttit.trkabikha.arraylist.ImageCapturedList;
import com.shuvo.ttit.trkabikha.arraylist.LocationLists;
import com.shuvo.ttit.trkabikha.connection.retrofit.ApiClient;
import com.shuvo.ttit.trkabikha.connection.retrofit.ProjectRequest;
import com.shuvo.ttit.trkabikha.connection.retrofit.ProjectResponse;
import com.shuvo.ttit.trkabikha.dialogue.ImageDialogue;
import com.shuvo.ttit.trkabikha.gpxCreation.GpxCreationMap;
import com.shuvo.ttit.trkabikha.progressbar.WaitProgress;
import com.shuvo.ttit.trkabikha.projectUpdate.editProject.showMap.ShowInMap;
import com.shuvo.ttit.trkabikha.projectUpdate.editProject.showPicture.ShowImage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.sql.CallableStatement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;

public class ProjectEdit extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ImageCapturedAdapter.ClickedItem {

    TextInputEditText internalNo;
    TextInputEditText projectCode;
    TextInputEditText entryDate;
    TextInputEditText projectNo;
    TextInputEditText approvalDate;
    TextInputEditText projectName;
    TextInputEditText startDate;
    TextInputEditText endDate;
    TextInputEditText financialYear;
    TextInputEditText sourceOfFund;
    TextInputEditText projectValue;
    AmazingSpinner projectValueType;
    TextInputEditText projectType;
    TextInputEditText projectSubType;
    TextInputEditText projectPICDetails;
    TextInputEditText projectDetails;

    CardView mapTaken;
    CardView imageTaken;

    ArrayList<ChoiceList> projectvalurTypeLists;

    AmazingSpinner sanctionCat;
    AmazingSpinner sanctionSubCat;

    ArrayList<ChoiceList> sanctionCatLists;
    ArrayList<ChoiceList> sanctionSubCatLists;

    Button firstImage;
    Button save;

    public static String INTERNAL_NO = "";
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
    public String P_SUB_TYPE = "";
    public String F_NAME = "";
    public String SANC_CAT = "";
    public String PIC_DET = "";
    public String EVAL = "";
    public static String PCM_ID_PE = "";
    public String S_TYPE = "";
    public String S_TYPE_ID = "";
    public String PSC_SANCTION_CAT_ID = "";
    public String PCM_CATEGORY_ID = "";
    public Boolean IMAGE_DATA = false;
    public String DISTANCE_METER = "";

    WaitProgress waitProgress = new WaitProgress();
    private Boolean conn = false;
    private Boolean connected = false;

    private GoogleApiClient googleApiClient;

    FusedLocationProviderClient fusedLocationProviderClientCamera;
    LocationRequest locationRequestCamera;
    LocationCallback locationCallbackCamera;
    LocationManager locationManager;
    public static String currentPhotoPath;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    public static String imageFileName = "";
    final LatLng[] cameraLatLng = {null};
    public static Location targetLocation = null;
    String address = "";
    public static Bitmap firstBitmap = null;

    private int mYear, mMonth, mDay;

    RecyclerView imageCapturedview;
    RecyclerView.LayoutManager layoutManager;
    @SuppressLint("StaticFieldLeak")
    public static ImageCapturedAdapter imageCapturedAdapter;

    public static ArrayList<ImageCapturedList> imageCapturedLists;

    boolean gps_enabled = false;
    boolean network_enabled = false;
    boolean gpxUploaded = false;
    boolean gpxAvailable = false;

    public static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>";
    public static final String TAG_GPX = "<gpx"
            + " xmlns=\"http://www.topografix.com/GPX/1/1\""
            + " version=\"1.1\""
            + " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\""
            + " xsi:schemaLocation=\"http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd\">";


    public static String gpxContent = "";
    public static ArrayList<LocationLists> locationListsCreate;
    LatLng[] WayLatLng;

    public static RelativeLayout gpxFileLayout;
    public static TextView gpxFileName;
    ImageView deleteGpxFile;

    public static Button addWaypoint;
    public static Button addTrack;

    String project_no_to_update = "";
    String project_name_to_update = "";
    String project_value_to_update = "";
    String pic_details_to_update = "";
    String project_details_to_update = "";

    CardView showMap;
    CardView showImage;

    private int numberOfRequestsToMake;
    private boolean hasRequestFailed = false;

    ArrayList<LocationLists> preLocationLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_edit);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        internalNo = findViewById(R.id.internal_no_project_edit);
        projectCode = findViewById(R.id.project_code_project_edit);
        entryDate = findViewById(R.id.project_entry_date_project_edit);
        projectNo = findViewById(R.id.project_no_project_edit);
        approvalDate = findViewById(R.id.approval_date_project_edit);
        projectName = findViewById(R.id.project_name_project_edit);
        startDate = findViewById(R.id.project_start_date_project_edit);
        endDate = findViewById(R.id.project_end_date_project_edit);
        financialYear = findViewById(R.id.financial_year_project_edit_spinner);
        sourceOfFund = findViewById(R.id.source_fund_spinner_project_edit);
        projectValue = findViewById(R.id.project_value_project_edit);
        projectValueType = findViewById(R.id.project_value_type_spinner);
        sanctionCat = findViewById(R.id.project_sanction_cat_spinner);
        sanctionSubCat = findViewById(R.id.project_sanction_sub_cat_spinner);
        projectType = findViewById(R.id.project_type_project_edit);
        projectSubType = findViewById(R.id.project_sub_type_project_edit);
        projectPICDetails = findViewById(R.id.project_pic_contractor_details_project_edit);
        projectDetails = findViewById(R.id.project_details_project_edit);
        showMap = findViewById(R.id.show_map_data_card);
        showMap.setVisibility(View.GONE);
        showImage = findViewById(R.id.show_picture_data_card);
        showImage.setVisibility(View.GONE);
        save = findViewById(R.id.updated_project_save_button);

        mapTaken = findViewById(R.id.taken_map_data_card);
        mapTaken.setVisibility(View.GONE);
        imageTaken = findViewById(R.id.taken_image_data_card);
        imageTaken.setVisibility(View.GONE);

        gpxFileLayout = findViewById(R.id.gpx_file_name_layout);
        gpxFileLayout.setVisibility(View.GONE);
        gpxFileName = findViewById(R.id.gpx_file_name);
        deleteGpxFile = findViewById(R.id.delete_gpx_file);
        gpxFileName.setText("");

        addWaypoint = findViewById(R.id.button_for_waypoint);
        addTrack = findViewById(R.id.button_for_track);

        firstImage = findViewById(R.id.first_picture);
        imageCapturedview = findViewById(R.id.image_captured_list_view);
        imageCapturedLists = new ArrayList<>();

        gpxContent = "";
        locationListsCreate = new ArrayList<>();

        projectvalurTypeLists = new ArrayList<>();
        sanctionCatLists = new ArrayList<>();
        sanctionSubCatLists = new ArrayList<>();

        preLocationLists = new ArrayList<>();

        fusedLocationProviderClientCamera = LocationServices.getFusedLocationProviderClient(this);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationRequestCamera = LocationRequest.create();
        locationRequestCamera.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequestCamera.setInterval(5000);
        locationRequestCamera.setFastestInterval(1000);

        Intent intent = getIntent();
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
        P_SUB_TYPE = intent.getStringExtra("P_SUB_TYPE");
        F_NAME = intent.getStringExtra("F_NAME");
        SANC_CAT = intent.getStringExtra("SANC_CAT");
        EVAL = intent.getStringExtra("EVAL");
        PIC_DET = intent.getStringExtra("PIC_DET");
        PCM_ID_PE = intent.getStringExtra("PCM_ID");
        S_TYPE = intent.getStringExtra("S_TYPE");
        S_TYPE_ID = intent.getStringExtra("S_TYPE_ID");
        PSC_SANCTION_CAT_ID = intent.getStringExtra("PSC_SANCTION_CAT_ID");
        PCM_CATEGORY_ID = intent.getStringExtra("PCM_CATEGORY_ID");
        IMAGE_DATA = intent.getBooleanExtra("IMAGE_DATA",false);
        DISTANCE_METER = intent.getStringExtra("DISTANCE_METER");

        projectvalurTypeLists.add(new ChoiceList("0", "Taka(টাকা)"));
        projectvalurTypeLists.add(new ChoiceList("1","Rice(চাল) (MT)"));
        projectvalurTypeLists.add(new ChoiceList("2","Wheat(গম) (MT)"));

        internalNo.setText(INTERNAL_NO);
        projectCode.setText(P_CODE);
        entryDate.setText(ENTRY_DATE);
        projectNo.setText(P_NO);
        approvalDate.setText(P_DATE);
        projectName.setText(P_NAME);
        startDate.setText(START_DATE);
        endDate.setText(END_DATE);
        financialYear.setText(F_YEAR);
        sourceOfFund.setText(F_NAME);
        projectValue.setText(ES_VAL);
        projectType.setText(P_TYPE);
        projectSubType.setText(P_SUB_TYPE);
//        projectPICDetails.setText(PIC_DET);
        projectDetails.setText(P_DETAILS);

        preLocationLists = locationListsAdapterPU;


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            PIC_DET = String.valueOf(Html.fromHtml(PIC_DET,Html.FROM_HTML_MODE_COMPACT));
            projectPICDetails.setText(PIC_DET);
        } else {
            PIC_DET = String.valueOf(Html.fromHtml(PIC_DET));
            projectPICDetails.setText(PIC_DET);
        }

        if (locationListsAdapterPU.size() != 0) {
            mapTaken.setVisibility(View.VISIBLE);
            showMap.setVisibility(View.VISIBLE);
        }
        else {
            mapTaken.setVisibility(View.GONE);
            showMap.setVisibility(View.GONE);
        }

        if (IMAGE_DATA) {
            imageTaken.setVisibility(View.VISIBLE);
            showImage.setVisibility(View.VISIBLE);
        }
        else {
            imageTaken.setVisibility(View.GONE);
            showImage.setVisibility(View.GONE);
        }

        imageCapturedview.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        imageCapturedview.setLayoutManager(layoutManager);
        imageCapturedAdapter = new ImageCapturedAdapter(ProjectEdit.this,imageCapturedLists,ProjectEdit.this);
        imageCapturedview.setAdapter(imageCapturedAdapter);

        projectValueType.setText(S_TYPE);

        approvalDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                if(P_DATE.isEmpty()) {
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);
                }
                else {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yy",Locale.getDefault());
                    Date date = null;
                    try {
                        date = simpleDateFormat.parse(P_DATE);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (date != null) {
                        c.setTime(date);
                    }

                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);
                }

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    DatePickerDialog datePickerDialog = new DatePickerDialog(ProjectEdit.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                            String monthName = "";
                            String dayOfMonthName = "";
                            String yearName = "";
                            month = month + 1;
                            if (month == 1) {
                                monthName = "JAN";
                            } else if (month == 2) {
                                monthName = "FEB";
                            } else if (month == 3) {
                                monthName = "MAR";
                            } else if (month == 4) {
                                monthName = "APR";
                            } else if (month == 5) {
                                monthName = "MAY";
                            } else if (month == 6) {
                                monthName = "JUN";
                            } else if (month == 7) {
                                monthName = "JUL";
                            } else if (month == 8) {
                                monthName = "AUG";
                            } else if (month == 9) {
                                monthName = "SEP";
                            } else if (month == 10) {
                                monthName = "OCT";
                            } else if (month == 11) {
                                monthName = "NOV";
                            } else if (month == 12) {
                                monthName = "DEC";
                            }

                            if (dayOfMonth <= 9) {
                                dayOfMonthName = "0" + String.valueOf(dayOfMonth);
                            } else {
                                dayOfMonthName = String.valueOf(dayOfMonth);
                            }
                            yearName  = String.valueOf(year);
                            yearName = yearName.substring(yearName.length()-2);
                            System.out.println(yearName);
                            System.out.println(dayOfMonthName);
                            approvalDate.setText(dayOfMonthName + "-" + monthName + "-" + yearName);
                            P_DATE = Objects.requireNonNull(approvalDate.getText()).toString();
                        }
                    }, mYear, mMonth, mDay);
//                    datePickerDialog.getDatePicker().setMaxDate(result.getTime());
//                    datePickerDialog.getDatePicker().setMinDate(ff.getTime());
                    datePickerDialog.show();
                }
            }
        });

        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                if (!START_DATE.isEmpty()) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yy", Locale.getDefault());
                    Date date = null;
                    try {
                        date = simpleDateFormat.parse(START_DATE);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (date != null) {
                        c.setTime(date);
                    }

                }
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    DatePickerDialog datePickerDialog = new DatePickerDialog(ProjectEdit.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                            String monthName = "";
                            String dayOfMonthName = "";
                            String yearName = "";
                            month = month + 1;
                            if (month == 1) {
                                monthName = "JAN";
                            } else if (month == 2) {
                                monthName = "FEB";
                            } else if (month == 3) {
                                monthName = "MAR";
                            } else if (month == 4) {
                                monthName = "APR";
                            } else if (month == 5) {
                                monthName = "MAY";
                            } else if (month == 6) {
                                monthName = "JUN";
                            } else if (month == 7) {
                                monthName = "JUL";
                            } else if (month == 8) {
                                monthName = "AUG";
                            } else if (month == 9) {
                                monthName = "SEP";
                            } else if (month == 10) {
                                monthName = "OCT";
                            } else if (month == 11) {
                                monthName = "NOV";
                            } else if (month == 12) {
                                monthName = "DEC";
                            }

                            if (dayOfMonth <= 9) {
                                dayOfMonthName = "0" + String.valueOf(dayOfMonth);
                            } else {
                                dayOfMonthName = String.valueOf(dayOfMonth);
                            }
                            yearName  = String.valueOf(year);
                            yearName = yearName.substring(yearName.length()-2);
                            System.out.println(yearName);
                            System.out.println(dayOfMonthName);
                            startDate.setText(dayOfMonthName + "-" + monthName + "-" + yearName);
                            START_DATE = Objects.requireNonNull(startDate.getText()).toString();
                        }
                    }, mYear, mMonth, mDay);
//                    datePickerDialog.getDatePicker().setMaxDate(result.getTime());
//                    datePickerDialog.getDatePicker().setMinDate(ff.getTime());
                    datePickerDialog.show();
                }
            }
        });

        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                if (!END_DATE.isEmpty()) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yy", Locale.getDefault());
                    Date date = null;
                    try {
                        date = simpleDateFormat.parse(END_DATE);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (date != null) {
                        c.setTime(date);
                    }

                }
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    DatePickerDialog datePickerDialog = new DatePickerDialog(ProjectEdit.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                            String monthName = "";
                            String dayOfMonthName = "";
                            String yearName = "";
                            month = month + 1;
                            if (month == 1) {
                                monthName = "JAN";
                            } else if (month == 2) {
                                monthName = "FEB";
                            } else if (month == 3) {
                                monthName = "MAR";
                            } else if (month == 4) {
                                monthName = "APR";
                            } else if (month == 5) {
                                monthName = "MAY";
                            } else if (month == 6) {
                                monthName = "JUN";
                            } else if (month == 7) {
                                monthName = "JUL";
                            } else if (month == 8) {
                                monthName = "AUG";
                            } else if (month == 9) {
                                monthName = "SEP";
                            } else if (month == 10) {
                                monthName = "OCT";
                            } else if (month == 11) {
                                monthName = "NOV";
                            } else if (month == 12) {
                                monthName = "DEC";
                            }

                            if (dayOfMonth <= 9) {
                                dayOfMonthName = "0" + String.valueOf(dayOfMonth);
                            } else {
                                dayOfMonthName = String.valueOf(dayOfMonth);
                            }
                            yearName  = String.valueOf(year);
                            yearName = yearName.substring(yearName.length()-2);
                            System.out.println(yearName);
                            System.out.println(dayOfMonthName);
                            endDate.setText(dayOfMonthName + "-" + monthName + "-" + yearName);
                            END_DATE = Objects.requireNonNull(endDate.getText()).toString();
                        }
                    }, mYear, mMonth, mDay);
//                    datePickerDialog.getDatePicker().setMaxDate(result.getTime());
//                    datePickerDialog.getDatePicker().setMinDate(ff.getTime());
                    datePickerDialog.show();
                }
            }
        });

        ArrayList<String> type = new ArrayList<>();
        for(int i = 0; i < projectvalurTypeLists.size(); i++) {
            type.add(projectvalurTypeLists.get(i).getName());
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type);

        projectValueType.setAdapter(arrayAdapter);

        projectValueType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String name = adapterView.getItemAtPosition(i).toString();
                for (int j = 0; j < projectvalurTypeLists.size(); j++) {
                    if (name.equals(projectvalurTypeLists.get(j).getName())) {
                        S_TYPE_ID = (projectvalurTypeLists.get(j).getId());
                        S_TYPE = name;
                    }
                }
                System.out.println(S_TYPE_ID);
            }
        });

        sanctionCat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String name = adapterView.getItemAtPosition(i).toString();
                for (int j = 0; j < sanctionCatLists.size(); j++) {
                    if (name.equals(sanctionCatLists.get(j).getName())) {
                        PSC_SANCTION_CAT_ID = (sanctionCatLists.get(j).getId());
                        SANC_CAT = name;
                    }
                }
                System.out.println(name);
                System.out.println(PSC_SANCTION_CAT_ID);

            }
        });

        sanctionSubCat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String name = adapterView.getItemAtPosition(i).toString();
                for (int j = 0; j < sanctionSubCatLists.size(); j++) {
                    if (name.equals(sanctionSubCatLists.get(j).getName())) {
                        PCM_CATEGORY_ID = (sanctionSubCatLists.get(j).getId());
                        CATEGORY = name;
                    }
                }
                System.out.println(name);
                System.out.println(PCM_CATEGORY_ID);

            }
        });

        locationCallbackCamera = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {

                    targetLocation = location;
                    cameraLatLng[0] = new LatLng(location.getLatitude(), location.getLongitude());
                    Log.i("Camera LocationUpdate:",cameraLatLng[0].toString());
                }
            }
        };

        firstImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cameraLatLng[0] != null) {
                    if (preLocationLists.size() != 0) {
                        if (preLocationLists.size() == 1) {
                            double startLatitude = Double.parseDouble(preLocationLists.get(0).getLatitude());
                            double startLongitude = Double.parseDouble(preLocationLists.get(0).getLongitude());
                            float[] distance = new float[1];
                            Location.distanceBetween(startLatitude,startLongitude,cameraLatLng[0].latitude,cameraLatLng[0].longitude,distance);

                            float radius;
                            if (DISTANCE_METER != null) {
                                if (!DISTANCE_METER.isEmpty()) {
                                    radius = Float.parseFloat(DISTANCE_METER);
                                }
                                else {
                                    radius = 0;
                                }
                            }
                            else {
                                radius = 0;
                            }
                            System.out.println("DISTANCE FROM MAIN LOCATION: " + distance[0]);

                            if (radius == 0) {
                                cameraClick();
                            }
                            else if (distance[0] <= radius) {
                                cameraClick();
                            }
                            else {
                                int dis = (int) distance[0];
                                Toast.makeText(getApplicationContext(), "You are so far from Main Project Location",Toast.LENGTH_SHORT).show();
                                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(ProjectEdit.this);
                                builder.setTitle("Alert!")
                                        .setMessage("You are "+dis+" meters away from project location. You need to be inside around "+ (int) radius+" meters. Do you still want to take pictures?")
                                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                cameraClick();
                                            }
                                        })
                                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();

                            }
                        }
                        else {
                            float[] distance = new float[1];
                            float radius;
                            float[] finalDistance = new float[1];
                            boolean found = false;
                            if (DISTANCE_METER != null) {
                                if (!DISTANCE_METER.isEmpty()) {
                                    radius = Float.parseFloat(DISTANCE_METER);
                                    for (int i = 0 ; i < preLocationLists.size(); i++) {
                                        double startLatitude = Double.parseDouble(preLocationLists.get(i).getLatitude());
                                        double startLongitude = Double.parseDouble(preLocationLists.get(i).getLongitude());
                                        Location.distanceBetween(startLatitude,startLongitude,cameraLatLng[0].latitude,cameraLatLng[0].longitude,distance);
                                        if (distance[0] <= radius) {
                                            finalDistance[0] = distance[0];
                                            found = true;
                                            break;
                                        }
                                        else {
                                            if (i == 0) {
                                                finalDistance[0] = distance[0];
                                            }
                                            else {
                                                if (distance[0] < finalDistance[0]) {
                                                    finalDistance[0] = distance[0];
                                                }
                                            }
                                        }
                                    }
                                }
                                else {
                                    radius = 0;
                                }
                            }
                            else {
                                radius = 0;
                            }

                            System.out.println("SHORTEST DISTANCE FROM MAIN LOCATION: " + finalDistance[0]);

                            if (radius == 0) {
                                cameraClick();
                            }
                            else if (found) {
                                cameraClick();
                            }
                            else {
                                float dd = finalDistance[0] - radius;
                                if (dd < 100) {
                                    Toast.makeText(getApplicationContext(), "You are only "+(int) dd+" meters behind from Project Location. Try to move a little forward.",Toast.LENGTH_SHORT).show();
                                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(ProjectEdit.this);
                                    builder.setTitle("Alert!")
                                            .setMessage("You are only "+ (int) finalDistance[0]+" meters away from project location. You need to be inside around "+ (int) radius+" meters. Do you still want to take pictures?")
                                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    cameraClick();
                                                }
                                            })
                                            .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                }
                                            });
                                    AlertDialog alert = builder.create();
                                    alert.show();
                                }
                                else {
                                    Toast.makeText(getApplicationContext(), "You are so far from Main Project Location",Toast.LENGTH_SHORT).show();
                                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(ProjectEdit.this);
                                    builder.setTitle("Alert!")
                                            .setMessage("You are "+(int) finalDistance[0]+" meters away from project location. You need to be inside around "+ (int) radius+" meters. Do you still want to take pictures?")
                                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    cameraClick();
                                                }
                                            })
                                            .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                }
                                            });
                                    AlertDialog alert = builder.create();
                                    alert.show();
                                }
                            }
                        }
                    }
                    else {
                        System.out.println("PRE LOCATION NOT FOUND");
                        cameraClick();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Location Not Found",Toast.LENGTH_SHORT).show();
                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                project_no_to_update = Objects.requireNonNull(projectNo.getText()).toString();
                project_name_to_update = Objects.requireNonNull(projectName.getText()).toString();
                project_value_to_update = Objects.requireNonNull(projectValue.getText()).toString();
                pic_details_to_update = Objects.requireNonNull(projectPICDetails.getText()).toString();
                project_details_to_update = Objects.requireNonNull(projectDetails.getText()).toString();

                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(ProjectEdit.this);
                builder.setTitle("Update!")
                        .setMessage("Do you want update the current information according to this project?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                                new Check().execute();
                                updateProjectQuery(createRequest());
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();

            }
        });

        deleteGpxFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gpxFileName.setText("");
                gpxContent = "";
                locationListsCreate = new ArrayList<>();
                gpxFileLayout.setVisibility(View.GONE);
                addWaypoint.setVisibility(View.VISIBLE);
                addTrack.setVisibility(View.VISIBLE);
            }
        });

        addWaypoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent1 = new Intent(ProjectEdit.this, GpxCreationMap.class);
                intent1.putExtra("VALUE","WAYPOINT");
                intent1.putExtra("FROM","PROJECT_EDIT");
                startActivity(intent1);
            }
        });

        addTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent1 = new Intent(ProjectEdit.this, GpxCreationMap.class);
                intent1.putExtra("VALUE","TRACK");
                intent1.putExtra("FROM","PROJECT_EDIT");
                startActivity(intent1);
            }
        });

        showMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(ProjectEdit.this, ShowInMap.class);
                intent1.putExtra("P_NAME",P_NAME);
                intent1.putExtra("P_NO",P_NO);
                intent1.putExtra("P_CODE",P_CODE);
                intent1.putExtra("P_DATE",P_DATE);
                intent1.putExtra("ES_VAL",ES_VAL);
                intent1.putExtra("S_TYPE",S_TYPE);
                intent1.putExtra("F_YEAR",F_YEAR);
                startActivity(intent1);
            }
        });

        showImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(ProjectEdit.this, ShowImage.class);
                startActivity(intent1);
            }
        });

        closeKeyBoard();
    }

    private void cameraClick() {
        //Toast.makeText(getApplicationContext(), "Open Camera",Toast.LENGTH_SHORT).show();
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                System.out.println("PhotoFile: " + ex.getLocalizedMessage());
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getApplicationContext(),
                        "com.ttit.android.shuvoCameraProviderTR",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                try {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    Log.i("Activity:", "Shuru hoise");

                } catch (ActivityNotFoundException e) {
                    // display error state to the user
                    System.out.println("Activity: "+e.getLocalizedMessage());
                }

            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        fusedLocationProviderClientCamera.removeLocationUpdates(locationCallbackCamera);
        System.out.println("STOPPED");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sanctionCatLists.size() != 0 && sanctionSubCatLists.size() != 0) {
            try {
                gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                System.out.println(gps_enabled);
                Log.i("GPS", String.valueOf(gps_enabled));
            } catch(Exception ex) {
                ex.printStackTrace();
            }

            try {
                network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                System.out.println(network_enabled);
                Log.i("Network", String.valueOf(network_enabled));
            } catch(Exception ex) {
                ex.printStackTrace();
            }

            if(!gps_enabled && !network_enabled) {
                System.out.println(gps_enabled);
                Log.i("GPS1", String.valueOf(gps_enabled));
                Log.i("Network1", String.valueOf(network_enabled));
                System.out.println(network_enabled);
                // notify user
                enableGPS();

            } else {
                //Toast.makeText(getApplicationContext(), "OK", Toast.LENGTH_SHORT).show();
                if (ActivityCompat.checkSelfPermission(ProjectEdit.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ProjectEdit.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                fusedLocationProviderClientCamera.requestLocationUpdates(locationRequestCamera, locationCallbackCamera, Looper.getMainLooper());
                System.out.println("RESUMED");
            }
        }
        else {
//            new DataCheck().execute();
            getData();
        }

    }

    @Override
    public void onBackPressed() {
        MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(ProjectEdit.this)
                .setTitle("EXIT!")
                .setMessage("Do you leave this page without updating this project?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        gpxContent = "";
                        locationListsCreate = new ArrayList<>();
                        imageCapturedLists = new ArrayList<>();
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Do nothing
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        System.out.println(timeStamp);
        String imageFileName = "IMGLC_" + timeStamp + "_";
        System.out.println(imageFileName);
        Boolean exists = false;
        File file = null;
        //File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void closeKeyBoard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            view.clearFocus();
            InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void enableGPS() {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API).addConnectionCallbacks(ProjectEdit.this)
                    .addOnConnectionFailedListener(ProjectEdit.this).build();
            googleApiClient.connect();
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            // **************************
            builder.setAlwaysShow(true); // this is the key ingredient
            // **************************

            PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi
                    .checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    final LocationSettingsStates state = result
                            .getLocationSettingsStates();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.SUCCESS:
                            // All location settings are satisfied. The client can
                            // initialize location
                            // requests here.
                            Log.i("Exit", "3");
                            //info.setText("Done");
//                            if (ActivityCompat.checkSelfPermission(ProjectEdit.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ProjectEdit.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                                // TODO: Consider calling
//                                //    ActivityCompat#requestPermissions
//                                // here to request the missing permissions, and then overriding
//                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                                //                                          int[] grantResults)
//                                // to handle the case where the user grants the permission. See the documentation
//                                // for ActivityCompat#requestPermissions for more details.
//                                return;
//                            }
//                            fusedLocationProviderClientCamera.requestLocationUpdates(locationRequestCamera, locationCallbackCamera, Looper.getMainLooper());
                            break;
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            // Location settings are not satisfied. But could be
                            // fixed by showing the user
                            // a dialog.
                            Log.i("Exit", "4");
                            try {
                                // Show the dialog by calling
                                // startResolutionForResult(),
                                // and check the result in onActivityResult().
                                status.startResolutionForResult(ProjectEdit.this, 1000);
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // Location settings are not satisfied. However, we have
                            // no way to fix the
                            // settings so we won't show the dialog.
                            Log.i("Exit", "5");
                            break;
                    }
                }

            });


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("result");
                //info.setText("Done");
                if (ActivityCompat.checkSelfPermission(ProjectEdit.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                fusedLocationProviderClientCamera.requestLocationUpdates(locationRequestCamera, locationCallbackCamera, Looper.getMainLooper());
                Log.i("Hoise ", "1");
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
                Log.i("Hoise ", "2");
                finish();
//                System.exit(0);
            }
        }
        else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            double latitude = cameraLatLng[0].latitude;
            double longitude = cameraLatLng[0].longitude;

            // \n is for new line
            //Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();

            getAddress(latitude, longitude);

            // Getting ImageFile Name
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",Locale.getDefault()).format(new Date());
            System.out.println(timeStamp);
            imageFileName = "IMG_" + timeStamp;
            System.out.println(imageFileName);

            //fusedLocationProviderClient.removeLocationUpdates(locationCallback);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yy, hh:mm:ss a", Locale.getDefault());
            Date c = Calendar.getInstance().getTime();
            String dd = simpleDateFormat.format(c);
            System.out.println(dd);
            String timeLatLng = "Time: " + dd + "\n" + "Latitude: " + latitude + "\n" + "Longitude: " + longitude;
            address = timeLatLng + "\n"+ "Address: " + address;
            System.out.println(address);

            File imgFile = new  File(currentPhotoPath);
            if(imgFile.exists()) {
                //cameraImage.setImageURI(Uri.fromFile(imgFile));
                System.out.println(currentPhotoPath);

                firstBitmap = BitmapFactory.decodeFile(currentPhotoPath);
                try {
                    firstBitmap = modifyOrientation(firstBitmap, currentPhotoPath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Resources resources = getResources();
                float scale = resources.getDisplayMetrics().density;
                //Bitmap bitmap = BitmapFactory.decodeResource(resources, gResId);

                android.graphics.Bitmap.Config bitmapConfig = firstBitmap.getConfig();
                // set default bitmap config if none
                if(bitmapConfig == null) {
                    bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
                }
                // resource bitmaps are imutable,
                // so we need to convert it to mutable one
                firstBitmap = firstBitmap.copy(bitmapConfig, true);

                Canvas canvas = new Canvas(firstBitmap);

                // new antialiased Paint
                TextPaint paint=new TextPaint(Paint.ANTI_ALIAS_FLAG);
                // text color - #3D3D3D
                paint.setColor(Color.WHITE);
                // text size in pixels
                paint.setTextSize((int) (36 * scale));
                // text shadow
                paint.setShadowLayer(4f, 0f, 2f, Color.BLACK);
                paint.setFakeBoldText(true);

                // set text width to canvas width minus 16dp padding
                int textWidth = canvas.getWidth() - (int) (16 * scale);

                // init StaticLayout for text

                StaticLayout textLayout = new StaticLayout(
                        address, paint, textWidth, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);

                // get height of multiline text
                int textHeight = textLayout.getHeight();

                // get position of text's top left corner
                float x = (firstBitmap.getWidth() - textWidth)/2;
                float y = (firstBitmap.getHeight() - textHeight)/2;


                // draw text to the Canvas center
                int yyyy = firstBitmap.getHeight() - textHeight - 16;
                canvas.save();
                canvas.translate(5, yyyy);
                textLayout.draw(canvas);
                canvas.restore();

                ImageDialogue imageDialogue = new ImageDialogue();
                imageDialogue.show(getSupportFragmentManager(),"Image");
            }
        }
    }

    public static Bitmap modifyOrientation(Bitmap bitmap, String image_absolute_path) throws IOException {
        ExifInterface ei = new ExifInterface(image_absolute_path);
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotate(bitmap, 90);

            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotate(bitmap, 180);

            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotate(bitmap, 270);

            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                return flip(bitmap, true, false);

            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                return flip(bitmap, false, true);

            default:
                return bitmap;
        }
    }

    public static Bitmap rotate(Bitmap bitmap, float degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static Bitmap flip(Bitmap bitmap, boolean horizontal, boolean vertical) {
        Matrix matrix = new Matrix();
        matrix.preScale(horizontal ? -1 : 1, vertical ? -1 : 1);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public void getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(ProjectEdit.this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            String adds = obj.getAddressLine(0);
            String add = "Address from GeoCODE: ";
            add = add + "\n" + obj.getCountryName();
            add = add + "\n" + obj.getCountryCode();
            add = add + "\n" + obj.getAdminArea();
            add = add + "\n" + obj.getPostalCode();
            add = add + "\n" + obj.getSubAdminArea();
            add = add + "\n" + obj.getLocality();
            add = add + "\n" + obj.getSubThoroughfare();

            Log.v("IGA", "Address: " + add);
            Log.v("NEW ADD", "Address: " + adds);
            address = adds;
            // Toast.makeText(this, "Address=>" + add,
            // Toast.LENGTH_SHORT).show();

            // TennisAppActivity.showDialog(add);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            address = "Address Not Found";
//            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onCategoryClicked(int CategoryPosition) {

    }

    public ProjectRequest createRequest() {
        ProjectRequest p = new ProjectRequest();
        p.setPCM_PROJECT_NO(project_no_to_update);
        p.setPCM_PROJECT_DATE(P_DATE);
        p.setPCM_PROJECT_NAME(project_name_to_update);
        p.setSTART_DATE(START_DATE);
        p.setEND_DATE(END_DATE);
        p.setPROJECT_VALUE(project_value_to_update);
        p.setPCM_PROJECT_SANCTION_TYPE(S_TYPE_ID);
        p.setPCM_PSC_ID(PSC_SANCTION_CAT_ID);
        p.setPCM_PCM_ID(PCM_CATEGORY_ID);
        p.setCHAIRMAN_DETAILS(pic_details_to_update);
        p.setPROJECT_DETAILS(project_details_to_update);
        p.setPCM_ID(PCM_ID_PE);

        return p;
    }

    public void updateProjectQuery(ProjectRequest projectRequest) {
        waitProgress.show(getSupportFragmentManager(), "WaitBar");
        waitProgress.setCancelable(false);
        conn = false;
        connected = false;

        Call<ProjectResponse> projectResponseCall = ApiClient.getService().updateProject(projectRequest);
        projectResponseCall.enqueue(new Callback<ProjectResponse>() {
            @Override
            public void onResponse(Call<ProjectResponse> call, retrofit2.Response<ProjectResponse> response) {

                if (response.isSuccessful()) {
//                    System.out.println(response.body().getString_out());
                    connected = true;
                    updateGpxQuery();
                }
                else {
                    System.out.println("Failed");
                    conn = true;
                    connected = false;
                    updateInterface();
                }
            }

            @Override
            public void onFailure(Call<ProjectResponse> call, Throwable t) {
                System.out.println("Failed: ERROR");
                conn = true;
                connected = false;
                updateInterface();
            }
        });
    }

    public void updateGpxQuery() {
        gpxUploaded = false;
        gpxAvailable = false;
        String update_gpx_url = "http://103.56.208.123:8086/terrain/tr_kabikha/update_project/update_gpx_file";

        if (!gpxContent.isEmpty()) {
            gpxAvailable = true;
            RequestQueue requestQueue = Volley.newRequestQueue(ProjectEdit.this);
            String gpxName = gpxFileName.getText().toString();
            byte[] bArray = gpxContent.getBytes();

            StringRequest updateGpxRequest = new StringRequest(Request.Method.POST, update_gpx_url, response -> {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String string_out = jsonObject.getString("string_out");
                    if (string_out.equals("Successfully Created")) {
                        gpxUploaded = true;
                        updatePicQuery();
                    }
                    else {
                        conn = true;
                        gpxUploaded = false;
                        updateInterface();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    conn = true;
                    gpxUploaded = false;
                    updateInterface();
                }
            },error -> {
                conn = false;
                gpxUploaded = false;
                updateInterface();
            }) {
                @Override
                public byte[] getBody() throws AuthFailureError {
                    return bArray;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("PCM_ID",PCM_ID_PE);
                    headers.put("P_TYPE",P_TYPE);
                    headers.put("FILE_NAME",gpxName);
                    return headers;
                }

                @Override
                public String getBodyContentType() {
                    return "application/binary";
                }
            };

            requestQueue.add(updateGpxRequest);
        }
        else {
            updatePicQuery();
        }
    }

    public void updatePicQuery() {
        numberOfRequestsToMake = 0;
        hasRequestFailed = false;

        if (imageCapturedLists.size() != 0) {
            for (int i = 0; i < imageCapturedLists.size(); i++) {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                Bitmap bitmap = imageCapturedLists.get(i).getBitmap();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 60, bos);
                byte[] bArray = bos.toByteArray();
                numberOfRequestsToMake++;
                updatePicRequest(bArray,i);

            }
        }
        else {
            conn = true;
            updateInterface();
        }


    }

    public void updatePicRequest(byte[] bArray, int i) {
        String update_pic_url = "http://103.56.208.123:8086/terrain/tr_kabikha/update_project/update_picture";
        RequestQueue requestQueue = Volley.newRequestQueue(ProjectEdit.this);

        StringRequest updatePicRequest = new StringRequest(Request.Method.POST, update_pic_url, response -> {
            try {
                numberOfRequestsToMake--;
                JSONObject jsonObject = new JSONObject(response);
                String string_out = jsonObject.getString("string_out");
                if (string_out.equals("Successfully Created")) {
                    imageCapturedLists.get(i).setUploaded(true);
                }
                else {
                    hasRequestFailed = true;
                }

                if (numberOfRequestsToMake == 0) {
                    conn = true;
                    updateInterface();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                numberOfRequestsToMake--;
                hasRequestFailed = true;
                if(numberOfRequestsToMake == 0) {
                    //The last request failed
                    conn = true;
                    updateInterface();
                }
            }
        }, error -> {
            numberOfRequestsToMake--;
            hasRequestFailed = true;
            if(numberOfRequestsToMake == 0) {
                //The last request failed
                conn = false;
                updateInterface();
            }
        }) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                return bArray;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("PCM_ID",PCM_ID_PE);
                headers.put("FILE_NAME",imageCapturedLists.get(i).getFileName());
                headers.put("USER_NAME",picUserDetails.get(0).getUserName());
                headers.put("STAGE",imageCapturedLists.get(i).getStage());
                return headers;
            }

            @Override
            public String getBodyContentType() {
                return "application/binary";
            }
        };

        requestQueue.add(updatePicRequest);
    }

    public void updateInterface() {

        waitProgress.dismiss();
        if (conn) {
            if (connected) {
                int index = 0;
                boolean found = false;
                for (int i = 0; i < projectUpdateLists.size(); i++) {
                    if (INTERNAL_NO.equals(projectUpdateLists.get(i).getPcmInternalNo())) {
                        found = true;
                        index = i;
                        break;
                    }
                }
                if (found) {
                    projectUpdateLists.get(index).setPcmProjectNo(project_no_to_update);
                    projectUpdateLists.get(index).setPcmProjectDate(P_DATE);
                    projectUpdateLists.get(index).setPcmProjectName(project_name_to_update);
                    projectUpdateLists.get(index).setProjectStartDate(START_DATE);
                    projectUpdateLists.get(index).setProjectEndDate(END_DATE);
                    projectUpdateLists.get(index).setPcmEstimateProjectValue(project_value_to_update);
                    projectUpdateLists.get(index).setSanctionType(S_TYPE);
                    projectUpdateLists.get(index).setSanctionType_id(S_TYPE_ID);
                    projectUpdateLists.get(index).setPscSanctionCatId(PSC_SANCTION_CAT_ID);
                    projectUpdateLists.get(index).setPscSanctionCatName(SANC_CAT);
                    projectUpdateLists.get(index).setPcmCategoryId(PCM_CATEGORY_ID);
                    projectUpdateLists.get(index).setPcmCategoryName(CATEGORY);
                    projectUpdateLists.get(index).setPcmPicChairmanDetails(pic_details_to_update);
                    projectUpdateLists.get(index).setProjectDetails(project_details_to_update);
                    if (gpxAvailable) {
                        if (gpxUploaded) {
                            if(locationListsCreate.size() != 0) {
                                projectUpdateLists.get(index).setLocationLists(locationListsCreate);
                            }
                            gpxContent = "";
                            locationListsCreate = new ArrayList<>();

                            if (!hasRequestFailed) {
                                Toast.makeText(ProjectEdit.this, "Project Updated Successfully", Toast.LENGTH_SHORT).show();
                                imageCapturedLists = new ArrayList<>();
                                finish();
                            }
                            else {
                                int up = -1;
                                boolean upload = true;

                                while (upload) {
                                    for (int i = 0 ; i < imageCapturedLists.size(); i++) {
                                        if (imageCapturedLists.get(i).isUploaded()) {
                                            up = i;
                                            upload = true;
                                            break;
                                        }
                                        else {
                                            upload = false;
                                        }
                                    }
                                    if (upload) {
                                        imageCapturedLists.remove(up);
                                    }
                                }

                                AlertDialog dialog = new AlertDialog.Builder(ProjectEdit.this)
                                        .setMessage(imageCapturedLists.size() + " image files missed to upload")
                                        .setPositiveButton("Retry", null)
                                        .setNegativeButton("Cancel",null)
                                        .show();

                                dialog.setCancelable(false);
                                dialog.setCanceledOnTouchOutside(false);
                                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                                positive.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        updateProjectQuery(createRequest());
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
                        else {
                            AlertDialog dialog = new AlertDialog.Builder(ProjectEdit.this)
                                    .setMessage("Failed to Upload GPX File.")
                                    .setPositiveButton("Retry", null)
                                    .setNegativeButton("Cancel",null)
                                    .show();

                            dialog.setCancelable(false);
                            dialog.setCanceledOnTouchOutside(false);
                            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                            positive.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    updateProjectQuery(createRequest());
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
                    else {
                        if (!hasRequestFailed) {
                            Toast.makeText(ProjectEdit.this, "Project Updated Successfully", Toast.LENGTH_SHORT).show();
                            imageCapturedLists = new ArrayList<>();
                            finish();
                        }
                        else {
                            int up = -1;
                            boolean upload = true;

                            while (upload) {
                                for (int i = 0 ; i < imageCapturedLists.size(); i++) {
                                    if (imageCapturedLists.get(i).isUploaded()) {
                                        up = i;
                                        upload = true;
                                        break;
                                    }
                                    else {
                                        upload = false;
                                    }
                                }
                                if (upload) {
                                    imageCapturedLists.remove(up);
                                }
                            }

                            AlertDialog dialog = new AlertDialog.Builder(ProjectEdit.this)
                                    .setMessage(imageCapturedLists.size() + " image files missed to upload")
                                    .setPositiveButton("Retry", null)
                                    .setNegativeButton("Cancel",null)
                                    .show();

                            dialog.setCancelable(false);
                            dialog.setCanceledOnTouchOutside(false);
                            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                            positive.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    updateProjectQuery(createRequest());
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
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(ProjectEdit.this)
                        .setMessage("Failed to Update Data.")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel",null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        updateProjectQuery(createRequest());
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
        else {
            if (connected) {
                int index = 0;
                boolean found = false;
                for (int i = 0; i < projectUpdateLists.size(); i++) {
                    if (INTERNAL_NO.equals(projectUpdateLists.get(i).getPcmInternalNo())) {
                        found = true;
                        index = i;
                        break;
                    }
                }
                if (found) {
                    projectUpdateLists.get(index).setPcmProjectNo(project_no_to_update);
                    projectUpdateLists.get(index).setPcmProjectDate(P_DATE);
                    projectUpdateLists.get(index).setPcmProjectName(project_name_to_update);
                    projectUpdateLists.get(index).setProjectStartDate(START_DATE);
                    projectUpdateLists.get(index).setProjectEndDate(END_DATE);
                    projectUpdateLists.get(index).setPcmEstimateProjectValue(project_value_to_update);
                    projectUpdateLists.get(index).setSanctionType(S_TYPE);
                    projectUpdateLists.get(index).setSanctionType_id(S_TYPE_ID);
                    projectUpdateLists.get(index).setPscSanctionCatId(PSC_SANCTION_CAT_ID);
                    projectUpdateLists.get(index).setPscSanctionCatName(SANC_CAT);
                    projectUpdateLists.get(index).setPcmCategoryId(PCM_CATEGORY_ID);
                    projectUpdateLists.get(index).setPcmCategoryName(CATEGORY);
                    projectUpdateLists.get(index).setPcmPicChairmanDetails(pic_details_to_update);
                    projectUpdateLists.get(index).setProjectDetails(project_details_to_update);
                    if (gpxAvailable) {
                        if (gpxUploaded) {
                            if(locationListsCreate.size() != 0) {
                                projectUpdateLists.get(index).setLocationLists(locationListsCreate);
                            }

                            gpxContent = "";
                            locationListsCreate = new ArrayList<>();

                            if (hasRequestFailed) {
                                int up = -1;
                                boolean upload = true;

                                while (upload) {
                                    for (int i = 0 ; i < imageCapturedLists.size(); i++) {
                                        if (imageCapturedLists.get(i).isUploaded()) {
                                            up = i;
                                            upload = true;
                                            break;
                                        }
                                        else {
                                            upload = false;
                                        }
                                    }
                                    if (upload) {
                                        imageCapturedLists.remove(up);
                                    }
                                }

                                AlertDialog dialog = new AlertDialog.Builder(ProjectEdit.this)
                                        .setMessage(imageCapturedLists.size() + " image files missed to upload for Internet Connection. Please try again!")
                                        .setPositiveButton("Retry", null)
                                        .setNegativeButton("Cancel",null)
                                        .show();

                                dialog.setCancelable(false);
                                dialog.setCanceledOnTouchOutside(false);
                                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                                positive.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        updateProjectQuery(createRequest());
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
                        else {
                            AlertDialog dialog = new AlertDialog.Builder(ProjectEdit.this)
                                    .setMessage("Please Check Your Internet Connection")
                                    .setPositiveButton("Retry", null)
                                    .setNegativeButton("Exit",null)
                                    .show();

                            dialog.setCancelable(false);
                            dialog.setCanceledOnTouchOutside(false);
                            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                            positive.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    updateProjectQuery(createRequest());
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
                    else {
                        if (hasRequestFailed) {
                            int up = -1;
                            boolean upload = true;

                            while (upload) {
                                for (int i = 0 ; i < imageCapturedLists.size(); i++) {
                                    if (imageCapturedLists.get(i).isUploaded()) {
                                        up = i;
                                        upload = true;
                                        break;
                                    }
                                    else {
                                        upload = false;
                                    }
                                }
                                if (upload) {
                                    imageCapturedLists.remove(up);
                                }
                            }

                            AlertDialog dialog = new AlertDialog.Builder(ProjectEdit.this)
                                    .setMessage(imageCapturedLists.size() + " image files missed to upload for Internet Connection. Please try again!")
                                    .setPositiveButton("Retry", null)
                                    .setNegativeButton("Cancel",null)
                                    .show();

                            dialog.setCancelable(false);
                            dialog.setCanceledOnTouchOutside(false);
                            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                            positive.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    updateProjectQuery(createRequest());
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
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(ProjectEdit.this)
                        .setMessage("Please Check Your Internet Connection")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Exit",null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        updateProjectQuery(createRequest());
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

    // Getting Neccessary Data for Project Edit and Updating UI
    public void getData() {
        waitProgress.show(getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        conn = false;

        sanctionCatLists = new ArrayList<>();
        sanctionSubCatLists = new ArrayList<>();

        String sanc_cat_url = "http://103.56.208.123:8086/terrain/tr_kabikha/utility_data/sanction_cat_lists";
        String pcm_cat_url = "http://103.56.208.123:8086/terrain/tr_kabikha/utility_data/pcm_category_lists";

        RequestQueue requestQueue = Volley.newRequestQueue(ProjectEdit.this);

        StringRequest pcmCatRequest = new StringRequest(Request.Method.GET, pcm_cat_url, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray jsonArray = new JSONArray(items);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject pcmCatObject = jsonArray.getJSONObject(i);
                        String pcm_id = pcmCatObject.getString("pcm_id");
                        String pcm_category_name = pcmCatObject.getString("pcm_category_name");
                        pcm_category_name = transformText(pcm_category_name);

                        sanctionSubCatLists.add(new ChoiceList(pcm_id,pcm_category_name));
                    }
                }
                conn = true;
                updateUI();
            } catch (JSONException e) {
                e.printStackTrace();
                conn = false;
                updateUI();
            }
        }, error -> {
            conn = false;
            updateUI();
        });

        StringRequest sancCatRequest = new StringRequest(Request.Method.GET, sanc_cat_url, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray jsonArray = new JSONArray(items);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject sancCatObject = jsonArray.getJSONObject(i);
                        String psc_id = sancCatObject.getString("psc_id");
                        String psc_sanction_cat_name = sancCatObject.getString("psc_sanction_cat_name");
                        psc_sanction_cat_name = transformText(psc_sanction_cat_name);

                        sanctionCatLists.add(new ChoiceList(psc_id,psc_sanction_cat_name));
                    }
                }
                requestQueue.add(pcmCatRequest);
            } catch (JSONException e) {
                e.printStackTrace();
                conn = false;
                updateUI();
            }
        }, error -> {
            conn = false;
            updateUI();
        });

        requestQueue.add(sancCatRequest);
    }

    public void updateUI() {
        waitProgress.dismiss();
        if (conn) {

            conn = false;

            sanctionCat.setText(SANC_CAT);

            ArrayList<String> type = new ArrayList<>();
            for(int i = 0; i < sanctionCatLists.size(); i++) {
                type.add(sanctionCatLists.get(i).getName());
            }
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type);

            sanctionCat.setAdapter(arrayAdapter);

            sanctionSubCat.setText(CATEGORY);

            ArrayList<String> type1 = new ArrayList<>();
            for(int i = 0; i < sanctionSubCatLists.size(); i++) {
                type1.add(sanctionSubCatLists.get(i).getName());
            }
            ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(getApplicationContext(),R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type1);

            sanctionSubCat.setAdapter(arrayAdapter1);

            try {
                gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                System.out.println(gps_enabled);
                Log.i("GPS", String.valueOf(gps_enabled));
            } catch(Exception ex) {
                ex.printStackTrace();
            }

            try {
                network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                System.out.println(network_enabled);
                Log.i("Network", String.valueOf(network_enabled));
            } catch(Exception ex) {
                ex.printStackTrace();
            }

            if(!gps_enabled && !network_enabled) {
                System.out.println(gps_enabled);
                Log.i("GPS1", String.valueOf(gps_enabled));
                Log.i("Network1", String.valueOf(network_enabled));
                System.out.println(network_enabled);
                // notify user
                enableGPS();

            } else {
                //Toast.makeText(getApplicationContext(), "OK", Toast.LENGTH_SHORT).show();
                if (ActivityCompat.checkSelfPermission(ProjectEdit.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ProjectEdit.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                fusedLocationProviderClientCamera.requestLocationUpdates(locationRequestCamera, locationCallbackCamera, Looper.getMainLooper());
                System.out.println("RESUMED");
            }

        }
        else {
            AlertDialog dialog = new AlertDialog.Builder(ProjectEdit.this)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("EXIT",null)
                    .show();

            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    getData();
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

    //    --------------------------Transforming Bangla Text-----------------------------
    private String transformText(String text) {
        byte[] bytes = text.getBytes(ISO_8859_1);
        return new String(bytes, UTF_8);
    }
}