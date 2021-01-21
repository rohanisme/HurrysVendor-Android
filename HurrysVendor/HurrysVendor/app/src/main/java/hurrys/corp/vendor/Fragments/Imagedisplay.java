package hurrys.corp.vendor.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import hurrys.corp.vendor.R;


public class Imagedisplay extends Fragment {


    public Imagedisplay() {
        // Required empty public constructor
    }

   String path="";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_imagedisplay, container, false);

        LinearLayout bottomnavigation=(getActivity()).findViewById(R.id.bottomnavigation);
        bottomnavigation.setVisibility(View.GONE);

        path=getArguments().getString("path");
        ImageView image=v.findViewById(R.id.image);

        Glide.with(getActivity())
                .load(path)
                .into(image);

        return v;
    }
}
