package hurrys.corp.vendor.Fragments;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.Html;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.pedant.SweetAlert.SweetAlertDialog;
import hurrys.corp.vendor.Configurations.Session;
import hurrys.corp.vendor.R;

public class Welcome extends Fragment {


    public Welcome() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_welcome, container, false);

        LinearLayout bottomnavigation=(getActivity()).findViewById(R.id.bottomnavigation);
        bottomnavigation.setVisibility(View.GONE);

        TextView comments,status,vendorid;
        Button start;
        Session session;
        ImageView image;
        session=new Session(getActivity());


        FragmentManager fm = getActivity().getSupportFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount() - 1; ++i) {
            fm.popBackStack();
        }


        v.setFocusableInTouchMode(true);
        v.requestFocus();
        v.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    System.exit(0);
                    return true;
                }
                return false;
            }
        });

        start=v.findViewById(R.id.start);
        comments=v.findViewById(R.id.comments);
        vendorid=v.findViewById(R.id.vendorid);
        status=v.findViewById(R.id.status);
        image=v.findViewById(R.id.image);

        String comments1=getArguments().getString("comments");
        comments.setText(comments1);

        if(!TextUtils.isEmpty(session.getsubmitted())){
            if(session.getsubmitted().equals("yes")) {
                start.setVisibility(View.GONE);
                if(comments.getText().toString().equals("Please proceed to fill the form "))
                    comments.setText("This process usually takes 24-48 Hrs");
            }
            else
                start.setVisibility(View.VISIBLE);
        }


        if(session.getapprovalstatus().equals("Pending")) {
            status.setText("Pending for Approval");
            image.setImageResource(R.drawable.w1);
            status.setTextColor(Color.parseColor("#0e4d97"));
        }
        else {
            start.setVisibility(View.VISIBLE);
            start.setText("Return to Registration");
        }


        vendorid.setText("Vendor ID : "+session.getusername());
        session.setaddress("");

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getActivity()!=null){
                    Fragment fragment = new Regsiteration();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .addToBackStack(null)
                            .replace(R.id.frame_container, fragment).commitAllowingStateLoss();
                }
            }
        });




        return v;
    }
}
