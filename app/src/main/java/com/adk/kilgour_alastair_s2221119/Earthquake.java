/*
Name: Alastair Kilgour
SN: S2221119
Program: Computer
*/


package com.adk.kilgour_alastair_s2221119;

import java.io.Serializable;

public class Earthquake implements Serializable {
    private String Date;
    private String Time;
    private String Latitude;
    private String Longitude;
    private String Locality;
    private String Region;
    private int Depth;
    private String Magnitude;
    private int Distance;
    private int Bearing;

    // GETTERS
    public String getDate() { return this.Date; }
    public String getTime() { return this.Time; }
    public String getLatitude() { return this.Latitude; }
    public String getLongitude() { return this.Longitude; }
    public String getLocality() { return this.Locality; }
    public String getRegion() { return this.Region; }
    public int getDepth() { return this.Depth; }
    public String getMagnitude() { return this.Magnitude; }
    public int getDistance() { return this.Distance; }
    public int getBearing() { return Bearing; }

    // SETTERS
    public void setLocationRegion(String title) {
        // getting information to set earthquakes location name from Title
        String[] split = title.split(":");
        String locReg = split[2].trim().substring(0, split[2].indexOf(", "));

        // formats location to look pretty
        locReg = locReg.toLowerCase(); // sets all to lower case

        // sets the first letter at the start of each word to a capital
        split = locReg.split(",");
        String loc, reg;
        if (split.length > 1) {
            loc = split[0];
            reg = split[1];
        } else {
            loc = "No Locality";
            reg = split[0];
        }


        split = loc.split(" ");
        loc = "";
        for (int i = 0; i < split.length; i++) {
            if (!split[i].equals("and"))
                split[i] = split[i].substring(0, 1).toUpperCase() + split[i].substring(1);

            loc = loc + split[i] + " ";
        }

        split = reg.split(" ");
        reg = "";
        for (int i = 0; i < split.length; i++) {
            if (!split[i].equals("and"))
                split[i] = split[i].substring(0, 1).toUpperCase() + split[i].substring(1);

            reg = reg + split[i] + " ";
        }

        this.Locality = loc.trim();
        this.Region = reg.trim();
    }
    public void setDepthMagnitude(String description) {
        String[] splitDesc = description.split(" ; ");
        String depth, magnitude;

        for (int i = 0; i < splitDesc.length; i++) {
            if (splitDesc[i].contains("Depth: ")) {
                String depthString = "";
                depthString = splitDesc[i].replace("Depth: ", "").trim();
                depthString = depthString.substring(0, depthString.indexOf(' '));
                this.Depth = Integer.parseInt(depthString);
            } else if (splitDesc[i].contains("Magnitude: ")) {
                this.Magnitude = splitDesc[i].replace("Magnitude: ", "").trim();
            }
        }
    }
    public void setDate(String date) {
        date = date.substring(date.indexOf(' ') + 1);
        String[] dateSplit = date.split(" ");
        this.Date = dateSplit[0] + " " + dateSplit[1] + " " + dateSplit[2];
        this.Time = dateSplit[3];
    }
    public void setLatitude(String latitude) { this.Latitude = latitude; }
    public void setLongitude(String longitude) {this.Longitude = longitude; }
    public void setDistance(int distance) { this.Distance = distance; }
    public void setBearing(int bearing) { Bearing = bearing; }
}


