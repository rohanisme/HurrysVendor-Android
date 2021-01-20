package hurrys.corp.vendor.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import hurrys.corp.vendor.Activities.RegisterDetails;
import hurrys.corp.vendor.Activities.WelcomePage;
import hurrys.corp.vendor.Configurations.Session;
import hurrys.corp.vendor.R;


public class ApprovalWelcome extends Fragment {


    public ApprovalWelcome() {
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
        View v=inflater.inflate(R.layout.fragment_approval_welcome, container, false);
        Button start=v.findViewById(R.id.start);

        Session session=new Session(getActivity());

        Button go=v.findViewById(R.id.go);



        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(getActivity()!=null) {
                    Fragment fragment = new Dashboard();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.frame_container, fragment).commitAllowingStateLoss();
                }

            }
        });

        return v;
    }
}
