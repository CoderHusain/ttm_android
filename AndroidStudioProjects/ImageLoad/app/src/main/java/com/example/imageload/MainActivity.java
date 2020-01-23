package com.example.imageload;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.reflect.Field;


public class MainActivity extends AppCompatActivity {

    public String PATH;
    ImageView imageView,imageView1;
    private RelativeLayout layout;

    private int screenHeight = 80, screenWidth=320;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);

        //TO increase and decrease brightness
        StringBuilder builder = new StringBuilder();
        builder.append("android : ").append(Build.VERSION.RELEASE);

        Field[] fields = Build.VERSION_CODES.class.getFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            int fieldValue = -1;

            try {
                fieldValue = field.getInt(new Object());
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

            if (fieldValue == Build.VERSION.SDK_INT) {
                builder.append(" : ").append(fieldName).append(" : ");
                builder.append("sdk=").append(fieldValue);
            }
        }

        Log.d("Card Display: ", "OS: " + builder.toString());

        String url = "https://media-exp1.licdn.com/dms/image/C510BAQGXSkwC0-gIag/company-logo_200_200/0?e=2159024400&v=beta&t=9R49YbLqg5YT0pCBY0pLPgR4Y93A6Euf5PSPf9kwCCQ";

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

