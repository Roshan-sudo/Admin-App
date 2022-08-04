package com.ace.services.one.adminapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.ace.services.one.adminapp.models.TransactionModel;

public class TransactionDetailsActivity extends AppCompatActivity {
    // Variables For Transactions Status
    public static final int TRANSACTION_FAILED = 0;
    public static final int TRANSACTION_SUCCESS = 1;
    public static final int TRANSACTION_PENDING = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_details);

        // Link XML Components
        TextView transactionId = findViewById(R.id.transaction_id);
        TextView amount = findViewById(R.id.amount);
        TextView paymentMode = findViewById(R.id.payment_mode);
        TextView status = findViewById(R.id.status);
        TextView date = findViewById(R.id.date);
        TextView time = findViewById(R.id.time);
        TextView loanId = findViewById(R.id.loan_id);

        // Get Data from previous Activity
        int position = getIntent().getIntExtra("position", -1);

        if (position == -1){
            Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Set Values in the UI
        TransactionModel transactionModel = TransactionsListActivity.transactionModels.get(position);
        transactionId.setText(transactionModel.getTransactionId());
        amount.setText(String.format("Rs %s", transactionModel.getAmount()));
        paymentMode.setText(transactionModel.getPaymentMode());
        date.setText(transactionModel.getDate());
        time.setText(transactionModel.getTime());
        loanId.setText(transactionModel.getLoanId());

        // Set transaction status
        int transactionStatus = transactionModel.getStatus();
        if (transactionStatus == TRANSACTION_FAILED) status.setText("Failed");
        else if (transactionStatus == TRANSACTION_PENDING) status.setText("Pendins");
        else if (transactionStatus == TRANSACTION_SUCCESS) status.setText("Success");
    }
}