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

import cn.pedant.SweetAlert.SweetAlertDialog;
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
    private LinearLayout l1,search_layout;
    private ImageView searchicon;
    private EditText search;
    private ImageView clear;
    private ArrayList<Sub> subcategory = new ArrayList<Sub>();
    private SubCategory subCategoryAdapter;
    private LinearLayout offline;
    private Button go,category,plus;
    LinearLayout z1,z2,z3,header;
    TextView t1,t2,t3;
    View s1,s2,s3;

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


        z1 = v.findViewById(R.id.z1);
        z2 = v.findViewById(R.id.z2);
        z3 = v.findViewById(R.id.z3);
        t1 = v.findViewById(R.id.t1);
        t2 = v.findViewById(R.id.t2);
        t3 = v.findViewById(R.id.t3);
        s1 = v.findViewById(R.id.s1);
        s2 = v.findViewById(R.id.s2);
        s3 = v.findViewById(R.id.s3);
        plus=v.findViewById(R.id.plus);
        category=v.findViewById(R.id.category);
        go=v.findViewById(R.id.go);
        offline=v.findViewById(R.id.offline);
        header=v.findViewById(R.id.header);


        search=v.findViewById(R.id.txtSearch);
        clear=v.findViewById(R.id.clear);
        search_layout=v.findViewById(R.id.search_layout);
        searchicon=v.findViewById(R.id.searchicon);
        l1=v.findViewById(R.id.l1);
        clear.setVisibility(View.GONE);


        search_layout.setVisibility(View.GONE);
        l1.setVisibility(View.VISIBLE);

        inventories.clear();
        inventoryAdapter=new InventoryAdapter(inventories,getContext());
        inventoryAdapter1=new InventoryAdapter1(inventories);
        recyclerView=v.findViewById(R.id.recyclerView);


        t2.setTextColor(Color.parseColor("#00B246"));
        t1.setTextColor(Color.parseColor("#808080"));
        s2.setBackgroundColor(Color.parseColor("#00B246"));
        s1.setBackgroundColor(Color.parseColor("#eaeaea"));

        session =new Session(getActivity());

        t1.setTextColor(Color.parseColor("#00B246"));
        t2.setTextColor(Color.parseColor("#808080"));
        t3.setTextColor(Color.parseColor("#808080"));
        s1.setBackgroundColor(Color.parseColor("#00B246"));
        s2.setBackgroundColor(Color.parseColor("#eaeaea"));
        s3.setBackgroundColor(Color.parseColor("#eaeaea"));

        loadApproved();

        z1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                t1.setTextColor(Color.parseColor("#00B246"));
                t2.setTextColor(Color.parseColor("#808080"));
                t3.setTextColor(Color.parseColor("#808080"));
                s1.setBackgroundColor(Color.parseColor("#00B246"));
                s2.setBackgroundColor(Color.parseColor("#eaeaea"));
                s3.setBackgroundColor(Color.parseColor("#eaeaea"));
                loadApproved();
            }
        });

        z2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                t2.setTextColor(Color.parseColor("#00B246"));
                t1.setTextColor(Color.parseColor("#808080"));
                t3.setTextColor(Color.parseColor("#808080"));
                s2.setBackgroundColor(Color.parseColor("#00B246"));
                s1.setBackgroundColor(Color.parseColor("#eaeaea"));
                s3.setBackgroundColor(Color.parseColor("#eaeaea"));
                loadPending();
            }
        });

        z3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                t3.setTextColor(Color.parseColor("#00B246"));
                t2.setTextColor(Color.parseColor("#808080"));
                t1.setTextColor(Color.parseColor("#808080"));
                s3.setBackgroundColor(Color.parseColor("#00B246"));
                s2.setBackgroundColor(Color.parseColor("#eaeaea"));
                s1.setBackgroundColor(Color.parseColor("#eaeaea"));
                loadRejected();
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
                    FirebaseDatabase.getInstance().getReference().child("Vendor").child(session.getusername())
                            .child("SubCategory").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                Fragment fragment = new AddProducts();
                                if(getActivity()!=null) {
                                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                    fragmentManager.beginTransaction()
                                            .addToBackStack(null)
                                            .replace(R.id.frame_container, fragment).commitAllowingStateLoss();
                                }
                            }
                            else{
                                if(getContext()!=null) {
                                    SweetAlertDialog sDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                                            .setTitleText("Alert!")
                                            .setContentText("First create categories to add products!")
                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                    sweetAlertDialog.dismiss();
                                                }
                                            });
                                    sDialog.setCancelable(false);
                                    sDialog.show();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }
            }
        });

        category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadcategories();
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
                if(session.getcategory().equals("Food Delivery")||session.getcategory().equals("Home Food")) {
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


        return v;
    }

    public void loadcategories(){

        header.setVisibility(View.GONE);
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

    public void loadApproved(){

        inventories.clear();
        inventoryAdapter = new InventoryAdapter(inventories,getContext());
        recyclerView.setAdapter(inventoryAdapter);

        if(session.getcategory().equals("Food Delivery")||session.getcategory().equals("Home Food")) {
            FirebaseDatabase.getInstance().getReference().child("Vendor")
                    .child(session.getusername())
                    .child("Products")
                    .orderByChild("ApprovalStatus")
                    .equalTo("Approved")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                recyclerView.setVisibility(View.VISIBLE);
                                for (DataSnapshot v : dataSnapshot.getChildren()) {
                                    if (v.exists()) {
                                        String foodtype = "";
                                        String a ="",b="",c="",d="",e="";
                                        if (v.child("FoodImage").exists())
                                            foodtype = v.child("FoodImage").getValue().toString();

                                        if (v.child("ItemName").exists())
                                            a = v.child("ItemName").getValue().toString();

                                        if (v.child("PushId").exists())
                                            b = v.child("PushId").getValue().toString();

                                        if (v.child("Status").exists())
                                            c = v.child("Status").getValue().toString();

                                        String f ="";
                                        if (v.child("Type").exists()) {
                                            d = "Meal";
                                            f = "Yes";
                                        }

                                        if (v.child("SellingPrice").exists())
                                            e = v.child("SellingPrice").getValue().toString();

                                        if(v.child("Portions").exists())
                                            f="Yes";

                                        if(v.child("Addons").exists())
                                            f="Yes";

                                        String g="";
                                        if(v.child("Stock").exists())
                                            g=""+v.child("Stock").getValue().toString();

                                        inventories.add(new Inventory(
                                                a,
                                                b,
                                                c,
                                                foodtype,
                                                "Approved",
                                                e,
                                                d,
                                                f,
                                                d,
                                                session.getcategory(),
                                                g
                                        ));
                                    }
                                }

                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                                recyclerView.setLayoutManager(mLayoutManager);
                                inventoryAdapter = new InventoryAdapter(inventories,getContext());
                                recyclerView.setAdapter(inventoryAdapter);
                                recyclerView.setVisibility(View.VISIBLE);
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
                    .equalTo("Approved")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                inventories.clear();
                                recyclerView.setVisibility(View.VISIBLE);
                                for (DataSnapshot v : dataSnapshot.getChildren()) {
                                    if (v.exists()) {
                                        String foodtype = "",featured="";
                                        if (v.child("ItemImage1").exists())
                                            foodtype = v.child("ItemImage1").getValue().toString();
                                        if (v.child("Featured").exists())
                                            featured = v.child("Featured").getValue().toString();

                                        inventories.add(new Inventory(
                                                v.child("ItemName").getValue().toString(),
                                                v.child("PushId").getValue().toString(),
                                                v.child("Status").getValue().toString(),
                                                foodtype,
                                                v.child("ApprovalStatus").getValue().toString(),
                                                "",
                                                featured,
                                                "",
                                                "",
                                                session.getcategory(),
                                                ""
                                        ));
                                    }
                                }
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                                recyclerView.setLayoutManager(mLayoutManager);
                                inventoryAdapter1 = new InventoryAdapter1(inventories);
                                recyclerView.setAdapter(inventoryAdapter1);
                                recyclerView.setVisibility(View.VISIBLE);
                            }
                            else{
                                recyclerView.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

        }

    }

    public void loadRejected(){
        inventories.clear();
        inventoryAdapter = new InventoryAdapter(inventories,getContext());
        recyclerView.setAdapter(inventoryAdapter);

        if(session.getcategory().equals("Food Delivery")||session.getcategory().equals("Home Food")) {
            FirebaseDatabase.getInstance().getReference().child("Vendor")
                    .child(session.getusername())
                    .child("Products")
                    .orderByChild("ApprovalStatus")
                    .equalTo("Rejected")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                for (DataSnapshot v : dataSnapshot.getChildren()) {
                                    if (v.exists()) {
                                        String foodtype = "";
                                        String a ="",b="",c="",d="",e="";
                                        if (v.child("FoodImage").exists())
                                            foodtype = v.child("FoodImage").getValue().toString();

                                        if (v.child("ItemName").exists())
                                            a = v.child("ItemName").getValue().toString();

                                        if (v.child("PushId").exists())
                                            b = v.child("PushId").getValue().toString();

                                        if (v.child("Status").exists())
                                            c = v.child("Status").getValue().toString();

                                        String f ="";
                                        if (v.child("Type").exists()) {
                                            d = "Meal";
                                            f = "Yes";
                                        }

                                        if (v.child("SellingPrice").exists())
                                            e = v.child("SellingPrice").getValue().toString();

                                        if(v.child("Portions").exists())
                                            f="Yes";

                                        if(v.child("Addons").exists())
                                            f="Yes";

                                        String g="";
                                        if(v.child("Stock").exists())
                                            g=""+v.child("Stock").getValue().toString();


                                        inventories.add(new Inventory(
                                                a,
                                                b,
                                                c,
                                                foodtype,
                                                "Approved",
                                                e,
                                                d,
                                                f,
                                                d,
                                                session.getcategory(),
                                                g
                                        ));
                                    }
                                }
                            }

                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                            recyclerView.setLayoutManager(mLayoutManager);
                            inventoryAdapter = new InventoryAdapter(inventories,getContext());
                            recyclerView.setAdapter(inventoryAdapter);
                            recyclerView.setVisibility(View.VISIBLE);

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
                    .equalTo("Rejected")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                inventories.clear();
                                recyclerView.setVisibility(View.VISIBLE);
                                for (DataSnapshot v : dataSnapshot.getChildren()) {
                                    if (v.exists()) {
                                        String foodtype = "",featured="";
                                        if (v.child("ItemImage1").exists())
                                            foodtype = v.child("ItemImage1").getValue().toString();
                                        if (v.child("Featured").exists())
                                            featured = v.child("Featured").getValue().toString();

                                        inventories.add(new Inventory(
                                                v.child("ItemName").getValue().toString(),
                                                v.child("PushId").getValue().toString(),
                                                v.child("Status").getValue().toString(),
                                                foodtype,
                                                v.child("ApprovalStatus").getValue().toString(),
                                                "",
                                                featured,
                                                "",
                                                "",
                                                session.getcategory(),
                                                ""
                                        ));
                                    }
                                }
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                                recyclerView.setLayoutManager(mLayoutManager);
                                inventoryAdapter1 = new InventoryAdapter1(inventories);
                                recyclerView.setAdapter(inventoryAdapter1);
                                recyclerView.setVisibility(View.VISIBLE);
                            }
                            else{
                                recyclerView.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

        }
    }

    public void loadPending(){
        inventories.clear();
        inventoryAdapter = new InventoryAdapter(inventories,getContext());
        recyclerView.setAdapter(inventoryAdapter);
        if(session.getcategory().equals("Food Delivery")||session.getcategory().equals("Home Food")) {
            FirebaseDatabase.getInstance().getReference().child("Vendor")
                    .child(session.getusername())
                    .child("Products")
                    .orderByChild("ApprovalStatus")
                    .equalTo("Pending")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                recyclerView.setVisibility(View.VISIBLE);
                                for (DataSnapshot v : dataSnapshot.getChildren()) {
                                    if (v.exists()) {
                                        String foodtype = "";
                                        String a ="",b="",c="",d="",e="";
                                        if (v.child("FoodImage").exists())
                                            foodtype = v.child("FoodImage").getValue().toString();

                                        if (v.child("ItemName").exists())
                                            a = v.child("ItemName").getValue().toString();

                                        if (v.child("PushId").exists())
                                            b = v.child("PushId").getValue().toString();

                                        if (v.child("Status").exists())
                                            c = v.child("Status").getValue().toString();

                                        String f ="";
                                        if (v.child("Type").exists()) {
                                            d = "Meal";
                                            f = "Yes";
                                        }

                                        if (v.child("SellingPrice").exists())
                                            e = v.child("SellingPrice").getValue().toString();

                                        if(v.child("Portions").exists())
                                            f="Yes";

                                        if(v.child("Addons").exists())
                                            f="Yes";

                                        String g="";
                                        if(v.child("Stock").exists())
                                            g=""+v.child("Stock").getValue().toString();

                                        inventories.add(new Inventory(
                                                a,
                                                b,
                                                c,
                                                foodtype,
                                                "Pending",
                                                e,
                                                d,
                                                f,
                                                d,
                                                session.getcategory(),
                                                g
                                        ));
                                    }
                                }


                                FirebaseDatabase.getInstance().getReference().child("Vendor")
                                        .child(session.getusername())
                                        .child("Products")
                                        .orderByChild("ApprovalStatus")
                                        .equalTo("")
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.exists()) {
                                                    for (DataSnapshot v : dataSnapshot.getChildren()) {
                                                        if (v.exists()) {
                                                            String foodtype = "";
                                                            String a ="",b="",c="",d="",e="";
                                                            if (v.child("FoodImage").exists())
                                                                foodtype = v.child("FoodImage").getValue().toString();

                                                            if (v.child("ItemName").exists())
                                                                a = v.child("ItemName").getValue().toString();

                                                            if (v.child("PushId").exists())
                                                                b = v.child("PushId").getValue().toString();

                                                            if (v.child("Status").exists())
                                                                c = v.child("Status").getValue().toString();

                                                            String f ="";
                                                            if (v.child("Type").exists()) {
                                                                d = "Meal";
                                                                f = "Yes";
                                                            }

                                                            if (v.child("SellingPrice").exists())
                                                                e = v.child("SellingPrice").getValue().toString();

                                                            if(v.child("Portions").exists()) {
                                                                f = "Yes";
                                                                d = "Portions";
                                                            }

                                                            if(v.child("Addons").exists()) {
                                                                f = "Yes";
                                                                d = "Addons";
                                                            }

                                                            String g="";
                                                            if(v.child("Stock").exists())
                                                                g=""+v.child("Stock").getValue().toString();

                                                            inventories.add(new Inventory(
                                                                    a,
                                                                    b,
                                                                    c,
                                                                    foodtype,
                                                                    "Approved",
                                                                    e,
                                                                    d,
                                                                    f,
                                                                    d,
                                                                    session.getcategory(),
                                                                    g
                                                            ));
                                                        }
                                                    }


                                                }

                                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                                                recyclerView.setLayoutManager(mLayoutManager);
                                                inventoryAdapter = new InventoryAdapter(inventories,getContext());
                                                recyclerView.setAdapter(inventoryAdapter);
                                                recyclerView.setVisibility(View.VISIBLE);

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
                                        .equalTo("")
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.exists()) {
                                                    for (DataSnapshot v : dataSnapshot.getChildren()) {
                                                        if (v.exists()) {
                                                            String foodtype = "";
                                                            String a ="",b="",c="",d="",e="";
                                                            if (v.child("FoodImage").exists())
                                                                foodtype = v.child("FoodImage").getValue().toString();

                                                            if (v.child("ItemName").exists())
                                                                a = v.child("ItemName").getValue().toString();

                                                            if (v.child("PushId").exists())
                                                                b = v.child("PushId").getValue().toString();

                                                            if (v.child("Status").exists())
                                                                c = v.child("Status").getValue().toString();

                                                            String f ="";
                                                            if (v.child("Type").exists()) {
                                                                d = "Meal";
                                                                f = "Yes";
                                                            }

                                                            if (v.child("SellingPrice").exists())
                                                                e = v.child("SellingPrice").getValue().toString();

                                                            if(v.child("Portions").exists()) {
                                                                f = "Yes";
                                                                d = "Portions";
                                                            }

                                                            if(v.child("Addons").exists()) {
                                                                f = "Yes";
                                                                d = "Addons";
                                                            }

                                                            String g="";
                                                            if(v.child("Stock").exists())
                                                                g=""+v.child("Stock").getValue().toString();

                                                            inventories.add(new Inventory(
                                                                    a,
                                                                    b,
                                                                    c,
                                                                    foodtype,
                                                                    "Approved",
                                                                    e,
                                                                    d,
                                                                    f,
                                                                    d,
                                                                    session.getcategory(),
                                                                    g
                                                            ));
                                                        }
                                                    }


                                                }

                                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                                                recyclerView.setLayoutManager(mLayoutManager);
                                                inventoryAdapter = new InventoryAdapter(inventories,getContext());
                                                recyclerView.setAdapter(inventoryAdapter);
                                                recyclerView.setVisibility(View.VISIBLE);

                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
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
                    .equalTo("Pending")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                inventories.clear();
                                recyclerView.setVisibility(View.VISIBLE);
                                for (DataSnapshot v : dataSnapshot.getChildren()) {
                                    if (v.exists()) {
                                        String foodtype = "",featured="";
                                        if (v.child("ItemImage1").exists())
                                            foodtype = v.child("ItemImage1").getValue().toString();
                                        if (v.child("Featured").exists())
                                            featured = v.child("Featured").getValue().toString();

                                        inventories.add(new Inventory(
                                                v.child("ItemName").getValue().toString(),
                                                v.child("PushId").getValue().toString(),
                                                v.child("Status").getValue().toString(),
                                                foodtype,
                                                v.child("ApprovalStatus").getValue().toString(),
                                                "",
                                                featured,
                                                "",
                                                "",
                                                session.getcategory(),
                                                ""
                                        ));
                                    }
                                }
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                                recyclerView.setLayoutManager(mLayoutManager);
                                inventoryAdapter1 = new InventoryAdapter1(inventories);
                                recyclerView.setAdapter(inventoryAdapter1);
                                recyclerView.setVisibility(View.VISIBLE);
                            }
                            else{
                                recyclerView.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

        }

    }

}
