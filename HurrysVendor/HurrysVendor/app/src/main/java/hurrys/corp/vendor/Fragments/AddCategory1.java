package hurrys.corp.vendor.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

import hurrys.corp.vendor.Configurations.Session;
import hurrys.corp.vendor.Models.Category.Category;
import hurrys.corp.vendor.Models.Category.Category1;
import hurrys.corp.vendor.R;


public class AddCategory1 extends Fragment {

    RecyclerView recyclerview;
    ImageView submit;
    EditText category1;
    Category1 category;
    String a="";


    public AddCategory1() {
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
        View v=inflater.inflate(R.layout.fragment_add_category1, container, false);


        LinearLayout bottomnavigation=(getActivity()).findViewById(R.id.bottomnavigation);
        bottomnavigation.setVisibility(View.GONE);

        ImageView back = v.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        submit=v.findViewById(R.id.submit);
        category1=v.findViewById(R.id.category);
        recyclerview=v.findViewById(R.id.recyclerview);


        Session session=new Session(getContext());



                    FirebaseDatabase.getInstance().getReference().child("Vendor")
                            .child(session.getusername())
                            .child("ItemCategory")
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists()){
                                        String temp=dataSnapshot.getValue().toString();
                                        a=temp;

                                        ArrayList<String> category1 = new ArrayList<String>(Arrays.asList(temp.split(",")));

                                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                                        recyclerview.setLayoutManager(mLayoutManager);
                                        category = new Category1(category1);
                                        recyclerview.setAdapter(category);

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(TextUtils.isEmpty(category1.getText().toString())){
                    category1.setError("Enter Category");
                    category1.requestFocus();
                    return;
                }

                a+=category1.getText().toString()+",";

                FirebaseDatabase.getInstance().getReference().child("Vendor")
                        .child(session.getusername())
                        .child("ItemCategory")
                        .setValue(a);

                category1.setText("");

            }
        });


        return v;
    }
}
