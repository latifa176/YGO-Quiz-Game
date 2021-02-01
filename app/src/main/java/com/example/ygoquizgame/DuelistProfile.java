package com.example.ygoquizgame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.storage.StorageReference;

public class DuelistProfile extends AppCompatActivity {
    TextView backLink, profileNumOfAnswers, profileNumOfCorrectAnswers, profileCorrectnessRate, reset, deleteAccount;
    ImageView profilePic, editProfilePic, deleteProfilePic;
    EditText profileName, profileEmail, profilePassword, profileBio;
    DatabaseReference db;
    StorageReference storageRef;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.duelist_profile);
        backLink=findViewById(R.id.back2);
        profileNumOfAnswers=findViewById(R.id.profileNumOfAnswers);
        profileNumOfCorrectAnswers=findViewById(R.id.profileNumOfCorrectAnswers);
        profileCorrectnessRate=findViewById(R.id.profileCorrectnessRate);
        reset=findViewById(R.id.reset);
        deleteAccount=findViewById(R.id.deleteAccount);
        profilePic=findViewById(R.id.profilePic);
        editProfilePic=findViewById(R.id.editProfilePic);
        deleteProfilePic=findViewById(R.id.deleteProfilePic);
        profileName=findViewById(R.id.profileName);
        profileEmail=findViewById(R.id.profileEmail);
        profilePassword=findViewById(R.id.profilePassword);
        profileBio=findViewById(R.id.profileBio);
        db= FirebaseDatabase.getInstance().getReference("users");
        user= FirebaseAuth.getInstance().getCurrentUser();

        ValueEventListener userListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    for (DataSnapshot singleDataSnapshot : snapshot.getChildren()) {
                        if(singleDataSnapshot.getKey().equalsIgnoreCase(user.getUid())) {
                            User userSnapshot = singleDataSnapshot.getValue(User.class);
                            profileNumOfAnswers.setText(""+userSnapshot.getNumOfAnswers());
                            profileNumOfCorrectAnswers.setText(""+userSnapshot.getNumOfCorrectAnswers());
                            profileCorrectnessRate.setText(userSnapshot.getCorrectnessRate() + "%");
                            if (!userSnapshot.getProfilePic().equals("")) {
                                storageRef = FirebaseStorage.getInstance().getReference().child("users/"+user.getUid());/////////////??????????????
                                Glide.with(DuelistProfile.this).load(storageRef).into(profilePic); //load profile pic from firebase storage
                            }
                            profileName.setText(userSnapshot.getDuelistName());
                            profileEmail.setText(userSnapshot.getEmail());
                            profileBio.setText(userSnapshot.getBio());
                        }
                    }
                }
                catch (Exception e){ Toast.makeText(DuelistProfile.this, "Failed to load profile: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DuelistProfile.this, "Failed to load profile: "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };
        db.addValueEventListener(userListener);

        backLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //back to main menu
                startActivity(new Intent(DuelistProfile.this, MainMenu.class).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP));
                finish();
            }
        });

        /*reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //////////////////////////
            }
        });*/
        /*deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //////////////////////////
            }
        });*/

        /*editProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ////////////////////////
            }
        });*/
        /*deleteProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ////////////////////////
            }
        });*/

    } //end of onCreate

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onBackPressed() {
        //back to main menu
        startActivity(new Intent(DuelistProfile.this, MainMenu.class).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP));
        finish();    }
}
