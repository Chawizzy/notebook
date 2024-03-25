package com.example.notebookandroidproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

public class CreateAccountActivity extends AppCompatActivity {
    /*
    Variables
    */
    private static final String TAG = "EmailPassword";
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private TextInputEditText fullNameEditText, gmailAddressEditText, passwordEditText;
    private TextView loginActivityTextView;
    private ProgressDialog progressDialog;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        /*
        Initializing the variables
        */
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        fullNameEditText = findViewById(R.id.editTextFullName);
        fullNameEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        gmailAddressEditText = findViewById(R.id.editTextEmailAddress);
        passwordEditText = findViewById(R.id.editTextPassword);
        loginActivityTextView = findViewById(R.id.textViewLoginActivity);
        registerButton = findViewById(R.id.buttonRegister);
    }

    @Override
    protected void onStart() {
        super.onStart();

        /*
        Check if user is signed in and react accordingly.
        */
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            openMainActivity();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        registerButton.setOnClickListener(v -> {
            String fullName = Objects.requireNonNull(fullNameEditText.getText()).toString();
            String gmailAddress = Objects.requireNonNull(gmailAddressEditText.getText()).toString();
            String password = Objects.requireNonNull(passwordEditText.getText()).toString();

            if(fullName.isEmpty() || gmailAddress.isEmpty() || password.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please Enter All Details", Toast.LENGTH_SHORT).show();
            }else if(password.length() < 6) {
                Toast.makeText(getApplicationContext(), "Password Too Short", Toast.LENGTH_SHORT).show();
            }else {
                registerButton.setClickable(false);

                showProgressDialog();

                createAccount(fullName, gmailAddress, password);
            }
        });

        loginActivityTextView.setOnClickListener(v -> openLoginActivity());
    }

    private void createAccount(String fullName, String gmailAddress, String password) {
        mAuth.createUserWithEmailAndPassword(gmailAddress, password).addOnSuccessListener(authResult -> {
            HashMap<String,Object> hashMap = new HashMap<>();
            hashMap.put("userID", Objects.requireNonNull(mAuth.getCurrentUser()).getUid());
            hashMap.put("fullName", fullName);
            hashMap.put("gmailAddress", gmailAddress);
            hashMap.put("password", password);

            mDatabase.child("User").child(mAuth.getCurrentUser().getUid()).setValue(hashMap).addOnCompleteListener(task -> {
                progressDialog.dismiss();

                openMainActivity();

                Log.d(TAG, "createUserWithEmail:success");
            });
        }).addOnFailureListener(e -> {
            progressDialog.dismiss();

            Toast.makeText(getApplicationContext(), "Registration Failed", Toast.LENGTH_SHORT).show();

            Log.w(TAG, "createUserWithEmail:failure", e);

            registerButton.setClickable(true);
        });
    }

    public void showProgressDialog() {
        progressDialog = new ProgressDialog(CreateAccountActivity.this);
        progressDialog.show();

        progressDialog.setContentView(R.layout.custom_progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    private void openLoginActivity() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void openMainActivity() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}