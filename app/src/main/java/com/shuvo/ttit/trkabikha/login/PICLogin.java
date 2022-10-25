package com.shuvo.ttit.trkabikha.login;

import static com.shuvo.ttit.trkabikha.connection.OracleConnection.createConnection;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
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

import com.google.android.material.textfield.TextInputEditText;
import com.shuvo.ttit.trkabikha.R;
import com.shuvo.ttit.trkabikha.arraylist.PICUserDetails;
import com.shuvo.ttit.trkabikha.arraylist.UserDetails;
import com.shuvo.ttit.trkabikha.mainmenu.HomePage;
import com.shuvo.ttit.trkabikha.progressbar.WaitProgress;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

                    new CheckLogin().execute();

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
        } catch (Exception ex) { } // for now eat exceptions
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

    public class CheckLogin extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            waitProgress.show(getSupportFragmentManager(), "WaitBar");
            waitProgress.setCancelable(false);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (isConnected() && isOnline()) {

                LoginQuery();
                if (connected) {
                    conn = true;
                }

            } else {
                conn = false;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


            if (conn) {

                if (!userId.equals("-1")) {
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


                    } else {
                        waitProgress.dismiss();
                        new CheckLogin().execute();
                    }

                } else {
                    waitProgress.dismiss();
                    login_failed.setVisibility(View.VISIBLE);
                }
                conn = false;
                connected = false;
                infoConnected = false;

            } else {

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

                        new CheckLogin().execute();
                        dialog.dismiss();
                    }
                });
            }
        }
    }

    public void LoginQuery () {

        try {
            this.connection = createConnection();

            picUserDetails = new ArrayList<>();

            Statement stmt = connection.createStatement();

            ResultSet rs = stmt.executeQuery("select VALIDATE_USER_DB('" + userName + "',HAMID_ENCRYPT_DESCRIPTION_PACK.HEDP_ENCRYPT('" + password + "')) val from dual\n");

            while (rs.next()) {
                userId = rs.getString(1);
            }
            rs.close();

            if (!userId.equals("-1")) {

                ResultSet resultSet = stmt.executeQuery("Select USR_NAME, USR_FNAME, USR_LNAME, USR_EMAIL, USR_CONTACT, USR_EMP_ID, \n" +
                        "USR_DIST_ID,\n" +
                        "USR_DD_ID,USR_ACCESS_TYPE,\n" +
                        "(Select DIST_NAME FROM DISTRICT WHERE DIST_ID = USR_DIST_ID) DIST_NAME,\n" +
                        "(Select DD_THANA_NAME FROM DISTRICT_DTL WHERE DD_ID = USR_DD_ID) THANA_NAME\n" +
                        "FROM ISP_USER\n" +
                        "WHERE USR_ID = "+userId+"");

                while (resultSet.next()) {
                    picUserDetails.add(new PICUserDetails(userId,resultSet.getString(1), resultSet.getString(2), resultSet.getString(3),
                            resultSet.getString(4),resultSet.getString(5),resultSet.getString(6),resultSet.getString(7),
                            resultSet.getString(8), resultSet.getString(9),null,null,
                            resultSet.getString(10),resultSet.getString(11)));
                }
                resultSet.close();

                ResultSet resultSet1 = stmt.executeQuery("Select div_name, div_id \n" +
                            "from DIVISION,DISTRICT\n" +
                            "where DIVISION.div_id = DISTRICT.DIST_DIV_ID \n" +
                            "and DISTRICT.DIST_ID  = "+picUserDetails.get(0).getDist_id()+"\n" +
                            "group by div_name, div_id ");
                String div_id = "";
                String div_name = "";
                while (resultSet1.next()) {
                    div_name = resultSet1.getString(1);
                    div_id = resultSet1.getString(2);
                }
                resultSet1.close();

                picUserDetails.get(0).setDiv_id(div_id);
                picUserDetails.get(0).setDiv_name(div_name);

                ResultSet resultSet2 = stmt.executeQuery("SELECT SYS_CONTEXT ('USERENV', 'SESSIONID') --INTO P_IULL_SESSION_ID\n" +
                        "   FROM DUAL\n");

                while (resultSet2.next()) {
                    System.out.println("SESSION ID: "+ resultSet2.getString(1));
                    sessionId = resultSet2.getString(1);
                }

                resultSet2.close();

                String userName = picUserDetails.get(0).getUserName();

                CallableStatement callableStatement1 = connection.prepareCall("{call USERLOGINDTL(?,?,?,?,?,?,?,?,?)}");
                callableStatement1.setString(1,userName);
                callableStatement1.setString(2, brand+" "+model);
                callableStatement1.setString(3,ipAddress);
                callableStatement1.setString(4,hostUserName);
                callableStatement1.setInt(5,Integer.parseInt(userId));
                callableStatement1.setInt(6,Integer.parseInt(sessionId));
                callableStatement1.setString(7,"1");
                callableStatement1.setString(8,osName);
                callableStatement1.setInt(9,3);
                callableStatement1.execute();

                callableStatement1.close();

                infoConnected = true;

            }

            stmt.close();

            connected = true;

            connection.close();

        } catch (Exception e) {

            //   Toast.makeText(MainActivity.this, ""+e,Toast.LENGTH_LONG).show();
            Log.i("ERRRRR", e.getLocalizedMessage());
            e.printStackTrace();
        }

    }
}