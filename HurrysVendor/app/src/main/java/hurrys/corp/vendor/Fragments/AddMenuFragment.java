package hurrys.corp.vendor.Fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ServiceWorkerClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import hurrys.corp.vendor.Configurations.Session;
import hurrys.corp.vendor.Models.Inventory.Inventory;
import hurrys.corp.vendor.Models.Inventory.InventoryAdapter;
import hurrys.corp.vendor.Models.Inventory.InventoryAdapter1;
import hurrys.corp.vendor.R;

public class AddMenuFragment extends Fragment {



    public AddMenuFragment() {
        // Required empty public constructor
    }

    LinearLayout z1,z2,z3;
    TextView t1,t2,t3;
    View s1,s2,s3;
    RecyclerView recyclerView;
    ImageView back;

    Session session;
    private ArrayList<Inventory> inventories=new ArrayList<Inventory>();
    private InventoryAdapter1 inventoryAdapter1;
    private InventoryAdapter inventoryAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_add_menu, container, false);

        Button menu = v.findViewById(R.id.menu);
        Button meal = v.findViewById(R.id.meal);

        z1 = v.findViewById(R.id.z1);
        z2 = v.findViewById(R.id.z2);
        z3 = v.findViewById(R.id.z3);
        t1 = v.findViewById(R.id.t1);
        t2 = v.findViewById(R.id.t2);
        t3 = v.findViewById(R.id.t3);
        s1 = v.findViewById(R.id.s1);
        s2 = v.findViewById(R.id.s2);
        s3 = v.findViewById(R.id.s3);
        back = v.findViewById(R.id.back);
        recyclerView = v.findViewById(R.id.recyclerView);

        session = new Session(getActivity());
        inventories.clear();
        inventoryAdapter = new InventoryAdapter(inventories,getContext());
        inventoryAdapter1 = new InventoryAdapter1(inventories);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getActivity()!=null)
                    getActivity().onBackPressed();
            }
        });


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


        if(getActivity()!=null) {
            LinearLayout bottomnavigation = (getActivity()).findViewById(R.id.bottomnavigation);
            bottomnavigation.setVisibility(View.GONE);
        }

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getActivity()!=null) {
                    Fragment fragment = new AddFoodItems();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .addToBackStack(null)
                            .replace(R.id.frame_container, fragment).commit();
                }
            }
        });


        meal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getActivity()!=null) {
                    Fragment fragment = new CreateMeal();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .addToBackStack(null)
                            .replace(R.id.frame_container, fragment).commit();
                }
            }
        });


        return v;
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

                                        String g="";
                                        if(v.child("Stock").exists())
                                            g=""+v.child("Stock").getValue().toString();

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
                                                g
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