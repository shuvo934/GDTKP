package com.shuvo.ttit.trkabikha.projectsWithMap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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
import com.shuvo.ttit.trkabikha.adapter.ProjectMapAdapter;
import com.shuvo.ttit.trkabikha.arraylist.LocationLists;
import com.shuvo.ttit.trkabikha.arraylist.MarkerData;
import com.shuvo.ttit.trkabikha.arraylist.PolyLindata;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

import static com.shuvo.ttit.trkabikha.mainmenu.HomePage.projectMapsLists;

public class ProjectsMaps extends AppCompatActivity implements OnMapReadyCallback,ProjectMapAdapter.ClickedItem {

    private GoogleMap mMap;
    RecyclerView itemView;
    ProjectMapAdapter projectMapAdapter;
    RecyclerView.LayoutManager layoutManager;

    ArrayList<MarkerData> markerData;
    ArrayList<PolyLindata> polyLindata;
    public static int selectedAdapterPosition = -1;
    public static int viewTop = 0;
    public static int viewBottom = 0;

    private Spinner selection;

    Boolean fullScreen = false;
    CardView projectCard;
    ImageView imageView;

    final Random mRandom = new Random(System.currentTimeMillis());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        itemView = findViewById(R.id.project_location_details_review);
        selection = findViewById(R.id.spinnnnn_multi);
        projectCard = findViewById(R.id.project_card);
        imageView = findViewById(R.id.full_screen_changer_map_view);

        itemView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        itemView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(itemView.getContext(),DividerItemDecoration.VERTICAL);
        itemView.addItemDecoration(dividerItemDecoration);

        projectMapAdapter = new ProjectMapAdapter(projectMapsLists, ProjectsMaps.this, ProjectsMaps.this);
        ScaleInAnimationAdapter animationAdapter = new ScaleInAnimationAdapter(projectMapAdapter);
        animationAdapter.setDuration(500);
        animationAdapter.setInterpolator(new AccelerateDecelerateInterpolator());
        animationAdapter.setFirstOnly(false);
        itemView.setAdapter(animationAdapter);


        markerData = new ArrayList<>();
        polyLindata = new ArrayList<>();
        List<String> categories = new ArrayList<String>();
        categories.add("NORMAL");
        categories.add("SATELLITE");
        categories.add("TERRAIN");
        categories.add("HYBRID");
        categories.add("NO LANDMARK");

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, categories);

        spinnerAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        selection.setAdapter(spinnerAdapter);


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!fullScreen) {
                    projectCard.setVisibility(View.GONE);
                    imageView.setImageResource(R.drawable.fullscreen_exit);
                    fullScreen = true;
                } else {
                    projectCard.setVisibility(View.VISIBLE);
                    imageView.setImageResource(R.drawable.fullscreen);
                    fullScreen = false;
                }


            }
        });
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

//        try {
//            // Customise the styling of the base map using a JSON object defined
//            // in a raw resource file.
//            boolean success = googleMap.setMapStyle(
//                    MapStyleOptions.loadRawResourceStyle(
//                            this, R.raw.no_landmark));
//
//            if (!success) {
//                Log.i("Failed ", "Style parsing failed.");
//            }
//        } catch (Resources.NotFoundException e) {
//            Log.e("Style ", "Can't find style. Error: ", e);
//        }

        mMap.getUiSettings().setZoomControlsEnabled(true);
        // Add a marker in Sydney and move the camera
        //LatLng bangladesh = new LatLng(23.6850, 90.3563);
        //LatLng bangladesh = new LatLng(23.664745, 90.167949);
        LatLng bangladesh = new LatLng(23.9697833326, 90.2372869849);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bangladesh, 12));

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
                                            ProjectsMaps.this, R.raw.normal));

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
                                            ProjectsMaps.this, R.raw.no_landmark));

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

        for (int i = 0 ; i < projectMapsLists.size(); i++) {
            ArrayList<LocationLists> locationLists = projectMapsLists.get(i).getLocationLists();
            String proName = projectMapsLists.get(i).getPcmProjectName();
            String proNo = projectMapsLists.get(i).getPcmProjectNo();
            String projectCode = projectMapsLists.get(i).getPcmProjectCode();
            //String proDate = projectMapsLists.get(i).getPcmProjectDate();
            String pcmId = projectMapsLists.get(i).getPcmId();
            String stype = projectMapsLists.get(i).getSanctionType();
            String totalVal = "";
            if (stype.contains("Taka")) {
                DecimalFormat formatter = new DecimalFormat("##,##,##,###");
                double val = 0.0;
                if (projectMapsLists.get(i).getPcmEstimateProjectValue() != null) {
                    val = Double.parseDouble(projectMapsLists.get(i).getPcmEstimateProjectValue());
                }
                String formatted = formatter.format(val);
                totalVal = stype + " " + formatted;
            }
            else {
                totalVal = stype + " " + projectMapsLists.get(i).getPcmEstimateProjectValue();

            }

            String finYear = projectMapsLists.get(i).getFyFinancialYearName();
            String dateC = projectMapsLists.get(i).getPcmProjectDate().substring(0, 10);
            System.out.println(dateC);

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy", Locale.getDefault());

            String proDate = "";
            Date date = null;

            try {
                date = df.parse(dateC);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (date != null) {
                proDate = sdf.format(date);
            }
            if (locationLists.size() != 0) {
                if (locationLists.size() == 1 ) {
                    LatLng latLng = new LatLng(Double.parseDouble(locationLists.get(0).getLatitude()),Double.parseDouble(locationLists.get(0).getLongitude()));
                    Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).title(proName)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_micro_18))
                            .snippet("Project No (প্রকল্প নং): "+ proNo+"\nProject Code (প্রকল্প কোড): "+projectCode+"\nProject Date: "+proDate+
                                    "\nEstimated Value: " + totalVal + "\nFinancial Year: " + finYear));
                    markerData.add(new MarkerData(marker,pcmId,false));
                }
                else {
                    int color = generateRandomColor();

//                    PolylineOptions option = new PolylineOptions().width(16)
//                            .color(Color.BLACK)
//                            .geodesic(true).clickable(true)
//                            .zIndex(Float.parseFloat(pcmId));
//                    PolylineOptions option1 = new PolylineOptions().width(10)
//                            //.color(Color.parseColor("#00cec9"))
//                            //.color(Color.CYAN)
//                            .color(color)
//                            .geodesic(true).clickable(true)
//                            .zIndex(Float.parseFloat(pcmId));
//                    for (int j = 0 ; j < locationLists.size(); j++ ) {
//                        LatLng point = new LatLng(Double.parseDouble(locationLists.get(j).getLatitude()), Double.parseDouble(locationLists.get(j).getLongitude()));
//                        option.add(point);
//                        option1.add(point);
//                    }


                    int segment = 0;
                    for (int j = 0; j < locationLists.size(); j++) {
                        if (locationLists.get(j).getSegment() > segment) {
                            segment = locationLists.get(j).getSegment();
                        }
                    }

                    for (int s = 0; s <= segment; s++) {
                        int pointNumber = 0;
                        LatLng point = null;
                        PolylineOptions option = new PolylineOptions().width(16)
                                .color(Color.BLACK)
                                .geodesic(true).clickable(true)
                                .zIndex(Float.parseFloat(pcmId));
                        PolylineOptions option1 = new PolylineOptions().width(10)
                                .color(color)
                                .geodesic(true).clickable(true)
                                .zIndex(Float.parseFloat(pcmId));

                        for (int j = 0; j < locationLists.size(); j++) {

                            if (s == locationLists.get(j).getSegment()) {
                                pointNumber++;
                                point = new LatLng(Double.parseDouble(locationLists.get(j).getLatitude()), Double.parseDouble(locationLists.get(j).getLongitude()));
                                option.add(point);
                                option1.add(point);
                            }
                        }

                        if (pointNumber == 1) {
                            Marker marker = mMap.addMarker(new MarkerOptions().position(point).title(proName)
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_micro_18))
                                    .snippet("Project No (প্রকল্প নং): "+ proNo+"\nProject Code (প্রকল্প কোড): "+projectCode+"\nProject Date: "+proDate+
                                            "\nEstimated Value: " + totalVal + "\nFinancial Year: " + finYear));
                            markerData.add(new MarkerData(marker,pcmId,false));
                        }
                        else if (pointNumber > 1) {

                            Polyline polyline = mMap.addPolyline(option);
                            Polyline polyline1 = mMap.addPolyline(option1);
                            polyLindata.add(new PolyLindata(polyline,pcmId));

                            int a = polyline.getPoints().size()/2;

                            LatLng latLng = new LatLng(polyline.getPoints().get(a).latitude,polyline.getPoints().get(a).longitude);
                            Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).title(proName)
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.transparent_circle))
                                    .anchor((float) 0.5,(float) 0.5)
                                    .snippet("Project No (প্রকল্প নং): "+ proNo+"\nProject Code (প্রকল্প কোড): "+projectCode+"\nProject Date: "+proDate+
                                            "\nEstimated Value: " + totalVal + "\nFinancial Year: " + finYear));
                            markerData.add(new MarkerData(marker,pcmId,true));
                        }

                    }

//                    Polyline polyline = mMap.addPolyline(option);
//                    //option.color(Color.CYAN).width(10);
//                    Polyline polyline1 = mMap.addPolyline(option1);
//                    polyLindata.add(new PolyLindata(polyline,pcmId));
//
//                    int a = locationLists.size()/2;
//
//                    LatLng latLng = new LatLng(Double.parseDouble(locationLists.get(a).getLatitude()),Double.parseDouble(locationLists.get(a).getLongitude()));
//                    Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).title(proName)
//                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.transparent_circle))
//                            .anchor((float) 0.5,(float) 0.5)
//                            .snippet("Project No (প্রকল্প নং): "+ proNo+"\nProject Code (প্রকল্প কোড): "+projectCode+"\nProject Date: "+proDate));
//                    markerData.add(new MarkerData(marker,pcmId,true));

                }
            }
        }

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 15));

                String pcmId = "";
                for (int i = 0 ; i < markerData.size(); i++) {
                    Marker marker1 = markerData.get(i).getMarker();
                    boolean isPoly = markerData.get(i).isPoly();
                    if (!isPoly) {
                        marker1.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker_micro_18));
                    }
//                    else {
//                        for (int j = 0; j < polyLindata.size(); j++) {
//                            Polyline polyline = polyLindata.get(j).getPolyline();
//                            polyline.setColor(Color.BLACK);
//                            polyline.setWidth(16);
//                        }
//                    }
                    String id = marker.getId();
                    String allId = marker1.getId();


                    if (id.equals(allId)) {
                        if (!isPoly) {
                            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker_micro_24_select));
                            pcmId = markerData.get(i).getId();
                            for (int j = 0 ; j< projectMapsLists.size(); j++) {
                                String marId = projectMapsLists.get(j).getPcmId();
                                if (pcmId.equals(marId)) {
                                    selectedAdapterPosition = j;
                                }
                            }
                        }
                        else {
                            pcmId = markerData.get(i).getId();
                            for (int j = 0 ; j< projectMapsLists.size(); j++) {
                                String marId = projectMapsLists.get(j).getPcmId();
                                if (pcmId.equals(marId)) {
                                    selectedAdapterPosition = j;
                                }
                            }
                        }
//                        else {
//                            for (int j = 0; j < polyLindata.size(); j++) {
//                                Polyline polyline = polyLindata.get(j).getPolyline();
//                                String poid = polyLindata.get(j).getId();
//                                if (poid.equals(markerid)) {
//                                    System.out.println("ASHE EKHANE");
//                                    System.out.println(polyline.getZIndex());
//                                    System.out.println(polyline.getPoints());
//                                    polyline.setColor(Color.parseColor("#0984e3"));
//                                    polyline.setWidth(30);
//                                } else {
//                                    polyline.setColor(Color.BLACK);
//                                    polyline.setWidth(16);
//                                }
//
//                            }
//                        }

                    }
                }
                if (!pcmId.isEmpty()) {
                    for (int i = 0 ; i < polyLindata.size(); i++) {

                        System.out.println("PCMID: "+pcmId);
                        Polyline polyline = polyLindata.get(i).getPolyline();
                        String poid = polyLindata.get(i).getId();
                        if (poid.equals(pcmId)) {
                            polyline.setColor(Color.parseColor("#0984e3"));
                            polyline.setWidth(30);
                            //                            int size = polyline.getPoints().size();
                            //                            size = size / 2;
                            //                            double latitude = polyline.getPoints().get(size).latitude;
                            //                            double longitude = polyline.getPoints().get(size).longitude;
                            //                            LatLng gpx = new LatLng(latitude, longitude);
                            //                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(gpx, 15));
                        } else {
                            polyline.setColor(Color.BLACK);
                            polyline.setWidth(16);
                        }

                    }
                }

                projectMapAdapter.notifyDataSetChanged();
                itemView.scrollToPosition(selectedAdapterPosition);
                return false;
            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                for (int i = 0 ; i < polyLindata.size(); i++) {
                    Polyline polyline = polyLindata.get(i).getPolyline();
                    polyline.setColor(Color.BLACK);
                    polyline.setWidth(16);
                }
                for (int i = 0; i < markerData.size(); i++) {
                    Marker marker = markerData.get(i).getMarker();
                    boolean isPoly = markerData.get(i).isPoly();
                    if (!isPoly) {
                        marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker_micro_18));
                    }
                    marker.hideInfoWindow();
                }
                selectedAdapterPosition = -1;
                projectMapAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onCategoryClicked(int CategoryPosition) {

        String pcmId = projectMapsLists.get(CategoryPosition).getPcmId();

        for (int i = 0; i < markerData.size(); i++) {
            String id = markerData.get(i).getId();
            boolean isPoly = markerData.get(i).isPoly();
            Marker marker = markerData.get(i).getMarker();

            if (id.equals(pcmId) && !isPoly) {
                marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker_micro_24_select));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 15));
                marker.showInfoWindow();
            }
            else if (!isPoly) {
                marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker_micro_18));
                marker.hideInfoWindow();
            }
            else if (id.equals(pcmId)) {
                marker.showInfoWindow();
            }
            else {

                marker.hideInfoWindow();
            }

        }

        for (int i = 0; i < polyLindata.size(); i++) {
            Polyline polyline = polyLindata.get(i).getPolyline();
            String poid = polyLindata.get(i).getId();
            if (poid.equals(pcmId)) {
                polyline.setColor(Color.parseColor("#0984e3"));
                polyline.setWidth(30);
                int size = polyline.getPoints().size();
                size = size / 2;
                double latitude = polyline.getPoints().get(size).latitude;
                double longitude = polyline.getPoints().get(size).longitude;
                LatLng gpx = new LatLng(latitude, longitude);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(gpx, 15));
            } else {
                polyline.setColor(Color.BLACK);
                polyline.setWidth(16);
            }
        }

        if (selectedAdapterPosition != CategoryPosition) {
            selectedAdapterPosition = CategoryPosition;
        }
        projectMapAdapter.notifyDataSetChanged();
    }

    public int generateRandomColor() {
        // This is the base color which will be mixed with the generated one
        final int baseColor = Color.WHITE;

        final int baseRed = Color.red(baseColor);
        final int baseGreen = Color.green(baseColor);
        final int baseBlue = Color.blue(baseColor);

        final int red = (baseRed + mRandom.nextInt(256)) / 2;
        final int green = (baseGreen + mRandom.nextInt(256)) / 2;
        final int blue = (baseBlue + mRandom.nextInt(256)) / 2;

        return Color.rgb(red, green, blue);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        selectedAdapterPosition = -1;
    }
}