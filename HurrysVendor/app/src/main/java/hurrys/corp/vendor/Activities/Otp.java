
package hurrys.corp.vendor.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;

import java.util.concurrent.TimeUnit;

import hurrys.corp.vendor.Configurations.Session;
import hurrys.corp.vendor.Models.Users;
import hurrys.corp.vendor.R;

public class Otp extends AppCompatActivity {

    TextView verify,Tresend,textView3,timer;
    String Number="",CountryCode="",status="";
    Users users;
    OtpView Tverify;
    Session session;
    ImageView thankyou;
    LinearLayout l1;

    FirebaseUser user;

    private static final String TAG = "PhoneAuthActivity";
    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";
    private static final int STATE_INITIALIZED = 1;
    private static final int STATE_CODE_SENT = 2;
    private static final int STATE_VERIFY_FAILED = 3;
    private static final int STATE_VERIFY_SUCCESS = 4;
    private static final int STATE_SIGNIN_FAILED = 5;
    private static final int STATE_SIGNIN_SUCCESS = 6;
    private FirebaseAuth mAuth;
    private boolean mVerificationInProgress = false;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        verify=findViewById(R.id.verify);
        Tverify=findViewById(R.id.Tverify);
        Tresend=findViewById(R.id.Tresend);
        textView3=findViewById(R.id.textView3);
        timer=findViewById(R.id.timer);
        l1=findViewById(R.id.l1);
        thankyou=findViewById(R.id.thankyou);

        ImageView back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        session = new Session(this);
        Bundle bundle = getIntent().getExtras();
        Number=bundle.getString("number");
        CountryCode=bundle.getString("code");

        Tverify.requestFocus();
        Tverify.setFocusableInTouchMode(true);
        Tresend.setVisibility(View.GONE);


        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                timer.setText("WAIT FOR " + millisUntilFinished / 1000 +" SECONDS");
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                timer.setText("RESEND OTP");
                timer.setTextColor(R.color.colorPrimaryDark);
                Tresend.setVisibility(View.VISIBLE);
            }

        }.start();

        Tresend.setText(Html.fromHtml("<p>Didn't receive OTP? <span style='color:#87D069'>RESEND</span></p>"));

        textView3.setText("Please Enter the code we just sent to\n("+CountryCode+")"+Number);

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


         if(!status.equals("notregistered")&&!status.equals("registered")) {
                    FirebaseDatabase.getInstance().getReference().child("Vendor")
                            .orderByChild("MobileNumber").equalTo(CountryCode+Number)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (!dataSnapshot.exists()) {
                                        startPhoneNumberVerification(CountryCode+Number);
                                        status = "notregistered";
                                        return;
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
                                                        startPhoneNumberVerification(CountryCode+Number);
                                                        status = "registered";
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

        Tresend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendVerificationCode(CountryCode+Number, mResendToken);
                Toast.makeText(Otp.this,"Otp Sent",Toast.LENGTH_SHORT).show();
            }
        });

        Tverify.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override
            public void onOtpCompleted(String otp) {
                View view = Otp.this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                    verifyPhoneNumberWithCode(mVerificationId, otp);
            }
        });



        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user!=null) {
                    if(status.equals("notregistered")) {
                        Intent intent = new Intent(Otp.this, WelcomePage.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        session = new Session(Otp.this);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        Intent intent = new Intent(Otp.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        session = new Session(Otp.this);
                        session.setusername(users.UserId);
                        session.setnumber(users.MobileNumber);
                        session.setname(users.Name);
                        session.setstorename(users.StoreName);
                        session.setemail(users.Email);
                        session.setcategory(users.Category);
                        session.setaddress(users.ShopAddress);
                        session.seta("");
                        session.setb("");
                        session.setc("");
                        session.setd("");
                        session.sete("");
                        session.setf("");
                        session.setg("");
                        session.seth("");
                        session.seti("");
                        session.setj("");
                        startActivity(intent);
                        finish();
                    }
                }


            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        // [START_EXCLUDE]
        if (mVerificationInProgress && validatePhoneNumber()) {
            startPhoneNumberVerification(CountryCode+Number);
        }
        // [END_EXCLUDE]
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
        }
        catch (Exception e){
            Toast toast = Toast.makeText(this, "Verification Code is wrong", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
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
                            user = task.getResult().getUser();
                            Tverify.setVisibility(View.GONE);
                            timer.setVisibility(View.INVISIBLE);
                            Tresend.setVisibility(View.INVISIBLE);
                            textView3.setVisibility(View.GONE);
                            verify.setText("Continue");
                            thankyou.setVisibility(View.VISIBLE);
                        }  else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Tverify.setError("Invalid code.");
                            }
                        }
                    }
                });
    }
    private boolean validatePhoneNumber() {
        String phoneNumber =Number;
        if (TextUtils.isEmpty(phoneNumber)) {
            return false;
        }
        return true;
    }
}
