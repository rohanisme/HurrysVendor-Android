package hurrys.corp.vendor.Models.ProductsList;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

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

    public void setDetails(Context ctx,String ItemName,String PushId,String ItemDescription,String FoodType){
        TextView name,pushid,details;
        ImageView indicator;

        name=mView.findViewById(R.id.name);
        details=mView.findViewById(R.id.details);
        pushid=mView.findViewById(R.id.pushid);
        indicator=mView.findViewById(R.id.indicator);

        name.setText(ItemName);
        details.setText(ItemDescription);
        pushid.setText(PushId);

//        if(!TextUtils.isEmpty(FoodType)) {
//            if (FoodType.equals("Veg")) {
//                indicator.setImageResource(R.drawable.veg);
//            }
//            else if(FoodType.equals("NonVeg")){
//                indicator.setImageResource(R.drawable.nonveg);
//            }
//            else{
//                indicator.setImageResource(R.drawable.egg);
//            }
//        }
//        else{
//            indicator.setVisibility(View.GONE);
//        }

        if(!TextUtils.isEmpty(FoodType)) {
            if(!FoodType.equals("No"))
                Glide.with(ctx)
                        .load(FoodType)
                        .into(indicator);
        }


    }

    public void setDetails1(Context ctx,String ItemName,String PushId,String ItemDescription,String Image){
        TextView name,pushid,details;
        ImageView indicator;

        name=mView.findViewById(R.id.name);
        details=mView.findViewById(R.id.details);
        pushid=mView.findViewById(R.id.pushid);
        indicator=mView.findViewById(R.id.indicator);

        name.setText(ItemName);
        details.setText(ItemDescription);
        pushid.setText(PushId);

        if(!TextUtils.isEmpty(Image)) {
            if(!Image.equals("No"))
                Glide.with(ctx)
                    .load(Image)
                    .into(indicator);
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
