package hurrys.corp.vendor.Fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import hurrys.corp.vendor.Configurations.Session;
import hurrys.corp.vendor.Models.Category.Category;
import hurrys.corp.vendor.Models.Category.Sub;
import hurrys.corp.vendor.Models.Category.SubCategory;
import hurrys.corp.vendor.Models.Category.SubCategoryMaster;
import hurrys.corp.vendor.Models.Inventory.Inventory;
import hurrys.corp.vendor.Models.Inventory.InventoryAdapter;
import hurrys.corp.vendor.Models.Inventory.InventoryAdapter1;
import hurrys.corp.vendor.R;


public class InventoryFragment extends Fragment {

    private ImageView i1,i2,i4,i3;
    private Session session;
    private RecyclerView recyclerView;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mref;


    private ArrayList<Inventory> inventories=new ArrayList<Inventory>();
    private InventoryAdapter inventoryAdapter;
    private InventoryAdapter1 inventoryAdapter1;
    private FloatingActionButton plus;

    private LinearLayout l1,search_layout;
    private ImageView searchicon;

    private EditText search;
    private ImageView clear;

    private ArrayList<Sub> subcategory = new ArrayList<Sub>();
    private SubCategory subCategoryAdapter;

    private LinearLayout offline;
    private Button go;

    private LinearLayout z1,z2;
    private LinearLayout l10,l11;
    private TextView t1,t2;
    private View s1,s2;

    public InventoryFragment() {
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
        View v=inflater.inflate(R.layout.fragment_inventory, container, false);

        if(getActivity()!=null) {
            LinearLayout bottomnavigation = (getActivity()).findViewById(R.id.bottomnavigation);
            bottomnavigation.setVisibility(View.VISIBLE);

            i1=getActivity().findViewById(R.id.i1);
            i2=getActivity().findViewById(R.id.i2);
            i3=getActivity().findViewById(R.id.i3);

            i1.setImageResource(R.drawable.b1);
            i2.setImageResource(R.drawable.hb2);
            i3.setImageResource(R.drawable.b3);
        }





        z1=v.findViewById(R.id.z1);
        z2=v.findViewById(R.id.z2);
        t1=v.findViewById(R.id.t1);
        t2=v.findViewById(R.id.t2);
        s1=v.findViewById(R.id.s1);
        s2=v.findViewById(R.id.s2);
        plus=v.findViewById(R.id.plus);
        go=v.findViewById(R.id.go);
        offline=v.findViewById(R.id.offline);

        search=v.findViewById(R.id.txtSearch);
        clear=v.findViewById(R.id.clear);
        search_layout=v.findViewById(R.id.search_layout);
        searchicon=v.findViewById(R.id.searchicon);
        l1=v.findViewById(R.id.l1);
        clear.setVisibility(View.GONE);


        search_layout.setVisibility(View.GONE);
        l1.setVisibility(View.VISIBLE);

        inventories.clear();
        inventoryAdapter=new InventoryAdapter(inventories);
        inventoryAdapter1=new InventoryAdapter1(inventories);
        recyclerView=v.findViewById(R.id.recyclerView);
        l10=v.findViewById(R.id.l10);
        l11=v.findViewById(R.id.l11);

        l10.setVisibility(View.VISIBLE);
        l11.setVisibility(View.GONE);

        t2.setTextColor(Color.parseColor("#00B246"));
        t1.setTextColor(Color.parseColor("#808080"));
        s2.setBackgroundColor(Color.parseColor("#00B246"));
        s1.setBackgroundColor(Color.parseColor("#eaeaea"));

        session =new Session(getActivity());

        loadproducts();

        z1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(session.getcategory().equals("Food Delivery")||session.getcategory().equals("Home Food")) {
                        Toast.makeText(getContext(),"Add Category while adding products",Toast.LENGTH_LONG).show();
                }
                else {
                    loadcategories();
                    offline.setVisibility(View.GONE);
                    t1.setTextColor(Color.parseColor("#00B246"));
                    t2.setTextColor(Color.parseColor("#808080"));
                    s1.setBackgroundColor(Color.parseColor("#00B246"));
                    s2.setBackgroundColor(Color.parseColor("#eaeaea"));
                }
            }
        });

        z2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadproducts();
                t2.setTextColor(Color.parseColor("#00B246"));
                t1.setTextColor(Color.parseColor("#808080"));
                s2.setBackgroundColor(Color.parseColor("#00B246"));
                s1.setBackgroundColor(Color.parseColor("#eaeaea"));
            }
        });



        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(session.getcategory().equals("Food Delivery")||session.getcategory().equals("Home Food")) {
                    Fragment fragment = new AddFoodItems();
                    if(getActivity()!=null) {
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .addToBackStack(null)
                                .replace(R.id.frame_container, fragment).commitAllowingStateLoss();
                    }
                }
                else{
                    Fragment fragment = new AddProducts();
                    if(getActivity()!=null) {
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .addToBackStack(null)
                                .replace(R.id.frame_container, fragment).commitAllowingStateLoss();
                    }
                }
            }
        });

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(session.getcategory().equals("Food Delivery")||session.getcategory().equals("Home Food")) {
                    Fragment fragment = new AddFoodItems();
                    if(getActivity()!=null) {
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .addToBackStack(null)
                                .replace(R.id.frame_container, fragment).commitAllowingStateLoss();
                    }
                }
                else{
                    Fragment fragment = new AddProducts();
                    if(getActivity()!=null) {
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .addToBackStack(null)
                                .replace(R.id.frame_container, fragment).commitAllowingStateLoss();
                    }
                }
            }
        });


        searchicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search_layout.setVisibility(View.VISIBLE);
                searchicon.setVisibility(View.GONE);
            }
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                clear.setVisibility(View.VISIBLE);
                search_layout.setVisibility(View.VISIBLE);
                searchicon.setVisibility(View.GONE);
                if(session.getcategory().equals("Food Delivery")) {
                    inventoryAdapter.getFilter().filter(charSequence);
                }
                else{
                    inventoryAdapter1.getFilter().filter(charSequence);
                }

                if(charSequence.length()==0){
                    if(getActivity()!=null) {
                        View view = getActivity().getCurrentFocus();
                        if (view != null) {
                            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            assert imm != null;
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        }
                    }
                    clear.setVisibility(View.GONE);
                    search_layout.setVisibility(View.GONE);
                    searchicon.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search.setText("");
                clear.setVisibility(View.GONE);
                search_layout.setVisibility(View.GONE);
                searchicon.setVisibility(View.VISIBLE);
            }
        });

        if(session.getcategory().equals("Food Delivery")||session.getcategory().equals("Home Food")) {
                l10.setVisibility(View.GONE);
                l11.setVisibility(View.VISIBLE);
        }


        return v;
    }

    public void loadcategories(){

        plus.setVisibility(View.GONE);
        subcategory.clear();
        subCategoryAdapter=new SubCategory(subcategory);

        FirebaseDatabase.getInstance().getReference().child("Vendor").child(session.getusername()).child("SubCategory")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        subcategory.clear();

                        if(dataSnapshot.exists()){
                            for(DataSnapshot v:dataSnapshot.getChildren()){
                                if(v.child("Name").exists()&&v.child("PushId").exists()&&v.child("SubCategoryImage").exists()) {
                                    subcategory.add(new Sub(v.child("Name").getValue().toString(),
                                            v.child("PushId").getValue().toString(),
                                            v.child("SubCategoryImage").getValue().toString()));
                                }
                            }
                        }

                        subcategory.add(new Sub("New","New","New"));

                        GridLayoutManager mLayoutManager = new GridLayoutManager(getContext(),3);
                        recyclerView.setLayoutManager(mLayoutManager);
                        subCategoryAdapter = new SubCategory(subcategory);
                        recyclerView.setAdapter(subCategoryAdapter);
                        recyclerView.setVisibility(View.VISIBLE);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


    }

    public void loadproducts(){
        plus.setVisibility(View.VISIBLE);
        if(session.getcategory().equals("Food Delivery")||session.getcategory().equals("Home Food")) {
            FirebaseDatabase.getInstance().getReference().child("Vendor")
                    .child(session.getusername())
                    .child("Products")
                    .orderByChild("ApprovalStatus")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                inventories.clear();
                                recyclerView.setVisibility(View.VISIBLE);
                                offline.setVisibility(View.GONE);
                                plus.setVisibility(View.VISIBLE);
                                for (DataSnapshot v : dataSnapshot.getChildren()) {
                                    if (v.exists()) {
                                        String foodtype = "";
                                        if (v.child("FoodImage").exists())
                                            foodtype = v.child("FoodImage").getValue().toString();
                                        inventories.add(new Inventory(
                                                v.child("ItemName").getValue().toString(),
                                                v.child("PushId").getValue().toString(),
                                                v.child("Status").getValue().toString(),
                                                foodtype,
                                                v.child("ApprovalStatus").getValue().toString(),
                                                v.child("SellingPrice").getValue().toString()
                                        ));
                                    }
                                }
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                                recyclerView.setLayoutManager(mLayoutManager);
                                inventoryAdapter = new InventoryAdapter(inventories);
                                recyclerView.setAdapter(inventoryAdapter);
                                plus.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.VISIBLE);
                            }
                            else{
                                recyclerView.setVisibility(View.GONE);
                                offline.setVisibility(View.VISIBLE);
                                plus.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }
        else{
            FirebaseDatabase.getInstance().getReference().child("Vendor")
                    .child(session.getusername())
                    .child("Products")
                    .orderByChild("ApprovalStatus")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                inventories.clear();
                                recyclerView.setVisibility(View.VISIBLE);
                                offline.setVisibility(View.GONE);
                                plus.setVisibility(View.VISIBLE);
                                for (DataSnapshot v : dataSnapshot.getChildren()) {
                                    if (v.exists()) {
                                        String foodtype = "";
                                        if (v.child("ItemImage1").exists())
                                            foodtype = v.child("ItemImage1").getValue().toString();
                                        inventories.add(new Inventory(
                                                v.child("ItemName").getValue().toString(),
                                                v.child("PushId").getValue().toString(),
                                                v.child("Status").getValue().toString(),
                                                foodtype,
                                                v.child("ApprovalStatus").getValue().toString(),
                                                ""
                                        ));
                                    }
                                }
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                                recyclerView.setLayoutManager(mLayoutManager);
                                inventoryAdapter1 = new InventoryAdapter1(inventories);
                                recyclerView.setAdapter(inventoryAdapter1);
                                plus.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.VISIBLE);
                                offline.setVisibility(View.GONE);
                                plus.setVisibility(View.VISIBLE);
                            }
                            else{
                                recyclerView.setVisibility(View.GONE);
                                offline.setVisibility(View.VISIBLE);
                                plus.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

        }
    }
}
