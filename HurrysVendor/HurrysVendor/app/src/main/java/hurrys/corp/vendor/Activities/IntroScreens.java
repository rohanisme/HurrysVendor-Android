package hurrys.corp.vendor.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jama.carouselview.CarouselView;
import com.jama.carouselview.CarouselViewListener;
import com.jama.carouselview.enums.IndicatorAnimationType;
import com.jama.carouselview.enums.OffsetType;

import hurrys.corp.vendor.R;

public class IntroScreens extends AppCompatActivity {

    TextView skip;
    ImageView next;
    CarouselView carouselView;
    int[] sampleImages = {R.drawable.image1, R.drawable.image2, R.drawable.image3};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_screens);

        carouselView=findViewById(R.id.carousel);
        skip=findViewById(R.id.skip);
        next=findViewById(R.id.next);


        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(IntroScreens.this,
                        Login.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(IntroScreens.this,
                        Login.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();

            }
        });

        carouselView.setSize(3);
        carouselView.setAutoPlay(true);
        carouselView.setResource(R.layout.image_carousel);
        carouselView.setIndicatorAnimationType(IndicatorAnimationType.THIN_WORM);
        carouselView.setCarouselOffset(OffsetType.CENTER);
        carouselView.setCarouselViewListener(new CarouselViewListener() {
            @Override
            public void onBindView(View view, int position) {
                // Example here is setting up a full image carousel
                ImageView imageView = view.findViewById(R.id.imageView);
                Glide.with(getApplicationContext()).load(sampleImages[position]).into(imageView);
            }
        });
        // After you finish setting up, show the CarouselView
        carouselView.show();

    }
}
