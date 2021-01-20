package hurrys.corp.vendor.Models.Payment;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;

import hurrys.corp.vendor.R;

public class ViewHolder  extends RecyclerView.ViewHolder  {
    View mView;

    private ViewHolder.ClickListener mClickListener;

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

    public void setDetails(Context ctx,String PushId, String UserId, String UserBalance, String Date, String Amount, String Generated, String TransactionType, String TransactionName, String TransactionId, String Status,String OrderPushId) {

        TextView orderid,pushid,date,total,details,orderpushid,status;
        orderid=mView.findViewById(R.id.orderid);
        pushid=mView.findViewById(R.id.pushid);
        date=mView.findViewById(R.id.date);
        total=mView.findViewById(R.id.total);
        orderpushid=mView.findViewById(R.id.orderpushid);
        status=mView.findViewById(R.id.status);

        DecimalFormat form=new DecimalFormat("0.00");

        if(TransactionName.equals("Settlement")) {
            orderid.setText("Settlement");
            String a[] = Date.split("-");
            if (a.length > 2)
                date.setText(a[2] + "/" + a[1] + "/" + a[0]);
            pushid.setText(PushId);
            total.setText("\u00a3" + form.format(Double.parseDouble(Amount)));
//            total.setTextColor(Color.RED);
            orderpushid.setText("");
//            status.setTextColor(Color.parseColor("#7FFF00"));
            status.setText("Delivered");
            status.setVisibility(View.GONE);
        }
        else{
            orderid.setText("Order No:#" + TransactionId);
            String a[] = Date.split("-");
            if (a.length > 2)
                date.setText(a[2] + "/" + a[1] + "/" + a[0]);
            pushid.setText(PushId);
            total.setText("\u00a3" + form.format(Double.parseDouble(Amount)));
//            total.setTextColor(Color.parseColor("#008000"));
            orderpushid.setText(OrderPushId);
            status.setText("Delivered");
//            if(Status.equals("Approved")){
////                status.setTextColor(Color.parseColor("#7FFF00"));
////                status.setText("Settled");
//            }
//            else if(Status.equals("Pending")){
//                status.setTextColor(Color.RED);
//                status.setText("Pending");
//            }
//            else{
//                status.setTextColor(Color.parseColor("#E41C39"));
//                status.setText("Cancelled");
//            }
        }

    }




    public void setDetailssettlement(Context ctx,String PushId, String UserId, String UserBalance, String Date, String Amount, String Generated, String TransactionType, String TransactionName, String TransactionId, String Status,String OrderPushId) {

        ConstraintLayout constraintLayout;
        TextView orderid,pushid,date,total,details,orderpushid,status;
        orderid=mView.findViewById(R.id.orderid);
        pushid=mView.findViewById(R.id.pushid);
        date=mView.findViewById(R.id.date);
        total=mView.findViewById(R.id.total);
        orderpushid=mView.findViewById(R.id.orderpushid);
        status=mView.findViewById(R.id.status);
        constraintLayout=mView.findViewById(R.id.constraintLayout);

        DecimalFormat form=new DecimalFormat("0.00");

        if(TransactionName.equals("Settlement")) {
            orderid.setText("Settlement");
            String a[] = Date.split("-");
            if (a.length > 2)
                date.setText(a[2] + "/" + a[1] + "/" + a[0]);
            pushid.setText(PushId);
            total.setText("\u00a3" + form.format(Double.parseDouble(Amount)));
            total.setTextColor(Color.RED);
            orderpushid.setText("");
            status.setTextColor(Color.parseColor("#7FFF00"));
            status.setText("Settled");
            status.setVisibility(View.GONE);
        }
        else
        {
            constraintLayout.setVisibility(View.GONE);
            constraintLayout.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
        }


    }



    public void setOnClickListener(ViewHolder.ClickListener clickListener) {
        mClickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(View v, int position);

        void onItemLongClick(View v, int position);
    }
}

