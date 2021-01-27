package com.example.ygoquizgame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.*;
import com.google.firebase.storage.*;

public class SignUp extends AppCompatActivity {
    EditText duelistName, email, password, password2;
    TextView back;
    Button signUpButton;
    FirebaseAuth mAuth;
    StorageReference usersDatabase;
    String nameInput, emailInput, passwordInput, passwordInput2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        duelistName=findViewById(R.id.duelistName);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        password2=findViewById(R.id.password2);
        back=findViewById(R.id.back);
        signUpButton=findViewById(R.id.signUpButton);
        mAuth=FirebaseAuth.getInstance();
        usersDatabase=FirebaseStorage.getInstance().getReference().child("Users");

        //sign up button event listener
        signUpButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                nameInput = duelistName.getText().toString().trim();
                                                emailInput = email.getText().toString().trim();
                                                passwordInput = password.getText().toString();
                                                passwordInput2 = password2.getText().toString();

                                                if (nameInput.isEmpty()) {
                                                    duelistName.setError("Please enter your name");
                                                    duelistName.requestFocus();
                                                    return;
                                                }
                                                if (emailInput.isEmpty()) {
                                                    email.setError("Please enter your email");
                                                    email.requestFocus();
                                                    return;
                                                }
                                                if (!isValidEmail(emailInput)) {
                                                    email.setError("Please enter a valid email");
                                                    email.requestFocus();
                                                    return;
                                                }
                                                if (passwordInput.isEmpty() || passwordInput.length() < 6) {
                                                    password.setError("Please enter a password that is longer than 6 characters");
                                                    password.requestFocus();
                                                    return;
                                                }
                                                if (!passwordInput2.equals(passwordInput)) {
                                                    password2.setError("Please enter an identical password");
                                                    password2.requestFocus();
                                                    return;
                                                }
                                                Toast.makeText(SignUp.this, "Creating account...", Toast.LENGTH_SHORT).show();
                                                mAuth.createUserWithEmailAndPassword(emailInput, passwordInput).addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                                        if (!task.isSuccessful()) {
                                                            Toast.makeText(SignUp.this, "Authentication failed" + task.getException(), Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            startActivity(new Intent(SignUp.this, MainMenu.class));
                                                            finish();
                                                        }
                                                    }
                                                });
                                            }
                                        });

        //"go back" link event listener
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //back to sign in
                startActivity(new Intent(SignUp.this,MainActivity.class));
            }
        });

    }//end of OnCreate

    public boolean isValidEmail(String email) {
        String regex="^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
        }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(SignUp.this,MainActivity.class));
    }
}
