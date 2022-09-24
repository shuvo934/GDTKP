package com.shuvo.ttit.trkabikha.gpxCreation;

import static com.shuvo.ttit.trkabikha.gpxCreation.necessary.DistanceCalculator.CalculationByDistance;
import static com.shuvo.ttit.trkabikha.projectUpdate.editProject.ProjectEdit.INTERNAL_NO;
import static com.shuvo.ttit.trkabikha.projectUpdate.editProject.ProjectEdit.TAG_GPX;
import static com.shuvo.ttit.trkabikha.projectUpdate.editProject.ProjectEdit.XML_HEADER;
import static com.shuvo.ttit.trkabikha.projectUpdate.editProject.ProjectEdit.addTrack;
import static com.shuvo.ttit.trkabikha.projectUpdate.editProject.ProjectEdit.addWaypoint;
import static com.shuvo.ttit.trkabikha.projectUpdate.editProject.ProjectEdit.gpxContent;
import static com.shuvo.ttit.trkabikha.projectUpdate.editProject.ProjectEdit.gpxFileLayout;
import static com.shuvo.ttit.trkabikha.projectUpdate.editProject.ProjectEdit.gpxFileName;
import static com.shuvo.ttit.trkabikha.projectUpdate.editProject.ProjectEdit.targetLocation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.shuvo.ttit.trkabikha.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class GpxCreationMap extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    FusedLocationProviderClient fusedLocationProviderClient;
    private Spinner selection;
    private Button drawLine, drawWayPoint, clearMap, autoLine, removeLast;
    private Button saveFile;
    private Button instantWay;

    public static String length_multi = "";
    public static String fileName = null;

    private Boolean lineValue = true;
    private Boolean autoLineValue = false;
    private Boolean wayPointValue = false;
    private Boolean isStart = false;
    private Boolean isWayStart = false;
    private Boolean fromMap = false;
    private Boolean fromButton = false;
    String address = "";

    private String lineStart = "0";

    public static ArrayList<LatLng> gpxLatLng;

    public static ArrayList<LatLng> autoGpxLatLng;

    public static ArrayList<LatLng> wpLatLng;

    public static ArrayList<String> trk;

    LocationManager locationManager;
    LocationRequest locationRequest;
    LocationCallback locationCallback;

    String val = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gpx_creation_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.gpx_map);
        mapFragment.getMapAsync(this);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        selection = findViewById(R.id.spinnnnn_multi);
        drawLine = findViewById(R.id.line_draw_save_multi);
        drawWayPoint = findViewById(R.id.waypoint_save_multi);
        clearMap = findViewById(R.id.map_clear_multi);
        autoLine = findViewById(R.id.auto_line_multi);
        removeLast = findViewById(R.id.last_point_remove_multi);
        saveFile = findViewById(R.id.save_gpx_file);
        saveFile.setEnabled(false);
        instantWay = findViewById(R.id.instant_way);

        gpxLatLng = new ArrayList<>();
        autoGpxLatLng = new ArrayList<>();
        wpLatLng = new ArrayList<>();
        trk = new ArrayList<>();

        Intent intent = getIntent();
        val = intent.getStringExtra("VALUE");

        if (val.equals("TRACK")) {
//            instantWay.setVisibility(View.GONE);
            drawWayPoint.setVisibility(View.GONE);
            autoLine.setVisibility(View.VISIBLE);
            drawLine.setVisibility(View.VISIBLE);
        }
        else if (val.equals("WAYPOINT")){
//            instantWay.setVisibility(View.VISIBLE);
            drawWayPoint.setVisibility(View.VISIBLE);
            autoLine.setVisibility(View.GONE);
            drawLine.setVisibility(View.GONE);
        }


        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        locationRequest.setInterval(5);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(1000);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        List<String> categories = new ArrayList<String>();
        categories.add("NORMAL");
        categories.add("SATELLITE");
        categories.add("TERRAIN");
        categories.add("HYBRID");

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, categories);

        spinnerAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        selection.setAdapter(spinnerAdapter);
    }

    private void closeKeyBoard() {
        View view = getCurrentFocus();
        if (view != null) {
            view.clearFocus();
            InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        mMap = googleMap;

        zoomToUserLocation();
        googleMap.getUiSettings().setZoomControlsEnabled(true);

        // Variable for Draw Line (Manual Line)
        final int[] i = {0};
        final PolylineOptions[] option = {new PolylineOptions().width(5).color(Color.RED).geodesic(true)};
        MarkerOptions options = new MarkerOptions();
        final Marker[] mm = {null};
        final Polyline[] polylines = {null};
        final Double[] j = {0.0};
        final Double[] k = {0.0};
        final LatLng[] preLatLng = {new LatLng(0, 0)};
        final LatLng[] previousLatlng = {new LatLng(0, 0)};

        // Variable for Auto Line (Automatic Line)
        final PolylineOptions[] nop = {new PolylineOptions().width(5).color(Color.RED).geodesic(true)};
        MarkerOptions mp = new MarkerOptions();
        final int[] local = {0};
        final LatLng[] autoPreLatlng = {new LatLng(0, 0)};
        final LatLng[] lastLatLongitude = {new LatLng(0, 0)};
        final Double[] w = {0.0};

        // Variable for WayPoint (Marker)
        MarkerOptions wp = new MarkerOptions();
        final LatLng[] preWayPoint = {new LatLng(0, 0)};
        final Marker[] wwpp = {null};
        final LatLng[] instantWayLatLng = {new LatLng(0,0)};
        final LatLng[] checkLatLng = {new LatLng(0,0)};

        selection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String name = parent.getItemAtPosition(position).toString();
                switch (name) {
                    case "NORMAL":
                        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
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
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (isWayStart) {
                        instantWayLatLng[0] = new LatLng(location.getLatitude(),location.getLongitude());
                    }
                    if (isStart) {

                        Log.i("LocationFused ", location.toString());
                        lastLatLongitude[0] = new LatLng(location.getLatitude(), location.getLongitude());
                        if (local[0] == 0) {

                            local[0]++;
                            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
                            autoPreLatlng[0] = ll;
                            nop[0].add(ll);
                            mMap.addPolyline(nop[0]);
                            mp.position(ll);
                            mp.icon(BitmapDescriptorFactory.fromResource(R.drawable.circle));
                            mp.anchor((float) 0.5, (float) 0.5);
                            autoGpxLatLng.add(ll);
                            mp.snippet("0 KM");
                            mp.title("Starting Point");
                            mMap.addMarker(mp);
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ll, 18));
                        } else {

                            local[0]++;
                            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
                            Double distance = CalculationByDistance(autoPreLatlng[0], ll);

                            Log.i("Distance", distance.toString());

                            if (distance >= 0.01) {

                                nop[0].add(ll);
                                mMap.addPolyline(nop[0]);
                                mp.position(ll);
                                mp.icon(BitmapDescriptorFactory.fromResource(R.drawable.circle));
                                mp.anchor((float) 0.5, (float) 0.5);
                                w[0] = w[0] + distance;
                                mp.snippet(String.format("%.3f", w[0]) + " KM");
                                mp.title("Road Point");
                                mMap.addMarker(mp);
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ll, 18));
                                autoGpxLatLng.add(ll);
                                autoPreLatlng[0] = ll;
                            }

                        }

                    }
                }
            }
        };

        autoLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (lineStart.equals("0")) {
                    drawLine.setVisibility(View.GONE);
                    drawWayPoint.setVisibility(View.GONE);
                    autoLine.setText("রাস্তা শুরু করুন / Start Track");
                    autoGpxLatLng = new ArrayList<>();
                    lineStart = "1";
                    Toast.makeText(getApplicationContext(),"আরও ভাল পজিশন পেতে অনুগ্রহ করে ৫ থেকে ১০ সেকেন্ড অপেক্ষা করুন",Toast.LENGTH_SHORT).show();
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
                } else if (!autoLineValue && lineStart.equals("1")) {

                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    drawLine.setVisibility(View.GONE);
                    drawWayPoint.setVisibility(View.GONE);
                    autoGpxLatLng = new ArrayList<>();
                    lineStart = "2";
                    isStart = true;
                    autoLine.setText("রাস্তা শেষ এবং সংরক্ষণ করুন / Stop and Save Track");
                    autoLineValue = true;
                    //fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
                    //locationManager.requestLocationUpdates(provider, 0, 0, locationListener);
                    //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
                } else if (lineStart.equals("2") && autoLineValue) {
                    saveFile.setEnabled(true);
                    drawLine.setVisibility(View.VISIBLE);
                    if (val.equals("TRACK")) {
                        drawWayPoint.setVisibility(View.GONE);
                    }
                    else {
                        drawWayPoint.setVisibility(View.VISIBLE);
                    }
                    autoLine.setText("রাস্তা/Track (Automatic)");
                    local[0] = 0;
                    autoLineValue = false;
                    lineStart = "0";
                    fusedLocationProviderClient.removeLocationUpdates(locationCallback);
                    isStart = false;

                    Log.i("Last Location", lastLatLongitude.toString());

                    LatLng lastLatlng = lastLatLongitude[0];
                    nop[0].add(lastLatlng);
                    mMap.addPolyline(nop[0]);
                    mp.position(lastLatlng);
                    autoGpxLatLng.add(lastLatlng);
                    mp.icon(BitmapDescriptorFactory.fromResource(R.drawable.circle));
                    mp.anchor((float) 0.5, (float) 0.5);
                    Double distance = CalculationByDistance(autoPreLatlng[0], lastLatlng);
                    w[0] = w[0] + distance;
                    mp.snippet(String.format("%.3f", w[0]) + " KM");
                    mp.title("End Point");
                    mMap.addMarker(mp);

                    length_multi = (String.format("%.3f", w[0]) + " KM");

                    String start = "\t<trk>\n" +
                            "\t\t<name>TTIT</name>\n";
                    String desc = "\t\t<desc>Length: " + length_multi + "</desc>\n";
                    String trkseg = "\t\t<trkseg>\n";
                    String trkpt = "";
                    for (int b = 0; b < autoGpxLatLng.size(); b++) {
                        Log.i("Latlng :", autoGpxLatLng.get(b).toString());
                        trkpt += "\t\t\t<trkpt lat=\"" + autoGpxLatLng.get(b).latitude + "\" lon=\"" + autoGpxLatLng.get(b).longitude + "\"></trkpt>\n";
                    }
                    String trksegFinish = "\t\t</trkseg>\n";
                    String finish = "\t</trk>\n";

                    trk.add(start + desc + trkseg + trkpt + trksegFinish + finish);


                    nop[0] = new PolylineOptions().width(5).color(Color.RED).geodesic(true);

                    //locationManager.removeUpdates(locationListener);
                    w[0] = 0.0;
                    autoGpxLatLng.clear();
                    autoGpxLatLng = new ArrayList<>();
                    local[0] = 0;
                    autoPreLatlng[0] = new LatLng(0, 0);
                    lastLatLongitude[0] = new LatLng(0,0);
                }
            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (!lineValue) {
                    removeLast.setVisibility(View.VISIBLE);
                    clearMap.setVisibility(View.VISIBLE);
                    if (i[0] == 0) {
                        preLatLng[0] = latLng;
                        Log.i("Hobe ", "hobe");

                        options.position(latLng);
                        options.icon(BitmapDescriptorFactory.fromResource(R.drawable.circle));
                        options.anchor((float) 0.5, (float) 0.5);
                        options.snippet("0 KM");
                        options.title("Starting Point");
                        previousLatlng[0] = latLng;
                        gpxLatLng.add(latLng);
//                            mMap.addMarker(options).setTitle("Starting Point");
                        mm[0] = mMap.addMarker(options);
                        option[0].add(latLng);
                        i[0]++;
//                            mMap.addPolyline(option[0]);
                        polylines[0] = mMap.addPolyline(option[0]);
                    } else {
                        i[0]++;
                        Log.i("Hobe ", "hobe");
                        previousLatlng[0] = preLatLng[0];
                        options.position(latLng);
                        Double distance = CalculationByDistance(preLatLng[0], latLng);
                        k[0] = distance;
                        j[0] = j[0] + distance;
                        options.snippet(String.format("%.3f", j[0]) + " KM");
                        options.icon(BitmapDescriptorFactory.fromResource(R.drawable.circle));
                        options.anchor((float) 0.5, (float) 0.5);
                        options.title("Road Point");
                        gpxLatLng.add(latLng);
//                            mMap.addMarker(options).setTitle("Road Point");
                        mm[0] = mMap.addMarker(options);

                        preLatLng[0] = latLng;
                        option[0].add(latLng);
//                            mMap.addPolyline(option[0]);
                        polylines[0] = mMap.addPolyline(option[0]);

                    }

                }
                if (wayPointValue) {
                    removeLast.setVisibility(View.VISIBLE);
                    clearMap.setVisibility(View.VISIBLE);
                    preWayPoint[0] = latLng;
                    wp.position(latLng);
                    wp.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
                    wp.title("TTIT");
                    wpLatLng.add(latLng);
                    fromMap = true;
                    fromButton = false;
                    wwpp[0] = mMap.addMarker(wp);
                } else {
                    Log.i("Hobe na", "Kissu");
                }
            }
        });

        instantWay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wayPointValue && isWayStart) {
                    removeLast.setVisibility(View.VISIBLE);
                    clearMap.setVisibility(View.VISIBLE);
                    if (checkLatLng[0] != instantWayLatLng[0]) {
                        System.out.println(instantWayLatLng[0]);
                        checkLatLng[0] = instantWayLatLng[0];
                        preWayPoint[0] = instantWayLatLng[0];
                        wp.position(instantWayLatLng[0]);
                        fromButton = true;
                        fromMap = false;
                        wp.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
                        wp.title("TTIT");
                        wpLatLng.add(instantWayLatLng[0]);
                        wwpp[0] = mMap.addMarker(wp);
                    } else {
//                        Toast.makeText(getApplicationContext(), "This Way Point matches the previous one",Toast.LENGTH_SHORT).show();
                        Toast.makeText(getApplicationContext(), "এই জায়গাটি আগে আরেকবার নেয়া হয়েছে",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        removeLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i[0]--;
                if (mm[0] != null && polylines[0] != null && i[0] == 0) {
                    Log.i("Pai", "na");
                    mm[0].remove();
                    polylines[0].remove();
                    int index = gpxLatLng.lastIndexOf(previousLatlng[0]);
                    gpxLatLng.remove(index);
                    option[0] = new PolylineOptions().width(8).color(Color.RED).geodesic(true);
                    removeLast.setVisibility(View.GONE);
                }
                if (mm[0] != null && polylines[0] != null && i[0] != 0) {
                    Log.i("Pai", "na");
                    int index = gpxLatLng.lastIndexOf(preLatLng[0]);
                    gpxLatLng.remove(index);
                    mm[0].remove();
                    polylines[0].remove();
                    option[0] = new PolylineOptions().width(8).color(Color.RED).geodesic(true);
                    option[0].add(previousLatlng[0]);
                    preLatLng[0] = previousLatlng[0];
                    j[0] = j[0] - k[0];
                    removeLast.setVisibility(View.GONE);

                }
                if (wayPointValue && wwpp[0] != null && fromMap) {
                    wwpp[0].remove();
                    fromMap = false;
                    int index = wpLatLng.lastIndexOf(preWayPoint[0]);
                    wpLatLng.remove(index);
                    removeLast.setVisibility(View.GONE);
                }
                if (wayPointValue && wwpp[0] != null && fromButton) {
                    wwpp[0].remove();
                    fromButton = false;
                    checkLatLng[0] = new LatLng(0,0);
                    int index = wpLatLng.lastIndexOf(preWayPoint[0]);
                    wpLatLng.remove(index);
                    removeLast.setVisibility(View.GONE);
                }
            }
        });

        drawLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lineValue) {
                    gpxLatLng = new ArrayList<>();
                    lineValue = false;
                    autoLine.setVisibility(View.GONE);
                    drawWayPoint.setVisibility(View.GONE);
                    drawLine.setText("রাস্তা সংরক্ষণ করুন / Save Track");
                    option[0] = new PolylineOptions().width(8).color(Color.RED).geodesic(true);

                } else {
                    if (gpxLatLng.size() == 0) {
//                        Toast.makeText(getApplicationContext(), "Please Draw Line to Save", Toast.LENGTH_SHORT).show();
                        Toast.makeText(getApplicationContext(), "আগে রাস্তা চিহ্নিত করুন তারপরে সংরক্ষণ করুন", Toast.LENGTH_SHORT).show();

                    } else {
                        saveFile.setEnabled(true);
                        autoLine.setVisibility(View.VISIBLE);
                        if (val.equals("TRACK")) {
                            drawWayPoint.setVisibility(View.GONE);
                        }
                        else {
                            drawWayPoint.setVisibility(View.VISIBLE);
                        }
                        lineValue = true;
                        removeLast.setVisibility(View.GONE);

                        drawLine.setText("রাস্তা/Track (Manual)");
                        length_multi = (String.format("%.3f", j[0]) + " KM");
                        preLatLng[0] = new LatLng(0, 0);

                        String start = "\t<trk>\n" +
                                "\t\t<name>TTIT</name>\n";
                        String desc = "\t\t<desc>Length: " + length_multi + "</desc>\n";
                        String trkseg = "\t\t<trkseg>\n";
                        String trkpt = "";
                        for (int b = 0; b < gpxLatLng.size(); b++) {
                            Log.i("Latlng :", gpxLatLng.get(b).toString());
                            trkpt += "\t\t\t<trkpt lat=\"" + gpxLatLng.get(b).latitude + "\" lon=\"" + gpxLatLng.get(b).longitude + "\"></trkpt>\n";
                        }
                        String trksegFinish = "\t\t</trkseg>\n";
                        String finish = "\t</trk>\n";

                        trk.add(start + desc + trkseg + trkpt + trksegFinish + finish);

                        gpxLatLng.clear();;
                        gpxLatLng = new ArrayList<>();

                        i[0] = 0;
                        j[0] = 0.0;
                        option[0] = new PolylineOptions().width(5).color(Color.RED).geodesic(true);
                        mm[0] = null;
                        polylines[0] = null;
                        k[0] = 0.0;
                        preLatLng[0] = new LatLng(0, 0);
                        previousLatlng[0] = new LatLng(0, 0);

                    }
                }
            }
        });

        drawWayPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!wayPointValue) {
                    wpLatLng = new ArrayList<>();
                    wayPointValue = true;
                    isWayStart = true;
                    autoLine.setVisibility(View.GONE);
                    drawLine.setVisibility(View.GONE);
                    instantWay.setVisibility(View.VISIBLE);
                    drawWayPoint.setText("সংরক্ষণ করুন / Save WayPoint");
                    Toast.makeText(getApplicationContext(),"আরও ভাল পজিশন পেতে অনুগ্রহ করে ৫ থেকে ১০ সেকেন্ড অপেক্ষা করুন",Toast.LENGTH_LONG).show();
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
                } else {
                    if (wpLatLng.size() == 0) {
                        Toast.makeText(getApplicationContext(), "আগে চিহ্নিত করুন তারপরে সংরক্ষণ করুন", Toast.LENGTH_SHORT).show();
                    }else {
                        wayPointValue = false;
                        saveFile.setEnabled(true);
                        if (val.equals("TRACK")) {
                            autoLine.setVisibility(View.VISIBLE);
                            drawLine.setVisibility(View.VISIBLE);
                        }
                        else {
                            autoLine.setVisibility(View.GONE);
                            drawLine.setVisibility(View.GONE);
                        }

                        instantWay.setVisibility(View.GONE);
                        removeLast.setVisibility(View.GONE);

                        drawWayPoint.setText("স্থাপনা/WayPoint");
                        // notun code
                        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
                        //
                        for (int i = 0; i < wpLatLng.size(); i++) {

                            String wpt = "\t<wpt lat=\""+ wpLatLng.get(i).latitude +"\" lon=\""+ wpLatLng.get(i).longitude+"\">\n" +
                                    "\t\t<name>TTIT</name>\n" +
                                    "\t</wpt>";
                            trk.add(wpt);
                        }

                        wpLatLng.clear();
                        wpLatLng = new ArrayList<>();

                        preWayPoint[0] = new LatLng(0,0);
                        checkLatLng[0] = new LatLng(0,0);
                        instantWayLatLng[0] = new LatLng(0,0);
                        wwpp[0] = null;
                    }
                }
            }
        });

        clearMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.clear();
                fusedLocationProviderClient.removeLocationUpdates(locationCallback);
                i[0] = 0;
                option[0] = new PolylineOptions().width(5).color(Color.RED).geodesic(true);
                mm[0] = null;
                polylines[0] = null;
                nop[0] = new PolylineOptions().width(5).color(Color.RED).geodesic(true);
                j[0] = 0.0;
                k[0] = 0.0;
                preLatLng[0] = new LatLng(0, 0);
                previousLatlng[0] = new LatLng(0, 0);
                local[0] = 0;
                autoPreLatlng[0] = new LatLng(0, 0);
                lastLatLongitude[0] = new LatLng(0,0);
                preWayPoint[0] = new LatLng(0,0);
                checkLatLng[0] = new LatLng(0,0);
                instantWayLatLng[0] = new LatLng(0,0);
                wwpp[0] = null;
                w[0] = 0.0;
                gpxLatLng.clear();
                gpxLatLng = new ArrayList<>();
                autoGpxLatLng.clear();
                autoGpxLatLng= new ArrayList<>();
                wpLatLng.clear();
                wpLatLng = new ArrayList<>();
                trk.clear();
                trk = new ArrayList<>();

                lineValue = true;
                autoLineValue = false;
                wayPointValue= false;
                isStart = false;
                isWayStart = false;
                fromMap = false;
                fromButton = false;
                lineStart = "0";


                drawLine.setVisibility(View.VISIBLE);
                drawLine.setText("রাস্তা/Track (Manual)");
                drawWayPoint.setVisibility(View.VISIBLE);
                drawWayPoint.setText("স্থাপনা/WayPoint");
                autoLine.setVisibility(View.VISIBLE);
                autoLine.setText("রাস্তা/Track (Automatic)");
                removeLast.setVisibility(View.GONE);
                instantWay.setVisibility(View.GONE);
                clearMap.setVisibility(View.VISIBLE);
                saveFile.setEnabled(false);
                if (val.equals("TRACK")) {
//            instantWay.setVisibility(View.GONE);
                    drawWayPoint.setVisibility(View.GONE);
                    autoLine.setVisibility(View.VISIBLE);
                    drawLine.setVisibility(View.VISIBLE);
                }
                else if (val.equals("WAYPOINT")){
//            instantWay.setVisibility(View.VISIBLE);
                    drawWayPoint.setVisibility(View.VISIBLE);
                    autoLine.setVisibility(View.GONE);
                    drawLine.setVisibility(View.GONE);
                }
            }
        });

        saveFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!gpxContent.isEmpty()) {
                    MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(GpxCreationMap.this)
                            .setTitle("Add GPX File!")
                            .setMessage("Do you want to add a new GPX File replacing the previous one?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    gpxContent = "";
                                    String innnn = INTERNAL_NO.replace("/","_");
                                    gpxContent = XML_HEADER + "\n" + TAG_GPX + "\n";
                                    for (int i = 0; i < trk.size(); i++) {
                                        gpxContent = gpxContent + "\n" + trk.get(i);
                                    }
                                    gpxContent = gpxContent + "\n</gpx>";
                                    System.out.println(gpxContent);
                                    gpxFileLayout.setVisibility(View.VISIBLE);
                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale.getDefault());
                                    Date c = Calendar.getInstance().getTime();
                                    String f = simpleDateFormat.format(c);
                                    String n = innnn + "_" + f+".gpx";
                                    gpxFileName.setText(n);
                                    if (val.equals("TRACK")) {
                                        addTrack.setVisibility(View.VISIBLE);
                                        addWaypoint.setVisibility(View.GONE);
                                    }
                                    else if (val.equals("WAYPOINT")){
                                        addTrack.setVisibility(View.GONE);
                                        addWaypoint.setVisibility(View.VISIBLE);
                                    }

                                    fusedLocationProviderClient.removeLocationUpdates(locationCallback);
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
                else {
                    MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(GpxCreationMap.this)
                            .setTitle("Add GPX File!")
                            .setMessage("Do you want to add a new GPX File?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    gpxContent = "";
                                    String innnn = INTERNAL_NO.replace("/","_");
                                    gpxContent = XML_HEADER + "\n" + TAG_GPX + "\n";
                                    for (int i = 0; i < trk.size(); i++) {
                                        gpxContent = gpxContent + "\n" + trk.get(i);
                                    }
                                    gpxContent = gpxContent + "\n</gpx>";
                                    System.out.println(gpxContent);
                                    gpxFileLayout.setVisibility(View.VISIBLE);
                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale.getDefault());
                                    Date c = Calendar.getInstance().getTime();
                                    String f = simpleDateFormat.format(c);
                                    String n = innnn + "_" + f+".gpx";
                                    gpxFileName.setText(n);
                                    if (val.equals("TRACK")) {
                                        addTrack.setVisibility(View.VISIBLE);
                                        addWaypoint.setVisibility(View.GONE);
                                    }
                                    else if (val.equals("WAYPOINT")){
                                        addTrack.setVisibility(View.GONE);
                                        addWaypoint.setVisibility(View.VISIBLE);
                                    }
                                    fusedLocationProviderClient.removeLocationUpdates(locationCallback);
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
            }
        });

    }

    public void zoomToUserLocation(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            Log.i("Ekhane", "1");
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);

        Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();
        locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
//                Log.i("lattt", location.toString());
                LatLng latLng = new LatLng(23.6850, 90.3563);


                if (location != null) {
                    latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    System.out.println(latLng);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
                } else {
                    latLng = new LatLng(23.6850, 90.3563);
                    System.out.println(latLng);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 7));
                }

            }
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("পিছনে যান/GO BACK")
                .setMessage("আপনি কি আগের পেইজ এ যেতে চান?/Are you sure want to go back?")
                .setPositiveButton("হ্যাঁ/YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
                        finish();
                    }
                })
                .setNegativeButton("না/NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        AlertDialog alert = builder.create();
        alert.setCanceledOnTouchOutside(false);
        alert.setCancelable(false);
        alert.show();
    }

}