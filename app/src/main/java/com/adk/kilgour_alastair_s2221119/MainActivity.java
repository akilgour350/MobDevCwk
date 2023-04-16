/*
Name: Alastair Kilgour
SN: S2221119
Program: Computer
*/
package com.adk.kilgour_alastair_s2221119;

import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.pow;
import static java.lang.Math.sin;

import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.SpannableStringBuilder;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.snackbar.Snackbar;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity
{

    private ArrayList<Earthquake> earthquakes;
    private ArrayList<TagQuake> interestingQuakes;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        loadData(); // calls method to get all the data for the app
    }

    public void openBrowseQuakes(View v) {
        Intent browse = new Intent(getApplicationContext(), BrowseQuakes.class);
        browse.putExtra("allQuakes", earthquakes);
        startActivity(browse);
    }

    public void loadInterestingQuakes() {
        interestingQuakes = new ArrayList<>();
        Earthquake nearestNorth = null, nearestEast = null, nearestSouth = null, nearestWest = null;
        Earthquake deepest = earthquakes.get(0), shallowest = earthquakes.get(0), biggest = earthquakes.get(0), smallest = earthquakes.get(0);

        for (int i = 0; i < earthquakes.size(); i++) {
            Earthquake current = earthquakes.get(i);

            if (current.getBearing() >= 315 || current.getBearing() < 45) {
                if (nearestNorth == null || nearestNorth.getDistance() > current.getDistance()) {
                    nearestNorth = current;
                }
            } else if (current.getBearing() >= 45 && current.getBearing() < 135) {
                if (nearestEast == null || nearestEast.getDistance() > current.getDistance()) {
                    nearestEast = current;
                }
            } else if (current.getBearing() >= 135 && current.getBearing() < 225) {
                if (nearestSouth == null || nearestSouth.getDistance() > current.getDistance()){
                    nearestSouth = current;
                }
            } else if (current.getBearing() >= 225 && current.getBearing() < 315) {
                if (nearestWest == null || nearestWest.getDistance() > current.getDistance()) {
                    nearestWest = current;
                }
            }

            if (current.getDepth() > deepest.getDepth()) {
                deepest = current;
            } else if (current.getDepth() < shallowest.getDepth()) {
                shallowest = current;
            }

            if (Double.parseDouble(current.getMagnitude()) > Double.parseDouble(biggest.getMagnitude())) {
                biggest = current;
            } else if (Double.parseDouble(current.getMagnitude()) < Double.parseDouble(smallest.getMagnitude())) {
                smallest = current;
            }
        }

        if (nearestNorth != null) {
            TagQuake tagQuake = new TagQuake(nearestNorth, "nearest north of glasgow");
            interestingQuakes.add(tagQuake);
        }

        if (nearestEast != null) {
            TagQuake tagQuake = new TagQuake(nearestEast, "nearest east of glasgow");
            interestingQuakes.add(tagQuake);
        }

        if (nearestSouth != null) {
            TagQuake tagQuake = new TagQuake(nearestSouth, "nearest south of glasgow");
            interestingQuakes.add(tagQuake);
        }

        if (nearestWest != null) {
            TagQuake tagQuake = new TagQuake(nearestWest, "nearest west of glasgow");
            interestingQuakes.add(tagQuake);
        }

        if (deepest != null) {
            TagQuake tagQuake = new TagQuake(deepest, "deepest earthquake");
            interestingQuakes.add(tagQuake);
        }

        if (shallowest != null) {
            TagQuake tagQuake = new TagQuake(shallowest, "shallowest earthquake");
            interestingQuakes.add(tagQuake);
        }

        if (biggest != null) {
            TagQuake tagQuake = new TagQuake(biggest, "biggest earthquake");
            interestingQuakes.add(tagQuake);
        }

        if (smallest != null) {
            TagQuake tagQuake = new TagQuake(smallest, "smallest earthquake");
            interestingQuakes.add(tagQuake);
        }

        ListView lv = findViewById(R.id.interestingQuakes);
        ArrayAdapter<TagQuake> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.listview_layout, interestingQuakes);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener((adapterView, view, i, l) -> {
            TagQuake tagQuake = (TagQuake) adapterView.getItemAtPosition(i);
            Intent moreInfo = new Intent(getApplicationContext(), MoreInfo.class);
            moreInfo.putExtra("selectedQuake", tagQuake.earthquake);
            startActivity(moreInfo);
        });
    }

    public void refreshData(View v) {
        earthquakes.removeAll(earthquakes);
        loadData();
    }

    // FOR GETTING DATA FROM SOURCE
    public void loadData()
    {
        // Run network access on a separate thread;
        new Thread(new Task("http://quakes.bgs.ac.uk/feeds/MhSeismology.xml")).start();
    }
    private class Task implements Runnable
    {
        private String url;
        public Task(String myUrl)
        {
            url = myUrl;
        }

        @Override
        public void run()
        {
            URL myUrl;
            URLConnection yc;
            BufferedReader in;
            String inputLine;
            String result = "";
            earthquakes = new ArrayList<>();

            try
            {
                myUrl = new URL(url);
                yc = myUrl.openConnection();
                in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
                while ((inputLine = in.readLine()) != null)
                {
                    result = result + inputLine;
                    System.out.println(inputLine);

                }
                in.close();

                if (!result.equals("")) {
                    // PullParser Setup
                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                    factory.setNamespaceAware(true);
                    XmlPullParser xpp = factory.newPullParser();
                    xpp.setInput( new StringReader( result ) );

                    int eventType = xpp.getEventType(); // initialise eventType variable to track which type of event the Parser is on
                    Earthquake quake = new Earthquake(); // creates Earthquake object to be populated
                    boolean building = false; // creates boolean to store whether an Earthquake object is being built

                    while (eventType != XmlPullParser.END_DOCUMENT) { // checks to ensure the Parser hasn't reach the end of the document

                        if(eventType == XmlPullParser.START_TAG) // checks for an XML start tag (eg <myTag>)
                        {
                            if (xpp.getName().equals("item")) {
                                quake = new Earthquake(); // initialises earthquake object
                                building = true; // sets that an Earthquake object is being built

                            } else if (xpp.getName().equals("title") && building) {
                                xpp.next(); // moves to next event (will be TEXT)
                                quake.setLocationRegion(xpp.getText()); // stores title of the earthquake

                            } else if (xpp.getName().equals("description") && building) {
                                xpp.next(); // moves to next event (will be TEXT)
                                quake.setDepthMagnitude((xpp.getText())); // stores description of the earthquake

                            } else if (xpp.getName().equals("pubDate") && building) {
                                xpp.next(); // moves to next event (will be TEXT)
                                quake.setDate(xpp.getText()); // stores date of the earthquake

                            } else if (xpp.getName().equals("lat") && building) {
                                xpp.next(); // moves to next event (will be TEXT)
                                quake.setLatitude(xpp.getText()); // stores latitude of the earthquake

                            } else if (xpp.getName().equals("long") && building) {
                                xpp.next(); // moves to next event (will be TEXT)
                                quake.setLongitude(xpp.getText()); // stores longitude of the earthquake

                            }
                        } else if (eventType == XmlPullParser.END_TAG) {
                            if (xpp.getName().equals("item") && building) {
                                float[] tempDist = new float[3];
                                android.location.Location.distanceBetween(Double.parseDouble(quake.getLatitude()), Double.parseDouble(quake.getLongitude()), 55.8642, 4.2518, tempDist);
                                quake.setDistance((int)tempDist[0]);
                                quake.setBearing(calculateBearing(55.8642, -4.2518, Double.parseDouble(quake.getLatitude()), Double.parseDouble(quake.getLongitude())));

                                earthquakes.add(quake); // adds complete Earthquake object to ArrayList
                                building = false; // sets that an Earthquake object is NOT being built
                                System.out.println("Created earthquake: ");
                                System.out.println("Location: " + quake.getLocality() + "," + quake.getRegion());
                                System.out.println("Date: " + quake.getDate());
                            }
                        }
                        eventType = xpp.next(); // moves to next event
                    }
                    Snackbar sb = Snackbar.make(findViewById(R.id.actMainLayout), "Data loaded successfully!", 3000);
                    sb.show();
                    loadInterestingQuakes();
                } else {
                    throw new Exception("Result was empty.");
                }
            }
            catch (Exception ex)
            {
                System.out.println("Error: " + ex.getMessage());
                Snackbar sb = Snackbar.make(findViewById(R.id.actMainLayout), "Something went wrong...", 3000);
                sb.show();
            }
        }

    }

    // credits for this code and maths: https://stackoverflow.com/questions/4308262/calculate-compass-bearing-heading-to-location-in-android
    public int calculateBearing(double startLatitude, double startLongitude, double endLatitude, double endLongitude){
        Location startLoc = new Location("");
        startLoc.setLatitude(startLatitude);
        startLoc.setLongitude(startLongitude);

        Location destination = new Location("");
        destination.setLatitude(endLatitude);
        destination.setLongitude(endLongitude);

        int bearing = (int) startLoc.bearingTo(destination);

        if (bearing < 0)
        {
            bearing = bearing + 360;
        }
        System.out.println("Bearing: " + bearing);
        return bearing;
    }
}