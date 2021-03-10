package hurrys.corp.vendor.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bruce.pickerview.popwindow.DatePickerPopWin;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;
import com.hbb20.CountryCodePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import hurrys.corp.vendor.Configurations.Session;
import hurrys.corp.vendor.Models.Users;
import hurrys.corp.vendor.R;

import static java.security.AccessController.getContext;

public class RegisterDetails extends AppCompatActivity {

    EditText number,name,emailid,bname,address,postcode;
    TextView dname,ddob,demail,dbusiness,dcategory,daddress;
    TextView dob;
    ImageView next;
    ProgressBar progressbar;
    String uniqueid = "HVID", mobile = "";
    long id=0;
    double dbalance=0;
    Spinner category;
    Session session;
    CountryCodePicker country;
//    LinearLayout l1,l2,linearLayout2,stage1,stage2,blocks;
//    ImageView e1,e2,next,previous,previous1,next1;
    ArrayList<String> category1=new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_details);

        number=findViewById(R.id.mobilenumber);
        name=findViewById(R.id.name);
        dob=findViewById(R.id.dob);
        emailid=findViewById(R.id.email);
        bname=findViewById(R.id.bname);
        address=findViewById(R.id.address);
        postcode=findViewById(R.id.postcode);
        progressbar=findViewById(R.id.progressbar);
        category=findViewById(R.id.category);
        next=findViewById(R.id.next);
        dname=findViewById(R.id.dname);
        country=findViewById(R.id.country);

        next=findViewById(R.id.next);

        session=new Session(this);

        progressbar.setVisibility(View.GONE);


        int year = Calendar.getInstance().get(Calendar.YEAR) - 18;

        DatePickerPopWin pickerPopWin = new DatePickerPopWin.Builder(RegisterDetails.this, new DatePickerPopWin.OnDatePickedListener() {
            @Override
            public void onDatePickCompleted(int year, int month, int day, String dateDesc) {
                String a[]=dateDesc.split("-");
                dob.setText(a[2]+"/"+a[1]+"/"+a[0]);
            }
        }).textConfirm("CONFIRM") //text of confirm button
                .textCancel("CANCEL") //text of cancel button
                .btnTextSize(16) // button text size
                .viewTextSize(25) // pick view text size
                .colorCancel(Color.parseColor("#999999")) //color of cancel button
                .colorConfirm(Color.parseColor("#009900"))//color of confirm button
                .minYear(1920) //min year in loop
                .maxYear(year) // max year in loop
                .showDayMonthYear(true) // shows like dd mm yyyy (default is false)
                .dateChose("2000-01-01") // date chose when init popwindow
                .build();

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = RegisterDetails.this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                pickerPopWin.showPopWin(RegisterDetails.this);
            }
        });

        String pattern = "yyyy-MM-dd";
        final String dateInString = new SimpleDateFormat(pattern).format(new Date());
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        mobile=user.getPhoneNumber();
        number.setText(mobile);

        int len=mobile.length();
        if(len==0) {
            number.setText(mobile);
        }
        else if(len==13){
            country.setCountryForPhoneCode(Integer.parseInt(mobile.substring(1,3)));
            number.setText(mobile.substring(3,13));
        }
        else if(len==12){
            country.setCountryForPhoneCode(Integer.parseInt(mobile.substring(1,2)));
            number.setText(mobile.substring(2,12));
        }

        category1.clear();
        category1.add("Select");
        category1.add("Food Delivery");
        category1.add("Groceries & Essentials");
        category1.add("Home Food");
        category1.add("Meat & Fish");
        category1.add("Fruits & Vegetables");
        category1.add("Pet Supplies");
        category1.add("Medicine Store");
        category1.add("Fashion");
        category1.add("Health & Wellness");




        if(this.getApplicationContext()!=null){
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner , category1);
            category.setAdapter(adapter);
        }

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                next.setEnabled(false);

                if(TextUtils.isEmpty(name.getText().toString())){
                      Toast.makeText(RegisterDetails.this,"Enter Name",Toast.LENGTH_SHORT).show();
                    next.setEnabled(true);
                    return;
                }

//                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                if(TextUtils.isEmpty(emailid.getText().toString())){
                    Toast.makeText(RegisterDetails.this,"Enter EmailId",Toast.LENGTH_SHORT).show();
                    next.setEnabled(true);
                    return;
                }

//                if (!emailid.getText().toString().matches(emailPattern))
//                {
//                    emailid.setError("Enter Valid EmailId");
//                    emailid.requestFocus();
//                    next.setEnabled(true);
//                    return;
//                }

                if(dob.getText().toString().equals("DD/MM/YYYY")){
                    Toast.makeText(getApplicationContext(),"Select Date of Birth",Toast.LENGTH_SHORT).show();
                    next.setEnabled(true);
                    return;
                }

                if(getAge(dob.getText().toString())<18){
                    Toast.makeText(getApplicationContext(),"Must be greater than 18 Yrs",Toast.LENGTH_LONG).show();
                    next.setEnabled(true);
                    return;
                }

                if(TextUtils.isEmpty(bname.getText().toString())){
                    Toast.makeText(RegisterDetails.this,"Enter Business Name",Toast.LENGTH_SHORT).show();
                    next.setEnabled(true);
                    return;
                }

                if(category.getSelectedItem().toString().equals("Select")){
                    Toast.makeText(getApplicationContext(),"Select Category",Toast.LENGTH_SHORT).show();
                    next.setEnabled(true);
                    return;
                }

                if(TextUtils.isEmpty(address.getText().toString())){
                    Toast.makeText(RegisterDetails.this,"Enter Business Address",Toast.LENGTH_SHORT).show();
                    next.setEnabled(true);
                    return;
                }

                if(TextUtils.isEmpty(postcode.getText().toString())){
                    Toast.makeText(RegisterDetails.this,"Enter PostCode",Toast.LENGTH_SHORT).show();
                    next.setEnabled(true);
                    return;
                }

                progressbar.setVisibility(View.VISIBLE);

            DatabaseReference dref = FirebaseDatabase.getInstance().getReference().child("VendorID");

                dref.runTransaction(new Transaction.Handler() {
                    @NonNull
                    @Override
                    public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                        double value = 0;
                        if (currentData.getValue() != null) {
                            value = Long.parseLong(currentData.getValue().toString()) + 1;
                            id = Long.parseLong(currentData.getValue().toString()) + 1;
                        }
                        currentData.setValue(value);
                        return Transaction.success(currentData);
                    }

                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {


                        uniqueid+=id;

                        DatabaseReference mref = FirebaseDatabase.getInstance().getReference().child("Vendor").child(uniqueid);

                        Users users=new Users(uniqueid,
                                name.getText().toString(),
                                dob.getText().toString(),
                                "",
                                emailid.getText().toString(),
                                mobile,
                                "",
                                address.getText().toString(),
                                postcode.getText().toString(),
                                bname.getText().toString(),
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                category.getSelectedItem().toString(),
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "InActive",
                                "Pending",
                                dateInString,
                                5,
                                0,
                                0,
                                "",
                                "",
                                1000,
                                0,
                                "");

                        mref.setValue(users);

                        progressbar.setVisibility(View.GONE);
                        session.setsubmitted("no");
                        Intent intent = new Intent(RegisterDetails.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        session = new Session(RegisterDetails.this);
                        session.setusername(users.UserId);
                        session.setnumber(users.MobileNumber);
                        session.setname(users.Name);
                        session.setstorename(users.BusinessName);
                        session.setemail(users.Email);
                        session.setcategory(users.Category);
                        session.setapprovalfirst("Yes");
                        startActivity(intent);
                        finish();

                    }
                });

            }
        });
    }

    private int getAge(String dobString){

        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            date = sdf.parse(dobString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(date == null) return 0;

        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.setTime(date);

        int year = dob.get(Calendar.YEAR);
        int month = dob.get(Calendar.MONTH);
        int day = dob.get(Calendar.DAY_OF_MONTH);

        dob.set(year, month+1, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }

        return age;
    }
}
