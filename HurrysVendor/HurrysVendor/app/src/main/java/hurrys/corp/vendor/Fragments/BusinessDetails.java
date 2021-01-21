package hurrys.corp.vendor.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
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
import android.widget.ViewAnimator;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.internal.LatLngAdapter;

import java.util.ArrayList;
import java.util.List;

import hurrys.corp.vendor.Configurations.Session;
import hurrys.corp.vendor.Models.PlacePredictionAdapter;
import hurrys.corp.vendor.Models.Users;
import hurrys.corp.vendor.R;


public class BusinessDetails extends Fragment {

    private TextView bname,dname,vatcode,registeredaddress,shopaddress,bankaccount,bnumber,bsortcode;
    private TextView businessdescription;
    private TextView otime,ctime,commision;
    private TextView category;
    private Session session;
    private ProgressBar progressBar;

    private BottomSheetDialog bottomSheetDialog;

    ArrayList<String> category1=new ArrayList<String>();

    public BusinessDetails() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_business_details, container, false);

        LinearLayout bottomnavigation=(getActivity()).findViewById(R.id.bottomnavigation);
        bottomnavigation.setVisibility(View.GONE);

        bname=v.findViewById(R.id.bname);
        dname=v.findViewById(R.id.dname);
        vatcode=v.findViewById(R.id.vatcode);
        registeredaddress=v.findViewById(R.id.registeredaddress);
        shopaddress=v.findViewById(R.id.shopaddress);
        bankaccount=v.findViewById(R.id.bankaccount);
        bnumber=v.findViewById(R.id.bnumber);
        bsortcode=v.findViewById(R.id.bsortcode);
        businessdescription=v.findViewById(R.id.businessdescription);
        otime=v.findViewById(R.id.otime);
        ctime=v.findViewById(R.id.ctime);
        commision=v.findViewById(R.id.commision);
        category=v.findViewById(R.id.category);
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

                            bname.setText(users.BusinessName);
                            category.setText(users.Category);
                            dname.setText(users.BusinessName);
                            vatcode.setText(users.VATCode);
                            registeredaddress.setText(users.BusinessAddress);
                            shopaddress.setText(users.ShopAddress);
                            bankaccount.setText(users.AccountName);
                            bnumber.setText(users.AccountNumber);
                            bsortcode.setText(users.SortCode);
                            businessdescription.setText(users.StoreDescription);
                            if(TextUtils.isEmpty(users.Commission)){
                                commision.setText("Wil be entered by admin");
                            }
                            else
                                commision.setText(users.Commission);
                            if(TextUtils.isEmpty(users.StoreOpenTime))
                                otime.setText("HH:MM");
                            else
                                otime.setText(users.StoreOpenTime);
                            if(TextUtils.isEmpty(users.StoreCloseTime))
                                ctime.setText("HH:MM");
                            else
                                ctime.setText(users.StoreCloseTime);

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
