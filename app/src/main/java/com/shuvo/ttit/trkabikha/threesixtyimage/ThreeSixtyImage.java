package com.shuvo.ttit.trkabikha.threesixtyimage;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.shuvo.ttit.trkabikha.R;


import pl.rjuszczyk.panorama.viewer.PanoramaGLSurfaceView;

import static com.shuvo.ttit.trkabikha.projectDetails.ProjectDetails.URL_360;


public class ThreeSixtyImage extends AppCompatActivity {



    private PanoramaGLSurfaceView panoramaGLSurfaceView;
    ImageView loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_three_sixty_image);


        loading = findViewById(R.id.loading_image);
        loading.setVisibility(View.VISIBLE);
        panoramaGLSurfaceView = findViewById(R.id.panorama);

        Glide.with(ThreeSixtyImage.this)
                .asBitmap()
                .load(URL_360)
                .placeholder(R.drawable.loading)
                .error(R.drawable.loading_error)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        Matrix matrix = new Matrix();
                        matrix.preScale(-1.0f, 1.0f);
                        resource = Bitmap.createBitmap(resource, 0, 0, resource.getWidth(), resource.getHeight(), matrix, true);
                        panoramaGLSurfaceView.setPanoramaBitmap(resource);
                        System.out.println("getting resource");
                        loading.setVisibility(View.GONE);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        loading.setVisibility(View.GONE);
                        System.out.println("LOADING FINISH");
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                        loading.setVisibility(View.VISIBLE);
                        loading.setImageResource(R.drawable.loading_error);
                        System.out.println("LOADING ERROR");
                    }
                });

    }



    @Override
    protected void onResume() {
        super.onResume();
        panoramaGLSurfaceView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        panoramaGLSurfaceView.onPause();
//        if(panoramaGLSurfaceView != null)  {
//            panoramaGLSurfaceView.onPause();
//        } else {
//            finish();
//        }

    }
}