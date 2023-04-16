package com.adk.kilgour_alastair_s2221119;

/*
Name: Alastair Kilgour
SN: S2221119
Program: Computer
*/

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Comparator;

public class BrowseQuakes extends AppCompatActivity {
    private ArrayList<Earthquake> earthquakes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_quakes);
        getSupportActionBar().hide();

        earthquakes = new ArrayList<>();
        Intent intent = getIntent();
        earthquakes = (ArrayList<Earthquake>) intent.getSerializableExtra("allQuakes");

        ArrayList<ResultQuake> resultQuakes = new ArrayList<>();
        for (int i = 0; i < earthquakes.size(); i++) {
            resultQuakes.add(new ResultQuake(earthquakes.get(i), Resemblance.Exact));
        }
        showSearchResults(resultQuakes);

        initInterface();
    }

    // FOR SETTING UP THE MAIN ACTIVITY GUI
    private void initInterface() {
        // sets layout and items in searchBy spinner
        Spinner spn = findViewById(R.id.searchBy);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getApplicationContext(), R.array.search_by_array, R.layout.spinner_layout);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn.setAdapter(adapter);

        EditText et = findViewById(R.id.searchInput);
        et.setOnEditorActionListener((v, actionId, event) -> {
            if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                searchForInput(et);
            }
            return false;
        });
    }

    public void showSearchResults(ArrayList<ResultQuake> results) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("resultQuakes", results);
        SearchFragment sFrag = new SearchFragment();
        sFrag.setArguments(bundle);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.searchPlaceholder, sFrag);
        ft.commit();
    }

    public void searchForInput(View v) {
        ArrayList<ResultQuake> results = new ArrayList<>();
        EditText input = findViewById(R.id.searchInput);
        String toSearch = input.getText().toString().toLowerCase();

        if (toSearch != "" && toSearch != null) {
            Spinner spn = findViewById(R.id.searchBy);
            String searchBy = spn.getSelectedItem().toString();

            switch (searchBy) {
                case "Locality / Region":
                    for (int i = 0; i < earthquakes.size(); i++) {
                        if (toSearch.contains(earthquakes.get(i).getLocality().toLowerCase()) && toSearch.contains(earthquakes.get(i).getRegion().toLowerCase())) {
                            ResultQuake rq = new ResultQuake(earthquakes.get(i), Resemblance.Exact);
                            results.add(rq);
                        } else if (toSearch.contains(earthquakes.get(i).getLocality().toLowerCase()) || toSearch.contains(earthquakes.get(i).getRegion().toLowerCase())) {
                            ResultQuake rq = new ResultQuake(earthquakes.get(i), Resemblance.Near);
                            results.add(rq);
                        }
                    }

                    break;

                case "Date":
                    System.out.println("Searching by date");
                    for (int i = 0; i < earthquakes.size(); i++) {
                        if (earthquakes.get(i).getDate().toLowerCase().contains(toSearch.toLowerCase())) {
                            ResultQuake rq = new ResultQuake(earthquakes.get(i), Resemblance.Exact);
                            results.add(rq);
                        }
                    }
                    break;
            }

            if (results.size() != 0) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    results.sort(Comparator.comparing(ResultQuake::getResemblance));
                }
                showSearchResults(results);
            } else {
                Snackbar sb = Snackbar.make(findViewById(R.id.browseLayout), "No results!", 3000);
                sb.show();
            }


        } else {
            Snackbar sb = Snackbar.make(findViewById(R.id.actMainLayout), "Enter search parameters!", 3000);
            sb.show();
        }
    }
}