package hurrys.corp.vendor.Models.Requests;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import hurrys.corp.vendor.R;


public  class ViewHolder extends RecyclerView.ViewHolder {
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

    public void setDetails(Context ctx,String PushId,String Name,String Number,String Email,String Request,String Status,String Reference){

        TextView requesttype,status,desc,details;
        requesttype=mView.findViewById(R.id.requesttype);
        status=mView.findViewById(R.id.status);
        desc=mView.findViewById(R.id.desc);

        status.setText(Status);
        desc.setText(Request);
        requesttype.setText(Reference);

    }


    public void setDetails1(Context ctx,String Amount,String City,String Date,String Generated,String MKID,String Memebership,String MobileNumber,String Name,String PushId,String SAmount,String Status,String UserId){

        TextView requesttype,status,desc,details;
        requesttype=mView.findViewById(R.id.requesttype);
        status=mView.findViewById(R.id.status);
        desc=mView.findViewById(R.id.desc);
        details=mView.findViewById(R.id.details);

        requesttype.setText("Name : "+Name);

        status.setText("Redeemed");
        status.setTextColor(Color.RED);

        desc.setText("Membership Type:"+Memebership+"\nYou have earned "+ Amount +" Coins on this referral");





        String text="";

            text="<p>Referral Name : "+Name
                    +"<br>Referral Number : "+MobileNumber
                    +"<br>Membership Type : "+Memebership
                    +"<br>Memebership Amount : \u20b9"+SAmount
                    +"<br>Referral Amount : "+Amount
                    +"</p>";


        details.setText(text);



    }




    public void setOnClickListener(ClickListener clickListener) {
        mClickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(View v, int position);

        void onItemLongClick(View v, int position);
    }
}
