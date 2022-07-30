package com.ace.services.one.adminapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ace.services.one.adminapp.models.CreditModel;
import com.ace.services.one.adminapp.models.RequestLoanModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class KycVerificationActivity extends AppCompatActivity {
    private static final int KYC_VERIFIED = 1;

    // XML Components
    private EditText et_interest_rate, et_max_loan_amount, et_tenure, et_processing_fee_rate, et_approved_limit_amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kyc_verification);

        // Link XML Components
        et_interest_rate = findViewById(R.id.et_interest_rate);
        et_max_loan_amount = findViewById(R.id.et_max_loan_amount);
        et_tenure = findViewById(R.id.et_tenure);
        et_processing_fee_rate = findViewById(R.id.et_processing_fee_rate);
        et_approved_limit_amount = findViewById(R.id.et_approved_limit_amount);
        Button btn_submit = findViewById(R.id.btn_submit);

        Objects.requireNonNull(getSupportActionBar()).setTitle("KYC Verification");

        // Initialize Firebase
        String userId = getIntent().getStringExtra("userId");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(userId);

        btn_submit.setOnClickListener(view -> {
            String interestRate = et_interest_rate.getText().toString().trim();
            String maxLoanAmount = et_max_loan_amount.getText().toString().trim();
            String maxTenure = et_tenure.getText().toString().trim();
            String processingFeeRate = et_processing_fee_rate.getText().toString().trim();
            String approvedLimit = et_approved_limit_amount.getText().toString().trim();

            if (interestRate.isEmpty() || maxLoanAmount.isEmpty() || maxTenure.isEmpty() || processingFeeRate.isEmpty() || approvedLimit.isEmpty())
                Toast.makeText(this, "Please fill all the required fields!", Toast.LENGTH_LONG).show();
            else {
                CreditModel creditModel = new CreditModel(Float.parseFloat(approvedLimit), 0.0f);
                RequestLoanModel loanModel = new RequestLoanModel(Float.parseFloat(processingFeeRate), Float.parseFloat(interestRate), Float.parseFloat(maxLoanAmount), Integer.parseInt(maxTenure));

                databaseReference.child("ProfileActivity").child("kycStatus").setValue(KYC_VERIFIED);
                databaseReference.child("CreditActivity").setValue(creditModel);
                databaseReference.child("RequestLoanActivity").setValue(loanModel);

                Toast.makeText(this, "Data successfully updated in database.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}