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
import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.rosemaryapp.amazingspinner.AmazingSpinner;
import com.shuvo.ttit.trkabikha.R;
import com.shuvo.ttit.trkabikha.arraylist.ChoiceList;
import com.shuvo.ttit.trkabikha.arraylist.ImageCapturedList;
import com.shuvo.ttit.trkabikha.arraylist.LocationLists;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
        if (firstBitmap != null)
        imageView.setImageBitmap(firstBitmap);


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
}
