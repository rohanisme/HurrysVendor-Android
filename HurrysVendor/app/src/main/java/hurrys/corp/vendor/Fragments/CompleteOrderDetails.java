package hurrys.corp.vendor.Fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.willy.ratingbar.RotationRatingBar;
import com.willy.ratingbar.ScaleRatingBar;

import java.text.DecimalFormat;

import cn.pedant.SweetAlert.SweetAlertDialog;
import hurrys.corp.vendor.Configurations.Session;
import hurrys.corp.vendor.Models.OrderDetails.OrderDetails;
import hurrys.corp.vendor.Models.OrderDetails.OrderDetails1;
import hurrys.corp.vendor.Models.OrderDetails.ViewHolder;
import hurrys.corp.vendor.R;

public class CompleteOrderDetails extends Fragment {

    private String pushid = "";

    private Session session;
    private RecyclerView mRecyclerView;
    private DatabaseReference mref;
    private TextView orderid, date, daname, address,support,reason;

    private TextView subtotal,discount,commision,delivery,tax,grandtotal,status;

    private Button neworder;


    public CompleteOrderDetails() {
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
        View v=inflater.inflate(R.layout.fragment_complete_order_details, container, false);
        orderid = v.findViewById(R.id.orderid);
        date =v.findViewById(R.id.date);

        daname = v.findViewById(R.id.daname);
        address = v.findViewById(R.id.address);
        support = v.findViewById(R.id.support);
        status = v.findViewById(R.id.status);
        reason = v.findViewById(R.id.reason);

        subtotal=v.findViewById(R.id.subtotal);
        delivery=v.findViewById(R.id.delivery);
        commision=v.findViewById(R.id.commision);
        tax=v.findViewById(R.id.tax);
        grandtotal=v.findViewById(R.id.grandtotal);
        neworder=v.findViewById(R.id.neworder);


        reason.setVisibility(View.GONE);


        ImageView back=v.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getActivity()!=null)
                    getActivity().onBackPressed();
            }
        });

        if(getActivity()!=null) {
            LinearLayout bottomnavigation = (getActivity()).findViewById(R.id.bottomnavigation);
            bottomnavigation.setVisibility(View.GONE);
        }

        session = new Session(getContext());
        mRecyclerView = v.findViewById(R.id.recyclerView);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);


        support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getActivity()!=null) {
                    Fragment fragment = new SupportFragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .addToBackStack(null)
                            .replace(R.id.frame_container, fragment).commitAllowingStateLoss();
                }
            }
        });


        pushid = getArguments().getString("pushid");


        FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            orderid.setText("Order ID : #"+dataSnapshot.child("OrderNo").getValue().toString().substring(5));
                            DecimalFormat form=new DecimalFormat("0.00");

                            date.setText(dataSnapshot.child("OrderDateTime").getValue().toString());
                            subtotal.setText("\u00a3"+form.format(Double.parseDouble(dataSnapshot.child("Subtotal").getValue().toString())));
                            delivery.setText("\u00a3"+form.format(Double.parseDouble(dataSnapshot.child("DeliveryCharges").getValue().toString())));

                            orderid.setText(dataSnapshot.child("OrderNo").getValue().toString());



                            double price = Double.parseDouble(subtotal.getText().toString().substring(1));
                            double del = Double.parseDouble(delivery.getText().toString().substring(1));
                            double com = 25;

                            double tot = price * (com/100.0);


                            double gtot = (price - tot - del );

                            commision.setText("\u00a3"+form.format(Math.round(tot*100.0)/100.0));
                            tax.setText("\u00a3"+form.format(0.00));
                            grandtotal.setText("\u00a3"+form.format(Math.round(gtot*100.0)/100.0));


                            address.setText(dataSnapshot.child("Address").getValue().toString());
                            daname.setText(dataSnapshot.child("CName").getValue().toString());

                            if(dataSnapshot.child("Status").getValue().toString().equals("5")) {
                                status.setText("DELIVERED");
                                status.setTextColor(Color.parseColor("#00B246"));
                            }
                            else if(dataSnapshot.child("Status").getValue().toString().equals("10")){
                                status.setText("CANCELLED");
                                if(dataSnapshot.child("RejectionReason").exists()) {
                                    reason.setText("Reason : " + dataSnapshot.child("RejectionReason").getValue().toString());
                                    reason.setVisibility(View.VISIBLE);
                                }
                                else{
                                    reason.setVisibility(View.GONE);
                                }
                                status.setTextColor(Color.parseColor("#FF0000"));
                            }


                            if(dataSnapshot.child("OrderType").getValue().toString().equals("Others")){
                                mref = FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("Cart");

                                FirebaseRecyclerAdapter<OrderDetails, ViewHolder> firebaseRecyclerAdapter =
                                        new FirebaseRecyclerAdapter<OrderDetails, ViewHolder>(
                                                OrderDetails.class,
                                                R.layout.orders_details_row,
                                                ViewHolder.class,
                                                mref
                                        ) {
                                            @Override
                                            protected void populateViewHolder(ViewHolder viewHolder, OrderDetails orderDetails, int position) {
                                                viewHolder.setDetails(getContext(),orderDetails.Image,orderDetails.Name,orderDetails.Price,orderDetails.Qty,orderDetails.Total,orderDetails.Units);

                                            }

                                            @Override
                                            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                                                final  ViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);
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
                            else{

                                mref = FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("Cart");
                                FirebaseRecyclerAdapter<OrderDetails1, ViewHolder> firebaseRecyclerAdapter =
                                        new FirebaseRecyclerAdapter<OrderDetails1, ViewHolder>(
                                                OrderDetails1.class,
                                                R.layout.orders_details_row,
                                                ViewHolder.class,
                                                mref
                                        ) {
                                            @Override
                                            protected void populateViewHolder(ViewHolder viewHolder, OrderDetails1 orderDetails, int position) {
                                                viewHolder.setDetails2(getContext(),orderDetails.Name,orderDetails.Price,orderDetails.Type,orderDetails.Qty,orderDetails.Image,orderDetails.Customised,orderDetails.CustomisedQty);
                                            }

                                            @Override
                                            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                                                final  ViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);
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

        neworder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getActivity()!=null) {
                    Fragment fragment = new Dashboard();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .addToBackStack(null)
                            .replace(R.id.frame_container, fragment).commit();
                }
            }
        });

//
//        rate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                final View ratingDialogView = getLayoutInflater().inflate(R.layout.ratings, null);
//                final AlertDialog ratingDialog = new AlertDialog.Builder(getActivity()).create();
//                ratingDialog.setView(ratingDialogView);
//                ratingDialog.show();
//                RotationRatingBar ratingBar = ratingDialogView.findViewById(R.id.ratebar);
//                ratingBar.setNumStars(5);
//                ratingBar.setStepSize(0.5f);
//                ratingBar.setMinimumStars(1);
//
//                EditText comments=ratingDialogView.findViewById(R.id.comments);
//                Button submit=ratingDialogView.findViewById(R.id.submit);
//
//
//                submit.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                        DatabaseReference dref = FirebaseDatabase.getInstance().getReference().child("DeliveryPartner").child(deliverypartner).child("Orders");
//
//                        dref.runTransaction(new Transaction.Handler() {
//                            @NonNull
//                            @Override
//                            public Transaction.Result doTransaction(@NonNull MutableData currentData) {
//                                double value = 0;
//                                if (currentData.getValue() != null) {
//                                    value = Long.parseLong(currentData.getValue().toString()) + 1;
//                                    id = Long.parseLong(currentData.getValue().toString()) + 1;
//                                }
//                                currentData.setValue(value);
//                                return Transaction.success(currentData);
//                            }
//
//                            @Override
//                            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
//
//
//                                DatabaseReference dref = FirebaseDatabase.getInstance().getReference().child("DeliveryPartner").child(deliverypartner).child("Ratings");
//
//                                dref.runTransaction(new Transaction.Handler() {
//                                    @NonNull
//                                    @Override
//                                    public Transaction.Result doTransaction(@NonNull MutableData currentData) {
//                                        double value = 0;
//                                        if (currentData.getValue() != null) {
//                                            value = Double.parseDouble(currentData.getValue().toString());
//                                            value = value*(id-1);
//                                            value+=ratingBar.getRating();
//                                            value=value/id;
//                                            value = value*100;
//                                            value = (double)((int) value);
//                                            value = value /100;
//                                            ratings=value;
//                                        }
//                                        currentData.setValue(value);
//                                        return Transaction.success(currentData);
//                                    }
//
//                                    @Override
//                                    public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
//
//
//
//                                        FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("DeliveryVendor").setValue(""+ratingBar.getRating());
//                                        FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("DeliveryVendorC").setValue(""+comments.getText().toString());
//
//                                        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("DeliveryPartner").child(deliverypartner).child("TRatings").push();
//                                        ref.child("PushId").setValue(ref.getKey());
//                                        ref.child("Comments").setValue(comments.getText().toString());
//                                        ref.child("Ratings").setValue(""+ratingBar.getRating());
//                                        ref.child("OrderId").setValue(ordno);
//                                        ref.child("OrderPushId").setValue(pushid);
//                                        rate.setVisibility(View.GONE);
//                                        ratingDialog.dismiss();
//
//                                    }
//                                });
//
//
//
//                            }
//                        });
//
//
//                    }
//                });
//
//
//            }
//        });

        return v;
    }

}