package hurrys.corp.vendor.Models.Orders;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.DecimalFormat;

import de.hdodenhof.circleimageview.CircleImageView;
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

    public void setDetails(Context ctx, String CName, String Address, String Subtotal, String Pushid, String OrderNo, String OrderDateTime, String Qty, String Payment, String Status, String DeliveryPartner, String DeliveryNumber, String DeliveryImage) {

        CardView cardView;
        TextView name, date, amount, payment, pushid, address, items, orderid, status, number;
        LinearLayout addressrow, deliveryrow;
        CircleImageView pp;
        TextView deliveryname;
        ImageView call;

        cardView = mView.findViewById(R.id.card);
        name = mView.findViewById(R.id.name);
        date = mView.findViewById(R.id.date);
        amount = mView.findViewById(R.id.amount);
        payment = mView.findViewById(R.id.payment);
        pushid = mView.findViewById(R.id.pushid);
        address = mView.findViewById(R.id.address);
        items = mView.findViewById(R.id.items);
        orderid = mView.findViewById(R.id.orderid);
        status = mView.findViewById(R.id.status);
        deliveryrow = mView.findViewById(R.id.deliveryrow);
        addressrow = mView.findViewById(R.id.addressrow);
        pp = mView.findViewById(R.id.pp);
        deliveryname = mView.findViewById(R.id.deliveryname);
        call = mView.findViewById(R.id.call);
        number = mView.findViewById(R.id.number);


        double price = Double.parseDouble(Subtotal);
        double com = 25;
        double tot = price * (com / 100.0);
        double gtot = price - tot;

        DecimalFormat form = new DecimalFormat("0.00");

        pushid.setText(Pushid);
        orderid.setText("Order ID : " + OrderNo);
        date.setText(OrderDateTime);
        amount.setText("\u00a3 " + form.format(Math.round(gtot * 100.0) / 100.0));
        items.setText(Qty + " items");
        payment.setText(Payment);
        address.setText(Address);
        name.setText(CName);
        if (Status.equals("1")) {
            status.setText("PENDING");
            status.setTextColor(Color.parseColor("#b38400"));
            status.setBackgroundColor(Color.parseColor("#FFF0C5"));
        } else if (Status.equals("2")) {
            status.setText("PREPARING");
            status.setTextColor(Color.parseColor("#00B246"));
           // status.setBackgroundColor(Color.parseColor("#e5f7ec"));
        } else if (Status.equals("3")) {
            status.setText("READY TO DELIVERY");
            status.setTextColor(Color.parseColor("#00B246"));
            // status.setBackgroundColor(Color.parseColor("#e5f7ec"));
        } else if (Status.equals("4")) {
            status.setText("AWAITING DELIVERY");
            status.setTextColor(Color.parseColor("#00B246"));
            // status.setBackgroundColor(Color.parseColor("#e5f7ec"));
        } else if (Status.equals("5")) {
            status.setText("DELIVERED");
            status.setTextColor(Color.parseColor("#00B246"));
            // status.setBackgroundColor(Color.parseColor("#e5f7ec"));
        } else if (Status.equals("10")) {
            status.setText("CANCELLED");
            status.setTextColor(Color.parseColor("#FF0000"));
            //status.setBackgroundColor(Color.parseColor("#FF0000"));
        }

        if (!TextUtils.isEmpty(DeliveryPartner)) {
            addressrow.setVisibility(View.GONE);
            deliveryrow.setVisibility(View.VISIBLE);
            deliveryname.setText(DeliveryPartner);
            number.setText(DeliveryNumber);
            Glide.with(ctx)
                    .load(DeliveryImage)
                    .into(pp);
        } else {
            deliveryrow.setVisibility(View.GONE);
            addressrow.setVisibility(View.VISIBLE);
        }

    }


    public void setDetails1(Context ctx, String CName, String Address, String Subtotal, String Pushid, String OrderNo, String OrderDateTime, String Qty, String Payment, String Status) {

        CardView cardView;
        TextView name, date, amount, payment, pushid, address, items, orderid, status;


        cardView = mView.findViewById(R.id.card);
        name = mView.findViewById(R.id.name);
        date = mView.findViewById(R.id.date);
        amount = mView.findViewById(R.id.amount);
        payment = mView.findViewById(R.id.payment);
        pushid = mView.findViewById(R.id.pushid);
        address = mView.findViewById(R.id.address);
        items = mView.findViewById(R.id.items);
        orderid = mView.findViewById(R.id.orderid);
        status = mView.findViewById(R.id.status);


        double price = Double.parseDouble(Subtotal);
        double com = 25;
        double tot = price * (com / 100.0);
        double gtot = price - tot;

        DecimalFormat form = new DecimalFormat("0.00");

        pushid.setText(Pushid);
        orderid.setText("OrderID #" + OrderNo);
        date.setText(OrderDateTime);
        amount.setText("\u00a3 " + form.format(Math.round(gtot * 100.0) / 100.0));
        items.setText(Qty + " items");
        payment.setText(Payment);
        address.setText(Address);
        name.setText(CName);
        if (Status.equals("5")) {
            status.setText("DELIVERED");
            status.setTextColor(Color.parseColor("#00B246"));
        } else if (Status.equals("10")) {
            status.setText("CANCELLED");
            status.setTextColor(Color.parseColor("#FF0000"));
        } else {
            cardView.setVisibility(View.GONE);
            cardView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
        }

    }


    public void setDetails2(Context ctx, String OrderNo, String ItemDetails, String Pushid, String OrderDateTime, String ChefTotal, String Status, String ChefCommision, String Subtotal, String DeliveryCharges, String Chef, String OrderType, String VendorCustomer) {

//        TextView orderid,items,pushid,date,total,status;
//        ImageView statusimage;
//        LinearLayout linearLayout;
//        RotationRatingBar ratebar = mView.findViewById(R.id.ratebar);
//        orderid = mView.findViewById(R.id.orderid);
//        date = mView.findViewById(R.id.date);
//        total = mView.findViewById(R.id.total);
//        pushid = mView.findViewById(R.id.pushid);
//        items = mView.findViewById(R.id.items);
//        status = mView.findViewById(R.id.status);
////        statusimage = mView.findViewById(R.id.statusimage);
//        linearLayout = mView.findViewById(R.id.linearLayout);
//
//        Session session=new Session(ctx);
//
//
//        if(Chef.equals(session.getusername())) {
//
//            if (Status.equals("5")) {
//
//                double price = Double.parseDouble(Subtotal);
//                double com = 25;
//                double tot = price * (com / 100.0);
//                double gtot = price - tot ;
//
//                DecimalFormat form = new DecimalFormat("0.00");
//                pushid.setText(Pushid);
//                orderid.setText(OrderNo);
//                date.setText(OrderDateTime);
//                total.setText("\u00a3" + form.format(Math.round(gtot*100.0)/100.0));
//                items.setText(ItemDetails);
//                status.setText(Status);
//                if(!TextUtils.isEmpty(VendorCustomer)) {
//                    float a=Float.parseFloat(VendorCustomer);
//                    ratebar.setRating(a);
//                }
//                else
//                    ratebar.setVisibility(View.GONE);
//                statusimage.setVisibility(View.GONE);
//            } else {
//                linearLayout.setVisibility(View.GONE);
//                linearLayout.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
//            }
//        }
//        else{
//            linearLayout.setVisibility(View.GONE);
//            linearLayout.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
//        }

    }


    public void setOnClickListener(ClickListener clickListener) {
        mClickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(View v, int position);

        void onItemLongClick(View v, int position);
    }
}
