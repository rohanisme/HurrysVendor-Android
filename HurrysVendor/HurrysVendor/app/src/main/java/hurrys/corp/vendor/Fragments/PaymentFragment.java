package hurrys.corp.vendor.Fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import hurrys.corp.vendor.Configurations.Session;
import hurrys.corp.vendor.Models.Payment.Payment;
import hurrys.corp.vendor.Models.Payment.ViewHolder;
import hurrys.corp.vendor.R;

public class PaymentFragment extends Fragment {

    Session session;

    private RecyclerView mRecyclerView;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mref;
    private Session sessions;

    TextView bank,earnings,tt1,tt2,paymentdate;
    private LinearLayout t1,t2;
    View v1,v2;
    String a="",b="";


    public PaymentFragment() {
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
        View v=inflater.inflate(R.layout.fragment_payment, container, false);

        LinearLayout bottomnavigation=(getActivity()).findViewById(R.id.bottomnavigation);
        bottomnavigation.setVisibility(View.GONE);

        session = new Session(getActivity());

        ImageView back=v.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getActivity()!=null)
                    getActivity().onBackPressed();
            }
        });




        final Date currentDate = new Date(System.currentTimeMillis());
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        final String date1 = df.format(currentDate);


        final DecimalFormat form = new DecimalFormat("0.00");

        sessions = new Session(getContext());
        bank = v.findViewById(R.id.bank);
        earnings = v.findViewById(R.id.earnings);
        tt1 = v.findViewById(R.id.tt1);
        tt2 = v.findViewById(R.id.tt2);
        t1 = v.findViewById(R.id.t1);
        t2 = v.findViewById(R.id.t2);
        v1 = v.findViewById(R.id.v1);
        v2 = v.findViewById(R.id.v2);
        paymentdate = v.findViewById(R.id.paymentdate);

        mRecyclerView = v.findViewById(R.id.recyclerView);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        if(getContext()!=null)
            mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), mLayoutManager.getOrientation()));

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mref = mFirebaseDatabase.getReference().child("Vendor").child(sessions.getusername()).child("Transactions");


        tt1.setTextColor(Color.parseColor("#00B246"));
        tt2.setTextColor(Color.parseColor("#808080"));
        v2.setBackgroundColor(Color.parseColor("#eaeaea"));
        v1.setBackgroundColor(Color.parseColor("#00B246"));
        Payment();

        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tt1.setTextColor(Color.parseColor("#00B246"));
                tt2.setTextColor(Color.parseColor("#808080"));
                v2.setBackgroundColor(Color.parseColor("#eaeaea"));
                v1.setBackgroundColor(Color.parseColor("#00B246"));
                Payment();
            }
        });

        t2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tt2.setTextColor(Color.parseColor("#00B246"));
                tt1.setTextColor(Color.parseColor("#808080"));
                v1.setBackgroundColor(Color.parseColor("#eaeaea"));
                v2.setBackgroundColor(Color.parseColor("#00B246"));
                Settlement();
            }
        });



        FirebaseDatabase.getInstance().getReference().child("Vendor").child(session.getusername())
                .child("BankName")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            a=dataSnapshot.getValue().toString();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
        FirebaseDatabase.getInstance().getReference().child("Vendor").child(session.getusername())
                .child("AccountNumber")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            b=dataSnapshot.getValue().toString();

                            bank.setText(a+","+b);
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



        FirebaseDatabase.getInstance().getReference().child("Vendor").child(sessions.getusername())
                .child("PendingAmount")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            earnings.setText("\u00a3"+form.format(Double.parseDouble(dataSnapshot.getValue().toString())));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });




        return v;
    }



    public void Payment(){

        Query q=mref.orderByChild("TransactionType").equalTo("Cr");

        FirebaseRecyclerAdapter<Payment, ViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Payment, ViewHolder>(
                        Payment.class,
                        R.layout.payment_row,
                        ViewHolder.class,
                        q
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
                                TextView total = v.findViewById(R.id.total);
                                TextView status = v.findViewById(R.id.status);

                                if(!orderid.getText().toString().equals("Settlement")) {
                                    Bundle bundle = new Bundle();
                                    CompleteOrderDetails fragment = new CompleteOrderDetails();
                                    bundle.putString("pushid", orderspushid.getText().toString());
                                    bundle.putString("date", date.getText().toString());
                                    bundle.putString("orderno", orderid.getText().toString());
                                    bundle.putString("total", total.getText().toString());
                                    bundle.putString("status", status.getText().toString());
                                    fragment.setArguments(bundle);
                                    if(getActivity()!=null) {
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

        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }


    public void Settlement(){
        Query q=mref.orderByChild("TransactionType").equalTo("Dr");

        FirebaseRecyclerAdapter<Payment, ViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Payment, ViewHolder>(
                        Payment.class,
                        R.layout.payment_row,
                        ViewHolder.class,
                        q
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
                                TextView total = v.findViewById(R.id.total);
                                TextView status = v.findViewById(R.id.status);

                                if(!orderid.getText().toString().equals("Settlement")) {
                                    Bundle bundle = new Bundle();
                                    CompleteOrderDetails fragment = new CompleteOrderDetails();
                                    bundle.putString("pushid", orderspushid.getText().toString());
                                    bundle.putString("date", date.getText().toString());
                                    bundle.putString("orderno", orderid.getText().toString());
                                    bundle.putString("total", total.getText().toString());
                                    bundle.putString("status", status.getText().toString());
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

                };

        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }

}