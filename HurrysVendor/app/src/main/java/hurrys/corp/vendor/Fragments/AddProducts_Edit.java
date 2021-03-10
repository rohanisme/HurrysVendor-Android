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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.pedant.SweetAlert.SweetAlertDialog;
import hurrys.corp.vendor.Configurations.Session;
import hurrys.corp.vendor.Models.Category.Category;
import hurrys.corp.vendor.Models.Product.Product;
import hurrys.corp.vendor.Models.Product.ProductsAdapter;
import hurrys.corp.vendor.R;

import static android.app.Activity.RESULT_OK;

public class AddProducts_Edit extends Fragment {

    private EditText name,details,key,units,disclaimer,shell,manufacture,seller;
    private ImageView image,image2,image3,add;
    private Spinner itemcategory,subcategory;
    private RecyclerView r1;
    private Button submit,plus;

    private ProductsAdapter productsAdapter;
    private ArrayList<Product> products=new ArrayList<Product>();
    private ArrayList<String> sname1= new ArrayList<String>();
    private ArrayList<String> spushid= new ArrayList<String>();
    private ArrayList<String> category1 =new ArrayList<String>();
    private  int temp=0;
    private BottomSheetDialog bottomSheetDialog;

    private ArrayList<String> subcategoryname= new ArrayList<String>();
    private ArrayList<String> subcategorypushid= new ArrayList<String>();

    private String a;
    private int selection=0;
    int count = 0;

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
    private String path="No",path2="No",path3="No";

    private int mHour;
    private int mMinute;
    double gtot=0;
    private String pushid="";

    Category category1000;

    public AddProducts_Edit() {
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
        View v=inflater.inflate(R.layout.fragment_add_products__edit, container, false);

        name=v.findViewById(R.id.name);
        details=v.findViewById(R.id.details);
        image=v.findViewById(R.id.doc1);
        image2=v.findViewById(R.id.doc2);
        image3=v.findViewById(R.id.doc3);
        plus=v.findViewById(R.id.plus);
        itemcategory=v.findViewById(R.id.itemcategory);
        subcategory=v.findViewById(R.id.subcategory);
        key=v.findViewById(R.id.key);
        units=v.findViewById(R.id.units);
        disclaimer=v.findViewById(R.id.disclaimer);
        shell=v.findViewById(R.id.shell);
        manufacture=v.findViewById(R.id.manufacture);
        seller=v.findViewById(R.id.seller);
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
        sname1.clear();
        spushid.clear();
        subcategoryname.clear();
        subcategorypushid.clear();

        sname1.add("Select");
        spushid.add("Select");
        subcategorypushid.add("Select");
        subcategoryname.add("Select");

        category1.add("Select");
        if(getContext()!=null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.spinner, category1);
            itemcategory.setAdapter(adapter);
        }

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selection=1;
                final CharSequence[] items = {"Take Photo", "Choose from Library",
                        "Cancel"};
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), AlertDialog.THEME_HOLO_LIGHT);
                builder.setTitle("Add Photo!");

                //SET ITEMS AND THERE LISTENERS
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {

                        if (items[item].equals("Take Photo")) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                                    && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                                    != PackageManager.PERMISSION_GRANTED) {
                                requestPermissions(new String[]{Manifest.permission.CAMERA},
                                        REQUEST_CAMERA_ACCESS_PERMISSION);
                            } else {
                                cameraIntent();
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

        image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selection=2;
                final CharSequence[] items = {"Take Photo", "Choose from Library",
                        "Cancel"};
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), AlertDialog.THEME_HOLO_LIGHT);
                builder.setTitle("Add Photo!");

                //SET ITEMS AND THERE LISTENERS
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        selection=2;
                        if (items[item].equals("Take Photo")) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                                    && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                                    != PackageManager.PERMISSION_GRANTED) {
                                requestPermissions(new String[]{Manifest.permission.CAMERA},
                                        REQUEST_CAMERA_ACCESS_PERMISSION);
                            } else {
                                cameraIntent();
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

        image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selection=3;
                final CharSequence[] items = {"Take Photo", "Choose from Library",
                        "Cancel"};
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), AlertDialog.THEME_HOLO_LIGHT);
                builder.setTitle("Add Photo!");

                //SET ITEMS AND THERE LISTENERS
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {

                        if (items[item].equals("Take Photo")) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                                    && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                                    != PackageManager.PERMISSION_GRANTED) {
                                requestPermissions(new String[]{Manifest.permission.CAMERA},
                                        REQUEST_CAMERA_ACCESS_PERMISSION);
                            } else {
                                cameraIntent();
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

        v.setFocusableInTouchMode(true);
        v.requestFocus();
        v.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if(temp==0) {
                        temp++;
                        if(getContext()!=null) {
                            SweetAlertDialog sDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("Are you sure you want to exit! Your data will not be saved")
                                    .setConfirmText("Yes")
                                    .setCancelText("No")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            sweetAlertDialog.dismiss();
                                            if (getActivity() != null)
                                                getActivity().onBackPressed();
                                        }
                                    })
                                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            sweetAlertDialog.dismiss();
                                            temp = 0;
                                        }
                                    });

                            sDialog.setCancelable(false);
                            sDialog.show();
                        }
                    }
                }
                return false;
            }
        });

        ImageView back = v.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getContext()!=null) {
                    SweetAlertDialog sDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Are you sure you want to exit! Your data will not be saved")
                            .setConfirmText("Yes")
                            .setCancelText("No")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                    if (getActivity() != null)
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
        FirebaseDatabase.getInstance().getReference().child("Vendor")
                .child(session.getusername())
                .child("SubCategory")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            for(DataSnapshot v:dataSnapshot.getChildren()){
                                subcategoryname.add(v.child("Name").getValue().toString());
                                subcategorypushid.add(v.child("PushId").getValue().toString());
                            }
                        }
                        if(getContext()!=null) {
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.spinner, subcategoryname);
                            subcategory.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        subcategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i>0){

                    FirebaseDatabase.getInstance().getReference().child("Vendor")
                            .child(session.getusername())
                            .child("SubCategory")
                            .child(subcategorypushid.get(i))
                            .child("ItemCategory")
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists()){
                                        String temp=dataSnapshot.getValue().toString();
                                        a=temp;
                                        category1 = new ArrayList<String>(Arrays.asList(temp.split(",")));
                                        if(getContext()!=null) {
                                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.spinner, category1);
                                            itemcategory.setAdapter(adapter);
                                        }
                                    }
                                    else{
                                        category1.clear();
                                        category1.add("Select");
                                        if(getContext()!=null) {
                                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.spinner, category1);
                                            itemcategory.setAdapter(adapter);
                                        }
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        pushid=getArguments().getString("pushid");

        FirebaseDatabase.getInstance().getReference().child("Vendor")
                .child(session.getusername())
                .child("SubCategory")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            for(DataSnapshot v:dataSnapshot.getChildren()){
                                if(v.child("Name").exists()&&v.child("PushId").exists()) {
                                    subcategoryname.add(v.child("Name").getValue().toString());
                                    subcategorypushid.add(v.child("PushId").getValue().toString());
                                }
                            }

                            if(getContext()!=null) {
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.spinner, subcategoryname);
                                subcategory.setAdapter(adapter);
                            }

                            FirebaseDatabase.getInstance().getReference().child("Vendor").child(session.getusername()).child("Products").child(pushid)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if(dataSnapshot.exists()){
                                                name.setText(dataSnapshot.child("ItemName").getValue().toString());
                                                details.setText(dataSnapshot.child("ItemDescription").getValue().toString());
                                                disclaimer.setText(dataSnapshot.child("Disclaimer").getValue().toString());
                                                units.setText(dataSnapshot.child("Unit").getValue().toString());
                                                key.setText(dataSnapshot.child("Features").getValue().toString());
                                                seller.setText(dataSnapshot.child("Seller").getValue().toString());
                                                shell.setText(dataSnapshot.child("ShellLife").getValue().toString());
                                                manufacture.setText(dataSnapshot.child("Manufacture").getValue().toString());

                                                if(subcategoryname.indexOf(dataSnapshot.child("SubCategory").getValue().toString())>-1) {
                                                    subcategory.setSelection(subcategoryname.indexOf(dataSnapshot.child("SubCategory").getValue().toString()));

                                                    FirebaseDatabase.getInstance().getReference().child("Vendor")
                                                            .child(session.getusername())
                                                            .child("SubCategory")
                                                            .child(subcategorypushid.get(subcategory.getSelectedItemPosition()))
                                                            .child("ItemCategory")
                                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                    if (dataSnapshot.exists()) {
                                                                        String temp = dataSnapshot.getValue().toString();
                                                                        a = temp;
                                                                        category1 = new ArrayList<String>(Arrays.asList(temp.split(",")));
                                                                        if (getContext() != null) {
                                                                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.spinner, category1);
                                                                            itemcategory.setAdapter(adapter);
                                                                        }

                                                                        if (dataSnapshot.child("ItemCategory").exists()) {
                                                                            if (category1.indexOf(dataSnapshot.child("ItemCategory").getValue().toString()) > -1) {
                                                                                itemcategory.setSelection(category1.indexOf(dataSnapshot.child("ItemCategory").getValue().toString()));
                                                                            }
                                                                        }
                                                                    }
                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                }
                                                            });
                                                }





                                                if(dataSnapshot.child("Weights").exists()) {
                                                    for (DataSnapshot v : dataSnapshot.child("Weights").getChildren())
                                                    {
                                                        products.add(new Product((v.child("Name").getValue().toString()),"\u00a3"+v.child("Price").getValue().toString()));
                                                    }

                                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                                                    r1.setLayoutManager(mLayoutManager);

                                                    productsAdapter = new ProductsAdapter(products);

                                                    r1.setAdapter(productsAdapter);

                                                    r1.setVisibility(View.VISIBLE);

                                                }


                                                if(dataSnapshot.child("ItemImage1").exists()){
                                                    path=dataSnapshot.child("ItemImage1").getValue().toString();
                                                    if(!path.equals("No")){
                                                        Glide.with(getContext()).load(path).into(image);
                                                    }
                                                }
                                                if(dataSnapshot.child("ItemImage2").exists()){
                                                    path2=dataSnapshot.child("ItemImage2").getValue().toString();
                                                    if(!path2.equals("No")){
                                                        Glide.with(getContext()).load(path2).into(image2);
                                                    }
                                                }
                                                if(dataSnapshot.child("ItemImage3").exists()){
                                                    path3=dataSnapshot.child("ItemImage3").getValue().toString();
                                                    if(!path3.equals("No")){
                                                        Glide.with(getContext()).load(path3).into(image3);
                                                    }
                                                }

                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });


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

                if(subcategory.getSelectedItem().toString().equals("Select")){
                    Toast.makeText(getContext(),"Select Product Category",Toast.LENGTH_LONG).show();
                    return;
                }

                if(!subcategoryname.contains(subcategory.getSelectedItem().toString())){
                    Toast.makeText(getContext(),"Select Product Category",Toast.LENGTH_LONG).show();
                    return;
                }

                session.settemp(""+(subcategorypushid.get(subcategoryname.indexOf(subcategory.getSelectedItem().toString()))));

                FirebaseDatabase.getInstance().getReference().child("Vendor")
                        .child(session.getusername())
                        .child("SubCategory")
                        .child((subcategorypushid.get(subcategoryname.indexOf(subcategory.getSelectedItem().toString()))))
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
                                    category1000 = new Category(category1);
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
                                .child("SubCategory")
                                .child((subcategorypushid.get(subcategoryname.indexOf(subcategory.getSelectedItem().toString()))))
                                .child("ItemCategory")
                                .setValue(a);

                        categoryname.setText("");

                        FirebaseDatabase.getInstance().getReference().child("Vendor")
                                .child(session.getusername())
                                .child("SubCategory")
                                .child((subcategorypushid.get(subcategoryname.indexOf(subcategory.getSelectedItem().toString()))))
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
                                            category1000 = new Category(category1);
                                            recyclerView.setAdapter(category1000);

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                        FirebaseDatabase.getInstance().getReference().child("Vendor")
                                .child(session.getusername())
                                .child("SubCategory")
                                .child((subcategorypushid.get(subcategoryname.indexOf(subcategory.getSelectedItem().toString()))))
                                .child("ItemCategory")
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.exists()){
                                            String temp=dataSnapshot.getValue().toString();
                                            a=temp;
                                            category1 = new ArrayList<String>(Arrays.asList(temp.split(",")));
                                            if(getContext()!=null) {
                                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.spinner, category1);
                                                itemcategory.setAdapter(adapter);
                                            }
                                        }
                                        else{
                                            category1.clear();
                                            category1.add("Select");
                                            if(getContext()!=null) {
                                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.spinner, category1);
                                                itemcategory.setAdapter(adapter);
                                            }
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
        final View bottomSheetDialogView=getLayoutInflater().inflate(R.layout.bottom_variationsothers_edit,null);
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

                View v = getActivity().getCurrentFocus();
                if (v != null) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
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
                if (subcategory.getSelectedItem().toString().equals("Select")){
                    Toast.makeText(getActivity(),"Select SubCategory",Toast.LENGTH_LONG).show();
                    return;
                }

                if (itemcategory.getSelectedItem().toString().equals("Select")){
                    Toast.makeText(getActivity(),"Select Product Sub Category",Toast.LENGTH_LONG).show();
                    return;
                }

                if (productsAdapter.getItemCount()==0){
                    Toast.makeText(getContext(),"Add Quantity and prices",Toast.LENGTH_SHORT).show();
                    return;
                }

                DatabaseReference mref=FirebaseDatabase.getInstance().getReference().child("Vendor").child(session.getusername()).child("Products").child(pushid);
                mref.child("PushId").setValue(mref.getKey());
                mref.child("ItemName").setValue(name.getText().toString());
                mref.child("ItemDescription").setValue(details.getText().toString());
                mref.child("ItemCategory").setValue(itemcategory.getSelectedItem().toString());
                mref.child("SubCategory").setValue(subcategory.getSelectedItem().toString());
                mref.child("Status").setValue("Active");
                mref.child("ItemImage1").setValue(path);
                mref.child("ItemImage2").setValue(path2);
                mref.child("ItemImage3").setValue(path3);
                mref.child("Disclaimer").setValue(disclaimer.getText().toString());
                mref.child("Unit").setValue(units.getText().toString());
                mref.child("Features").setValue(key.getText().toString());
                mref.child("Seller").setValue(seller.getText().toString());
                mref.child("ShellLife").setValue(shell.getText().toString());
                mref.child("Manufacture").setValue(shell.getText().toString());

                if(count>0)
                    mref.child("ApprovalStatus").setValue("Pending");

                mref.child("Weights").setValue(null);

                for (int i = 0; i < productsAdapter.getItemCount(); i++) {
                    Product product = products.get(i);
                    DatabaseReference mref1=mref.child("Weights").push();
                    mref1.child("Name").setValue(product.PushId);
                    mref1.child("PushId").setValue(mref1.getKey());
                    mref1.child("Price").setValue(product.Name.substring(1));
                }


                SweetAlertDialog sDialog  = new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
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


                path="No";
                path3="No";
                path2="No";
                image.setImageResource(R.drawable.addimage);
                image2.setImageResource(R.drawable.addimage);
                image3.setImageResource(R.drawable.addimage);
                name.setText("");
                details.setText("");
                itemcategory.setSelection(0);
                subcategory.setSelection(0);
                submit.setVisibility(View.VISIBLE);
                session.settemp("");

                disclaimer.setText("");
                units.setText("");
                key.setText("");
                seller.setText("");
                shell.setText("");
                manufacture.setText("");


                products.clear();
                productsAdapter = new ProductsAdapter(products);
                r1.setAdapter(productsAdapter);

                if(getActivity()!=null){
                    getActivity().onBackPressed();
                }

            }
        });

        return v;
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

                File f = new File(String.valueOf(imageHoldUri));
                String imageName = f.getName();

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

                                        if(selection==1) {
                                            path=u[0];
                                            Glide.with(getContext()).load(path).into(image);
                                        }
                                        else if(selection==2) {
                                            path2=u[0];
                                            Glide.with(getContext()).load(path2).into(image2);
                                        }
                                        else if(selection==3) {
                                            path3=u[0];
                                            Glide.with(getContext()).load(path3).into(image3);
                                        }

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

                    File f = new File(String.valueOf(imageHoldUri));
                    String imageName = f.getName();

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
                                            if(selection==1) {
                                                path=u[0];
                                                Glide.with(getContext()).load(path).into(image);
                                            }
                                            else if(selection==2) {
                                                path2=u[0];
                                                Glide.with(getContext()).load(path2).into(image2);
                                            }
                                            else if(selection==3) {
                                                path3=u[0];
                                                Glide.with(getContext()).load(path3).into(image3);
                                            }
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
