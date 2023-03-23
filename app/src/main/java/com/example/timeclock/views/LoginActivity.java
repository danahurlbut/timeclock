package com.example.timeclock.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.timeclock.R;
import com.example.timeclock.models.User;
import com.example.timeclock.viewmodels.LoginViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.apache.commons.lang3.StringUtils;

public class LoginActivity extends AppCompatActivity {
    private LoginViewModel viewModel; //future enhancement: replace with dependency injection
    private Button loginButton;
    private TextInputLayout userNameTextInputLayout;
    private TextInputEditText userNameEditText;
    private TextInputLayout passwordTextInputLayout;
    private TextInputEditText passwordEditText;
    private ProgressBar progressBar;
    private TextView errorMessageView;

    final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            checkFormFields();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        initializeView();
    }

    private void initializeView() {
        userNameTextInputLayout = findViewById(R.id.username_layout);
        userNameEditText = findViewById(R.id.username_edittext);
        passwordTextInputLayout = findViewById(R.id.password_layout);
        passwordEditText = findViewById(R.id.password_edittext);
        loginButton = findViewById(R.id.login_button);
        progressBar = findViewById(R.id.loading_spinner);
        errorMessageView = findViewById(R.id.error_message);

        loginButton.setOnClickListener(view -> {
            errorMessageView.setVisibility(View.GONE);
            hideKeyboard(view);

            //this condition should always be met due to validation but will check anyway to be safe
            if (userNameEditText.getText() != null && passwordEditText.getText() != null) {
                progressBar.setVisibility(View.VISIBLE);
                String userNameInput = userNameEditText.getText().toString();
                String passwordInput = passwordEditText.getText().toString();
                //clear form fields for security reasons. e.g. call fails and user has walked away
                userNameEditText.setText("");
                passwordEditText.setText("");

                viewModel.onClick(userNameInput, passwordInput).observe(this, user -> {
                    progressBar.setVisibility(View.GONE);

                    if (user != null) {
                        Bundle bundle = new Bundle();
                        if (user.getUserType() == User.UserType.administrator) {
                            bundle.putParcelable("user", user);
                            Intent intent = new Intent(this, AdminActivity.class);
                            intent.putExtra("bundle", bundle);
                            startActivity(intent);
                            finish();
                        } else {
                            bundle.putParcelable("user", user);
                            Intent intent = new Intent(this, ShiftActivity.class);
                            intent.putExtra("bundle", bundle);
                            startActivity(intent);
                            finish();
                        }
                    } else { // error state
                        // future enhancement -- handle different error types differently
                        // (e.g. user not found, bad password, socket timeout, etc)
                        errorMessageView.setVisibility(View.VISIBLE);
                    }
                });
            }
        });

        loginButton.setEnabled(false);

        userNameEditText.addTextChangedListener(textWatcher);
        passwordEditText.addTextChangedListener(textWatcher);
    }

    private boolean checkUserName() {
        if (userNameEditText != null && userNameEditText.getText() != null
                && !StringUtils.isEmpty(userNameEditText.getText().toString())) {
            userNameTextInputLayout.setHelperText(null);
            return true;
        } else {
            userNameTextInputLayout.setHelperText(getResources().getString(R.string.login_field_helper));
            return false;
        }
    }

    private boolean checkPassword() {
        if (passwordEditText != null && passwordEditText.getText() != null
                && !StringUtils.isEmpty(passwordEditText.getText().toString())) {
            passwordTextInputLayout.setHelperText(null);
            return true;
        } else {
            passwordTextInputLayout.setHelperText(getResources().getString(R.string.login_field_helper));
            return false;
        }
    }

    //enable login button if neither field is empty
    //FUTURE ENHANCEMENT -- further validation for userName and password
    private void checkFormFields() {
        loginButton.setEnabled(checkUserName() && checkPassword());
    }

    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager)
                getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getApplicationWindowToken(),0);
    }
}