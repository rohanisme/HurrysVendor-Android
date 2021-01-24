package hurrys.corp.vendor.Models.Category;


import android.graphics.Color;
import android.media.Image;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

import cn.pedant.SweetAlert.SweetAlertDialog;
import hurrys.corp.vendor.Activities.MainActivity;
import hurrys.corp.vendor.Configurations.Session;
import hurrys.corp.vendor.R;

public class SubCategory extends RecyclerView.Adapter<SubCategory.ViewHolder> {

    private ArrayList<Sub> category;
    private Session session;
    private BottomSheetDialog bottomSheetDialog;
    String a="";
    Category category1000;
    public SubCategory(ArrayList<Sub> inventories) {
        this.category = inventories;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v=(View) LayoutInflater.from(parent.getContext()).inflate(R.layout.subcategory_row,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final SubCategory.ViewHolder holder, final int position) {

        Sub sub=category.get(position);
        session = new Session(holder.view.getContext());

        if(!sub.Name.equals("New")) {
            holder.name.setText(sub.Name);
            holder.pushid.setText(sub.PushId);
            Glide.with(holder.view.getContext())
                    .load(sub.Image)
                    .into(holder.image);
            holder.close.setVisibility(View.VISIBLE);
        }
        else{
            holder.name.setText("New");
            holder.name.setVisibility(View.INVISIBLE);
            holder.close.setVisibility(View.INVISIBLE);
        }


        holder.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference().child("Vendor").child(session.getusername()).child("SubCategory")
                        .child(holder.pushid.getText().toString())
                        .child("ItemCategory")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    if(TextUtils.isEmpty(dataSnapshot.getValue().toString())){
                                        SweetAlertDialog sDialog = new SweetAlertDialog(holder.view.getContext(), SweetAlertDialog.WARNING_TYPE)
                                                .setTitleText("Are you sure you want to delete the category!")
                                                .setConfirmText("Yes")
                                                .setCancelText("No")
                                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override
                                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                        sweetAlertDialog.dismiss();
                                                        FirebaseDatabase.getInstance().getReference().child("Vendor").child(session.getusername()).child("SubCategory")
                                                                .child(holder.pushid.getText().toString()).removeValue();
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
                                    else{
                                        SweetAlertDialog sDialog = new SweetAlertDialog(holder.view.getContext(), SweetAlertDialog.WARNING_TYPE)
                                                .setTitleText("Please Delete all SubCategory and Items to delete Category!")
                                                .setConfirmText("Ok")
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
                                else{
                                    SweetAlertDialog sDialog = new SweetAlertDialog(holder.view.getContext(), SweetAlertDialog.WARNING_TYPE)
                                            .setTitleText("Are you sure you want to delete the category!")
                                            .setConfirmText("Yes")
                                            .setCancelText("No")
                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                    sweetAlertDialog.dismiss();
                                                    FirebaseDatabase.getInstance().getReference().child("Vendor").child(session.getusername()).child("SubCategory")
                                                            .child(holder.pushid.getText().toString()).removeValue();
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

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
            }
        });


        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.name.getText().toString().equals("New")){
                    MainActivity activity=(MainActivity) holder.view.getContext();
                    bottomSheetDialog=new BottomSheetDialog(holder.view.getContext());
                    final View bottomSheetDialogView=activity.getLayoutInflater().inflate(R.layout.bottom_category,null);
                    bottomSheetDialog.setContentView(bottomSheetDialogView);
                    RecyclerView recyclerView=bottomSheetDialogView.findViewById(R.id.recyclerView);
                    FirebaseDatabase.getInstance().getReference().child("Masters").child("SubCategory")
                            .orderByChild("Status").equalTo("Active")
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    ArrayList<Sub> subcategory = new ArrayList<Sub>();
                                    SubCategoryMaster subCategoryAdapter;
                                    subcategory.clear();

                                    if(dataSnapshot.exists()){
                                        for(DataSnapshot v:dataSnapshot.getChildren()){
                                            subcategory.add(new Sub(v.child("Name").getValue().toString(),
                                                    v.child("PushId").getValue().toString(),
                                                    v.child("SubCategoryImage").getValue().toString()));
                                        }
                                    }

                                    GridLayoutManager mLayoutManager = new GridLayoutManager(holder.view.getContext(),3);
                                    recyclerView.setLayoutManager(mLayoutManager);
                                    subCategoryAdapter = new SubCategoryMaster(subcategory);
                                    recyclerView.setAdapter(subCategoryAdapter);


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                    bottomSheetDialog.show();

                }
                else{
                    MainActivity activity=(MainActivity) holder.view.getContext();
                    bottomSheetDialog=new BottomSheetDialog(holder.view.getContext());
                    final View bottomSheetDialogView=activity.getLayoutInflater().inflate(R.layout.bottom_subcategory,null);
                    bottomSheetDialog.setContentView(bottomSheetDialogView);
                    RecyclerView recyclerView=bottomSheetDialogView.findViewById(R.id.recyclerview);
                    ImageView submit=bottomSheetDialogView.findViewById(R.id.submit);;
                    EditText categoryname=bottomSheetDialogView.findViewById(R.id.category);

                    session.settemp(holder.pushid.getText().toString());

                    FirebaseDatabase.getInstance().getReference().child("Vendor")
                            .child(session.getusername())
                            .child("SubCategory")
                            .child(holder.pushid.getText().toString())
                            .child("ItemCategory")
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists()){
                                        if(!TextUtils.isEmpty(dataSnapshot.getValue().toString())) {
                                            String temp = dataSnapshot.getValue().toString();
                                            a = temp;
                                            ArrayList<String> category1 = new ArrayList<String>(Arrays.asList(temp.split(",")));
                                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(holder.view.getContext());
                                            recyclerView.setLayoutManager(mLayoutManager);
                                            category1000 = new Category(category1);
                                            recyclerView.setAdapter(category1000);
                                        }
                                        else{
                                            ArrayList<String> category1 = new ArrayList<String>();
                                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(holder.view.getContext());
                                            recyclerView.setLayoutManager(mLayoutManager);
                                            category1000 = new Category(category1);
                                            recyclerView.setAdapter(category1000);
                                        }

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });


                    submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {


                            if(TextUtils.isEmpty(categoryname.getText().toString())){
                                categoryname.setError("Enter Category");
                                categoryname.requestFocus();
                                return;
                            }

                            a+=categoryname.getText().toString()+",";

                            FirebaseDatabase.getInstance().getReference().child("Vendor")
                                    .child(session.getusername())
                                    .child("SubCategory")
                                    .child(holder.pushid.getText().toString())
                                    .child("ItemCategory")
                                    .setValue(a);

                            categoryname.setText("");

                            FirebaseDatabase.getInstance().getReference().child("Vendor")
                                    .child(session.getusername())
                                    .child("SubCategory")
                                    .child(holder.pushid.getText().toString())
                                    .child("ItemCategory")
                                    .addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if(dataSnapshot.exists()){
                                                String temp=dataSnapshot.getValue().toString();
                                                a=temp;

                                                ArrayList<String> category1 = new ArrayList<String>(Arrays.asList(temp.split(",")));

                                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(holder.view.getContext());
                                                recyclerView.setLayoutManager(mLayoutManager);
                                                category1000 = new Category(category1);
                                                recyclerView.setAdapter(category1000);

                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });

                        }
                    });



                    bottomSheetDialog.show();
                }
            }
        });

    }



    @Override
    public int getItemCount() {
        return category.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {
        public final View view;

        TextView name,pushid;
        ImageView image,close;
        LinearLayout linearLayout;
        public ViewHolder(View mView) {
            super(mView);
            this.view = mView;
            name=mView.findViewById(R.id.name);
            pushid=mView.findViewById(R.id.pushid);
            image=mView.findViewById(R.id.image);
            close=mView.findViewById(R.id.close);
            linearLayout=mView.findViewById(R.id.linearLayout);
        }

        @Override
        public void onClick(View v) {
        }
    }

}
