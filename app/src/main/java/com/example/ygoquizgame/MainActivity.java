package com.example.ygoquizgame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.*;
public class MainActivity extends AppCompatActivity {
EditText email, password;
Button signInButton;
TextView signUpLink;
FirebaseAuth mAuth;
private FirebaseAuth.AuthStateListener authStateListener; //for checking changes in the authentication state

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth=FirebaseAuth.getInstance();
        email=findViewById(R.id.signInEmail);
        password=findViewById(R.id.signInPassword);
        signInButton=findViewById(R.id.signInButton);
        signUpLink=findViewById(R.id.signUpLink);

        authStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user=firebaseAuth.getCurrentUser();
                if(user!=null) {
                    //if user is signed in, go to the main menu
                    Intent I = new Intent(MainActivity.this, MainMenu.class);
                    startActivity(I);
                }
            }
        };

        //Sign in button event listener
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailInput=email.getText().toString();
                String passwordInput=password.getText().toString();

                if(emailInput.isEmpty()){
                    email.setError("Please enter your email");
                    email.requestFocus();
                    return;
                }
                if(passwordInput.isEmpty()){
                    password.setError("Please enter your password");
                    password.requestFocus();
                    return;
                }

                //When both fields are not empty:
                if(!(emailInput.isEmpty() && passwordInput.isEmpty())){
                    Toast.makeText(MainActivity.this, "Signing in...", Toast.LENGTH_SHORT).show();
                    mAuth.signInWithEmailAndPassword(emailInput, passwordInput).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //if there was an error
                            if(!task.isSuccessful()){
                                Toast.makeText(MainActivity.this, "Sign in failed! Please try again", Toast.LENGTH_LONG).show();
                            } else {
                                Intent intent=new Intent(MainActivity.this, MainMenu.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
                } else Toast.makeText(MainActivity.this, "Error! Please try again", Toast.LENGTH_SHORT).show();
            }
        });

        //sign up link event listener
        signUpLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if user wants to sign up new account, go to the sign up view
                Intent I = new Intent(MainActivity.this, SignUp.class);
                startActivity(I);
                finish();
            }
        });
    } //end of OnCreate

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authStateListener);
    }

    @Override
    public void onBackPressed() {
        //Do nothing when back is pressed from the sign in activity
    }
}
