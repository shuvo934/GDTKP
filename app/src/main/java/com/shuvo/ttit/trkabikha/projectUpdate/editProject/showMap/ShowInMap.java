package com.shuvo.ttit.trkabikha.projectUpdate.editProject.showMap;

import static com.shuvo.ttit.trkabikha.adapter.ProjectUpdateAdapter.locationListsAdapterPU;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

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
import com.shuvo.ttit.trkabikha.arraylist.LocationLists;
import com.shuvo.ttit.trkabikha.projectDetails.ProjectDetails;

import java.util.ArrayList;
import java.util.List;

public class ShowInMap extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    ArrayList<LocationLists> locationListsDial;

    private Spinner selection;

    public String P_NO = "";
    public String P_CODE = "";
    public String P_NAME = "";
    public String P_DATE = "";
    public String F_YEAR = "";
    public String ES_VAL = "";
    public String S_TYPE = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_in_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_from_project_edit);
        mapFragment.getMapAsync(this);

        selection = findViewById(R.id.spinnnnn_multi3);

        locationListsDial = new ArrayList<>();

        locationListsDial = locationListsAdapterPU;

        Intent intent = getIntent();

        P_NAME = intent.getStringExtra("P_NAME");
        P_NO = intent.getStringExtra("P_NO");
        P_CODE = intent.getStringExtra("P_CODE");
        P_DATE = intent.getStringExtra("P_DATE");
        ES_VAL = intent.getStringExtra("ES_VAL");
        S_TYPE = intent.getStringExtra("S_TYPE");
        F_YEAR = intent.getStringExtra("F_YEAR");

        List<String> categories = new ArrayList<String>();
        categories.add("NORMAL");
        categories.add("SATELLITE");
        categories.add("TERRAIN");
        categories.add("HYBRID");
        categories.add("NO LANDMARK");

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, categories);

        spinnerAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        selection.setAdapter(spinnerAdapter);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

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
                                            ShowInMap.this, R.raw.normal));

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
                                            ShowInMap.this, R.raw.no_landmark));

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
            if (locationListsDial.size() == 1 ) {
                LatLng latLng = new LatLng(Double.parseDouble(locationListsDial.get(0).getLatitude()),Double.parseDouble(locationListsDial.get(0).getLongitude()));
                Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).title(P_NAME)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_micro_36_2))
                        .snippet("Project No (প্রকল্প নং): "+ P_NO+"\nProject Code (প্রকল্প কোড): "+P_CODE+"\nProject Date: "+P_DATE+
                                "\nEstimated Value: " + ES_VAL + " " + S_TYPE + "\nFinancial Year: " + F_YEAR));
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
                                        "\nEstimated Value: " + ES_VAL + " " + S_TYPE + "\nFinancial Year: " + F_YEAR));
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
                                        "\nEstimated Value: " + ES_VAL + " " + S_TYPE + "\nFinancial Year: " + F_YEAR));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                    }

                }

            }
        }

    }
}