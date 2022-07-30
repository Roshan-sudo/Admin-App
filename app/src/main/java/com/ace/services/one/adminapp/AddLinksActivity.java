package com.ace.services.one.adminapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddLinksActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_links);

        // Link XML Components
        EditText et_link = findViewById(R.id.et_link);
        Button btnAdd = findViewById(R.id.btnAdd);

        // Firebase Database Initialization
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Links");

        int linkType = getIntent().getIntExtra("link", -1);

        btnAdd.setOnClickListener(view -> {
            String link = et_link.getText().toString().trim();
            if (link.isEmpty()){
                Toast.makeText(this, "Please type/paste the link!!!", Toast.LENGTH_SHORT).show();
            }else {
                if (linkType >= 0){
                    if (linkType == MainActivity.AD_LINK)
                        databaseReference.child("homePageAd").setValue(link);
                    else if (linkType == MainActivity.HELP_LINK)
                        databaseReference.child("helpLink").setValue(link);
                    else if (linkType == MainActivity.KYC_LINK)
                        databaseReference.child("kycLink").setValue(link);
                    Toast.makeText(this, "Link successfully added.", Toast.LENGTH_SHORT).show();
                }else Toast.makeText(this, "Something went wrong, Please try again!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}