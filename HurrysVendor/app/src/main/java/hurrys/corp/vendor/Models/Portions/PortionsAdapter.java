package hurrys.corp.vendor.Models.Portions;

import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.zcw.togglebutton.ToggleButton;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hurrys.corp.vendor.Activities.MainActivity;
import hurrys.corp.vendor.Models.Portions.Portions;
import hurrys.corp.vendor.R;

public class PortionsAdapter extends RecyclerView.Adapter<PortionsAdapter.ViewHolder> {

    private ArrayList<Portions> slit;


    public PortionsAdapter(ArrayList<Portions> cities) {
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
        Portions Portions=slit.get(position);

        holder.name.setText(Portions.PushId);
        holder.gender.setText(Portions.Name);

        holder.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeItem(position);
            }
        });

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(holder.view.getContext());
                MainActivity mainActivity = (MainActivity) holder.view.getContext();
                final View bottomSheetDialogView=mainActivity.getLayoutInflater().inflate(R.layout.bottom_variationportions,null);
                bottomSheetDialog.setContentView(bottomSheetDialogView);

                EditText addons=bottomSheetDialogView.findViewById(R.id.addons);
                EditText payment=bottomSheetDialogView.findViewById(R.id.payment);
                TextView cancel=bottomSheetDialogView.findViewById(R.id.cancel);
                ToggleButton defaultstatus=bottomSheetDialogView.findViewById(R.id.defaultstatus);
                Button add=bottomSheetDialogView.findViewById(R.id.add);
                final Boolean[] isChecked = {Portions.Default};

                if(Portions.Default){
                    defaultstatus.setToggleOn(true);
                    payment.setText("0");
                    payment.setEnabled(false);
                }
                else{
                    defaultstatus.setToggleOff(true);
                    payment.setEnabled(true);
                }

                defaultstatus.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
                    @Override
                    public void onToggle(boolean on) {
                        if(on){
                            isChecked[0] = true;
                            payment.setText("0");
                            payment.setEnabled(false);
                        }
                        else {
                            isChecked[0] = false;
                            payment.setEnabled(true);
                        }
                    }
                });

                Log.d("InitialStatus",""+isChecked[0]);


                payment.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(10,2)});

                addons.setText(Portions.PushId);
                payment.setText(Portions.Name.substring(1));

                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(TextUtils.isEmpty(addons.getText().toString())){
                            addons.setError("Enter Quantity");
                            addons.requestFocus();
                            return;
                        }

                        if(TextUtils.isEmpty(payment.getText().toString())){
                            payment.setError("Enter Price");
                            payment.requestFocus();
                            return;
                        }

                        Portions.setName("\u00a3"+payment.getText().toString());
                        Portions.setPushId(addons.getText().toString());
                        if (isChecked[0]) {
                            Portions.setDefault(true);
                            for(int i=0;i<slit.size();i++){
                                if(i!=position) {
                                    Portions Portions = slit.get(i);
                                    Portions.setDefault(false);
                                }
                            }
                        }
                        else {
                            Portions.setDefault(false);
                        }


                        Log.d("InitialStatus1",""+Portions.isDefault());

                        notifyDataSetChanged();
                        bottomSheetDialog.dismiss();
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        addons.setText("");
                        payment.setText("");
                        bottomSheetDialog.dismiss();
                    }
                });

                bottomSheetDialog.show();

            }
        });

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



    public class ViewHolder extends RecyclerView.ViewHolder  {
        public final View view;
        public final TextView name;
        public final TextView gender;
        public final CardView linearLayout;
        LinearLayout close;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            name = view.findViewById(R.id.name);
            gender = view.findViewById(R.id.gender);
            close = view.findViewById(R.id.close);
            linearLayout=view.findViewById(R.id.linearLayout);

        }

    }

    public class DecimalDigitsInputFilter implements InputFilter {

        Pattern mPattern;

        public DecimalDigitsInputFilter(int digitsBeforeZero,int digitsAfterZero) {
            mPattern=Pattern.compile("[0-9]{0," + (digitsBeforeZero-1) + "}+((\\.[0-9]{0," + (digitsAfterZero-1) + "})?)||(\\.)?");
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            Matcher matcher=mPattern.matcher(dest);
            if(!matcher.matches())
                return "";
            return null;
        }

    }
}



