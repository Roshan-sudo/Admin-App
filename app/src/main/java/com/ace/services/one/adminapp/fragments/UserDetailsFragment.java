package com.ace.services.one.adminapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ace.services.one.adminapp.MainActivity;
import com.ace.services.one.adminapp.R;

public class UserDetailsFragment extends Fragment {

    public UserDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_details, container, false);

        // Link XML Components
        TextView user_id = view.findViewById(R.id.user_id);
        TextView name = view.findViewById(R.id.name);
        TextView phone_no = view.findViewById(R.id.phone_no);
        TextView course = view.findViewById(R.id.course);
        TextView institute = view.findViewById(R.id.institute);

        user_id.setText(MainActivity.userModel.getUserId());
        name.setText(MainActivity.userModel.getName());
        phone_no.setText(MainActivity.userModel.getPhone());
        course.setText(MainActivity.userModel.getCourse());
        institute.setText(MainActivity.userModel.getInstitute());

        return view;
    }
}