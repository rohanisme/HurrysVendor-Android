package hurrys.corp.vendor.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;
import com.mukesh.OtpView;

import hurrys.corp.vendor.Configurations.Session;
import hurrys.corp.vendor.Models.Users;
import hurrys.corp.vendor.R;

public class Welcome extends AppCompatActivity {

    private Button Bverify;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Bverify=findViewById(R.id.verify);


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        //On Click Listener
        Bverify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Welcome.this, Login.class));

               }

        });


    }


    @Override
    public void onStart() {
        super.onStart();
    }

}