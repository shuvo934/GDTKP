package com.shuvo.ttit.trkabikha.dialogue;


import static com.shuvo.ttit.trkabikha.projectUpdate.editProject.ProjectEdit.INTERNAL_NO;
import static com.shuvo.ttit.trkabikha.projectUpdate.editProject.ProjectEdit.TAG_GPX;
import static com.shuvo.ttit.trkabikha.projectUpdate.editProject.ProjectEdit.XML_HEADER;
import static com.shuvo.ttit.trkabikha.projectUpdate.editProject.ProjectEdit.addTrack;
import static com.shuvo.ttit.trkabikha.projectUpdate.editProject.ProjectEdit.addWaypoint;
import static com.shuvo.ttit.trkabikha.projectUpdate.editProject.ProjectEdit.currentPhotoPath;
import static com.shuvo.ttit.trkabikha.projectUpdate.editProject.ProjectEdit.firstBitmap;
import static com.shuvo.ttit.trkabikha.projectUpdate.editProject.ProjectEdit.gpxContent;
import static com.shuvo.ttit.trkabikha.projectUpdate.editProject.ProjectEdit.gpxFileLayout;
import static com.shuvo.ttit.trkabikha.projectUpdate.editProject.ProjectEdit.gpxFileName;
import static com.shuvo.ttit.trkabikha.projectUpdate.editProject.ProjectEdit.imageCapturedAdapter;
import static com.shuvo.ttit.trkabikha.projectUpdate.editProject.ProjectEdit.imageCapturedLists;
import static com.shuvo.ttit.trkabikha.projectUpdate.editProject.ProjectEdit.imageFileName;
import static com.shuvo.ttit.trkabikha.projectUpdate.editProject.ProjectEdit.locationListsCreate;
import static com.shuvo.ttit.trkabikha.projectUpdate.editProject.ProjectEdit.targetLocation;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Address;
import android.location.Geocoder;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.core.widget.NestedScrollView;

import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.rosemaryapp.amazingspinner.AmazingSpinner;
import com.shuvo.ttit.trkabikha.R;
import com.shuvo.ttit.trkabikha.arraylist.ChoiceList;
import com.shuvo.ttit.trkabikha.arraylist.ImageCapturedList;
import com.shuvo.ttit.trkabikha.arraylist.LocationLists;
import com.shuvo.ttit.trkabikha.projectUpdate.editProject.ProjectEdit;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class ImageDialogue extends AppCompatDialogFragment {

    ImageView imageView;
    TextInputEditText fileName;
    TextInputLayout fileLayout;
    TextView imageStageLayout;
    AmazingSpinner imageStageSpinner;
    Button cancel;
    Button save;

    ArrayList<ChoiceList> imageStageTypeLists;

    String imageStageName = "";
    String imageStageId = "";
    NestedScrollView fullLayout;
    CircularProgressIndicator circularProgressIndicator;
    String address = "";

    AlertDialog dialog;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.image_view, null);

        imageView = view.findViewById(R.id.image_captured);
        fileName = view.findViewById(R.id.editTextImage);
        fileLayout = view.findViewById(R.id.editTextImage_layout);
        save = view.findViewById(R.id.save_image);
        cancel = view.findViewById(R.id.cancel_save_image);
        imageStageLayout = view.findViewById(R.id.project_image_stage_spinner_layout);
        imageStageLayout.setVisibility(View.GONE);
        imageStageSpinner = view.findViewById(R.id.project_image_stage_spinner);
        fullLayout = view.findViewById(R.id.image_load_full_layout);
        circularProgressIndicator = view.findViewById(R.id.progress_indicator_image_load);
        circularProgressIndicator.setVisibility(View.GONE);

        imageStageTypeLists = new ArrayList<>();

        imageStageTypeLists.add(new ChoiceList("1", "Pre-Work"));
        imageStageTypeLists.add(new ChoiceList("2","On-Working"));
        imageStageTypeLists.add(new ChoiceList("3","Finish-Work"));

        builder.setView(view);

        dialog = builder.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        ArrayList<String> type = new ArrayList<>();
        for(int i = 0; i < imageStageTypeLists.size(); i++) {
            type.add(imageStageTypeLists.get(i).getName());
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(),R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type);

        imageStageSpinner.setAdapter(arrayAdapter);

        imageStageSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                imageStageLayout.setVisibility(View.GONE);
                String name = adapterView.getItemAtPosition(i).toString();
                for (int j = 0; j < imageStageTypeLists.size(); j++) {
                    if (name.equals(imageStageTypeLists.get(j).getName())) {
                        imageStageId = (imageStageTypeLists.get(j).getId());
                        imageStageName = name;
                    }
                }
                System.out.println(imageStageId);
            }
        });

        fileName.setText(imageFileName);
//        if (firstBitmap != null)
//        imageView.setImageBitmap(firstBitmap);


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File deletefile = new File(currentPhotoPath);
                boolean deleted = deletefile.delete();
                if (deleted) {
                    System.out.println("deleted");
                }
                dialog.dismiss();
            }
        });

//        fileName.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                fileLayout.setHint("ফাইলের নাম / File Name");
//                return false;
//            }
//        });

        fileName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT || event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER &&
                        event.getKeyCode() == KeyEvent.KEYCODE_NAVIGATE_NEXT) {
                    if (event == null || !event.isShiftPressed()) {
                        // the user is done typing.
                        Log.i("Let see", "Come here");
                        fileName.clearFocus();
                        closeKeyBoard();

                        return false; // consume.
                    }
                }
                return false;
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                if (imageStageId.isEmpty()) {
                    imageStageLayout.setVisibility(View.VISIBLE);
                }
                else {
                    imageStageLayout.setVisibility(View.GONE);
                    if (fileName.getText().toString().isEmpty()) {
                        Toast.makeText(getContext(), "Please Write Your File Name", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        // deleting file
                        File deletefile = new File(currentPhotoPath);
                        boolean deleted = deletefile.delete();
                        if (deleted) {
                            System.out.println("deleted");
                        }

                        FileOutputStream fileOutputStream = null;
//                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
//                    System.out.println(timeStamp);
                        String imageFileName = fileName.getText().toString() +".jpg";
                        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/DCIM/Camera/" + imageFileName);
                        try {
                            file.createNewFile();
                            currentPhotoPath = file.getAbsolutePath();
                            fileOutputStream = new FileOutputStream(file);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        firstBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                        try {
                            fileOutputStream.flush();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            fileOutputStream.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        galleryAddPic();

                        String imaaa = file.getAbsolutePath();

                        //Writes Exif Information to the Image
                        try {
                            ExifInterface exif = new ExifInterface(imaaa);
                            Log.w("Location", String.valueOf(targetLocation));

                            exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE, convert(targetLocation.getLatitude()));
                            exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, latitudeRef(targetLocation.getLatitude()));
                            exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, convert(targetLocation.getLongitude()));
                            exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, longitudeRef(targetLocation.getLongitude()));

                            exif.saveAttributes();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        scanFile(getContext(),currentPhotoPath, null);

                        Toast.makeText(getContext(), "Photo is saved", Toast.LENGTH_SHORT).show();
                        imageCapturedLists.add(new ImageCapturedList(firstBitmap,imageFileName,false,imageStageId));
                        File deletefile1 = new File(currentPhotoPath);
                        boolean deleted1 = deletefile1.delete();
                        if (deleted1) {
                            System.out.println("deleted");
                        }
                        System.out.println(imageCapturedLists.size());
                        imageCapturedAdapter.notifyDataSetChanged();

                        if (!gpxContent.isEmpty()) {
                            MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(getContext())
                                    .setTitle("Add GPX File!")
                                    .setMessage("Do you want to add a Single WAYPOINT as GPX File from this Image Location replacing the previous one?")
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            String innnn = INTERNAL_NO.replace("/","_");
                                            gpxContent = "";
                                            locationListsCreate = new ArrayList<>();
                                            String wpt = "\t<wpt lat=\""+ targetLocation.getLatitude() +"\" lon=\""+ targetLocation.getLongitude()+"\">\n" +
                                                    "\t\t<name>"+innnn+"</name>\n" +
                                                    "\t</wpt>";

                                            gpxContent = XML_HEADER + "\n" + TAG_GPX + "\n" + wpt + "\n</gpx>";
                                            locationListsCreate.add(new LocationLists(String.valueOf(targetLocation.getLatitude()),String.valueOf(targetLocation.getLongitude()),0));
                                            System.out.println(gpxContent);
                                            gpxFileLayout.setVisibility(View.VISIBLE);
                                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale.getDefault());
                                            Date c = Calendar.getInstance().getTime();
                                            String f = simpleDateFormat.format(c);
                                            String n = innnn + "_" + f+".gpx";
                                            gpxFileName.setText(n);
                                            addTrack.setVisibility(View.GONE);
                                            addWaypoint.setVisibility(View.VISIBLE);
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
                            MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(getContext())
                                    .setTitle("Add GPX File!")
                                    .setMessage("Do you want to add a Single WAYPOINT as GPX File from this Image Location?")
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String innnn = INTERNAL_NO.replace("/","_");
                                            gpxContent = "";
                                            locationListsCreate = new ArrayList<>();
                                            String wpt = "\t<wpt lat=\""+ targetLocation.getLatitude() +"\" lon=\""+ targetLocation.getLongitude()+"\">\n" +
                                                    "\t\t<name>"+innnn+"</name>\n" +
                                                    "\t</wpt>";

                                            gpxContent = XML_HEADER + "\n" + TAG_GPX + "\n" + wpt + "\n</gpx>";
                                            locationListsCreate.add(new LocationLists(String.valueOf(targetLocation.getLatitude()),String.valueOf(targetLocation.getLongitude()),0));
                                            System.out.println(gpxContent);
                                            gpxFileLayout.setVisibility(View.VISIBLE);
                                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale.getDefault());
                                            Date c = Calendar.getInstance().getTime();
                                            String f = simpleDateFormat.format(c);
                                            String n = innnn + "_" + f+".gpx";
                                            gpxFileName.setText(n);
                                            addTrack.setVisibility(View.GONE);
                                            addWaypoint.setVisibility(View.VISIBLE);
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

                        dialog.dismiss();


                    }
                }
            }
        });

        new getAddress().execute();
        return dialog;
    }

    private void closeKeyBoard() {
        View view = requireActivity().getCurrentFocus();
        if (view != null) {
            System.out.println("FOCUS Check");
            view.clearFocus();
            InputMethodManager mgr = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        getContext().sendBroadcast(mediaScanIntent);

    }

    private static final String convert(double latitude) {
        latitude= Math.abs(latitude);
        int degree = (int) latitude;
        latitude *= 60;
        latitude -= (degree * 60.0d);
        int minute = (int) latitude;
        latitude *= 60;
        latitude -= (minute * 60.0d);
        int second = (int) (latitude*1000.0d);

        StringBuilder sb = new StringBuilder(20);
        sb.append(degree);
        sb.append("/1,");
        sb.append(minute);
        sb.append("/1,");
        sb.append(second);
        sb.append("/1000");
        return sb.toString();
    }

    private static String latitudeRef(double latitude) {
        return latitude<0.0d?"S":"N";
    }

    private static String longitudeRef(double longitude) {
        return longitude<0.0d?"W":"E";
    }

    public static void scanFile(Context context, String path, String mimeType ) {
        Client client = new Client(path, mimeType);
        MediaScannerConnection connection =
                new MediaScannerConnection(context, client);
        client.connection = connection;
        connection.connect();
    }

    private static final class Client implements MediaScannerConnection.MediaScannerConnectionClient {
        private final String path;
        private final String mimeType;
        MediaScannerConnection connection;

        public Client(String path, String mimeType) {
            this.path = path;
            this.mimeType = mimeType;
        }

        @Override
        public void onMediaScannerConnected() {
            connection.scanFile(path, mimeType);
        }

        @Override
        public void onScanCompleted(String path, Uri uri) {
            connection.disconnect();
        }
    }

    public class getAddress extends  AsyncTask<Void,Void,Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            circularProgressIndicator.setVisibility(View.VISIBLE);
            fullLayout.setVisibility(View.GONE);

        }

        @Override
        protected Void doInBackground(Void... voids) {

            double lat = targetLocation.getLatitude();
            double lng = targetLocation.getLongitude();

            Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
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
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yy, hh:mm:ss a", Locale.getDefault());
            Date c = Calendar.getInstance().getTime();
            String dd = simpleDateFormat.format(c);
            System.out.println(dd);
            String timeLatLng = "Time: " + dd + "\n" + "Latitude: " + targetLocation.getLatitude() + "\n" + "Longitude: " + targetLocation.getLongitude();
            address = timeLatLng + "\n"+ "Address: " + address;
            System.out.println(address);
            Resources resources = getResources();
            float scale = resources.getDisplayMetrics().density;
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
//                            float x = (firstBitmap.getWidth() - textWidth)/2;
//                            float y = (firstBitmap.getHeight() - textHeight)/2;


            // draw text to the Canvas center
            int yyyy = firstBitmap.getHeight() - textHeight - 16;
            canvas.save();
            canvas.translate(5, yyyy);
            textLayout.draw(canvas);
            canvas.restore();
            if (firstBitmap != null) {
                Glide.with(getActivity().getApplicationContext())
                    .load(firstBitmap)
                    .error(R.drawable.loading_error)
                    .placeholder(R.drawable.loading)
                    .into(imageView);
            }
            circularProgressIndicator.setVisibility(View.GONE);
            fullLayout.setVisibility(View.VISIBLE);
        }
    }
}
