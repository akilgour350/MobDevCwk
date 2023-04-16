/*
Name: Alastair Kilgour
SN: S2221119
Program: Computer
*/

package com.adk.kilgour_alastair_s2221119;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {
    ArrayList<ResultQuake> quakes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_search, container, false);
        quakes = new ArrayList<>();

        if (getArguments() != null) {
            Bundle extras = getArguments();
            quakes = extras.getParcelableArrayList("resultQuakes");
            TableLayout table = v.findViewById(R.id.search_table); // get the TableLayout object

            for (int i = 0; i < quakes.size(); i++) {
                TableRow row = new TableRow(getContext());
                Earthquake eq = quakes.get(i).getQuake();
                row.setId(i);

                TextView txt = new TextView(getContext());
                txt.setTextSize(20);
                if (!eq.getLocality().equals("No Locality")) {
                    txt.setText(eq.getDate() + " @" + eq.getTime() + "\n" + eq.getLocality() + ", " + eq.getRegion());
                } else {
                    txt.setText(eq.getDate() + " @" + eq.getTime() + "\n" + eq.getRegion());
                }

                row.addView(txt);
                row.setOnClickListener(view -> {
                    Earthquake quake = quakes.get(row.getId()).getQuake();
                    Intent intent = new Intent(getContext(), MoreInfo.class);
                    intent.putExtra("selectedQuake", quake);
                    startActivity(intent);
                });

                switch (Integer.parseInt(quakes.get(i).getQuake().getMagnitude().substring(0, 1))) {
                    case 0:
                        row.setBackgroundColor(Color.parseColor("#009512"));
                        break;

                    case 1:
                        row.setBackgroundColor(Color.parseColor("#3e8e00"));
                        break;

                    case 2:
                        row.setBackgroundColor(Color.parseColor("#5a8700"));
                        break;

                    case 3:
                        row.setBackgroundColor(Color.parseColor("#717e00"));
                        break;

                    case 4:
                        row.setBackgroundColor(Color.parseColor("#857400"));
                        break;

                    case 5:
                        row.setBackgroundColor(Color.parseColor("#976800"));
                        break;

                    case 6:
                        row.setBackgroundColor(Color.parseColor("#a75a00"));
                        break;

                    case 7:
                        row.setBackgroundColor(Color.parseColor("#b64900"));
                        break;

                    case 8:
                        row.setBackgroundColor(Color.parseColor("#c23200"));
                        break;

                    case 9:
                        row.setBackgroundColor(Color.parseColor("#cc0000"));
                        break;

                    default:
                        row.setBackgroundColor(Color.parseColor("#2c2c2c"));
                }

                row.setPadding(20, 20, 20, 20);
                row.setGravity(1);

                table.addView(row);
            }
        } else {
            System.out.println("null quakes passed");
        }
        return v;
    }
}