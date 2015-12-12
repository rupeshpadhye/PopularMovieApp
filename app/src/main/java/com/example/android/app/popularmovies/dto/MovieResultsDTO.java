package com.example.android.app.popularmovies.dto;
//--------------------------------------------------------------------------------------------------
import java.io.Serializable;
import java.util.List;
import lombok.Data;
//--------------------------------------------------------------------------------------------------
/**
 *  @Author Rupesh Padhye
 */
//--------------------------------------------------------------------------------------------------
@Data
public class MovieResultsDTO implements Serializable {

    private int page;
    private int total_pages;
    private int total_results;
    private List<MovieDetails> results;

}
//--------------------------------------------------------------------------------------------------
//
//--------------------------------------------------------------------------------------------------