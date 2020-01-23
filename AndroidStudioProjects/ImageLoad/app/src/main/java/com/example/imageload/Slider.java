package com.example.imageload;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ViewFlipper;

import com.squareup.picasso.Picasso;

public class Slider extends AppCompatActivity {

    ViewFlipper viewFlipper,viewFlipper1;
    private int screenHeight = 80, screenWidth=320;
    private RelativeLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_slider);


        loadSlider();

    }

    private void loadSlider(){
        layout = new RelativeLayout(this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(screenWidth, screenHeight);
        setContentView(layout, params);

        viewFlipper = new ViewFlipper(this);

        viewFlipper1 = new ViewFlipper(this);
        params.setMargins(160, 0, 0, 0);
        viewFlipper1.setLayoutParams(params);


        layout.addView(viewFlipper);
        layout.addView(viewFlipper1);

        viewFlipper.getLayoutParams().height = 80;
        viewFlipper.getLayoutParams().width = 160;

        viewFlipper1.getLayoutParams().height = 80;
        viewFlipper1.getLayoutParams().width = 160;

        String imageUrl[] = new String[]{
                "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcTDzvNd1W-a9iTayy-S7VNHLHpeP5MvhuUpRELoWjr3tZR-y8bG",
                "https://cdn.pixabay.com/photo/2015/12/01/20/28/road-1072823__340.jpg",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcSHNxBZIBnA5OOif47Oz8WSnYUFlMV2oXIqW26zo6Zs17NKcUSp"
        };
        for(String image: imageUrl){
            flipImages(image);
        }
    }

    public void flipImages(String image){
        ImageView imageView = new ImageView(this);
        Picasso.get().load(image).into(imageView);

        ImageView imageView1 = new ImageView(this);
        Picasso.get().load(image).into(imageView1);

        viewFlipper.addView(imageView);
        viewFlipper.setFlipInterval(8000); //4 seconds
        viewFlipper.setAutoStart(true);
        viewFlipper.setInAnimation(this, android.R.anim.fade_in);
        viewFlipper.setOutAnimation(this, android.R.anim.fade_out);

        viewFlipper1.addView(imageView1);
        viewFlipper1.setFlipInterval(8000); //4 seconds
        viewFlipper1.setAutoStart(true);
        viewFlipper1.setInAnimation(this, android.R.anim.fade_in);
        viewFlipper1.setOutAnimation(this, android.R.anim.fade_out);


    }
}
