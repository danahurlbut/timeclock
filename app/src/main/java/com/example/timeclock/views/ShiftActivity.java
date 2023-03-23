package com.example.timeclock.views;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.timeclock.R;
import com.example.timeclock.helpers.IdleEventHandler;
import com.example.timeclock.viewmodels.ShiftActivityViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

public class ShiftActivity extends AppCompatActivity {
    private ShiftActivityViewModel viewModel; //Future enhancement replace with dependency injection
    private final String[] REQUIRED_CAMERA_PERMISSIONS =
            new String[]{"android.permission.CAMERA",
                    "android.permission.WRITE_EXTERNAL_STORAGE"};

    private final String[] REQUIRED_LOCATION_PERMISSIONS =
            new String[]{"android.permission.ACCESS_COARSE_LOCATION"};
    private static final int CAMERA_REQUEST_CODE = 123;
    private static final int LOCATION_REQUEST_CODE = 456;
    private ActivityResultLauncher<Intent> cameraActivityResultLauncher;
    private Button proceedButton;
    private TextView instructionsText;
    private FusedLocationProviderClient fusedLocationClient;
    private IdleEventHandler idleEventHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shift);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        viewModel = new ViewModelProvider(this).get(ShiftActivityViewModel.class);

        idleEventHandler = new IdleEventHandler(this);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        cameraActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == CAMERA_REQUEST_CODE) {
                        // There are no request codes
                        Intent intent = result.getData();
                        if (intent != null) {
                            Bundle bundle = intent.getExtras();
                            Uri uri = bundle.getParcelable("uri");
                            //TODO ideally with a real API we would upload this pic
                            // somewhere and we would be setting the pic's URL
                            // for this variable below -- pretend that happened here
                            viewModel.setPicture(uri);
                            displayStep2();
                        }
                    }
                });

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            viewModel.setUser(bundle.getParcelable("user"));
            initializeView();
        } else { //should never happen
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        idleEventHandler.startHandler();
    }

    @Override
    protected void onPause() {
        super.onPause();
        idleEventHandler.stopHandler();
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        idleEventHandler.onUserInteractionOccurred();
    }

    private void initializeView() {
        TextView welcomeTextView = findViewById(R.id.welcome_text);
        TextView shiftTextView = findViewById(R.id.shift_text);
        instructionsText = findViewById(R.id.instructions_text);
        proceedButton = findViewById(R.id.start_shift_button);

        welcomeTextView.setText(getResources().getString(R.string.welcome_user,
                viewModel.getUser().getName()));
        if (viewModel.hasStartedShift())
            shiftTextView.setText(getResources().getString(R.string.end_shift_header));
        else
            shiftTextView.setText(getResources().getString(R.string.start_shift_header));

        //user has not started a shift
        // check what step should be displayed -- in case of configuration change recreating activity
        int step = viewModel.getStep();
        switch (step) {
            case 1:
                displayStep1();
                break;
            case 2:
                displayStep2();
                break;
            case 3:
                displayStep3();
                break;
        }
    }

    private void displayStep1() {
        //user has not taken their photo or saved location
        if (viewModel.hasStartedShift())
            instructionsText.setText(getResources().getString(R.string.shift_end_step1_instructions));
        else
            instructionsText.setText(getResources().getString(R.string.shift_start_step1_instructions));
        proceedButton.setOnClickListener(view -> {
            if (hasCameraPermissions())
                startCameraActivity();
            else
                requestCameraPermissions();
        });
    }

    private void displayStep2() {
        //user has taken their photo but still needs to get location
        displayImage();

        instructionsText.setText(getResources().getString(R.string.shift_start_end_step2_instructions));
        proceedButton.setText(getResources().getString(R.string.get_location_button));
        proceedButton.setOnClickListener(view -> {
            if (hasLocationPermissions())
                getLocation();
            else
                requestLocationPermissions();
        });
    }

    private void displayStep3() {
        //user has taken photo and given location.
        displayImage(); //in case of config change

        int step3instructions = R.string.shift_start_step3_instructions;
        int step3ButtonText = R.string.start_shift_button;
        if (viewModel.hasStartedShift()) {
            step3instructions = R.string.shift_end_step3_instructions;
            step3ButtonText = R.string.end_shift_button;
        }
        instructionsText.setText(getResources().getString(step3instructions,
                viewModel.getLocation().getLongitude(), viewModel.getLocation().getLatitude()));
        proceedButton.setText(getResources().getString(step3ButtonText));

        proceedButton.setOnClickListener(view -> {
            //TODO when real API is ready, uncomment the code below to start or end user shift
//            if (viewModel.hasStartedShift()) {
//                viewModel.endShift().observe(ShiftActivity.this, user -> {
//                    if (user != null) {
//                        endActivity();
//                    } else {
//                        Toast.makeText(ShiftActivity.this,
//                                getResources().getText(R.string.call_failed),
//                                Toast.LENGTH_SHORT).show();
//                    }
//                });
//            } else {
//                viewModel.startShift().observe(ShiftActivity.this, user -> {
//                    if (user != null) {
//                        endActivity();
//                    } else {
//                        Toast.makeText(ShiftActivity.this,
//                                getResources().getText(R.string.call_failed),
//                                Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
             endActivity(); //TODO delete this line when real API is ready and code above is uncommented
        });
    }

    private boolean hasCameraPermissions() {
        for (String permission : REQUIRED_CAMERA_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private boolean hasLocationPermissions() {
        return ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void requestCameraPermissions() {
        ActivityCompat.requestPermissions(this, REQUIRED_CAMERA_PERMISSIONS,
                CAMERA_REQUEST_CODE);
    }

    private void requestLocationPermissions() {
        ActivityCompat.requestPermissions(this, REQUIRED_LOCATION_PERMISSIONS,
                LOCATION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (hasCameraPermissions())
                startCameraActivity();
            else
                Toast.makeText(this,
                        getResources().getString(R.string.camera_permissions_message),
                        Toast.LENGTH_SHORT).show();
        } else if (requestCode == LOCATION_REQUEST_CODE) {
            if (hasLocationPermissions())
                getLocation();
            else
                Toast.makeText(this,
                        getResources().getString(R.string.location_permissions_message),
                        Toast.LENGTH_SHORT).show();
        }
    }

    private void startCameraActivity() {
        Intent intent = new Intent(this, CameraActivity.class);
        cameraActivityResultLauncher.launch(intent);
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        if (location != null) {
                            viewModel.setLocation(location);
                            displayStep3();
                        } else {
                            //Future enhancement: better logic to handle rare case that location comes back null
                            Toast.makeText(ShiftActivity.this,
                                    getResources().getString(R.string.location_access_failed),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            requestLocationPermissions();
        }
    }

    private void displayImage() {
        ImageView userPhoto = findViewById(R.id.user_photo);
        Glide.with(this).asBitmap().load(viewModel.getPicture()).centerCrop()
                .apply(new RequestOptions().fitCenter())
                .into(new BitmapImageViewTarget(userPhoto) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        userPhoto.setImageBitmap(resource);
                    }
                }); //future enhancement - load an error symbol if image load error occurs

        userPhoto.setVisibility(View.VISIBLE);
    }

    private void endActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        idleEventHandler.stopHandler();
        idleEventHandler.release();
    }
}