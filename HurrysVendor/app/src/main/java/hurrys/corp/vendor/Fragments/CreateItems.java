package hurrys.corp.vendor.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import hurrys.corp.vendor.Configurations.Session;
import hurrys.corp.vendor.Models.Items.Items;
import hurrys.corp.vendor.Models.Items.ItemsAdapter;
import hurrys.corp.vendor.Models.MenuTitles.Title;
import hurrys.corp.vendor.Models.MenuTitles.TitleAdapter;
import hurrys.corp.vendor.R;

public class CreateItems extends Fragment {


    public CreateItems() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    LinearLayout back;
    TextView txtCategoryName,txt;
    RecyclerView recyclerView;
    Button btnFooditems,btnRemove;

    Session session;
    String pushid="",title="",tpushid="";

    ArrayList<Items> titles = new ArrayList<Items>();
    ItemsAdapter titleAdapter;
    private BottomSheetDialog bottomSheetDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_create_items, container, false);

        back = v.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getActivity()!=null){
                    getActivity().onBackPressed();
                }
            }
        });

        if(getActivity()!=null){
            LinearLayout bottomnavigation = getActivity().findViewById(R.id.bottomnavigation);
            bottomnavigation.setVisibility(View.GONE);
        }

        btnRemove = v.findViewById(R.id.btnRemove);
        btnFooditems = v.findViewById(R.id.btnFooditems);
        recyclerView = v.findViewById(R.id.recyclerView);
        txtCategoryName = v.findViewById(R.id.txtCategoryName);
        txt = v.findViewById(R.id.txt);
        recyclerView.setVisibility(View.GONE);

        session = new Session(getActivity());

        pushid = getArguments().getString("pushid");
        tpushid = getArguments().getString("tpushid");
        title = getArguments().getString("title");

        txtCategoryName.setText("Add Items to "+title);

        titles.clear();
        titleAdapter = new ItemsAdapter(titles);

        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(getContext()!=null) {
                    SweetAlertDialog sDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Are you sure you want to remove all the items in this category? If you want to remove a particular item then click on close button of that particular item")
                            .setConfirmText("Yes")
                            .setCancelText("No")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                    FirebaseDatabase.getInstance().getReference().child("Vendor").child(session.getusername())
                                            .child("Products")
                                            .child(pushid)
                                            .child("Menu")
                                            .child(tpushid).removeValue();

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


        FirebaseDatabase.getInstance().getReference().child("Vendor").child(session.getusername())
                .child("Products")
                .child(pushid)
                .child("Items")
                .orderByChild("Menu").equalTo(tpushid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            titles.clear();
                            for(DataSnapshot d:dataSnapshot.getChildren()){
                                String a="",b="",c="";
                                if(d.child("Name").exists())
                                    a = d.child("Name").getValue().toString();

                                if(d.child("Price").exists())
                                    b = d.child("Price").getValue().toString();

                                if(d.child("PushId").exists())
                                    c = d.child("PushId").getValue().toString();

                                titles.add(new Items(a,b,c,pushid));
                            }
                        }

                        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                        recyclerView.setLayoutManager(mLayoutManager);
                        titleAdapter = new ItemsAdapter(titles);
                        recyclerView.setAdapter(titleAdapter);

                        if(titleAdapter.getItemCount()>0) {
                            txt.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                        }
                        else {
                            txt.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


        btnFooditems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(getContext()!=null)
                    bottomSheetDialog=new BottomSheetDialog(getContext());

                final View bottomSheetDialogView=getLayoutInflater().inflate(R.layout.bottom_items,null);
                bottomSheetDialog.setContentView(bottomSheetDialogView);

                EditText addons=bottomSheetDialogView.findViewById(R.id.addons);
                EditText payment=bottomSheetDialogView.findViewById(R.id.payment);
                TextView cancel=bottomSheetDialogView.findViewById(R.id.cancel);
                Button add=bottomSheetDialogView.findViewById(R.id.add);

                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(TextUtils.isEmpty(addons.getText().toString())){
                            addons.setError("Enter Menu Title");
                            addons.requestFocus();
                            return;
                        }

                        if(TextUtils.isEmpty(payment.getText().toString())){
                            payment.setError("Enter Required");
                            payment.requestFocus();
                            return;
                        }

                        DatabaseReference mref = FirebaseDatabase.getInstance().getReference().child("Vendor").child(session.getusername()).child("Products").child(pushid)
                                .child("Items").push();

                        mref.child("Name").setValue(addons.getText().toString());
                        mref.child("Price").setValue(payment.getText().toString());
                        mref.child("Status").setValue("Active");
                        mref.child("PushId").setValue(mref.getKey());
                        mref.child("Menu").setValue(tpushid);

                        addons.setText("");
                        payment.setText("");

                        if(getActivity()!=null) {
                            View v = getActivity().getCurrentFocus();
                            if (v != null) {
                                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                assert imm != null;
                                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                            }
                        }

                        bottomSheetDialog.dismiss();
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        addons.setText("");
                        payment.setText("");
                        bottomSheetDialog.dismiss();

                    }
                });

                bottomSheetDialog.show();

            }
        });



        return v;
    }
}