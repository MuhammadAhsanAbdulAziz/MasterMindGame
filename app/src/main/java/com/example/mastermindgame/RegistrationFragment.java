package com.example.mastermindgame;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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

public class RegistrationFragment extends Fragment {
    FirebaseStorage storage;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    TextInputEditText namefield, emailfield, passwordfield, confirmpasswordfield;
    TextInputLayout namefielderror, emailfielderror, passwordfielderror, confirmpasswordfielderror;
    TextView imageerrortext;
    ImageButton imgfield;
    Button regRegisterBtn;
    Uri u;
    ViewGroup layout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_registration, container, false);
        namefield = view.findViewById(R.id.namefield);
        emailfield = view.findViewById(R.id.regemailfield);
        passwordfield = view.findViewById(R.id.regpasswordfild);
        confirmpasswordfield = view.findViewById(R.id.regconfirmpasswordfield);
        imgfield = view.findViewById(R.id.imgfield);
        regRegisterBtn = view.findViewById(R.id.regRegisterBtn);
        imageerrortext = view.findViewById(R.id.imageerrortext);
        namefielderror = view.findViewById(R.id.namefielderror);
        emailfielderror = view.findViewById(R.id.regemailfielderror);
        passwordfielderror = view.findViewById(R.id.regpasswordfilderror);
        confirmpasswordfielderror = view.findViewById(R.id.regconfirmpasswordfielderror);
        layout = view.findViewById(R.id.regfrag);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        emailfield.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                emailfielderror.setError("");
                if (!isValidEmailId(s.toString().trim())) {
                    emailfielderror.setError("Enter a Valid Email");
                }

                checkEmail(s.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {
                checkEmail(s.toString().trim());
            }
        });

        imgfield.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, 404);
            }
        });

        regRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Name = namefield.getText().toString().trim();
                String Email = emailfield.getText().toString().trim();
                String Password = passwordfield.getText().toString().trim();
                // for registering
                if (!Name.isEmpty() && !Email.isEmpty() && !Password.isEmpty() && u != null) {
                    if (Password.length() > 8) {
                        if (isValidPassword(Password)) {
                            if (emailfielderror.getError() == null) {
                                if (Password.equals(confirmpasswordfield.getText().toString().trim())) {
                                    String uniqueID = UUID.randomUUID().toString();
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
                                                    user.put("Easy Score", 0);
                                                    user.put("Medium Score", 0);
                                                    user.put("Hard Score", 0);
                                                    databaseReference.child(uniqueID).setValue(user);
                                                    Intent intent = new Intent(getContext(), Login.class);
                                                    startActivity(intent);
                                                    getActivity().finish();
                                                }
                                            });
                                        }
                                    });
                                    Snackbar.make(layout,"Acount Created Successfully",Snackbar.LENGTH_SHORT).show();
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
                    } else if (confirmpasswordfield.getText().toString().trim().isEmpty()) {
                        confirmpasswordfielderror.setError("Enter Password again");
                    }
                }
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 404) {
            if (data != null) {
                u = data.getData();
                imgfield.setImageURI(u);
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

    private void checkEmail(String email) {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String a = ds.child("Email").getValue(String.class);
                    if (email.equals(a)) {
                        emailfielderror.setError("Email already Exist");
                        return;

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }
}