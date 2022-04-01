package hurrys.corp.vendor.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import hurrys.corp.vendor.Configurations.Session;
import hurrys.corp.vendor.Models.Safety.ViewHolder;
import hurrys.corp.vendor.Models.Safety.Safety;
import hurrys.corp.vendor.R;

import static android.app.Activity.RESULT_OK;


public class SafetyStandards extends Fragment {

    public SafetyStandards() {
        // Required empty public constructor
    }

    Button yes,no,add;
    EditText edtTitle,edtDescription,edtUrl;
    ImageView image,back;
    CheckBox mc,tc,wc,thc,fc,sc,suc;
    Spinner ms,me,ts,te,ws,we,ths,the,fs,fe,ss,se,sus,sue;
    Session session;
    ArrayList<String> time = new ArrayList<String>();
    int temp=0;
    String path = "No";

    private Uri imageUri;
    private Uri imageHoldUri = null;
    private static final int REQUEST_CAMERA = 3;
    private static final int REQUEST_CODE = 5;
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int REQUEST_CAMERA_ACCESS_PERMISSION = 5674;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    private static final int SELECT_FILE = 2;
    private final int RESULT_CROP = 400;
    private StorageReference mstorageReference;
    private ProgressBar progressBar2;

    private RecyclerView mRecyclerView;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_safety_standards, container, false);

        if(getActivity()!=null) {
            LinearLayout bottomnavigation = (getActivity()).findViewById(R.id.bottomnavigation);
            bottomnavigation.setVisibility(View.GONE);
        }

        viewInilization(v);
        setData();
        onClick();
        getData();

        return v;
    }

    public void viewInilization(View v){
        yes = v.findViewById(R.id.yes);
        no = v.findViewById(R.id.no);
        add = v.findViewById(R.id.add);
        edtTitle = v.findViewById(R.id.edtTitle);
        edtDescription = v.findViewById(R.id.edtDescription);
        edtUrl = v.findViewById(R.id.edtUrl);
        image = v.findViewById(R.id.image);
        mc = v.findViewById(R.id.mc);
        tc = v.findViewById(R.id.tc);
        wc = v.findViewById(R.id.wc);
        thc = v.findViewById(R.id.thc);
        fc = v.findViewById(R.id.fc);
        sc = v.findViewById(R.id.sc);
        suc = v.findViewById(R.id.suc);
        ms = v.findViewById(R.id.ms);
        me = v.findViewById(R.id.me);
        ts = v.findViewById(R.id.ts);
        te = v.findViewById(R.id.te);
        ws = v.findViewById(R.id.ws);
        we = v.findViewById(R.id.we);
        ths = v.findViewById(R.id.ths);
        the = v.findViewById(R.id.the);
        fs = v.findViewById(R.id.fs);
        fe = v.findViewById(R.id.fe);
        ss = v.findViewById(R.id.ss);
        se = v.findViewById(R.id.se);
        sus = v.findViewById(R.id.sus);
        sue = v.findViewById(R.id.sue);
        back = v.findViewById(R.id.back);
        mRecyclerView = v.findViewById(R.id.recyclerView);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        session = new Session(getActivity());

        mstorageReference= FirebaseStorage.getInstance().getReference();
    }

    public void onClick(){

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference().child("Vendor")
                        .child(session.getusername())
                        .child("MoreInfo")
                        .child("LocationDisplay")
                        .setValue("Yes");
                yes.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                no.setBackgroundColor(getResources().getColor(R.color.colorGrey));
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference().child("Vendor")
                        .child(session.getusername())
                        .child("MoreInfo")
                        .child("LocationDisplay")
                        .setValue("No");
                no.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                yes.setBackgroundColor(getResources().getColor(R.color.colorGrey));
            }
        });

        ms.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(temp!=0) {
                    FirebaseDatabase.getInstance().getReference().child("Vendor")
                            .child(session.getusername())
                            .child("MoreInfo")
                            .child("MondayStart")
                            .setValue(ms.getSelectedItem().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        me.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(temp!=0) {
                    FirebaseDatabase.getInstance().getReference().child("Vendor")
                            .child(session.getusername())
                            .child("MoreInfo")
                            .child("MondayEnd")
                            .setValue(me.getSelectedItem().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    FirebaseDatabase.getInstance().getReference().child("Vendor")
                            .child(session.getusername())
                            .child("MoreInfo")
                            .child("MondayClosed")
                            .setValue("Yes");
                }
                else{
                    FirebaseDatabase.getInstance().getReference().child("Vendor")
                            .child(session.getusername())
                            .child("MoreInfo")
                            .child("MondayClosed")
                            .setValue("No");
                }
            }
        });

        ts.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(temp!=0) {
                    FirebaseDatabase.getInstance().getReference().child("Vendor")
                            .child(session.getusername())
                            .child("MoreInfo")
                            .child("TuesdayStart")
                            .setValue(ts.getSelectedItem().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        te.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(temp!=0) {
                    FirebaseDatabase.getInstance().getReference().child("Vendor")
                            .child(session.getusername())
                            .child("MoreInfo")
                            .child("TuesdayEnd")
                            .setValue(te.getSelectedItem().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        tc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    FirebaseDatabase.getInstance().getReference().child("Vendor")
                            .child(session.getusername())
                            .child("MoreInfo")
                            .child("TuesdayClosed")
                            .setValue("Yes");
                }
                else{
                    FirebaseDatabase.getInstance().getReference().child("Vendor")
                            .child(session.getusername())
                            .child("MoreInfo")
                            .child("TuesdayClosed")
                            .setValue("No");
                }
            }
        });

        ws.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(temp!=0) {
                    FirebaseDatabase.getInstance().getReference().child("Vendor")
                            .child(session.getusername())
                            .child("MoreInfo")
                            .child("WednesdayStart")
                            .setValue(ws.getSelectedItem().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        we.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(temp!=0) {
                    FirebaseDatabase.getInstance().getReference().child("Vendor")
                            .child(session.getusername())
                            .child("MoreInfo")
                            .child("WednesdayEnd")
                            .setValue(we.getSelectedItem().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        wc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    FirebaseDatabase.getInstance().getReference().child("Vendor")
                            .child(session.getusername())
                            .child("MoreInfo")
                            .child("WednesdayClosed")
                            .setValue("Yes");
                }
                else{
                    FirebaseDatabase.getInstance().getReference().child("Vendor")
                            .child(session.getusername())
                            .child("MoreInfo")
                            .child("WednesdayClosed")
                            .setValue("No");
                }
            }
        });

        ths.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(temp!=0) {
                    FirebaseDatabase.getInstance().getReference().child("Vendor")
                            .child(session.getusername())
                            .child("MoreInfo")
                            .child("ThursdayStart")
                            .setValue(ths.getSelectedItem().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        the.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(temp!=0) {
                    FirebaseDatabase.getInstance().getReference().child("Vendor")
                            .child(session.getusername())
                            .child("MoreInfo")
                            .child("ThursdayEnd")
                            .setValue(the.getSelectedItem().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        thc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    FirebaseDatabase.getInstance().getReference().child("Vendor")
                            .child(session.getusername())
                            .child("MoreInfo")
                            .child("ThursdayClosed")
                            .setValue("Yes");
                }
                else{
                    FirebaseDatabase.getInstance().getReference().child("Vendor")
                            .child(session.getusername())
                            .child("MoreInfo")
                            .child("ThursdayClosed")
                            .setValue("No");
                }
            }
        });

        fs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(temp!=0) {
                    FirebaseDatabase.getInstance().getReference().child("Vendor")
                            .child(session.getusername())
                            .child("MoreInfo")
                            .child("FridayStart")
                            .setValue(fs.getSelectedItem().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        fe.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(temp!=0) {
                    FirebaseDatabase.getInstance().getReference().child("Vendor")
                            .child(session.getusername())
                            .child("MoreInfo")
                            .child("FridayEnd")
                            .setValue(fe.getSelectedItem().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        fc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    FirebaseDatabase.getInstance().getReference().child("Vendor")
                            .child(session.getusername())
                            .child("MoreInfo")
                            .child("FridayClosed")
                            .setValue("Yes");
                }
                else{
                    FirebaseDatabase.getInstance().getReference().child("Vendor")
                            .child(session.getusername())
                            .child("MoreInfo")
                            .child("FridayClosed")
                            .setValue("No");
                }
            }
        });

        ss.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(temp!=0) {
                    FirebaseDatabase.getInstance().getReference().child("Vendor")
                            .child(session.getusername())
                            .child("MoreInfo")
                            .child("SaturdayStart")
                            .setValue(ss.getSelectedItem().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        se.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(temp!=0) {
                    FirebaseDatabase.getInstance().getReference().child("Vendor")
                            .child(session.getusername())
                            .child("MoreInfo")
                            .child("SaturdayEnd")
                            .setValue(se.getSelectedItem().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    FirebaseDatabase.getInstance().getReference().child("Vendor")
                            .child(session.getusername())
                            .child("MoreInfo")
                            .child("SaturdayClosed")
                            .setValue("Yes");
                }
                else{
                    FirebaseDatabase.getInstance().getReference().child("Vendor")
                            .child(session.getusername())
                            .child("MoreInfo")
                            .child("SaturdayClosed")
                            .setValue("No");
                }
            }
        });

        sus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(temp!=0) {
                    FirebaseDatabase.getInstance().getReference().child("Vendor")
                            .child(session.getusername())
                            .child("MoreInfo")
                            .child("SundayStart")
                            .setValue(sus.getSelectedItem().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sue.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(temp!=0) {
                    FirebaseDatabase.getInstance().getReference().child("Vendor")
                            .child(session.getusername())
                            .child("MoreInfo")
                            .child("SundayEnd")
                            .setValue(sue.getSelectedItem().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        suc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    FirebaseDatabase.getInstance().getReference().child("Vendor")
                            .child(session.getusername())
                            .child("MoreInfo")
                            .child("SundayClosed")
                            .setValue("Yes");
                }
                else{
                    FirebaseDatabase.getInstance().getReference().child("Vendor")
                            .child(session.getusername())
                            .child("MoreInfo")
                            .child("SundayClosed")
                            .setValue("No");
                }
            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CharSequence[] items = {"Take Photo", "Choose from Library",
                        "Cancel"};
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), AlertDialog.THEME_HOLO_LIGHT);
                builder.setTitle("Add Photo!");

                //SET ITEMS AND THERE LISTENERS
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {

                        if (items[item].equals("Take Photo")) {
                            if(getContext()!=null) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                                        && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                                        != PackageManager.PERMISSION_GRANTED) {
                                    requestPermissions(new String[]{Manifest.permission.CAMERA},
                                            REQUEST_CAMERA_ACCESS_PERMISSION);
                                } else {
                                    cameraIntent();
                                }
                            }
                        } else if (items[item].equals("Choose from Library")) {
                            galleryIntent();
                        } else if (items[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getActivity()!=null)
                    getActivity().onBackPressed();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference mref = FirebaseDatabase.getInstance().getReference().child("Vendor").child(session.getusername())
                        .child("MoreInfo")
                        .child("AdditionalInformation").push();

                mref.child("PushId").setValue(mref.getKey());
                mref.child("Title").setValue(edtTitle.getText().toString());
                mref.child("Description").setValue(edtDescription.getText().toString());
                mref.child("Image").setValue(path);
                mref.child("Url").setValue(edtUrl.getText().toString());

                Toast.makeText(getContext(),"Information Added",Toast.LENGTH_LONG).show();
                edtDescription.setText("");
                edtTitle.setText("");
                edtUrl.setText("");
                path = "No";

                Glide.with(getContext()).load(R.drawable.addimage).into(image);

            }
        });

    }

    public void getData(){
        FirebaseDatabase.getInstance().getReference().child("Vendor")
                .child(session.getusername())
                .child("MoreInfo")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            if(dataSnapshot.child("LocationDisplay").exists()) {
                                if (dataSnapshot.child("LocationDisplay").getValue().toString().equals("Yes")) {
                                    yes.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                                    no.setBackgroundColor(getResources().getColor(R.color.colorGrey));
                                } else if (dataSnapshot.child("LocationDisplay").getValue().toString().equals("No")) {
                                    no.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                                    yes.setBackgroundColor(getResources().getColor(R.color.colorGrey));
                                }
                            }

                            if(dataSnapshot.child("MondayEnd").exists()) {
                               me.setSelection(time.indexOf(dataSnapshot.child("MondayEnd").getValue().toString()));
                            }

                            if(dataSnapshot.child("MondayStart").exists()) {
                                ms.setSelection(time.indexOf(dataSnapshot.child("MondayStart").getValue().toString()));
                            }

                            if(dataSnapshot.child("MondayClosed").exists()) {
                               if(dataSnapshot.child("MondayClosed").getValue().toString().equals("Yes")){
                                    mc.setChecked(true);
                               }
                               else{
                                   mc.setChecked(false);
                               }
                            }

                            if(dataSnapshot.child("TuesdayEnd").exists()) {
                                te.setSelection(time.indexOf(dataSnapshot.child("TuesdayEnd").getValue().toString()));
                            }

                            if(dataSnapshot.child("TuesdayStart").exists()) {
                                ts.setSelection(time.indexOf(dataSnapshot.child("TuesdayStart").getValue().toString()));
                            }

                            if(dataSnapshot.child("TuesdayClosed").exists()) {
                                if(dataSnapshot.child("TuesdayClosed").getValue().toString().equals("Yes")){
                                    tc.setChecked(true);
                                }
                                else{
                                    tc.setChecked(false);
                                }
                            }

                            if(dataSnapshot.child("WednesdayEnd").exists()) {
                                we.setSelection(time.indexOf(dataSnapshot.child("WednesdayEnd").getValue().toString()));
                            }

                            if(dataSnapshot.child("WednesdayStart").exists()) {
                                ws.setSelection(time.indexOf(dataSnapshot.child("WednesdayStart").getValue().toString()));
                            }

                            if(dataSnapshot.child("WednesdayClosed").exists()) {
                                if(dataSnapshot.child("WednesdayClosed").getValue().toString().equals("Yes")){
                                    wc.setChecked(true);
                                }
                                else{
                                    wc.setChecked(false);
                                }
                            }

                            if(dataSnapshot.child("ThursdayEnd").exists()) {
                                the.setSelection(time.indexOf(dataSnapshot.child("ThursdayEnd").getValue().toString()));
                            }

                            if(dataSnapshot.child("ThursdayStart").exists()) {
                                ths.setSelection(time.indexOf(dataSnapshot.child("ThursdayStart").getValue().toString()));
                            }

                            if(dataSnapshot.child("ThursdayClosed").exists()) {
                                if(dataSnapshot.child("ThursdayClosed").getValue().toString().equals("Yes")){
                                    thc.setChecked(true);
                                }
                                else{
                                    thc.setChecked(false);
                                }
                            }

                            if(dataSnapshot.child("FridayEnd").exists()) {
                                fe.setSelection(time.indexOf(dataSnapshot.child("FridayEnd").getValue().toString()));
                            }

                            if(dataSnapshot.child("FridayStart").exists()) {
                                fs.setSelection(time.indexOf(dataSnapshot.child("FridayStart").getValue().toString()));
                            }

                            if(dataSnapshot.child("FridayClosed").exists()) {
                                if(dataSnapshot.child("FridayClosed").getValue().toString().equals("Yes")){
                                    fc.setChecked(true);
                                }
                                else{
                                    fc.setChecked(false);
                                }
                            }

                            if(dataSnapshot.child("SaturdayEnd").exists()) {
                                se.setSelection(time.indexOf(dataSnapshot.child("SaturdayEnd").getValue().toString()));
                            }

                            if(dataSnapshot.child("SaturdayStart").exists()) {
                                ss.setSelection(time.indexOf(dataSnapshot.child("SaturdayStart").getValue().toString()));
                            }

                            if(dataSnapshot.child("SaturdayClosed").exists()) {
                                if(dataSnapshot.child("SaturdayClosed").getValue().toString().equals("Yes")){
                                    sc.setChecked(true);
                                }
                                else{
                                    sc.setChecked(false);
                                }
                            }

                            if(dataSnapshot.child("SundayEnd").exists()) {
                                sue.setSelection(time.indexOf(dataSnapshot.child("SundayEnd").getValue().toString()));
                            }

                            if(dataSnapshot.child("SundayStart").exists()) {
                                sus.setSelection(time.indexOf(dataSnapshot.child("SundayStart").getValue().toString()));
                            }

                            if(dataSnapshot.child("SundayClosed").exists()) {
                                if(dataSnapshot.child("SundayClosed").getValue().toString().equals("Yes")){
                                    suc.setChecked(true);
                                }
                                else{
                                    suc.setChecked(false);
                                }
                            }

                            temp++;

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


    }

    public void setData(){
        time.add("00:00");
        time.add("00:30");
        time.add("01:00");
        time.add("01:30");
        time.add("02:00");
        time.add("02:30");
        time.add("03:00");
        time.add("03:30");
        time.add("04:00");
        time.add("04:30");
        time.add("05:00");
        time.add("05:30");
        time.add("06:00");
        time.add("06:30");
        time.add("07:00");
        time.add("07:30");
        time.add("08:00");
        time.add("08:30");
        time.add("09:00");
        time.add("09:30");
        time.add("10:00");
        time.add("10:30");
        time.add("11:00");
        time.add("11:30");
        time.add("12:00");
        time.add("12:30");
        time.add("13:00");
        time.add("13:30");
        time.add("14:00");
        time.add("14:30");
        time.add("15:00");
        time.add("15:30");
        time.add("16:00");
        time.add("16:30");
        time.add("17:00");
        time.add("17:30");
        time.add("18:00");
        time.add("18:30");
        time.add("19:00");
        time.add("20:00");
        time.add("20:30");
        time.add("21:00");
        time.add("21:30");
        time.add("22:00");
        time.add("22:30");
        time.add("23:00");
        time.add("23:30");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item,time);
        ms.setAdapter(adapter);
        me.setAdapter(adapter);
        ts.setAdapter(adapter);
        te.setAdapter(adapter);
        ws.setAdapter(adapter);
        we.setAdapter(adapter);
        ths.setAdapter(adapter);
        the.setAdapter(adapter);
        fs.setAdapter(adapter);
        fe.setAdapter(adapter);
        ss.setAdapter(adapter);
        se.setAdapter(adapter);
        sus.setAdapter(adapter);
        sue.setAdapter(adapter);

        final Query firebasequery = FirebaseDatabase.getInstance().getReference().child("Vendor").child(session.getusername())
                .child("MoreInfo")
                .child("AdditionalInformation");



        FirebaseRecyclerAdapter<Safety, ViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Safety, ViewHolder>(
                        Safety.class,
                        R.layout.safetyrow,
                        ViewHolder.class,
                        firebasequery
                ) {
                    @Override
                    protected void populateViewHolder(ViewHolder viewHolder, Safety safety, int position) {
                        viewHolder.setDetails(getContext(),safety.Description,safety.Title,safety.Image,safety.Url);

                    }

                    @Override
                    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                        ViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);
                        viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
                            @Override
                            public void onItemClick(View v, int position) {
                            }

                            @Override
                            public void onItemLongClick(View v, int position) {

                            }
                        });
                        return viewHolder;
                    }

                };

        mRecyclerView.setAdapter(firebaseRecyclerAdapter);

    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "MyPicture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Photo taken on " + System.currentTimeMillis());
        imageUri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }

    private void galleryIntent() {
        //CHOOSE IMAGE FROM GALLERY
//        Log.d("gola", "entered here");
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //SAVE URI FROM GALLERY
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            imageHoldUri = data.getData();
            if (imageHoldUri != null) {
                final Date c = Calendar.getInstance().getTime();
                System.out.println("Current time => " + c);
                StorageReference riversRef = mstorageReference.child("FoodItems/" +c + ".jpg");
                final ProgressDialog progressDialog = new ProgressDialog(getContext());
                progressDialog.setTitle("Updating....!");
                progressDialog.show();
                progressDialog.setCancelable(false);
                riversRef.putFile(imageHoldUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // Get a URL to the uploaded content
                                StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                                final String[] u = new String[1];
                                storageRef.child("FoodItems/" +c + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        u[0] = uri.toString();
                                        path=u[0];
                                        if(getContext()!=null)
                                            Glide.with(getContext()).load(path).into(image);

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        // Handle any errors
                                    }
                                });
                                progressDialog.dismiss();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle unsuccessful uploads
                                Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                                progressDialog.setMessage((int) progress + "%Uploaded");
                            }
                        });

            } else {
                Toast.makeText(getContext(), "File Path Null", Toast.LENGTH_SHORT).show();
            }
        }
        else if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {


            imageHoldUri = imageUri;


            if (imageHoldUri != null) {
                if (imageHoldUri != null) {
                    final Date c = Calendar.getInstance().getTime();
                    System.out.println("Current time => " + c);

                    StorageReference riversRef = mstorageReference.child("FoodItems/" + c + ".jpg");
                    final ProgressDialog progressDialog = new ProgressDialog(getContext());
                    progressDialog.setTitle("Updating....!");
                    progressDialog.show();
                    progressDialog.setCancelable(false);
                    riversRef.putFile(imageHoldUri)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    // Get a URL to the uploaded content
                                    StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                                    final String[] u = new String[1];
                                    storageRef.child("FoodItems/" + c + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            u[0] = uri.toString();
                                            path = u[0];
                                            if(getContext()!=null)
                                                Glide.with(getContext()).load(path).into(image);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception exception) {
                                            // Handle any errors
                                        }
                                    });
                                    progressDialog.dismiss();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle unsuccessful uploads
                                    Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                                    progressDialog.setMessage((int) progress + "%Uploaded");
                                }
                            });
                }
            }
        }
    }

}

