package com.ace.services.one.adminapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;

import com.ace.services.one.adminapp.adapters.ApproveLoanAdapter;
import com.ace.services.one.adminapp.fragments.LoanDetailsFragment;
import com.ace.services.one.adminapp.fragments.UserDetailsFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

public class ApproveLoanActivity extends AppCompatActivity {
    // XML Components
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve_loan);

        // Link XML Components
        tabLayout = findViewById(R.id.tab_layout);
        viewPager2 = findViewById(R.id.view_pager);

        String userId = getIntent().getStringExtra("userId");

        Objects.requireNonNull(getSupportActionBar()).setTitle("Approve Loan");

        // Add tabs to tabLayout
        tabLayout.addTab(tabLayout.newTab().setText("Loan Details"));
        tabLayout.addTab(tabLayout.newTab().setText("User Details"));

        // initialize fragment adapter
        ApproveLoanAdapter fragmentAdapter = new ApproveLoanAdapter(getSupportFragmentManager(), getLifecycle());
        fragmentAdapter.addFragment(LoanDetailsFragment.newInstance(userId));
        fragmentAdapter.addFragment(new UserDetailsFragment());
        viewPager2.setAdapter(fragmentAdapter);

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
}