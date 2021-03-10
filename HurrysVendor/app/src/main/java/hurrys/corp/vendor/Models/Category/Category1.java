package hurrys.corp.vendor.Models.Category;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import hurrys.corp.vendor.Configurations.Session;
import hurrys.corp.vendor.R;


public class Category1 extends RecyclerView.Adapter<Category1.ViewHolder> {

    ArrayList<String> category;
    Session session;

    public Category1(ArrayList<String> inventories) {
        this.category = inventories;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v=(View) LayoutInflater.from(parent.getContext()).inflate(R.layout.products_row,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final Category1.ViewHolder holder, final int position) {

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



