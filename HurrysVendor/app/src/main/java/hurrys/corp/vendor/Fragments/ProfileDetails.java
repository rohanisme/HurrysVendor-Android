package hurrys.corp.vendor.Fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

import java.util.ArrayList;

import hurrys.corp.vendor.Configurations.Session;
import hurrys.corp.vendor.Models.Users;
import hurrys.corp.vendor.R;

public class ProfileDetails extends Fragment {

    public ProfileDetails() {
        // Required empty public constructor
    }


    private TextView t1,t2,t3;
    private View s1,s2,s3;
    private Session session;


    private ImageView doc1,doc2,doc3,doc4,doc5,doc6,doc7,doc8,back1,back2,back3;
    private LinearLayout stage1,stage2,stage3,z1,z2,z3,substage1,substage2,substage3;
    private ProgressBar progressBar;
    private TextView dob,otime,ctime,minimum;
    private TextView name,email,anumber,address,postcode,bankaccount,bnumber,bcnumber,bsortcode,bankname,businessname,baddress,bdescription,deliverytime,dname,vatcode,paddress;
    private TextView commision;
    private TextView gender,category;
    private CardView b1,b2,b3;



    private CountryCodePicker country;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_profile_details, container, false);

        LinearLayout bottomnavigation=(getActivity()).findViewById(R.id.bottomnavigation);
        bottomnavigation.setVisibility(View.GONE);

        ImageView back=v.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getActivity()!=null)
                    getActivity().onBackPressed();
            }
        });


        z1=v.findViewById(R.id.z1);
        z2=v.findViewById(R.id.z2);
        z3=v.findViewById(R.id.z3);
        t1=v.findViewById(R.id.t1);
        t2=v.findViewById(R.id.t2);
        t3=v.findViewById(R.id.t3);
        s1=v.findViewById(R.id.s1);
        s2=v.findViewById(R.id.s2);
        s3=v.findViewById(R.id.s3);
        stage1=v.findViewById(R.id.stage1);
        stage2=v.findViewById(R.id.stage2);
        stage3=v.findViewById(R.id.stage3);

        doc1=v.findViewById(R.id.doc1);
        doc2=v.findViewById(R.id.doc2);
        doc3=v.findViewById(R.id.doc3);
        doc4=v.findViewById(R.id.doc4);
        doc5=v.findViewById(R.id.doc5);
        doc6=v.findViewById(R.id.doc6);
        doc7=v.findViewById(R.id.doc7);
        doc8=v.findViewById(R.id.doc8);
        country=v.findViewById(R.id.country);

        name=v.findViewById(R.id.name);
        dob=v.findViewById(R.id.dob);
        email=v.findViewById(R.id.email);
        anumber=v.findViewById(R.id.anumber);
        address=v.findViewById(R.id.address);
        postcode=v.findViewById(R.id.postcode);
        bankaccount=v.findViewById(R.id.bankaccount);
        minimum=v.findViewById(R.id.minimum);
        bnumber=v.findViewById(R.id.bnumber);
        bcnumber=v.findViewById(R.id.bcnumber);
        bsortcode=v.findViewById(R.id.bsortcode);
        gender=v.findViewById(R.id.gender);
        bankname=v.findViewById(R.id.bankname);
        businessname=v.findViewById(R.id.businessname);
        dname=v.findViewById(R.id.dname);
        vatcode=v.findViewById(R.id.vatcode);
        paddress=v.findViewById(R.id.paddress);
        category=v.findViewById(R.id.category);
        baddress=v.findViewById(R.id.baddress);
        bdescription=v.findViewById(R.id.bdescription);
        otime=v.findViewById(R.id.otime);
        ctime=v.findViewById(R.id.ctime);
        commision=v.findViewById(R.id.commision);
        deliverytime=v.findViewById(R.id.deliverytime);
        substage1=v.findViewById(R.id.substage1);
        substage2=v.findViewById(R.id.substage2);
        substage3=v.findViewById(R.id.substage3);
        b1=v.findViewById(R.id.b1);
        b2=v.findViewById(R.id.b2);
        b3=v.findViewById(R.id.b3);
        back1=v.findViewById(R.id.back1);
        back2=v.findViewById(R.id.back2);
        back3=v.findViewById(R.id.back3);
        progressBar=v.findViewById(R.id.progressbar);

        session = new Session(getActivity());

        name.setText(session.getname());
        stage1.setVisibility(View.VISIBLE);
        stage2.setVisibility(View.GONE);
        stage3.setVisibility(View.GONE);

        t1.setTextColor(Color.parseColor("#00B246"));
        t2.setTextColor(Color.parseColor("#808080"));
        t3.setTextColor(Color.parseColor("#808080"));
        s1.setBackgroundColor(Color.parseColor("#00B246"));
        s2.setBackgroundColor(Color.parseColor("#eaeaea"));
        s3.setBackgroundColor(Color.parseColor("#eaeaea"));


        z1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stage1.setVisibility(View.VISIBLE);
                stage2.setVisibility(View.GONE);
                stage3.setVisibility(View.GONE);
                substage1.setVisibility(View.GONE);
                substage2.setVisibility(View.GONE);
                substage3.setVisibility(View.GONE);

                t1.setTextColor(Color.parseColor("#00B246"));
                t2.setTextColor(Color.parseColor("#808080"));
                t3.setTextColor(Color.parseColor("#808080"));
                s1.setBackgroundColor(Color.parseColor("#00B246"));
                s2.setBackgroundColor(Color.parseColor("#eaeaea"));
                s3.setBackgroundColor(Color.parseColor("#eaeaea"));


            }
        });

        z2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stage1.setVisibility(View.GONE);
                stage2.setVisibility(View.VISIBLE);
                stage3.setVisibility(View.GONE);
                substage1.setVisibility(View.GONE);
                substage2.setVisibility(View.GONE);
                substage3.setVisibility(View.GONE);

                t2.setTextColor(Color.parseColor("#00B246"));
                t1.setTextColor(Color.parseColor("#808080"));
                t3.setTextColor(Color.parseColor("#808080"));
                s2.setBackgroundColor(Color.parseColor("#00B246"));
                s1.setBackgroundColor(Color.parseColor("#eaeaea"));
                s3.setBackgroundColor(Color.parseColor("#eaeaea"));
            }
        });

        z3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stage1.setVisibility(View.GONE);
                stage2.setVisibility(View.GONE);
                stage3.setVisibility(View.VISIBLE);
                substage1.setVisibility(View.GONE);
                substage2.setVisibility(View.GONE);
                substage3.setVisibility(View.GONE);

                t3.setTextColor(Color.parseColor("#00B246"));
                t2.setTextColor(Color.parseColor("#808080"));
                t1.setTextColor(Color.parseColor("#808080"));
                s3.setBackgroundColor(Color.parseColor("#00B246"));
                s2.setBackgroundColor(Color.parseColor("#eaeaea"));
                s1.setBackgroundColor(Color.parseColor("#eaeaea"));
            }
        });


        back1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stage1.setVisibility(View.GONE);
                stage2.setVisibility(View.VISIBLE);
                stage3.setVisibility(View.GONE);
                substage1.setVisibility(View.GONE);
                substage2.setVisibility(View.GONE);
                substage3.setVisibility(View.GONE);
            }
        });

        back2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stage1.setVisibility(View.GONE);
                stage2.setVisibility(View.VISIBLE);
                stage3.setVisibility(View.GONE);
                substage1.setVisibility(View.GONE);
                substage2.setVisibility(View.GONE);
                substage3.setVisibility(View.GONE);
            }
        });

        back3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stage1.setVisibility(View.GONE);
                stage2.setVisibility(View.VISIBLE);
                stage3.setVisibility(View.GONE);
                substage1.setVisibility(View.GONE);
                substage2.setVisibility(View.GONE);
                substage3.setVisibility(View.GONE);
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stage1.setVisibility(View.GONE);
                stage2.setVisibility(View.GONE);
                stage3.setVisibility(View.GONE);
                substage1.setVisibility(View.VISIBLE);
                substage2.setVisibility(View.GONE);
                substage3.setVisibility(View.GONE);
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stage1.setVisibility(View.GONE);
                stage2.setVisibility(View.GONE);
                stage3.setVisibility(View.GONE);
                substage1.setVisibility(View.GONE);
                substage2.setVisibility(View.VISIBLE);
                substage3.setVisibility(View.GONE);
            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stage1.setVisibility(View.GONE);
                stage2.setVisibility(View.GONE);
                stage3.setVisibility(View.GONE);
                substage1.setVisibility(View.GONE);
                substage2.setVisibility(View.GONE);
                substage3.setVisibility(View.VISIBLE);
            }
        });

        FirebaseDatabase.getInstance().getReference().child("Vendor")
                .child(session.getusername())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            Users users=dataSnapshot.getValue(Users.class);

                            if(dataSnapshot.child("MinimumOrder").exists())
                                minimum.setText("\u00a3"+dataSnapshot.child("MinimumOrder").getValue().toString());

                            assert users != null;
                            name.setText(users.Name);
                            dob.setText(users.Age);
                            gender.setText(users.Gender);
                            category.setText(users.Category);

                            email.setText(users.Email);
//                            anumber.setText(users.AlternateNumber);

                            int len=users.AlternateNumber.length();
                            if(len==0) {
                                anumber.setText(users.AlternateNumber);
                            }
                            else if(len==13){
                                country.setCountryForPhoneCode(Integer.parseInt(users.AlternateNumber.substring(1,3)));
                                anumber.setText(users.AlternateNumber.substring(3,13));
                            }
                            else if(len==12){
                                country.setCountryForPhoneCode(Integer.parseInt(users.AlternateNumber.substring(1,2)));
                                anumber.setText(users.AlternateNumber.substring(2,12));
                            }



                            address.setText(users.Address);
                            postcode.setText(users.PostCode);

                            bankaccount.setText(users.AccountName);
                            bnumber.setText(users.AccountNumber);
                            bcnumber.setText(users.AccountNumber);
                            bsortcode.setText(users.SortCode);
                            bankname.setText(users.BankName);
                            businessname.setText(users.BusinessName);
                            dname.setText(users.StoreName);
                            baddress.setText(users.BusinessAddress);
                            paddress.setText(users.ShopAddress);
                            vatcode.setText(users.VATCode);
                            bdescription.setText(users.StoreDescription);
                            otime.setText(users.StoreOpenTime);
                            ctime.setText(users.StoreCloseTime);
                            commision.setText(users.Commission);
                            deliverytime.setText(users.DeliveryTime);

                            if(!TextUtils.isEmpty(users.Doc1))
                                if(getContext()!=null) {
                                    Glide.with(getContext())
                                            .load(users.Doc1)
                                            .into(doc1);
                                }
                            if(!TextUtils.isEmpty(users.Doc2))
                                if(getContext()!=null) {
                                    Glide.with(getContext())
                                            .load(users.Doc2)
                                            .into(doc2);
                                }
                            if(!TextUtils.isEmpty(users.Doc3))
                                if(getContext()!=null) {
                                    Glide.with(getContext())
                                            .load(users.Doc3)
                                            .into(doc3);
                                }
                            if(!TextUtils.isEmpty(users.Doc4))
                                if(getContext()!=null) {
                                    Glide.with(getContext())
                                            .load(users.Doc4)
                                            .into(doc4);
                                }
                            if(!TextUtils.isEmpty(users.Doc5))
                                if(getContext()!=null) {
                                    Glide.with(getContext())
                                            .load(users.Doc5)
                                            .into(doc5);
                                }
                            if(!TextUtils.isEmpty(users.Doc6))
                                if(getContext()!=null) {
                                    Glide.with(getContext())
                                            .load(users.Doc6)
                                            .into(doc6);
                                }
                            if(!TextUtils.isEmpty(users.Doc7))
                                if(getContext()!=null) {
                                    Glide.with(getContext())
                                            .load(users.Doc7)
                                            .into(doc7);
                                }
                            if(!TextUtils.isEmpty(users.Doc8))
                                if(getContext()!=null) {
                                    Glide.with(getContext())
                                            .load(users.Doc8)
                                            .into(doc8);
                                }

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


//        FirebaseDatabase.getInstance().getReference().child("Vendor")
//                .child(session.getusername())
//                .addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        if(dataSnapshot.exists()){
//                            Users users=dataSnapshot.getValue(Users.class);
//
//
//                            assert users != null;
//                            name.setText(users.Name);
//                            dob.setText(users.Age);
//                            gender.setText(users.Gender);
//                            category.setText(users.Category);
////                            if(gender1.indexOf(users.Gender)>-1)
////                                gender.setSelection(gender1.indexOf(users.Gender));
////                            else
////                                gender.setSelection(0);
//                            email.setText(users.Email);
////                            anumber.setText(users.AlternateNumber);
//
//                            int len=users.AlternateNumber.length();
//                            if(len==0) {
//                                anumber.setText(users.AlternateNumber);
//                            }
//                            else if(len==13){
//                                country.setCountryForPhoneCode(Integer.parseInt(users.AlternateNumber.substring(1,3)));
//                                anumber.setText(users.AlternateNumber.substring(3,13));
//                            }
//                            else if(len==12){
//                                country.setCountryForPhoneCode(Integer.parseInt(users.AlternateNumber.substring(1,2)));
//                                anumber.setText(users.AlternateNumber.substring(2,12));
//                            }
//
////                            if(category1.indexOf(users.Category)>-1)
////                                category.setSelection(category1.indexOf(users.Category));
////                            else
////                                category.setSelection(0);
//
//
//
//                            address.setText(users.Address);
//                            postcode.setText(users.PostCode);
//
//                            bankaccount.setText(users.AccountName);
//                            bnumber.setText(users.AccountNumber);
//                            bcnumber.setText(users.AccountNumber);
//                            bsortcode.setText(users.SortCode);
//                            bankname.setText(users.BankName);
//                            businessname.setText(users.BusinessName);
//                            baddress.setText(users.BusinessAddress);
//                            bdescription.setText(users.StoreDescription);
//                            otime.setText(users.StoreOpenTime);
//                            ctime.setText(users.StoreCloseTime);
//                            commision.setText(users.Commission);
//                            deliverytime.setText(users.DeliveryTime);
//
//
//                            if(!TextUtils.isEmpty(users.Doc1))
//                                if(getContext()!=null)
//                                    Glide.with(getContext())
//                                            .load(users.Doc1)
//                                            .into(doc1);
//                            if(!TextUtils.isEmpty(users.Doc2))
//                                if(getContext()!=null)
//                                    Glide.with(getContext())
//                                            .load(users.Doc2)
//                                            .into(doc2);
//                            if(!TextUtils.isEmpty(users.Doc3))
//                                if(getContext()!=null)
//                                    Glide.with(getContext())
//                                            .load(users.Doc3)
//                                            .into(doc3);
//                            if(!TextUtils.isEmpty(users.Doc4))
//                                if(getContext()!=null)
//                                    Glide.with(getContext())
//                                            .load(users.Doc4)
//                                            .into(doc4);
//                            if(!TextUtils.isEmpty(users.Doc5))
//                                if(getContext()!=null)
//                                    Glide.with(getContext())
//                                            .load(users.Doc5)
//                                            .into(doc5);
//                            if(!TextUtils.isEmpty(users.Doc6))
//                                if(getContext()!=null)
//                                    Glide.with(getContext())
//                                            .load(users.Doc6)
//                                            .into(doc6);
//                            if(!TextUtils.isEmpty(users.Doc7))
//                                if(getContext()!=null)
//                                    Glide.with(getContext())
//                                            .load(users.Doc7)
//                                            .into(doc7);
//                            if(!TextUtils.isEmpty(users.Doc8))
//                                if(getContext()!=null)
//                                    Glide.with(getContext())
//                                            .load(users.Doc8)
//                                            .into(doc8);
//
//
//
//                        }
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });



        return v;
    }
}
