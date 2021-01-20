package hurrys.corp.vendor.Models.Product;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import hurrys.corp.vendor.R;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder> {

    private ArrayList<Product> slit;


    public ProductsAdapter(ArrayList<Product> cities) {
        this.slit = cities;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v=(View) LayoutInflater.from(parent.getContext()).inflate(R.layout.products_row1,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product=slit.get(position);

        holder.name.setText(product.PushId);
        holder.gender.setText(product.Name);

//        if(position%2 == 0){
//            holder.linearLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
//        } else {
//            holder.linearLayout.setBackgroundColor(Color.parseColor("#F0F0F0"));
//        }


    }

    public void removeItem(int position) {
        slit.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        if(slit!=null){
            return slit.size();
        }
        else {
            return 0;
        }
    }



    public class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {
        public final View view;
        public final TextView name;
        public final TextView gender;
        public final CardView linearLayout;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            name = view.findViewById(R.id.name);
            gender = view.findViewById(R.id.gender);
            linearLayout=view.findViewById(R.id.linearLayout);

            view.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {
            removeItem(getAdapterPosition()); //calls the method above to delete
        }
    }

}



