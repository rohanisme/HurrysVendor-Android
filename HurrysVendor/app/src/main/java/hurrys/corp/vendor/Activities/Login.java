package hurrys.corp.vendor.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import hurrys.corp.vendor.Configurations.Session;
import hurrys.corp.vendor.Models.Users;
import hurrys.corp.vendor.R;

public class Login extends AppCompatActivity {

    private TextView Tresend, skip, verify, otp;
    private EditText Tmobile;
    private FirebaseAuth mAuth;
    private boolean mVerificationInProgress = false;
    private Session session;
    ProgressBar progressBar;
    OtpView Tverify;
    String status = "";
    Users users;
    CountryCodePicker country;
    EditText mobilenumber;
    ImageView next;
    private static final String EMAIL = "email";

    TextView textView3;

    private static final String TAG = "PhoneAuthActivity";
    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";
    private static final int STATE_INITIALIZED = 1;
    private static final int STATE_CODE_SENT = 2;
    private static final int STATE_VERIFY_FAILED = 3;
    private static final int STATE_VERIFY_SUCCESS = 4;
    private static final int STATE_SIGNIN_FAILED = 5;
    private static final int STATE_SIGNIN_SUCCESS = 6;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Tmobile = findViewById(R.id.mobilenumber);
        mobilenumber = findViewById(R.id.mobilenumber);
        country = findViewById(R.id.country);
        Tresend = findViewById(R.id.Tresend);
        Tverify = findViewById(R.id.Tverify);
        skip = findViewById(R.id.skip);
        verify = findViewById(R.id.verify);
        otp = findViewById(R.id.otp);
        next = findViewById(R.id.next);

        Tverify.setVisibility(View.GONE);
        otp.setVisibility(View.GONE);

        session = new Session(Login.this);

        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);

        mAuth = FirebaseAuth.getInstance();

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:" + credential);


                signInWithPhoneAuthCredential(credential);

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // ...
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // ...
                }

                // Show a message and update the UI
                // ...
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:" + verificationId);
                Log.d(TAG, "token:" + token);
                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;

                // ...
            }
        };


        Tresend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendVerificationCode(country.getSelectedCountryCodeWithPlus() + mobilenumber.getText().toString(), mResendToken);
                Toast.makeText(Login.this, "Otp Sent", Toast.LENGTH_SHORT).show();
            }
        });

        Tverify.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override
            public void onOtpCompleted(String otp) {
                progressBar.setVisibility(View.VISIBLE);
                verifyPhoneNumberWithCode(mVerificationId, otp);
            }
        });

        mobilenumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                verify.setVisibility(View.VISIBLE);
                Tresend.setVisibility(View.GONE);
                otp.setVisibility(View.GONE);
                Tverify.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (getCurrentFocus() != null) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    assert inputMethodManager != null;
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }

                if (TextUtils.isEmpty(mobilenumber.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Enter Mobile Number", Toast.LENGTH_LONG).show();
                    return;
                }

                if (mobilenumber.getText().toString().length() != 10) {
                    Toast.makeText(getApplicationContext(), "Enter 10 Digit Mobile Number", Toast.LENGTH_LONG).show();
                    return;
                }

                verify.setVisibility(View.GONE);
                Tresend.setVisibility(View.VISIBLE);
                Tverify.requestFocus();
                Tverify.setFocusableInTouchMode(true);

                progressBar.setVisibility(View.VISIBLE);

                FirebaseDatabase.getInstance().getReference().child("Vendor")
                        .orderByChild("MobileNumber").equalTo(country.getSelectedCountryCodeWithPlus() + mobilenumber.getText().toString())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (!dataSnapshot.exists()) {
                                    String mobno = country.getSelectedCountryCodeWithPlus() + mobilenumber.getText().toString();
                                    startPhoneNumberVerification(mobno);
                                    status = "notregistered";
                                    Tverify.setVisibility(View.VISIBLE);
                                    otp.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.GONE);
                                } else {
                                    String userid = "";
                                    for (DataSnapshot v : dataSnapshot.getChildren()) {
                                        userid = v.child("UserId").getValue().toString();
                                        break;
                                    }
                                    FirebaseDatabase.getInstance().getReference().child("Vendor")
                                            .child(userid)
                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    users = dataSnapshot.getValue(Users.class);
                                                    String mobno = country.getSelectedCountryCodeWithPlus() + mobilenumber.getText().toString();
                                                    startPhoneNumberVerification(mobno);
                                                    status = "registered";
                                                    Tverify.setVisibility(View.VISIBLE);
                                                    otp.setVisibility(View.VISIBLE);
                                                    progressBar.setVisibility(View.GONE);
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

            }
        });

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (getCurrentFocus() != null) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    assert inputMethodManager != null;
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }

                if (TextUtils.isEmpty(mobilenumber.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Enter Mobile Number", Toast.LENGTH_LONG).show();
                    return;
                }

                if (mobilenumber.getText().toString().length() != 10) {
                    Toast.makeText(getApplicationContext(), "Enter 10 Digit Mobile Number", Toast.LENGTH_LONG).show();
                    return;
                }

                verify.setVisibility(View.GONE);
                Tresend.setVisibility(View.VISIBLE);
                Tverify.requestFocus();
                Tverify.setFocusableInTouchMode(true);

                progressBar.setVisibility(View.VISIBLE);

                FirebaseDatabase.getInstance().getReference().child("Vendor")
                        .orderByChild("MobileNumber").equalTo(country.getSelectedCountryCodeWithPlus() + mobilenumber.getText().toString())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (!dataSnapshot.exists()) {
                                    String mobno = country.getSelectedCountryCodeWithPlus() + mobilenumber.getText().toString();
                                    startPhoneNumberVerification(mobno);
                                    status = "notregistered";
                                    Tverify.setVisibility(View.VISIBLE);
                                    otp.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.GONE);
                                } else {
                                    String userid = "";
                                    for (DataSnapshot v : dataSnapshot.getChildren()) {
                                        userid = v.child("UserId").getValue().toString();
                                        break;
                                    }
                                    FirebaseDatabase.getInstance().getReference().child("Vendor")
                                            .child(userid)
                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    users = dataSnapshot.getValue(Users.class);
                                                    String mobno = country.getSelectedCountryCodeWithPlus() + mobilenumber.getText().toString();
                                                    startPhoneNumberVerification(mobno);
                                                    status = "registered";
                                                    Tverify.setVisibility(View.VISIBLE);
                                                    otp.setVisibility(View.VISIBLE);
                                                    progressBar.setVisibility(View.GONE);
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
            }
        });

    }


    @Override
    public void onStart() {
        super.onStart();
        if (mVerificationInProgress && validatePhoneNumber()) {
            startPhoneNumberVerification(country + mobilenumber.getText().toString());
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_VERIFY_IN_PROGRESS, mVerificationInProgress);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mVerificationInProgress = savedInstanceState.getBoolean(KEY_VERIFY_IN_PROGRESS);
    }

    private void startPhoneNumberVerification(String phoneNumber) {
        // [START start_phone_auth]
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
        // [END start_phone_auth]

        mVerificationInProgress = true;
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]
        try {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
            // [END verify_with_code]
            signInWithPhoneAuthCredential(credential);
        } catch (Exception e) {
            Toast toast = Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            progressBar.setVisibility(View.GONE);
        }

    }

    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if (status.equals("notregistered")) {

                                Intent intent = new Intent(Login.this, RegisterDetails.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                session = new Session(Login.this);
                                startActivity(intent);
                                finish();
                            } else {
                                Intent intent = new Intent(Login.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                session = new Session(Login.this);
                                session.setusername(users.UserId);
                                session.setnumber(users.MobileNumber);
                                session.setname(users.Name);
                                session.setemail(users.Email);
                                session.setcategory(users.Category);
                                startActivity(intent);
                                finish();
                            }
                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Tverify.setError("Invalid code.");
                                progressBar.setVisibility(View.GONE);
                            }

                        }
                    }
                });
    }

    private boolean validatePhoneNumber() {
        String phoneNumber = mobilenumber.getText().toString();
        if (TextUtils.isEmpty(phoneNumber)) {
            return false;
        }
        return true;
    }
}