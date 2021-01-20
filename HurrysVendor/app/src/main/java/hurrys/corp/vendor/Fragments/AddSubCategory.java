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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

import hurrys.corp.vendor.Configurations.Session;
import hurrys.corp.vendor.Models.Category.Category;
import hurrys.corp.vendor.Models.Category.Sub;
import hurrys.corp.vendor.Models.Category.SubCategory;
import hurrys.corp.vendor.R;


public class AddSubCategory extends Fragment {


    RecyclerView recyclerview;
    ImageView submit;
    Spinner category;
    String a="";

    private ArrayList<Sub> sub1=new ArrayList<Sub>();
    SubCategory sub;

    private ArrayList<String> subcategoryname= new ArrayList<String>();
    private ArrayList<String> subcategorypushid= new ArrayList<String>();
    private ArrayList<String> subcategoryimage= new ArrayList<String>();



    public AddSubCategory() {
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
        View v=inflater.inflate(R.layout.fragment_add_sub_category, container, false);



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
        category=v.findViewById(R.id.category);
        recyclerview=v.findViewById(R.id.recyclerview);


        Session session=new Session(getContext());

        subcategoryimage.clear();
        subcategoryname.clear();
        subcategorypushid.clear();

        sub1.clear();
        sub=new SubCategory(sub1);




        FirebaseDatabase.getInstance().getReference().child("Vendor").child(session.getusername())
                .child("SubCategory")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            sub1.clear();
                            for(DataSnapshot v:dataSnapshot.getChildren()) {
                                if(v.child("Name").exists()&&v.child("PushId").exists()&&v.child("SubCategoryImage").exists())
                                    sub1.add(new Sub(v.child("Name").getValue().toString(),v.child("PushId").getValue().toString(),v.child("SubCategoryImage").getValue().toString()));
                            }

                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                            recyclerview.setLayoutManager(mLayoutManager);
                            sub = new SubCategory(sub1);
                            recyclerview.setAdapter(sub);
                        }

                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                        recyclerview.setLayoutManager(mLayoutManager);
                        sub = new SubCategory(sub1);
                        recyclerview.setAdapter(sub);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(category.getSelectedItem().toString().equals("Select")){
                    Toast.makeText(getActivity(),"Select Sub Category",Toast.LENGTH_LONG).show();
                    return;
                }

                DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Vendor").child(session.getusername())
                        .child("SubCategory").child(subcategorypushid.get(category.getSelectedItemPosition()));
                ref.child("Name").setValue(category.getSelectedItem().toString());
                ref.child("PushId").setValue(subcategorypushid.get(category.getSelectedItemPosition()));
                ref.child("SubCategoryImage").setValue(subcategoryimage.get(category.getSelectedItemPosition()));

                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                recyclerview.setLayoutManager(mLayoutManager);
                sub = new SubCategory(sub1);
                recyclerview.setAdapter(sub);



            }
        });


        return v;
    }
}