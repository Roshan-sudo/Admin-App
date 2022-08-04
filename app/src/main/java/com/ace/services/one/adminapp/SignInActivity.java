package com.ace.services.one.adminapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ace.services.one.adminapp.models.AdminModel;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class SignInActivity extends AppCompatActivity {
    // XML Components
    private TextView p_text, o_text;
    private ImageView p_text_img, o_text_img;
    private EditText phone, otp;
    private CardView get_otp, verify_otp;
    private ProgressBar get_otp_progressBar, verify_otp_progressBar;

    private String verificationId = "";
    private String adminId = "";
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        // Link XML Components
        p_text = findViewById(R.id.p_text);
        o_text = findViewById(R.id.o_text);
        p_text_img = findViewById(R.id.p_text_img);
        o_text_img = findViewById(R.id.o_text_img);
        phone = findViewById(R.id.phone);
        otp = findViewById(R.id.otp);
        get_otp = findViewById(R.id.get_otp);
        verify_otp = findViewById(R.id.verify_otp);
        get_otp_progressBar = findViewById(R.id.get_otp_progressBar);
        verify_otp_progressBar = findViewById(R.id.verify_otp_progressBar);

        // Firebase initialization
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        // CLick listener on "Request OTP" button
        get_otp.setOnClickListener(view -> {
            // Get phone number from editText
            String txtPhone = phone.getText().toString().trim();

            if (isPhoneValid(txtPhone) && !txtPhone.isEmpty()){
                // Phone number is valid - Send OTP

                // Disable "Request OTP" button
                get_otp.setEnabled(false);
                get_otp.setCardBackgroundColor(ContextCompat.getColor(SignInActivity.this, R.color.light_button));
                get_otp_progressBar.setVisibility(View.VISIBLE);

                PhoneAuthOptions phoneAuthOptions = PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber("+91" + txtPhone)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(SignInActivity.this)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                // Enable "Request OTP" button
                                get_otp.setEnabled(true);
                                get_otp.setCardBackgroundColor(ContextCompat.getColor(SignInActivity.this, R.color.button));
                                get_otp_progressBar.setVisibility(View.GONE);

                                if (e instanceof FirebaseAuthInvalidCredentialsException)
                                    Toast.makeText(SignInActivity.this, "Invalid request", Toast.LENGTH_SHORT).show();
                                else if (e instanceof FirebaseTooManyRequestsException)
                                    Toast.makeText(SignInActivity.this, "SMS quota for the project has been exceeded", Toast.LENGTH_LONG).show();
                                else
                                    Toast.makeText(SignInActivity.this, "Error : " + e, Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(s, forceResendingToken);
                                verificationId = s;

                                // Enable "Request OTP" button
                                get_otp.setEnabled(true);
                                get_otp.setCardBackgroundColor(ContextCompat.getColor(SignInActivity.this, R.color.button));
                                get_otp_progressBar.setVisibility(View.GONE);

                                // Make required XML components visible
                                p_text.setVisibility(View.GONE);
                                p_text_img.setVisibility(View.GONE);
                                get_otp.setVisibility(View.GONE);
                                phone.setVisibility(View.INVISIBLE);
                                o_text.setVisibility(View.VISIBLE);
                                o_text_img.setVisibility(View.VISIBLE);
                                verify_otp.setVisibility(View.VISIBLE);
                                otp.setVisibility(View.VISIBLE);
                                otp.requestFocus();
                            }
                        })
                        .build();
                PhoneAuthProvider.verifyPhoneNumber(phoneAuthOptions);
            }else {
                // Phone number is invalid - show toast message
                Toast.makeText(this, "Please enter valid phone number", Toast.LENGTH_SHORT).show();
            }
        });

        // Click listener on "Verify OTP" button
        verify_otp.setOnClickListener(view -> {
            // Get OTP from editText
            String txtOtp = otp.getText().toString().trim();

            if (txtOtp.isEmpty())
                Toast.makeText(this, "Please enter OTP", Toast.LENGTH_SHORT).show();
            else {
                // Disable "Verify Code" button
                verify_otp.setEnabled(false);
                verify_otp.setCardBackgroundColor(ContextCompat.getColor(SignInActivity.this, R.color.light_button));
                verify_otp_progressBar.setVisibility(View.VISIBLE);

                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, txtOtp);
                auth.signInWithCredential(credential)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()){
                                // Sign in success
                                adminId = Objects.requireNonNull(auth.getCurrentUser()).getUid();

                                // Check if user already exists, if not - add user
                                databaseReference.child("admin").child(adminId).addValueEventListener(isUserExistsListener);
//                                databaseReference.removeEventListener(isUserExistsListener);

                                // Open HomeActivity
                                startActivity(new Intent(SignInActivity.this, MainActivity.class));
                                SignInActivity.this.finish();
                            }else {
                                // Sign in failed
                                if (task.getException() instanceof FirebaseAuthInvalidCredentialsException){
                                    Toast.makeText(this, "Enter correct OTP", Toast.LENGTH_SHORT).show();

                                    // Enable "Verify Code" button
                                    verify_otp.setEnabled(true);
                                    verify_otp.setCardBackgroundColor(ContextCompat.getColor(SignInActivity.this, R.color.button));
                                    verify_otp_progressBar.setVisibility(View.GONE);
                                }
                                else{
                                    Toast.makeText(this, "Please try again", Toast.LENGTH_SHORT).show();
                                    // Reload activity
                                    recreate();
                                }
                            }
                        });
            }
        });
    }

    /**
     * Function to check if the given phone number is valid or not
     * @param txtPhone Phone number
     * @return Returns "true" if the phone no. is valid else "false"
     */
    private boolean isPhoneValid(String txtPhone) {
        Pattern pattern = Pattern.compile("[6-9][0-9]{9}");
        return pattern.matcher(txtPhone).matches();
    }

    /**
     * ValueEventListener
     * to check if current user already exists in Database
     * if not - add user details to Database
     */
    private final ValueEventListener isUserExistsListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (!snapshot.exists()){
                AdminModel adminModel = new AdminModel(adminId, "NA", Objects.requireNonNull(auth.getCurrentUser()).getPhoneNumber());
                databaseReference.child("admin").child(adminId).setValue(adminModel);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    @Override
    protected void onStart() {
        super.onStart();

        // If the user is logged in - redirect to "Home Activity"
        if (auth.getCurrentUser() != null){
            adminId = auth.getCurrentUser().getUid();
            startActivity(new Intent(SignInActivity.this, MainActivity.class));
            SignInActivity.this.finish();
        }
    }
}