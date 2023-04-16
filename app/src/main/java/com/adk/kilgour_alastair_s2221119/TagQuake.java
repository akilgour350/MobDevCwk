package com.adk.kilgour_alastair_s2221119;

/*
Name: Alastair Kilgour
SN: S2221119
Program: Computer
*/

public class TagQuake {
    public Earthquake earthquake;
    public String tag;

    public TagQuake(Earthquake earthquake, String tag) {
        this.earthquake = earthquake;
        this.tag = tag;
    }

    @Override
    public String toString() {
        return this.tag.toUpperCase() + "\n" + earthquake.getDate() + " @" + earthquake.getTime() + "\n" + earthquake.getLocality() + ", " + earthquake.getRegion();
    }
}
