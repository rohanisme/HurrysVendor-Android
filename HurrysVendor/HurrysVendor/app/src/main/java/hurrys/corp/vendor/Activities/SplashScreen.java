package hurrys.corp.vendor.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;

import hurrys.corp.vendor.Configurations.Session;
import hurrys.corp.vendor.R;

public class SplashScreen extends AppCompatActivity {

    private static final String TAG ="LOGIN DATA" ;
    protected boolean _active = true;
    protected int _splashTime = 3000;
    private Session session;

    Animation anim;
    ImageView imageView;

    DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseApp.initializeApp(getApplicationContext());

        setContentView(R.layout.activity_splash_screen);

        session = new Session(SplashScreen.this);

        LottieAnimationView animation_view=findViewById(R.id.animation_view);

        animation_view.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                database = FirebaseDatabase.getInstance().getReference();
                if(TextUtils.isEmpty(session.getisfirsttime()))
                {
                    FirebaseDynamicLinks.getInstance()
                            .getDynamicLink(getIntent())
                            .addOnSuccessListener(SplashScreen.this, new OnSuccessListener<PendingDynamicLinkData>() {
                                @Override
                                public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                                    // Get deep link from result (may be null if no link is found)
                                    Uri deepLink = null;
                                    if (pendingDynamicLinkData != null) {
                                        deepLink = pendingDynamicLinkData.getLink();
                                    }

                                    if (deepLink != null
                                            && deepLink.getBooleanQueryParameter("invitedby", false)) {
                                        String referrerUid = deepLink.getQueryParameter("invitedby");
                                        session.setreferral(referrerUid);

                                        startActivity(new Intent(SplashScreen.this,
                                                Permissions.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                        finish();
                                    }
                                    else{
                                        startActivity(new Intent(SplashScreen.this,
                                                Permissions.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                        finish();
                                    }



                                }
                            });
                }
                else
                {
                    if (session.getusername() != "") {
                        startActivity(new Intent(SplashScreen.this,
                                MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finish();
                    } else {
                        startActivity(new Intent(SplashScreen.this,
                                Login.class));
                        finish();
                    }
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

//        VideoView videoView=findViewById(R.id.video);
//        String videopath="android.resource://"+getPackageName()+"/"+R.raw.video;
//        Uri uri=Uri.parse(videopath);
//        videoView.setVideoURI(uri);
//        videoView.start();
//
//
////        MediaController mediaController=new MediaController(this);
////        videoView.setMediaController(mediaController);
////        mediaController.setAnchorView(videoView);
//
//        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mediaPlayer) {
//                database = FirebaseDatabase.getInstance().getReference();
//                if(TextUtils.isEmpty(session.getisfirsttime()))
//                {
//                    FirebaseDynamicLinks.getInstance()
//                            .getDynamicLink(getIntent())
//                            .addOnSuccessListener(SplashScreen.this, new OnSuccessListener<PendingDynamicLinkData>() {
//                                @Override
//                                public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
//                                    // Get deep link from result (may be null if no link is found)
//                                    Uri deepLink = null;
//                                    if (pendingDynamicLinkData != null) {
//                                        deepLink = pendingDynamicLinkData.getLink();
//                                    }
//
//                                    if (deepLink != null
//                                            && deepLink.getBooleanQueryParameter("invitedby", false)) {
//                                        String referrerUid = deepLink.getQueryParameter("invitedby");
//                                        session.setreferral(referrerUid);
//
//                                        startActivity(new Intent(SplashScreen.this,
//                                                Permissions.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
//                                        finish();
//                                    }
//                                    else{
//                                        startActivity(new Intent(SplashScreen.this,
//                                                Permissions.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
//                                        finish();
//                                    }
//
//
//
//                                }
//                            });
//                }
//                else
//                {
//                    if (session.getusername() != "") {
//                        startActivity(new Intent(SplashScreen.this,
//                                MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
//                        finish();
//                    } else {
//                        startActivity(new Intent(SplashScreen.this,
//                                Login.class));
//                        finish();
//                    }
//                }
//            }
//        });




    }
}