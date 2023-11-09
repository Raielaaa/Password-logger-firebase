package com.example.passwordlogger.ui.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.passwordlogger.R;
import com.example.passwordlogger.Util;
import com.example.passwordlogger.databinding.ActivityMainBinding;
import com.example.passwordlogger.ui.list.ListActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    //  Date and Time
    private final Calendar currentTime = Calendar.getInstance();
    private final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy hh:mm a", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initClickedViews();
    }

    private void initClickedViews() {
        binding.btnViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeActivity();
            }
        });

        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String inputtedPassword = binding.etPassword.getText().toString();

                    insertPasswordToFirebase(inputtedPassword);
                } catch (Exception e) {
                    displayToast("Password field required!");
                }
            }
        });
    }

    private void insertPasswordToFirebase(String inputtedPassword) {
        if (inputtedPassword.isEmpty()) {
            Toast.makeText(
                    getApplicationContext(),
                    "Password required.",
                    Toast.LENGTH_LONG
            ).show();

            return;
        }

        FirebaseFirestore fireStore = FirebaseFirestore.getInstance();

        Map<String, String> map = new HashMap<>();
        map.put("password", inputtedPassword);
        map.put("currentDate", getCurrentBorrowDate());

        Util util = new Util();
        util.displayCustomDialog(
                this,
                R.layout.custom_dialog_loading
        );

        try {
            fireStore.collection("main-app")
                    .add(map)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            //  Message
                            displayToast("Password successfully added.");
                            util.dismissDialog();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //  Message
                            displayToast("An error occurred: " + e.getLocalizedMessage());
                            Log.e("MyTag", "onFailure: " + e.getMessage());

                            //  Close running dialog
                            util.dismissDialog();
                        }
                    });
        } catch (Exception e) {
            displayToast("Critical error occurred!");
            Log.e("MyTag", "insertPasswordToFirebase: " + e.getMessage());
            util.dismissDialog();
        } finally {
            binding.etPassword.setText("");
        }
    }

    private String getCurrentBorrowDate() {
        return sdf.format(currentTime.getTime());
    }

    private void displayToast(String message) {
        Toast.makeText(
                this,
                message,
                Toast.LENGTH_LONG
        ).show();
    }

    private void changeActivity() {
        Intent intent = new Intent(this, ListActivity.class);
        startActivity(intent);
    }
}