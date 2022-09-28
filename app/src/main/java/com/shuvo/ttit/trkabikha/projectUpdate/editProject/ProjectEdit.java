package com.shuvo.ttit.trkabikha.projectUpdate.editProject;

import static com.shuvo.ttit.trkabikha.adapter.ProjectUpdateAdapter.locationListsAdapterPU;
import static com.shuvo.ttit.trkabikha.connection.OracleConnection.createConnection;
import static com.shuvo.ttit.trkabikha.login.Login.userInfoLists;
import static com.shuvo.ttit.trkabikha.mainmenu.HomePage.projectUpdateLists;

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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.MediaStore;
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
import com.shuvo.ttit.trkabikha.dialogue.ImageDialogue;
import com.shuvo.ttit.trkabikha.gpxCreation.GpxCreationMap;
import com.shuvo.ttit.trkabikha.mainmenu.HomePage;
import com.shuvo.ttit.trkabikha.progressbar.WaitProgress;
import com.shuvo.ttit.trkabikha.projectUpdate.editProject.showMap.ShowInMap;
import com.shuvo.ttit.trkabikha.projectUpdate.editProject.showPicture.ShowImage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

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

    WaitProgress waitProgress = new WaitProgress();
    private String message = null;
    private Boolean conn = false;
    private Boolean connected = false;

    private Connection connection;

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
    boolean databaseImage = false;

    String project_no_to_update = "";
    String project_name_to_update = "";
    String project_value_to_update = "";
    String pic_details_to_update = "";
    String project_details_to_update = "";

    CardView showMap;
    CardView showImage;

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
        projectPICDetails.setText(PIC_DET);
        projectDetails.setText(P_DETAILS);

        if (locationListsAdapterPU.size() != 0) {
            mapTaken.setVisibility(View.VISIBLE);
            showMap.setVisibility(View.VISIBLE);
        }
        else {
            mapTaken.setVisibility(View.GONE);
            showMap.setVisibility(View.GONE);
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
                                new Check().execute();
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
                startActivity(intent1);
            }
        });

        addTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent1 = new Intent(ProjectEdit.this, GpxCreationMap.class);
                intent1.putExtra("VALUE","TRACK");
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
            new DataCheck().execute();
        }

    }

    @Override
    public void onBackPressed() {
        gpxContent = "";
        locationListsCreate = new ArrayList<>();
        imageCapturedLists = new ArrayList<>();
        super.onBackPressed();

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
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
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

    public class Check extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            waitProgress.show(getSupportFragmentManager(), "WaitBar");
            waitProgress.setCancelable(false);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (isConnected() && isOnline()) {

                Query();
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
                    projectUpdateLists.get(index).setLocationLists(locationListsCreate);
                }
                Toast.makeText(ProjectEdit.this, "Project Updated Successfully", Toast.LENGTH_SHORT).show();
                gpxContent = "";
                locationListsCreate = new ArrayList<>();
                imageCapturedLists = new ArrayList<>();
                finish();

                conn = false;
                connected = false;

            } else {
                int up = 0;
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
                if (gpxUploaded) {
                    Toast.makeText(getApplicationContext(), imageCapturedLists.size() + "image files missed to upload", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "GPX file and "+imageCapturedLists.size() + "image files missed to upload", Toast.LENGTH_SHORT).show();
                }

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

                        new Check().execute();
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

    public class DataCheck extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            waitProgress.show(getSupportFragmentManager(), "WaitBar");
            waitProgress.setCancelable(false);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (isConnected() && isOnline()) {

                DataQuery();
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

                conn = false;
                connected = false;

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

                if (databaseImage) {
                    imageTaken.setVisibility(View.VISIBLE);
                    showImage.setVisibility(View.VISIBLE);
                }
                else {
                    imageTaken.setVisibility(View.GONE);
                    showImage.setVisibility(View.GONE);
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

            } else {
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

                        new DataCheck().execute();
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

    private void Query() {
        try {
            this.connection = createConnection();


            Statement statement = connection.createStatement();

            statement.executeUpdate("UPDATE PROJECT_CREATION_MST \n" +
                    "SET PCM_PROJECT_NO = '"+project_no_to_update+"',\n" +
                    "    PCM_PROJECT_DATE = TO_DATE('"+P_DATE+"','DD-MON-YY'),\n" +
                    "    PCM_PROJECT_NAME = '"+project_name_to_update+"',\n" +
                    "    PCM_ESTIMATE_START_DATE = TO_DATE('"+START_DATE+"','DD-MON-YY'),\n" +
                    "    PCM_ESTIMATE_END_DATE = TO_DATE('"+END_DATE+"','DD-MON-YY'),\n" +
                    "    PCM_ESTIMATE_PROJECT_VALUE = "+project_value_to_update+",\n" +
                    "    PCM_PROJECT_SANCTION_TYPE = "+S_TYPE_ID+",\n" +
                    "    PCM_PSC_ID = "+PSC_SANCTION_CAT_ID+",\n" +
                    "    PCM_PCM_ID = "+PCM_CATEGORY_ID+",\n" +
                    "    PCM_PIC_CHAIRMAN_DETAILS = '"+pic_details_to_update+"',\n" +
                    "    PCM_PROJECT_DETAILS = '"+project_details_to_update+"'\n" +
                    "WHERE PCM_ID = "+PCM_ID_PE+"");

            if (!gpxContent.isEmpty()) {
                String gpxName = gpxFileName.getText().toString();
                byte[] bArray = gpxContent.getBytes();
                InputStream stream = new ByteArrayInputStream(bArray);

                CallableStatement callableStatement1 = connection.prepareCall("{call androaid_gpx_file_process(?,?,?,?,?)}");
                callableStatement1.setInt(1,Integer.parseInt(PCM_ID_PE));
                callableStatement1.setString(2,P_TYPE);
                callableStatement1.setString(3,"Default from Android");
                callableStatement1.setString(4,gpxName);
                callableStatement1.setBinaryStream(5,stream,bArray.length);
                callableStatement1.execute();

                callableStatement1.close();
                gpxUploaded = true;
            }

            for (int i = 0; i < imageCapturedLists.size(); i++) {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                Bitmap bitmap = imageCapturedLists.get(i).getBitmap();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                byte[] bArray = bos.toByteArray();
                InputStream in = new ByteArrayInputStream(bArray);

//                CallableStatement callableStatement1 = connection.prepareCall("{call write_to_file(?,?,?)}");
//                callableStatement1.setString(1,"GENERATED_FROM_APPS_"+i+".jpg");
//                callableStatement1.setString(2, "A2I_PDDP_DOC_ARCH");
//                callableStatement1.setBinaryStream(3,in,bArray.length);
//                callableStatement1.execute();
//
//                callableStatement1.close();

                CallableStatement callableStatement1 = connection.prepareCall("{call androaid_proj_pic_upl_process(?,?,?,?)}");
                callableStatement1.setInt(1,Integer.parseInt(PCM_ID_PE));
                callableStatement1.setString(2, imageCapturedLists.get(i).getFileName());
                callableStatement1.setString(3,userInfoLists.get(0).getUserName());
                callableStatement1.setBinaryStream(4,in,bArray.length);
                callableStatement1.execute();

                callableStatement1.close();
                imageCapturedLists.get(i).setUploaded(true);
            }

            connected = true;

            connection.close();

        }
        catch (Exception e) {

            Log.i("ERRRRR", e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    private void DataQuery() {
        try {
            this.connection = createConnection();

            Statement stmt = connection.createStatement();

            sanctionCatLists = new ArrayList<>();
            sanctionSubCatLists = new ArrayList<>();

            ResultSet resultSet = stmt.executeQuery("Select psc_id, psc_sanction_cat_name from project_sanction_category where psc_active_flag is NULL");

            while (resultSet.next()) {
                sanctionCatLists.add(new ChoiceList(resultSet.getString(1),resultSet.getString(2)));
            }
            resultSet.close();

            ResultSet resultSet1 = stmt.executeQuery("Select pcm_id, pcm_category_name from project_category_mst where pcm_active_flag = 0");

            while (resultSet1.next()) {
                sanctionSubCatLists.add(new ChoiceList(resultSet1.getString(1),resultSet1.getString(2)));
            }

            resultSet1.close();

            ResultSet resultSet2 = stmt.executeQuery("select * from uploaded_docs where ud_pcm_id = "+PCM_ID_PE+"");

            while (resultSet2.next()) {
                databaseImage = true;
            }

            resultSet2.close();

            connected = true;

            connection.close();

        }
        catch (Exception e) {

            Log.i("ERRRRR", e.getLocalizedMessage());
            e.printStackTrace();
        }
    }
}