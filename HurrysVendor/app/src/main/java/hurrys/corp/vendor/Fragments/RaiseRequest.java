package hurrys.corp.vendor.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import cn.pedant.SweetAlert.SweetAlertDialog;
import hurrys.corp.vendor.Configurations.Session;
import hurrys.corp.vendor.R;


public class RaiseRequest extends Fragment {


    public RaiseRequest() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private Session sessions;
    EditText name,email,number,request;
    Button add;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_raise_request, container, false);

        if(getActivity()!=null) {
            LinearLayout bottomnavigation=(getActivity()).findViewById(R.id.bottomnavigation);
            bottomnavigation.setVisibility(View.GONE);
        }

        ImageView back = v.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getActivity()!=null)
                    getActivity().onBackPressed();
            }
        });


        sessions = new Session(getContext());
        name = v.findViewById(R.id.name);
        email = v.findViewById(R.id.email);
        number = v.findViewById(R.id.number);
        request = v.findViewById(R.id.request);
        add = v.findViewById(R.id.add);


        name.setText(sessions.getusername());
        email.setText(sessions.getemail());
        number.setText(sessions.getnumber());

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(name.getText().toString())){
                    name.setError("Enter Name");
                    name.requestFocus();
                    return;
                }

                if(TextUtils.isEmpty(email.getText().toString())){
                    email.setError("Enter Email");
                    email.requestFocus();
                    return;
                }

                if(TextUtils.isEmpty(number.getText().toString())){
                    number.setError("Enter Number");
                    number.requestFocus();
                    return;
                }

                if(TextUtils.isEmpty(request.getText().toString())){
                    request.setError("Enter Request");
                    request.requestFocus();
                    return;
                }

                if (getContext() != null) {
                    SweetAlertDialog sDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Are you sure you want to raise request!")
                            .setConfirmText("Yes")
                            .setCancelText("No")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();

                                    DatabaseReference mref = FirebaseDatabase.getInstance().getReference().child("Requests").push();
                                    mref.child("PushId").setValue(mref.getKey());
                                    mref.child("Name").setValue(name.getText().toString());
                                    mref.child("Number").setValue(number.getText().toString());
                                    mref.child("Email").setValue(email.getText().toString());
                                    mref.child("Request").setValue(request.getText().toString());
                                    mref.child("Status").setValue("Open");
                                    mref.child("UserId").setValue(sessions.getusername());
                                    mref.child("Reference").setValue(""+System.currentTimeMillis());

                                    if(getActivity()!=null)
                                        getActivity().onBackPressed();


                                }
                            })
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                }
                            });

                    sDialog.setCancelable(false);
                    sDialog.show();
                }





            }
        });


        return v;
    }
}