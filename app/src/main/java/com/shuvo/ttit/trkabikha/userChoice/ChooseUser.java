package com.shuvo.ttit.trkabikha.userChoice;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.activity.OnBackPressedCallback;
import static com.shuvo.ttit.trkabikha.Constants.api_pre_url;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.shuvo.ttit.trkabikha.R;
import com.shuvo.ttit.trkabikha.login.Login;
import com.shuvo.ttit.trkabikha.login.PICLogin;
import com.shuvo.ttit.trkabikha.mainmenu.HomePage;
import com.shuvo.ttit.trkabikha.progressbar.WaitProgress;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class ChooseUser extends AppCompatActivity {

    LinearLayout guest;
    LinearLayout admin;
    LinearLayout picUser;

    WaitProgress waitProgress = new WaitProgress();
    ArrayList<String> urls;
    String text_url = "https://raw.githubusercontent.com/shuvo934/Story/refs/heads/master/TRKABIKAHServer";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_user);

        guest = findViewById(R.id.guest_button);
        admin = findViewById(R.id.admin_button);
        picUser = findViewById(R.id.pic_user_button);

        guest.setOnClickListener(view -> {
            Intent intent = new Intent(ChooseUser.this, HomePage.class);
            intent.putExtra("USER","GUEST");
            startActivity(intent);
        });

        admin.setOnClickListener(view -> {
            Intent intent = new Intent(ChooseUser.this, Login.class);
            startActivity(intent);
        });

        picUser.setOnClickListener(view -> {
            Intent intent = new Intent(ChooseUser.this, PICLogin.class);
            startActivity(intent);
        });
        readApiText();

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                AlertDialog.Builder builder = new AlertDialog.Builder(ChooseUser.this);
                builder.setTitle("EXIT!")
                        .setMessage("Do you want to Exit?")
                        .setPositiveButton("YES", (dialog, which) -> finish())
                        .setNegativeButton("NO", (dialog, which) -> {

                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    public void readApiText() {
        waitProgress.show(getSupportFragmentManager(), "WaitBar");
        waitProgress.setCancelable(false);
        new Thread(() -> {
            urls = new ArrayList<>();
            try {
                URL url = new URL(text_url);
                HttpURLConnection conn=(HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(60000); // timing out in a minute
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String str;
                while ((str = in.readLine()) != null) {
                    urls.add(str);
                }
                in.close();
            }
            catch (Exception e) {
                urls.add("https://bpa-bd.org/terrain/tr_kabikha/");
            }

            runOnUiThread(() -> {
                if (urls.isEmpty()) {
                    api_pre_url = "https://bpa-bd.org/terrain/tr_kabikha/";
                }
                else {
                    for (int i = 0; i < urls.size(); i++) {
                        api_pre_url= urls.get(i);
                    }
                }
                waitProgress.dismiss();
            });

        }).start();
    }
}