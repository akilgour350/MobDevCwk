/*
Name: Alastair Kilgour
SN: S2221119
Program: Computer
*/

package com.adk.kilgour_alastair_s2221119;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class ResultQuake implements Parcelable, Serializable {
    public Earthquake quake;
    public Resemblance resemblance;

    public ResultQuake(Earthquake eq, Resemblance r) {
        quake = eq;
        resemblance = r;
    }

    protected ResultQuake(Parcel in) {
    }

    public static final Creator<ResultQuake> CREATOR = new Creator<ResultQuake>() {
        @Override
        public ResultQuake createFromParcel(Parcel in) {
            return new ResultQuake(in);
        }

        @Override
        public ResultQuake[] newArray(int size) {
            return new ResultQuake[size];
        }
    };

    public Earthquake getQuake() { return quake; }
    public Resemblance getResemblance() { return resemblance; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        System.out.println("writeToParcel called");
    }

    @Override
    public String toString() {
        return quake.getDate() + " @" + quake.getTime() + "\n" + quake.getLocality() + ", " + quake.getRegion();
    }
}

enum Resemblance {
    Exact, Near
}
