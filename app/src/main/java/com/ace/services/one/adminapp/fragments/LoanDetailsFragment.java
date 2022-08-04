package com.ace.services.one.adminapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ace.services.one.adminapp.R;
import com.ace.services.one.adminapp.UsersListActivity;
import com.ace.services.one.adminapp.models.PassbookModel;
import com.ace.services.one.adminapp.models.UpcomingEmiModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class LoanDetailsFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    private String userId;

    public LoanDetailsFragment() {
        // Required empty public constructor
    }

    public static LoanDetailsFragment newInstance(String userId) {
        LoanDetailsFragment fragment = new LoanDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userId = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_loan_details, container, false);

        // Link XML Components
        TextView loan_amount = view.findViewById(R.id.loan_amount);
        TextView loan_tenure = view.findViewById(R.id.loan_tenure);
        TextView interest_rate = view.findViewById(R.id.interest_rate);
        TextView processing_fee_rate = view.findViewById(R.id.processing_fee_rate);
        Button btnApprove = view.findViewById(R.id.btnApprove);

        // Firebase Database Initialization
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(userId);

        loan_amount.setText(String.format("₹ %s", UsersListActivity.loansModel.getLoanAmount()));
        loan_tenure.setText(String.format(Locale.ENGLISH, "%d Months", UsersListActivity.loansModel.getLoanTenure()));
        interest_rate.setText(String.format("%s%%", UsersListActivity.requestLoanModel.getInterestRate()));
        processing_fee_rate.setText(String.format("%s%%", UsersListActivity.requestLoanModel.getProcessingFeeRate()));

        btnApprove.setOnClickListener(view1 -> {
            // Update the user selected values (loan amount and tenure) to database
            String loanId = databaseReference.child("HomeActivity").push().getKey();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
            String currentDate = formatter.format(LocalDate.now());

            UpcomingEmiModel upcomingEmiModel = new UpcomingEmiModel(UsersListActivity.loansModel.getLoanAmount(), getEmiDate(1), loanId);
            PassbookModel passbookModel = new PassbookModel(UsersListActivity.loansModel.getLoanTenure(), true, currentDate, loanId);
            assert loanId != null;
            databaseReference.child("HomeActivity").child(loanId).setValue(upcomingEmiModel);
            databaseReference.child("PassbookActivity").child(loanId).setValue(passbookModel);

            Toast.makeText(requireActivity(), "Loan Approved Successfully.", Toast.LENGTH_LONG).show();
        });

        return view;
    }

    /**
     * Function to add the given months in the current date to get the new date
     * @param emiDuration EMI duration in months
     * @return returns the new calculated emi date
     */
    private String getEmiDate(int emiDuration){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
        LocalDate emiDate = LocalDate.now().plusMonths(emiDuration);
        return formatter.format(emiDate);
    }
}