package com.ace.services.one.adminapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    public static final int AD_LINK = 0;
    public static final int HELP_LINK = 1;
    public static final int KYC_LINK = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Link XML Components
        Button btn_kyc = findViewById(R.id.btn_kyc);
        Button btn_approve = findViewById(R.id.btn_approve);
        Button btn_update = findViewById(R.id.btn_update);

        // Click listener on "KYC Verification" button
        btn_kyc.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, UsersListActivity.class);
            intent.putExtra("nextActivity", "kycVerification");
            startActivity(intent);
        });

        // Click listener on "Approve Loan" button
        btn_approve.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, UsersListActivity.class);
            intent.putExtra("nextActivity", "approveLoan");
            startActivity(intent);
        });

        // Click Listener On "Update Transaction" Button
        btn_update.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, TransactionsListActivity.class)));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent = new Intent(MainActivity.this, AddLinksActivity.class);
        switch (item.getItemId()){
            case R.id.adLink:
                intent.putExtra("link", AD_LINK);
                startActivity(intent);
                return true;
            case R.id.helpLink:
                intent.putExtra("link", HELP_LINK);
                startActivity(intent);
                return true;
            case R.id.kycLink:
                intent.putExtra("link", KYC_LINK);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}