package com.example.android.app.popularmovies.dto;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class MovieDetails implements Parcelable {

    private boolean adult;
    private String backdrop_path;
    private List<Integer> genre_ids;
    private int id;
    private String original_language;
    private String original_title;
    private String overview;
    private Date release_date;
    private String poster_path;
    private double popularity;
    private String title;
    private boolean video;
    private float vote_average;
    private int vote_count;

    public MovieDetails() {

    }

    private MovieDetails(Parcel parcel) {
        adult = (boolean) parcel.readValue(null);
        backdrop_path=parcel.readString();
        genre_ids=parcel.readArrayList(null);
        id=parcel.readInt();
        original_language=parcel.readString();
        original_title=parcel.readString();
        overview=parcel.readString();
        release_date=new Date(parcel.readLong());
        poster_path=parcel.readString();
        popularity=parcel.readDouble();
        title=parcel.readString();
      //  video=(boolean)parcel.readValue(null);
        vote_average=parcel.readFloat();
        vote_count=parcel.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeValue(adult);
        out.writeString(backdrop_path);
        out.writeList(genre_ids);
        out.writeInt(id);
        out.writeString(original_language);
        out.writeString(original_title);
        out.writeString(overview);
        out.writeLong(release_date.getTime());
        out.writeString(poster_path);
        out.writeDouble(popularity);
        out.writeString(title);
        //out.writeValue(video);
        out.writeFloat(vote_average);
        out.writeInt(vote_count);

    }


    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public MovieDetails createFromParcel(Parcel in) {
            return new MovieDetails(in);
        }

        public MovieDetails[] newArray(int size) {
            return new MovieDetails[size];
        }
    };

}
