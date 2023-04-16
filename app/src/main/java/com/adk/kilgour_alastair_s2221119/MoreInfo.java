/*
Name: Alastair Kilgour
SN: S2221119
Program: Computer
*/

package com.adk.kilgour_alastair_s2221119;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

public class MoreInfo extends AppCompatActivity implements OnMapReadyCallback {
    Earthquake quake;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_info);
        getSupportActionBar().hide();

        // gets info on selected quake
        Intent i = getIntent();
        quake = (Earthquake) i.getSerializableExtra("selectedQuake");
        i.getParcelableArrayListExtra("namehere");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        loadData();
        loadMap();
    }

    public void loadData() {

        if (quake != null) { // makes sure a quake has actually be passed
            TableLayout table = findViewById(R.id.moreInfoTable);

            TextView label1 = new TextView(this);
            label1.setTextSize(15);
            TextView info1 = new TextView(this);
            info1.setTextSize(15);

            TableRow row1 = new TableRow(this);
            row1.setPadding(5, 5, 5, 5);
            label1.setText("Date @Time:");
            label1.setPadding(5, 5, 5,5);
            label1.setTextColor(Color.parseColor("#e8e8e8"));
            info1.setText(quake.getDate() + " @" + quake.getTime());
            info1.setPadding(5, 5, 5,5);
            info1.setTextColor(Color.parseColor("#e8e8e8"));
            row1.addView(label1);
            row1.addView(info1);
            table.addView(row1);



            TextView label2 = new TextView(this);
            label2.setTextSize(15);
            TextView info2 = new TextView(this);
            info2.setTextSize(15);

            TableRow row2 = new TableRow(this);
            row2.setPadding(5, 5, 5, 5);
            label2.setText("Region, Locality:");
            label2.setPadding(5, 5, 5,5);
            label2.setTextColor(Color.parseColor("#e8e8e8"));
            info2.setText(quake.getLocality() + ", " + quake.getRegion());
            info2.setPadding(5, 5, 5,5);
            info2.setTextColor(Color.parseColor("#e8e8e8"));
            row2.addView(label2);
            row2.addView(info2);
            table.addView(row2);



            TextView label3 = new TextView(this);
            label3.setTextSize(15);
            TextView info3 = new TextView(this);
            info3.setTextSize(15);

            TableRow row3 = new TableRow(this);
            row3.setPadding(5, 5, 5, 5);
            label3.setText("Latitude Longitude:");
            label3.setPadding(5, 5, 5,5);
            label3.setTextColor(Color.parseColor("#e8e8e8"));
            info3.setText(quake.getLatitude() + "° " + quake.getLongitude() + "°");
            info3.setPadding(5, 5, 5,5);
            info3.setTextColor(Color.parseColor("#e8e8e8"));
            row3.addView(label3);
            row3.addView(info3);
            table.addView(row3);


            TextView label4 = new TextView(this);
            label4.setTextSize(15);
            TextView info4 = new TextView(this);
            info4.setTextSize(15);

            TableRow row4 = new TableRow(this);
            row4.setPadding(5, 5, 5, 5);
            label4.setText("Magnitude:");
            label4.setPadding(5, 5, 5,5);
            label4.setTextColor(Color.parseColor("#e8e8e8"));
            info4.setText(quake.getMagnitude());
            info4.setPadding(5, 5, 5,5);
            info4.setTextColor(Color.parseColor("#e8e8e8"));
            row4.addView(label4);
            row4.addView(info4);
            table.addView(row4);



            TextView label5 = new TextView(this);
            label5.setTextSize(15);
            TextView info5 = new TextView(this);
            info5.setTextSize(15);

            TableRow row5 = new TableRow(this);
            row5.setPadding(5, 5, 5, 5);
            label5.setText("Depth:");
            label5.setPadding(5, 5, 5,5);
            label5.setTextColor(Color.parseColor("#e8e8e8"));
            info5.setText(quake.getDepth() + "km");
            info5.setPadding(5, 5, 5,5);
            info5.setTextColor(Color.parseColor("#e8e8e8"));
            row5.addView(label5);
            row5.addView(info5);
            table.addView(row5);

        } else {
            System.out.println("Error: no quake passed!");
            Snackbar sb = Snackbar.make(findViewById(R.id.actMainLayout), "Something went wrong...", 3000);
            sb.show();
        }
    }

    private void loadMap() {
        // Get a handle to the fragment and register the callback.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    // Get a handle to the GoogleMap object and display marker.
    public void onMapReady(GoogleMap googleMap) {
        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(Double.parseDouble(quake.getLatitude()), Double.parseDouble(quake.getLongitude())))
                .title(quake.getDate() + " " + quake.getTime()));

        CameraUpdate center=
                CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(quake.getLatitude()), Double.parseDouble(quake.getLongitude())), 5);

        googleMap.moveCamera(center);
    }
}