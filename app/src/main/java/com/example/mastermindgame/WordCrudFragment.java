package com.example.mastermindgame;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WordCrudFragment extends Fragment {

    EditText wordfield;
    TextInputLayout wordfielderror;
    RadioButton easyradiobtn, mediumradiobtn, hardradiobtn;
    Button submitbtn;
    RadioGroup diffgroup;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ViewGroup layout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_word_crud, container, false);

        wordfield = view.findViewById(R.id.wordfield);
        easyradiobtn = view.findViewById(R.id.easyradiobtn);
        mediumradiobtn = view.findViewById(R.id.mediumradiobtn);
        hardradiobtn = view.findViewById(R.id.hardradiobtn);
        submitbtn = view.findViewById(R.id.submitbtn);
        diffgroup = view.findViewById(R.id.diffgroup);
        wordfielderror = view.findViewById(R.id.wordfielderror);
        layout = view.findViewById(R.id.wordscurdfrag);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Words");

        wordfield.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                wordfielderror.setError("");
                searchInDatabase(databaseReference, s.toString().trim().toUpperCase());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String seletectedWord = wordfield.getText().toString().trim().toUpperCase();
                if (seletectedWord.equals("")) {
                    wordfielderror.setError("Enter a Word");
                } else {
                    if (wordfielderror.getError() == null) {
                        if (easyradiobtn.isChecked()) {
                            databaseReference = firebaseDatabase.getReference("Words").child("Easy");
                            String uniqueID = UUID.randomUUID().toString();
                            Map<String, Object> word = new HashMap<>();
                            word.put("Word", seletectedWord);
                            databaseReference.child(uniqueID).setValue(word);
                            wordfield.setText("");
                            diffgroup.clearCheck();
                            Snackbar.make(layout, "Word added", Snackbar.LENGTH_SHORT).show();
                            Intent intent = new Intent(getContext(), MainMenu.class);
                            startActivity(intent);
                            getActivity().finish();
                        } else if (mediumradiobtn.isChecked()) {
                            databaseReference = firebaseDatabase.getReference("Words").child("Medium");
                            String uniqueID = UUID.randomUUID().toString();
                            Map<String, Object> word = new HashMap<>();
                            word.put("Word", seletectedWord);
                            databaseReference.child(uniqueID).setValue(word);
                            wordfield.setText("");
                            diffgroup.clearCheck();
                            Snackbar.make(layout, "Word added", Snackbar.LENGTH_SHORT).show();
                            Intent intent = new Intent(getContext(), MainMenu.class);
                            startActivity(intent);
                            getActivity().finish();

                        } else if (hardradiobtn.isChecked()) {
                            databaseReference = firebaseDatabase.getReference("Words").child("Hard");
                            String uniqueID = UUID.randomUUID().toString();
                            Map<String, Object> word = new HashMap<>();
                            word.put("Word", seletectedWord);
                            databaseReference.child(uniqueID).setValue(word);
                            wordfield.setText("");
                            wordfield.clearFocus();
                            Snackbar.make(layout, "Word added", Snackbar.LENGTH_SHORT).show();
                            Intent intent = new Intent(getContext(), MainMenu.class);
                            startActivity(intent);
                            getActivity().finish();
                        }
                    }
                }

            }
        });
        return view;
    }

    public void searchInDatabase(DatabaseReference databaseReference, String word) {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    for (DataSnapshot dss : ds.getChildren()) {
                        if (word.equals(dss.child("Word").getValue(String.class))) {
                            wordfielderror.setError("Word already Exists");
                            return;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}