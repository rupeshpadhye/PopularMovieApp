package com.example.android.app.popularmovies.dto;

import lombok.Data;

/**
 * Created by RUPESH on 12/27/2015.
 */

@Data
public class MovieTrailer {
    private String id;
    private String iso_639_1;
    private String key;
    private String name;
    private String site;
    private String size;
    private String type;
}
