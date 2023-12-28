package com.shuvo.ttit.trkabikha.projectCreation;

import static com.shuvo.ttit.trkabikha.login.PICLogin.picUserDetails;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_8;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.core.widget.NestedScrollView;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
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
import com.google.android.material.textfield.TextInputLayout;
import com.rosemaryapp.amazingspinner.AmazingSpinner;
import com.shuvo.ttit.trkabikha.R;
import com.shuvo.ttit.trkabikha.adapter.ImageCapturedAdapter;
import com.shuvo.ttit.trkabikha.adapter.SelectedVillageAdapter;
import com.shuvo.ttit.trkabikha.adapter.SelectedWardAdapter;
import com.shuvo.ttit.trkabikha.arraylist.ChoiceList;
import com.shuvo.ttit.trkabikha.arraylist.FinancialYearLists;
import com.shuvo.ttit.trkabikha.arraylist.ImageCapturedList;
import com.shuvo.ttit.trkabikha.arraylist.LocationLists;
import com.shuvo.ttit.trkabikha.arraylist.ProjectSubTypeLists;
import com.shuvo.ttit.trkabikha.arraylist.ProjectTypeLists;
import com.shuvo.ttit.trkabikha.arraylist.SelectedVillageList;
import com.shuvo.ttit.trkabikha.arraylist.SelectedWardList;
import com.shuvo.ttit.trkabikha.arraylist.SourceFundLists;
import com.shuvo.ttit.trkabikha.arraylist.UnionLists;
import com.shuvo.ttit.trkabikha.connection.retrofit.ApiClient;
import com.shuvo.ttit.trkabikha.connection.retrofit.ProjectResponse;
import com.shuvo.ttit.trkabikha.dialogue.ImageDialoguePC;
import com.shuvo.ttit.trkabikha.dialogue.SetVillageDialogue;
import com.shuvo.ttit.trkabikha.dialogue.SetWardDialogue;
import com.shuvo.ttit.trkabikha.gpxCreation.GpxCreationMap;
import com.shuvo.ttit.trkabikha.progressbar.WaitProgress;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
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
import retrofit2.Response;

public class CreateProject extends AppCompatActivity implements ImageCapturedAdapter.ClickedItem, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    NestedScrollView scrollView;
    TextInputEditText entryDate;
    TextInputEditText projectNo;
    TextInputEditText approvalDate;
    TextInputEditText projectName;
    TextInputEditText startDate;
    TextInputEditText endDate;
    TextInputEditText projectValue;
    AmazingSpinner projectValueType;
    AmazingSpinner sanctionCat;
    AmazingSpinner sanctionSubCat;
    AmazingSpinner financialYear;
    AmazingSpinner sourceOfFund;
    AmazingSpinner projectType;
    TextInputLayout projectTypeLay;
    AmazingSpinner projectSubType;
    TextInputLayout projectSubTypeLay;
    TextInputEditText projectPICDetails;
    TextInputEditText projectDetails;

    AmazingSpinner projectUpazilla;
    TextInputLayout projectUpazillaLay;
    AmazingSpinner projectUnion;
    LinearLayout wardLayout;
    Button ward;
    RecyclerView wardView;
    RecyclerView.LayoutManager wardLayoutManager;
    public static SelectedWardAdapter selectedWardAdapter;

    public static LinearLayout villageLayout;
    Button village;
    RecyclerView villageView;
    RecyclerView.LayoutManager villageLayoutManager;
    public static SelectedVillageAdapter selectedVillageAdapter;

    Button createProject;

    String pcm_user = "";
    String entry_date = "";
    String project_no = "";
    String approval_date = "";
    String project_name = "";
    String start_date = "";
    String end_date = "";
    String project_value = "";
    String project_value_type_id = "";
    String project_value_type_name = "";
    String sanction_category_id = "";
    String sanction_sub_cat_id = "";
    String financial_year_id = "";
    String source_of_fund_id = "";
    String project_type_id = "";
    String project_sub_type_id = "";
    String project_pic_details = "";
    String project_details = "";

    String pcm_internal_no = "";

    String p_type_name = "";

    String dd_id = "";
    String dd_name = "";
    public static String ddu_id = "";

    ArrayList<ChoiceList> projectValueTypeLists;
    ArrayList<ChoiceList> sanctionCatLists;
    ArrayList<ChoiceList> sanctionSubCatLists;
    ArrayList<FinancialYearLists> financialYearLists;
    ArrayList<SourceFundLists> sourceFundLists;
    ArrayList<ProjectTypeLists> projectTypeLists;
    ArrayList<ProjectSubTypeLists> projectSubTypeLists;

    ArrayList<UnionLists> unionLists;
    public static ArrayList<SelectedWardList> selectedWardLists;
    public static ArrayList<SelectedVillageList> selectedVillageLists;

    private int mYear, mMonth, mDay;

    WaitProgress waitProgress = new WaitProgress();
    private Boolean conn = false;

    boolean firstStage = false;
    boolean secondStage = false;
    boolean thirdStage = false;

    Button imageCaptureButton;
    private GoogleApiClient googleApiClient;

    FusedLocationProviderClient fusedLocationProviderClientCamera;
    LocationRequest locationRequestCamera;
    LocationCallback locationCallbackCamera;
    LocationManager locationManager;
    public static String currentPhotoPath_pc;
    static final int REQUEST_IMAGE_CAPTURE_PC = 1;
    public static String imageFileName_pc = "";
    final LatLng[] cameraLatLng_pc = {null};
    public static Location targetLocation_pc = null;
    String address = "";
    public static Bitmap firstBitmap_pc = null;

    RecyclerView imageCapturedview;
    RecyclerView.LayoutManager layoutManager;
    @SuppressLint("StaticFieldLeak")
    public static ImageCapturedAdapter imageCapturedAdapter_pc;

    public static ArrayList<ImageCapturedList> imageCapturedListsPC;

    boolean gps_enabled = false;
    boolean network_enabled = false;
    boolean gpxUploaded = false;

    public static final String XML_HEADER_PC = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>";
    public static final String TAG_GPX_PC = "<gpx"
            + " xmlns=\"http://www.topografix.com/GPX/1/1\""
            + " version=\"1.1\""
            + " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\""
            + " xsi:schemaLocation=\"http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd\">";


    public static String gpxContent_pc = "";
    public static ArrayList<LocationLists> locationListsCreatePC;

    public static RelativeLayout gpxFileLayout_pc;
    public static TextView gpxFileName_pc;
    ImageView deleteGpxFile_pc;

    public static Button addWaypoint_pc;
    public static Button addTrack_pc;

    private int numberOfLocationRequestsToMake;
    private boolean hasLocationRequestFailed = false;

    private int numberOfRequestsToMake;
    private boolean hasRequestFailed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_project);

        scrollView = findViewById(R.id.project_creation_scroll_view);
        entryDate = findViewById(R.id.project_entry_date_project_creation);
        projectNo = findViewById(R.id.project_no_project_creation);
        approvalDate = findViewById(R.id.approval_date_project_creation);
        projectName = findViewById(R.id.project_name_project_creation);
        startDate = findViewById(R.id.project_start_date_project_creation);
        endDate = findViewById(R.id.project_end_date_project_creation);
        projectValue = findViewById(R.id.project_value_project_creation);
        projectValueType = findViewById(R.id.project_value_type_spinner_project_creation);
        sanctionCat = findViewById(R.id.project_sanction_cat_spinner_project_creation);
        sanctionSubCat = findViewById(R.id.project_sanction_sub_cat_spinner_project_creation);
        financialYear = findViewById(R.id.financial_year_spinner_project_creation);
        sourceOfFund = findViewById(R.id.source_fund_spinner_project_creation);
        projectType = findViewById(R.id.project_type_spinner_project_creation);
        projectTypeLay = findViewById(R.id.project_type_spinner_layout_project_creation);
        projectTypeLay.setEnabled(false);
        projectSubType = findViewById(R.id.project_sub_type_spinner_project_creation);
        projectSubTypeLay = findViewById(R.id.project_sub_type_spinner_layout_project_creation);
        projectSubTypeLay.setEnabled(false);
        projectPICDetails = findViewById(R.id.project_pic_contractor_details_project_creation);
        projectDetails = findViewById(R.id.project_details_project_creation);

        projectUpazilla = findViewById(R.id.project_upazilla_spinner_project_creation);
        projectUpazillaLay = findViewById(R.id.project_upazilla_spinner_layout_project_creation);
        projectUpazillaLay.setEnabled(false);
        projectUnion = findViewById(R.id.project_union_spinner_project_creation);
        wardLayout = findViewById(R.id.ward_layout_p_crtn);
        wardLayout.setVisibility(View.GONE);
        villageLayout = findViewById(R.id.village_layout_p_crtn);
        villageLayout.setVisibility(View.GONE);
        ward = findViewById(R.id.button_for_set_ward_p_crtn);
        wardView = findViewById(R.id.selected_ward_list_view_p_crtn);
        village = findViewById(R.id.button_for_set_village_p_crtn);
        villageView = findViewById(R.id.selected_village_list_view_p_crtn);

        imageCaptureButton = findViewById(R.id.first_picture_p_crtn);
        imageCapturedview = findViewById(R.id.image_captured_list_view_p_crtn);
        gpxFileLayout_pc = findViewById(R.id.gpx_file_name_layout_p_crtn);
        gpxFileLayout_pc.setVisibility(View.GONE);
        gpxFileName_pc = findViewById(R.id.gpx_file_name_p_crtn);
        gpxFileName_pc.setText("");
        deleteGpxFile_pc = findViewById(R.id.delete_gpx_file_p_crtn);
        addWaypoint_pc = findViewById(R.id.button_for_waypoint_p_crtn);
        addTrack_pc = findViewById(R.id.button_for_track_p_crtn);

        createProject = findViewById(R.id.project_creation_button);

        fusedLocationProviderClientCamera = LocationServices.getFusedLocationProviderClient(this);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationRequestCamera = LocationRequest.create();
        locationRequestCamera.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequestCamera.setInterval(5000);
        locationRequestCamera.setFastestInterval(1000);

        projectValueTypeLists = new ArrayList<>();
        sanctionCatLists = new ArrayList<>();
        sanctionSubCatLists = new ArrayList<>();
        financialYearLists = new ArrayList<>();
        sourceFundLists = new ArrayList<>();
        projectTypeLists = new ArrayList<>();
        projectSubTypeLists = new ArrayList<>();
        imageCapturedListsPC = new ArrayList<>();
        gpxContent_pc = "";
        locationListsCreatePC = new ArrayList<>();

        unionLists = new ArrayList<>();
        selectedWardLists = new ArrayList<>();
        selectedVillageLists = new ArrayList<>();

        pcm_user = picUserDetails.get(0).getUserName();
        Intent intent = getIntent();
        dd_id = intent.getStringExtra("UPAZILLA");
        dd_name = intent.getStringExtra("UPAZILLA_NAME");

        wardView.setHasFixedSize(true);
        wardLayoutManager = new LinearLayoutManager(this);
        wardView.setLayoutManager(wardLayoutManager);
        selectedWardAdapter = new SelectedWardAdapter(selectedWardLists,CreateProject.this);
        wardView.setAdapter(selectedWardAdapter);

        villageView.setHasFixedSize(true);
        villageLayoutManager = new LinearLayoutManager(this);
        villageView.setLayoutManager(villageLayoutManager);
        selectedVillageAdapter = new SelectedVillageAdapter(selectedVillageLists,CreateProject.this);
        villageView.setAdapter(selectedVillageAdapter);

        imageCapturedview.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        imageCapturedview.setLayoutManager(layoutManager);
        imageCapturedAdapter_pc = new ImageCapturedAdapter(CreateProject.this,imageCapturedListsPC,CreateProject.this);
        imageCapturedview.setAdapter(imageCapturedAdapter_pc);


        // Project Data Setup
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yy", Locale.getDefault());
        entry_date = simpleDateFormat.format(date);
        entry_date = entry_date.toUpperCase(Locale.ROOT);

        entryDate.setText(entry_date);

        approvalDate.setOnClickListener(v -> {
            closeKeyBoard();
            final Calendar c = Calendar.getInstance();
            if(approval_date.isEmpty()) {
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
            }
            else {
                SimpleDateFormat simpleDateFormat12 = new SimpleDateFormat("dd-MMM-yy",Locale.getDefault());
                Date date12 = null;
                try {
                    date12 = simpleDateFormat12.parse(approval_date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (date12 != null) {
                    c.setTime(date12);
                }

                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
            }

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(CreateProject.this, new DatePickerDialog.OnDateSetListener() {
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
                        String a_d = dayOfMonthName + "-" + monthName + "-" + yearName;
                        approvalDate.setText(a_d);
                        approval_date = Objects.requireNonNull(approvalDate.getText()).toString();
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyBoard();
                String aD = Objects.requireNonNull(approvalDate.getText()).toString();
                if (aD.isEmpty()) {
                    Toast.makeText(getApplicationContext(),"Please Select Approval Date First.",Toast.LENGTH_SHORT).show();
                }
                else {
                    final Calendar c = Calendar.getInstance();
                    if(start_date.isEmpty()) {
                        mYear = c.get(Calendar.YEAR);
                        mMonth = c.get(Calendar.MONTH);
                        mDay = c.get(Calendar.DAY_OF_MONTH);
                    }
                    else {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yy",Locale.getDefault());
                        Date date = null;
                        try {
                            date = simpleDateFormat.parse(start_date);
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
                        DatePickerDialog datePickerDialog = new DatePickerDialog(CreateProject.this, new DatePickerDialog.OnDateSetListener() {
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
                                String s_d = dayOfMonthName + "-" + monthName + "-" + yearName;
                                start_date = s_d;
                                Date sDate = null;
                                Date aDate = null;

                                SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy", Locale.getDefault());

                                try {
                                    sDate = sdf.parse(start_date);
                                    aDate = sdf.parse(approval_date);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                if (sDate != null && aDate != null) {
                                    if (sDate.after(aDate)) {
                                        startDate.setText(s_d);
                                        start_date = Objects.requireNonNull(startDate.getText()).toString();
                                    }
                                    else {
                                        startDate.setText("");
                                        start_date = "";
                                        Toast.makeText(getApplicationContext(), "Project Start Date can not be before Approval Date!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }, mYear, mMonth, mDay);
                        datePickerDialog.show();
                    }
                }
            }
        });

        endDate.setOnClickListener(v -> {
            closeKeyBoard();
            if (start_date.isEmpty()) {
                Toast.makeText(getApplicationContext(),"Please Select Project Start Date First.",Toast.LENGTH_SHORT).show();
            }
            else {
                final Calendar c = Calendar.getInstance();
                if(end_date.isEmpty()) {
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);
                }
                else {
                    SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd-MMM-yy",Locale.getDefault());
                    Date date1 = null;
                    try {
                        date1 = simpleDateFormat1.parse(end_date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (date1 != null) {
                        c.setTime(date1);
                    }

                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);
                }

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    DatePickerDialog datePickerDialog = new DatePickerDialog(CreateProject.this, (view, year, month, dayOfMonth) -> {

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
                        String e_d = dayOfMonthName + "-" + monthName + "-" + yearName;
                        end_date = e_d;

                        Date sDate = null;
                        Date eDate = null;

                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy", Locale.getDefault());

                        try {
                            sDate = sdf.parse(start_date);
                            eDate = sdf.parse(end_date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        if (sDate != null && eDate != null) {
                            if (eDate.after(sDate)) {
                                endDate.setText(e_d);
                                end_date = Objects.requireNonNull(endDate.getText()).toString();
                            }
                            else {
                                endDate.setText("");
                                end_date = "";
                                Toast.makeText(getApplicationContext(), "Project End Date can not be before Project Start Date!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, mYear, mMonth, mDay);
                    datePickerDialog.show();
                }
            }
        });

        projectValueTypeLists.add(new ChoiceList("0", "Taka(টাকা)"));
        projectValueTypeLists.add(new ChoiceList("1","Rice(চাল) (MT)"));
        projectValueTypeLists.add(new ChoiceList("2","Wheat(গম) (MT)"));

        ArrayList<String> type = new ArrayList<>();
        for(int i = 0; i < projectValueTypeLists.size(); i++) {
            type.add(projectValueTypeLists.get(i).getName());
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type);

        projectValueType.setAdapter(arrayAdapter);

        projectValueType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeKeyBoard();
            }
        });
        projectValueType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String name = adapterView.getItemAtPosition(i).toString();
                for (int j = 0; j < projectValueTypeLists.size(); j++) {
                    if (name.equals(projectValueTypeLists.get(j).getName())) {
                        project_value_type_id = (projectValueTypeLists.get(j).getId());
                        project_value_type_name = name;
                    }
                }
                System.out.println(project_value_type_id);
            }
        });

        sanctionCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeKeyBoard();
            }
        });

        sanctionCat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String name = adapterView.getItemAtPosition(i).toString();
                for (int j = 0; j < sanctionCatLists.size(); j++) {
                    if (name.equals(sanctionCatLists.get(j).getName())) {
                        sanction_category_id = (sanctionCatLists.get(j).getId());
                        //SANC_CAT = name;
                    }
                }
                System.out.println(name);
                System.out.println(sanction_category_id);

            }
        });

        sanctionSubCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeKeyBoard();
            }
        });

        sanctionSubCat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String name = adapterView.getItemAtPosition(i).toString();
                for (int j = 0; j < sanctionSubCatLists.size(); j++) {
                    if (name.equals(sanctionSubCatLists.get(j).getName())) {
                        sanction_sub_cat_id = (sanctionSubCatLists.get(j).getId());
                        //CATEGORY = name;
                    }
                }
                System.out.println(name);
                System.out.println(sanction_sub_cat_id);

            }
        });

        financialYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeKeyBoard();
            }
        });

        financialYear.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String name = adapterView.getItemAtPosition(i).toString();
                for (int j = 0; j < financialYearLists.size(); j++) {
                    if (name.equals(financialYearLists.get(j).getFinancialYearName())) {
                        financial_year_id = (financialYearLists.get(j).getFyId());
                    }
                }
                System.out.println(name);
                System.out.println(financial_year_id);

            }
        });

        sourceOfFund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeKeyBoard();
            }
        });

        sourceOfFund.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                project_type_id = "";
                projectType.setText("");
                projectTypeLay.setEnabled(false);
                project_sub_type_id = "";
                projectSubType.setText("");
                projectSubTypeLay.setEnabled(false);
                String name = adapterView.getItemAtPosition(i).toString();
                for (int j = 0; j < sourceFundLists.size(); j++) {
                    if (name.equals(sourceFundLists.get(j).getFundName())) {
                        source_of_fund_id = (sourceFundLists.get(j).getFsmId());
                    }
                }
                System.out.println(name);
                System.out.println(source_of_fund_id);

//                new ProjectTypeCheck().execute();
                getProjectType();

            }
        });

        projectType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeKeyBoard();
            }
        });

        projectType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                project_sub_type_id = "";
                projectSubType.setText("");
                projectSubTypeLay.setEnabled(false);

                String name = adapterView.getItemAtPosition(i).toString();
                for (int j = 0; j < projectTypeLists.size(); j++) {
                    if (name.equals(projectTypeLists.get(j).getProjectTypeName())) {
                        project_type_id = (projectTypeLists.get(j).getPtmId());

                    }
                }
                p_type_name = name;
                System.out.println(name);
                System.out.println(project_type_id);

//                new ProjectSubTypeCheck().execute();
                getProjectSubType();

            }
        });

        projectSubType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeKeyBoard();
            }
        });

        projectSubType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String name = adapterView.getItemAtPosition(i).toString();
                for (int j = 0; j < projectSubTypeLists.size(); j++) {
                    if (name.equals(projectSubTypeLists.get(j).getProjectSubTypeName())) {
                        project_sub_type_id = (projectSubTypeLists.get(j).getPtdId());
                    }
                }
                System.out.println(project_sub_type_id);

            }
        });


        // Location Setup
        projectUpazilla.setText(dd_name);

        projectUnion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeKeyBoard();
            }
        });

        projectUnion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String name = adapterView.getItemAtPosition(i).toString();
                for (int j = 0; j < unionLists.size(); j++) {
                    if (name.equals(unionLists.get(j).getUnionName())) {
                        ddu_id = (unionLists.get(j).getDduId());
                    }
                }
                System.out.println(ddu_id);

                selectedWardLists.clear();
                selectedVillageLists.clear();
                System.out.println(selectedWardLists.size());
                selectedWardAdapter.notifyDataSetChanged();
                selectedVillageAdapter.notifyDataSetChanged();

                wardLayout.setVisibility(View.VISIBLE);
                villageLayout.setVisibility(View.GONE);

            }
        });

        ward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetWardDialogue setWardDialogue = new SetWardDialogue();
                setWardDialogue.show(getSupportFragmentManager(),"WARD");
            }
        });

        village.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetVillageDialogue setVillageDialogue = new SetVillageDialogue();
                setVillageDialogue.show(getSupportFragmentManager(),"VILLAGE");
            }
        });


        //Image Location
        locationCallbackCamera = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {

                    targetLocation_pc = location;
                    cameraLatLng_pc[0] = new LatLng(location.getLatitude(), location.getLongitude());
                    Log.i("Camera LocationUpdate:",cameraLatLng_pc[0].toString());
                }
            }
        };

        imageCaptureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cameraLatLng_pc[0] != null) {
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
//                                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE_PC);
                                activityResultLauncher.launch(takePictureIntent);
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


        //map data
        deleteGpxFile_pc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gpxFileName_pc.setText("");
                gpxContent_pc = "";
                locationListsCreatePC = new ArrayList<>();
                gpxFileLayout_pc.setVisibility(View.GONE);
                addWaypoint_pc.setVisibility(View.VISIBLE);
                addTrack_pc.setVisibility(View.VISIBLE);
            }
        });

        addWaypoint_pc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent1 = new Intent(CreateProject.this, GpxCreationMap.class);
                intent1.putExtra("VALUE","WAYPOINT");
                intent1.putExtra("FROM","PROJECT_CREATION");
                startActivity(intent1);
            }
        });

        addTrack_pc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent1 = new Intent(CreateProject.this, GpxCreationMap.class);
                intent1.putExtra("VALUE","TRACK");
                intent1.putExtra("FROM","PROJECT_CREATION");
                startActivity(intent1);
            }
        });


        // create Project
        createProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                project_no = Objects.requireNonNull(projectNo.getText()).toString();
                project_name = Objects.requireNonNull(projectName.getText()).toString();
                project_value = Objects.requireNonNull(projectValue.getText()).toString();
                project_pic_details = Objects.requireNonNull(projectPICDetails.getText()).toString();
                project_details = Objects.requireNonNull(projectDetails.getText()).toString();

                if (!project_no.isEmpty()) {
                    if (!approval_date.isEmpty()) {
                        if (!project_name.isEmpty()) {
                            if (!start_date.isEmpty()) {
                                if (!end_date.isEmpty()) {
                                    if (!project_value.isEmpty()) {
                                        if (!project_value_type_id.isEmpty()) {
                                            if (!sanction_category_id.isEmpty()) {
                                                if (!sanction_sub_cat_id.isEmpty()) {
                                                    if (!financial_year_id.isEmpty()) {
                                                        if (!source_of_fund_id.isEmpty()) {
                                                            if (!project_type_id.isEmpty()) {
                                                                if (!project_sub_type_id.isEmpty()) {
                                                                    if (!project_pic_details.isEmpty()) {
                                                                        if (!project_details.isEmpty()) {
                                                                            if (!ddu_id.isEmpty()) {
                                                                                if (gpxContent_pc.isEmpty() && imageCapturedListsPC.size() == 0) {
                                                                                    MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(CreateProject.this)
                                                                                            .setTitle("Create Project!")
                                                                                            .setMessage("Do you want to create new project without taking MAP DATA and IMAGE DATA?")
                                                                                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                                                                @Override
                                                                                                public void onClick(DialogInterface dialog, int which) {

//                                                                                                    new InsertProjectCheck().execute();
                                                                                                    postProjectInformation(creationRequest());
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
                                                                                else if (gpxContent_pc.isEmpty()) {
                                                                                    MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(CreateProject.this)
                                                                                            .setTitle("Create Project!")
                                                                                            .setMessage("Do you want to create new project without taking MAP DATA?")
                                                                                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                                                                @Override
                                                                                                public void onClick(DialogInterface dialog, int which) {

//                                                                                                    new InsertProjectCheck().execute();
                                                                                                    postProjectInformation(creationRequest());
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
                                                                                else if (imageCapturedListsPC.size() == 0) {
                                                                                    MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(CreateProject.this)
                                                                                            .setTitle("Create Project!")
                                                                                            .setMessage("Do you want to create new project without taking IMAGE DATA?")
                                                                                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                                                                @Override
                                                                                                public void onClick(DialogInterface dialog, int which) {

//                                                                                                    new InsertProjectCheck().execute();
                                                                                                    postProjectInformation(creationRequest());
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
                                                                                else {
                                                                                    MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(CreateProject.this)
                                                                                            .setTitle("Create Project!")
                                                                                            .setMessage("Do you want to create new project?")
                                                                                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                                                                @Override
                                                                                                public void onClick(DialogInterface dialog, int which) {

//                                                                                                    new InsertProjectCheck().execute();
                                                                                                    postProjectInformation(creationRequest());
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
                                                                            }
                                                                            else {
                                                                                Toast.makeText(getApplicationContext(),"Please Give Project Union to Proceed",Toast.LENGTH_SHORT).show();
                                                                                scrollView.scrollTo(projectUnion.getScrollX(),projectUnion.getScrollY());
                                                                            }
                                                                        }
                                                                        else {
                                                                            Toast.makeText(getApplicationContext(),"Please Give Project Details to Proceed",Toast.LENGTH_SHORT).show();
                                                                            projectDetails.requestFocus();
                                                                            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                                                                            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,InputMethodManager.HIDE_IMPLICIT_ONLY);
                                                                            scrollView.scrollTo(projectDetails.getScrollX(),projectDetails.getScrollY());
                                                                        }
                                                                    }
                                                                    else {
                                                                        Toast.makeText(getApplicationContext(),"Please Give Project PIC Details to Proceed",Toast.LENGTH_SHORT).show();
                                                                        projectPICDetails.requestFocus();
                                                                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                                                                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,InputMethodManager.HIDE_IMPLICIT_ONLY);
                                                                        scrollView.scrollTo(projectPICDetails.getScrollX(),projectPICDetails.getScrollY());
                                                                    }
                                                                }
                                                                else {
                                                                    Toast.makeText(getApplicationContext(),"Please Give Project Sub Type to Proceed",Toast.LENGTH_SHORT).show();
                                                                    scrollView.scrollTo(projectSubType.getScrollX(),projectSubType.getScrollY());
                                                                }
                                                            }
                                                            else {
                                                                Toast.makeText(getApplicationContext(),"Please Give Project Type to Proceed",Toast.LENGTH_SHORT).show();
                                                                scrollView.scrollTo(projectType.getScrollX(),projectType.getScrollY());
                                                            }
                                                        }
                                                        else {
                                                            Toast.makeText(getApplicationContext(),"Please Give Project Source of Fund to Proceed",Toast.LENGTH_SHORT).show();
                                                            scrollView.scrollTo(sourceOfFund.getScrollX(),sourceOfFund.getScrollY());
                                                        }
                                                    }
                                                    else {
                                                        Toast.makeText(getApplicationContext(),"Please Give Project Financial Year to Proceed",Toast.LENGTH_SHORT).show();
                                                        scrollView.scrollTo(financialYear.getScrollX(),financialYear.getScrollY());
                                                    }
                                                }
                                                else {
                                                    Toast.makeText(getApplicationContext(),"Please Give Project Sanction Category to Proceed",Toast.LENGTH_SHORT).show();
                                                    scrollView.scrollTo(sanctionSubCat.getScrollX(),sanctionSubCat.getScrollY());
                                                }
                                            }
                                            else {
                                                Toast.makeText(getApplicationContext(),"Please Give Project Sanction Category to Proceed",Toast.LENGTH_SHORT).show();
                                                scrollView.scrollTo(sanctionCat.getScrollX(),sanctionCat.getScrollY());
                                            }
                                        }
                                        else {
                                            Toast.makeText(getApplicationContext(),"Please Give Project Value Type to Proceed",Toast.LENGTH_SHORT).show();
                                            scrollView.scrollTo(projectValueType.getScrollX(),projectValueType.getScrollY());
                                        }
                                    }
                                    else {
                                        Toast.makeText(getApplicationContext(),"Please Give Project Value to Proceed",Toast.LENGTH_SHORT).show();
                                        projectValue.requestFocus();
                                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,InputMethodManager.HIDE_IMPLICIT_ONLY);
                                        scrollView.scrollTo(projectValue.getScrollX(),projectValue.getScrollY());
                                    }
                                }
                                else {
                                    Toast.makeText(getApplicationContext(),"Please Give End Date to Proceed",Toast.LENGTH_SHORT).show();
                                    scrollView.scrollTo(endDate.getScrollX(),endDate.getScrollY());
                                }
                            }
                            else {
                                Toast.makeText(getApplicationContext(),"Please Give Start Date to Proceed",Toast.LENGTH_SHORT).show();
                                scrollView.scrollTo(startDate.getScrollX(),startDate.getScrollY());
                            }
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"Please Give Project Name to Proceed",Toast.LENGTH_SHORT).show();
                            closeKeyBoard();
                            projectName.requestFocus();
                            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,InputMethodManager.HIDE_IMPLICIT_ONLY);
                            scrollView.scrollTo(projectName.getScrollX(),projectName.getScrollY());
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"Please Give Approval Date to Proceed",Toast.LENGTH_SHORT).show();
                        scrollView.scrollTo(approvalDate.getScrollX(),approvalDate.getScrollY());
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(),"Please Give Project No to Proceed",Toast.LENGTH_SHORT).show();
                    closeKeyBoard();
                    projectNo.requestFocus();
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,InputMethodManager.HIDE_IMPLICIT_ONLY);
                    scrollView.scrollTo(projectNo.getScrollX(),projectNo.getScrollY());
                }
            }
        });

        closeKeyBoard();

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
                if (ActivityCompat.checkSelfPermission(CreateProject.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(CreateProject.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
        MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(CreateProject.this)
                .setTitle("EXIT!")
                .setMessage("Do you want leave this page without creating this project?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        gpxContent_pc = "";
                        ddu_id = "";
                        locationListsCreatePC = new ArrayList<>();
                        imageCapturedListsPC = new ArrayList<>();
                        selectedWardLists = new ArrayList<>();
                        selectedVillageLists = new ArrayList<>();
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
                    .addApi(LocationServices.API).addConnectionCallbacks(CreateProject.this)
                    .addOnConnectionFailedListener(CreateProject.this).build();
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
                                status.startResolutionForResult(CreateProject.this, 1000);
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
        currentPhotoPath_pc = image.getAbsolutePath();
        return image;
    }

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            int resultCode = result.getResultCode();

            if (resultCode == RESULT_OK) {

                // Getting ImageFile Name
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",Locale.getDefault()).format(new Date());
                System.out.println(timeStamp);
                imageFileName_pc = "IMG_" + timeStamp;
                System.out.println(imageFileName_pc);


                File imgFile = new  File(currentPhotoPath_pc);
                if(imgFile.exists()) {
                    System.out.println(currentPhotoPath_pc);

                    firstBitmap_pc = BitmapFactory.decodeFile(currentPhotoPath_pc);
                    try {
                        firstBitmap_pc = modifyOrientation(firstBitmap_pc, currentPhotoPath_pc);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    android.graphics.Bitmap.Config bitmapConfig = firstBitmap_pc.getConfig();
                    // set default bitmap config if none
                    if(bitmapConfig == null) {
                        bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
                    }
                    // resource bitmaps are imutable,
                    // so we need to convert it to mutable one
                    firstBitmap_pc = firstBitmap_pc.copy(bitmapConfig, true);

                    ImageDialoguePC imageDialogue = new ImageDialoguePC();
                    imageDialogue.show(getSupportFragmentManager(),"ImagePC");
                }
            }
        }
    });

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("result");
                //info.setText("Done");
                if (ActivityCompat.checkSelfPermission(CreateProject.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
//        else if (requestCode == REQUEST_IMAGE_CAPTURE_PC && resultCode == RESULT_OK) {
//
//            double latitude = cameraLatLng_pc[0].latitude;
//            double longitude = cameraLatLng_pc[0].longitude;
//
//            // \n is for new line
//            //Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
//
//            getAddress(latitude, longitude);
//
//            // Getting ImageFile Name
//            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",Locale.getDefault()).format(new Date());
//            System.out.println(timeStamp);
//            imageFileName_pc = "IMG_" + timeStamp;
//            System.out.println(imageFileName_pc);
//
//            //fusedLocationProviderClient.removeLocationUpdates(locationCallback);
//            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yy, hh:mm:ss a", Locale.getDefault());
//            Date c = Calendar.getInstance().getTime();
//            String dd = simpleDateFormat.format(c);
//            System.out.println(dd);
//            String timeLatLng = "Time: " + dd + "\n" + "Latitude: " + latitude + "\n" + "Longitude: " + longitude;
//            address = timeLatLng + "\n"+ "Address: " + address;
//            System.out.println(address);
//
//            File imgFile = new  File(currentPhotoPath_pc);
//            if(imgFile.exists()) {
//                //cameraImage.setImageURI(Uri.fromFile(imgFile));
//                System.out.println(currentPhotoPath_pc);
//
//                firstBitmap_pc = BitmapFactory.decodeFile(currentPhotoPath_pc);
//                try {
//                    firstBitmap_pc = modifyOrientation(firstBitmap_pc, currentPhotoPath_pc);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                Resources resources = getResources();
//                float scale = resources.getDisplayMetrics().density;
//                //Bitmap bitmap = BitmapFactory.decodeResource(resources, gResId);
//
//                android.graphics.Bitmap.Config bitmapConfig = firstBitmap_pc.getConfig();
//                // set default bitmap config if none
//                if(bitmapConfig == null) {
//                    bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
//                }
//                // resource bitmaps are imutable,
//                // so we need to convert it to mutable one
//                firstBitmap_pc = firstBitmap_pc.copy(bitmapConfig, true);
//
//                Canvas canvas = new Canvas(firstBitmap_pc);
//
//                // new antialiased Paint
//                TextPaint paint=new TextPaint(Paint.ANTI_ALIAS_FLAG);
//                // text color - #3D3D3D
//                paint.setColor(Color.WHITE);
//                // text size in pixels
//                paint.setTextSize((int) (36 * scale));
//                // text shadow
//                paint.setShadowLayer(4f, 0f, 2f, Color.BLACK);
//                paint.setFakeBoldText(true);
//
//                // set text width to canvas width minus 16dp padding
//                int textWidth = canvas.getWidth() - (int) (16 * scale);
//
//                // init StaticLayout for text
//
//                StaticLayout textLayout = new StaticLayout(
//                        address, paint, textWidth, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
//
//                // get height of multiline text
//                int textHeight = textLayout.getHeight();
//
//                // get position of text's top left corner
//                float x = (firstBitmap_pc.getWidth() - textWidth)/2;
//                float y = (firstBitmap_pc.getHeight() - textHeight)/2;
//
//
//                // draw text to the Canvas center
//                int yyyy = firstBitmap_pc.getHeight() - textHeight - 16;
//                canvas.save();
//                canvas.translate(5, yyyy);
//                textLayout.draw(canvas);
//                canvas.restore();
//
//                ImageDialoguePC imageDialogue = new ImageDialoguePC();
//                imageDialogue.show(getSupportFragmentManager(),"ImagePC");
//            }
//        }
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

//    public void getAddress(double lat, double lng) {
//        Geocoder geocoder = new Geocoder(CreateProject.this, Locale.getDefault());
//        try {
//            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
//            Address obj = addresses.get(0);
//            String adds = obj.getAddressLine(0);
//            String add = "Address from GeoCODE: ";
//            add = add + "\n" + obj.getCountryName();
//            add = add + "\n" + obj.getCountryCode();
//            add = add + "\n" + obj.getAdminArea();
//            add = add + "\n" + obj.getPostalCode();
//            add = add + "\n" + obj.getSubAdminArea();
//            add = add + "\n" + obj.getLocality();
//            add = add + "\n" + obj.getSubThoroughfare();
//
//            Log.v("IGA", "Address: " + add);
//            Log.v("NEW ADD", "Address: " + adds);
//            address = adds;
//            // Toast.makeText(this, "Address=>" + add,
//            // Toast.LENGTH_SHORT).show();
//
//            // TennisAppActivity.showDialog(add);
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//            address = "Address Not Found";
////            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
//        }
//    }

//    public boolean isConnected () {
//        boolean connected = false;
//        boolean isMobile = false;
//        try {
//            ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
//            NetworkInfo nInfo = cm.getActiveNetworkInfo();
//            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
//            return connected;
//        } catch (Exception e) {
//            Log.e("Connectivity Exception", e.getMessage());
//        }
//        return connected;
//    }
//
//    public boolean isOnline () {
//
//        Runtime runtime = Runtime.getRuntime();
//        try {
//            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
//            int exitValue = ipProcess.waitFor();
//            return (exitValue == 0);
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        return false;
//    }

    @Override
    public void onCategoryClicked(int CategoryPosition) {

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

    // ----------------------Getting Necessary Data----------------------
    public void getData() {
        waitProgress.show(getSupportFragmentManager(), "WaitBar");
        waitProgress.setCancelable(false);
        conn = false;

        sanctionCatLists = new ArrayList<>();
        sanctionSubCatLists = new ArrayList<>();
        sourceFundLists = new ArrayList<>();
        financialYearLists = new ArrayList<>();
        unionLists = new ArrayList<>();

        String sanc_cat_url = "http://103.56.208.123:8086/terrain/tr_kabikha/utility_data/sanction_cat_lists";
        String pcm_cat_url = "http://103.56.208.123:8086/terrain/tr_kabikha/utility_data/pcm_category_lists";
        String fy_url = "http://103.56.208.123:8086/terrain/tr_kabikha/utility_data/fy_lists";
        String fund_url = "http://103.56.208.123:8086/terrain/tr_kabikha/utility_data/source_of_fund_lists";
        String union_url = "http://103.56.208.123:8086/terrain/tr_kabikha/utility_data/union_lists?dd_id="+dd_id;

        RequestQueue requestQueue = Volley.newRequestQueue(CreateProject.this);

        StringRequest unionRequest = new StringRequest(Request.Method.GET, union_url, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray jsonArray = new JSONArray(items);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject unionObject = jsonArray.getJSONObject(i);
                        String pcun_ddu_id = unionObject.getString("pcun_ddu_id");
                        String ddu_union_name = unionObject.getString("ddu_union_name");

                        ddu_union_name = transformText(ddu_union_name);

                        unionLists.add(new UnionLists(pcun_ddu_id,ddu_union_name));

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

        StringRequest fundRequest = new StringRequest(Request.Method.GET, fund_url, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray jsonArray = new JSONArray(items);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject fundObject = jsonArray.getJSONObject(i);
                        String p_fsm_id = fundObject.getString("p_fsm_id");
                        String fsm_fund_name = fundObject.getString("fsm_fund_name");

                        fsm_fund_name = transformText(fsm_fund_name);

                        sourceFundLists.add(new SourceFundLists(p_fsm_id,fsm_fund_name));
                    }
                }
                requestQueue.add(unionRequest);

            } catch (JSONException e) {
                e.printStackTrace();
                conn = false;
                updateUI();
            }
        }, error -> {
            conn = false;
            updateUI();
        });

        StringRequest fyRequest = new StringRequest(Request.Method.GET, fy_url, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray jsonArray = new JSONArray(items);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject fyObject = jsonArray.getJSONObject(i);
                        String p_fy_id = fyObject.getString("p_fy_id");
                        String fy_financial_year_name = fyObject.getString("fy_financial_year_name");
                        String fy_from_year = fyObject.getString("fy_from_year");
                        String fy_to_year = fyObject.getString("fy_to_year");
                        String fy_details = fyObject.getString("fy_details");
                        String fy_active_flag = fyObject.getString("fy_active_flag");

                        financialYearLists.add(new FinancialYearLists(p_fy_id,fy_financial_year_name,fy_from_year,
                                fy_to_year,fy_details,fy_active_flag));
                    }
                }
                requestQueue.add(fundRequest);
            } catch (JSONException e) {
                e.printStackTrace();
                conn = false;
                updateUI();
            }
        }, error -> {
            conn = false;
            updateUI();
        });

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
                requestQueue.add(fyRequest);
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

    private void updateUI() {
        waitProgress.dismiss();
        if (conn) {

            conn = false;

            ArrayList<String> type = new ArrayList<>();
            for(int i = 0; i < sanctionCatLists.size(); i++) {
                type.add(sanctionCatLists.get(i).getName());
            }
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type);

            sanctionCat.setAdapter(arrayAdapter);

            ArrayList<String> type1 = new ArrayList<>();
            for(int i = 0; i < sanctionSubCatLists.size(); i++) {
                type1.add(sanctionSubCatLists.get(i).getName());
            }
            ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(getApplicationContext(),R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type1);

            sanctionSubCat.setAdapter(arrayAdapter1);

            ArrayList<String> type2 = new ArrayList<>();
            for(int i = 0; i < financialYearLists.size(); i++) {
                type2.add(financialYearLists.get(i).getFinancialYearName());
            }
            ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(getApplicationContext(),R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type2);

            financialYear.setAdapter(arrayAdapter2);

            ArrayList<String> type3 = new ArrayList<>();
            for(int i = 0; i < sourceFundLists.size(); i++) {
                type3.add(sourceFundLists.get(i).getFundName());
            }
            ArrayAdapter<String> arrayAdapter3 = new ArrayAdapter<String>(getApplicationContext(),R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type3);

            sourceOfFund.setAdapter(arrayAdapter3);

            if (dd_id != null) {
                if (!dd_id.isEmpty()) {
                    ArrayList<String> type7 = new ArrayList<>();
                    for(int i = 0; i < unionLists.size(); i++) {
                        type7.add(unionLists.get(i).getUnionName());
                    }
                    ArrayAdapter<String> arrayAdapter7 = new ArrayAdapter<String>(getApplicationContext(),R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type7);

                    projectUnion.setAdapter(arrayAdapter7);
                }
            }

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

            }
            else {
                //Toast.makeText(getApplicationContext(), "OK", Toast.LENGTH_SHORT).show();
                if (ActivityCompat.checkSelfPermission(CreateProject.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(CreateProject.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
            AlertDialog dialog = new AlertDialog.Builder(CreateProject.this)
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

    // ----------------------Getting Project Types Data and Updating UI----------------------
    public void getProjectType() {
        waitProgress.show(getSupportFragmentManager(), "WaitBar");
        waitProgress.setCancelable(false);
        conn = false;

        projectTypeLists = new ArrayList<>();

        if (source_of_fund_id == null) {
            source_of_fund_id = "";
        }

        String p_type_url = "http://103.56.208.123:8086/terrain/tr_kabikha/utility_data/modified_project_type_lists?fsm_id="+source_of_fund_id;

        RequestQueue requestQueue = Volley.newRequestQueue(CreateProject.this);

        StringRequest projectTypeRequest = new StringRequest(Request.Method.GET, p_type_url, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray jsonArray = new JSONArray(items);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject pTypeObject = jsonArray.getJSONObject(i);
                        String ptm_id = pTypeObject.getString("ptm_id");
                        String ptm_project_type_name = pTypeObject.getString("ptm_project_type_name");

                        ptm_project_type_name = transformText(ptm_project_type_name);

                        projectTypeLists.add(new ProjectTypeLists(ptm_id,ptm_project_type_name));
                    }
                }

                conn = true;
                updateProjectType();

            } catch (JSONException e) {
                e.printStackTrace();
                conn = false;
                updateProjectType();
            }
        }, error -> {
            conn = false;
            updateProjectType();
        });

        requestQueue.add(projectTypeRequest);

    }

    private void updateProjectType() {
        waitProgress.dismiss();
        if (conn) {

            if (projectTypeLists.size() != 0) {
                projectTypeLay.setEnabled(true);
            }
            ArrayList<String> type = new ArrayList<>();
            for(int i = 0; i < projectTypeLists.size(); i++) {
                type.add(projectTypeLists.get(i).getProjectTypeName());
            }
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type);

            projectType.setAdapter(arrayAdapter);

            conn = false;

        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
            AlertDialog dialog = new AlertDialog.Builder(CreateProject.this)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .show();

//                dialog.setCancelable(false);
//                dialog.setCanceledOnTouchOutside(false);
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    getProjectType();
                    dialog.dismiss();
                }
            });
        }
    }

    //    --------------------------Getting Project Sub Types and updating UI-----------------------------
    public void getProjectSubType() {
        waitProgress.show(getSupportFragmentManager(), "WaitBar");
        waitProgress.setCancelable(false);
        conn = false;

        projectSubTypeLists = new ArrayList<>();

        if (project_type_id == null) {
            project_type_id = "";
        }

        String pr_sub_type_url = "http://103.56.208.123:8086/terrain/tr_kabikha/utility_data/project_sub_type_lists?ptm_id="+project_type_id;

        RequestQueue requestQueue = Volley.newRequestQueue(CreateProject.this);

        StringRequest prSubTypeRequest = new StringRequest(Request.Method.GET, pr_sub_type_url, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray jsonArray = new JSONArray(items);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject prSubTypeObject = jsonArray.getJSONObject(i);
                        String p_ptd_id = prSubTypeObject.getString("p_ptd_id");
                        String ptd_project_subtype_name = prSubTypeObject.getString("ptd_project_subtype_name");

                        ptd_project_subtype_name = transformText(ptd_project_subtype_name);

                        projectSubTypeLists.add(new ProjectSubTypeLists(p_ptd_id,ptd_project_subtype_name));

                    }
                }
                conn = true;
                updateProjectSubTypes();

            } catch (JSONException e) {
                e.printStackTrace();
                conn = false;
                updateProjectSubTypes();
            }
        }, error -> {
            conn = false;
            updateProjectSubTypes();
        });

        requestQueue.add(prSubTypeRequest);
    }

    public void updateProjectSubTypes() {
        waitProgress.dismiss();
        if (conn) {

            if (projectSubTypeLists.size() != 0) {
                projectSubTypeLay.setEnabled(true);
            }

            ArrayList<String> type = new ArrayList<>();
            for(int i = 0; i < projectSubTypeLists.size(); i++) {
                type.add(projectSubTypeLists.get(i).getProjectSubTypeName());
            }
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type);

            projectSubType.setAdapter(arrayAdapter);

            conn = false;

        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
            AlertDialog dialog = new AlertDialog.Builder(CreateProject.this)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .show();

//                dialog.setCancelable(false);
//                dialog.setCanceledOnTouchOutside(false);
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    getProjectSubType();
                    dialog.dismiss();
                }
            });
        }
    }

    //    --------------------------INSERTING PROJECT DATA-----------------------------
    public ProjectCreationRequest creationRequest() {
        ProjectCreationRequest p = new ProjectCreationRequest();
        p.setP_ENTRY_DATE(entry_date);
        p.setP_PCM_USER(pcm_user);
        p.setP_DD_ID(dd_id);
        p.setP_DDU_ID(ddu_id);
        p.setP_PROJECT_NAME(project_name);
        p.setP_PROJECT_NO(project_no);
        p.setP_FY_ID(financial_year_id);
        p.setP_PTM_ID(project_type_id);
        p.setP_PTD_ID(project_sub_type_id);
        p.setP_APPROVAL_DATE(approval_date);
        p.setP_PIC_DETAILS(project_pic_details);
        p.setP_FSM_ID(source_of_fund_id);
        p.setP_PROJECT_VALUE(project_value);
        p.setP_START_DATE(start_date);
        p.setP_END_DATE(end_date);
        p.setP_PCM_PSC_ID(sanction_category_id);
        p.setP_PCM_PCM_ID(sanction_sub_cat_id);
        p.setP_PROJECT_DETAILS(project_details);
        p.setP_PROJECT_VALUE_TYPE_ID(project_value_type_id);

        return p;
    }

    public void postProjectInformation(ProjectCreationRequest projectCreationRequest) {

        waitProgress.show(getSupportFragmentManager(), "WaitBar");
        waitProgress.setCancelable(false);
        conn = false;

        firstStage = false;
        secondStage = false;
        thirdStage = false;

        Call<ProjectCreationResponse> projectCreationResponseCall = ApiClient.getService().createProject(projectCreationRequest);
        projectCreationResponseCall.enqueue(new Callback<ProjectCreationResponse>() {
            @Override
            public void onResponse(Call<ProjectCreationResponse> call, Response<ProjectCreationResponse> response) {

                if (response.isSuccessful()) {
                    String string_out = Objects.requireNonNull(response.body()).getString_out();
                    String pcun_id = response.body().getPcun_id();
                    String pcm_id = response.body().getPcm_id();
                    pcm_internal_no = response.body().getPcm_internal_no();

                    if (!pcun_id.isEmpty() && !pcun_id.equals("null") && string_out.equals("Successfully Created")) {
                        firstStage = true;
                        postWard(pcun_id,pcm_id);
                    }
                    else {
                        conn = false;
                        firstStage = false;
                        updateInterface();
                    }
                }
                else {
                    System.out.println("Failed");
                    conn = false;
                    firstStage = false;
                    updateInterface();
                }

            }

            @Override
            public void onFailure(Call<ProjectCreationResponse> call, Throwable t) {
                System.out.println("Failed: ERROR");
                conn = false;
                firstStage = false;
                updateInterface();
            }
        });

    }

    public void postWard(String pcun_id,String pcm_id) {

        numberOfLocationRequestsToMake = 0;
        hasLocationRequestFailed = false;

        if (selectedWardLists.size() != 0) {
            for (int i = 0 ; i < selectedWardLists.size() ; i++) {
                String ddw_id = selectedWardLists.get(i).getDdw_id();
                numberOfLocationRequestsToMake++;
                System.out.println("numberOfRequestsToMake: "+ numberOfLocationRequestsToMake);
                postWardRequest(ddw_id,i,pcun_id,pcm_id);
            }
        }
        else {
            secondStage = true;
            updateGpxQuery(pcm_id);
        }

    }

    public void postWardRequest(String ddw_id, int i, String pcun_id, String pcm_id){

        String url = "http://103.56.208.123:8086/terrain/tr_kabikha/project_creation/insert_wards";

        RequestQueue requestQueue = Volley.newRequestQueue(CreateProject.this);

        StringRequest wardRequest = new StringRequest(Request.Method.POST, url, response -> {

            try {
                numberOfLocationRequestsToMake--;
                System.out.println("numberOfRequestsToMake: "+ numberOfLocationRequestsToMake);
                JSONObject jsonObject = new JSONObject(response);
                String string_out = jsonObject.getString("string_out");
                String pcw_id = jsonObject.getString("pcw_id");
                if (!pcw_id.isEmpty() && !pcw_id.equals("null") && string_out.equals("Successfully Created")) {

                    postVillage(pcw_id,ddw_id,pcm_id);
                }
                else {
                    hasLocationRequestFailed = true;
                    if(numberOfLocationRequestsToMake == 0) {
                        //The last request failed
                        conn = false;
                        secondStage = false;
                        updateInterface();
                    }
                }
            } catch (JSONException e) {
                numberOfLocationRequestsToMake--;
                e.printStackTrace();
                hasLocationRequestFailed = true;
                if(numberOfLocationRequestsToMake == 0) {
                    //The last request failed
                    conn = false;
                    secondStage = false;
                    updateInterface();
                }
            }
        },error -> {
            numberOfLocationRequestsToMake--;
            hasLocationRequestFailed = true;
            if(numberOfLocationRequestsToMake == 0) {
                //The last request failed
                conn = false;
                secondStage = false;
                updateInterface();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("DD_ID",dd_id);
                headers.put("PCUN_ID",pcun_id);
                headers.put("PCM_USER",pcm_user);
                headers.put("DDU_ID",ddu_id);
                headers.put("DDW_ID",ddw_id);

                return headers;
            }
        };

        requestQueue.add(wardRequest);
    }

    public void postVillage(String pcw_id, String ddw_id, String pcm_id) {

        if (selectedVillageLists.size()!= 0) {
            for (int j = 0; j < selectedVillageLists.size(); j++) {
                if (ddw_id.equals(selectedVillageLists.get(j).getDdv_ddw_id())) {
                    String ddv_id = selectedVillageLists.get(j).getDdv_id();
                    numberOfLocationRequestsToMake++;
                    System.out.println("numberOfRequestsToMake: "+ numberOfLocationRequestsToMake);
                    postVillageRequest(ddw_id, ddv_id, pcw_id, pcm_id);
                }
            }
        }
        else {
            if (numberOfLocationRequestsToMake == 0) {
                if (!hasLocationRequestFailed) {
                    secondStage = true;
                    updateGpxQuery(pcm_id);
                }
                else {
                    conn = false;
                    secondStage = false;
                    updateInterface();
                }
            }

        }

    }

    public void postVillageRequest(String ddw_id, String ddv_id, String pcw_id,String pcm_id) {

        String url = "http://103.56.208.123:8086/terrain/tr_kabikha/project_creation/insert_villages";

        RequestQueue requestQueue = Volley.newRequestQueue(CreateProject.this);

        StringRequest villageRequest = new StringRequest(Request.Method.POST, url, response -> {

            try {
                numberOfLocationRequestsToMake--;
                System.out.println("numberOfRequestsToMake: "+ numberOfLocationRequestsToMake);
                JSONObject jsonObject = new JSONObject(response);
                String string_out = jsonObject.getString("string_out");
                if (string_out.equals("Successfully Created")) {

                    if (numberOfLocationRequestsToMake == 0) {
                        if (!hasLocationRequestFailed) {
                            secondStage = true;
                            updateGpxQuery(pcm_id);
                        }
                        else {
                            conn = false;
                            secondStage = false;
                            updateInterface();
                        }
                    }
                }
                else {
                    hasLocationRequestFailed = true;
                    if(numberOfLocationRequestsToMake == 0) {
                        //The last request failed
                        conn = false;
                        secondStage = false;
                        updateInterface();
                    }
                }
            } catch (JSONException e) {
                numberOfLocationRequestsToMake--;
                e.printStackTrace();
                hasLocationRequestFailed = true;
                if(numberOfLocationRequestsToMake == 0) {
                    //The last request failed
                    conn = false;
                    secondStage = false;
                    updateInterface();
                }
            }
        },error -> {
            numberOfLocationRequestsToMake--;
            hasLocationRequestFailed = true;
            if(numberOfLocationRequestsToMake == 0) {
                //The last request failed
                conn = false;
                secondStage = false;
                updateInterface();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("DD_ID",dd_id);
                headers.put("PCW_ID",pcw_id);
                headers.put("PCM_USER",pcm_user);
                headers.put("DDU_ID",ddu_id);
                headers.put("DDW_ID",ddw_id);
                headers.put("DDV_ID",ddv_id);

                return headers;
            }
        };

        requestQueue.add(villageRequest);
    }

    public void updateGpxQuery(String pcm_id) {
        gpxUploaded = false;
        String update_gpx_url = "http://103.56.208.123:8086/terrain/tr_kabikha/update_project/update_gpx_file";

        if (!gpxContent_pc.isEmpty()) {
            RequestQueue requestQueue = Volley.newRequestQueue(CreateProject.this);
            String gpxName = gpxFileName_pc.getText().toString();
            byte[] bArray = gpxContent_pc.getBytes();

            StringRequest updateGpxRequest = new StringRequest(Request.Method.POST, update_gpx_url, response -> {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String string_out = jsonObject.getString("string_out");
                    if (string_out.equals("Successfully Created")) {
                        gpxUploaded = true;
                        updatePicQuery(pcm_id);
                    }
                    else {
                        conn = false;
                        gpxUploaded = false;
                        updateInterface();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    conn = false;
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
                    headers.put("PCM_ID",pcm_id);
                    headers.put("P_TYPE",p_type_name);
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
            gpxUploaded = true;
            updatePicQuery(pcm_id);
        }
    }

    public void updatePicQuery(String pcm_id) {
        numberOfRequestsToMake = 0;
        hasRequestFailed = false;

        if (imageCapturedListsPC.size() != 0) {
            for (int i = 0; i < imageCapturedListsPC.size(); i++) {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                Bitmap bitmap = imageCapturedListsPC.get(i).getBitmap();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 60, bos);
                byte[] bArray = bos.toByteArray();
                numberOfRequestsToMake++;
                updatePicRequest(bArray,i,pcm_id);

            }
        }
        else {
            conn = true;
            updateInterface();
        }

    }

    public void updatePicRequest(byte[] bArray, int i,String pcm_id) {
        String update_pic_url = "http://103.56.208.123:8086/terrain/tr_kabikha/update_project/update_picture";
        RequestQueue requestQueue = Volley.newRequestQueue(CreateProject.this);

        StringRequest updatePicRequest = new StringRequest(Request.Method.POST, update_pic_url, response -> {
            try {
                numberOfRequestsToMake--;
                JSONObject jsonObject = new JSONObject(response);
                String string_out = jsonObject.getString("string_out");
                if (string_out.equals("Successfully Created")) {
                    imageCapturedListsPC.get(i).setUploaded(true);
                }
                else {
                    hasRequestFailed = true;
                }

                if (numberOfRequestsToMake == 0) {
                    if (!hasRequestFailed) {
                        thirdStage = true;
                        conn = true;
                        updateInterface();
                    }
                    else {
                        conn = false;
                        thirdStage = false;
                        updateInterface();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                numberOfRequestsToMake--;
                hasRequestFailed = true;
                if(numberOfRequestsToMake == 0) {
                    //The last request failed
                    conn = false;
                    thirdStage = false;
                    updateInterface();
                }
            }
        }, error -> {
            numberOfRequestsToMake--;
            hasRequestFailed = true;
            if(numberOfRequestsToMake == 0) {
                //The last request failed
                conn = false;
                thirdStage = false;
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
                headers.put("PCM_ID",pcm_id);
                headers.put("FILE_NAME",imageCapturedListsPC.get(i).getFileName());
                headers.put("USER_NAME",picUserDetails.get(0).getUserName());
                headers.put("STAGE",imageCapturedListsPC.get(i).getStage());
                return headers;
            }

            @Override
            public String getBodyContentType() {
                return "application/binary";
            }
        };

        requestQueue.add(updatePicRequest);
    }

    public void updateInterface(){
        waitProgress.dismiss();
        if (conn) {

            conn = false;

            MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(CreateProject.this)
                    .setTitle("SUCCESS!")
                    .setMessage("Project Created Successfully with all information. Your Project's Internal No is "+pcm_internal_no+". You can use this Internal no to search project from project lists and update your project.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            gpxContent_pc = "";
                            ddu_id = "";
                            locationListsCreatePC = new ArrayList<>();
                            imageCapturedListsPC = new ArrayList<>();
                            selectedWardLists = new ArrayList<>();
                            selectedVillageLists = new ArrayList<>();
                            finish();
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.setCancelable(false);
            alertDialog.show();

        } else {
            if (!firstStage && !secondStage && !gpxUploaded && !thirdStage ) {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(CreateProject.this)
                        .setMessage("Please Check Your Internet Connection")
                        .setPositiveButton("Retry", null);

                AlertDialog alertDialog = dialog.create();
                alertDialog.show();
                Button positive = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        postProjectInformation(creationRequest());
                        alertDialog.dismiss();
                    }
                });
            }
            else if (firstStage && !secondStage && !gpxUploaded && !thirdStage ) {
                MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(CreateProject.this)
                        .setTitle("Network Connection Interrupted!")
                        .setMessage("Project Created Successfully but failed to upload Location data, Map Data and Image Data. Your Project's Internal No is "+pcm_internal_no+". You can use this Internal no to search project from project lists and update your project.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                gpxContent_pc = "";
                                ddu_id = "";
                                locationListsCreatePC = new ArrayList<>();
                                imageCapturedListsPC = new ArrayList<>();
                                selectedWardLists = new ArrayList<>();
                                selectedVillageLists = new ArrayList<>();
                                finish();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.setCancelable(false);
                alertDialog.show();
            }
            else if (firstStage && secondStage && !gpxUploaded && !thirdStage ) {
                MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(CreateProject.this)
                        .setTitle("Network Connection Interrupted!")
                        .setMessage("Project Created Successfully but failed to upload Map Data and Image Data. Your Project's Internal No is "+pcm_internal_no+". You can use this Internal no to search project from project lists and update your project.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                gpxContent_pc = "";
                                ddu_id = "";
                                locationListsCreatePC = new ArrayList<>();
                                imageCapturedListsPC = new ArrayList<>();
                                selectedWardLists = new ArrayList<>();
                                selectedVillageLists = new ArrayList<>();
                                finish();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.setCancelable(false);
                alertDialog.show();
            }
            else if (firstStage && secondStage && gpxUploaded && !thirdStage) {
                MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(CreateProject.this)
                        .setTitle("Network Connection Interrupted!")
                        .setMessage("Project Created Successfully but failed to upload Image Data. Your Project's Internal No is "+pcm_internal_no+". You can use this Internal no to search project from project lists and update your project.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                gpxContent_pc = "";
                                ddu_id = "";
                                locationListsCreatePC = new ArrayList<>();
                                imageCapturedListsPC = new ArrayList<>();
                                selectedWardLists = new ArrayList<>();
                                selectedVillageLists = new ArrayList<>();
                                finish();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.setCancelable(false);
                alertDialog.show();
            }
        }
    }

    //    --------------------------Transforming Bangla Text-----------------------------
    private String transformText(String text) {
        byte[] bytes = text.getBytes(ISO_8859_1);
        return new String(bytes, UTF_8);
    }
}