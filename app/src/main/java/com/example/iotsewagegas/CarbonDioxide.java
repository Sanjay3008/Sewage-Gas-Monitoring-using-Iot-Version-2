package com.example.iotsewagegas;


import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class CarbonDioxide extends Fragment {


    LineChart lineChart;
    RequestQueue requestQueue;
    ProgressBar progressBar;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        lineChart=view.findViewById(R.id.linechart_carbondioxide);
        requestQueue= Volley.newRequestQueue(getContext());
        progressBar=view.findViewById(R.id.prog_co2);
        String url ="https://api.thingspeak.com/channels/1102451/feeds.json";
        Toast.makeText(getContext(), "Data is loading from server....Plz wait", Toast.LENGTH_SHORT).show();
        progressBar.setVisibility(View.VISIBLE);
        JsonObjectRequest arrayRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                ArrayList<String> date = new ArrayList<>();
                ArrayList<Entry> con_values = new ArrayList<>();
                try {
                    JSONArray jsonArray = response.getJSONArray("feeds");
                    int j=0;
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        if(jsonObject.getString("field2")!="null"){
                            LocalDateTime l = LocalDateTime.ofInstant(Instant.parse(jsonObject.getString("created_at")),
                                    ZoneId.of("Asia/Kolkata"));
                            date.add(l.toString().substring(0,10)+" "+l.toString().substring(11));
                            con_values.add(new Entry(j,jsonObject.getInt("field2")));
                            j++;
                        }
                    }
                    LineDataSet set = new LineDataSet(con_values,"Carbondioxide");
                    set.setLineWidth(2f);
                    set.setColor(Color.parseColor("#FFAF3939"));
                    set.setCircleRadius(5f);
                    set.setCircleHoleColor(Color.parseColor("#FFAF3939"));
                    set.setCircleHoleRadius(1f);
                    set.setValueTextSize(6f);
                    set.setCircleColor(Color.parseColor("#FF883C3C"));
                    LineData data = new LineData(set);
                    XAxis axis = lineChart.getXAxis();
                    lineChart.getAxisRight().setEnabled(false);
                    axis.setPosition(XAxis.XAxisPosition.BOTTOM);
                    axis.setDrawGridLines(false);
                    axis.setDrawAxisLine(false);
                    axis.setGranularity(1f);
                    axis.setLabelCount(date.size());
                    axis.setValueFormatter(new IndexAxisValueFormatter(date));
                    axis.setLabelRotationAngle(270);
                    lineChart.setData(data);
                    lineChart.animateY(2000);
                    lineChart.setVisibleXRangeMaximum(10);
                    lineChart.setDragEnabled(true);
                    lineChart.setDescription(null);
                    lineChart.moveViewToX(data.getEntryCount());
                    lineChart.invalidate();
                    progressBar.setVisibility(View.INVISIBLE);
                    lineChart.setVisibility(View.VISIBLE);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(arrayRequest);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_carbon_dioxide, container, false);
    }

}
