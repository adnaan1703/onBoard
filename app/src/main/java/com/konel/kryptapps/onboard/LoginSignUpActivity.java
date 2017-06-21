package com.konel.kryptapps.onboard;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class LoginSignUpActivity extends AppCompatActivity {

    private Unbinder unbinder;
    @BindView(R.id.et_phone_number)
    TextInputEditText et_phone;
    @BindView(R.id.et_otp)
    TextInputEditText et_otp;
    @BindView(R.id.container)
    LinearLayout container;

    private String mVerificationId;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_sign_up);
        unbinder = ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }

    @OnClick(R.id.bt_submit)
    public void onSubmit() {
        String phoneNumber = et_phone.getText().toString();
        phoneNumber = "+91" + phoneNumber;
        PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback =
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                        signInWithCredentials(phoneAuthCredential);
                        Log.d("KRYPTO_FIREBASE", "onVerificationCompleted : " + phoneAuthCredential.getSmsCode());
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        Log.d("KRYPTO_FIREBASE", "onVerificationFailed", e);
                    }

                    @Override
                    public void onCodeSent(String verificationId,
                                           PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(verificationId, forceResendingToken);
                        mVerificationId = verificationId;
                        Log.d("KRYPTO_FIREBASE", "onCodeSent: " + verificationId);

                    }
                };

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                this,
                mCallback
        );
    }

    @OnClick(R.id.btn_otp)
    public void onOtpSubmit() {
        String otp = et_otp.getText().toString();
        signInWithCredentials(PhoneAuthProvider.getCredential(mVerificationId, otp));

    }

    private void signInWithCredentials(@NonNull PhoneAuthCredential phoneAuthCredential) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Snackbar.make(container, "Login Successful", Snackbar.LENGTH_LONG).show();
                            user = task.getResult().getUser();
                            Log.d("KRYPTO_FIREBASE", String.format(Locale.getDefault(),
                                    "signInWithCredentials: Success %s %s %s %s",
                                    user.getDisplayName(),
                                    user.getPhoneNumber(),
                                    user.getEmail(),
                                    user.getPhotoUrl()));

                            User userOb = new User("halnoa", "999999999");

                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference usersCollection = database.getReference("users");
                            DatabaseReference userObject = usersCollection.child("1234567");
                            userObject.setValue(userOb);

                            userObject.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    Toast.makeText(LoginSignUpActivity.this, "1", Toast.LENGTH_SHORT);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                        } else {
                            Log.d("KRYPTO_FIREBASE", "signInWithCredentials: Failed", task.getException());
                        }
                    }
                });
    }
}
