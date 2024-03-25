package com.example.notebookandroidproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    /*
    Variables
    */
    private static final String TAG = "EmailPassword";
    private FirebaseAuth mAuth;
    private TextInputEditText gmailAddressEditText, passwordEditText;
    private Button loginButton;
    private ProgressDialog progressDialog;
    private TextView registerActivityTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /*
        Initializing the variables
        */
        mAuth = FirebaseAuth.getInstance();
        gmailAddressEditText = findViewById(R.id.editTextLoginEmailAddress);
        passwordEditText = findViewById(R.id.editTextLoginPassword);
        loginButton = findViewById(R.id.buttonLogin);
        registerActivityTextView = findViewById(R.id.textViewRegisterActivity);
    }

    @Override
    protected void onStart() {
        super.onStart();

        /*
        Checks if user is already signed in, and react accordingly.
        */
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            openMainActivity();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        /*
        Checks if both fields have user input and decide to display toast or proceed to signIn
        method.
        */
        loginButton.setOnClickListener(v -> {
            String gmailAddress = Objects.requireNonNull(gmailAddressEditText.getText()).toString();
            String password = Objects.requireNonNull(passwordEditText.getText()).toString();

            if(gmailAddress.isEmpty() || password.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Empty Credentials", Toast.LENGTH_SHORT).show();
            }else {
                loginButton.setClickable(false);

                showProgressDialog();

                signIn(gmailAddress, password);
            }
        });

        /*
        Opens the create account activity.
        */
        registerActivityTextView.setOnClickListener(v -> openCreateAccountActivity());
    }

    private void signIn(String gmailAddress, String password) {
        mAuth.signInWithEmailAndPassword(gmailAddress, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, opens main activity.
                        progressDialog.dismiss();

                        openMainActivity();

                        Log.d(TAG, "createUserWithEmail:success");
                    } else {
                        // If sign in fails, display a message to the user.
                        progressDialog.dismiss();

                        Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_SHORT).show();

                        Log.w(TAG, "signInWithEmail:failure", task.getException());

                        loginButton.setClickable(true);
                    }
                });
    }

    public void showProgressDialog() {
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.show();

        progressDialog.setContentView(R.layout.custom_progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    private void openCreateAccountActivity() {
        Intent intent = new Intent(getApplicationContext(), CreateAccountActivity.class);
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