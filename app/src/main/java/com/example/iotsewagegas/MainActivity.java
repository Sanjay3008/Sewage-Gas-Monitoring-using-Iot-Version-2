package com.example.iotsewagegas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;



import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;
    DrawerLayout drawerLayout;
    Toolbar toolbar;

    FirebaseAuth auth;
    int i=0;
    Report_Receiver broadcast = new Report_Receiver(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocalae();
        setContentView(R.layout.activity_main);

        navigationView=findViewById(R.id.navigationview);
        auth=FirebaseAuth.getInstance();

        Menu menu = navigationView.getMenu();
        for(int i=0;i<menu.size();i++){
            MenuItem item = menu.getItem(i);
            if(item.getItemId()==R.id.dashboard){
                item.setTitle(getString(R.string.dashboard));
            }
            else if(item.getItemId()==R.id.Language){
                item.setTitle(getString(R.string.language));
            }
            else if(item.getItemId()==R.id.share){
                item.setTitle(getString(R.string.share));
            }
            else if(item.getItemId()==R.id.logout){
                item.setTitle(getString(R.string.logout));
            }
        }

        toolbar=findViewById(R.id.toolbar);
        drawerLayout=findViewById(R.id.drawerlayout);
        navigationView.bringToFront();
        navigationView.setCheckedItem(R.id.dashboard);
        navigationView.setItemIconTintList(null);
        Fragment fragment =null;
        fragment=new DashboardFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.main,fragment);
        ft.commit();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment =null;
                int id=item.getItemId();
                int index;
                switch(id){
                    case R.id.dashboard:
                        index=0;
                        for(int i=0;i<4;i++){
                            navigationView.getMenu().getItem(i).setChecked(false);
                        }
                        fragment=new DashboardFragment();
                        navigationView.getMenu().getItem(index).setChecked(true);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.Language:
                        for(int i=0;i<4;i++){
                            navigationView.getMenu().getItem(i).setChecked(false);
                        }
                        drawerLayout.closeDrawer(GravityCompat.START);


                        String language[]={"English","Tamil","Hindi"};
                        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("Choose Language...");
                        builder.setSingleChoiceItems(language, -1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(which==0){
                                    setLocalae("en");
                                   recreate();
                                }
                                else if(which==1){
                                    setLocalae("ta");
                                    try{
                                        recreate();
                                    }catch (Exception e){
                                        Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                                    }

                                }
                                else if(which==2){
                                    setLocalae("hi");
                                   recreate();
                                }
                                dialog.dismiss();
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                        break;
                    case R.id.share:
                        for(int i=0;i<4;i++){
                            navigationView.getMenu().getItem(i).setChecked(false);
                        }
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.logout:
                        for(int i=0;i<4;i++){
                            navigationView.getMenu().getItem(i).setChecked(false);
                        }
                        drawerLayout.closeDrawer(GravityCompat.START);
                        auth.signOut();
                        Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                }
                if(fragment!=null){
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction ft = fragmentManager.beginTransaction();
                    ft.replace(R.id.main,fragment);
                    ft.commit();
                }
                return false;
            }
        });
        drawerToggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        drawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.tooglecolour));


    }

    private void setLocalae(String Lang){

        Locale locale = new Locale(Lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        getBaseContext().getResources().updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = getSharedPreferences("Settings",MODE_PRIVATE).edit();
        editor.putString("My_Lang",Lang);
        editor.apply();

    }

    private void loadLocalae(){

        SharedPreferences pref = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String lang = pref.getString("My_Lang","");
        setLocalae(lang);
    }

    @Override
    public void onBackPressed(){

        if(this.drawerLayout.isDrawerOpen(GravityCompat.START)){
            this.drawerLayout.closeDrawer(GravityCompat.START);
        }
        if(getSupportFragmentManager().getBackStackEntryCount()==0){
            if(i==1){
                super.onBackPressed();
            }
            else{
                Toast.makeText(this, "Press Back button again to exit", Toast.LENGTH_SHORT).show();
                i++;
            }


        }
        else{
            getSupportFragmentManager().popBackStack();
        }

    }
    Snackbar snackbar;
    boolean first=false;
    public  void alert(boolean noconnectivity){
        if(noconnectivity){
            DrawerLayout dl = findViewById(R.id.drawerlayout);
             snackbar = Snackbar
                    .make(dl,"Check Your Internet....",Snackbar.LENGTH_LONG);
            snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_FADE);
            snackbar.show();
            first=true;
        }
        else{
            if(first){
                DrawerLayout dl = findViewById(R.id.drawerlayout);
                snackbar = Snackbar
                        .make(dl,"Internet Connected!!! ",Snackbar.LENGTH_SHORT);
                snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_FADE);
                snackbar.setBackgroundTint(Color.parseColor("#FF4E5E30"));
                snackbar.show();
                first=false;
            }
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(broadcast,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(broadcast);
    }

}
