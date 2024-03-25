package com.example.notebookandroidproject.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.notebookandroidproject.LoginActivity;
import com.example.notebookandroidproject.R;
import com.example.notebookandroidproject.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {
    // Variables
    private TextView rowUserIDTextView, rowNameTextView, rowEmailAddressTextView, rowPasswordTextView;
    private Button logoutButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Initializing the variables
        rowUserIDTextView = view.findViewById(R.id.textViewRowUserID);
        rowNameTextView = view.findViewById(R.id.textViewRowName);
        rowEmailAddressTextView = view.findViewById(R.id.textViewRowEmailAddress);
        rowPasswordTextView = view.findViewById(R.id.textViewRowPassword);
        logoutButton = view.findViewById(R.id.buttonLogout);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        loadProfile();

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();

                openLoginActivity();
            }
        });
    }

    public void loadProfile() {
        FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance()
                .getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);

                rowUserIDTextView.setText(user.getUserID());
                rowNameTextView.setText(user.getFullName());
                rowEmailAddressTextView.setText(user.getGmailAddress());
                rowPasswordTextView.setText(user.getPassword());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void openLoginActivity() {
        Intent intent = new Intent(getContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        getActivity().finish();
    }
}