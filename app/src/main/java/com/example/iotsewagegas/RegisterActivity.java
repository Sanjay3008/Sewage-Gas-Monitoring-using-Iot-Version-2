package com.example.iotsewagegas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    TextInputEditText email,pass;
    Button register,tologin;
    FirebaseAuth firebaseAuth;
    ProgressBar progressBar;
    Report_Receiver broadcast = new Report_Receiver(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email=findViewById(R.id.email_register);
        pass = findViewById(R.id.pass_reg);
        firebaseAuth=FirebaseAuth.getInstance();
        register=findViewById(R.id.register);
        tologin=findViewById(R.id.gobacktologin);
        progressBar=findViewById(R.id.progressbar1);


        tologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String email_reg = email.getText().toString();
                String password = pass.getText().toString();
                if(email_reg.isEmpty()){
                    Toast.makeText(RegisterActivity.this, "Please Enter Email", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                    email.requestFocus();
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(email_reg).matches()){
                    Toast.makeText(RegisterActivity.this, "Please Enter Valid Email", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                    email.getText().clear();
                    email.requestFocus();
                }
                else if(password.isEmpty()){
                    Toast.makeText(RegisterActivity.this, "Please Enter Password", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                   pass.requestFocus();
                }
                else if(password.length()<7){
                    Toast.makeText(RegisterActivity.this, "Password Should be Atleast 7 characters", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                    pass.getText().clear();
                    pass.requestFocus();

                }
                else if(!email_reg.isEmpty() && !password.isEmpty() && password.length()>=7)
                {
                    firebaseAuth.createUserWithEmailAndPassword(email_reg,password)
                            .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(RegisterActivity.this, "successfully Registered..Log In to Continue..", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                                        startActivity(intent);
                                        progressBar.setVisibility(View.INVISIBLE);
                                        finish();

                                    }else{
                                        Toast.makeText(RegisterActivity.this, "failure", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }

    Snackbar snackbar;
    boolean first =false;
    public  void alert(boolean noconnectivity){
        if(noconnectivity){
            LinearLayout dl = findViewById(R.id.registerlayout);
            snackbar= Snackbar
                    .make(dl,"Check Your Internet....",Snackbar.LENGTH_LONG);
            snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_FADE);
            snackbar.show();
            onPause();
            first=true;
        }
        else{
            onResume();
            LinearLayout dl = findViewById(R.id.registerlayout);
            if(first){
                snackbar= Snackbar
                        .make(dl,"Internet Connected Back!!!",Snackbar.LENGTH_SHORT);
                snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_FADE);
                snackbar.setBackgroundTint(Color.parseColor("#FF4E5E30"));
                snackbar.show();
            }
            first=false;
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

    @Override
    protected void onPause() {
        super.onPause();

        register=findViewById(R.id.register);
        tologin=findViewById(R.id.gobacktologin);

        register.setEnabled(false);
        tologin.setEnabled(false);
    }

    @Override
    protected void onResume() {
        super.onResume();

        register=findViewById(R.id.register);
        tologin=findViewById(R.id.gobacktologin);

        register.setEnabled(true);
        tologin.setEnabled(true);

    }
}
