package com.example.mastermindgame;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UsersCredentials extends AppCompatActivity {
    FirebaseStorage storage;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    TextInputEditText usernamefield, useremailfield, userpasswordfield, userconfirmpasswordfield, usereasyscore, usermediumscore, userhardscore;
    TextInputLayout namefielderror, emailfielderror, passwordfielderror, confirmpasswordfielderror;
    TextView imageerrortext;
    ImageButton userimgfield;
    Button UserUpdateBtn, userDeleteBtn;
    String UserId;
    Uri u;
    ViewGroup layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_credentials);
        UserId = getIntent().getStringExtra("Id");
        usernamefield = findViewById(R.id.usernamefield);
        useremailfield = findViewById(R.id.useremailfield);
        userpasswordfield = findViewById(R.id.userpasswordfield);
        userconfirmpasswordfield = findViewById(R.id.userconfirmpasswordfield);
        userimgfield = findViewById(R.id.userimgfield);
        UserUpdateBtn = findViewById(R.id.userUpdateBtn);
        imageerrortext = findViewById(R.id.imageerrortext);
        namefielderror = findViewById(R.id.namefielderror);
        emailfielderror = findViewById(R.id.regemailfielderror);
        passwordfielderror = findViewById(R.id.regpasswordfilderror);
        confirmpasswordfielderror = findViewById(R.id.regconfirmpasswordfielderror);
        layout = findViewById(R.id.usercred);
        userDeleteBtn = findViewById(R.id.userDeleteBtn);
        usereasyscore = findViewById(R.id.usereasyscroe);
        usermediumscore = findViewById(R.id.usermediumscroe);
        userhardscore = findViewById(R.id.userhardscroe);
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(UserId);
        getData();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        useremailfield.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                emailfielderror.setError("");
                if (!isValidEmailId(s.toString().trim())) {
                    emailfielderror.setError("Enter a Valid Email");
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        userimgfield.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, 404);
            }
        });
        userDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainMenu.class);
                startActivity(intent);
                finish();
                databaseReference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Snackbar.make(layout, "Acount Deleted", Snackbar.LENGTH_SHORT).show();

                    }
                });
                finish();
            }
        });
        UserUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Name = usernamefield.getText().toString().trim();
                String Email = useremailfield.getText().toString().trim();
                String Password = userpasswordfield.getText().toString().trim();
                String easy = usereasyscore.getText().toString().trim();
                String medium = usermediumscore.getText().toString().trim();
                String hard = userhardscore.getText().toString().trim();
                // for registering
                if (!Name.isEmpty() && !Email.isEmpty() && !Password.isEmpty()) {
                    if (Password.length() > 8) {
                        if (isValidPassword(Password)) {
                            if (emailfielderror.getError() == null) {
                                if (Password.equals(userconfirmpasswordfield.getText().toString().trim())) {
                                    final StorageReference ref = storageReference.child("Users Images/" + u.getLastPathSegment());
                                    ref.putFile(u).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    Map<String, Object> user = new HashMap<>();
                                                    user.put("Name", Name);
                                                    user.put("Email", Email);
                                                    user.put("Password", Password);
                                                    user.put("Profile Picture", uri.toString());
                                                    user.put("Easy Score", Long.valueOf(easy));
                                                    user.put("Medium Score", Long.valueOf(medium));
                                                    user.put("Hard Score", Long.valueOf(hard));
                                                    databaseReference.updateChildren(user);
                                                    Intent intent = new Intent(getApplicationContext(), MainMenu.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            });

                                        }

                                    });
                                    Snackbar.make(layout, "Acount Updated", Snackbar.LENGTH_SHORT).show();
                                } else {
                                    confirmpasswordfielderror.setError("Password not matched");
                                }
                            }
                        } else {
                            passwordfielderror.setError("Use Special Characters and Numbers");
                        }
                    } else {
                        passwordfielderror.setError("Use 8 characters or more for your password");
                    }
                } else {
                    if (u == null) {
                        imageerrortext.setText("Select Image");
                        imageerrortext.setTextColor(getResources().getColor(R.color.red));
                    } else if (Name.isEmpty()) {
                        namefielderror.setError("Enter Name");
                    } else if (Email.isEmpty()) {
                        emailfielderror.setError("Enter Email");
                    } else if (Password.isEmpty()) {
                        passwordfielderror.setError("Enter Password");
                    } else if (userconfirmpasswordfield.getText().toString().trim().isEmpty()) {
                        confirmpasswordfielderror.setError("Enter Password again");
                    }
                }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 404) {
            if (data != null) {
                u = data.getData();
                userimgfield.setImageURI(u);
            }
        }
    }

    private boolean isValidEmailId(String email) {

        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }

    public boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }

    void getData() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                u = Uri.parse(snapshot.child("Profile Picture").getValue(String.class));
                Glide.with(getApplicationContext()).load(snapshot.child("Profile Picture").getValue(String.class)).error(R.drawable.ic_person).dontAnimate().into(userimgfield);
                usernamefield.setText(snapshot.child("Name").getValue(String.class));
                useremailfield.setText(snapshot.child("Email").getValue(String.class));
                userpasswordfield.setText(snapshot.child("Password").getValue(String.class));
                userconfirmpasswordfield.setText(snapshot.child("Password").getValue(String.class));
                usereasyscore.setText("" + (long) snapshot.child("Easy Score").getValue());
                usermediumscore.setText("" + (long) snapshot.child("Medium Score").getValue());
                userhardscore.setText("" + (long) snapshot.child("Hard Score").getValue());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static String getDefaults(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, null);
    }
}