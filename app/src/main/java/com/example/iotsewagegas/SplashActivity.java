package com.example.iotsewagegas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.util.SharedPreferencesUtils;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {

    ImageView image1,image2;
    private long splashtime=5000,ms=0;
    boolean splashctive=true,paused=false;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        TextView v1 = findViewById(R.id.splash1);
        TextView v2 = findViewById(R.id.splash2);
        v1.setText(getString(R.string.sewage_gas_monitoring));
        v2.setText(getString(R.string.save_your_life));
        auth=FirebaseAuth.getInstance();

        image1 = findViewById(R.id.sp_img_1);
        image2 = findViewById(R.id.sp_img_2);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.splashanimation);
        image1.setAnimation(animation);
        image2.setAnimation(animation);
        Thread thread = new Thread() {
            public void run() {
                try {
                    while (splashctive && ms < splashtime) {
                        if (!paused) {
                            ms += 150;
                        }
                        sleep(100);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if(online()){
                        gomain();
                    }
                    else{
                        ConstraintLayout constraintLayout = findViewById(R.id.cl);
                        Snackbar snackbar = Snackbar
                                .make(constraintLayout,"Check your Internet Connection!!!", Snackbar.LENGTH_INDEFINITE)
                                .setAction("Retry", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        recreate();
                                    }
                                });
                        snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
                        snackbar.show();
                    }

                }
            }
        };
        thread.start();
    }

    private boolean online() {
        ConnectivityManager connectivityManager =(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        return connectivityManager.getActiveNetworkInfo()!=null && connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    public void gomain(){

        if(auth.getCurrentUser()==null){
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
            finish();
        }
        else{
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();
        }

    }

}
