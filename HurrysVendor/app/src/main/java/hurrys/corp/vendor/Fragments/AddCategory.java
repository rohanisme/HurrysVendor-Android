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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import hurrys.corp.vendor.Configurations.Session;
import hurrys.corp.vendor.Models.Category.Category;
import hurrys.corp.vendor.R;

public class AddCategory extends Fragment {


    RecyclerView recyclerview;
    ImageView submit;
    EditText category1;
    Category category;
    Spinner subcategory;
    String a="";

    private ArrayList<String> subcategoryname= new ArrayList<String>();
    private ArrayList<String> subcategorypushid= new ArrayList<String>();
    private ArrayList<String> subcategoryimage= new ArrayList<String>();


    public AddCategory() {
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
        View v=inflater.inflate(R.layout.fragment_add_category, container, false);

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
        subcategory=v.findViewById(R.id.subcategory);
        recyclerview=v.findViewById(R.id.recyclerview);


        Session session=new Session(getContext());

        subcategoryimage.clear();
        subcategoryname.clear();
        subcategorypushid.clear();

        subcategorypushid.add("Select");
        subcategoryname.add("Select");
        subcategoryimage.add("Select");


        FirebaseDatabase.getInstance().getReference().child("Vendor").child(session.getusername())
                .child("SubCategory")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            for(DataSnapshot v:dataSnapshot.getChildren()){
                                if(v.exists()){
                                    subcategoryname.add(v.child("Name").getValue().toString());
                                    subcategorypushid.add(v.child("PushId").getValue().toString());
                                    subcategoryimage.add(v.child("SubCategoryImage").getValue().toString());
                                }

                                if(getContext()!=null) {
                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.spinner, subcategoryname);
                                    subcategory.setAdapter(adapter);
                                }
                            }
                        }
                        else {
                            if (getContext() != null) {
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.spinner, subcategoryname);
                                subcategory.setAdapter(adapter);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });




        subcategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i>0){
                    session.settemp(subcategorypushid.get(i));
                    FirebaseDatabase.getInstance().getReference().child("Vendor")
                            .child(session.getusername())
                            .child("SubCategory")
                            .child(subcategorypushid.get(i))
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
                                        category = new Category(category1);
                                        recyclerview.setAdapter(category);

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });




        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(subcategory.getSelectedItem().toString().equals("Select")){
                    Toast.makeText(getActivity(),"Select Sub Category",Toast.LENGTH_LONG).show();
                    return;
                }


                if(TextUtils.isEmpty(category1.getText().toString())){
                    category1.setError("Enter Category");
                    category1.requestFocus();
                    return;
                }

                a+=category1.getText().toString()+",";

                FirebaseDatabase.getInstance().getReference().child("Vendor")
                        .child(session.getusername())
                        .child("SubCategory")
                        .child(subcategorypushid.get(subcategory.getSelectedItemPosition()))
                        .child("ItemCategory")
                        .setValue(a);

                category1.setText("");

                FirebaseDatabase.getInstance().getReference().child("Vendor")
                        .child(session.getusername())
                        .child("SubCategory")
                        .child(subcategorypushid.get(subcategory.getSelectedItemPosition()))
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
                                    category = new Category(category1);
                                    recyclerview.setAdapter(category);

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

            }
        });


        return v;
    }
}
