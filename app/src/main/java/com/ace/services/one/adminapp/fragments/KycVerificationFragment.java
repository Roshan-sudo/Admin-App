package com.ace.services.one.adminapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ace.services.one.adminapp.KycVerificationActivity;
import com.ace.services.one.adminapp.MainActivity;
import com.ace.services.one.adminapp.R;
import com.ace.services.one.adminapp.adapters.KycVerificationAdapter;

import java.util.ArrayList;
import java.util.List;

public class KycVerificationFragment extends Fragment {

    public KycVerificationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_kyc_verification, container, false);

        // Link XML Components
        RecyclerView recyclerView = view.findViewById(R.id.kycRecyclerView);

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        List<String> phoneNos = new ArrayList<>(MainActivity.kycList.keySet());
        KycVerificationAdapter adapter = new KycVerificationAdapter(phoneNos);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(phoneNo -> {
            Intent intent = new Intent(requireActivity(), KycVerificationActivity.class);
            intent.putExtra("userId", MainActivity.kycList.get(phoneNo));
            startActivity(intent);
        });

        return view;
    }
}