package com.example.notebookandroidproject;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.notebookandroidproject.models.Item;
import com.example.notebookandroidproject.models.ItemLocation;
import com.github.drjacky.imagepicker.ImagePicker;
import com.github.drjacky.imagepicker.constant.ImageProvider;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

public class CreateItemActivity extends AppCompatActivity {
    /*
    Variables
    */
    private TextInputEditText nameTextInputEditText, descriptionTextInputEditText, priceTextInputEditText;
    private TextInputLayout nameTextInputLayout;
    private ImageView itemImageView;
    private Button doneButton;
    private Uri imageURI;
    private ProgressDialog progressDialog;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FusedLocationProviderClient fusedLocationClient;
    private final static int REQUEST_CODE = 100;
    private String locationID;
    private final static String KEY_ITEM_ID = "itemID";
    private final static String KEY_USER_ID = "userID";
    private final static String KEY_LOCATION_ID = "locationID";
    private final static String KEY_NAME = "name";
    private final static String KEY_DESCRIPTION = "description";
    private final static String KEY_PRICE = "price";
    private final static String KEY_IMAGE_URI = "imageURL";
    private final static String KEY_PURCHASED = "isPurchased";
    private final static String KEY_LATITUDE = "latitude";
    private final static String KEY_LONGITUDE = "longitude";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_item);

        /*
        Initializing the variables
        */
        Toolbar myToolbar = findViewById(R.id.toolbarCustom);
        nameTextInputEditText = findViewById(R.id.editTextName);
        descriptionTextInputEditText = findViewById(R.id.editTextDescription);
        priceTextInputEditText = findViewById(R.id.editTextPrice);
        nameTextInputLayout = findViewById(R.id.textInputLayoutName);
        itemImageView = findViewById(R.id.imageOutput);
        doneButton = findViewById(R.id.buttonDone);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        setSupportActionBar(myToolbar);
        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).setHomeAsUpIndicator(R.drawable.back_arrow_icon);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        nameTextInputLayout.setEndIconOnClickListener(v -> getImage());

        doneButton.setOnClickListener(view -> {
            if(!Objects.requireNonNull(nameTextInputEditText.getText()).toString().isEmpty() ||
                    !Objects.requireNonNull(descriptionTextInputEditText.getText()).toString().isEmpty() ||
                    !Objects.requireNonNull(priceTextInputEditText.getText()).toString().isEmpty()) {

                doneButton.setClickable(false);

                showProgressDialog();

                uploadItem();
            }else {
                Toast.makeText(getApplicationContext(), "Enter All Fields", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getImage() {
        ImagePicker
                .Companion
                .with(CreateItemActivity.this)
                .crop(10f,10f)
                .maxResultSize(800,800,true)
                .provider(ImageProvider.BOTH).createIntentFromDialog(new Function1() {
                    @Override
                    public Object invoke(Object var1) {
                        this.invoke((Intent)var1);
                        return Unit.INSTANCE;
                    }

                    public void invoke(@NotNull Intent it){
                        Intrinsics.checkNotNullParameter(it,"it");
                        launcher.launch(it);
                    }
                });
    }

    ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),(ActivityResult result)->{
        if(result.getResultCode()==RESULT_OK) {
            imageURI = result.getData().getData();

            Picasso.get().load(imageURI).into(itemImageView);
        }
    });

    private String getFileExtension() {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(this.getContentResolver().getType(imageURI));
    }

    private void uploadItem() {
        getItemLocation();

        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if(imageURI != null) {
                StorageReference fileReference = FirebaseStorage.getInstance().getReference("Item")
                        .child(System.currentTimeMillis() + "." + getFileExtension());

                fileReference.putFile(imageURI).addOnSuccessListener(taskSnapshot -> fileReference.getDownloadUrl()
                        .addOnCompleteListener(task -> {
                            String itemID = mDatabase.push().getKey();
                            String userID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
                            String name = Objects.requireNonNull(nameTextInputEditText.getText()).toString();
                            String description = Objects.requireNonNull(descriptionTextInputEditText
                                    .getText()).toString();
                            double price = Double.parseDouble(Objects.requireNonNull(priceTextInputEditText
                                    .getText()).toString());
                            String downloadURI = task.getResult().toString();

                            Item item = new Item(itemID, userID, locationID, name, description, price, downloadURI, false);
                            addItemDatabase(item);
                        })).addOnFailureListener(e -> {
                    progressDialog.dismiss();

                    doneButton.setClickable(true);

                    Toast.makeText(getApplicationContext(), "Failed To Add Item", Toast.LENGTH_SHORT).show();
                });

            }else {
                progressDialog.dismiss();

                doneButton.setClickable(true);

                Toast.makeText(getApplicationContext(), "Add Image Of Item", Toast.LENGTH_SHORT).show();
            }
        }else {
            ActivityCompat.requestPermissions(CreateItemActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
        }
    }

    private void getItemLocation() {
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
                if (location != null){
                    try{
                        Geocoder geocoder = new Geocoder(CreateItemActivity.this, Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        locationID = mDatabase.push().getKey();
                        double latitude = addresses.get(0).getLatitude();
                        double longitude = addresses.get(0).getLongitude();

                        ItemLocation itemLocation = new ItemLocation(locationID, latitude, longitude);
                        addItemLocationDatabase(itemLocation);
                    }catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }else {
            ActivityCompat.requestPermissions(CreateItemActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @org.jetbrains.annotations.NotNull String[] permissions, @NonNull @org.jetbrains.annotations.NotNull int[] grantResults) {
        if (requestCode == REQUEST_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                uploadItem();
            }else {
                Toast.makeText(CreateItemActivity.this,"Please provide the required permission",Toast.LENGTH_SHORT).show();
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void addItemLocationDatabase(ItemLocation itemLocation) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(KEY_LOCATION_ID , itemLocation.getLocationID());
        hashMap.put(KEY_LATITUDE, itemLocation.getLatitude());
        hashMap.put(KEY_LONGITUDE, itemLocation.getLongitude());

        mDatabase.child("ItemLocation").child(itemLocation.getLocationID()).setValue(hashMap);
    }

    private void addItemDatabase(Item item) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(KEY_ITEM_ID , item.getItemID());
        hashMap.put(KEY_USER_ID, item.getUserID());
        hashMap.put(KEY_LOCATION_ID, item.getLocationID());
        hashMap.put(KEY_NAME, item.getName());
        hashMap.put(KEY_DESCRIPTION, item.getDescription());
        hashMap.put(KEY_PRICE, item.getPrice());
        hashMap.put(KEY_IMAGE_URI, item.getImageURL());
        hashMap.put(KEY_PURCHASED, item.isPurchased());

        mDatabase.child("Item").child(item.getItemID()).setValue(hashMap);
        progressDialog.dismiss();
        Toast.makeText(getApplicationContext(), "Item Added Successfully", Toast.LENGTH_SHORT).show();
        openMainActivity();
    }

    public void showProgressDialog() {
        progressDialog = new ProgressDialog(CreateItemActivity.this);
        progressDialog.show();

        progressDialog.setContentView(R.layout.custom_progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    private void openMainActivity() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}