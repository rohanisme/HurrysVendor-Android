package hurrys.corp.vendor.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ahmedelsayed.sunmiprinterutill.PrintMe;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.sunmi.peripheral.printer.ICallback;
import com.sunmi.peripheral.printer.ILcdCallback;
import com.sunmi.peripheral.printer.ITax;
import com.sunmi.peripheral.printer.InnerPrinterCallback;
import com.sunmi.peripheral.printer.InnerPrinterException;
import com.sunmi.peripheral.printer.InnerPrinterManager;
import com.sunmi.peripheral.printer.InnerResultCallbcak;
import com.sunmi.peripheral.printer.SunmiPrinterService;
import com.sunmi.peripheral.printer.TransBean;

import cn.pedant.SweetAlert.SweetAlertDialog;
import hurrys.corp.vendor.Configurations.Session;
import hurrys.corp.vendor.Fragments.AddMenuFragment;
import hurrys.corp.vendor.Fragments.ApprovalWelcome;
import hurrys.corp.vendor.Fragments.CreateMeal;
import hurrys.corp.vendor.Fragments.Dashboard;
import hurrys.corp.vendor.Fragments.InventoryFragment;
import hurrys.corp.vendor.Fragments.PaymentFragment;
import hurrys.corp.vendor.Fragments.Profile;
import hurrys.corp.vendor.Fragments.Welcome;
import hurrys.corp.vendor.R;
import woyou.aidlservice.jiuiv5.IWoyouService;

public class MainActivity extends AppCompatActivity {

    LinearLayout b1,b2,b3;
    private ImageView i1,i2,i3;
    Session session;
    ProgressBar progressBar;
    private static final String TAG = "MyFirebaseMsgService";
    MediaPlayer player = null;

    private PrintMe printMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b1 = findViewById(R.id.b1);
        b2 = findViewById(R.id.b2);
        b3 = findViewById(R.id.b3);

        i1=findViewById(R.id.i1);
        i2=findViewById(R.id.i2);
        i3=findViewById(R.id.i3);
        progressBar=findViewById(R.id.progressbar);
        printMe =  new PrintMe(this);

        session = new Session(MainActivity.this);
        session.setisfirsttime("no");


        LinearLayout bottomnavigation=findViewById(R.id.bottomnavigation);
        bottomnavigation.setVisibility(View.GONE);

        FirebaseMessaging.getInstance().setAutoInitEnabled(true);

        if (!TextUtils.isEmpty(session.getusername())) {
            FirebaseInstanceId.getInstance().getInstanceId()
                    .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                        @Override
                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                            if (!task.isSuccessful()) {
                                Log.w(TAG, "getInstanceId failed", task.getException());
                                return;
                            }

                            // Get new Instance ID token
                            String token = task.getResult().getToken();
                            session.settoken(token);
                            FirebaseMessaging.getInstance().subscribeToTopic("Vendor");
                            FirebaseDatabase.getInstance().getReference().child("Vendor").child(session.getusername()).child("MessagingToken").setValue(token);
                        }
                    });
        }


        progressBar.setVisibility(View.VISIBLE);



//        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
//        if(user==null){
//            session.setusername("");
//            session.settoken("");
//            session.setname("");
//            session.setpp("");
//            session.setpassword("");
//            session.setextras("");
//            session.setnumber("");
//            session.setpincode("");
//            session.setsub("");
//            session.setrange("");
//            session.settoken("");
//            session.setcart("");
//            session.setdaaddress("");
//            session.setdadist("");
//            session.setdaf("");
//            session.setdal("");
//            session.setdaloc("");
//            session.setdaname("");
//            session.setstatus("");
//
//
//            startActivity(new Intent(this,
//                    Login.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK));
//            finish();
//        }





        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                i1.setImageResource(R.drawable.hb1);
                i2.setImageResource(R.drawable.b2);
                i3.setImageResource(R.drawable.b3);


                Fragment fragment = new Dashboard();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.frame_container, fragment).commit();



            }
        });


        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                i1.setImageResource(R.drawable.b1);
                i2.setImageResource(R.drawable.hb2);
                i3.setImageResource(R.drawable.b3);


                if(session.getcategory().equals("Food Delivery")||session.getcategory().equals("Home Food")) {
                    Fragment fragment = new AddMenuFragment();
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .addToBackStack(null)
                            .replace(R.id.frame_container, fragment).commit();
                }
                else{
                    Fragment fragment = new InventoryFragment();
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .addToBackStack(null)
                            .replace(R.id.frame_container, fragment).commit();
                }

            }
        });


        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                i1.setImageResource(R.drawable.b1);
                i2.setImageResource(R.drawable.b2);
                i3.setImageResource(R.drawable.hb3);

                Fragment fragment = new Profile();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.frame_container, fragment).commit();

            }
        });


        if(TextUtils.isEmpty(session.getselection())){
            session.setselection("New");
        }


        FirebaseDatabase.getInstance().getReference().child("Vendor")
                .child(session.getusername())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            if(dataSnapshot.child("Status").exists())
                                session.setstatus(dataSnapshot.child("Status").getValue().toString());
                            if(dataSnapshot.child("Submitted").exists())
                                session.setsubmitted("yes");
                            else
                                session.setsubmitted("no");

                            if(dataSnapshot.child("Category").exists()){
                                session.setcategory(dataSnapshot.child("Category").getValue().toString());
                            }

                            if(dataSnapshot.child("StoreOpenTime").exists()){
                                session.setstartime(dataSnapshot.child("StoreOpenTime").getValue().toString());
                            }

                            if(dataSnapshot.child("DeliveryTime").exists()){
                                session.setdeliverytime(dataSnapshot.child("DeliveryTime").getValue().toString());
                            }

                            if(dataSnapshot.child("StoreCloseTime").exists()){
                                session.setendtime(dataSnapshot.child("StoreCloseTime").getValue().toString());
                            }

                            if(dataSnapshot.child("Commision").exists())
                                session.setcommision(dataSnapshot.child("Commision").getValue().toString());
                            if(dataSnapshot.child("ApprovalStatus").exists()){
                                session.setapprovalstatus(dataSnapshot.child("ApprovalStatus").getValue().toString());
                                if(!session.getapprovalstatus().equals("Approved")){
                                    String comments="";
                                    if(dataSnapshot.child("Comments").exists()){
                                        comments=dataSnapshot.child("Comments").getValue().toString();
                                    }
                                    else{
                                        comments="Please proceed to fill the form ";
                                    }

                                    progressBar.setVisibility(View.GONE);
                                    Fragment fragment = new Welcome();
                                    Bundle bundle=new Bundle();
                                    bundle.putString("comments",comments);
                                    fragment.setArguments(bundle);
                                    FragmentManager fragmentManager = getSupportFragmentManager();
                                    fragmentManager.beginTransaction()
                                            .addToBackStack(null)
                                            .replace(R.id.frame_container, fragment).commit();

                                }
                                else{
                                    if(session.getapprovalfirst().equals("Yes")){
                                        session.setapprovalfirst("No");
                                        progressBar.setVisibility(View.GONE);
                                        Fragment fragment = new ApprovalWelcome();
                                        FragmentManager fragmentManager = getSupportFragmentManager();
                                        fragmentManager.beginTransaction()
                                                .addToBackStack(null)
                                                .replace(R.id.frame_container, fragment).commit();
                                    }
                                    else {
                                        progressBar.setVisibility(View.GONE);
                                        Fragment fragment = new Dashboard();
                                        FragmentManager fragmentManager = getSupportFragmentManager();
                                        fragmentManager.beginTransaction()
                                                .addToBackStack(null)
                                                .replace(R.id.frame_container, fragment).commit();
                                    }

                                }
                            }
                            else{
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                        else{
                            startActivity(new Intent(MainActivity.this,
                                    Login.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            finish();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }

                });


        player = MediaPlayer.create(getApplicationContext(), R.raw.sound);
        FirebaseDatabase.getInstance().getReference().child("Orders")
                .orderByChild("SellerStatus").equalTo(session.getusername())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            for(DataSnapshot v: dataSnapshot.getChildren()) {
                                if (v.child("Status").exists()) {
                                    if (v.child("Status").getValue().toString().equals("1")) {
                                        if(!player.isPlaying()) {
                                            player.start();
                                        }
                                    } else {
                                        stopPlaying();
                                    }
                                } else {
                                    stopPlaying();
                                }
                            }
                        }
                        else{
                            stopPlaying();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });


}

    private void stopPlaying() {
        if (player != null) {
            if(player.isPlaying()) {
                player.pause();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

//        final SweetAlertDialog sDialog = new SweetAlertDialog(this, SweetAlertDialog.BUTTON_CONFIRM);
//        sDialog.setTitleText("App Update!");
//        sDialog.setContentText("Please update the app for a faster and better experience!");
//        sDialog.setConfirmText("Update");
//        sDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//            @Override
//            public void onClick(SweetAlertDialog sweetAlertDialog) {
//                sweetAlertDialog.dismiss();
//                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
//                try {
//                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
//                } catch (android.content.ActivityNotFoundException anfe) {
//                    anfe.printStackTrace();
//                }
//            }
//        });
//
//        PackageManager manager = this.getPackageManager();
//        PackageInfo info = null;
//        try {
//            info = manager.getPackageInfo(this.getPackageName(), PackageManager.GET_ACTIVITIES);
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//
//
//        try {
//            assert info != null;
//            final int version = info.versionCode;
//
//
//            FirebaseDatabase.getInstance().getReference().child("AppContent").child("Application").addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    if (dataSnapshot.exists()) {
//                        double vno = Double.parseDouble(dataSnapshot.child("VendorAppVersion").getValue().toString());
//                        String imp = dataSnapshot.child("IMP").getValue().toString();
//                        if (version != vno) {
//                            if (imp.equals("No")) {
//                                sDialog.show();
//                                sDialog.setCancelText("No");
//                                sDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                                    @Override
//                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
//                                        sweetAlertDialog.dismiss();
//                                    }
//                                });
//                            } else {
//                                sDialog.show();
//                                sDialog.setCancelable(false);
//                            }
//                        }
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//                }
//            });
//        }
//        catch (Exception e){
//            e.printStackTrace();
//        }

    }


}
