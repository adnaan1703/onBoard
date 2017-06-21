package com.konel.kryptapps.onboard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.konel.kryptapps.onboard.Home.HomeActivity;
import com.konel.kryptapps.onboard.model.User;
import com.konel.kryptapps.onboard.utils.CodeUtil;
import com.konel.kryptapps.onboard.utils.PreferenceUtil;

import java.util.concurrent.TimeUnit;

/**
 * @author Anupam Singh
 * @version 1.0
 * @since 2017-06-21
 */
public class OnBoardingActivity extends Activity implements
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {

    private static final String TAG = "onBoardLog";
    private static final int RC_SIGN_IN = 9001;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private GoogleApiClient mGoogleApiClient;
    private ProgressBar progressBar;
    private EditText userPhoneNumber;
    private Button loginButton;
    private FirebaseUser user;
    private String mVerificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding_activity);
        progressBar = (ProgressBar) findViewById(R.id.onboarding_progressbar);
        userPhoneNumber = (EditText) findViewById(R.id.user_phone_number);
        loginButton = (Button) findViewById(R.id.user_loginbutton);
        loginButton.setOnClickListener(this);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }

        };

        if (CodeUtil.isConnectedToInternet(this))
            signIn();
        else {
            Toast.makeText(this, getString(R.string.kindly_check_your_internet), Toast.LENGTH_SHORT);
            finish();
        }

    }


    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                // ...
            }
        }
    }


    // [START auth_with_google]
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        // [START_EXCLUDE silent]
        showProgressDialog();
        // [END_EXCLUDE]
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(OnBoardingActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // [START_EXCLUDE]
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        //here we will check for user data and open the home screen or onbaording screen if the data is incorrect
        if (TextUtils.isEmpty(user.getPhoneNumber())) {
            //the case when phone number is not present
            userPhoneNumber.setVisibility(View.VISIBLE);
            loginButton.setVisibility(View.VISIBLE);
            Toast.makeText(this, " kindly verify your phone  number", Toast.LENGTH_SHORT).show();
        } else {
            //phone number is there , verify it first
            openHomeScreen(user.getPhoneNumber());
        }
    }

    private void hideProgressDialog() {
        progressBar.setVisibility(View.GONE);
    }

    private void showProgressDialog() {
        progressBar.setVisibility(View.VISIBLE);
    }


//    private void signOut() {
//        // Firebase sign out
//        mAuth.signOut();
//
//        // Google sign out
//        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
//                new ResultCallback<Status>() {
//                    @Override
//                    public void onResult(@NonNull Status status) {
//                        //  updateUI(null);
//                    }
//                });
//    }
//
//    private void revokeAccess() {
//        // Firebase sign out
//        mAuth.signOut();
//        // Google revoke access
//        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
//                new ResultCallback<Status>() {
//                    @Override
//                    public void onResult(@NonNull Status status) {
//                        // updateUI(null);
//                    }
//                });
//    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

//    private void checkLoginOnFb(String email, String password) {
//        mAuth.signInWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
//                        // If sign in fails, display a message to the user. If sign in succeeds
//                        // the auth state listener will be notified and logic to handle the
//                        // signed in user can be handled in the listener.
//                        if (!task.isSuccessful()) {
//                            Log.w(TAG, "signInWithEmail", task.getException());
//                            Toast.makeText(OnBoardingActivity.this, "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();
//                        } else {
//                            //sign in is completed;
//                            getUserDataFromFB();
//                        }
//
//                        // ...
//                    }
//                });
//    }

//    private void getUserDataFromFB() {
//
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        if (user != null) {
//            // Name, email address, and profile photo Url
//            String name = user.getDisplayName();
//            String email = user.getEmail();
//            Uri photoUrl = user.getPhotoUrl();
//            // The user's ID, unique to the Firebase project. Do NOT use this value to
//            // authenticate with your backend server, if you have one. Use
//            // FirebaseUser.getToken() instead.
//            String uid = user.getUid();
//        }
//    }

//    private void createAnUser(String email, String password) {
//        mAuth.createUserWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
//
//                        // If sign in fails, display a message to the user. If sign in succeeds
//                        // the auth state listener will be notified and logic to handle the
//                        // signed in user can be handled in the listener.
//                        if (!task.isSuccessful()) {
//                            Toast.makeText(OnBoardingActivity.this, "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();
//                        }
//
//                        // ...
//                    }
//                });
//    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
//        if (i == R.id.sign_in_button) {
//            signIn();
//        } else if (i == R.id.sign_out_button) {
//            signOut();
//        } else if (i == R.id.disconnect_button) {
//            revokeAccess();
//        }

        if (i == R.id.user_loginbutton) {
            displayAndCreateRtduser();
        }

    }

    private void displayAndCreateRtduser() {
        String userPhoneNum = userPhoneNumber.getText().toString();
        if (isValidPhoneNumber(userPhoneNum)) {
            onSubmit(userPhoneNum);

        } else {
            Toast.makeText(this, "kindly enter a valid phone number", Toast.LENGTH_SHORT).show();
        }

    }

    public void onSubmit(String phoneNumber1) {
        String phoneNumber = phoneNumber1;
        phoneNumber = "+91" + phoneNumber;
        final String finalPhoneNumber = phoneNumber;
        PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback =
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                        signInWithCredentials(phoneAuthCredential, finalPhoneNumber);
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

    private void signInWithCredentials(PhoneAuthCredential phoneAuthCredential, final String phoneNumber) {

        user.updatePhoneNumber(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.e("status", String.valueOf(task.isSuccessful()));
                if (task.isSuccessful()) {
                    User userOb;
                    String fcmId = PreferenceUtil.getString(PreferenceUtil.FCM_TOKEN);
                    if (!TextUtils.isEmpty(fcmId)) {
                        userOb = new User(user.getDisplayName(), user.getEmail(), phoneNumber, String.valueOf(user.getPhotoUrl()), fcmId);
                    } else
                        userOb = new User(user.getDisplayName(), user.getEmail(), String.valueOf(user.getPhotoUrl()), phoneNumber);

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference userCollection = database.getReference("users");
                    DatabaseReference userObject = userCollection.child(phoneNumber);
                    userObject.setValue(userOb);
                    // Opening Home after succesful user creation

                } else {
                    Toast.makeText(OnBoardingActivity.this, "error updating", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void openHomeScreen(String phoneNumber) {
        Intent intent = new Intent(OnBoardingActivity.this, HomeActivity.class);
        startActivity(intent);
        PreferenceUtil.setString(PreferenceUtil.USER_ID, phoneNumber);
        finish();
    }



    private boolean isValidPhoneNumber(String text) {

        return !TextUtils.isEmpty(text) && text.length() == 10;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
// An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }
}