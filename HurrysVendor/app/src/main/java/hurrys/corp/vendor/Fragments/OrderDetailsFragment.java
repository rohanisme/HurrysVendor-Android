package hurrys.corp.vendor.Fragments;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmedelsayed.sunmiprinterutill.PrintMe;
import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import hurrys.corp.vendor.Configurations.Session;
import hurrys.corp.vendor.Models.Earnings;
import hurrys.corp.vendor.Models.OrderDetails.OrderDetails;
import hurrys.corp.vendor.Models.OrderDetails.OrderDetails1;
import hurrys.corp.vendor.Models.OrderDetails.ViewHolder;
import hurrys.corp.vendor.R;

public class OrderDetailsFragment extends Fragment {

    public OrderDetailsFragment() {
        // Required empty public constructor
    }

    private String pushid = "";
    private Session session;
    private RecyclerView mRecyclerView,precyclerview;
    private DatabaseReference mref;
    private TextView orderid, date, daname, address, support,instructions;
    private LinearLayout deliveryrow,deliveryamountrow;
    private TextView subtotal, discount, commision, delivery, grandtotal, status, number,deliverytype,slot,paymenttype,deliverytypetext;
    private Button accpet, accpet1,accept2, ready, decline, delivered, dispatched,print;
    private double gtot = 0, dbalance = 0;
    private BottomSheetDialog bottomSheetDialog;
    TextView storename;
    String selection = "";
    DataSnapshot d;
    double max=0;

    private ArrayList<String> foodpushid=new ArrayList<String>();
    private ArrayList<String> quantity=new ArrayList<String>();

    CircleImageView pp;
    TextView deliveryname;
    ImageView call, openMenu;
    private PrintMe printMe;
    TextView pstorename,porderno,pdeliverytime,pdeliverynote,psubmitted,paddress,pcustomer,pphone,pcookinginstructions,pqty,psubtotal,pdeliveryfee,ptotal;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_order_details, container, false);

        if (getActivity() != null) {
            LinearLayout bottomnavigation = (getActivity()).findViewById(R.id.bottomnavigation);
            bottomnavigation.setVisibility(View.GONE);
        }

        orderid = v.findViewById(R.id.orderid);
        date = v.findViewById(R.id.date);
        printMe =  new PrintMe(getContext());
        daname = v.findViewById(R.id.daname);
        address = v.findViewById(R.id.address);
        support = v.findViewById(R.id.support);
        status = v.findViewById(R.id.status);
        dispatched = v.findViewById(R.id.dispatched);
        delivered = v.findViewById(R.id.delivered);
        deliverytype=v.findViewById(R.id.delivertype);
        paymenttype=v.findViewById(R.id.paymenttype);
        instructions=v.findViewById(R.id.instructions);
        print=v.findViewById(R.id.print);
        slot=v.findViewById(R.id.slot);
//        storename=v.findViewById(R.id.storename);
        print.setVisibility(View.GONE);
        subtotal = v.findViewById(R.id.subtotal);
        delivery = v.findViewById(R.id.delivery);
        commision = v.findViewById(R.id.commision);
        discount = v.findViewById(R.id.discount);
//        tax = v.findViewById(R.id.tax);
        grandtotal = v.findViewById(R.id.grandtotal);
        accpet = v.findViewById(R.id.accept);
        accpet1 = v.findViewById(R.id.accept1);
        accept2 = v.findViewById(R.id.accept2);
        ready = v.findViewById(R.id.ready);
        decline = v.findViewById(R.id.decline);
        deliveryrow = v.findViewById(R.id.deliveryrow);
        deliveryamountrow = v.findViewById(R.id.deliveryamountrow);
        pp = v.findViewById(R.id.pp);
        deliveryname = v.findViewById(R.id.deliveryname);
        call = v.findViewById(R.id.call);
        openMenu = v.findViewById(R.id.openMenu);
        number = v.findViewById(R.id.number);
        deliverytypetext = v.findViewById(R.id.deliverytypetext);

        //Printing Data
        pstorename = v.findViewById(R.id.pstorename);
        porderno = v.findViewById(R.id.porderno);
        pdeliverytime = v.findViewById(R.id.pdeliverytime);
        pdeliverynote = v.findViewById(R.id.pdeliverynote);
        psubmitted = v.findViewById(R.id.psubmitted);
        paddress = v.findViewById(R.id.paddress);
        pcustomer = v.findViewById(R.id.pcustomer);
        pphone = v.findViewById(R.id.pphone);
        pcookinginstructions = v.findViewById(R.id.pcookinginstructions);
        pqty = v.findViewById(R.id.pqty);
        psubtotal = v.findViewById(R.id.psubtotal);
        pdeliveryfee = v.findViewById(R.id.pdeliveryfee);
        ptotal = v.findViewById(R.id.ptotal);
        precyclerview = v.findViewById(R.id.precyclerview);

        LinearLayoutManager mLayoutManager1 = new LinearLayoutManager(getContext());
        precyclerview.setLayoutManager(mLayoutManager1);

        ImageView back = v.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() != null)
                    getActivity().onBackPressed();
            }
        });

        session = new Session(getContext());
        mRecyclerView = v.findViewById(R.id.recyclerView);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        support.setOnClickListener(new View.OnClickListener() {
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

        pushid = getArguments().getString("pushid");

        FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            orderid.setText("Order ID : #" + dataSnapshot.child("OrderNo").getValue().toString().substring(5));
                            porderno.setText("#" + dataSnapshot.child("OrderNo").getValue().toString().substring(5));
                            pdeliverytime.setText("Delivery By : 21:50");


                            DecimalFormat form = new DecimalFormat("0.00");
                            d = dataSnapshot;

                            date.setText(dataSnapshot.child("OrderDateTime").getValue().toString());
                            psubmitted.setText("Submitted : "+dataSnapshot.child("OrderDateTime").getValue().toString());
                            subtotal.setText("\u00a3" + form.format(Double.parseDouble(dataSnapshot.child("Subtotal").getValue().toString())));
                            discount.setText("\u00a3" + form.format(Double.parseDouble(dataSnapshot.child("Discount").getValue().toString())));
                            delivery.setText("\u00a3" + form.format(Double.parseDouble(dataSnapshot.child("DeliveryCharges").getValue().toString())));
//                            tax.setText("\u00a3" + form.format(Double.parseDouble(dataSnapshot.child("Taxes").getValue().toString())));

//                            orderid.setText(dataSnapshot.child("OrderNo").getValue().toString());

                            if (dataSnapshot.child("DeliverySelection").exists()) {
                                selection = dataSnapshot.child("DeliverySelection").getValue().toString();
                            }

                            if(dataSnapshot.child("Payment").exists()){
                                paymenttype.setText(dataSnapshot.child("Payment").getValue().toString());
                            }

                            if(dataSnapshot.child("VendorInstructions").exists()){
                                if(!TextUtils.isEmpty(dataSnapshot.child("VendorInstructions").getValue().toString()))
                                    instructions.setText(dataSnapshot.child("VendorInstructions").getValue().toString());
                                else
                                    instructions.setText("No Special Instructions");
                            }
                            else
                                instructions.setText("No Special Instructions");

                            if(dataSnapshot.child("DeliverySelection").exists()){
                                if(dataSnapshot.child("DeliverySelection").getValue().toString().equals("Self")){
                                    deliverytype.setText("Self Delivery");
                                }
                                else if(dataSnapshot.child("DeliverySelection").getValue().toString().equals("Self PickUp")){
                                    deliverytype.setText("Self Pickup");}
                                else{
                                    deliverytype.setText("Delivery By Hurrys");
                                    }
                            }

                            if(dataSnapshot.child("DeliveryTime").exists()){
                                if(dataSnapshot.child("DeliveryTime").getValue().toString().equals("Immediately")){
                                    slot.setText("Immediately");
                                }
                                else{
                                    try {
                                        String a[] = dataSnapshot.child("DeliveryDate").getValue().toString().split("-");
                                        slot.setText(dataSnapshot.child("DeliveryTime").getValue().toString() + " - " + a[2] +"/"+ a[1] +"/"+ a[0]);
                                    }
                                    catch (Exception e){
                                        slot.setText(dataSnapshot.child("DeliveryTime").getValue().toString() + "\n" + dataSnapshot.child("DeliveryDate").getValue().toString());
                                    }
                                }
                            }

                            if (dataSnapshot.child("Status").getValue().toString().equals("1")) {
                                deliverytype.setVisibility(View.GONE);
                                deliverytypetext.setVisibility(View.GONE);
                            }

                            double price = Double.parseDouble(subtotal.getText().toString().substring(1));
                            double del = Double.parseDouble(delivery.getText().toString().substring(1));
//                            double taxes = Double.parseDouble(tax.getText().toString().substring(1));
                            double disc = Double.parseDouble(discount.getText().toString().substring(1));
                            double com = 25;

                            double tot = price * (com / 100.0);


                            deliveryamountrow.setVisibility(View.GONE);
                            gtot = (price - tot );

                            if(selection.equals("Self")){
                                gtot = gtot + del;
                                deliveryamountrow.setVisibility(View.VISIBLE);
                            }
                            psubtotal.setText("\u00a3" + form.format(Math.round(price * 100.0) / 100.0));
                            pdeliveryfee.setText("\u00a3" + form.format(Math.round(del * 100.0) / 100.0));
                            ptotal.setText("\u00a3" + form.format(Math.round(gtot * 100.0) / 100.0));

                            commision.setText("\u00a3" + form.format(Math.round(tot * 100.0) / 100.0));
                            grandtotal.setText("\u00a3" + form.format(Math.round(gtot * 100.0) / 100.0));
                            gtot = Double.parseDouble(grandtotal.getText().toString().substring(1));

                            address.setText(dataSnapshot.child("Address").getValue().toString());
                            paddress.setText("Address : "+dataSnapshot.child("Address").getValue().toString());
                            daname.setText(dataSnapshot.child("CName").getValue().toString());
                            pcustomer.setText("Customer : "+dataSnapshot.child("CName").getValue().toString());
                            pphone.setText("Phone Number : "+dataSnapshot.child("Number").getValue().toString());
                            pstorename.setText(session.getstorename());

                            pqty.setText(dataSnapshot.child("Qty").getValue().toString());
                            pcookinginstructions.setText(dataSnapshot.child("VendorInstructions").getValue().toString());

                                if (dataSnapshot.child("Status").getValue().toString().equals("1")) {
                                   if(!selection.equals("Self PickUp")){
                                        accpet.setVisibility(View.VISIBLE);
                                        accpet1.setVisibility(View.VISIBLE);
                                        accept2.setVisibility(View.GONE);
                                    }
                                   else{
                                       accpet.setVisibility(View.GONE);
                                       accpet1.setVisibility(View.GONE);
                                       accept2.setVisibility(View.VISIBLE);
                                   }
                                    decline.setVisibility(View.VISIBLE);
                                    ready.setVisibility(View.GONE);
                                    dispatched.setVisibility(View.GONE);
                                    delivered.setVisibility(View.GONE);
                                }
                                else if (dataSnapshot.child("Status").getValue().toString().equals("2")) {
                                    ready.setVisibility(View.VISIBLE);
                                    decline.setVisibility(View.GONE);
                                    accpet.setVisibility(View.GONE);
                                    accept2.setVisibility(View.GONE);
                                    accpet1.setVisibility(View.GONE);
                                    dispatched.setVisibility(View.GONE);
                                    delivered.setVisibility(View.GONE);
                                }
                                else if (dataSnapshot.child("Status").getValue().toString().equals("3")) {
                                    if (!TextUtils.isEmpty(selection)) {
                                        if (selection.equals("Self")) {
                                            ready.setVisibility(View.GONE);
                                            decline.setVisibility(View.GONE);
                                            accpet.setVisibility(View.GONE);
                                            accept2.setVisibility(View.GONE);
                                            accpet1.setVisibility(View.GONE);
                                            dispatched.setVisibility(View.VISIBLE);
                                            delivered.setVisibility(View.GONE);
                                        }
                                    }
                                }
                                else if (dataSnapshot.child("Status").getValue().toString().equals("4")) {
                                    if (!TextUtils.isEmpty(selection)) {
                                        if (selection.equals("Self")||selection.equals("Self PickUp")) {
                                            ready.setVisibility(View.GONE);
                                            decline.setVisibility(View.GONE);
                                            accept2.setVisibility(View.GONE);
                                            accpet.setVisibility(View.GONE);
                                            accpet1.setVisibility(View.GONE);
                                            dispatched.setVisibility(View.GONE);
                                            delivered.setVisibility(View.VISIBLE);
                                        }
                                    }
                                }
                                else {
                                    ready.setVisibility(View.GONE);
                                    accpet.setVisibility(View.GONE);
                                    accept2.setVisibility(View.GONE);
                                    accpet1.setVisibility(View.GONE);
                                    decline.setVisibility(View.GONE);
                                    dispatched.setVisibility(View.GONE);
                                    delivered.setVisibility(View.GONE);
                                }

                            if (dataSnapshot.child("DeliveryPartner").exists()) {
                                deliveryrow.setVisibility(View.VISIBLE);
                                deliveryname.setText(dataSnapshot.child("DeliveryPartner").getValue().toString());
                                number.setText(dataSnapshot.child("DeliveryNumber").getValue().toString());
                                if (dataSnapshot.child("DeliveryImage").exists())
                                    if (getContext() != null)
                                        Glide.with(getContext())
                                                .load(dataSnapshot.child("DeliveryImage").getValue().toString())
                                                .into(pp);
                            } else
                                deliveryrow.setVisibility(View.GONE);

                            if (dataSnapshot.child("Status").getValue().toString().equals("1")) {
                                status.setText("PENDING");
                                status.setTextColor(Color.parseColor("#b38400"));
                                status.setBackgroundColor(Color.parseColor("#FFF0C5"));
                                print.setVisibility(View.GONE);
                            }
                            else if (dataSnapshot.child("Status").getValue().toString().equals("2")) {
                                status.setText("PREPARING");
                                status.setTextColor(Color.parseColor("#00B246"));
                                status.setBackgroundColor(Color.parseColor("#e5f7ec"));
                                print.setVisibility(View.VISIBLE);
                            }
                            else if (dataSnapshot.child("Status").getValue().toString().equals("3")) {
                                status.setText("READY TO DELIVERY");
                                status.setTextColor(Color.parseColor("#00B246"));
                                status.setBackgroundColor(Color.parseColor("#e5f7ec"));
                                print.setVisibility(View.VISIBLE);
                            }
                            else if (dataSnapshot.child("Status").getValue().toString().equals("4")) {
                                status.setText("AWAITING DELIVERY");
                                status.setTextColor(Color.parseColor("#00B246"));
                                status.setBackgroundColor(Color.parseColor("#e5f7ec"));
                                print.setVisibility(View.VISIBLE);
                            }
                            else if (dataSnapshot.child("Status").getValue().toString().equals("5")) {
                                status.setText("DELIVERED");
                                status.setTextColor(Color.parseColor("#00B246"));
                                status.setBackgroundColor(Color.parseColor("#e5f7ec"));
                                print.setVisibility(View.VISIBLE);
                            }
                            else if (dataSnapshot.child("Status").getValue().toString().equals("10")) {
                                status.setText("CANCELLED");
                                status.setTextColor(Color.parseColor("#FF0000"));
                                status.setBackgroundColor(Color.parseColor("#F1B2B2"));
                                print.setVisibility(View.GONE);
                            }

                            if (dataSnapshot.child("OrderType").getValue().toString().equals("Others")) {
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
                                                viewHolder.setDetails(getContext(), orderDetails.Image, orderDetails.Name, orderDetails.Price, orderDetails.Qty, orderDetails.Total, orderDetails.Units);

                                            }

                                            @Override
                                            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                                                final ViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);
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
                            else {

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
                                                viewHolder.setDetails2(getContext(), orderDetails.Name, orderDetails.Price, orderDetails.Type, orderDetails.Qty, orderDetails.Image,orderDetails.Customised,orderDetails.CustomisedQty);
                                            }

                                            @Override
                                            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                                                final ViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);
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

                            if (dataSnapshot.child("OrderType").getValue().toString().equals("Others")) {
                                mref = FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("Cart");

                                FirebaseRecyclerAdapter<OrderDetails, ViewHolder> firebaseRecyclerAdapter =
                                        new FirebaseRecyclerAdapter<OrderDetails, ViewHolder>(
                                                OrderDetails.class,
                                                R.layout.orders_details_rowprint,
                                                ViewHolder.class,
                                                mref
                                        ) {
                                            @Override
                                            protected void populateViewHolder(ViewHolder viewHolder, OrderDetails orderDetails, int position) {
                                                viewHolder.setDetailsPrint(getContext(), orderDetails.Image, orderDetails.Name, orderDetails.Price, orderDetails.Qty, orderDetails.Total, orderDetails.Units);

                                            }

                                            @Override
                                            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                                                final ViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);
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
                                precyclerview.setAdapter(firebaseRecyclerAdapter);
                            }
                            else {
                                mref = FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("Cart");
                                FirebaseRecyclerAdapter<OrderDetails1, ViewHolder> firebaseRecyclerAdapter =
                                        new FirebaseRecyclerAdapter<OrderDetails1, ViewHolder>(
                                                OrderDetails1.class,
                                                R.layout.orders_details_rowprint,
                                                ViewHolder.class,
                                                mref
                                        ) {
                                            @Override
                                            protected void populateViewHolder(ViewHolder viewHolder, OrderDetails1 orderDetails, int position) {
                                                viewHolder.setDetailsPrint2(getContext(), orderDetails.Name, orderDetails.Price, orderDetails.Type, orderDetails.Qty, orderDetails.Image,orderDetails.Customised,orderDetails.CustomisedQty);
                                            }

                                            @Override
                                            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                                                final ViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);
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

                                precyclerview.setAdapter(firebaseRecyclerAdapter);
                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

         call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:" + number.getText().toString()));
                        startActivity(intent);
                    }
                    catch (Exception e){
                        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Delivery Partner Number : "+number.getText().toString())
                                .show();
                    }
                }
            });

        openMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(getContext(), view);
                popupMenu.inflate(R.menu.option_menu_item);
                popupMenu.setOnMenuItemClickListener(new OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.supportMenu:
                                if (getActivity() != null) {
                                    Fragment fragment = new SupportFragment();
                                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                    fragmentManager.beginTransaction()
                                            .addToBackStack(null)
                                            .replace(R.id.frame_container, fragment).commitAllowingStateLoss();
                                }
                                return true;

                        }

                        return false;
                    }
                });
                popupMenu.show();

            }
        });

        final Date currentDate = new Date(System.currentTimeMillis());
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        final String date1 = df.format(currentDate);

        SimpleDateFormat df1 = new SimpleDateFormat("dd, MMM yyyy  HH:MM");
        final String date2 = df1.format(currentDate);

        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                storename.setText("Rohan");
                printMe.sendViewToPrinter(v.findViewById(R.id.print_me_layout));
//                Toast.makeText(getContext(),"Service Not Available",Toast.LENGTH_LONG).show();
            }
        });

        accpet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (getContext() != null) {
                    SweetAlertDialog sDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Are you sure you want to accept the order!")
                            .setConfirmText("Yes")
                            .setCancelText("No")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();

                                    if(!slot.getText().toString().equals("Immediately")){

                                        FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("Status").setValue("2");
                                        FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("DeliverySelection").setValue("Hurrys");
                                        FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("SellerName").setValue(session.getstorename());
                                        FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("SellerAddress").setValue(session.getaddress());
                                        FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("SellerNumber").setValue(session.getnumber());
                                        session.setselection("On");

                                        if(session.getcategory().equals("Home Food")){
                                            reduceCartData();
                                        }
                                        else {
                                            if (getActivity() != null)
                                                getActivity().onBackPressed();
                                        }

                                    }
                                    else {
                                        final EditText editText = new EditText(getContext());
                                        FrameLayout container1 = new FrameLayout(getActivity());
                                        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                        params.leftMargin = getResources().getDimensionPixelSize(R.dimen.dialog_marginLeft);
                                        params.rightMargin = getResources().getDimensionPixelSize(R.dimen.dialog_marginLeft);
                                        params.topMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
                                        params.bottomMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
                                        editText.setLayoutParams(params);
                                        editText.setGravity(Gravity.CENTER);
                                        container1.addView(editText);
                                        editText.setText(session.getdeliverytime());
                                        int a = Integer.parseInt(session.getdeliverytime());
                                        if(a<=90){
                                            max = 90;
                                        }
                                        else{
                                            max = a;
                                        }
                                        editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
                                        SweetAlertDialog dialog = new SweetAlertDialog(getContext(), SweetAlertDialog.NORMAL_TYPE)
                                                .setTitleText("Please mention the delivery time in mins")
                                                .setConfirmText("Ok")
                                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override
                                                    public void onClick(SweetAlertDialog sweetAlertDialog) {

                                                        String reason = editText.getText().toString();



                                                        if(TextUtils.isEmpty(reason)){
                                                           editText.setError("Enter Minutes");
                                                           return;
                                                        }

                                                        if(Integer.parseInt(reason)>max){
                                                            editText.setError("Maximum Minutes can be "+max +" Mins only");
                                                            return;
                                                        }

                                                        if(Integer.parseInt(reason)<=30){
                                                            editText.setError("Minimim Minutes can be 30 Mins only");
                                                            return;
                                                        }

                                                        FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("Status").setValue("2");
                                                        FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("DeliverySelection").setValue("Hurrys");
                                                        FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("SellerName").setValue(session.getstorename());
                                                        FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("SellerAddress").setValue(session.getaddress());
                                                        FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("SellerNumber").setValue(session.getnumber());
                                                        FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("DeliveryTimeSelected").setValue("" + reason);
                                                        session.setselection("On");
                                                        sweetAlertDialog.dismiss();

                                                        if(session.getcategory().equals("Home Food")){
                                                            reduceCartData();
                                                        }
                                                        else {
                                                            if (getActivity() != null)
                                                                getActivity().onBackPressed();
                                                        }

                                                    }
                                                })
                                                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override
                                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                        sweetAlertDialog.dismiss();
                                                    }
                                                });
                                        dialog.setCustomView(container1);
                                        dialog.show();
                                    }

                                }
                            })
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                }
                            });

                    sDialog.setCancelable(false);
                    sDialog.show();
                }


            }
        });

        accpet1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (getContext() != null) {
                    SweetAlertDialog sDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Are you sure you want to accept the order and self delivery!")
                            .setConfirmText("Yes")
                            .setCancelText("No")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();

                                    if(!slot.getText().toString().equals("Immediately")){

                                        FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("Status").setValue("2");
                                        FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("DeliverySelection").setValue("Self");
                                        FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("SellerName").setValue(session.getstorename());
                                        FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("SellerAddress").setValue(session.getaddress());
                                        FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("SellerNumber").setValue(session.getnumber());
                                        session.setselection("On");

                                        if(session.getcategory().equals("Home Food")){
                                            reduceCartData();
                                        }
                                        else {
                                            if (getActivity() != null)
                                                getActivity().onBackPressed();
                                        }
                                    }
                                    else {
                                        final EditText editText = new EditText(getContext());
                                        FrameLayout container1 = new FrameLayout(getActivity());
                                        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                        params.leftMargin = getResources().getDimensionPixelSize(R.dimen.dialog_marginLeft);
                                        params.rightMargin = getResources().getDimensionPixelSize(R.dimen.dialog_marginLeft);
                                        params.topMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
                                        params.bottomMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
                                        editText.setLayoutParams(params);
                                        editText.setGravity(Gravity.CENTER);
                                        container1.addView(editText);
                                        editText.setText(session.getdeliverytime());
                                        int a = Integer.parseInt(session.getdeliverytime());
                                        if(a<=90){
                                            max = 90;
                                        }
                                        else{
                                            max = a;
                                        }
                                        editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
                                        SweetAlertDialog dialog = new SweetAlertDialog(getContext(), SweetAlertDialog.NORMAL_TYPE)
                                                .setTitleText("Please mention the delivery time in mins")
                                                .setConfirmText("Ok")
                                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override
                                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                        String reason = editText.getText().toString();

                                                        if(TextUtils.isEmpty(reason)){
                                                            editText.setError("Enter Minutes");
                                                            return;
                                                        }

                                                        if(Integer.parseInt(reason)>max){
                                                            editText.setError("Maximum Minutes can be "+max +" Mins only");
                                                            return;
                                                        }

                                                        if(Integer.parseInt(reason)<=30){
                                                            editText.setError("Minimim Minutes can be 30 Mins only");
                                                            return;
                                                        }

                                                        FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("Status").setValue("2");
                                                        FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("DeliverySelection").setValue("Self");
                                                        FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("SellerName").setValue(session.getstorename());
                                                        FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("SellerAddress").setValue(session.getaddress());
                                                        FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("SellerNumber").setValue(session.getnumber());
                                                        FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("DeliveryTimeSelected").setValue("" + reason);
                                                        sweetAlertDialog.dismiss();
                                                        session.setselection("On");

                                                        if(session.getcategory().equals("Home Food")){
                                                            reduceCartData();
                                                        }
                                                        else {
                                                            if (getActivity() != null)
                                                                getActivity().onBackPressed();
                                                        }

                                                    }
                                                })
                                                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override
                                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                        sweetAlertDialog.dismiss();
                                                    }
                                                });
                                        dialog.setCustomView(container1);
                                        dialog.show();
                                    }

                                }
                            })
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                }
                            });

                    sDialog.setCancelable(false);
                    sDialog.show();
                }


            }
        });

        accept2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (getContext() != null) {
                    SweetAlertDialog sDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Are you sure you want to accept the order!")
                            .setConfirmText("Yes")
                            .setCancelText("No")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();

                                    if(slot.getText().toString().equals("Immediately")){

                                        FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("Status").setValue("2");
                                        FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("DeliverySelection").setValue("Self PickUp");
                                        FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("SellerName").setValue(session.getstorename());
                                        FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("SellerAddress").setValue(session.getaddress());
                                        FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("SellerNumber").setValue(session.getnumber());
                                        session.setselection("On");
                                        if(session.getcategory().equals("Home Food")){
                                            reduceCartData();
                                        }
                                        else {
                                            if (getActivity() != null)
                                                getActivity().onBackPressed();
                                        }
                                    }
                                    else {
                                        final EditText editText = new EditText(getContext());
                                        FrameLayout container1 = new FrameLayout(getActivity());
                                        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                        params.leftMargin = getResources().getDimensionPixelSize(R.dimen.dialog_marginLeft);
                                        params.rightMargin = getResources().getDimensionPixelSize(R.dimen.dialog_marginLeft);
                                        params.topMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
                                        params.bottomMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
                                        editText.setLayoutParams(params);
                                        editText.setGravity(Gravity.CENTER);
                                        container1.addView(editText);
                                        editText.setText(session.getdeliverytime());
                                        editText.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
                                        SweetAlertDialog dialog = new SweetAlertDialog(getContext(), SweetAlertDialog.NORMAL_TYPE)
                                                .setTitleText("Please mention the delivery time in mins")
                                                .setConfirmText("Ok")
                                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override
                                                    public void onClick(SweetAlertDialog sweetAlertDialog) {

                                                        String reason = editText.getText().toString();
                                                        FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("Status").setValue("2");
                                                        FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("DeliverySelection").setValue("Self PickUp");
                                                        FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("SellerName").setValue(session.getstorename());
                                                        FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("SellerAddress").setValue(session.getaddress());
                                                        FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("SellerNumber").setValue(session.getnumber());
                                                        FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("DeliveryTimeSelected").setValue("" + reason);
                                                        session.setselection("On");
                                                        sweetAlertDialog.dismiss();

                                                        if(session.getcategory().equals("Home Food")){
                                                            reduceCartData();
                                                        }
                                                        else {
                                                            if (getActivity() != null)
                                                                getActivity().onBackPressed();
                                                        }


                                                    }
                                                })
                                                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override
                                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                        sweetAlertDialog.dismiss();
                                                    }
                                                });
                                        dialog.setCustomView(container1);
                                        dialog.show();
                                    }



                                }
                            })
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                }
                            });

                    sDialog.setCancelable(false);
                    sDialog.show();
                }


            }
        });

        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (getContext() != null) {

                    final EditText editText = new EditText(getContext());
                    SweetAlertDialog dialog = new SweetAlertDialog(getContext(), SweetAlertDialog.NORMAL_TYPE)
                            .setTitleText("Please mention the reason for rejection")
                            .setConfirmText("Ok")
                            .setCancelText("Cancel")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    if (TextUtils.isEmpty(editText.getText().toString())) {
                                        Toast.makeText(getContext(), "Enter Reason", Toast.LENGTH_SHORT).show();
                                        return;
                                    }

                                    String reason = editText.getText().toString();

                                    Date currentDate = new Date(System.currentTimeMillis());

                                    SimpleDateFormat df2 = new SimpleDateFormat("dd,MMM yyyy HH:mm");
                                    final String date3 = df2.format(currentDate);

                                    FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("Status").setValue("10");
                                    FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("SellerStatus").setValue(null);
                                    FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("SellerName").setValue(session.getstorename());
                                    FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("SellerAddress").setValue(session.getaddress());
                                    FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("SellerNumber").setValue(session.getnumber());
                                    FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("RejectionDate").setValue(date3);
                                    FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("RejectionReason").setValue(reason);


                                    if (getActivity() != null) {
                                        sweetAlertDialog.dismiss();
                                        getActivity().onBackPressed();
                                    }

                                }
                            })
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                }
                            });
                    dialog.setCustomView(editText);
                    dialog.show();
                }
            }

        });

        ready.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getContext() != null) {
                    SweetAlertDialog sDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Is the order ready!")
                            .setConfirmText("Yes")
                            .setCancelText("No")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                    if (selection.equals("Self PickUp")) {
                                        FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("Status").setValue("4");
                                    }
                                    else{
                                        FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("Status").setValue("3");
                                    }
                                    DatabaseReference dref = FirebaseDatabase.getInstance().getReference().child("Vendor").child(session.getusername()).child("PendingAmount");

                                    dref.runTransaction(new Transaction.Handler() {
                                        @NonNull
                                        @Override
                                        public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                                            double value = 0;
                                            if (currentData.getValue() != null) {
                                                value = Double.parseDouble(currentData.getValue().toString()) + gtot;
                                                value = value * 100;
                                                value = Math.round(value);
                                                value = value / 100;
                                                dbalance = Double.parseDouble(currentData.getValue().toString()) + gtot;
                                                dbalance = dbalance * 100;
                                                dbalance = Math.round(dbalance);
                                                dbalance = dbalance / 100;
                                            }
                                            currentData.setValue(value);
                                            return Transaction.success(currentData);
                                        }

                                        @Override
                                        public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                                            DatabaseReference rref1 = FirebaseDatabase.getInstance().getReference().child("Vendor").child(session.getusername()).child("Transactions").push();
                                            Earnings earnings = new Earnings(rref1.getKey(), session.getusername(), Double.toString(dbalance), date1, Double.toString(gtot), "Online",
                                                    "Cr", "Order Total", orderid.getText().toString().substring(12), "Pending", pushid);
                                            rref1.setValue(earnings);
                                            if (getContext() != null) {
                                                if (selection.equals("Self")) {
                                                    SweetAlertDialog sDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                                                            .setTitleText("Good job!")
                                                            .setContentText("You Accepted  to self deliver the order!")
                                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                @Override
                                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                                    sweetAlertDialog.dismiss();
                                                                    if (getActivity() != null)
                                                                        getActivity().onBackPressed();
                                                                }
                                                            });
                                                    sDialog.setCancelable(false);
                                                    sDialog.show();
                                                }
                                                else if (selection.equals("Self PickUp")) {
                                                    SweetAlertDialog sDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                                                            .setTitleText("Good job!")
                                                            .setContentText("Your order will be self picked by customer shortly!")
                                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                @Override
                                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                                    sweetAlertDialog.dismiss();
                                                                    if (getActivity() != null)
                                                                        getActivity().onBackPressed();
                                                                }
                                                            });
                                                    sDialog.setCancelable(false);
                                                    sDialog.show();
                                                }
                                                else {
                                                    SweetAlertDialog sDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                                                            .setTitleText("Good job!")
                                                            .setContentText("You order will be picked shortly!")
                                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                @Override
                                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                                    sweetAlertDialog.dismiss();
                                                                    if (getActivity() != null)
                                                                        getActivity().onBackPressed();
                                                                }
                                                            });
                                                    sDialog.setCancelable(false);
                                                    sDialog.show();
                                                }
                                            }
                                        }
                                    });
                                }
                            })
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                }
                            });

                    sDialog.setCancelable(false);
                    sDialog.show();
                }
            }
        });

        dispatched.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (getContext() != null) {
                    SweetAlertDialog sDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Are you sure you want to dispatch the order!")
                            .setConfirmText("Yes")
                            .setCancelText("No")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                    FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("Status").setValue("4");
                                    if (getActivity() != null)
                                        getActivity().onBackPressed();

                                }
                            })
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                }
                            });

                    sDialog.setCancelable(false);
                    sDialog.show();
                }


            }
        });

        delivered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (getContext() != null) {
                    SweetAlertDialog sDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Are you sure you want to mark the order delivered!")
                            .setConfirmText("Yes")
                            .setCancelText("No")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                    FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("Status").setValue("5");
                                    FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("SellerStatus").removeValue();
                                    if (getActivity() != null)
                                        getActivity().onBackPressed();

                                }
                            })
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                }
                            });

                    sDialog.setCancelable(false);
                    sDialog.show();
                }


            }
        });

//        handover.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                handover.setEnabled(false);
//
//                if(getContext()!=null) {
//                    SweetAlertDialog sDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
//                            .setTitleText("Has the delivery partner arrived!")
//                            .setConfirmText("Yes")
//                            .setCancelText("No")
//                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                                @Override
//                                public void onClick(SweetAlertDialog sweetAlertDialog) {
//                                    sweetAlertDialog.dismiss();
//
//
//                                }
//                            })
//                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                                @Override
//                                public void onClick(SweetAlertDialog sweetAlertDialog) {
//                                    sweetAlertDialog.dismiss();
//                                    handover.setEnabled(true);
//                                }
//                            });
//
//                    sDialog.setCancelable(false);
//                    sDialog.show();
//                }
//
//
//
//            }
//        });

        return v;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.option_menu_item, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    /*@Override
    public boolean onMenuItemClick(MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.cancelmenu:
                return true;
            case R.id.supportMenu:
                if (getActivity() != null) {
                    Fragment fragment = new SupportFragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .addToBackStack(null)
                            .replace(R.id.frame_container, fragment).commitAllowingStateLoss();
                }
                return true;

        }

        return false;
    }*/

    public void reduceCartData(){
      FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("Cart")
              .addListenerForSingleValueEvent(new ValueEventListener() {
                  @Override
                  public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                      if(dataSnapshot.exists()){
                            long qty = dataSnapshot.getChildrenCount();

                              for (DataSnapshot v : dataSnapshot.getChildren()) {
                                  foodpushid.add(v.child("PushId").getValue().toString());
                                  quantity.add(v.child("Qty").getValue().toString());
                              }

                              if(0<qty){
                                  final double[] a = {0};
                                DatabaseReference dref = FirebaseDatabase.getInstance().getReference().child("Vendor")
                                        .child(session.getusername())
                                        .child("Products")
                                        .child(foodpushid.get(0)).child("Stock");

                                dref.runTransaction(new Transaction.Handler() {
                                    @NonNull
                                    @Override
                                    public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                                        double value = 0;
                                        if (currentData.getValue() != null) {
                                            value = Double.parseDouble(currentData.getValue().toString()) - Integer.parseInt(quantity.get(0));
                                            a[0] = value;
                                        }
                                        currentData.setValue(value);
                                        return Transaction.success(currentData);
                                    }
                                    @Override
                                    public void onComplete(@androidx.annotation.Nullable DatabaseError databaseError, boolean b, @androidx.annotation.Nullable DataSnapshot dataSnapshot) {
                                            if(a[0]<=0){
                                                FirebaseDatabase.getInstance().getReference().child("Vendor")
                                                        .child(session.getusername())
                                                        .child("Products")
                                                        .child(foodpushid.get(0)).child("Status").setValue("InActive");
                                            }
                                    }
                                });
                            }

                              if(1<qty){
                                  final double[] a = {0};
                                DatabaseReference dref = FirebaseDatabase.getInstance().getReference().child("Vendor")
                                        .child(session.getusername())
                                        .child("Products")
                                        .child(foodpushid.get(1)).child("Stock");

                                dref.runTransaction(new Transaction.Handler() {
                                    @NonNull
                                    @Override
                                    public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                                        double value = 0;
                                        if (currentData.getValue() != null) {
                                            value = Double.parseDouble(currentData.getValue().toString()) - Integer.parseInt(quantity.get(1));
                                            a[0] = value;
                                        }
                                        currentData.setValue(value);
                                        return Transaction.success(currentData);
                                    }
                                    @Override
                                    public void onComplete(@androidx.annotation.Nullable DatabaseError databaseError, boolean b, @androidx.annotation.Nullable DataSnapshot dataSnapshot) {
                                        if(a[0]<=0){
                                            FirebaseDatabase.getInstance().getReference().child("Vendor")
                                                    .child(session.getusername())
                                                    .child("Products")
                                                    .child(foodpushid.get(1)).child("Status").setValue("InActive");
                                        }
                                    }
                                });
                            }

                              if(2<qty){
                                  final double[] a = {0};
                                DatabaseReference dref = FirebaseDatabase.getInstance().getReference().child("Vendor")
                                        .child(session.getusername())
                                        .child("Products")
                                        .child(foodpushid.get(2)).child("Stock");

                                dref.runTransaction(new Transaction.Handler() {
                                    @NonNull
                                    @Override
                                    public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                                        double value = 0;
                                        if (currentData.getValue() != null) {
                                            value = Double.parseDouble(currentData.getValue().toString()) - Integer.parseInt(quantity.get(2));
                                            a[0] = value;
                                        }
                                        currentData.setValue(value);
                                        return Transaction.success(currentData);
                                    }
                                    @Override
                                    public void onComplete(@androidx.annotation.Nullable DatabaseError databaseError, boolean b, @androidx.annotation.Nullable DataSnapshot dataSnapshot) {
                                        if(a[0]<=0){
                                            FirebaseDatabase.getInstance().getReference().child("Vendor")
                                                    .child(session.getusername())
                                                    .child("Products")
                                                    .child(foodpushid.get(2)).child("Status").setValue("InActive");
                                        }
                                    }
                                });
                            }

                              if(3<qty){
                                  final double[] a = {0};
                                DatabaseReference dref = FirebaseDatabase.getInstance().getReference().child("Vendor")
                                        .child(session.getusername())
                                        .child("Products")
                                        .child(foodpushid.get(3)).child("Stock");

                                dref.runTransaction(new Transaction.Handler() {
                                    @NonNull
                                    @Override
                                    public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                                        double value = 0;
                                        if (currentData.getValue() != null) {
                                            value = Double.parseDouble(currentData.getValue().toString()) - Integer.parseInt(quantity.get(3));
                                            a[0] = value;
                                        }
                                        currentData.setValue(value);
                                        return Transaction.success(currentData);
                                    }
                                    @Override
                                    public void onComplete(@androidx.annotation.Nullable DatabaseError databaseError, boolean b, @androidx.annotation.Nullable DataSnapshot dataSnapshot) {
                                        if(a[0]<=0){
                                            FirebaseDatabase.getInstance().getReference().child("Vendor")
                                                    .child(session.getusername())
                                                    .child("Products")
                                                    .child(foodpushid.get(3)).child("Status").setValue("InActive");
                                        }
                                    }
                                });
                            }

                              if(4<qty){
                                  final double[] a = {0};
                                  DatabaseReference dref = FirebaseDatabase.getInstance().getReference().child("Vendor")
                                          .child(session.getusername())
                                          .child("Products")
                                          .child(foodpushid.get(4)).child("Stock");

                                  dref.runTransaction(new Transaction.Handler() {
                                      @NonNull
                                      @Override
                                      public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                                          double value = 0;
                                          if (currentData.getValue() != null) {
                                              value = Double.parseDouble(currentData.getValue().toString()) - Integer.parseInt(quantity.get(4));
                                              a[0] = value;
                                          }
                                          currentData.setValue(value);
                                          return Transaction.success(currentData);
                                      }
                                      @Override
                                      public void onComplete(@androidx.annotation.Nullable DatabaseError databaseError, boolean b, @androidx.annotation.Nullable DataSnapshot dataSnapshot) {
                                          if(a[0]<=0){
                                              FirebaseDatabase.getInstance().getReference().child("Vendor")
                                                      .child(session.getusername())
                                                      .child("Products")
                                                      .child(foodpushid.get(4)).child("Status").setValue("InActive");
                                          }
                                      }
                                  });
                              }

                              if(5<qty){
                                  final double[] a = {0};
                                  DatabaseReference dref = FirebaseDatabase.getInstance().getReference().child("Vendor")
                                          .child(session.getusername())
                                          .child("Products")
                                          .child(foodpushid.get(5)).child("Stock");

                                  dref.runTransaction(new Transaction.Handler() {
                                      @NonNull
                                      @Override
                                      public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                                          double value = 0;
                                          if (currentData.getValue() != null) {
                                              value = Double.parseDouble(currentData.getValue().toString()) - Integer.parseInt(quantity.get(5));
                                              a[0] = value;
                                          }
                                          currentData.setValue(value);
                                          return Transaction.success(currentData);
                                      }
                                      @Override
                                      public void onComplete(@androidx.annotation.Nullable DatabaseError databaseError, boolean b, @androidx.annotation.Nullable DataSnapshot dataSnapshot) {
                                          if(a[0]<=0){
                                              FirebaseDatabase.getInstance().getReference().child("Vendor")
                                                      .child(session.getusername())
                                                      .child("Products")
                                                      .child(foodpushid.get(5)).child("Status").setValue("InActive");
                                          }
                                      }
                                  });
                              }

                              if(6<qty){
                                  final double[] a = {0};
                                  DatabaseReference dref = FirebaseDatabase.getInstance().getReference().child("Vendor")
                                          .child(session.getusername())
                                          .child("Products")
                                          .child(foodpushid.get(6)).child("Stock");

                                  dref.runTransaction(new Transaction.Handler() {
                                      @NonNull
                                      @Override
                                      public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                                          double value = 0;
                                          if (currentData.getValue() != null) {
                                              value = Double.parseDouble(currentData.getValue().toString()) - Integer.parseInt(quantity.get(6));
                                              a[0] = value;
                                          }
                                          currentData.setValue(value);
                                          return Transaction.success(currentData);
                                      }
                                      @Override
                                      public void onComplete(@androidx.annotation.Nullable DatabaseError databaseError, boolean b, @androidx.annotation.Nullable DataSnapshot dataSnapshot) {
                                          if(a[0]<=0){
                                              FirebaseDatabase.getInstance().getReference().child("Vendor")
                                                      .child(session.getusername())
                                                      .child("Products")
                                                      .child(foodpushid.get(6)).child("Status").setValue("InActive");
                                          }
                                      }
                                  });
                              }

                              if(7<qty){
                                  final double[] a = {0};
                                  DatabaseReference dref = FirebaseDatabase.getInstance().getReference().child("Vendor")
                                          .child(session.getusername())
                                          .child("Products")
                                          .child(foodpushid.get(7)).child("Stock");

                                  dref.runTransaction(new Transaction.Handler() {
                                      @NonNull
                                      @Override
                                      public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                                          double value = 0;
                                          if (currentData.getValue() != null) {
                                              value = Double.parseDouble(currentData.getValue().toString()) - Integer.parseInt(quantity.get(7));
                                              a[0] = value;
                                          }
                                          currentData.setValue(value);
                                          return Transaction.success(currentData);
                                      }
                                      @Override
                                      public void onComplete(@androidx.annotation.Nullable DatabaseError databaseError, boolean b, @androidx.annotation.Nullable DataSnapshot dataSnapshot) {
                                          if(a[0]<=0){
                                              FirebaseDatabase.getInstance().getReference().child("Vendor")
                                                      .child(session.getusername())
                                                      .child("Products")
                                                      .child(foodpushid.get(7)).child("Status").setValue("InActive");
                                          }
                                      }
                                  });
                              }

                              if(8<qty){
                                  final double[] a = {0};
                                  DatabaseReference dref = FirebaseDatabase.getInstance().getReference().child("Vendor")
                                          .child(session.getusername())
                                          .child("Products")
                                          .child(foodpushid.get(8)).child("Stock");

                                  dref.runTransaction(new Transaction.Handler() {
                                      @NonNull
                                      @Override
                                      public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                                          double value = 0;
                                          if (currentData.getValue() != null) {
                                              value = Double.parseDouble(currentData.getValue().toString()) - Integer.parseInt(quantity.get(8));
                                              a[0] = value;
                                          }
                                          currentData.setValue(value);
                                          return Transaction.success(currentData);
                                      }
                                      @Override
                                      public void onComplete(@androidx.annotation.Nullable DatabaseError databaseError, boolean b, @androidx.annotation.Nullable DataSnapshot dataSnapshot) {
                                          if(a[0]<=0){
                                              FirebaseDatabase.getInstance().getReference().child("Vendor")
                                                      .child(session.getusername())
                                                      .child("Products")
                                                      .child(foodpushid.get(8)).child("Status").setValue("InActive");
                                          }
                                      }
                                  });
                              }

                              if(9<qty){
                                  final double[] a = {0};
                                  DatabaseReference dref = FirebaseDatabase.getInstance().getReference().child("Vendor")
                                          .child(session.getusername())
                                          .child("Products")
                                          .child(foodpushid.get(9)).child("Stock");

                                  dref.runTransaction(new Transaction.Handler() {
                                      @NonNull
                                      @Override
                                      public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                                          double value = 0;
                                          if (currentData.getValue() != null) {
                                              value = Double.parseDouble(currentData.getValue().toString()) - Integer.parseInt(quantity.get(9));
                                              a[0] = value;
                                          }
                                          currentData.setValue(value);
                                          return Transaction.success(currentData);
                                      }
                                      @Override
                                      public void onComplete(@androidx.annotation.Nullable DatabaseError databaseError, boolean b, @androidx.annotation.Nullable DataSnapshot dataSnapshot) {
                                          if(a[0]<=0){
                                              FirebaseDatabase.getInstance().getReference().child("Vendor")
                                                      .child(session.getusername())
                                                      .child("Products")
                                                      .child(foodpushid.get(9)).child("Status").setValue("InActive");
                                          }
                                      }
                                  });
                              }

                              if(10<qty){
                                  final double[] a = {0};
                                  DatabaseReference dref = FirebaseDatabase.getInstance().getReference().child("Vendor")
                                          .child(session.getusername())
                                          .child("Products")
                                          .child(foodpushid.get(10)).child("Stock");

                                  dref.runTransaction(new Transaction.Handler() {
                                      @NonNull
                                      @Override
                                      public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                                          double value = 0;
                                          if (currentData.getValue() != null) {
                                              value = Double.parseDouble(currentData.getValue().toString()) - Integer.parseInt(quantity.get(10));
                                              a[0] = value;
                                          }
                                          currentData.setValue(value);
                                          return Transaction.success(currentData);
                                      }
                                      @Override
                                      public void onComplete(@androidx.annotation.Nullable DatabaseError databaseError, boolean b, @androidx.annotation.Nullable DataSnapshot dataSnapshot) {
                                          if(a[0]<=0){
                                              FirebaseDatabase.getInstance().getReference().child("Vendor")
                                                      .child(session.getusername())
                                                      .child("Products")
                                                      .child(foodpushid.get(10)).child("Status").setValue("InActive");
                                          }
                                      }
                                  });
                              }

                              if(11<qty){
                                  final double[] a = {0};
                                  DatabaseReference dref = FirebaseDatabase.getInstance().getReference().child("Vendor")
                                          .child(session.getusername())
                                          .child("Products")
                                          .child(foodpushid.get(11)).child("Stock");

                                  dref.runTransaction(new Transaction.Handler() {
                                      @NonNull
                                      @Override
                                      public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                                          double value = 0;
                                          if (currentData.getValue() != null) {
                                              value = Double.parseDouble(currentData.getValue().toString()) - Integer.parseInt(quantity.get(11));
                                              a[0] = value;
                                          }
                                          currentData.setValue(value);
                                          return Transaction.success(currentData);
                                      }
                                      @Override
                                      public void onComplete(@androidx.annotation.Nullable DatabaseError databaseError, boolean b, @androidx.annotation.Nullable DataSnapshot dataSnapshot) {
                                          if(a[0]<=0){
                                              FirebaseDatabase.getInstance().getReference().child("Vendor")
                                                      .child(session.getusername())
                                                      .child("Products")
                                                      .child(foodpushid.get(11)).child("Status").setValue("InActive");
                                          }
                                      }
                                  });
                              }

                              if(12<qty){
                                  final double[] a = {0};
                                  DatabaseReference dref = FirebaseDatabase.getInstance().getReference().child("Vendor")
                                          .child(session.getusername())
                                          .child("Products")
                                          .child(foodpushid.get(12)).child("Stock");

                                  dref.runTransaction(new Transaction.Handler() {
                                      @NonNull
                                      @Override
                                      public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                                          double value = 0;
                                          if (currentData.getValue() != null) {
                                              value = Double.parseDouble(currentData.getValue().toString()) - Integer.parseInt(quantity.get(12));
                                              a[0] = value;
                                          }
                                          currentData.setValue(value);
                                          return Transaction.success(currentData);
                                      }
                                      @Override
                                      public void onComplete(@androidx.annotation.Nullable DatabaseError databaseError, boolean b, @androidx.annotation.Nullable DataSnapshot dataSnapshot) {
                                          if(a[0]<=0){
                                              FirebaseDatabase.getInstance().getReference().child("Vendor")
                                                      .child(session.getusername())
                                                      .child("Products")
                                                      .child(foodpushid.get(12)).child("Status").setValue("InActive");
                                          }
                                      }
                                  });
                              }

                              if(13<qty){
                                  final double[] a = {0};
                                  DatabaseReference dref = FirebaseDatabase.getInstance().getReference().child("Vendor")
                                          .child(session.getusername())
                                          .child("Products")
                                          .child(foodpushid.get(13)).child("Stock");

                                  dref.runTransaction(new Transaction.Handler() {
                                      @NonNull
                                      @Override
                                      public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                                          double value = 0;
                                          if (currentData.getValue() != null) {
                                              value = Double.parseDouble(currentData.getValue().toString()) - Integer.parseInt(quantity.get(13));
                                              a[0] = value;
                                          }
                                          currentData.setValue(value);
                                          return Transaction.success(currentData);
                                      }
                                      @Override
                                      public void onComplete(@androidx.annotation.Nullable DatabaseError databaseError, boolean b, @androidx.annotation.Nullable DataSnapshot dataSnapshot) {
                                          if(a[0]<=0){
                                              FirebaseDatabase.getInstance().getReference().child("Vendor")
                                                      .child(session.getusername())
                                                      .child("Products")
                                                      .child(foodpushid.get(13)).child("Status").setValue("InActive");
                                          }
                                      }
                                  });
                              }

                              if(14<qty){
                                  final double[] a = {0};
                                  DatabaseReference dref = FirebaseDatabase.getInstance().getReference().child("Vendor")
                                          .child(session.getusername())
                                          .child("Products")
                                          .child(foodpushid.get(14)).child("Stock");

                                  dref.runTransaction(new Transaction.Handler() {
                                      @NonNull
                                      @Override
                                      public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                                          double value = 0;
                                          if (currentData.getValue() != null) {
                                              value = Double.parseDouble(currentData.getValue().toString()) - Integer.parseInt(quantity.get(14));
                                              a[0] = value;
                                          }
                                          currentData.setValue(value);
                                          return Transaction.success(currentData);
                                      }
                                      @Override
                                      public void onComplete(@androidx.annotation.Nullable DatabaseError databaseError, boolean b, @androidx.annotation.Nullable DataSnapshot dataSnapshot) {
                                          if(a[0]<=0){
                                              FirebaseDatabase.getInstance().getReference().child("Vendor")
                                                      .child(session.getusername())
                                                      .child("Products")
                                                      .child(foodpushid.get(14)).child("Status").setValue("InActive");
                                          }
                                      }
                                  });
                              }

                              if(15<qty){
                                  final double[] a = {0};
                                  DatabaseReference dref = FirebaseDatabase.getInstance().getReference().child("Vendor")
                                          .child(session.getusername())
                                          .child("Products")
                                          .child(foodpushid.get(15)).child("Stock");

                                  dref.runTransaction(new Transaction.Handler() {
                                      @NonNull
                                      @Override
                                      public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                                          double value = 0;
                                          if (currentData.getValue() != null) {
                                              value = Double.parseDouble(currentData.getValue().toString()) - Integer.parseInt(quantity.get(15));
                                              a[0] = value;
                                          }
                                          currentData.setValue(value);
                                          return Transaction.success(currentData);
                                      }
                                      @Override
                                      public void onComplete(@androidx.annotation.Nullable DatabaseError databaseError, boolean b, @androidx.annotation.Nullable DataSnapshot dataSnapshot) {
                                          if(a[0]<=0){
                                              FirebaseDatabase.getInstance().getReference().child("Vendor")
                                                      .child(session.getusername())
                                                      .child("Products")
                                                      .child(foodpushid.get(15)).child("Status").setValue("InActive");
                                          }
                                      }
                                  });
                              }

                              if(16<qty){
                                  final double[] a = {0};
                                  DatabaseReference dref = FirebaseDatabase.getInstance().getReference().child("Vendor")
                                          .child(session.getusername())
                                          .child("Products")
                                          .child(foodpushid.get(16)).child("Stock");

                                  dref.runTransaction(new Transaction.Handler() {
                                      @NonNull
                                      @Override
                                      public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                                          double value = 0;
                                          if (currentData.getValue() != null) {
                                              value = Double.parseDouble(currentData.getValue().toString()) - Integer.parseInt(quantity.get(16));
                                              a[0] = value;
                                          }
                                          currentData.setValue(value);
                                          return Transaction.success(currentData);
                                      }
                                      @Override
                                      public void onComplete(@androidx.annotation.Nullable DatabaseError databaseError, boolean b, @androidx.annotation.Nullable DataSnapshot dataSnapshot) {
                                          if(a[0]<=0){
                                              FirebaseDatabase.getInstance().getReference().child("Vendor")
                                                      .child(session.getusername())
                                                      .child("Products")
                                                      .child(foodpushid.get(16)).child("Status").setValue("InActive");
                                          }
                                      }
                                  });
                              }

                              if(17<qty){
                                  final double[] a = {0};
                                  DatabaseReference dref = FirebaseDatabase.getInstance().getReference().child("Vendor")
                                          .child(session.getusername())
                                          .child("Products")
                                          .child(foodpushid.get(17)).child("Stock");

                                  dref.runTransaction(new Transaction.Handler() {
                                      @NonNull
                                      @Override
                                      public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                                          double value = 0;
                                          if (currentData.getValue() != null) {
                                              value = Double.parseDouble(currentData.getValue().toString()) - Integer.parseInt(quantity.get(17));
                                              a[0] = value;
                                          }
                                          currentData.setValue(value);
                                          return Transaction.success(currentData);
                                      }
                                      @Override
                                      public void onComplete(@androidx.annotation.Nullable DatabaseError databaseError, boolean b, @androidx.annotation.Nullable DataSnapshot dataSnapshot) {
                                          if(a[0]<=0){
                                              FirebaseDatabase.getInstance().getReference().child("Vendor")
                                                      .child(session.getusername())
                                                      .child("Products")
                                                      .child(foodpushid.get(17)).child("Status").setValue("InActive");
                                          }
                                      }
                                  });
                              }

                              if(18<qty){
                                  final double[] a = {0};
                                  DatabaseReference dref = FirebaseDatabase.getInstance().getReference().child("Vendor")
                                          .child(session.getusername())
                                          .child("Products")
                                          .child(foodpushid.get(18)).child("Stock");

                                  dref.runTransaction(new Transaction.Handler() {
                                      @NonNull
                                      @Override
                                      public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                                          double value = 0;
                                          if (currentData.getValue() != null) {
                                              value = Double.parseDouble(currentData.getValue().toString()) - Integer.parseInt(quantity.get(18));
                                              a[0] = value;
                                          }
                                          currentData.setValue(value);
                                          return Transaction.success(currentData);
                                      }
                                      @Override
                                      public void onComplete(@androidx.annotation.Nullable DatabaseError databaseError, boolean b, @androidx.annotation.Nullable DataSnapshot dataSnapshot) {
                                          if(a[0]<=0){
                                              FirebaseDatabase.getInstance().getReference().child("Vendor")
                                                      .child(session.getusername())
                                                      .child("Products")
                                                      .child(foodpushid.get(18)).child("Status").setValue("InActive");
                                          }
                                      }
                                  });
                              }

                              if(19<qty){
                                  final double[] a = {0};
                                  DatabaseReference dref = FirebaseDatabase.getInstance().getReference().child("Vendor")
                                          .child(session.getusername())
                                          .child("Products")
                                          .child(foodpushid.get(19)).child("Stock");

                                  dref.runTransaction(new Transaction.Handler() {
                                      @NonNull
                                      @Override
                                      public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                                          double value = 0;
                                          if (currentData.getValue() != null) {
                                              value = Double.parseDouble(currentData.getValue().toString()) - Integer.parseInt(quantity.get(19));
                                              a[0] = value;
                                          }
                                          currentData.setValue(value);
                                          return Transaction.success(currentData);
                                      }
                                      @Override
                                      public void onComplete(@androidx.annotation.Nullable DatabaseError databaseError, boolean b, @androidx.annotation.Nullable DataSnapshot dataSnapshot) {
                                          if(a[0]<=0){
                                              FirebaseDatabase.getInstance().getReference().child("Vendor")
                                                      .child(session.getusername())
                                                      .child("Products")
                                                      .child(foodpushid.get(19)).child("Status").setValue("InActive");
                                          }
                                      }
                                  });
                              }

                              if(20<qty){
                                  final double[] a = {0};
                                  DatabaseReference dref = FirebaseDatabase.getInstance().getReference().child("Vendor")
                                          .child(session.getusername())
                                          .child("Products")
                                          .child(foodpushid.get(20)).child("Stock");

                                  dref.runTransaction(new Transaction.Handler() {
                                      @NonNull
                                      @Override
                                      public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                                          double value = 0;
                                          if (currentData.getValue() != null) {
                                              value = Double.parseDouble(currentData.getValue().toString()) - Integer.parseInt(quantity.get(20));
                                              a[0] = value;
                                          }
                                          currentData.setValue(value);
                                          return Transaction.success(currentData);
                                      }
                                      @Override
                                      public void onComplete(@androidx.annotation.Nullable DatabaseError databaseError, boolean b, @androidx.annotation.Nullable DataSnapshot dataSnapshot) {
                                          if(a[0]<=0){
                                              FirebaseDatabase.getInstance().getReference().child("Vendor")
                                                      .child(session.getusername())
                                                      .child("Products")
                                                      .child(foodpushid.get(20)).child("Status").setValue("InActive");
                                          }
                                      }
                                  });
                              }


                          if (getActivity() != null)
                              getActivity().onBackPressed();

                      }
                  }

                  @Override
                  public void onCancelled(@NonNull DatabaseError databaseError) {

                  }
              });

    }
}
