package hurrys.corp.vendor.Fragments;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;
import hurrys.corp.vendor.Configurations.Session;
import hurrys.corp.vendor.Models.Orders.Orders;
import hurrys.corp.vendor.Models.Orders.ViewHolder;
import hurrys.corp.vendor.R;

public class Dashboard extends Fragment {

    private ImageView i1,i2,i3;


    private Session sessions;

    private LinearLayout z1,z2,z3,offline,online,onlinepast;
    private TextView t1,t2,t3,earnings,earnings1,earnings2,paymentdate,paymentdate1;
    private View s1,s2,s3;
    private Button go;
    private RecyclerView recyclerView;

    public Dashboard() {
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
        View v=inflater.inflate(R.layout.fragment_dashboard, container, false);

        LinearLayout bottomnavigation=(getActivity()).findViewById(R.id.bottomnavigation);
        bottomnavigation.setVisibility(View.VISIBLE);


        i1=getActivity().findViewById(R.id.i1);
        i2=getActivity().findViewById(R.id.i2);
        i3=getActivity().findViewById(R.id.i3);


        i1.setImageResource(R.drawable.hb1);
        i2.setImageResource(R.drawable.b2);
        i3.setImageResource(R.drawable.b3);

        z1=v.findViewById(R.id.z1);
        z2=v.findViewById(R.id.z2);
        z3=v.findViewById(R.id.z3);
        t1=v.findViewById(R.id.t1);
        t2=v.findViewById(R.id.t2);
        t3=v.findViewById(R.id.t3);
        s1=v.findViewById(R.id.s1);
        s2=v.findViewById(R.id.s2);
        s3=v.findViewById(R.id.s3);
        go=v.findViewById(R.id.go);
        offline=v.findViewById(R.id.offline);
        online=v.findViewById(R.id.online);
        onlinepast=v.findViewById(R.id.onlinepast);
        earnings=v.findViewById(R.id.earnings);
        earnings1=v.findViewById(R.id.earnings1);
        earnings2=v.findViewById(R.id.earnings2);
        recyclerView=v.findViewById(R.id.recyclerView);
        paymentdate=v.findViewById(R.id.paymentdate);
        paymentdate1=v.findViewById(R.id.paymentdate1);

        sessions =new Session(getActivity());

        offline.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);


        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mLayoutManager);

//        t1.setTextColor(Color.parseColor("#00B246"));
//        t2.setTextColor(Color.parseColor("#808080"));
//        t3.setTextColor(Color.parseColor("#808080"));
//        s1.setBackgroundColor(Color.parseColor("#00B246"));
//        s2.setBackgroundColor(Color.parseColor("#eaeaea"));
//        s3.setBackgroundColor(Color.parseColor("#eaeaea"));





        z1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sessions.getstatus().equals("Active")){
                    recyclerView.setVisibility(View.VISIBLE);
                    load();
                    offline.setVisibility(View.GONE);
                }
                else{
                    offline.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
                sessions.setselection("New");
                t1.setTextColor(Color.parseColor("#00B246"));
                t2.setTextColor(Color.parseColor("#808080"));
                t3.setTextColor(Color.parseColor("#808080"));
                s1.setBackgroundColor(Color.parseColor("#00B246"));
                s2.setBackgroundColor(Color.parseColor("#eaeaea"));
                s3.setBackgroundColor(Color.parseColor("#eaeaea"));


            }
        });

        z2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                recyclerView.setVisibility(View.VISIBLE);
                loadpast();
                offline.setVisibility(View.GONE);

                sessions.setselection("Past");
                t2.setTextColor(Color.parseColor("#00B246"));
                t1.setTextColor(Color.parseColor("#808080"));
                t3.setTextColor(Color.parseColor("#808080"));
                s2.setBackgroundColor(Color.parseColor("#00B246"));
                s1.setBackgroundColor(Color.parseColor("#eaeaea"));
                s3.setBackgroundColor(Color.parseColor("#eaeaea"));
            }
        });

        z3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                recyclerView.setVisibility(View.VISIBLE);
                loadOngoing();
                offline.setVisibility(View.GONE);
                sessions.setselection("On");
                t3.setTextColor(Color.parseColor("#00B246"));
                t1.setTextColor(Color.parseColor("#808080"));
                t2.setTextColor(Color.parseColor("#808080"));
                s3.setBackgroundColor(Color.parseColor("#00B246"));
                s1.setBackgroundColor(Color.parseColor("#eaeaea"));
                s2.setBackgroundColor(Color.parseColor("#eaeaea"));
            }
        });


        FirebaseDatabase.getInstance().getReference().child("Vendor").child(sessions.getusername())
                .child("Status")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            if(dataSnapshot.getValue().toString().equals("Active")){
                                sessions.setstatus("Active");
                                recyclerView.setVisibility(View.VISIBLE);
                                load();
                                offline.setVisibility(View.GONE);
                                if(sessions.getselection().equals("New")){
                                    if(sessions.getstatus().equals("Active")){
                                        recyclerView.setVisibility(View.VISIBLE);
                                        load();
                                        offline.setVisibility(View.GONE);
                                    }
                                    else{
                                        offline.setVisibility(View.VISIBLE);
                                        recyclerView.setVisibility(View.GONE);
                                    }
                                    sessions.setselection("New");
                                    t1.setTextColor(Color.parseColor("#00B246"));
                                    t2.setTextColor(Color.parseColor("#808080"));
                                    t3.setTextColor(Color.parseColor("#808080"));
                                    s1.setBackgroundColor(Color.parseColor("#00B246"));
                                    s2.setBackgroundColor(Color.parseColor("#eaeaea"));
                                    s3.setBackgroundColor(Color.parseColor("#eaeaea"));
                                }
                                else if(sessions.getselection().equals("On")){

                                    recyclerView.setVisibility(View.VISIBLE);
                                    loadOngoing();
                                    offline.setVisibility(View.GONE);

                                    t3.setTextColor(Color.parseColor("#00B246"));
                                    t1.setTextColor(Color.parseColor("#808080"));
                                    t2.setTextColor(Color.parseColor("#808080"));
                                    s3.setBackgroundColor(Color.parseColor("#00B246"));
                                    s1.setBackgroundColor(Color.parseColor("#eaeaea"));
                                    s2.setBackgroundColor(Color.parseColor("#eaeaea"));

                                }
                                else if(sessions.getselection().equals("Past")){
                                    recyclerView.setVisibility(View.VISIBLE);
                                    loadpast();
                                    offline.setVisibility(View.GONE);

                                    t2.setTextColor(Color.parseColor("#00B246"));
                                    t1.setTextColor(Color.parseColor("#808080"));
                                    t3.setTextColor(Color.parseColor("#808080"));
                                    s2.setBackgroundColor(Color.parseColor("#00B246"));
                                    s1.setBackgroundColor(Color.parseColor("#eaeaea"));
                                    s3.setBackgroundColor(Color.parseColor("#eaeaea"));
                                }
                            }
                            else{
                                sessions.setstatus("InActive");
                                offline.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                                if(sessions.getselection().equals("New")){
                                    if(sessions.getstatus().equals("Active")){
                                        recyclerView.setVisibility(View.VISIBLE);
                                        load();
                                        offline.setVisibility(View.GONE);
                                    }
                                    else{
                                        offline.setVisibility(View.VISIBLE);
                                        recyclerView.setVisibility(View.GONE);
                                    }
                                    sessions.setselection("New");
                                    t1.setTextColor(Color.parseColor("#00B246"));
                                    t2.setTextColor(Color.parseColor("#808080"));
                                    t3.setTextColor(Color.parseColor("#808080"));
                                    s1.setBackgroundColor(Color.parseColor("#00B246"));
                                    s2.setBackgroundColor(Color.parseColor("#eaeaea"));
                                    s3.setBackgroundColor(Color.parseColor("#eaeaea"));
                                }
                                else if(sessions.getselection().equals("On")){

                                    recyclerView.setVisibility(View.VISIBLE);
                                    loadOngoing();
                                    offline.setVisibility(View.GONE);

                                    t3.setTextColor(Color.parseColor("#00B246"));
                                    t1.setTextColor(Color.parseColor("#808080"));
                                    t2.setTextColor(Color.parseColor("#808080"));
                                    s3.setBackgroundColor(Color.parseColor("#00B246"));
                                    s1.setBackgroundColor(Color.parseColor("#eaeaea"));
                                    s2.setBackgroundColor(Color.parseColor("#eaeaea"));

                                }
                                else if(sessions.getselection().equals("Past")){
                                    recyclerView.setVisibility(View.VISIBLE);
                                    loadpast();
                                    offline.setVisibility(View.GONE);

                                    t2.setTextColor(Color.parseColor("#00B246"));
                                    t1.setTextColor(Color.parseColor("#808080"));
                                    t3.setTextColor(Color.parseColor("#808080"));
                                    s2.setBackgroundColor(Color.parseColor("#00B246"));
                                    s1.setBackgroundColor(Color.parseColor("#eaeaea"));
                                    s3.setBackgroundColor(Color.parseColor("#eaeaea"));
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        FirebaseDatabase.getInstance().getReference().child("Masters")
                .child("PaymentDate")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            String a = dataSnapshot.getValue().toString();
                            if(a.equals("1")) {
                                LocalDate date = new LocalDate(System.currentTimeMillis());
                                Period period = Period.fieldDifference(date, date.withDayOfWeek(DateTimeConstants.MONDAY));
                                int days = period.getDays();
                                if (days < 1) {
                                    days = days + 7;
                                }
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd, MMM yyyy");
                                String a1 =""+ date.plusDays(days);

                                try {
                                    Date date1=new SimpleDateFormat("yyyy-MM-dd").parse(a1);
                                    paymentdate.setText(simpleDateFormat.format(date1));
                                    paymentdate1.setText(simpleDateFormat.format(date1));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                            }
                            else if(a.equals("2")) {
                                LocalDate date = new LocalDate(System.currentTimeMillis());
                                Period period = Period.fieldDifference(date, date.withDayOfWeek(DateTimeConstants.TUESDAY));
                                int days = period.getDays();
                                if (days < 1) {
                                    days = days + 7;
                                }
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd, MMM yyyy");
                                String a1 =""+ date.plusDays(days);

                                try {
                                    Date date1=new SimpleDateFormat("yyyy-MM-dd").parse(a1);
                                    paymentdate.setText(simpleDateFormat.format(date1));
                                    paymentdate1.setText(simpleDateFormat.format(date1));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                            }
                            else  if(a.equals("3")) {
                                LocalDate date = new LocalDate(System.currentTimeMillis());
                                Period period = Period.fieldDifference(date, date.withDayOfWeek(DateTimeConstants.WEDNESDAY));
                                int days = period.getDays();
                                if (days < 1) {
                                    days = days + 7;
                                }
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd, MMM yyyy");
                                String a1 =""+ date.plusDays(days);

                                try {
                                    Date date1=new SimpleDateFormat("yyyy-MM-dd").parse(a1);
                                    paymentdate.setText(simpleDateFormat.format(date1));
                                    paymentdate1.setText(simpleDateFormat.format(date1));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                            }
                            else  if(a.equals("4")) {
                                LocalDate date = new LocalDate(System.currentTimeMillis());
                                Period period = Period.fieldDifference(date, date.withDayOfWeek(DateTimeConstants.THURSDAY));
                                int days = period.getDays();
                                if (days < 1) {
                                    days = days + 7;
                                }
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd, MMM yyyy");
                                String a1 =""+ date.plusDays(days);

                                try {
                                    Date date1=new SimpleDateFormat("yyyy-MM-dd").parse(a1);
                                    paymentdate.setText(simpleDateFormat.format(date1));
                                    paymentdate1.setText(simpleDateFormat.format(date1));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                            }
                            else  if(a.equals("5")) {
                                LocalDate date = new LocalDate(System.currentTimeMillis());
                                Period period = Period.fieldDifference(date, date.withDayOfWeek(DateTimeConstants.FRIDAY));
                                int days = period.getDays();
                                if (days < 1) {
                                    days = days + 7;
                                }
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd, MMM yyyy");
                                String a1 =""+ date.plusDays(days);

                                try {
                                    Date date1=new SimpleDateFormat("yyyy-MM-dd").parse(a1);
                                    paymentdate.setText(simpleDateFormat.format(date1));
                                    paymentdate1.setText(simpleDateFormat.format(date1));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                            }
                            else  if(a.equals("6")) {
                                LocalDate date = new LocalDate(System.currentTimeMillis());
                                Period period = Period.fieldDifference(date, date.withDayOfWeek(DateTimeConstants.SATURDAY));
                                int days = period.getDays();
                                if (days < 1) {
                                    days = days + 7;
                                }
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd, MMM yyyy");
                                String a1 =""+ date.plusDays(days);

                                try {
                                    Date date1=new SimpleDateFormat("yyyy-MM-dd").parse(a1);
                                    paymentdate.setText(simpleDateFormat.format(date1));
                                    paymentdate1.setText(simpleDateFormat.format(date1));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                            }
                            else  if(a.equals("7")) {
                                LocalDate date = new LocalDate(System.currentTimeMillis());
                                Period period = Period.fieldDifference(date, date.withDayOfWeek(DateTimeConstants.SUNDAY));
                                int days = period.getDays();
                                if (days < 1) {
                                    days = days + 7;
                                }
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd, MMM yyyy");
                                String a1 =""+ date.plusDays(days);

                                try {
                                    Date date1=new SimpleDateFormat("yyyy-MM-dd").parse(a1);
                                    paymentdate.setText(simpleDateFormat.format(date1));
                                    paymentdate1.setText(simpleDateFormat.format(date1));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


        final DecimalFormat form = new DecimalFormat("0.00");

        FirebaseDatabase.getInstance().getReference().child("Vendor")
                .child(sessions.getusername())
                .child("PendingAmount")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists())
                            earnings.setText("\u00a3"+form.format(Double.parseDouble(dataSnapshot.getValue().toString())));
                            earnings1.setText("\u00a3"+form.format(Double.parseDouble(dataSnapshot.getValue().toString())));
                            earnings2.setText("\u00a3"+form.format(Double.parseDouble(dataSnapshot.getValue().toString())));

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

//        FirebaseDatabase.getInstance().getReference().child("Vendor").child(sessions.getusername())
//                .child("Category")
//                .addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        if(dataSnapshot)
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });

        FirebaseDatabase.getInstance().getReference().child("Vendor").child(sessions.getusername())
                .child("Address")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            sessions.setaddress(dataSnapshot.getValue().toString());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseDatabase.getInstance().getReference().child("Vendor").child(sessions.getusername()).child("Status").setValue("Active");
                sessions.setstatus("Active");

                offline.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                load();

            }
        });


        return v;

    }


    public void load(){

        final Query firebasequery = FirebaseDatabase.getInstance().getReference().child("Orders").orderByChild("SellerStatus").equalTo(sessions.getusername());

        final FirebaseRecyclerAdapter<hurrys.corp.vendor.Models.Orders.Orders, ViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<hurrys.corp.vendor.Models.Orders.Orders, ViewHolder>(
                        hurrys.corp.vendor.Models.Orders.Orders.class,
                        R.layout.new_orders_row,
                        ViewHolder.class,
                        firebasequery
                ) {
                    @Override
                    protected void populateViewHolder(ViewHolder viewHolder, hurrys.corp.vendor.Models.Orders.Orders order, int position) {
                        viewHolder.setDetails(getContext(),order.CName,order.Address,order.Subtotal,order.Pushid,order.OrderNo,order.OrderDateTime,order.Qty,order.Payment,order.Status,order.DeliveryName,order.DeliveryNumber,order.DeliveryImage,order.Taxes,order.DeliveryCharges,order.DeliverySelection);
                    }

                    @Override
                    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                        ViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);
                        viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
                            @Override
                            public void onItemClick(View v, int position) {


                                TextView number=v.findViewById(R.id.number);

                                ImageView call=v.findViewById(R.id.call);

                                call.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(Intent.ACTION_DIAL);
                                        intent.setData(Uri.parse("tel:"+number.getText().toString()));
                                        startActivity(intent);
                                    }
                                });

                                TextView pushid = v.findViewById(R.id.pushid);
                                Bundle bundle = new Bundle();
                                Fragment fragment = new OrderDetailsFragment();
                                bundle.putString("pushid", pushid.getText().toString());
                                fragment.setArguments(bundle);
                                if(getActivity()!=null) {
                                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                    fragmentManager.beginTransaction()
                                            .addToBackStack(null)
                                            .replace(R.id.frame_container, fragment).commitAllowingStateLoss();
                                }

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
                        if(getItemCount()==0){
                            onlinepast.setVisibility(View.GONE);
                            online.setVisibility(View.VISIBLE);
                            offline.setVisibility(View.GONE);
                        }
                        else{
                            onlinepast.setVisibility(View.GONE);
                            online.setVisibility(View.GONE);
                            offline.setVisibility(View.GONE);
                        }
                    }
                };

        recyclerView.setAdapter(firebaseRecyclerAdapter);
        recyclerView.setVisibility(View.VISIBLE);

}

    public void loadOngoing(){

        final Query firebasequery = FirebaseDatabase.getInstance().getReference().child("Orders").orderByChild("SellerStatus").equalTo(sessions.getusername());

        final FirebaseRecyclerAdapter<hurrys.corp.vendor.Models.Orders.Orders, ViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<hurrys.corp.vendor.Models.Orders.Orders, ViewHolder>(
                        hurrys.corp.vendor.Models.Orders.Orders.class,
                        R.layout.new_orders_row,
                        ViewHolder.class,
                        firebasequery
                ) {
                    @Override
                    protected void populateViewHolder(ViewHolder viewHolder, hurrys.corp.vendor.Models.Orders.Orders order, int position) {
                        viewHolder.setDetailsOngoing(getContext(),order.CName,order.Address,order.Subtotal,order.Pushid,order.OrderNo,order.OrderDateTime,order.Qty,order.Payment,order.Status,order.DeliveryName,order.DeliveryNumber,order.DeliveryImage,order.Taxes,order.DeliveryCharges,order.DeliverySelection);
                    }

                    @Override
                    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                        ViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);
                        viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
                            @Override
                            public void onItemClick(View v, int position) {


                                TextView number=v.findViewById(R.id.number);

                                ImageView call=v.findViewById(R.id.call);

                                call.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(Intent.ACTION_DIAL);
                                        intent.setData(Uri.parse("tel:"+number.getText().toString()));
                                        startActivity(intent);
                                    }
                                });

                                TextView pushid = v.findViewById(R.id.pushid);
                                Bundle bundle = new Bundle();
                                Fragment fragment = new OrderDetailsFragment();
                                bundle.putString("pushid", pushid.getText().toString());
                                fragment.setArguments(bundle);
                                if(getActivity()!=null) {
                                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                    fragmentManager.beginTransaction()
                                            .addToBackStack(null)
                                            .replace(R.id.frame_container, fragment).commitAllowingStateLoss();
                                }

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
                        if(getItemCount()==0){
                            onlinepast.setVisibility(View.GONE);
                            online.setVisibility(View.VISIBLE);
                            offline.setVisibility(View.GONE);
                        }
                        else{
                            onlinepast.setVisibility(View.GONE);
                            online.setVisibility(View.GONE);
                            offline.setVisibility(View.GONE);
                        }
                    }
                };

        recyclerView.setAdapter(firebaseRecyclerAdapter);
        recyclerView.setVisibility(View.VISIBLE);

    }

    public void loadpast(){

        final Query firebasequery = FirebaseDatabase.getInstance().getReference().child("Orders").orderByChild("Seller").equalTo(sessions.getusername());

        final FirebaseRecyclerAdapter<hurrys.corp.vendor.Models.Orders.Orders, ViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<hurrys.corp.vendor.Models.Orders.Orders, ViewHolder>(
                        hurrys.corp.vendor.Models.Orders.Orders.class,
                        R.layout.orders_row,
                        ViewHolder.class,
                        firebasequery
                ) {
                    @Override
                    protected void populateViewHolder(ViewHolder viewHolder, hurrys.corp.vendor.Models.Orders.Orders order, int position) {
                        viewHolder.setDetails1(getContext(),order.CName,order.Address,order.Subtotal,order.Pushid,order.OrderNo,order.OrderDateTime,order.Qty,order.Payment,order.Status,order.Taxes,order.DeliveryCharges,order.DeliverySelection);
                    }

                    @Override
                    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                        ViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);
                        viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
                            @Override
                            public void onItemClick(View v, int position) {


                                TextView pushid = v.findViewById(R.id.pushid);
                                TextView status = v.findViewById(R.id.status);

                                if(status.getText().toString().equals("DELIVERED")) {
                                    Bundle bundle = new Bundle();
                                    Fragment fragment = new CompleteOrderDetails();
                                    bundle.putString("pushid", pushid.getText().toString());
                                    fragment.setArguments(bundle);
                                    if (getActivity() != null) {
                                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                        fragmentManager.beginTransaction()
                                                .addToBackStack(null)
                                                .replace(R.id.frame_container, fragment).commitAllowingStateLoss();
                                    }
                                }
                                else{
                                    Bundle bundle = new Bundle();
                                    Fragment fragment = new CancelledOrders();
                                    bundle.putString("pushid", pushid.getText().toString());
                                    fragment.setArguments(bundle);
                                    if (getActivity() != null) {
                                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                        fragmentManager.beginTransaction()
                                                .addToBackStack(null)
                                                .replace(R.id.frame_container, fragment).commitAllowingStateLoss();
                                    }
                                }

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
                        if(getItemCount()==0){
                            onlinepast.setVisibility(View.VISIBLE);
                            online.setVisibility(View.GONE);
                            offline.setVisibility(View.GONE);
                        }
                        else{
                            onlinepast.setVisibility(View.GONE);
                            online.setVisibility(View.GONE);
                            offline.setVisibility(View.GONE);
                        }
                    }
                };

        recyclerView.setAdapter(firebaseRecyclerAdapter);
        recyclerView.setVisibility(View.VISIBLE);
    }
}
