package hurrys.corp.vendor.Fragments;

import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewAnimator;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.internal.LatLngAdapter;

import java.util.List;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import hurrys.corp.vendor.Configurations.Session;
import hurrys.corp.vendor.Models.PlacePredictionAdapter;
import hurrys.corp.vendor.R;


public class LocationSearch extends Fragment {

    EditText txtSearch;
    RecyclerView mRecyclerView;
    ImageView clear;
    private Handler handler = new Handler();
    private PlacePredictionAdapter adapter = new PlacePredictionAdapter();
    private Gson gson = new GsonBuilder().registerTypeAdapter(LatLng.class, new LatLngAdapter())
            .create();

    private RequestQueue queue;
    private PlacesClient placesClient;
    private AutocompleteSessionToken sessionToken;

    private ViewAnimator viewAnimator;

    LinearLayout current;

    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mref;
    Session sessions;


    public LocationSearch() {
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
        View v=inflater.inflate(R.layout.fragment_locationsearch, container, false);
        LinearLayout bottomnavigation=(getActivity()).findViewById(R.id.bottomnavigation);
        bottomnavigation.setVisibility(View.GONE);

//        buildGoogleApiClient();


        ImageView back=v.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
//
//        v.setFocusableInTouchMode(true);
//        v.requestFocus();
//        v.setOnKeyListener( new View.OnKeyListener()
//        {
//            @Override
//            public boolean onKey( View v, int keyCode, KeyEvent event )
//            {
//                if( keyCode == KeyEvent.KEYCODE_BACK )
//                {
//                    getActivity().onBackPressed();
//                    return false;
//                }
//                return false;
//            }
//        });



        if (!Places.isInitialized()) {
            Places.initialize(getContext(), getString(R.string.api_key));
        }

        txtSearch=v.findViewById(R.id.txtSearch);
        clear=v.findViewById(R.id.clear);
        mRecyclerView=v.findViewById(R.id.recyclerView);
//        clear=v.findViewById(R.id.clear);
        placesClient = Places.createClient(getContext());

        viewAnimator = v.findViewById(R.id.view_animator);

        current = v.findViewById(R.id.current);

        current.setVisibility(View.VISIBLE);

        viewAnimator.setVisibility(View.GONE);
        clear.setVisibility(View.GONE);

        sessions= new Session(getActivity());

        current.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LocationManager locationManager = (LocationManager)
                        getActivity().getSystemService(Context.LOCATION_SERVICE);
                if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                    SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("GPS!")
                            .setContentText("Please Enable GPS to continue!")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                }
                            });
                    sweetAlertDialog.show();
                    sweetAlertDialog.setCancelable(false);
                    return ;
                }

                Fragment fragment = new LocationPicker();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                Bundle bundle=new Bundle();
                bundle.putString("type","location");
                fragment.setArguments(bundle);
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_container, fragment).commit();

            }
        });


        placesClient = com.google.android.libraries.places.api.Places.createClient(getContext());
        queue = Volley.newRequestQueue(getContext());


        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), layoutManager.getOrientation()));


//        adapter.setPlaceClickListener();
//        LinearLayoutManager mLinearLayoutManager=new LinearLayoutManager(getContext());
//        mRecyclerView.setLayoutManager(mLinearLayoutManager);
//        mRecyclerView.setAdapter(mAutoCompleteAdapter);

        txtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {
                sessionToken = AutocompleteSessionToken.newInstance();
            }

            @Override
            public void onTextChanged(final CharSequence s, int i, int i1, int i2) {

                current.setVisibility(View.VISIBLE);

                viewAnimator.setVisibility(View.VISIBLE);
                clear.setVisibility(View.VISIBLE);


                // Cancel any previous place prediction requests
                handler.removeCallbacksAndMessages(null);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getPlacePredictions(s.toString());
                    }
                }, 300);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtSearch.setText("");
                current.setVisibility(View.VISIBLE);
                viewAnimator.setVisibility(View.GONE);
                clear.setVisibility(View.GONE);

            }
        });

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mref = mFirebaseDatabase.getReference().child("Users").child(sessions.getusername()).child("Address");

        return v;
    }

    private void getPlacePredictions(String query) {


//        final LocationBias bias = RectangularBounds.newInstance(
//            new LatLng(13.590194, 77.535981), // NE lat, lng
//            new LatLng(13.622520, 77.503923) // SW lat, lng
//        );

        // Create a new programmatic Place Autocomplete request in Places SDK for Android
        final FindAutocompletePredictionsRequest newRequest = FindAutocompletePredictionsRequest
                .builder()
                .setSessionToken(sessionToken)
//            .setLocationBias(bias)
//                .setTypeFilter(TypeFilter.ESTABLISHMENT)
                .setCountry("UK")
                .setQuery(query)
                .build();

        // Perform autocomplete predictions request
        placesClient.findAutocompletePredictions(newRequest).addOnSuccessListener(new OnSuccessListener<FindAutocompletePredictionsResponse>() {
            @Override
            public void onSuccess(FindAutocompletePredictionsResponse response) {
                List<AutocompletePrediction> predictions = response.getAutocompletePredictions();
                adapter.setPredictions(predictions);

                viewAnimator.setDisplayedChild(predictions.isEmpty() ? 0 : 1);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                if (exception instanceof ApiException) {
                    ApiException apiException = (ApiException) exception;
                    Log.e("TAG", "Place not found: " + apiException.getStatusCode());
                }
            }
        });



    }

    @Override
    public void onResume(){
        super.onResume();

        txtSearch.setText("");

        current.setVisibility(View.VISIBLE);

        viewAnimator.setVisibility(View.GONE);
        clear.setVisibility(View.GONE);

    }


}
