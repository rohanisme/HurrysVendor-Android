package hurrys.corp.vendor.Models.MenuTitles;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import hurrys.corp.vendor.Activities.MainActivity;
import hurrys.corp.vendor.Configurations.Session;
import hurrys.corp.vendor.Fragments.CreateItems;
import hurrys.corp.vendor.R;


public class TitleAdapter extends RecyclerView.Adapter<TitleAdapter.ViewHolder> {

    ArrayList<Title> title;
    Session session;

    public TitleAdapter(ArrayList<Title> inventories) {
        this.title = inventories;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.title_row, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final TitleAdapter.ViewHolder holder, final int position) {

        Title data = title.get(position);

        session = new Session(holder.view.getContext());

        holder.txtName.setText(data.Name);
        holder.txtNumber.setText(data.Required + " Required");

        holder.linearRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity mainActivity = (MainActivity) holder.view.getContext();
                Fragment fragment = new CreateItems();
                Bundle bundle = new Bundle();
                bundle.putString("title", holder.txtName.getText().toString());
                bundle.putString("tpushid", data.PushId);
                bundle.putString("pushid", data.ProductPushId);
                bundle.putString("index", "" + position);
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = mainActivity.getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.frame_container, fragment).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return title.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final View view;

        TextView txtName, txtNumber;
        LinearLayout linearRow;

        public ViewHolder(View mView) {
            super(mView);
            this.view = mView;

            txtName = mView.findViewById(R.id.txtName);
            txtNumber = mView.findViewById(R.id.txtNumber);
            linearRow = mView.findViewById(R.id.linearRow);
        }

        @Override
        public void onClick(View v) {

        }
    }
}



