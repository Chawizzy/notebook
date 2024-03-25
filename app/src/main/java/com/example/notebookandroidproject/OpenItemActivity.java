package com.example.notebookandroidproject;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notebookandroidproject.models.Item;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class OpenItemActivity extends AppCompatActivity {
    private TextView nameTextView, descriptionTextView, priceTextView;
    private ImageView imageView;
    private Item item;

    private MaterialButton openMapButton;
    private MaterialButton deleteButton;

    private DatabaseReference mDatabase;
    private FirebaseStorage firebaseStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_item);

        Toolbar myToolbar = findViewById(R.id.toolbarCustom);
        imageView = findViewById(R.id.imageViewOpenActivity);
        nameTextView = findViewById(R.id.textViewOpenActivityName);
        descriptionTextView = findViewById(R.id.textViewOpenActivityDescription);
        priceTextView = findViewById(R.id.textViewOpenActivityPrice);

        openMapButton = findViewById(R.id.openMap);
        deleteButton = findViewById(R.id.deleteItem);

        setSupportActionBar(myToolbar);
        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).setHomeAsUpIndicator(R.drawable.back_arrow_icon);
        actionBar.setDisplayHomeAsUpEnabled(true);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseStorage = FirebaseStorage.getInstance();

        item = getIntent().getParcelableExtra("item");
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        showItemDetails();

        openMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(OpenItemActivity.this, "Button Clicked", Toast.LENGTH_LONG).show();

//                Intent intent = new Intent(getApplicationContext(), OpenItemMapActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                intent.putExtra("locationID", item.getLocationID());
//                startActivity(intent);
//                finish();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItem();
            }
        });
    }

    private void showItemDetails() {
        Picasso.get().load(item.getImageURL()).fit().placeholder(R.drawable.image_icon).fit()
                .centerCrop().into(imageView);
        nameTextView.setText(item.getName());
        descriptionTextView.setText(item.getDescription());
        priceTextView.setText("P" + item.getPrice());
    }

    private void deleteItem() {
        StorageReference storageReference= firebaseStorage.getReferenceFromUrl(item.getImageURL());

        storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                mDatabase.child("Item").child(item.getItemID()).removeValue();

                openMainActivity();
            }
        });
    }

    private void openMainActivity() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}