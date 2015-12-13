package com.example.android.app.popularmovies;
//--------------------------------------------------------------------------------------------------
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.example.android.app.popularmovies.constants.PopularMovieConstants;
import com.example.android.app.popularmovies.dto.MovieDetails;
import com.example.android.app.popularmovies.fragmetns.MovieDetailFragment;
import com.example.android.app.popularmovies.fragmetns.MovieGridFragment;
//--------------------------------------------------------------------------------------------------
/**
 * @Author Rupesh Padhye
 */
//--------------------------------------------------------------------------------------------------
public class MainActivity extends AppCompatActivity implements MovieGridFragment.onMovieSelectedListener {

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mTwoPane = getResources().getBoolean(R.bool.dual_pane);
        if(mTwoPane) {
           if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.movie_detail_activity, new MovieDetailFragment())
                        .commit();
            }
        }
        else {
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment, new MovieGridFragment())
                        .commit();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onMovieSelected(MovieDetails movieDetails) {
        if(mTwoPane) {
            MovieDetailFragment fragment = (MovieDetailFragment)getSupportFragmentManager().
                                        findFragmentById(R.id.movie_detail_activity);
            if(fragment!=null) {
                fragment.updateView(movieDetails);
            }

        }
        else {
            Intent intent = new Intent(getApplicationContext(), MovieDetailActivity.class)
                    .putExtra(PopularMovieConstants.MOVIE_DATA, movieDetails);
            startActivity(intent);

        }
    }
}
//--------------------------------------------------------------------------------------------------
//
//--------------------------------------------------------------------------------------------------