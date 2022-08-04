package com.ace.services.one.adminapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.ace.services.one.adminapp.adapters.UserListAdapter;
import com.ace.services.one.adminapp.models.LoansModel;
import com.ace.services.one.adminapp.models.RequestLoanModel;
import com.ace.services.one.adminapp.models.UserModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class UsersListActivity extends AppCompatActivity {
    private static final int KYC_NOT_VERIFIED = 0;

    private final Map<String, String> kycList = new HashMap<>();
    private final Map<String, String> toBeApproveList = new HashMap<>();
    public static LoansModel loansModel;
    public static RequestLoanModel requestLoanModel;
    public static UserModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_list);

        // Firebase Initialization
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users");

        // Link XML Components
        RecyclerView recyclerView = findViewById(R.id.userListRecyclerView);

        // Get Data From Previous Activity
        String nextActivity = getIntent().getStringExtra("nextActivity");

        // Set title for this activity
        Objects.requireNonNull(getSupportActionBar()).setTitle("Users List");

        // Fetch Data from Database
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                kycList.clear();
                toBeApproveList.clear();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    userModel = dataSnapshot.child("ProfileActivity").getValue(UserModel.class);
                    requestLoanModel = dataSnapshot.child("RequestLoanActivity").getValue(RequestLoanModel.class);

                    // Check if next activity is "KYCVerificationActivity" or "ApproveLoanActivity"
                    if (Objects.equals(nextActivity, "kycVerification")){
                        // Get the list of all users whose KYC verification is pending
                        assert userModel != null;
                        if (userModel.getKycStatus() == KYC_NOT_VERIFIED)
                            kycList.put(userModel.getPhone(), userModel.getUserId());
                    }else if (Objects.equals(nextActivity, "approveLoan")){
                        // Get the list of all users whose loans are pending to approve
                        for (DataSnapshot loan : dataSnapshot.child("Loans").getChildren()){
                            loansModel = loan.getValue(LoansModel.class);
                            assert loansModel != null;
                            if (!loansModel.isApproved()){
                                toBeApproveList.put(userModel.getPhone(), userModel.getUserId());
                                break;
                            }
                        }
                    }
                }

                // Set up RecyclerView
                recyclerView.setLayoutManager(new LinearLayoutManager(UsersListActivity.this));

                List<String> phoneNos = new ArrayList<>();
                if (Objects.equals(nextActivity, "kycVerification"))
                    phoneNos = new ArrayList<>(kycList.keySet());
                else if (Objects.equals(nextActivity, "approveLoan"))
                    phoneNos = new ArrayList<>(toBeApproveList.keySet());

                UserListAdapter adapter = new UserListAdapter(phoneNos);
                recyclerView.setAdapter(adapter);

                adapter.setOnItemClickListener(phoneNo -> {
                    if (Objects.equals(nextActivity, "kycVerification")){
                        Intent intent = new Intent(UsersListActivity.this, KycVerificationActivity.class);
                        intent.putExtra("userId", kycList.get(phoneNo));
                        startActivity(intent);
                    } else if (Objects.equals(nextActivity, "approveLoan")){
                        Intent intent = new Intent(UsersListActivity.this, ApproveLoanActivity.class);
                        intent.putExtra("userId", toBeApproveList.get(phoneNo));
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}