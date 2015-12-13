package com.example.android.app.popularmovies.fragmetns;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.preference.PreferenceManager;
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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.app.popularmovies.BuildConfig;
import com.example.android.app.popularmovies.MovieDetailActivity;
import com.example.android.app.popularmovies.R;
import com.example.android.app.popularmovies.adapter.MovieGridViewAdapter;
import com.example.android.app.popularmovies.constants.PopularMovieConstants;
import com.example.android.app.popularmovies.dto.MovieDetails;
import com.example.android.app.popularmovies.dto.MovieResultsDTO;
import com.example.android.app.popularmovies.network.RemoteCaller;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    private SharedPreferences preferences;
    private TextView textView;
    private GridView gridView;
    private ProgressBar progressBar;


    public void updatePreference(String key, String sortOrder) {
        preferences.edit().clear().putString(key, sortOrder).commit();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        List<MovieDetails> mGridData = new ArrayList();
        mMovieGridAdapter = new MovieGridViewAdapter(getActivity(), R.layout.grid_item, mGridData);
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if (savedInstanceState == null || !savedInstanceState.containsKey("MOVIE_DATA")) {
            String sortOrder = preferences.getString(PopularMovieConstants.CURRENT_SORT_ORD, getString(R.string.default_sort_key));
            String currentPage = preferences.getString(PopularMovieConstants.CURRENT_PAGE, getString(R.string.default_page_key));
            PopularMovieTask popularMovieTask = new PopularMovieTask();
            popularMovieTask.execute(sortOrder, currentPage);
        } else {
            mGridData = savedInstanceState.getParcelableArrayList("MOVIE_DATA");
            mMovieGridAdapter.setGridData(mGridData);
        }
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
        PopularMovieTask popularMovieTask = new PopularMovieTask();
        popularMovieTask.execute(sortOrder
                , preferences.getString(
                PopularMovieConstants.CURRENT_PAGE, getString(R.string.default_page_key)));
        return super.onOptionsItemSelected(item);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.movie_grid_fragment, container, false);


        gridView = (GridView) rootView.findViewById(R.id.movieGridView);
        textView = (TextView) rootView.findViewById(R.id.status);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        gridView.setAdapter(mMovieGridAdapter);

        if (savedInstanceState != null && savedInstanceState.containsKey("MOVIE_DATA")) {
            textView.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            gridView.setVisibility(View.VISIBLE);
        }
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
        outState.putParcelableArrayList("MOVIE_DATA", (ArrayList<? extends Parcelable>) mMovieGridAdapter.getGridData());
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.v(LOG_TAG, "onViewStateRestored");
    }

    private class PopularMovieTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {

        }

        private String getPopularMovies(String orderBy, String pageNo) {
            String result = null;
            Uri builtUri = Uri.parse(PopularMovieConstants.BASE_URL)
                    .buildUpon()
                    .appendPath(PopularMovieConstants.MOVIE_APPENDER)
                    .appendPath(PopularMovieConstants.DISC_PATH)
                    .appendPath(PopularMovieConstants.MOVIE_PATH)
                    .appendQueryParameter(PopularMovieConstants.SORT_PARAM, orderBy)
                    .appendQueryParameter(PopularMovieConstants.PAGE_NO, pageNo)
                    .appendQueryParameter(PopularMovieConstants.API_KEY_PARAM, BuildConfig.THE_MOVIE_DB_API_KEY)
                    .build();
            try {
                URL url = new URL(builtUri.toString());
                Log.d(LOG_TAG, builtUri.toString());
                result = RemoteCaller.doRemoteCall(url, PopularMovieConstants.GET_METHOD);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.e(LOG_TAG, e.getMessage());
            }
            return result;
        }

        @Override
        protected String doInBackground(String... params) {
            Log.d(LOG_TAG, params[0] + " " + params[1]);
            if (params == null) {
                return null;
            }
            String result = getPopularMovies(params[0], params[1]);
            return result;
        }


        @Override
        protected void onPostExecute(String result) {
            if (result == null) {
                textView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
                textView.setText("No internet");
                return;
            }

            Log.d(LOG_TAG, result);
            textView.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            gridView.setVisibility(View.VISIBLE);

            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-DD").create();
            MovieResultsDTO resultsDTO = gson.fromJson(result, MovieResultsDTO.class);
            Log.d(LOG_TAG, resultsDTO.toString());
            mMovieGridAdapter.setGridData(resultsDTO.getResults());
            textView.setVisibility(View.GONE);
        }
    }

}


//--------------------------------------------------------------------------------------------------
//
//--------------------------------------------------------------------------------------------------