package com.example.iotsewagegas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.util.SharedPreferencesUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText email, pass;
    Button login, toreg, forgetpass;
    FirebaseAuth firebaseAuth;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = findViewById(R.id.email_login);
        pass = findViewById(R.id.pass_login);
        firebaseAuth = FirebaseAuth.getInstance();
        forgetpass = findViewById(R.id.forgotpass);
        progressBar = findViewById(R.id.progressbar);
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();





        login = findViewById(R.id.login);
        toreg = findViewById(R.id.gotoreg);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressBar.setVisibility(View.VISIBLE);
                final String email_login = email.getText().toString();
                final String password = pass.getText().toString();
                if (email_login.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please Enter Email", Toast.LENGTH_SHORT).show();
                    email.requestFocus();
                    progressBar.setVisibility(View.INVISIBLE);
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email_login).matches()) {
                    Toast.makeText(LoginActivity.this, "Please Enter Valid Email", Toast.LENGTH_SHORT).show();
                    email.getText().clear();
                    email.requestFocus();
                    progressBar.setVisibility(View.INVISIBLE);
                } else if (password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please Enter Password", Toast.LENGTH_SHORT).show();
                    pass.requestFocus();
                    progressBar.setVisibility(View.INVISIBLE);
                } else {

                    firebaseAuth.signInWithEmailAndPassword(email_login, password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(LoginActivity.this, "Logged In Successfully!", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                        progressBar.setVisibility(View.INVISIBLE);

                                    } else {
                                        Toast.makeText(LoginActivity.this, "Failure! Check Email or Password", Toast.LENGTH_SHORT).show();
                                        email.requestFocus();
                                        progressBar.setVisibility(View.INVISIBLE);
                                    }
                                }
                            });

                }
            }
        });
        toreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        forgetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final TextInputEditText resetmail = new TextInputEditText(v.getContext());
                final AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Reset password")
                        .setMessage("Enter Your Mail To Receive Reset Link");
                builder.setView(resetmail);

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        firebaseAuth.sendPasswordResetEmail(resetmail.getText().toString())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getApplicationContext(), "Reset Link Is Sent To Your Mail!!!", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(LoginActivity.this, "ERROR!! Reset Link Not Sent!" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });
    }
    Report_Receiver broadcast = new Report_Receiver(this);
    Snackbar snackbar;
    boolean first=false;
    public  void alert(boolean noconnectivity){
        if(noconnectivity){
            LinearLayout dl = findViewById(R.id.loginlayout);
            snackbar= Snackbar
                    .make(dl,"Check Your Internet....",Snackbar.LENGTH_LONG
                    );
            snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_FADE);
            snackbar.show();
            onPause();
            first=true;
        }
        else{
            onResume();
            if(first){
                LinearLayout dl = findViewById(R.id.loginlayout);
                snackbar= Snackbar
                        .make(dl,"Internet Connected Back!!!",Snackbar.LENGTH_SHORT);
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

    @Override
    protected void onPause() {
        super.onPause();

        login = findViewById(R.id.login);
        toreg = findViewById(R.id.gotoreg);
        forgetpass = findViewById(R.id.forgotpass);

        login.setEnabled(false);
        toreg.setEnabled(false);
        forgetpass.setEnabled(false);
    }

    @Override
    protected void onResume() {
        super.onResume();

        login = findViewById(R.id.login);
        toreg = findViewById(R.id.gotoreg);
        forgetpass = findViewById(R.id.forgotpass);
        login.setEnabled(true);
        toreg.setEnabled(true);
        forgetpass.setEnabled(true);

    }
}
