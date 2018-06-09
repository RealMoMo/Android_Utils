package com.hht.test;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.hht.test.particlesdrawable.ParticlesDrawable;
import com.hht.test.particlesdrawable.ParticlesView;

public class MainActivity extends AppCompatActivity {

    private ParticlesDrawable mDrawable;

    private ParticlesView mView;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //method 1:using ParticlesDrawable
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            mDrawable = (ParticlesDrawable) getDrawable(R.drawable.particles_customized);
//        } else {
//            mDrawable = new ParticlesDrawable();
//        }
//        //noinspection deprecation
//        findViewById(android.R.id.content).setBackgroundDrawable(mDrawable);

        //method 2:using ParticlesView
        setContentView(R.layout.activity_main);

        mView = findViewById(R.id.particleView);
    }

    @Override
    protected void onStart() {
        super.onStart();
       // mDrawable.start();
        mView.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
       // mDrawable.stop();
        mView.stop();
    }


}
