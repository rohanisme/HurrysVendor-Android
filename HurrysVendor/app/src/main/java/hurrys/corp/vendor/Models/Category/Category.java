package hurrys.corp.vendor.Models.Category;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import hurrys.corp.vendor.Configurations.Session;
import hurrys.corp.vendor.R;


public class Category extends RecyclerView.Adapter<Category.ViewHolder> {

    ArrayList<String> category;
    Session session;

    public Category(ArrayList<String> inventories) {
        this.category = inventories;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v=(View) LayoutInflater.from(parent.getContext()).inflate(R.layout.products_row,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final Category.ViewHolder holder, final int position) {

        String name=category.get(position);

        session = new Session(holder.view.getContext());


        holder.name.setText(name);

        if(position%2 == 0){
            holder.linearLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
        } else {
            holder.linearLayout.setBackgroundColor(Color.parseColor("#F0F0F0"));
        }



        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                category.remove(position);
                String a="";
                for(int i=0;i<category.size();i++){
                    a+=category.get(i)+",";
                }

                    FirebaseDatabase.getInstance().getReference()
                            .child("Vendor")
                            .child(session.getusername())
                            .child("SubCategory")
                            .child(session.gettemp())
                            .child("ItemCategory")
                            .setValue(a);

                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return category.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {
        public final View view;

        TextView name;
        LinearLayout linearLayout,cancel;

        public ViewHolder(View mView) {
            super(mView);
            this.view = mView;

            name=mView.findViewById(R.id.name);
            cancel=mView.findViewById(R.id.cancel);
            linearLayout=mView.findViewById(R.id.linearLayout);

        }


        @Override
        public void onClick(View v) {
        }
    }

}



