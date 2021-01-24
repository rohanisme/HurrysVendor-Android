package hurrys.corp.vendor.Fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.Calendar;

import hurrys.corp.vendor.Configurations.Session;
import hurrys.corp.vendor.Models.OrderDetails.OrderDetails;
import hurrys.corp.vendor.Models.OrderDetails.OrderDetails1;
import hurrys.corp.vendor.Models.OrderDetails.ViewHolder;
import hurrys.corp.vendor.Models.Orders.Orders;
import hurrys.corp.vendor.R;

public class CancelledOrders extends Fragment {


    public CancelledOrders() {
        // Required empty public constructor
    }


    Session session;
    TextView help,orderid;
    RecyclerView mRecyclerView;
    String pushid = "";
    private DatabaseReference mref;

    private TextView subtotal, discount, commision, delivery, tax, grandtotal;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_cancelled_orders, container, false);


        if(getActivity()!=null) {
            LinearLayout bottomnavigation = (getActivity()).findViewById(R.id.bottomnavigation);
            bottomnavigation.setVisibility(View.GONE);
        }

        session = new Session(getContext());
        mRecyclerView = v.findViewById(R.id.recyclerView);
        help = v.findViewById(R.id.help);
        orderid = v.findViewById(R.id.orderid);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        subtotal = v.findViewById(R.id.subtotal);
        delivery = v.findViewById(R.id.delivery);
        commision = v.findViewById(R.id.commision);
        discount = v.findViewById(R.id.discount);
        tax = v.findViewById(R.id.tax);
        grandtotal = v.findViewById(R.id.grandtotal);

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() != null) {
                    Fragment fragment = new SupportFragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .addToBackStack(null)
                            .replace(R.id.frame_container, fragment).commitAllowingStateLoss();
                }
            }
        });


        if(getArguments()!=null)
            pushid = getArguments().getString("pushid");


        FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            orderid.setText("Order ID : #" + dataSnapshot.child("OrderNo").getValue().toString().substring(5));
                            DecimalFormat form = new DecimalFormat("0.00");
                            subtotal.setText("\u00a3" + form.format(Double.parseDouble(dataSnapshot.child("Subtotal").getValue().toString())));
                            discount.setText("\u00a3" + form.format(Double.parseDouble(dataSnapshot.child("Discount").getValue().toString())));
                            delivery.setText("\u00a3" + form.format(Double.parseDouble(dataSnapshot.child("DeliveryCharges").getValue().toString())));
                            tax.setText("\u00a3" + form.format(Double.parseDouble(dataSnapshot.child("Taxes").getValue().toString())));

                            double price = Double.parseDouble(subtotal.getText().toString().substring(1));
                            double del = Double.parseDouble(delivery.getText().toString().substring(1));
                            double taxes = Double.parseDouble(tax.getText().toString().substring(1));
                            double disc = Double.parseDouble(discount.getText().toString().substring(1));
                            double com = 25;

                            double tot = price * (com / 100.0);


                            double gtot = (price - tot);

                            commision.setText("\u00a3" + form.format(Math.round(tot * 100.0) / 100.0));
//                            grandtotal.setText("\u00a3" + form.format(Math.round(gtot * 100.0) / 100.0));
                            grandtotal.setText("\u00a3 0.00");
                            gtot = Double.parseDouble(grandtotal.getText().toString().substring(1));


                            if (dataSnapshot.child("OrderType").getValue().toString().equals("Others")) {
                                mref = FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("Cart");

                                FirebaseRecyclerAdapter<OrderDetails, hurrys.corp.vendor.Models.OrderDetails.ViewHolder> firebaseRecyclerAdapter =
                                        new FirebaseRecyclerAdapter<OrderDetails, hurrys.corp.vendor.Models.OrderDetails.ViewHolder>(
                                                OrderDetails.class,
                                                R.layout.orders_details_row,
                                                hurrys.corp.vendor.Models.OrderDetails.ViewHolder.class,
                                                mref
                                        ) {
                                            @Override
                                            protected void populateViewHolder(hurrys.corp.vendor.Models.OrderDetails.ViewHolder viewHolder, OrderDetails orderDetails, int position) {
                                                viewHolder.setDetails(getContext(), orderDetails.Image, orderDetails.Name, orderDetails.Price, orderDetails.Qty, orderDetails.Total, orderDetails.Units);

                                            }

                                            @Override
                                            public hurrys.corp.vendor.Models.OrderDetails.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                                                final hurrys.corp.vendor.Models.OrderDetails.ViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);
                                                viewHolder.setOnClickListener(new hurrys.corp.vendor.Models.OrderDetails.ViewHolder.ClickListener() {
                                                    @Override
                                                    public void onItemClick(View v, final int position) {

                                                    }

                                                    @Override
                                                    public void onItemLongClick(View v, int position) {

                                                    }
                                                });
                                                return viewHolder;
                                            }

                                            @Override
                                            protected void onDataChanged() {
                                                super.onDataChanged();

                                            }

                                        };

                                mRecyclerView.setAdapter(firebaseRecyclerAdapter);
                            }
                            else {

                                mref = FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("Cart");
                                FirebaseRecyclerAdapter<OrderDetails1, hurrys.corp.vendor.Models.OrderDetails.ViewHolder> firebaseRecyclerAdapter =
                                        new FirebaseRecyclerAdapter<OrderDetails1, hurrys.corp.vendor.Models.OrderDetails.ViewHolder>(
                                                OrderDetails1.class,
                                                R.layout.orders_details_row,
                                                hurrys.corp.vendor.Models.OrderDetails.ViewHolder.class,
                                                mref
                                        ) {
                                            @Override
                                            protected void populateViewHolder(hurrys.corp.vendor.Models.OrderDetails.ViewHolder viewHolder, OrderDetails1 orderDetails, int position) {
                                                viewHolder.setDetails2(getContext(), orderDetails.Name, orderDetails.Price, orderDetails.Type, orderDetails.Qty, orderDetails.Image);
                                            }

                                            @Override
                                            public hurrys.corp.vendor.Models.OrderDetails.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                                                final hurrys.corp.vendor.Models.OrderDetails.ViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);
                                                viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
                                                    @Override
                                                    public void onItemClick(View v, final int position) {

                                                    }

                                                    @Override
                                                    public void onItemLongClick(View v, int position) {

                                                    }
                                                });
                                                return viewHolder;
                                            }

                                            @Override
                                            protected void onDataChanged() {
                                                super.onDataChanged();

                                            }

                                        };

                                mRecyclerView.setAdapter(firebaseRecyclerAdapter);
                            }


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


//        ImageView back = v.findViewById(R.id.back);
//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(getActivity()!=null)
//                getActivity().onBackPressed();
//            }
//        });




        return v;
    }

}