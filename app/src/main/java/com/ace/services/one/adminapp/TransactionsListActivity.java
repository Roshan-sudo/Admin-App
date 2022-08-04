package com.ace.services.one.adminapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.ace.services.one.adminapp.adapters.TransactionListAdapter;
import com.ace.services.one.adminapp.models.TransactionModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class TransactionsListActivity extends AppCompatActivity {
    public static List<TransactionModel> transactionModels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions_list);

        // Firebase Initialization
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users");

        // Link XML Components
        RecyclerView recyclerView = findViewById(R.id.transListRecyclerView);

        // Set Title For This Activity
        Objects.requireNonNull(getSupportActionBar()).setTitle("Transactions List");

        // Get Data From Database
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                transactionModels.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot loanDetails : dataSnapshot.child("LoanTransactionsActivity").getChildren()) {
                        for (DataSnapshot transactionDetails : loanDetails.getChildren()){
                            TransactionModel transactionModel = transactionDetails.getValue(TransactionModel.class);
                            transactionModels.add(transactionModel);
                        }
                    }
                }

                // Sorting the "transactionModels" list by date in ascending order i.e, the latest transaction should be in first place
                transactionModels.sort(Comparator.comparing(TransactionModel::getDate));

                // Set up RecyclerView
                recyclerView.setLayoutManager(new LinearLayoutManager(TransactionsListActivity.this));

                TransactionListAdapter adapter = new TransactionListAdapter(transactionModels);
                recyclerView.setAdapter(adapter);

                adapter.setOnItemClickListener(position -> {
                    Intent intent = new Intent(TransactionsListActivity.this, TransactionDetailsActivity.class);
                    intent.putExtra("position", position);
                    startActivity(intent);
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}