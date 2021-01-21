package hurrys.corp.vendor.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
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
import androidx.fragment.app.FragmentManager;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.zcw.togglebutton.ToggleButton;

import java.util.Calendar;
import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import hurrys.corp.vendor.Activities.Login;
import hurrys.corp.vendor.Activities.Welcome;
import hurrys.corp.vendor.Configurations.Session;
import hurrys.corp.vendor.R;

import static android.app.Activity.RESULT_OK;

public class Profile extends Fragment {

    private ImageView i1,i2,i3;
    private Session session;

    private TextView name,userid,lastorder;
    private CircleImageView pp;
    private LinearLayout l2,l3,l4,l5,logout,edit;


    private Uri imageUri;
    private Uri imageHoldUri = null;
    private static final int REQUEST_CAMERA = 3;
    private static final int REQUEST_CODE = 5;
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int REQUEST_CAMERA_ACCESS_PERMISSION = 5674;
    private static final int SELECT_FILE = 2;
    private final int RESULT_CROP = 400;
    private StorageReference mstorageReference;
    private ProgressBar progressBar2;
    private ToggleButton status,selfpickup;

    private int mHour;
    private int mMinute;



    public Profile() {
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
        View v=inflater.inflate(R.layout.fragment_profile, container, false);

        LinearLayout bottomnavigation=(getActivity()).findViewById(R.id.bottomnavigation);
        bottomnavigation.setVisibility(View.VISIBLE);

        mstorageReference= FirebaseStorage.getInstance().getReference();


        i1=getActivity().findViewById(R.id.i1);
        i2=getActivity().findViewById(R.id.i2);
        i3=getActivity().findViewById(R.id.i3);
        session = new Session(getActivity());

        session.settemp("");



        i1.setImageResource(R.drawable.b1);
        i2.setImageResource(R.drawable.b2);
        i3.setImageResource(R.drawable.hb3);

        name=v.findViewById(R.id.name);
        userid=v.findViewById(R.id.userid);
        pp=v.findViewById(R.id.pp);
        logout=v.findViewById(R.id.logout);
        l2=v.findViewById(R.id.l2);
        l3=v.findViewById(R.id.l3);
        l4=v.findViewById(R.id.l4);
        l5=v.findViewById(R.id.l5);
        edit=v.findViewById(R.id.edit);
        status=v.findViewById(R.id.status);
        selfpickup=v.findViewById(R.id.selfpickup);
        lastorder=v.findViewById(R.id.lastorder);

        FirebaseDatabase.getInstance().getReference().child("Vendor")
                .child(session.getusername())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            if(dataSnapshot.child("LastOrder").exists())
                                lastorder.setText(dataSnapshot.child("LastOrder").getValue().toString());
                            if(dataSnapshot.child("SelfPickup").exists()){
                                if(dataSnapshot.child("SelfPickup").getValue().toString().equals("Active")){
                                    selfpickup.setToggleOn(true);
                                }
                                else{
                                    selfpickup.setToggleOff(false);
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        lastorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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


                                lastorder.setText(h + ":" + m);
                                FirebaseDatabase.getInstance().getReference().child("Vendor").child(session.getusername()).child("LastOrder").setValue(lastorder.getText().toString());

                            }
                        }, mHour, mMinute, true);


                timePickerDialog.show();
            }
        });


        if(session.getstatus().equals("Active")){
            status.setToggleOn(true);
        }
        else{
            status.setToggleOff(false);
        }

        status.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                if(on){
                    FirebaseDatabase.getInstance().getReference().child("Vendor").child(session.getusername()).child("Status").setValue("Active");
                    session.setstatus("Active");
                }
                else{
                    FirebaseDatabase.getInstance().getReference().child("Vendor").child(session.getusername()).child("Status").setValue("InActive");
                    session.setstatus("InActive");
                }
            }
        });

        selfpickup.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                if(on){
                    FirebaseDatabase.getInstance().getReference().child("Vendor").child(session.getusername()).child("SelfPickup").setValue("Active");
                    session.setstatus("Active");
                }
                else{
                    FirebaseDatabase.getInstance().getReference().child("Vendor").child(session.getusername()).child("SelfPickup").setValue("InActive");
                    session.setstatus("InActive");
                }
            }
        });


        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getActivity()!=null) {
                    Fragment fragment = new ProfileDetails();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .addToBackStack(null)
                            .replace(R.id.frame_container, fragment).commitAllowingStateLoss();
                }
            }
        });


        if(!TextUtils.isEmpty(session.getpp())) {
            Glide.with(getActivity())
                    .load(session.getpp())
                    .into(pp);
        }

        pp.setOnClickListener(new View.OnClickListener() {
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


        name.setText(session.getstorename());
        userid.setText(session.getusername());



        l2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getActivity()!=null) {
                    Fragment fragment = new PaymentFragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .addToBackStack(null)
                            .replace(R.id.frame_container, fragment).commitAllowingStateLoss();
                }
            }
        });

        l3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getActivity()!=null) {
                    Fragment fragment = new SafetyStandards();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .addToBackStack(null)
                            .replace(R.id.frame_container, fragment).commitAllowingStateLoss();
                }
            }
        });

        l4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(getActivity()!=null) {
                    Fragment fragment = new SupportFragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .addToBackStack(null)
                            .replace(R.id.frame_container, fragment).commitAllowingStateLoss();
                }




            }
        });

        l5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if(session.getcategory().equals("Food Delivery")||session.getcategory().equals("Home Food")) {
//                    Fragment fragment = new AddFoodItems();
//                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                    fragmentManager.beginTransaction()
//                            .addToBackStack(null)
//                            .replace(R.id.frame_container, fragment).commit();
//                }
//                else{
//                    Fragment fragment = new AddProducts();
//                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                    fragmentManager.beginTransaction()
//                            .addToBackStack(null)
//                            .replace(R.id.frame_container, fragment).commit();
//                }

                Fragment fragment = new AboutUs();
                FragmentManager fragmentManager =getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.frame_container, fragment).commit();

            }
        });



        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                session.setusername("");
                session.setemail("");
                session.setname("");
                session.setpp("");
                session.setpassword("");

                startActivity(new Intent(getActivity(),
                        Login.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                getActivity().finish();
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
                session = new Session(getActivity());
                Date c = Calendar.getInstance().getTime();
                System.out.println("Current time => " + c);

                String path = "Vendor/" + session.getusername() + ".jpg";
                StorageReference riversRef = mstorageReference.child(path);
                final ProgressDialog progressDialog = new ProgressDialog(getContext());
                progressDialog.setTitle("Updating....!");
                progressDialog.show();
                progressDialog.setCancelable(false);
                riversRef.putFile(imageHoldUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // Get a URL to the uploaded content
                                session.setpp("");
                                StorageReference storageRef = FirebaseStorage.getInstance().getReference();


                                final String[] u = new String[1];

                                storageRef.child("Vendor/" + session.getusername() + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        u[0] = uri.toString();
                                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Vendor").child(session.getusername());
                                        ref.child("PP").setValue(u[0]);

                                        session.setpp(u[0]);

//                                                ImageView Hpp=(ImageView)getActivity().findViewById(R.id.Hpp);
                                        Glide.with(getContext()).load(u[0]).into(pp);
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
                Date c = Calendar.getInstance().getTime();
                System.out.println("Current time => " + c);

                String path = "Vendor/" + session.getusername() + ".jpg";
                StorageReference riversRef = mstorageReference.child(path);
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

                                storageRef.child("Vendor/" + session.getusername() + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        u[0] = uri.toString();
                                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Vendor").child(session.getusername());
                                        ref.child("PP").setValue(u[0]);

                                        session.setpp(u[0]);

//                                                ImageView Hpp=(ImageView)getActivity().findViewById(R.id.Hpp);
                                        Glide.with(getContext()).load(u[0]).into(pp);
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



    @Override
    public void onResume(){
        super.onResume();
    }
}

