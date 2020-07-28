package com.example.iotsewagegas;


import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;


/**
 * A simple {@link Fragment} subclass.
 */
public class GasMeasurement extends Fragment {

    private CircularProgressBar g1,g2,g3,g4,g5;
    private TextView gt1,gt2,gt3,gt4,gt5;
    private FirebaseDatabase db;
    private DatabaseReference ref1,ref2,ref3,ref4,ref5;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        g1=view.findViewById(R.id.ch4_progress);
        g2=view.findViewById(R.id.co2_progress);
        g3=view.findViewById(R.id.co_progress);
        g4=view.findViewById(R.id.h2s_progress);
        g5=view.findViewById(R.id.ch3_progress);
        gt1=view.findViewById(R.id.ch4_per);
        gt2=view.findViewById(R.id.co2_per);
        gt3=view.findViewById(R.id.co_per);
        gt4=view.findViewById(R.id.h2s_per);
        gt5=view.findViewById(R.id.ch3_per);
        g1.setProgressMax(5000);
        g2.setProgressMax(10000);
        g3.setProgressMax(3000);
        g4.setProgressMax(5000);
        g5.setProgressMax(10000);
        db = FirebaseDatabase.getInstance();
        ref1 = db.getReference().child("Concentration").child("Gas").child("CH4");
        ref2 = db.getReference().child("Concentration").child("Gas").child("CO2");
        ref3 = db.getReference().child("Concentration").child("Gas").child("CO");
        ref4 = db.getReference().child("Concentration").child("Gas").child("H2S");
        ref5 = db.getReference().child("Concentration").child("Gas").child("CH3");

        Toast.makeText(getContext(),"Data is loading from database",Toast.LENGTH_SHORT).show();
        ref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String ch4_con = dataSnapshot.getValue().toString();
                Integer gas1=Integer.parseInt(ch4_con);
                g1.setProgress(gas1);
                gt1.setText(ch4_con+getString(R.string.ppm));
                if(gas1<=500)
                {
                    g1.setProgressBarColor(Color.BLUE);
                }
                else if(gas1>501 && gas1<=1500)
                {
                    g1.setProgressBarColor(Color.GREEN);
                }
                else
                {
                    g1.setProgressBarColor(Color.RED);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(),"Error occured",Toast.LENGTH_SHORT).show();

            }
        });
        ref2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String co2_con = dataSnapshot.getValue().toString();
                Integer gas2=Integer.parseInt(co2_con);
                g2.setProgress(gas2);
                gt2.setText(co2_con+getString(R.string.ppm));
                if(gas2<=1000)
                {
                    g2.setProgressBarColor(Color.BLUE);
                }
                else if(gas2>1000 && gas2<=5000)
                {
                    g2.setProgressBarColor(Color.GREEN);
                }
                else
                {
                    g2.setProgressBarColor(Color.RED);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(),"Error occured",Toast.LENGTH_SHORT).show();

            }
        });
        ref3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String co_con = dataSnapshot.getValue().toString();
                Integer gas3=Integer.parseInt(co_con);
                g3.setProgress(gas3);
                gt3.setText(co_con+getString(R.string.ppm));
                if(gas3<=50)
                {
                    g3.setProgressBarColor(Color.BLUE);
                }
                else if(gas3>50 && gas3<=600)
                {
                    g3.setProgressBarColor(Color.GREEN);
                }
                else
                {
                    g3.setProgressBarColor(Color.RED);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(),"Error occured",Toast.LENGTH_SHORT).show();

            }
        });
        ref4.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String h2s_con = dataSnapshot.getValue().toString();
                Integer gas4=Integer.parseInt(h2s_con);
                g4.setProgress(gas4);
                gt4.setText(h2s_con+getString(R.string.ppm));
                if(gas4<=100)
                {
                    g4.setProgressBarColor(Color.BLUE);
                }
                else if(gas4>100 && gas4<=500)
                {
                    g4.setProgressBarColor(Color.GREEN);
                }
                else
                {
                    g4.setProgressBarColor(Color.RED);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(),"Error occured",Toast.LENGTH_SHORT).show();

            }
        });
        ref5.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String ch3_con = dataSnapshot.getValue().toString();
                Integer gas5=Integer.parseInt(ch3_con);
                g5.setProgress(gas5);
                gt5.setText(ch3_con+getString(R.string.ppm));
                if(gas5<=400)
                {
                    g5.setProgressBarColor(Color.BLUE);
                }
                else if(gas5>400 && gas5<=1500)
                {
                    g5.setProgressBarColor(Color.GREEN);
                }
                else
                {
                    g5.setProgressBarColor(Color.RED);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(),"Error occured",Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gas_measurement, container, false);
    }


}
