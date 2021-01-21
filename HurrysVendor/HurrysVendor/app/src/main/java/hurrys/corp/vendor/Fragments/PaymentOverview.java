package hurrys.corp.vendor.Fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.Calendar;

import hurrys.corp.vendor.Configurations.Session;
import hurrys.corp.vendor.Models.Payment.Payment;
import hurrys.corp.vendor.Models.Payment.ViewHolder;
import hurrys.corp.vendor.R;

public class PaymentOverview extends Fragment {

    public PaymentOverview() {
        // Required empty public constructor
    }

    private RecyclerView mRecyclerView;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mref;
    private Session sessions;

    TextView sdate,edate;

    DatePickerDialog datePickerDialog;
    Calendar calendar;
    int year,month,dayofmonth;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_payment_overview, container, false);
        LinearLayout bottomnavigation=(getActivity()).findViewById(R.id.bottomnavigation);
        bottomnavigation.setVisibility(View.GONE);

        ImageView back = v.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });


        sdate = v.findViewById(R.id.sdate);
        edate = v.findViewById(R.id.edate);



        sdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                View v = getActivity().getCurrentFocus();
                if (v != null) {
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }


                calendar= Calendar.getInstance();
                year=calendar.get(Calendar.YEAR);
                month=calendar.get(Calendar.MONTH);
                dayofmonth=calendar.get(Calendar.DAY_OF_MONTH);

                datePickerDialog=new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                String day1,month1;
                                if(day<=9)
                                    day1="0"+day;
                                else{
                                    day1=Integer.toString(day);
                                }
                                month++;
                                if(month<9)
                                    month1="0"+month;
                                else
                                    month1=Integer.toString(month);

                                sdate.setText(day1+"-"+(month1)+"-"+year);
                                generate();

                            }
                        },year,month,dayofmonth);
                datePickerDialog.setTitle("Please select date");
                // TODO Hide Future Date Here
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

                // TODO Hide Past Date Here
//                  mDatePicker.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.show();

            }
        });


        edate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                View v = getActivity().getCurrentFocus();
                if (v != null) {
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }


                calendar=Calendar.getInstance();
                year=calendar.get(Calendar.YEAR);
                month=calendar.get(Calendar.MONTH);
                dayofmonth=calendar.get(Calendar.DAY_OF_MONTH);

                datePickerDialog=new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                String day1,month1;
                                if(day<=9)
                                    day1="0"+day;
                                else{
                                    day1=Integer.toString(day);
                                }
                                month++;
                                if(month<9)
                                    month1="0"+month;
                                else
                                    month1=Integer.toString(month);

                                edate.setText(day1+"-"+(month1)+"-"+year);
                                generate();

                            }
                        },year,month,dayofmonth);
                datePickerDialog.setTitle("Please select date");
                // TODO Hide Future Date Here
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

                // TODO Hide Past Date Here
//                  mDatePicker.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.show();

            }
        });



        sessions = new Session(getContext());
        mRecyclerView = v.findViewById(R.id.recycylerView);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mref = mFirebaseDatabase.getReference().child("Vendor").child(sessions.getusername()).child("Transactions");



        return v;
    }


    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Payment, ViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Payment, ViewHolder>(
                        Payment.class,
                        R.layout.payment_row,
                        ViewHolder.class,
                        mref
                ) {
                    @Override
                    protected void populateViewHolder(ViewHolder viewHolder, Payment payment, int position) {
                        viewHolder.setDetails(getContext(),payment.PushId,payment.UserId,payment.UserBalance,payment.Date,payment.Amount,payment.Generated,payment.TransactionType,payment.TransactionName,payment.TransactionId,payment.Status,payment.OrderPushId);

                    }

                    @Override
                    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                        ViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);
                        viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
                            @Override
                            public void onItemClick(View v, int position) {


                                TextView orderspushid = v.findViewById(R.id.orderpushid);
                                TextView date = v.findViewById(R.id.date);
                                TextView orderid = v.findViewById(R.id.orderid);

                                if(!orderid.getText().toString().equals("Settlement")) {
                                    Bundle bundle = new Bundle();
                                    CompleteOrderDetails fragment = new CompleteOrderDetails();
                                    bundle.putString("pushid", orderspushid.getText().toString());
                                    bundle.putString("date", date.getText().toString());
                                    bundle.putString("orderno", orderid.getText().toString());
                                    fragment.setArguments(bundle);
                                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                    fragmentManager.beginTransaction()
                                            .addToBackStack(null)
                                            .replace(R.id.frame_container, fragment).commit();
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
                            Toast.makeText(getContext(),"No Payments Found",Toast.LENGTH_LONG).show();
                        }
                    }


                };

        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }


    public void generate(){

        String a[]=sdate.getText().toString().split("-");
        String b[]=edate.getText().toString().split("-");

        Query ref=mref.orderByChild("Date").startAt(a[2]+"-"+a[1]+"-"+a[0]).endAt(b[2]+"-"+b[1]+"-"+b[0]);


        FirebaseRecyclerAdapter<Payment, ViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Payment, ViewHolder>(
                        Payment.class,
                        R.layout.payment_row,
                        ViewHolder.class,
                        ref
                ) {
                    @Override
                    protected void populateViewHolder(ViewHolder viewHolder, Payment payment, int position) {
                        viewHolder.setDetails(getContext(),payment.PushId,payment.UserId,payment.UserBalance,payment.Date,payment.Amount,payment.Generated,payment.TransactionType,payment.TransactionName,payment.TransactionId,payment.Status,payment.OrderPushId);

                    }

                    @Override
                    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                        ViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);
                        viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
                            @Override
                            public void onItemClick(View v, int position) {


                                TextView orderspushid = v.findViewById(R.id.orderpushid);
                                TextView date = v.findViewById(R.id.date);
                                TextView orderid = v.findViewById(R.id.orderid);

                                if(!orderid.getText().toString().equals("Settlement")) {
                                    Bundle bundle = new Bundle();
                                    CompleteOrderDetails fragment = new CompleteOrderDetails();
                                    bundle.putString("pushid", orderspushid.getText().toString());
                                    bundle.putString("date", date.getText().toString());
                                    bundle.putString("orderno", orderid.getText().toString());
                                    fragment.setArguments(bundle);
                                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                    fragmentManager.beginTransaction()
                                            .addToBackStack(null)
                                            .replace(R.id.frame_container, fragment).commit();
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
                            Toast.makeText(getContext(),"No Payments Found",Toast.LENGTH_LONG).show();
                        }
                    }

                };

        mRecyclerView.setAdapter(firebaseRecyclerAdapter);


    }
}
