//--------------------------------------------------------------------------------------------------
package com.example.android.app.popularmovies;
//--------------------------------------------------------------------------------------------------
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.android.app.popularmovies.fragmetns.MovieDetailFragment;
//--------------------------------------------------------------------------------------------------
/**
 * Child activity  MovieDetailActivity class calls MovieDetailFragment
 * @Author Rupesh Padhye
 */
//--------------------------------------------------------------------------------------------------
public class MovieDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movie_detail_activity,new MovieDetailFragment())
                    .commit();
        }
    }
}
//--------------------------------------------------------------------------------------------------
//
//--------------------------------------------------------------------------------------------------
