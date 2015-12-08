package com.example.android.app.popularmovies.fragmetns;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import com.example.android.app.popularmovies.MovieDetailActivity;
import com.example.android.app.popularmovies.R;
import com.example.android.app.popularmovies.adapter.MovieGridViewAdapter;
import com.example.android.app.popularmovies.constants.PopularMovieConstants;
import com.example.android.app.popularmovies.dto.MovieDetails;
import com.example.android.app.popularmovies.tasks.PopularMovieTask;
import java.util.ArrayList;
import java.util.List;

//--------------------------------------------------------------------------------------------------

/**
 * A placeholder fragment containing a simple view.
 */
//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------
public class MovieGridFragment extends Fragment {

    private static String LOG_TAG = MovieDetailFragment.class.getSimpleName();
    private MovieGridViewAdapter mMovieGridAdapter;
    private List<MovieDetails> mGridData = new ArrayList<>();
    private SharedPreferences preferences;

    public MovieGridFragment() {
    }

    public void updatePreference(String key, String sortOrder) {
        preferences.edit().clear().putString(key, sortOrder).commit();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if (savedInstanceState==null || !savedInstanceState.containsKey("MOVIE_DATA"))
        {
            Log.d(LOG_TAG,"savedInstanceState is null"+savedInstanceState);

            mGridData = new ArrayList();
            mMovieGridAdapter = new MovieGridViewAdapter(getActivity(), R.layout.grid_item, mGridData);
            preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String sortOrder = preferences.getString(PopularMovieConstants.CURRENT_SORT_ORD, getString(R.string.default_sort_key));
            String currentPage = preferences.getString(PopularMovieConstants.CURRENT_PAGE, getString(R.string.default_page_key));
            PopularMovieTask popularMovieTask = new PopularMovieTask(mMovieGridAdapter);
            popularMovieTask.execute(sortOrder, currentPage);
        }
        else {
            mGridData = savedInstanceState.getParcelableArrayList("MOVIE_DATA");
            Log.d(LOG_TAG + " Retrieval", "MOVIE_DATA" + mGridData);
            mMovieGridAdapter.setGridData(mGridData);
        }

        /*mMovieGridAdapter = new MovieGridViewAdapter(getActivity(), R.layout.grid_item, mGridData);
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortOrder = preferences.getString(PopularMovieConstants.CURRENT_SORT_ORD, getString(R.string.default_sort_key));
        String currentPage = preferences.getString(PopularMovieConstants.CURRENT_PAGE, getString(R.string.default_page_key));
        PopularMovieTask popularMovieTask = new PopularMovieTask(mMovieGridAdapter);
        popularMovieTask.execute(sortOrder, currentPage);*/
        setHasOptionsMenu(true);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.grid_fragment_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        String sortOrder = null;
        switch (item.getItemId()) {
            case R.id.popularity_sort:
                sortOrder = "popularity.desc";
                break;
            case R.id.highest_rated_sort:
                sortOrder = "vote_average.desc";
                break;
        }

        updatePreference(PopularMovieConstants.CURRENT_SORT_ORD, sortOrder);
        PopularMovieTask popularMovieTask = new PopularMovieTask(mMovieGridAdapter);
        popularMovieTask.execute(sortOrder
                , preferences.getString(
                PopularMovieConstants.CURRENT_PAGE, getString(R.string.default_page_key)));
        return super.onOptionsItemSelected(item);
    }

    /*
        This method creates new Adpater and passes movie data to next screen

     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.movie_grid_fragment, container, false);
        GridView gridView = (GridView) rootView.findViewById(R.id.movieGridView);
        gridView.setAdapter(mMovieGridAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                MovieDetails movieDetails = mMovieGridAdapter.getItem(position);
                Log.d("LOG_TAG", movieDetails.toString());
                Intent intent = new Intent(getActivity(), MovieDetailActivity.class)
                        .putExtra(PopularMovieConstants.MOVIE_DATA, movieDetails);
                startActivity(intent);
            }
        });


        return rootView;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d(LOG_TAG + " onSaveInstanceState", "MOVIE_DATA" + mGridData);
        outState.putParcelableArrayList("MOVIE_DATA", (ArrayList<? extends Parcelable>) mGridData);
        super.onSaveInstanceState(outState);
    }




}
//--------------------------------------------------------------------------------------------------
//
//--------------------------------------------------------------------------------------------------