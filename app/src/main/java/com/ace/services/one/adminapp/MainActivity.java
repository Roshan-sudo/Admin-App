package com.ace.services.one.adminapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.ace.services.one.adminapp.adapters.MainActivityAdapter;
import com.ace.services.one.adminapp.fragments.KycVerificationFragment;
import com.ace.services.one.adminapp.fragments.ToBeApprovedLoansFragment;
import com.ace.services.one.adminapp.models.LoansModel;
import com.ace.services.one.adminapp.models.RequestLoanModel;
import com.ace.services.one.adminapp.models.UserModel;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final int KYC_NOT_VERIFIED = 0;

    public static final int AD_LINK = 0;
    public static final int HELP_LINK = 1;
    public static final int KYC_LINK = 2;

    // XML Components
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;

    public static Map<String, String> kycList = new HashMap<>();
    public static Map<String, String> toBeApproveList = new HashMap<>();
    public static LoansModel loansModel;
    public static RequestLoanModel requestLoanModel;
    public static UserModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Firebase Initialization
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users");

        // Link XML Components
        tabLayout = findViewById(R.id.tabLayout);
        viewPager2 = findViewById(R.id.viewPager);

        // Add tabs to tabLayout
        tabLayout.addTab(tabLayout.newTab().setText("KYC Verification"));
        tabLayout.addTab(tabLayout.newTab().setText("To Be Approved"));

        // Fetch Data from Database
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                kycList.clear();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    userModel = dataSnapshot.child("ProfileActivity").getValue(UserModel.class);
                    requestLoanModel = dataSnapshot.child("RequestLoanActivity").getValue(RequestLoanModel.class);
                    assert userModel != null;
                    if (userModel.getKycStatus() == KYC_NOT_VERIFIED)
                        kycList.put(userModel.getPhone(), userModel.getUserId());
                    else {
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

                // initialize fragment adapter
                MainActivityAdapter fragmentAdapter = new MainActivityAdapter(getSupportFragmentManager(), getLifecycle());
                fragmentAdapter.addFragment(new KycVerificationFragment());
                fragmentAdapter.addFragment(new ToBeApprovedLoansFragment());
                viewPager2.setAdapter(fragmentAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });
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