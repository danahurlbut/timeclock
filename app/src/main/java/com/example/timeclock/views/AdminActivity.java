package com.example.timeclock.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timeclock.R;
import com.example.timeclock.adapters.UserListAdapter;
import com.example.timeclock.helpers.IdleEventHandler;
import com.example.timeclock.models.AdminUser;
import com.example.timeclock.viewmodels.AdminViewModel;

public class AdminActivity extends AppCompatActivity {
    private AdminViewModel viewModel;
    private RecyclerView userListRecyclerView;
    private TextView tapToReload;
    private ProgressBar progressBar;
    private UserListAdapter userListAdapter;
    private IdleEventHandler idleEventHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        viewModel = new ViewModelProvider(this).get(AdminViewModel.class);

        idleEventHandler = new IdleEventHandler(this);

        Bundle bundle = getIntent().getBundleExtra("bundle");
        if (bundle != null) {
            AdminUser adminUser = bundle.getParcelable("user");
            viewModel.setAdminUser(adminUser);
            initializeView();
        } else { //should not happen
            finish();
        }

        fetchUserData();
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
        welcomeTextView.setText(getResources().getString(R.string.welcome_user,
                viewModel.getAdminUser().getName()));

        userListRecyclerView = findViewById(R.id.user_list_view);
        tapToReload = findViewById(R.id.tap_to_reload);
        progressBar = findViewById(R.id.loading_spinner);

        userListRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        userListAdapter = new UserListAdapter(this);
        userListRecyclerView.setAdapter(userListAdapter);

        Button logoutButton = findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(view -> endActivity());
    }

    private void fetchUserData() {
        progressBar.setVisibility(View.VISIBLE);
        tapToReload.setVisibility(View.GONE);
        userListRecyclerView.setVisibility(View.VISIBLE);

        viewModel.getUserList().observe(this, userList -> {
            progressBar.setVisibility(View.GONE);
            if (userList != null) {
                userListAdapter.setResults(userList.getUsers());
            } else {
                userListRecyclerView.setVisibility(View.GONE);
                tapToReload.setVisibility(View.VISIBLE);
                tapToReload.setOnClickListener(v -> fetchUserData());
            }
        });
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
