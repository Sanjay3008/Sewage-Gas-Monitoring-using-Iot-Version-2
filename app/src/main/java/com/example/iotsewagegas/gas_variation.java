package com.example.iotsewagegas;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class gas_variation extends Fragment implements View.OnClickListener {

    CardView cardView1,cardView2,cardView3,cardView4,cardView5;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cardView1=view.findViewById(R.id.ch4_graph);
        cardView1.setOnClickListener(this);
        cardView2=view.findViewById(R.id.co_graph);
        cardView2.setOnClickListener(this);
        cardView3=view.findViewById(R.id.co2_graph);
        cardView3.setOnClickListener(this);
        cardView4=view.findViewById(R.id.h2s_graph);
        cardView4.setOnClickListener(this);
        cardView5=view.findViewById(R.id.ch3_graph);
        cardView5.setOnClickListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gas_variation, container, false);
    }

    @Override
    public void onClick(View v) {
        FragmentManager fragmentManager;
        Fragment fragment = null;
        FragmentTransaction ft;
        switch (v.getId()){
            case R.id.co_graph:
                    fragment = new CarbonMonoxide();
                    fragmentManager=getFragmentManager();
                    ft=fragmentManager.beginTransaction();
                    ft.replace(R.id.main,fragment).addToBackStack("co_variation");
                    ft.commit();
                break;
            case R.id.ch4_graph:
                fragment = new Methane();
                fragmentManager=getFragmentManager();
                ft=fragmentManager.beginTransaction();
                ft.replace(R.id.main,fragment).addToBackStack("ch4_variation");
                ft.commit();
                break;
            case R.id.ch3_graph:
                fragment = new Ammonia();
                fragmentManager=getFragmentManager();
                ft=fragmentManager.beginTransaction();
                ft.replace(R.id.main,fragment).addToBackStack("ch3_variation");
                ft.commit();
                break;
            case R.id.h2s_graph:
                fragment = new HydrogenSulphide();
                fragmentManager=getFragmentManager();
                ft=fragmentManager.beginTransaction();
                ft.replace(R.id.main,fragment).addToBackStack("h2s_variation");
                ft.commit();
                break;
            case R.id.co2_graph:
                fragment = new CarbonDioxide();
                fragmentManager=getFragmentManager();
                ft=fragmentManager.beginTransaction();
                ft.replace(R.id.main,fragment).addToBackStack("co_variation");
                ft.commit();
                break;
        }
    }
}
