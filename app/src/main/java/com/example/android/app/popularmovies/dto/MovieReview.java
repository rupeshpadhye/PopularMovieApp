package com.example.android.app.popularmovies.dto;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

import lombok.Data;

/**
 * Created by RUPESH on 12/26/2015.
 */

@Data
public class MovieReview implements Parcelable {
    private String id;
    private  String author;
    private  String content;

    public  MovieReview()
    {}
    public MovieReview(Parcel parcel) {
        id=parcel.readString();
        author=parcel.readString();
        content=parcel.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(id);
        out.writeString(author);
        out.writeString(content);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public MovieReview createFromParcel(Parcel in) {
            return new MovieReview(in);
        }

        public MovieReview[] newArray(int size) {
            return new MovieReview[size];
        }
    };

}

