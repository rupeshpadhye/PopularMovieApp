package com.example.android.app.popularmovies.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * Created by RUPESH on 12/26/2015.
 */
@Data
public class MovieReviewsDTO implements Serializable {

  private int id;
  private int page;
  private List<MovieReview> results;
  private  int total_pages;
  private int total_results;

}

