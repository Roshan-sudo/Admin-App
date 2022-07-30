package com.ace.services.one.adminapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ace.services.one.adminapp.R;

import java.util.List;

public class KycVerificationAdapter extends RecyclerView.Adapter<KycVerificationAdapter.MyViewHolder> {
    private final List<String> phoneNosList;
    private OnItemClickListener mListener;

    public KycVerificationAdapter(List<String> phoneNosList) {
        this.phoneNosList = phoneNosList;
    }

    // Interface to detect click listener on list items
    public interface OnItemClickListener{ void OnItemClick(String phoneNo); }
    public void setOnItemClickListener(OnItemClickListener listener){ this.mListener = listener; }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @org.jetbrains.annotations.NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_adapter, parent, false);
        return new MyViewHolder(v, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull @org.jetbrains.annotations.NotNull MyViewHolder holder, int position) {
        holder.listText.setText(phoneNosList.get(position));
    }

    @Override
    public int getItemCount() {
        return phoneNosList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView listText;
        public MyViewHolder(@NonNull @org.jetbrains.annotations.NotNull View itemView, OnItemClickListener listener) {
            super(itemView);

            // Link XML Component
            listText = itemView.findViewById(R.id.listText);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.OnItemClick(phoneNosList.get(position));
                    }
                }
            });
        }
    }
}


