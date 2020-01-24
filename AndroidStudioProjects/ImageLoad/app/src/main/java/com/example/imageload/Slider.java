package com.example.imageload;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;

import android.util.Log;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import android.widget.ViewFlipper;

import com.google.gson.Gson;

import com.squareup.picasso.Picasso;



public class Slider extends AppCompatActivity {
    private static final String TAG =
            "LocUpload";
    ViewFlipper viewFlipper,viewFlipper1;
    private int screenHeight = 80, screenWidth=320;
    private RelativeLayout layout;
    public Media[] media;

    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onResume() {
        super.onResume();
        if(broadcastReceiver == null){
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    Log.d(TAG, intent.getExtras().get("data").toString());
                }
            };
            registerReceiver(broadcastReceiver, new IntentFilter("location_update"));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(broadcastReceiver != null){
            unregisterReceiver(broadcastReceiver);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_slider);

        Gson gson = new Gson();
        String json = "[\n" +
                "{\n" +
                "'mediaId':0,\n" +
                "'mediaType': 'Image',\n" +
                "'mediaUrl': 'https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcTDzvNd1W-a9iTayy-S7VNHLHpeP5MvhuUpRELoWjr3tZR-y8bG',\n" +
                "'mediaCount':  0\n" +
                "},\n" +
                "{\n" +
                "'mediaId':1,\n" +
                "'mediaType': 'Image',\n" +
                "'mediaUrl': 'https://cdn.pixabay.com/photo/2015/12/01/20/28/road-1072823__340.jpg',\n" +
                "'mediaCount':  0\n" +
                "},\n" +
                "{\n" +
                "'mediaId': 2,\n" +
                "'mediaType': 'Image',\n" +
                "'mediaUrl': 'https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcSHNxBZIBnA5OOif47Oz8WSnYUFlMV2oXIqW26zo6Zs17NKcUSp',\n" +
                "'mediaCount':  0\n" +
                "}\n" +
                "]";

        media = gson.fromJson(json, Media[].class);

        loadSlider();
        startService(new Intent(this, locationService.class));

    }






    
    public void checkRequestPerimssion() {
        if (ContextCompat.checkSelfPermission(Slider.this, Manifest.permission.RECORD_AUDIO)
                + ContextCompat.checkSelfPermission(
                Slider.this, Manifest.permission.ACCESS_FINE_LOCATION)
                + ContextCompat.checkSelfPermission(
                Slider.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                + ContextCompat.checkSelfPermission(
                Slider.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                + ContextCompat.checkSelfPermission(
                Slider.this, Manifest.permission.READ_PHONE_STATE)
                + ContextCompat.checkSelfPermission(
                Slider.this, Manifest.permission.RECEIVE_BOOT_COMPLETED)
                != PackageManager.PERMISSION_GRANTED) {
        }
    }





    private void loadSlider(){
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(screenWidth, screenHeight);
        layout = new RelativeLayout(this);
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

//        String videoPath= "android.resource://" + getPackageName() + "/" + R.raw.movie;



//        String mediaUrl[] = new String[]{
//                "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcTDzvNd1W-a9iTayy-S7VNHLHpeP5MvhuUpRELoWjr3tZR-y8bG",
//                "https://cdn.pixabay.com/photo/2015/12/01/20/28/road-1072823__340.jpg",
//                "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcSHNxBZIBnA5OOif47Oz8WSnYUFlMV2oXIqW26zo6Zs17NKcUSp",
//                videoPath
//        };

        media[0].mediaCount++;
        for(int i=0;i<media.length;i++){
            flipMedia(media[i].mediaUrl);
        }




//        final VideoView videoView = new VideoView(this);
//        final VideoView videoView1 = new VideoView(this);
//
//
//        videoView.setVideoURI(Uri.parse(videoPath));
//        videoView.start();
//
//        videoView1.setVideoURI(Uri.parse(videoPath));
//        videoView1.start();
//
//        viewFlipper.addView(videoView);
//        viewFlipper1.addView(videoView1);



    }



    public void flipMedia(String image){

        ImageView imageView = new ImageView(this);
        Picasso.get().load(image).into(imageView);

        ImageView imageView1 = new ImageView(this);
        Picasso.get().load(image).into(imageView1);

        viewFlipper.addView(imageView);
        viewFlipper1.addView(imageView1);

        viewFlipper.setFlipInterval(2000); //8 seconds
        viewFlipper.setAutoStart(true);
        viewFlipper.setInAnimation(this, android.R.anim.fade_in);
        viewFlipper.setOutAnimation(this, android.R.anim.fade_out);


        viewFlipper1.setFlipInterval(2000); //8 seconds
        viewFlipper1.setAutoStart(true);
        viewFlipper1.setInAnimation(this, android.R.anim.fade_in);
        viewFlipper1.setOutAnimation(this, android.R.anim.fade_out);

        viewFlipper.getInAnimation().setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation animation) {}
            public void onAnimationRepeat(Animation animation) {}
            public void onAnimationEnd(Animation animation) {
                int mId=viewFlipper.getDisplayedChild();
                media[mId].mediaCount++;
                System.out.println("Media Count for media "+mId+" is: "+media[mId].mediaCount);
            }
        });
    }


}
