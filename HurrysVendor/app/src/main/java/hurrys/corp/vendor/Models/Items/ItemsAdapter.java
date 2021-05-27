package hurrys.corp.vendor.Models.Items;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.annotations.Nullable;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import hurrys.corp.vendor.Activities.MainActivity;
import hurrys.corp.vendor.Configurations.Session;
import hurrys.corp.vendor.Fragments.CreateItems;
import hurrys.corp.vendor.Models.MenuTitles.Title;
import hurrys.corp.vendor.R;


public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {

    ArrayList<Items> title;
    Session session;

    public ItemsAdapter(ArrayList<Items> inventories) {
        this.title = inventories;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.products_row1, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemsAdapter.ViewHolder holder, final int position) {

        Items data = title.get(position);

        session = new Session(holder.view.getContext());

        holder.txtName.setText(data.Name);
        holder.txtNumber.setText("\u00a3"+data.Required);

        holder.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SweetAlertDialog sDialog = new SweetAlertDialog(holder.view.getContext(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure you want to remove the item. As the Item will be removed permanently!")
                        .setConfirmText("Yes")
                        .setCancelText("No")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();

                                FirebaseDatabase.getInstance().getReference().child("Vendor").child(session.getusername())
                                        .child("Products")
                                        .child(data.ProductPushId)
                                        .child("Items")
                                        .child(data.PushId).removeValue();

                                DatabaseReference dref = FirebaseDatabase.getInstance().getReference().child("Vendor").child(session.getusername()).child("Products").child(data.ProductPushId).child("Menu").child(data.MenuPushId).child("ItemsAdded");

                                dref.runTransaction(new Transaction.Handler() {
                                    @NonNull
                                    @Override
                                    public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                                        double value = 0;
                                        if (currentData.getValue() != null) {
                                            value = Long.parseLong(currentData.getValue().toString()) - 1;
                                        }
                                        currentData.setValue(value);
                                        return Transaction.success(currentData);
                                    }

                                    @Override
                                    public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
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
        });

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity mainActivity = (MainActivity) holder.view.getContext();
                BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(holder.view.getContext());
                final View bottomSheetDialogView=mainActivity.getLayoutInflater().inflate(R.layout.bottom_items,null);
                bottomSheetDialog.setContentView(bottomSheetDialogView);

                EditText addons=bottomSheetDialogView.findViewById(R.id.addons);
                EditText payment=bottomSheetDialogView.findViewById(R.id.payment);
                TextView cancel=bottomSheetDialogView.findViewById(R.id.cancel);
                Button add=bottomSheetDialogView.findViewById(R.id.add);

                addons.setText(data.Name);
                payment.setText(data.Required);

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

                        DatabaseReference mref = FirebaseDatabase.getInstance().getReference().child("Vendor").child(session.getusername()).child("Products").child(data.ProductPushId)
                                .child("Items").child(data.PushId);

                        mref.child("Name").setValue(addons.getText().toString());
                        mref.child("Price").setValue(payment.getText().toString());
                        mref.child("PushId").setValue(mref.getKey());

                        addons.setText("");
                        payment.setText("");

                        if(mainActivity!=null) {
                            View v = mainActivity.getCurrentFocus();
                            if (v != null) {
                                InputMethodManager imm = (InputMethodManager) mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                                assert imm != null;
                                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                            }
                        }

                        bottomSheetDialog.dismiss();
                        notifyDataSetChanged();
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
    }

    @Override
    public int getItemCount() {
        return title.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final View view;

        TextView txtName, txtNumber;
        CardView linearLayout;
        LinearLayout close;

        public ViewHolder(View mView) {
            super(mView);
            this.view = mView;

            txtName = mView.findViewById(R.id.name);
            txtNumber = mView.findViewById(R.id.gender);
            linearLayout = mView.findViewById(R.id.linearLayout);
            close = mView.findViewById(R.id.close);
        }

        @Override
        public void onClick(View v) {

        }
    }
}



