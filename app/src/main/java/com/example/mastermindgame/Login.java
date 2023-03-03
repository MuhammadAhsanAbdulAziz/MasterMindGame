package com.example.mastermindgame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

public class Login extends AppCompatActivity {

    Button registerbtn, loginbtnsubmit;
    EditText loginemailfiled, loginpasswordfiled;
    DatabaseReference databaseReference;
    TextInputLayout loginemailfielderror, loginpasswordfielderror;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        registerbtn = findViewById(R.id.registerbtn);
        loginbtnsubmit = findViewById(R.id.loginbtnsubmit);
        loginemailfiled = findViewById(R.id.loginemailfiled);
        loginpasswordfiled = findViewById(R.id.loginpasswordfield);
        loginemailfielderror = findViewById(R.id.loginemailfielderror);
        loginpasswordfielderror = findViewById(R.id.loginpasswordfielderror);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String LOGIN_EMAIL = preferences.getString("Email", null);
        String LOGIN_PASSWORD = preferences.getString("Password", null);

        if (LOGIN_EMAIL != null && LOGIN_PASSWORD != null) {
            Intent intent = new Intent(Login.this, MainMenu.class);
            startActivity(intent);
            finish();
        }
        loginemailfiled.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                loginemailfielderror.setError("");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        loginbtnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email = loginemailfiled.getText().toString().trim();
                String Password = loginpasswordfiled.getText().toString().trim();
                if (!isValidEmailId(Email)) {
                    loginemailfielderror.setError("Enter Valid Email");
                } else if (!Email.isEmpty() && !Password.isEmpty()) {
                    checkEmailPass(Email, Password);
                } else {
                    if (Email.isEmpty()) {
                        loginemailfielderror.setError("Enter Email");
                    } else if (Password.isEmpty()) {
                        loginpasswordfielderror.setError("Enter Password");
                    }
                }
            }
        });

        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changefragment(new RegistrationFragment());
                registerbtn.setEnabled(false);
                loginbtnsubmit.setEnabled(false);
            }
        });
    }

    public void changefragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        fragmentTransaction.replace(R.id.myframe, fragment);
        fragmentTransaction.addToBackStack("regframe");
        fragmentTransaction.commit();
    }

    private void checkEmailPass(String Email, String Password) {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String email = ds.child("Email").getValue(String.class);
                    String password = ds.child("Password").getValue(String.class);
                    if (Email.equals(email) && Password.equals(password)) {
                        setDefaults("userId", ds.getKey(), getApplicationContext());
                        setDefaults("Email", email, getApplicationContext());
                        setDefaults("Password", password, getApplicationContext());
                        setDefaults("Difficulty", "Easy", getApplicationContext());
                        Intent intent = new Intent(Login.this, MainMenu.class);
                        startActivity(intent);
                        finish();
                        return;
                    }
                }
                loginemailfielderror.setError("Invalid Email");
                loginpasswordfielderror.setError("Invalid Password");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        getFragmentManager().popBackStack("regframe", 0);
        registerbtn.setEnabled(true);
        loginbtnsubmit.setEnabled(true);

    }

    private boolean isValidEmailId(String email) {

        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }

    public static void setDefaults(String key, String value, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void buttonEffect(View button) {
        button.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        v.getBackground().setColorFilter(0xe0f47521, PorterDuff.Mode.SRC_ATOP);
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        v.getBackground().clearColorFilter();
                        v.invalidate();
                        break;
                    }
                }
                return false;
            }
        });
    }
}