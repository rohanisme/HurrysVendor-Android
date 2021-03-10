package hurrys.corp.vendor.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
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

import cn.pedant.SweetAlert.SweetAlertDialog;
import hurrys.corp.vendor.Configurations.Session;
import hurrys.corp.vendor.Models.Requests.Requests;
import hurrys.corp.vendor.Models.Requests.ViewHolder;
import hurrys.corp.vendor.R;

public class PendingRequest extends Fragment {



    public PendingRequest() {
        // Required empty public constructor
    }


    private RecyclerView mRecyclerView;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mref;
    private Session sessions;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_pending_request, container, false);

        if(getActivity()!=null) {
            LinearLayout bottomnavigation=(getActivity()).findViewById(R.id.bottomnavigation);
            bottomnavigation.setVisibility(View.GONE);
        }

        ImageView back = v.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getActivity()!=null)
                    getActivity().onBackPressed();
            }
        });

        sessions = new Session(getContext());
        mRecyclerView = v.findViewById(R.id.recyclerView);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mref = mFirebaseDatabase.getReference().child("Requests");

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        sessions = new Session(getContext());
        final Query firebasequery = mref.orderByChild("UserId").equalTo(sessions.getusername());


        FirebaseRecyclerAdapter<Requests, ViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Requests, ViewHolder>(
                        Requests.class,
                        R.layout.pending_row,
                        ViewHolder.class,
                        firebasequery
                ) {
                    @Override
                    protected void populateViewHolder(ViewHolder viewHolder, Requests requests, int position) {
                        viewHolder.setDetails(getContext(),requests.PushId,requests.Name,requests.Number,requests.Email,requests.Request,requests.Status,requests.Reference);

                    }

                    @Override
                    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                        ViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);
                        viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
                            @Override
                            public void onItemClick(View v, int position) {


                                TextView details = v.findViewById(R.id.desc);

                                if(getContext()!=null) {
                                    SweetAlertDialog sDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.NORMAL_TYPE)
                                            .setTitleText("Request Details!")
                                            .setContentText(details.getText().toString())
                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                    sweetAlertDialog.dismiss();
                                                }
                                            });
                                    sDialog.setCancelable(false);
                                    sDialog.show();
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