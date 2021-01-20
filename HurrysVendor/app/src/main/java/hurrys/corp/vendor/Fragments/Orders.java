package hurrys.corp.vendor.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import hurrys.corp.vendor.Configurations.Session;
import hurrys.corp.vendor.Models.Orders.ViewHolder;
import hurrys.corp.vendor.R;

public class Orders extends Fragment {


    Session session;

    private RecyclerView mRecyclerView;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mref;
    private Session sessions;

    public Orders() {
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
        View v=inflater.inflate(R.layout.fragment_orders, container, false);

        LinearLayout bottomnavigation=(getActivity()).findViewById(R.id.bottomnavigation);
        bottomnavigation.setVisibility(View.GONE);

        ImageView back=v.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getActivity()!=null){
                    getActivity().onBackPressed();
                }
            }
        });

        session = new Session(getActivity());

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mref = mFirebaseDatabase.getReference().child("Orders");

        sessions = new Session(getContext());
        mRecyclerView = v.findViewById(R.id.recycylerView);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(mLayoutManager);





        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        sessions = new Session(getContext());
        final Query firebasequery = mref.orderByChild("SellerStatus").equalTo(sessions.getusername());


        final FirebaseRecyclerAdapter<hurrys.corp.vendor.Models.Orders.Orders, ViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<hurrys.corp.vendor.Models.Orders.Orders, ViewHolder>(
                        hurrys.corp.vendor.Models.Orders.Orders.class,
                        R.layout.orders_row,
                        ViewHolder.class,
                        firebasequery
                ) {
                    @Override
                    protected void populateViewHolder(ViewHolder viewHolder, hurrys.corp.vendor.Models.Orders.Orders order, int position) {
//                        viewHolder.setDetails(getContext(),order.OrderNo,order.ItemsDetails,order.Pushid,order.OrderDateTime,order.Subtotal,order.Status,order.SellerCommission,order.Subtotal,order.DeliveryCharges,order.Seller,order.OrderType);

                    }

                    @Override
                    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                        ViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);
                        viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
                            @Override
                            public void onItemClick(View v, int position) {

//
//                                TextView pushid = v.findViewById(R.id.pushid);
//                                TextView date = v.findViewById(R.id.date);
//                                TextView orderid = v.findViewById(R.id.orderid);
//                                TextView status = v.findViewById(R.id.status);
//                                TextView type = v.findViewById(R.id.type);
//
//                                Bundle bundle = new Bundle();
//                                OrderDetailsFragment fragment = new OrderDetailsFragment();
//                                bundle.putString("pushid", pushid.getText().toString());
//                                bundle.putString("date", date.getText().toString());
//                                bundle.putString("orderno", orderid.getText().toString());
//                                bundle.putString("status", status.getText().toString());
//                                bundle.putString("type", type.getText().toString());
//                                fragment.setArguments(bundle);
//                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                                fragmentManager.beginTransaction()
//                                        .addToBackStack(null)
//                                        .replace(R.id.frame_container, fragment).commit();

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
//                        progressBar.setVisibility(View.GONE);
//                        if(getItemCount()==0){
//                            c1.setVisibility(View.VISIBLE);
//                        }
//                        else {
//                            c1.setVisibility(View.GONE);
//                        }
                    }
                };

        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }

}
