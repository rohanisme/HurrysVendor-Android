package hurrys.corp.vendor.Models.ItemStatus;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.zcw.togglebutton.ToggleButton;

import java.util.ArrayList;

import hurrys.corp.vendor.Activities.MainActivity;
import hurrys.corp.vendor.Configurations.Session;
import hurrys.corp.vendor.Models.Items.Items;
import hurrys.corp.vendor.R;


public class ItemStatusAdapter extends RecyclerView.Adapter<ItemStatusAdapter.ViewHolder> {

    ArrayList<ItemStatus> title;
    Session session;

    public ItemStatusAdapter(ArrayList<ItemStatus> inventories) {
        this.title = inventories;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.status_row, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemStatusAdapter.ViewHolder holder, final int position) {

        ItemStatus data = title.get(position);

        holder.name.setText(data.Name);
        holder.pushid.setText(data.PushId);

        if(data.Status.equals("Active")){
            holder.status.setToggleOn(true);
        }
        else{
            holder.status.setToggleOff(true);
        }

        session = new Session(holder.view.getContext());

        holder.status.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                if(on){
                    if(data.Type.equals("Addons"))
                        FirebaseDatabase.getInstance().getReference().child("Vendor")
                                .child(session.getusername())
                                .child("Products")
                                .child(data.ProductId)
                                .child("AddOns").child(data.PushId).child("Status").setValue("Active");
                    else if(data.Type.equals("Portions"))
                        FirebaseDatabase.getInstance().getReference().child("Vendor")
                                .child(session.getusername())
                                .child("Products")
                                .child(data.ProductId)
                                .child("Portions").child(data.PushId).child("Status").setValue("Active");
                    else if(data.Type.equals("Meals"))
                        FirebaseDatabase.getInstance().getReference().child("Vendor")
                                .child(session.getusername())
                                .child("Products")
                                .child(data.ProductId)
                                .child("Items").child(data.PushId).child("Status").setValue("Active");

                }
                else{
                    if(data.Type.equals("Addons"))
                        FirebaseDatabase.getInstance().getReference().child("Vendor")
                                .child(session.getusername())
                                .child("Products")
                                .child(data.ProductId)
                                .child("AddOns").child(data.PushId).child("Status").setValue("InActive");
                    else if(data.Type.equals("Portions"))
                        FirebaseDatabase.getInstance().getReference().child("Vendor")
                                .child(session.getusername())
                                .child("Products")
                                .child(data.ProductId)
                                .child("Portions").child(data.PushId).child("Status").setValue("InActive");
                    else if(data.Type.equals("Meals"))
                        FirebaseDatabase.getInstance().getReference().child("Vendor")
                                .child(session.getusername())
                                .child("Products")
                                .child(data.ProductId)
                                .child("Items").child(data.PushId).child("Status").setValue("InActive");
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return title.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final View view;

        TextView name,pushid;
        ToggleButton status;

        public ViewHolder(View mView) {
            super(mView);
            this.view = mView;

            status = mView.findViewById(R.id.status);
            name = mView.findViewById(R.id.name);
            pushid = mView.findViewById(R.id.pushid);

        }

        @Override
        public void onClick(View v) {

        }
    }
}



