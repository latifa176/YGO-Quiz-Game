package com.example.ygoquizgame;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class MainMenu extends AppCompatActivity {
    ImageView bgmIcon, duelistProfileIcon, friendsListIcon, signOutIcon;
    Button startQuizButton;
    TextView quizSettingsLink, searchUsersLink;
    FirebaseAuth mAuth;
    boolean doubleBackToExitPressedOnce=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);
        bgmIcon=findViewById(R.id.bgmIcon);
        duelistProfileIcon=findViewById(R.id.duelistProfileIcon);
        friendsListIcon=findViewById(R.id.friendsListIcon);
        signOutIcon=findViewById(R.id.signOutIcon);
        startQuizButton=findViewById(R.id.startQuizButton);
        quizSettingsLink=findViewById(R.id.quizSettingsLink);
        searchUsersLink=findViewById(R.id.searchUsersLink);
        mAuth=FirebaseAuth.getInstance();

        signOutIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Toast.makeText(MainMenu.this, "Signed out successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainMenu.this, MainActivity.class));
            }
        });

        /*friendsListIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ////////////////////////////////////////
            }
        });*/

        duelistProfileIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainMenu.this, DuelistProfile.class));
                finish();
            }
        });

        /*bgmIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ////////////////////////////////
            }
        });*/

        /*startQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ////////////////////////////////
            }
        });*/

        /*quizSettingsLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ////////////////////////
            }
        });*/
        /*searchUsersLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ////////////////////////////
            }
        });*/

    }//end of onCreate

    @Override
    public void onBackPressed() {
        if(doubleBackToExitPressedOnce){
            super.onBackPressed();
            finish();
        }
        this.doubleBackToExitPressedOnce=true;
        Toast.makeText(MainMenu.this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000); //in milliseconds, the desired time between two BACK presses
    }
}
