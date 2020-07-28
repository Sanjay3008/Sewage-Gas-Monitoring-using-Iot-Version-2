package com.example.iotsewagegas;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;



public class DashboardFragment extends Fragment implements View.OnClickListener {

    CardView gas_monitor, con_variation, information;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        gas_monitor = view.findViewById(R.id.gas_monitoring);
        con_variation = view.findViewById(R.id.concentration_variation);
        information = view.findViewById(R.id.info);
        gas_monitor.setOnClickListener(this);
        try {
            con_variation.setOnClickListener(this);
        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        information.setOnClickListener(this);






    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

       return inflater.inflate(R.layout.dashboard_layout,container,false);
    }

    @Override
    public void onClick(View v) {
        Fragment fragment = null;
        FragmentManager fragmentManager;
        FragmentTransaction ft;
        switch (v.getId()) {
            case R.id.gas_monitoring:
                try {
                    fragment = new GasMeasurement();
                    fragmentManager = getFragmentManager();
                    ft = fragmentManager.beginTransaction();
                    ft.replace(R.id.main, fragment).addToBackStack("measurement_fragment");
                    ft.commit();
                } catch (Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.concentration_variation:
                try {
                    fragment = new gas_variation();
                    fragmentManager = getFragmentManager();
                    ft = fragmentManager.beginTransaction();
                    ft.replace(R.id.main, fragment).addToBackStack("variation_fragment");
                    ft.commit();
                } catch (Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.info:
                try {
                    fragment = new information();
                    fragmentManager = getFragmentManager();
                    ft = fragmentManager.beginTransaction();
                    ft.replace(R.id.main, fragment).addToBackStack("info_fragment");
                    ;
                    ft.commit();
                    break;
                } catch (Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
        }
    }
}
