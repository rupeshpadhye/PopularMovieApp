package com.example.android.app.popularmovies.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * Created by RUPESH on 12/27/2015.
 */

@Data
public class MovieTrailersDTO implements Serializable {

    private String id;
    private List<MovieTrailer> results;

}
