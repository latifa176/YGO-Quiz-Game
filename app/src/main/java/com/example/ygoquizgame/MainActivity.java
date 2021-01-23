package com.example.ygoquizgame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.google.firebase.auth.*;
public class MainActivity extends AppCompatActivity {
EditText username, password;
Button signInButton;
TextView signUpLink;
FirebaseAuth mAuth;
private FirebaseAuth.AuthStateListener authStateListener; //for checking changes in the authentication state

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth=FirebaseAuth.getInstance();
        username=findViewById(R.id.signUpUsername);
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
                String usernameInput=username.getText().toString();
                final String passwordInput=password.getText().toString();

                if(usernameInput.isEmpty()){
                    username.setError("Please enter your username");
                    username.requestFocus();
                }
                if(passwordInput.isEmpty()){
                    password.setError("Please enter your password");
                    password.requestFocus();
                }

                //When both fields are not empty:
                if(!(usernameInput.isEmpty() && passwordInput.isEmpty())){
                ///////////////////////////////////////////////////////////////////////////////////////////
                }
            }
        });
    }
}
