package com.example.ygoquizgame;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;

public class MainMenu extends AppCompatActivity {
    Toolbar myToolbar;
    Button startQuizButton;
    TextView quizSettingsLink, searchUsersLink;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);
        myToolbar=findViewById(R.id.myToolbar);
        startQuizButton=findViewById(R.id.startQuizButton);
        quizSettingsLink=findViewById(R.id.quizSettingsLink);
        searchUsersLink=findViewById(R.id.searchUsersLink);
        mAuth=FirebaseAuth.getInstance();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_items, menu);
        myToolbar.setBackgroundColor(Color.WHITE);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //check which item (among the rest in the toolbar) has been selected
        switch(item.getItemId()){
            case R.id.signOutItem: //Sign out user and go to the sign in activity
                mAuth.signOut();
                startActivity(new Intent(MainMenu.this, MainActivity.class));
                return true;
            case R.id.friendsListItem: //Go to friends list
                //////////
                return true;
            case R.id.duelistProfileItem: //Go to the duelist profile
                //////////
                return true;
            case R.id.bgm: //Go to the BGM selection activity
                //////////
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        //Do nothing when back is pressed from the main menu
    }
}
