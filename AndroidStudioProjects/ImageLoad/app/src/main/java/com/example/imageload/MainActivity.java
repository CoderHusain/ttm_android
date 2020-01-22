package com.example.imageload;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;


public class MainActivity extends AppCompatActivity {

    public String PATH;
    ImageView imageView,imageView1;
    private RelativeLayout layout;

    private int screenHeight = 80, screenWidth=320;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        String url = "https://media.gettyimages.com/photos/cropped-image-of-person-eye-picture-id942369796?s=2048x2048";

        Picasso.get().load(url).into(target);

    }


    private Target target = new Target() {
        @Override
        public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
            new Thread(new Runnable() {
                @Override
                public void run() {

                    ContextWrapper cw = new ContextWrapper(getApplicationContext());

                    // path to /data/data/yourapp/app_data/imageDir
                    File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);

                    // Create file in imageDir
                    File file=new File(directory,"image.jpg");

                    FileOutputStream fos = null;
                    try {

                        fos = new FileOutputStream(file);
                        // Use the compress method on the BitMap object to write image to the OutputStream
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);

                        fos.close();
                        PATH = directory.getAbsolutePath();

                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {

                                    // Stuff that updates the UI
                                    loadImageFromStorage(PATH);
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }
            }).start();
        }
        @Override
        public void onBitmapFailed(Exception e, Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {}
    };

    private void loadImageFromStorage(String path)
    {
        layout = new RelativeLayout(this);
        try {


            System.out.println("The path of the image is: "+path);
            File f=new File(path, "image.jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(screenWidth, screenHeight);
            setContentView(layout, params);

            RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(screenWidth, screenHeight);


            imageView = new ImageView(this);
            //setting margin for the image
            params.setMargins(160, 0, 0, 0);
            imageView.setLayoutParams(params);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);


            imageView1 = new ImageView(this);
            imageView1.setScaleType(ImageView.ScaleType.FIT_XY);



            layout.addView(imageView);
            layout.addView(imageView1);

            imageView.setImageBitmap(b);
            imageView1.setImageBitmap(b);

            imageView.getLayoutParams().height = 80;
            imageView.getLayoutParams().width = 160;

            imageView1.getLayoutParams().height = 80;
            imageView1.getLayoutParams().width = 160;
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

    }


}
