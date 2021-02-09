package com.example.ygoquizgame;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class DuelistProfile extends AppCompatActivity {
    TextView backLink, profileNumOfAnswers, profileNumOfCorrectAnswers, profileCorrectnessRate, deleteAccount;
    ImageView profilePic, editProfilePic, deleteProfilePic, editName, editPassword, editBio;
    EditText profileName, profileEmail, profilePassword, profileBio;
    Button reset;
    DatabaseReference db;
    StorageReference storageRef;
    FirebaseUser user;
    ProgressDialog loadingDialog;
    public static final int RC_PHOTO_PICKER=1; //request code for calling onActivityResult (for profile pic uploading)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.duelist_profile);
        backLink=findViewById(R.id.back2);
        profileNumOfAnswers=findViewById(R.id.profileNumOfAnswers);
        profileNumOfCorrectAnswers=findViewById(R.id.profileNumOfCorrectAnswers);
        profileCorrectnessRate=findViewById(R.id.profileCorrectnessRate);
        deleteAccount=findViewById(R.id.deleteAccount);
        profilePic=findViewById(R.id.profilePic);
        editProfilePic=findViewById(R.id.editProfilePic);
        deleteProfilePic=findViewById(R.id.deleteProfilePic);
        editName=findViewById(R.id.editName);
        editPassword=findViewById(R.id.editPassword);
        editBio=findViewById(R.id.editBio);
        profileName=findViewById(R.id.profileName);
        profileName.setEnabled(false);
        profileEmail=findViewById(R.id.profileEmail);
        profileEmail.setEnabled(false);
        profilePassword=findViewById(R.id.profilePassword);
        profilePassword.setEnabled(false);
        profileBio=findViewById(R.id.profileBio);
        profileBio.setFocusableInTouchMode(false);
        profileBio.clearFocus();
        reset=findViewById(R.id.reset);
        db= FirebaseDatabase.getInstance().getReference("users");
        user= FirebaseAuth.getInstance().getCurrentUser();
        final String userID=user.getUid();
        loadingDialog=new ProgressDialog(DuelistProfile.this);
        loadingDialog.setMessage("Fetching profile data.. Please wait");
        loadingDialog.show();
        loadingDialog.setCancelable(false);
        storageRef = FirebaseStorage.getInstance().getReference().child("users/"+user.getUid());

        ValueEventListener userListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    DataSnapshot singleDataSnapshot=snapshot.child(user.getUid());
                    User userSnapshot = singleDataSnapshot.getValue(User.class);
                    profileNumOfAnswers.setText(""+userSnapshot.getNumOfAnswers());
                    profileNumOfCorrectAnswers.setText(""+userSnapshot.getNumOfCorrectAnswers());
                    profileCorrectnessRate.setText(userSnapshot.getCorrectnessRate() + "%");
                    db.child(user.getUid()).child("correctnessRate").setValue(userSnapshot.getCorrectnessRate()); //write correctnessRate in database

                    if (!userSnapshot.getProfilePic().equals("")) {  //if user has a previously stored profile pic
                        storageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if(task.isSuccessful())
                                    Glide.with(getApplicationContext()).load(task.getResult()).centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).into(profilePic); //load profile pic from firebase storage
                                else
                                    Toast.makeText(DuelistProfile.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    if (userSnapshot.getNumOfAnswers()==0) //reset answers data only when number of answers > 0
                        reset.setVisibility(View.GONE);

                    if (userSnapshot.getProfilePic().equals("")) //delete profile pic only when it's previously set
                        deleteProfilePic.setVisibility(View.GONE);

                    profileName.setText(userSnapshot.getDuelistName());
                    profileEmail.setText(userSnapshot.getEmail());
                    profileBio.setText(userSnapshot.getBio());
                }
                catch (Exception e){ Toast.makeText(DuelistProfile.this, "Failed to load profile: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                loadingDialog.dismiss();
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

        editName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileName.setEnabled(true);
                //disable the following buttons/links
                backLink.setEnabled(false);
                editProfilePic.setEnabled(false);
                deleteProfilePic.setEnabled(false);
                editPassword.setEnabled(false);
                editBio.setEnabled(false);
                reset.setEnabled(false);
                deleteAccount.setEnabled(false);

                editName.setImageResource(R.drawable.save);
                editName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //When user hits the check mark icon near the name field
                        if(profileName.getText().toString().isEmpty())
                            profileName.setError("Please enter your name");
                        else{
                            db.child(user.getUid()).child("duelistName").setValue(profileName.getText().toString());
                            DuelistProfile.this.recreate();
                        }
                    }
                });
            }
        });
        editPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profilePassword.setEnabled(true);
                //disable the following buttons/links
                backLink.setEnabled(false);
                editProfilePic.setEnabled(false);
                deleteProfilePic.setEnabled(false);
                editName.setEnabled(false);
                editBio.setEnabled(false);
                reset.setEnabled(false);
                deleteAccount.setEnabled(false);

                editPassword.setImageResource(R.drawable.save);
                editPassword.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //When user hits the check mark icon near the password field
                        if(profilePassword.getText().toString().length()<6)
                            profilePassword.setError("Please enter a password that is longer than 6 characters");
                        else{
                            user.updatePassword(profilePassword.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    db.child(user.getUid()).child("password").setValue(profilePassword.getText().toString());
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(DuelistProfile.this, "Could not update password! "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                            DuelistProfile.this.recreate();
                        }
                    }
                });
            }
        });
        editBio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileBio.setFocusableInTouchMode(true);
                //disable the following buttons/links
                backLink.setEnabled(false);
                editProfilePic.setEnabled(false);
                deleteProfilePic.setEnabled(false);
                editPassword.setEnabled(false);
                editName.setEnabled(false);
                reset.setEnabled(false);
                deleteAccount.setEnabled(false);

                editBio.setImageResource(R.drawable.save);
                editBio.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //When user hits the check mark icon near the bio field
                         db.child(user.getUid()).child("bio").setValue(profileBio.getText().toString());
                         DuelistProfile.this.recreate();
                    }
                });
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    new AlertDialog.Builder(DuelistProfile.this).setTitle(R.string.resetDataDialogTitle).setMessage(R.string.resetDataDialogMsg).setPositiveButton(R.string.dialogYes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //continue with reset operation
                            db.child(user.getUid()).child("numOfAnswers").setValue(0);
                            db.child(user.getUid()).child("numOfCorrectAnswers").setValue(0);
                            db.child(user.getUid()).child("correctnessRate").setValue(0);
                            DuelistProfile.this.recreate();
                        }
                    }).setNegativeButton(R.string.dialogCancel, null).show();
                }
        });

        deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(DuelistProfile.this).setTitle(R.string.deleteAccountDialogTitle).setMessage(R.string.deleteAccountDialogMsg).setPositiveButton(R.string.dialogYes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //continue with delete operation
                        user.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                if (!db.child(userID).child("profilePic").equals(""))  //if user has a previously stored profile pic
                                    storageRef.delete(); //delete profile pic stored in firebase storage
                                db.child(userID).removeValue();
                                Toast.makeText(DuelistProfile.this, "Account deleted successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(DuelistProfile.this, MainActivity.class));
                                DuelistProfile.super.finishAffinity();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(DuelistProfile.this, "Account could not be deleted! "+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).setNegativeButton(R.string.dialogCancel, null).show();
            }
        });

        editProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent uploadIntent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                setResult(Activity.RESULT_OK, uploadIntent);

                startActivityForResult(uploadIntent, RC_PHOTO_PICKER);
            }
        });
        deleteProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    new AlertDialog.Builder(DuelistProfile.this).setTitle(R.string.RemoveProfilePicDialogTitle).setMessage(R.string.RemoveProfilePicDialogMsg).setPositiveButton(R.string.dialogYes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //continue with remove profile pic operation
                            db.child(user.getUid()).child("profilePic").setValue("");
                            storageRef.delete();
                            DuelistProfile.this.recreate();
                        }
                    }).setNegativeButton(R.string.dialogCancel, null).show();
                }
        });

    } //end of onCreate

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==RC_PHOTO_PICKER && resultCode==RESULT_OK){
            Uri selectedImageUri=data.getData();
            loadingDialog=ProgressDialog.show(DuelistProfile.this, "Please wait", "processing image", true, false);
            storageRef.putFile(selectedImageUri).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> urlTask=taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            db.child(user.getUid()).child("profilePic").setValue(uri.toString());
                            loadingDialog.dismiss();
                            DuelistProfile.this.recreate();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            loadingDialog.dismiss();
                            Toast.makeText(DuelistProfile.this, "Could not upload selected image! "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }

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
