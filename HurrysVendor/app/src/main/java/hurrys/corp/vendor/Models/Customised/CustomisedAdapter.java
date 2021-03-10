package hurrys.corp.vendor.Models.Customised;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import hurrys.corp.vendor.Configurations.Session;
import hurrys.corp.vendor.R;


public class CustomisedAdapter extends RecyclerView.Adapter<CustomisedAdapter.ViewHolder>{

    private ArrayList<Customised> carts;
    private Session session;

    public CustomisedAdapter(ArrayList<Customised> carts) {
        this.carts = carts;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v=(View) LayoutInflater.from(parent.getContext()).inflate(R.layout.customised_row,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final Customised cart = carts.get(position);
        holder.name.setText(cart.Name);
        holder.price.setText("\u00a3"+cart.Price);
    }

    @Override
    public int getItemCount() {
        if(carts!=null){
            return carts.size();
        }
        else {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {
        public final View view;
        TextView name,price;
       
        public ViewHolder(View view) {
            super(view);
            this.view = view;
             name = view.findViewById(R.id.name);
             price = view.findViewById(R.id.price);
        }

        @Override
        public void onClick(View v) {
        }
    }

}



