package hurrys.corp.vendor.Models.OrderDetails;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

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

    public void setDetails(Context ctx,String Image,String Name,String Price,String Qty,String Total,String Units){

        TextView name,units,total,qty;
        ImageView image;
        image=mView.findViewById(R.id.image);
        name=mView.findViewById(R.id.name);
        units=mView.findViewById(R.id.units);
        total=mView.findViewById(R.id.total);
        qty=mView.findViewById(R.id.qty);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(20));

        if (!TextUtils.isEmpty(Image)) {
            if (!Image.equals("No"))
                Glide.with(ctx)
                        .load(Image)
                        .apply(requestOptions)
                        .into(image);
        }

        name.setText(Name);
        units.setText(Units);
        total.setText("\u00a3"+Total);
        qty.setText("x "+Qty);
        total.setVisibility(View.GONE);

    }

    public void setDetails1(Context ctx,String Name,String RPrice,String Type,String Qty){
        TextView name,qty,total;
        ImageView indicator;

        name = mView.findViewById(R.id.name);
        qty = mView.findViewById(R.id.qty);
        total = mView.findViewById(R.id.total);
        indicator = mView.findViewById(R.id.indicator);


        name.setText(Name);
        qty.setText("x "+Qty);
        total.setText("\u00a3"+RPrice);
        if(Type.equals("Veg")){
            indicator.setImageResource(R.drawable.veg);
        }
        else if(Type.equals("Non Veg")){
            indicator.setImageResource(R.drawable.nonveg);
        }
        else {
            indicator.setImageResource(R.drawable.egg);
        }
    }

    public void setDetails2(Context ctx,String Name,String RPrice,String Type,String Qty,String Image){
        TextView name,units,total,qty;
        ImageView image;
        image=mView.findViewById(R.id.image);
        name=mView.findViewById(R.id.name);
        units=mView.findViewById(R.id.units);
        total=mView.findViewById(R.id.total);
        qty=mView.findViewById(R.id.qty);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(20));

        if (!TextUtils.isEmpty(Image)) {
            if (!Image.equals("No"))
                Glide.with(ctx)
                        .load(Image)
                        .apply(requestOptions)
                        .into(image);
        }

        name = mView.findViewById(R.id.name);
        qty = mView.findViewById(R.id.qty);
        total = mView.findViewById(R.id.total);
        name.setText(Name);
        qty.setText("x "+Qty +" ("+Type+")");
        total.setText("\u00a3"+RPrice);
        units.setVisibility(View.GONE);
        total.setVisibility(View.GONE);

    }

    public void setOnClickListener(ClickListener clickListener) {
        mClickListener = clickListener;
    }

    public interface ClickListener {

        void onItemClick(View v, int position);
        void onItemLongClick(View v, int position);

    }

}
