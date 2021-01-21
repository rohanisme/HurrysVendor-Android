package hurrys.corp.vendor.Fragments;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import hurrys.corp.vendor.Configurations.Session;
import hurrys.corp.vendor.R;

public class Location extends Fragment implements
        GoogleMap.OnCameraIdleListener,
        GoogleMap.OnCameraMoveListener,
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback,
        LocationListener {

    public Location() {
        // Required empty public constructor
    }

    private GoogleMap mMap;
    ImageView pin;
    GoogleApiClient mGoogleApiClient;
    private android.location.Location mLastKnownLocation;


    LocationRequest mLocationRequest;
    int temp=0;

    String city="";

    TextView location,coord;
    Button save;
    Session session;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private PlacesClient placesClient;
    private List<AutocompletePrediction> predictionList;

    private final float DEFAULT_ZOOM = 15;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_location, container, false);

        Toolbar toolbar=(getActivity()).findViewById(R.id.toolbar);
        toolbar.setVisibility(View.GONE);

        LinearLayout bottomnavigation=(getActivity()).findViewById(R.id.bottomnavigation);
        bottomnavigation.setVisibility(View.GONE);

        pin=v.findViewById(R.id.pin);
        session=new Session(getContext());

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        save=v.findViewById(R.id.save);
        location=v.findViewById(R.id.location);
        coord=v.findViewById(R.id.coord);

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        Places.initialize(getContext(), "AIzaSyCPhxfpptoIc1yca5U8mXIigIajoERQCdE");
        placesClient = Places.createClient(getContext());
        final AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();


        final String placeId = getArguments().getString("placeid");
        List<Place.Field> placeFields = Arrays.asList(Place.Field.LAT_LNG);

        FetchPlaceRequest fetchPlaceRequest = FetchPlaceRequest.builder(placeId, placeFields).build();
        placesClient.fetchPlace(fetchPlaceRequest).addOnSuccessListener(new OnSuccessListener<FetchPlaceResponse>() {
            @Override
            public void onSuccess(FetchPlaceResponse fetchPlaceResponse) {
                Place place = fetchPlaceResponse.getPlace();
                Log.i("mytag", "Place found: " + place.getName());
                LatLng latLngOfPlace = place.getLatLng();
                if (latLngOfPlace != null) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngOfPlace, DEFAULT_ZOOM));

                    String address="";
                    String address1="";
                    Geocoder gcd = new Geocoder(getContext(), Locale.getDefault());
                    List<Address> addresses = new ArrayList<>();
                    try {
                        addresses = gcd.getFromLocation(mMap.getCameraPosition().target.latitude,mMap.getCameraPosition().target.longitude, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (addresses.size() > 0) {
                        address=addresses.get(0).getAddressLine(0);
                        city=addresses.get(0).getLocality();

                        location.setText(address);
                        coord.setText(""+latLngOfPlace);

                    }

                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ApiException) {
                    ApiException apiException = (ApiException) e;
                    apiException.printStackTrace();
                    int statusCode = apiException.getStatusCode();
                    Log.i("mytag", "place not found: " + e.getMessage());
                    Log.i("mytag", "status code: " + statusCode);
                }
            }
        });



        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(location.getText().toString().equals("LOCATING.....!"))
                {
                    Toast.makeText(getContext(),"Please Wait",Toast.LENGTH_LONG).show();
                    return;
                }



                session.setloc(coord.getText().toString());
                session.setaddress(location.getText().toString());
                getActivity().onBackPressed();

            }
        });



        return v;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        mMap.setOnCameraIdleListener(this);
        mMap.setOnCameraMoveListener(this);

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //Initialize Google Play Services
//        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (ContextCompat.checkSelfPermission(getContext(),
//                    Manifest.permission.ACCESS_FINE_LOCATION)
//                    == PackageManager.PERMISSION_GRANTED) {
//                //Location Permission already granted
//                buildGoogleApiClient();
//                mMap.setMyLocationEnabled(true);
////
//            } else {
//                //Request Location Permission
//                checkLocationPermission();
//            }
//        }
//        else {
//            buildGoogleApiClient();
//            mMap.setMyLocationEnabled(true);
//        }

//        mMap.addMarker(new MarkerOptions()
//                .position(new LatLng(0, 0))
//                .title("Marker")
//                .draggable(true));
    }

//    protected synchronized void buildGoogleApiClient() {
//        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .addApi(LocationServices.API)
//                .build();
//        mGoogleApiClient.connect();
//    }

//    @Override
//    public void onConnected(Bundle bundle) {
//        mLocationRequest = new LocationRequest();
//        mLocationRequest.setInterval(1000);
//        mLocationRequest.setFastestInterval(1000);
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
//        if (ContextCompat.checkSelfPermission(getContext(),
//                Manifest.permission.ACCESS_FINE_LOCATION)
//                == PackageManager.PERMISSION_GRANTED) {
//            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
//        }
//    }

//    @Override
//    public void onConnectionSuspended(int i) {}
//
//    @Override
//    public void onConnectionFailed(ConnectionResult connectionResult) {}

    @Override
    public void onLocationChanged(android.location.Location location)
    {
//        if(temp==0) {
//            mMap.clear();
//            temp++;
//            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//            mMap.addMarker(new MarkerOptions()
//                    .position(latLng)
//                    .title("Marker"));
//            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
//            mMap.setMapType(mMap.MAP_TYPE_NORMAL);
//        }


    }


    @Override
    public void onCameraMove() {

        mMap.clear();
        pin.setVisibility(View.VISIBLE);

    }


    @Override
    public void onCameraIdle() {

        pin.setVisibility(View.GONE);
        mMap.clear();
        mMap.addMarker(new MarkerOptions()
                .position(mMap.getCameraPosition().target)
                .title("Marker"));


        String address="";
        String address1="";
        Geocoder gcd = new Geocoder(getContext(), Locale.getDefault());
        List<Address> addresses = new ArrayList<>();
        try {
            addresses = gcd.getFromLocation(mMap.getCameraPosition().target.latitude,mMap.getCameraPosition().target.longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses.size() > 0) {
            address=addresses.get(0).getAddressLine(0);
            city=addresses.get(0).getLocality();
            location.setText(address);
            coord.setText(""+mMap.getCameraPosition().target.latitude+","+mMap.getCameraPosition().target.longitude);
        }

    }


    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(getContext())
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION );
            }
        }
    }
}
