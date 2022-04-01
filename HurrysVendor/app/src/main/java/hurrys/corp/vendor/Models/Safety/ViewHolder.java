package hurrys.corp.vendor.Models.Safety;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
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

    public void setDetails(Context ctx,String Description,String Heading,String Image,String Url){

        TextView heading,description,url;
        ImageView image;
        heading=mView.findViewById(R.id.heading);
        description=mView.findViewById(R.id.description);
        image=mView.findViewById(R.id.image);
        url=mView.findViewById(R.id.url);

        if(TextUtils.isEmpty(Heading))
            heading.setVisibility(View.GONE);
        else
            heading.setVisibility(View.VISIBLE);

        if(TextUtils.isEmpty(Description))
            description.setVisibility(View.GONE);
        else
            description.setVisibility(View.VISIBLE);

        heading.setText(Heading);

        description.setText(Description);

        if(TextUtils.isEmpty(Url)){
            url.setVisibility(View.GONE);
        }
        else {
            url.setVisibility(View.VISIBLE);
        }

        url.setText(Url);
        Glide.with(ctx).load(Image).into(image);

    }

    public void setOnClickListener(ClickListener clickListener) {
        mClickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(View v, int position);

        void onItemLongClick(View v, int position);
    }
}
