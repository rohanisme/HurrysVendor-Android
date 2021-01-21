package hurrys.corp.vendor.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import hurrys.corp.vendor.Configurations.Session;
import hurrys.corp.vendor.Models.Category.Category;
import hurrys.corp.vendor.Models.ProductsList.ProductsList;
import hurrys.corp.vendor.Models.ProductsList.ViewHolder;
import hurrys.corp.vendor.R;

public class ProductsListFragment extends Fragment {



    RecyclerView recyclerview;
    Session session;
    DatabaseReference mref;


    public ProductsListFragment() {
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
        View v=inflater.inflate(R.layout.fragment_products_list, container, false);
        LinearLayout bottomnavigation=(getActivity()).findViewById(R.id.bottomnavigation);
        bottomnavigation.setVisibility(View.GONE);

        ImageView back = v.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        recyclerview=v.findViewById(R.id.recyclerview);
        session=new Session(getActivity());

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        recyclerview.setLayoutManager(mLayoutManager);
        recyclerview.addItemDecoration(new DividerItemDecoration(getContext(), mLayoutManager.getOrientation()));

        mref= FirebaseDatabase.getInstance().getReference().child("Vendor").child(session.getusername())
                .child("Products");
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        if(session.getcategory().equals("Food Delivery")||session.getcategory().equals("Home Food")) {
            FirebaseRecyclerAdapter<ProductsList, ViewHolder> firebaseRecyclerAdapter =
                    new FirebaseRecyclerAdapter<ProductsList, ViewHolder>(
                            ProductsList.class,
                            R.layout.productlist_row1,
                            ViewHolder.class,
                            mref
                    ) {
                        @Override
                        protected void populateViewHolder(ViewHolder viewHolder, ProductsList productsList, int position) {
                            viewHolder.setDetails(getContext(), productsList.ItemName, productsList.PushId, productsList.ItemDescription, productsList.FoodImage);
                        }

                        @Override
                        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                            ViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);
                            viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
                                @Override
                                public void onItemClick(View v, int position) {


                                    TextView pushid = v.findViewById(R.id.pushid);

                                    Bundle bundle = new Bundle();
                                    bundle.putString("pushid", pushid.getText().toString());

                                        Fragment fragment = new AddFoodItems_Edit();
                                        fragment.setArguments(bundle);
                                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                        fragmentManager.beginTransaction()
                                                .addToBackStack(null)
                                                .replace(R.id.frame_container, fragment).commit();



                                }

                                @Override
                                public void onItemLongClick(View v, int position) {

                                }
                            });
                            return viewHolder;
                        }

                    };

            recyclerview.setAdapter(firebaseRecyclerAdapter);
        }
        else{
            FirebaseRecyclerAdapter<ProductsList, ViewHolder> firebaseRecyclerAdapter =
                    new FirebaseRecyclerAdapter<ProductsList, ViewHolder>(
                            ProductsList.class,
                            R.layout.productlist_row1,
                            ViewHolder.class,
                            mref
                    ) {
                        @Override
                        protected void populateViewHolder(ViewHolder viewHolder, ProductsList productsList, int position) {
                            viewHolder.setDetails1(getContext(), productsList.ItemName, productsList.PushId, productsList.ItemDescription, productsList.ItemImage1);
                        }

                        @Override
                        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                            ViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);
                            viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
                                @Override
                                public void onItemClick(View v, int position) {


                                    TextView pushid = v.findViewById(R.id.pushid);

                                    Bundle bundle = new Bundle();
                                    bundle.putString("pushid", pushid.getText().toString());

                                        Fragment fragment = new AddProducts_Edit();
                                        fragment.setArguments(bundle);
                                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                        fragmentManager.beginTransaction()
                                                .addToBackStack(null)
                                                .replace(R.id.frame_container, fragment).commit();


                                }

                                @Override
                                public void onItemLongClick(View v, int position) {

                                }
                            });
                            return viewHolder;
                        }

                    };

            recyclerview.setAdapter(firebaseRecyclerAdapter);
        }
    }
}