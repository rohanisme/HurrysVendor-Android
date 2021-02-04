package hurrys.corp.vendor.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.pedant.SweetAlert.SweetAlertDialog;
import hurrys.corp.vendor.Activities.MainActivity;
import hurrys.corp.vendor.Configurations.Session;
import hurrys.corp.vendor.Models.Category.Category;
import hurrys.corp.vendor.Models.Category.Category1;
import hurrys.corp.vendor.Models.Product.Product;
import hurrys.corp.vendor.Models.Product.ProductsAdapter;
import hurrys.corp.vendor.R;

import static android.app.Activity.RESULT_OK;

public class AddFoodItems extends Fragment {


    private EditText name,details,addons,payment,mrp,price;
    private TextView stime,etime;
    private Button plus;
    private Spinner itemcategory,category;
    private RecyclerView r1;
    private Button submit;
    private ImageView image,add;

    private ProductsAdapter productsAdapter;
    private ArrayList<Product> products=new ArrayList<Product>();
    private ArrayList<String> category1=new ArrayList<String>();
    private ArrayList<String> itemcategory1 = new ArrayList<String>();
    private ArrayList<String> sname1= new ArrayList<String>();
    private ArrayList<String> spushid= new ArrayList<String>();

    String a;

    private BottomSheetDialog bottomSheetDialog;

    private Session session;
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
    private String path="No";

    private int mHour;
    private int mMinute;
    double gtot=0;


    private Category1 category1000;


    public AddFoodItems() {
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
        View v=inflater.inflate(R.layout.fragment_add_food_items, container, false);

        name=v.findViewById(R.id.name);
        details=v.findViewById(R.id.details);
        addons=v.findViewById(R.id.addons);
        payment=v.findViewById(R.id.payment);
        mrp=v.findViewById(R.id.mrp);
        price=v.findViewById(R.id.price);
        image=v.findViewById(R.id.doc1);
        stime=v.findViewById(R.id.stime);
        etime=v.findViewById(R.id.etime);
        plus=v.findViewById(R.id.plus);
        itemcategory=v.findViewById(R.id.itemcategory);
        category=v.findViewById(R.id.category);
        r1=v.findViewById(R.id.r1);
        submit=v.findViewById(R.id.submit);
        add=v.findViewById(R.id.add);
        session=new Session(getActivity());

        if(getActivity()!=null) {
            LinearLayout bottomnavigation = (getActivity()).findViewById(R.id.bottomnavigation);
            bottomnavigation.setVisibility(View.GONE);
        }

        mstorageReference= FirebaseStorage.getInstance().getReference();

        submit.setVisibility(View.VISIBLE);

        products.clear();
        category1.clear();
        sname1.clear();
        spushid.clear();

        category1.add("Select");
        category1.add("Veg");
        category1.add("NonVeg");
        category1.add("Eggeterian");

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

        ImageView back = v.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getActivity()!=null)
                    getActivity().onBackPressed();
            }
        });

        if(getContext()!=null){
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),R.layout.spinner , category1);
            category.setAdapter(adapter);
        }






        stime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get Current Time
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                                mHour = hourOfDay;
                                mMinute = minute;
                                String h="",m="";
                                if (mHour>=0 && mHour<=9)
                                    h = "0" + hourOfDay;
                                else
                                    h = ""+ hourOfDay;

                                if(mMinute>=0 && mMinute <=9)
                                    m = "0" +minute;
                                else
                                    m =""+ minute;


                                stime.setText(h + ":" + m);


                            }
                        }, mHour, mMinute, true);


                timePickerDialog.show();
            }
        });


        etime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get Current Time
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                                mHour = hourOfDay;
                                mMinute = minute;
                                String h="",m="";
                                if (mHour>=0 && mHour<=9)
                                    h = "0" + hourOfDay;
                                else
                                    h = ""+ hourOfDay;

                                if(mMinute>=0 && mMinute <=9)
                                    m = "0" +minute;
                                else
                                    m =""+ minute;


                                etime.setText(h + ":" + m);


                            }
                        }, mHour, mMinute, true);


                timePickerDialog.show();
            }
        });



        if(!TextUtils.isEmpty(session.gettemp())){
            name.setText(session.gettemp());
        }



        itemcategory1.add("Select");
        if(getContext()!=null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),R.layout.spinner, itemcategory1);
            itemcategory.setAdapter(adapter);
        }


        FirebaseDatabase.getInstance().getReference().child("Vendor")
                .child(session.getusername())
                .child("ItemCategory")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            String temp=dataSnapshot.getValue().toString();
                            a=temp;
                           itemcategory1 = new ArrayList<String>(Arrays.asList(temp.split(",")));
                            if(getContext()!=null) {
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),R.layout.spinner, itemcategory1);
                                itemcategory.setAdapter(adapter);
                            }
                        }
                        else{
                            itemcategory1.clear();
                            itemcategory1.add("Select");
                            if(getContext()!=null) {
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.spinner, itemcategory1);
                                itemcategory.setAdapter(adapter);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog bottomSheetDialog1;
                bottomSheetDialog1=new BottomSheetDialog(getContext());
                final View bottomSheetDialogView=getActivity().getLayoutInflater().inflate(R.layout.bottom_subcategory,null);
                bottomSheetDialog1.setContentView(bottomSheetDialogView);
                RecyclerView recyclerView=bottomSheetDialogView.findViewById(R.id.recyclerview);
                ImageView submit=bottomSheetDialogView.findViewById(R.id.submit);;
                EditText categoryname=bottomSheetDialogView.findViewById(R.id.category);


                FirebaseDatabase.getInstance().getReference().child("Vendor")
                        .child(session.getusername())
                        .child("ItemCategory")
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    String temp=dataSnapshot.getValue().toString();
                                    a=temp;

                                    ArrayList<String> category1 = new ArrayList<String>(Arrays.asList(temp.split(",")));

                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                                    recyclerView.setLayoutManager(mLayoutManager);
                                    category1000 = new Category1(category1);
                                    recyclerView.setAdapter(category1000);

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        if(TextUtils.isEmpty(categoryname.getText().toString())){
                            categoryname.setError("Enter Category");
                            categoryname.requestFocus();
                            return;
                        }

                        a+=categoryname.getText().toString()+",";

                        FirebaseDatabase.getInstance().getReference().child("Vendor")
                                .child(session.getusername())
                                .child("ItemCategory")
                                .setValue(a);

                        categoryname.setText("");

                        FirebaseDatabase.getInstance().getReference().child("Vendor")
                                .child(session.getusername())
                                .child("ItemCategory")
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.exists()){
                                            String temp=dataSnapshot.getValue().toString();
                                            a=temp;

                                            ArrayList<String> category1 = new ArrayList<String>(Arrays.asList(temp.split(",")));

                                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                                            recyclerView.setLayoutManager(mLayoutManager);
                                            category1000 = new Category1(category1);
                                            recyclerView.setAdapter(category1000);

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                    }
                });



                bottomSheetDialog1.show();

            }
        });

        productsAdapter = new ProductsAdapter(products);

        bottomSheetDialog=new BottomSheetDialog(getContext());
        final View bottomSheetDialogView=getLayoutInflater().inflate(R.layout.bottom_variationsothers,null);
        bottomSheetDialog.setContentView(bottomSheetDialogView);

        EditText addons=bottomSheetDialogView.findViewById(R.id.addons);
        EditText payment=bottomSheetDialogView.findViewById(R.id.payment);
        TextView cancel=bottomSheetDialogView.findViewById(R.id.cancel);
        Button add=bottomSheetDialogView.findViewById(R.id.add);

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.show();
            }
        });

        payment.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(10,2)});

        productsAdapter = new ProductsAdapter(products);

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

                int temp=0,position=0;
                for(int i=0;i<productsAdapter.getItemCount();i++){
                    Product product = products.get(i);
                    if(product.Name.equals(addons.getText().toString())) {
                        temp++;
                        position = i;
                    }
                }

                if(temp==0)
                    products.add(new Product(addons.getText().toString(),"\u00a3"+payment.getText().toString()));
                else
                    products.set(position,new Product(addons.getText().toString(),"\u00a3"+payment.getText().toString()));


                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                r1.setLayoutManager(mLayoutManager);

                productsAdapter = new ProductsAdapter(products);

                r1.setAdapter(productsAdapter);

                addons.setText("");
                payment.setText("");

                if(getActivity()!=null) {
                    View v = getActivity().getCurrentFocus();
                    if (v != null) {
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        assert imm != null;
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                }

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

        payment.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(10,2)});
        price.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(10,2)});
        mrp.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(10,2)});





        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(name.getText().toString())){
                    name.setError("Enter Item Name");
                    name.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(details.getText().toString())){
                    details.setError("Enter Item Details");
                    details.requestFocus();
                    return;
                }


                if (TextUtils.isEmpty(mrp.getText().toString())){
                    mrp.setError("Enter Marked Price");
                    mrp.requestFocus();
                    return;
                }

//                if (TextUtils.isEmpty(price.getText().toString())){
//                    price.setError("Enter Offer Price");
//                    price.requestFocus();
//                    return;
//                }


                if(stime.getText().toString().equals("HH:MM")){
                    Toast.makeText(getContext(),"Select Start Time of Item Availability",Toast.LENGTH_LONG).show();
                    return;
                }

                if(etime.getText().toString().equals("HH:MM")){
                    Toast.makeText(getContext(),"Select End Time of Item Availability",Toast.LENGTH_LONG).show();
                    return;
                }


                if(sname1.indexOf(name.getText().toString())>-1){
                    name.setError("The Item already exists");
                    name.requestFocus();
                    return;
                }


                DatabaseReference mref=FirebaseDatabase.getInstance().getReference().child("Vendor").child(session.getusername()).child("Products").push();
                mref.child("PushId").setValue(mref.getKey());
                mref.child("ItemName").setValue(name.getText().toString());
                mref.child("ItemDescription").setValue(details.getText().toString());
                mref.child("FoodType").setValue(category.getSelectedItem().toString());
                mref.child("ItemCategory").setValue(itemcategory.getSelectedItem().toString());
                mref.child("SellingPrice").setValue(price.getText().toString());
                mref.child("MarkingPrice").setValue(mrp.getText().toString());
                mref.child("STime").setValue(stime.getText().toString());
                mref.child("ETime").setValue(etime.getText().toString());
                mref.child("ApprovalStatus").setValue("Pending");
                mref.child("Status").setValue("Active");
                mref.child("FoodImage").setValue(path);


                for (int i = 0; i < productsAdapter.getItemCount(); i++) {
                    Product product = products.get(i);
                    DatabaseReference mref1=mref.child("AddOns").push();
                    mref1.child("Name").setValue(product.PushId);
                    mref1.child("PushId").setValue(mref1.getKey());
                    mref1.child("Price").setValue(product.Name.substring(1));
                }




                if(getContext()!=null) {
                    SweetAlertDialog sDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Congrats!")
                            .setContentText("Item Added Successfully!")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                }
                            });
                    sDialog.setCancelable(false);
                    sDialog.show();
                }


                path="No";
                name.setText("");
                details.setText("");
                itemcategory.setSelection(0);
                category.setSelection(0);
                price.setText("");
                mrp.setText("");
                stime.setText("HH:MM");
                etime.setText("HH:MM");
                submit.setVisibility(View.VISIBLE);
                addons.setText("");
                payment.setText("");

                products.clear();
                productsAdapter = new ProductsAdapter(products);
                r1.setAdapter(productsAdapter);


                session.settemp("");

                if(getActivity()!=null)
                    getActivity().onBackPressed();


            }
        });

        return v;
    }

    @Override
    public void onStart(){
        super.onResume();
    }

    @Override
    public void onResume(){
        super.onResume();
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
