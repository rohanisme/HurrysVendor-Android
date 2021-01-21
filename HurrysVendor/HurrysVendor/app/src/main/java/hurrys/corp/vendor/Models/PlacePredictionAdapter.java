package hurrys.corp.vendor.Models;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.internal.LatLngAdapter;
import com.google.maps.model.GeocodingResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import hurrys.corp.vendor.Activities.MainActivity;
import hurrys.corp.vendor.Configurations.Session;
import hurrys.corp.vendor.Fragments.Location;
import hurrys.corp.vendor.R;


public class PlacePredictionAdapter extends RecyclerView.Adapter<PlacePredictionAdapter.PlacePredictionViewHolder> {

    private final List<AutocompletePrediction> predictions = new ArrayList<>();

//    private OnPlaceClickListener onPlaceClickListener;
    private Gson gson = new GsonBuilder().registerTypeAdapter(LatLng.class, new LatLngAdapter())
            .create();

//    private RequestQueue queue;


    @NonNull
    @Override
    public PlacePredictionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new PlacePredictionViewHolder(
                inflater.inflate(R.layout.search_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final PlacePredictionViewHolder holder, int position) {
        final AutocompletePrediction prediction = predictions.get(position);
        holder.setPrediction(prediction);


        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                   String pid=prediction.getPlaceId();

                Session session=new Session(holder.view);

                MainActivity mainActivity = (MainActivity) holder.view;

                    Fragment fragment = new Location();
                    Bundle bundle = new Bundle();
                    bundle.putString("placeid", pid);
                    fragment.setArguments(bundle);
                    FragmentManager fragmentManager = mainActivity.getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.frame_container, fragment).commit();
            }
        });


    }

    @Override
    public int getItemCount() {
        return predictions.size();
    }

    public void setPredictions(List<AutocompletePrediction> predictions) {
        this.predictions.clear();
        this.predictions.addAll(predictions);
        notifyDataSetChanged();
    }


    public static class PlacePredictionViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {

        public final Context view;
        private final TextView title;
        private final TextView address;
        private final LinearLayout linearLayout;

        public PlacePredictionViewHolder(@NonNull View itemView) {
            super(itemView);
            this.view = itemView.getContext();
            title = itemView.findViewById(R.id.address);
            address = itemView.findViewById(R.id.description);
            linearLayout = itemView.findViewById(R.id.linearLayout);
        }

        public void setPrediction(AutocompletePrediction prediction) {
            title.setText(prediction.getPrimaryText(null));
            address.setText(prediction.getSecondaryText(null));
        }

        @Override
        public void onClick(View v) {
        }

    }

    private void geocodePlaceAndDisplay(final AutocompletePrediction placePrediction) {
        // Construct the request URL
        final String apiKey = "AIzaSyCPhxfpptoIc1yca5U8mXIigIajoERQCdE";
        final String url = "https://maps.googleapis.com/maps/api/geocode/json?place_id=%s&key=%s";
        final String requestURL = String.format(url, placePrediction.getPlaceId(), apiKey);

        // Use the HTTP request URL for Geocoding API to get geographic coordinates for the place
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, requestURL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Inspect the value of "results" and make sure it's not empty
                            JSONArray results = response.getJSONArray("results");
                            if (results.length() == 0) {
//                                Log.w(TAG, "No results from geocoding request.");
                                return;
                            }

                            // Use Gson to convert the response JSON object to a POJO
                            GeocodingResult result = gson.fromJson(
                                    results.getString(0), GeocodingResult.class);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Log.e(TAG, "Request failed");
            }
        });

    }


}
