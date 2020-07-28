package com.example.iotsewagegas;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Config;
import android.widget.Toast;

import androidx.core.net.ConnectivityManagerCompat;

public class Report_Receiver extends BroadcastReceiver {
    LoginActivity loginActivity;
    MainActivity mainActivity;
    RegisterActivity registerActivity;

    private Activity calledactivity;

    public Report_Receiver(LoginActivity callingactivity){
        this.calledactivity=callingactivity;
        loginActivity=callingactivity;
    }

    public Report_Receiver(MainActivity callingactivity1){
        this.calledactivity=callingactivity1;
        mainActivity=callingactivity1;
    }
    public Report_Receiver(RegisterActivity callingactivity2){
        this.calledactivity=callingactivity2;
        registerActivity=callingactivity2;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction()==ConnectivityManager.CONNECTIVITY_ACTION){
            boolean noconnectvity = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY,false);
            if(noconnectvity){
                if(this.calledactivity instanceof LoginActivity){
                    loginActivity.alert(true);
                }
                else if(this.calledactivity instanceof MainActivity){
                    mainActivity.alert(true);
                }
                else if(this.calledactivity instanceof  RegisterActivity){
                    registerActivity.alert(true);
                }
            }
            else{
                if(this.calledactivity instanceof LoginActivity){
                    loginActivity.alert(false);
                }
                else if(this.calledactivity instanceof MainActivity){
                    mainActivity.alert(false);
                }
                else if(this.calledactivity instanceof  RegisterActivity){
                    registerActivity.alert(false);
                }
            }
        }
    }
}
