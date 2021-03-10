package hurrys.corp.vendor.Fragments;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.Calendar;

import hurrys.corp.vendor.Configurations.Session;
import hurrys.corp.vendor.Models.Orders.Orders;
import hurrys.corp.vendor.Models.Orders.ViewHolder;
import hurrys.corp.vendor.R;


public class PastOrders extends Fragment {



    public PastOrders() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    TextView sdate,edate;
    RecyclerView recycylerView;
    Session session;

    private DatePickerDialog datePickerDialog;
    private Calendar calendar;
    private int year,month,dayofmonth;
    private int mHour;
    private int mMinute;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_past_orders2, container, false);

        ImageView back = v.findViewById(R.id.back);
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

        sdate = v.findViewById(R.id.sdate);
        edate = v.findViewById(R.id.edate);
        recycylerView = v.findViewById(R.id.recycylerView);
        session =new Session(getActivity());
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        recycylerView.setLayoutManager(mLayoutManager);



        loadpast();

        sdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                calendar= Calendar.getInstance();
                year=calendar.get(Calendar.YEAR);
                month=calendar.get(Calendar.MONTH);
                dayofmonth=calendar.get(Calendar.DAY_OF_MONTH);

                if(getContext()!=null) {
                    datePickerDialog = new DatePickerDialog(getContext(),
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                    String day1, month1;
                                    if (day <= 9)
                                        day1 = "0" + day;
                                    else {
                                        day1 = Integer.toString(day);
                                    }
                                    month++;
                                    if (month <= 9)
                                        month1 = "0" + month;
                                    else
                                        month1 = Integer.toString(month);

                                    sdate.setText(day1 + "-" + (month1) + "-" + year);
                                    loadpast();
                                    final Calendar c = Calendar.getInstance();
                                    mHour = c.get(Calendar.HOUR_OF_DAY);
                                    mMinute = c.get(Calendar.MINUTE);

                                }
                            }, year, month, dayofmonth);
                    datePickerDialog.setTitle("Please select date");
                    datePickerDialog.show();
                }
            }
        });


        edate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                calendar= Calendar.getInstance();
                year=calendar.get(Calendar.YEAR);
                month=calendar.get(Calendar.MONTH);
                dayofmonth=calendar.get(Calendar.DAY_OF_MONTH);
                if(getContext()!=null) {
                    datePickerDialog = new DatePickerDialog(getContext(),
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                    String day1, month1;
                                    if (day <= 9)
                                        day1 = "0" + day;
                                    else {
                                        day1 = Integer.toString(day);
                                    }
                                    month++;
                                    if (month <= 9)
                                        month1 = "0" + month;
                                    else
                                        month1 = Integer.toString(month);

                                    edate.setText(day1 + "-" + (month1) + "-" + year);
                                    loadpast();
                                    final Calendar c = Calendar.getInstance();
                                    mHour = c.get(Calendar.HOUR_OF_DAY);
                                    mMinute = c.get(Calendar.MINUTE);


                                }
                            }, year, month, dayofmonth);
                    datePickerDialog.setTitle("Please select date");
                    datePickerDialog.show();
                }
            }
        });

        return v;
    }


    public void loadpast(){

        final Query firebasequery = FirebaseDatabase.getInstance().getReference().child("Orders").orderByChild("Seller").equalTo(session.getusername());

        final FirebaseRecyclerAdapter<Orders, ViewHolder> firebaseRecyclerAdapter =
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

                };

        recycylerView.setAdapter(firebaseRecyclerAdapter);
        recycylerView.setVisibility(View.VISIBLE);
    }
}