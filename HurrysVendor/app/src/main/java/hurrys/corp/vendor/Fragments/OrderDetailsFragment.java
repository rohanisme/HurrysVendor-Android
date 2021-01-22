package hurrys.corp.vendor.Fragments;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    private RecyclerView mRecyclerView;
    private DatabaseReference mref;
    private TextView orderid, date, daname, address, support;
    private LinearLayout deliveryrow;

    private TextView subtotal, discount, commision, delivery, tax, grandtotal, status, number;

    private Button accpet, accpet1, ready, decline, delivered, dispatched;
    private double gtot = 0, dbalance = 0;
    private BottomSheetDialog bottomSheetDialog;

    String selection = "";

    DataSnapshot d;

    CircleImageView pp;
    TextView deliveryname;
    ImageView call, openMenu;

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

        daname = v.findViewById(R.id.daname);
        address = v.findViewById(R.id.address);
        support = v.findViewById(R.id.support);
        status = v.findViewById(R.id.status);
        dispatched = v.findViewById(R.id.dispatched);
        delivered = v.findViewById(R.id.delivered);

        subtotal = v.findViewById(R.id.subtotal);
        delivery = v.findViewById(R.id.delivery);
        commision = v.findViewById(R.id.commision);
        discount = v.findViewById(R.id.discount);
        tax = v.findViewById(R.id.tax);
        grandtotal = v.findViewById(R.id.grandtotal);
        accpet = v.findViewById(R.id.accept);
        accpet1 = v.findViewById(R.id.accept1);
        ready = v.findViewById(R.id.ready);
        decline = v.findViewById(R.id.decline);
        deliveryrow = v.findViewById(R.id.deliveryrow);
        pp = v.findViewById(R.id.pp);
        deliveryname = v.findViewById(R.id.deliveryname);
        call = v.findViewById(R.id.call);
        openMenu = v.findViewById(R.id.openMenu);
        number = v.findViewById(R.id.number);


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
                            DecimalFormat form = new DecimalFormat("0.00");
                            d = dataSnapshot;

                            date.setText(dataSnapshot.child("OrderDateTime").getValue().toString());
                            subtotal.setText("\u00a3" + form.format(Double.parseDouble(dataSnapshot.child("Subtotal").getValue().toString())));
                            discount.setText("\u00a3" + form.format(Double.parseDouble(dataSnapshot.child("Discount").getValue().toString())));
                            delivery.setText("\u00a3" + form.format(Double.parseDouble(dataSnapshot.child("DeliveryCharges").getValue().toString())));
                            tax.setText("\u00a3" + form.format(Double.parseDouble(dataSnapshot.child("Taxes").getValue().toString())));

                            orderid.setText(dataSnapshot.child("OrderNo").getValue().toString());

                            if (dataSnapshot.child("DeliverySelection").exists()) {
                                selection = dataSnapshot.child("DeliverySelection").getValue().toString();
                            }


                            double price = Double.parseDouble(subtotal.getText().toString().substring(1));
                            double del = Double.parseDouble(delivery.getText().toString().substring(1));
                            double taxes = Double.parseDouble(tax.getText().toString().substring(1));
                            double disc = Double.parseDouble(discount.getText().toString().substring(1));
                            double com = 25;

                            double tot = price * (com / 100.0);


                            double gtot = (price - tot);

                            commision.setText("\u00a3" + form.format(Math.round(tot * 100.0) / 100.0));
                            grandtotal.setText("\u00a3" + form.format(Math.round(gtot * 100.0) / 100.0));
                            gtot = Double.parseDouble(grandtotal.getText().toString().substring(1));


                            address.setText(dataSnapshot.child("Address").getValue().toString());
                            daname.setText(dataSnapshot.child("CName").getValue().toString());

                            if (dataSnapshot.child("Status").getValue().toString().equals("1")) {
                                accpet.setVisibility(View.VISIBLE);
                                accpet1.setVisibility(View.VISIBLE);
                                decline.setVisibility(View.VISIBLE);
                                ready.setVisibility(View.GONE);
                                dispatched.setVisibility(View.GONE);
                                delivered.setVisibility(View.GONE);
                            } else if (dataSnapshot.child("Status").getValue().toString().equals("2")) {
                                ready.setVisibility(View.VISIBLE);
                                decline.setVisibility(View.GONE);
                                accpet.setVisibility(View.GONE);
                                accpet1.setVisibility(View.GONE);
                                dispatched.setVisibility(View.GONE);
                                delivered.setVisibility(View.GONE);
                            } else if (dataSnapshot.child("Status").getValue().toString().equals("3")) {
                                if (!TextUtils.isEmpty(selection)) {
                                    if (selection.equals("Self")) {
                                        ready.setVisibility(View.GONE);
                                        decline.setVisibility(View.GONE);
                                        accpet.setVisibility(View.GONE);
                                        accpet1.setVisibility(View.GONE);
                                        dispatched.setVisibility(View.VISIBLE);
                                        delivered.setVisibility(View.GONE);
                                    }
                                }
                            } else if (dataSnapshot.child("Status").getValue().toString().equals("4")) {
                                if (!TextUtils.isEmpty(selection)) {
                                    if (selection.equals("Self")) {
                                        ready.setVisibility(View.GONE);
                                        decline.setVisibility(View.GONE);
                                        accpet.setVisibility(View.GONE);
                                        accpet1.setVisibility(View.GONE);
                                        dispatched.setVisibility(View.GONE);
                                        delivered.setVisibility(View.VISIBLE);
                                    }
                                }
                            } else {
                                ready.setVisibility(View.GONE);
                                accpet.setVisibility(View.GONE);
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
                            } else if (dataSnapshot.child("Status").getValue().toString().equals("2")) {
                                status.setText("PREPARING");
                                status.setTextColor(Color.parseColor("#00B246"));
                            } else if (dataSnapshot.child("Status").getValue().toString().equals("3")) {
                                status.setText("READY TO DELIVER");
                                status.setTextColor(Color.parseColor("#00B246"));
                            } else if (dataSnapshot.child("Status").getValue().toString().equals("4")) {
                                status.setText("AWAITING DELIVERY");
                                status.setTextColor(Color.parseColor("#00B246"));
                            } else if (dataSnapshot.child("Status").getValue().toString().equals("5")) {
                                status.setText("DELIVERED");
                                status.setTextColor(Color.parseColor("#00B246"));
                            } else if (dataSnapshot.child("Status").getValue().toString().equals("10")) {
                                status.setText("CANCELLED");
                                status.setTextColor(Color.parseColor("#FF0000"));
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
                            } else {

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
                                                viewHolder.setDetails2(getContext(), orderDetails.Name, orderDetails.Price, orderDetails.Type, orderDetails.Qty, orderDetails.Image);
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


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + number.getText().toString()));
                startActivity(intent);
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

        bottomSheetDialog = new BottomSheetDialog(getContext());
        final View bottomSheetDialogView = getLayoutInflater().inflate(R.layout.bottom_deliveryselection, null);
        bottomSheetDialog.setContentView(bottomSheetDialogView);

        RadioGroup radioGroup = bottomSheetDialogView.findViewById(R.id.radio);
        Button submit = bottomSheetDialogView.findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int selectedId = radioGroup.getCheckedRadioButtonId();

                bottomSheetDialog.dismiss();

                if (selectedId == -1) {
                    Toast.makeText(getContext(), "Please select a required option", Toast.LENGTH_LONG).show();
                    return;
                }

                RadioButton radioButton = (RadioButton) bottomSheetDialogView.findViewById(selectedId);
                FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("Status").setValue("3");
                FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("DeliverySelection").setValue(radioButton.getText().toString());
                FirebaseDatabase.getInstance().getReference().child("PendingOrders").child(pushid).setValue(d.getValue());
                FirebaseDatabase.getInstance().getReference().child("PendingOrders").child(pushid).child("Status").setValue("2");
                FirebaseDatabase.getInstance().getReference().child("PendingOrders").child(pushid).child("SellerName").setValue(session.getstorename());
                FirebaseDatabase.getInstance().getReference().child("PendingOrders").child(pushid).child("SellerAddress").setValue(session.getaddress());
                FirebaseDatabase.getInstance().getReference().child("PendingOrders").child(pushid).child("SellerNumber").setValue(session.getnumber());

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
                                    FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("Status").setValue("2");
                                    FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("DeliverySelection").setValue("Hurrys");
                                    FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("SellerName").setValue(session.getstorename());
                                    FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("SellerAddress").setValue(session.getaddress());
                                    FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("SellerNumber").setValue(session.getnumber());
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
                                    FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("Status").setValue("2");
                                    FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("DeliverySelection").setValue("Self");
                                    FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("SellerName").setValue(session.getstorename());
                                    FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("SellerAddress").setValue(session.getaddress());
                                    FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("SellerNumber").setValue(session.getnumber());
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

        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (getContext() != null) {

                    final EditText editText = new EditText(getContext());
                    SweetAlertDialog dialog = new SweetAlertDialog(getContext(), SweetAlertDialog.NORMAL_TYPE)
                            .setTitleText("Please mention the reason for rejection")
                            .setConfirmText("Ok")
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
                                    FirebaseDatabase.getInstance().getReference().child("Orders").child(pushid).child("Status").setValue("3");
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
                                                } else {
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
}
