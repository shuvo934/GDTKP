package com.shuvo.ttit.trkabikha.userChoice;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.shuvo.ttit.trkabikha.R;
import com.shuvo.ttit.trkabikha.login.Login;
import com.shuvo.ttit.trkabikha.login.PICLogin;
import com.shuvo.ttit.trkabikha.mainmenu.HomePage;


public class ChooseUser extends AppCompatActivity {

    LinearLayout guest;
    LinearLayout admin;
    LinearLayout picUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_user);

        guest = findViewById(R.id.guest_button);
        admin = findViewById(R.id.admin_button);
        picUser = findViewById(R.id.pic_user_button);

        guest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChooseUser.this, HomePage.class);
                intent.putExtra("USER","GUEST");
                startActivity(intent);
            }
        });

        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChooseUser.this, Login.class);
                startActivity(intent);
            }
        });

        picUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChooseUser.this, PICLogin.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("EXIT!")
                .setMessage("Do you want to Exit?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        System.exit(0);
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
}