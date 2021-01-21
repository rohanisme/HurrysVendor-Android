package hurrys.corp.vendor.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dmallcott.dismissibleimageview.DismissibleImageView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import hurrys.corp.vendor.Configurations.Session;
import hurrys.corp.vendor.Models.Users;
import hurrys.corp.vendor.R;


public class DocumentDetails extends Fragment {

    public DocumentDetails() {
        // Required empty public constructor
    }


    TextView t1,t2,t3,t4,t5,t6,t7,t8;
    private Session session;
    private ProgressBar progressBar;
    String p1,p2,p3,p4,p5,p6,p7,p8;
    DismissibleImageView image;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_document_details, container, false);

        LinearLayout bottomnavigation=(getActivity()).findViewById(R.id.bottomnavigation);
        bottomnavigation.setVisibility(View.GONE);

        t1=v.findViewById(R.id.t1);
        t2=v.findViewById(R.id.t2);
        t3=v.findViewById(R.id.t3);
        t4=v.findViewById(R.id.t4);
        t5=v.findViewById(R.id.t5);
        t6=v.findViewById(R.id.t6);
        t7=v.findViewById(R.id.t7);
        t8=v.findViewById(R.id.t8);
        image=v.findViewById(R.id.image);

        progressBar=v.findViewById(R.id.progressbar);

        session = new Session(getActivity());


        progressBar.setVisibility(View.VISIBLE);


        FirebaseDatabase.getInstance().getReference().child("Vendor")
                .child(session.getusername())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            p1=dataSnapshot.child("Doc1").getValue().toString();
                            p2=dataSnapshot.child("Doc2").getValue().toString();
                            p3=dataSnapshot.child("Doc3").getValue().toString();
                            p4=dataSnapshot.child("Doc4").getValue().toString();
                            p5=dataSnapshot.child("Doc5").getValue().toString();
                            p6=dataSnapshot.child("Doc6").getValue().toString();
                            p7=dataSnapshot.child("Doc7").getValue().toString();
                            p8=dataSnapshot.child("Doc8").getValue().toString();

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


        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new Imagedisplay();
                Bundle bundle=new Bundle();
                bundle.putString("path",p1);
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.frame_container, fragment).commit();
            }
        });

        t2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new Imagedisplay();
                Bundle bundle=new Bundle();
                bundle.putString("path",p2);
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.frame_container, fragment).commit();
            }
        });
        t3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new Imagedisplay();
                Bundle bundle=new Bundle();
                bundle.putString("path",p3);
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.frame_container, fragment).commit();
            }
        });

        t4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new Imagedisplay();
                Bundle bundle=new Bundle();
                bundle.putString("path",p4);
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.frame_container, fragment).commit();
            }
        });

        t5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new Imagedisplay();
                Bundle bundle=new Bundle();
                bundle.putString("path",p5);
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.frame_container, fragment).commit();
            }
        });

        t6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new Imagedisplay();
                Bundle bundle=new Bundle();
                bundle.putString("path",p6);
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.frame_container, fragment).commit();
            }
        });

        t7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new Imagedisplay();
                Bundle bundle=new Bundle();
                bundle.putString("path",p7);
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.frame_container, fragment).commit();
            }
        });

        t8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new Imagedisplay();
                Bundle bundle=new Bundle();
                bundle.putString("path",p8);
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.frame_container, fragment).commit();
            }
        });


        return v;
    }
}
