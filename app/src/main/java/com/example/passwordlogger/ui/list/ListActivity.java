package com.example.passwordlogger.ui.list;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.passwordlogger.R;
import com.example.passwordlogger.Util;
import com.example.passwordlogger.databinding.ActivityListBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class ListActivity extends AppCompatActivity {
    ArrayList<HashMap<String, String>> dataFromDB = new ArrayList<>();
    private ActivityListBinding binding;
    private FirebaseFirestore fireStore;
    private Util util;
    private String TAG = "MyTag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        startLoadingDialog();
        initRefreshButton();
        getDataFromFireStore();
    }

    private void startLoadingDialog() {
        util = new Util();
        util.displayCustomDialog(
                this,
                R.layout.custom_dialog_loading
        );
    }

    private void initRefreshButton() {
        binding.btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startLoadingDialog();
                getDataFromFireStore();
            }
        });
    }

    private void getDataFromFireStore() {
        dataFromDB.clear();
        fireStore = FirebaseFirestore.getInstance();

        fireStore.collection("main-app")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.isEmpty()) {
                            Log.d(TAG, "onSuccess: LIST EMPTY");
                            util.dismissDialog();
                            Toast.makeText(getApplicationContext(), "List empty", Toast.LENGTH_LONG).show();
                            return;
                        } else {
                            for (DocumentSnapshot documentChange : queryDocumentSnapshots.getDocuments()) {
                                HashMap<String, String> dataHolder = new HashMap<>();

                                dataHolder.put("password", documentChange.get("password").toString());
                                dataHolder.put("currentDate", documentChange.get("currentDate").toString());

                                dataFromDB.add(dataHolder);
                            }

                            binding.rvMain.setAdapter(new ListItemAdapter(getApplicationContext(), dataFromDB));
                            util.dismissDialog();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Error getting data!!!", Toast.LENGTH_LONG).show();
                        util.dismissDialog();
                        return;
                    }
                });
    }
}