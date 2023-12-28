package com.shuvo.ttit.trkabikha.login;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_8;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.shuvo.ttit.trkabikha.R;
import com.shuvo.ttit.trkabikha.arraylist.PICUserDetails;
import com.shuvo.ttit.trkabikha.arraylist.UserDetails;
import com.shuvo.ttit.trkabikha.mainmenu.HomePage;
import com.shuvo.ttit.trkabikha.progressbar.WaitProgress;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PICLogin extends AppCompatActivity {

    TextInputEditText user;
    TextInputEditText pass;

    TextView login_failed;
    TextView contact;

    Button login;

    CheckBox checkBox;
    String userName = "";
    String password = "";

    WaitProgress waitProgress = new WaitProgress();
    private Boolean conn = false;
    private Boolean infoConnected = false;
    private Boolean connected = false;

    private Connection connection;

    SharedPreferences sharedpreferences;

    String getUserName = "";
    String getPassword = "";
    boolean getChecked = false;

    String userId = "";

    String android_id = "";
    String model = "";
    String brand = "";
    String ipAddress = "";
    String hostUserName = "";
    String sessionId = "";
    String osName = "";

    public static final String MyPREFERENCES = "UserPass_PIC_PROJECTMICRO" ;
    public static final String user_emp_code = "nameKey";
    public static final String user_password = "passKey";
    public static final String checked = "trueFalse";

    public static ArrayList<PICUserDetails> picUserDetails;

    private final Handler mHandler = new Handler();

    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_piclogin);

        picUserDetails = new ArrayList<>();

        user = findViewById(R.id.pic_user_name_given_log_in);
        pass = findViewById(R.id.pic_password_given_log_in);
        checkBox = findViewById(R.id.pic_remember_checkbox);

        login_failed = findViewById(R.id.pic_email_pass_miss);
        contact = findViewById(R.id.pic_contact_text);

        login = findViewById(R.id.pic_log_in_button);

        sharedpreferences = getSharedPreferences(MyPREFERENCES,MODE_PRIVATE);
        getUserName = sharedpreferences.getString(user_emp_code,null);
        getPassword = sharedpreferences.getString(user_password,null);
        getChecked = sharedpreferences.getBoolean(checked,false);

        if (getUserName != null) {
            user.setText(getUserName);
        }
        if (getPassword != null) {
            pass.setText(getPassword);
        }
        checkBox.setChecked(getChecked);

        StringBuilder builder = new StringBuilder();
        builder.append("ANDROID: ").append(Build.VERSION.RELEASE);

        Field[] fields = Build.VERSION_CODES.class.getFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            int fieldValue = -1;

            try {
                fieldValue = field.getInt(new Object());
            } catch (IllegalArgumentException | IllegalAccessException | NullPointerException e) {
                e.printStackTrace();
            }

            if (fieldValue == Build.VERSION.SDK_INT) {
                builder.append(": ").append(fieldName);
            }
        }

        System.out.println("OS: " + builder.toString());

        osName = builder.toString();

        android_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        System.out.println(android_id);

        model = android.os.Build.MODEL;

        brand = Build.BRAND;

        ipAddress = getIPAddress(true);

        hostUserName = getHostName("localhost");

        pass.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT || event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER &&
                        event.getKeyCode() == KeyEvent.KEYCODE_NAVIGATE_NEXT) {
                    if (event == null || !event.isShiftPressed()) {
                        // the user is done typing.
                        Log.i("Let see", "Come here");
                        pass.clearFocus();
                        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        mgr.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        closeKeyBoard();

                        return false; // consume.
                    }
                }
                return false;
            }
        });

        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mmm = "info@techterrain-it.com";
                AlertDialog dialog = new AlertDialog.Builder(PICLogin.this)
                        .setMessage("Do you want to send an email to "+mmm+" ?")
                        .setPositiveButton("Yes", null)
                        .setNegativeButton("No",null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        Uri data = Uri.parse("mailto:"+mmm);
                        intent.setData(data);
                        try {
                            startActivity(intent);

                        } catch (android.content.ActivityNotFoundException ex) {
                            Toast.makeText(PICLogin.this, "There is no email app found.", Toast.LENGTH_SHORT).show();
                        }



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
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                closeKeyBoard();

                login_failed.setVisibility(View.GONE);
                userName = user.getText().toString();
                password = pass.getText().toString();

                if (!userName.isEmpty() && !password.isEmpty()) {

//                    new CheckLogin().execute();
                    getUser();

                } else {
                    Toast.makeText(getApplicationContext(), "Please Give User Name and Password", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public static String getHostName(String defValue) {
        try {
            @SuppressLint("DiscouragedPrivateApi") Method getString = Build.class.getDeclaredMethod("getString", String.class);
            getString.setAccessible(true);
            return getString.invoke(null, "net.hostname").toString();
        } catch (Exception ex) {
            return defValue;
        }
    }

    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        boolean isIPv4 = sAddr.indexOf(':')<0;

                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                return delim<0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } // for now eat exceptions
        return "";
    }

    @Override
    public void onBackPressed () {
        View view = this.getCurrentFocus();
        if (view != null) {
            view.clearFocus();
            InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } else {
            super.onBackPressed();
        }

    }

    private void closeKeyBoard () {
        View view = this.getCurrentFocus();
        if (view != null) {
            view.clearFocus();
            InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public boolean onTouchEvent (MotionEvent event){
        closeKeyBoard();
        return super.onTouchEvent(event);
    }

    public void getUser() {
        waitProgress.show(getSupportFragmentManager(), "WaitBar");
        waitProgress.setCancelable(false);
        conn = false;
        connected = false;
        infoConnected = false;

        String get_pic_url = "http://103.56.208.123:8086/terrain/tr_kabikha/user_login/pic_user/"+userName+"/"+password;
        picUserDetails = new ArrayList<>();

        RequestQueue requestQueue = Volley.newRequestQueue(PICLogin.this);

        StringRequest getUserRequest = new StringRequest(Request.Method.GET, get_pic_url, response -> {
            try {
                conn = true;
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    connected = true;
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject userInfo = array.getJSONObject(i);
                        String usr_id = userInfo.getString("usr_id").equals("null") ? "" : userInfo.getString("usr_id");
                        String usr_name = userInfo.getString("usr_name").equals("null") ? "" : userInfo.getString("usr_name");
                        String usr_fname = userInfo.getString("usr_fname").equals("null") ? "" : userInfo.getString("usr_fname");
                        String usr_lname = userInfo.getString("usr_lname").equals("null") ? "" : userInfo.getString("usr_lname");
                        String usr_email = userInfo.getString("usr_email").equals("null") ? "" : userInfo.getString("usr_email");
                        String usr_contact = userInfo.getString("usr_contact").equals("null") ? "" : userInfo.getString("usr_contact");
                        String usr_emp_id = userInfo.getString("usr_emp_id").equals("null") ? "" : userInfo.getString("usr_emp_id");
                        String usr_dist_id = userInfo.getString("usr_dist_id").equals("null") ? "" : userInfo.getString("usr_dist_id");
                        String usr_dd_id = userInfo.getString("usr_dd_id").equals("null") ? "" : userInfo.getString("usr_dd_id");
                        String usr_access_type = userInfo.getString("usr_access_type").equals("null") ? "" : userInfo.getString("usr_access_type");
                        String dist_name = userInfo.getString("dist_name").equals("null") ? "" : userInfo.getString("dist_name");
                        String thana_name = userInfo.getString("thana_name").equals("null") ? "" : userInfo.getString("thana_name");

                        usr_name = transformText(usr_name);
                        usr_fname = transformText(usr_fname);
                        usr_lname = transformText(usr_lname);
                        usr_email = transformText(usr_email);
                        usr_contact = transformText(usr_contact);
                        thana_name = transformText(thana_name);
                        dist_name = transformText(dist_name);
                        usr_access_type = transformText(usr_access_type);

                        picUserDetails.add(new PICUserDetails(usr_id,usr_name, usr_fname, usr_lname,
                                usr_email,usr_contact,usr_emp_id,usr_dist_id,
                                usr_dd_id, usr_access_type,null,null,
                                dist_name,thana_name));
                    }
                    if (picUserDetails.size() != 0) {
                        updateNewRequest(picUserDetails.get(0).getDist_id());
                    }
                }
                else {
                    connected = false;
                    Toast.makeText(getApplicationContext(),"Invalid User",Toast.LENGTH_SHORT).show();
                    updateInterface();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                conn = false;
                updateInterface();

            }

        }, error -> {

            conn = false;
            updateInterface();
        });

        requestQueue.add(getUserRequest);


    }

    public void updateNewRequest(String dist_id) {
        String get_div_url = "http://103.56.208.123:8086/terrain/tr_kabikha/utility_data/div_from_dist?dist_id="+dist_id;
        String login_log_url = "http://103.56.208.123:8086/terrain/tr_kabikha/user_login/login_log";

        RequestQueue requestQueue = Volley.newRequestQueue(PICLogin.this);

        StringRequest loginLogReq = new StringRequest(Request.Method.POST, login_log_url, response -> {
            try {
                conn = true;
                JSONObject jsonObject = new JSONObject(response);
                String string_out = jsonObject.getString("string_out");
                if (string_out.equals("Successfully Created")) {
                    infoConnected = true;
                    updateInterface();
                }
                else {
                    infoConnected = false;
                    updateInterface();
                }

            } catch (JSONException e) {
                e.printStackTrace();
                conn = false;
                updateInterface();
            }
        }, error -> {

            conn = false;
            updateInterface();
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String u_name = picUserDetails.get(0).getUserName();
                String u_id = picUserDetails.get(0).getUser_id();
                headers.put("P_IULL_USER_ID",u_name);
                headers.put("P_IULL_CLIENT_HOST_NAME",brand+" "+model);
                headers.put("P_IULL_CLIENT_IP_ADDR",ipAddress);
                headers.put("P_IULL_CLIENT_HOST_USER_NAME",hostUserName);
                headers.put("P_IULL_SESSION_USER_ID",u_id);
                headers.put("P_IULL_OS_NAME",osName);
                return headers;
            }
        };

        StringRequest getDivPICRequest = new StringRequest(Request.Method.GET, get_div_url, response -> {
            try {
                conn = true;
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject divInfo = array.getJSONObject(i);
                        String div_name = divInfo.getString("div_name");
                        String div_id = divInfo.getString("div_id");
                        if (div_name.equals("null")) {
                            div_name = "";
                        }
                        if (div_id.equals("null")) {
                            div_id = "";
                        }

                        div_name = transformText(div_name);

                        System.out.println(div_id);
                        System.out.println(div_name);
                        picUserDetails.get(0).setDiv_id(div_id);
                        picUserDetails.get(0).setDiv_name(div_name);
                    }
                }
                else {
                    picUserDetails.get(0).setDiv_id("");
                    picUserDetails.get(0).setDiv_name("");
                }
                requestQueue.add(loginLogReq);

            } catch (JSONException e) {
                e.printStackTrace();
                conn = false;
                updateInterface();
            }
        }, error -> {
            conn = false;
            updateInterface();
        });

        requestQueue.add(getDivPICRequest);
    }

    public void updateInterface() {

        if (conn) {
            if (connected) {
                if (infoConnected) {

                    if (checkBox.isChecked()) {
                        System.out.println("Remembered");
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.remove(user_emp_code);
                        editor.remove(user_password);
                        editor.remove(checked);
                        editor.putString(user_emp_code,userName);
                        editor.putString(user_password,password);
                        editor.putBoolean(checked,true);
                        editor.apply();
                        editor.commit();
                    } else {
                        System.out.println("Not Remembered");
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.remove(user_emp_code);
                        editor.remove(user_password);
                        editor.remove(checked);

                        editor.apply();
                        editor.commit();
                    }


                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(PICLogin.this, HomePage.class);
                            intent.putExtra("USER","PIC_USER");
                            waitProgress.dismiss();
                            startActivity(intent);
                            finish();
                        }
                    }, 2000);

                }
                else {
                    waitProgress.dismiss();
                    Toast.makeText(getApplicationContext(), "Failed to Get User", Toast.LENGTH_SHORT).show();
                    AlertDialog dialog = new AlertDialog.Builder(PICLogin.this)
                            .setMessage("System Failed to Get User, Please Try Again.")
                            .setPositiveButton("Retry", null)
                            .show();

//                dialog.setCancelable(false);
//                dialog.setCanceledOnTouchOutside(false);
                    Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    positive.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getUser();
                            dialog.dismiss();
                        }
                    });
                }

            }
            else {
                waitProgress.dismiss();
                login_failed.setVisibility(View.VISIBLE);
            }
            conn = false;
            connected = false;
            infoConnected = false;

        }
        else {
            waitProgress.dismiss();
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
            AlertDialog dialog = new AlertDialog.Builder(PICLogin.this)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .show();

//                dialog.setCancelable(false);
//                dialog.setCanceledOnTouchOutside(false);
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    getUser();
                    dialog.dismiss();
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