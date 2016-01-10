package com.example.android.app.popularmovies.dto;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Data;

/**
 * Created by RUPESH on 12/27/2015.
 */

@Data
public class MovieTrailer implements Parcelable {
    // private String id;
    //private String iso_639_1;
    private String key;
    private String name;
    //private String site;
    //private String size;
    //private String type;


    public MovieTrailer(Parcel in) {
        key = in.readString();
        name = in.readString();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(key);
        out.writeString(name);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public MovieTrailer createFromParcel(Parcel in) {
            return new MovieTrailer(in);
        }

        public MovieTrailer[] newArray(int size) {
            return new MovieTrailer[size];
        }
    };
}
