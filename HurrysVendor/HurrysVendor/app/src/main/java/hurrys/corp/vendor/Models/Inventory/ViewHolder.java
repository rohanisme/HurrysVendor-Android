package hurrys.corp.vendor.Models.Inventory;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.zcw.togglebutton.ToggleButton;

import hurrys.corp.vendor.R;


public class ViewHolder extends RecyclerView.ViewHolder {
    View mView;

    private ClickListener mClickListener;

    public ViewHolder(View itemView) {

        super(itemView);
        mView = itemView;
        //Item Click
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onItemClick(v, getAdapterPosition());
            }
        });

        //Item Long Click
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mClickListener.onItemLongClick(v, getAdapterPosition());
                return true;
            }
        });
    }

    public void setDetails(Context ctx, String Name,String PushId,String Price,String Status,String Type){


        TextView name,pushid,price,status;
        ImageView indicator;
        ToggleButton toggle;

        name=mView.findViewById(R.id.name);
        pushid=mView.findViewById(R.id.pushid);
        price=mView.findViewById(R.id.price);
        indicator=mView.findViewById(R.id.indicator);
        toggle=mView.findViewById(R.id.toggle);
        status=mView.findViewById(R.id.status);

            name.setText(Name);
            pushid.setText(PushId);
            price.setText("\u00a3"+Price);
            if(!TextUtils.isEmpty(Type)) {
                if (Type.equals("Veg")) {
                    indicator.setImageResource(R.drawable.veg);
                } else if(Type.equals("NonVeg")){
                    indicator.setImageResource(R.drawable.nonveg);
                }
                else{
                    indicator.setImageResource(R.drawable.egg);
                }
            }

            status.setText(Status);
            if(Status.equals("Active")){
                toggle.setToggleOn(true);
            }
            else{
                toggle.setToggleOff(true);
            }

    }

    public void setOnClickListener(ClickListener clickListener) {
        mClickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(View v, int position);

        void onItemLongClick(View v, int position);
    }
}