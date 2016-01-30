package com.example.android.app.popularmovies.fragmetns;
//--------------------------------------------------------------------------------------------------

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
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
import android.widget.Toast;

import com.example.android.app.popularmovies.BuildConfig;
import com.example.android.app.popularmovies.R;
import com.example.android.app.popularmovies.adapter.MovieGridViewAdapter;
import com.example.android.app.popularmovies.constants.PopularMovieConstants;
import com.example.android.app.popularmovies.dto.MovieDetails;
import com.example.android.app.popularmovies.dto.MovieResultsDTO;
import com.example.android.app.popularmovies.helpers.DatabaseHelper;
import com.example.android.app.popularmovies.network.RemoteCaller;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
//--------------------------------------------------------------------------------------------------

/**
 * A placeholder fragment containing a simple view.
 */
//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------
public class MovieGridFragment extends Fragment {

    private static String LOG_TAG = MovieGridFragment.class.getSimpleName();
    private MovieGridViewAdapter mMovieGridAdapter;
    private SharedPreferences mPreferences;
    private TextView mTextView;
    private GridView mGridView;
    private ProgressBar mProgressBar;
    private onMovieSelectedListener mMovieClickListener;
    private int mPosition = GridView.INVALID_POSITION;
    List<MovieDetails> mGridData = new ArrayList<>();;
    private Dao<MovieDetails, Integer> movieDetailsDAO;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMovieGridAdapter = new MovieGridViewAdapter(getActivity(), R.layout.grid_item, mGridData);
        mPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        try {
            movieDetailsDAO = OpenHelperManager.getHelper(getContext(),
                    DatabaseHelper.class).getMovieDetailsDAO();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (savedInstanceState == null || !savedInstanceState.containsKey(PopularMovieConstants.MOVIE_DATA)) {
            Log.d(LOG_TAG, "savedInstanceState is null" + savedInstanceState);
            populateMovies();

        } else {
            Log.d(LOG_TAG, "savedInstanceState is not null");
            mGridData = savedInstanceState.getParcelableArrayList(PopularMovieConstants.MOVIE_DATA);
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
                sortOrder = PopularMovieConstants.POPULARITY_DESC;
                break;
            case R.id.highest_rated_sort:
                sortOrder = PopularMovieConstants.VOTE_AVG_DESC;
                break;
            case R.id.fav_movies_id:
                sortOrder = PopularMovieConstants.FAV_MOVIES;
                setScreenTextInvisible();
                break;
        }

        updatePreference(PopularMovieConstants.CURRENT_SORT_ORD, sortOrder);
        populateMovies();
        return super.onOptionsItemSelected(item);
    }

    private void populateMovies() {

        String sortOrder = mPreferences.getString(PopularMovieConstants.CURRENT_SORT_ORD
                , getString(R.string.default_sort_key));

        if (PopularMovieConstants.FAV_MOVIES.equals(sortOrder)) {
            try {
                Log.d(LOG_TAG, "fav order selected");
                mGridData = movieDetailsDAO.queryForAll();
                mMovieGridAdapter.setGridData(mGridData);
                setScreenTextInvisible();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            PopularMovieTask popularMovieTask = new PopularMovieTask();
            popularMovieTask.execute(sortOrder
                    , mPreferences.getString(
                    PopularMovieConstants.CURRENT_PAGE, getString(R.string.default_page_key)));
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof onMovieSelectedListener) {
            mMovieClickListener = (onMovieSelectedListener) context;
        } else {
            throw new ClassCastException("not instance of onMovieSelectedListener");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.movie_grid_fragment, container, false);
        mGridView = (GridView) rootView.findViewById(R.id.movieGridView);
        mTextView = (TextView) rootView.findViewById(R.id.status);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        mGridView.setAdapter(mMovieGridAdapter);

        if (savedInstanceState != null && savedInstanceState.containsKey(PopularMovieConstants.MOVIE_DATA)) {
            setScreenTextInvisible();
        }
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                MovieDetails movieDetails = mMovieGridAdapter.getItem(position);
                mPosition = position;
                mMovieClickListener.onMovieSelected(movieDetails);

            }
        });

        if (savedInstanceState != null && savedInstanceState.containsKey(PopularMovieConstants.SCROLL_POSITION)) {
            mPosition = savedInstanceState.getInt(PopularMovieConstants.SCROLL_POSITION);
        }

        if (mPosition != GridView.INVALID_POSITION) {
            mGridView.smoothScrollToPosition(mPosition);
        }
        return rootView;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putParcelableArrayList(PopularMovieConstants.MOVIE_DATA
                , (ArrayList<? extends Parcelable>) mMovieGridAdapter.getGridData());
        outState.putInt(PopularMovieConstants.SCROLL_POSITION, mPosition);

        super.onSaveInstanceState(outState);
    }

    private void updatePreference(String key, String sortOrder) {
        mPreferences.edit().clear().putString(key, sortOrder).commit();
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
                setScreenTextVisible();
                mTextView.setText(PopularMovieConstants.NO_INTERNET);
                return;
            }

            setScreenTextInvisible();
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-DD").create();
            MovieResultsDTO resultsDTO = gson.fromJson(result, MovieResultsDTO.class);
            Log.d(LOG_TAG, resultsDTO.toString());
            mMovieGridAdapter.setGridData(resultsDTO.getResults());
        }
    }

    public interface onMovieSelectedListener {
        public void onMovieSelected(MovieDetails movieDetails);

    }

    private void setScreenTextVisible() {
        //getView().findViewById(R.id.status).setVisibility(View.VISIBLE);
        //getView().findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
        mTextView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void setScreenTextInvisible() {
       // getView().findViewById(R.id.status).setVisibility(View.INVISIBLE);
        //getView().findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
        mTextView.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);
    }

}
//--------------------------------------------------------------------------------------------------
//
//--------------------------------------------------------------------------------------------------