package hurrys.corp.vendor.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import hurrys.corp.vendor.Configurations.Session;
import hurrys.corp.vendor.Models.Users;
import hurrys.corp.vendor.R;


public class PersonalDetails extends Fragment {

    public PersonalDetails() {
        // Required empty public constructor
    }

    private ProgressBar progressBar;
    private TextView dob;

    TextView name,email,anumber,address,postcode;
    TextView gender;
    private Session session;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_personal_details, container, false);

        LinearLayout bottomnavigation=(getActivity()).findViewById(R.id.bottomnavigation);
        bottomnavigation.setVisibility(View.GONE);



        name=v.findViewById(R.id.name);
        gender=v.findViewById(R.id.gender);
        email=v.findViewById(R.id.email);
        anumber=v.findViewById(R.id.anumber);
        address=v.findViewById(R.id.address);
        postcode=v.findViewById(R.id.postcode);
        dob=v.findViewById(R.id.dob);
        progressBar=v.findViewById(R.id.progressbar);

        session = new Session(getActivity());

        progressBar.setVisibility(View.VISIBLE);

        FirebaseDatabase.getInstance().getReference().child("Vendor")
                .child(session.getusername())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            Users users=dataSnapshot.getValue(Users.class);
                            name.setText(users.Name);
                            dob.setText(users.Age);
                            gender.setText(users.Gender);
                            email.setText(users.Email);
                            anumber.setText(users.AlternateNumber);
                            address.setText(users.Address);
                            postcode.setText(users.PostCode);

                            progressBar.setVisibility(View.GONE);
                        }
                        else{
                            progressBar.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


        return v;

    }
}
