package com.example.android.app.popularmovies.tasks;


import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.example.android.app.popularmovies.BuildConfig;
import com.example.android.app.popularmovies.adapter.MovieGridViewAdapter;
import com.example.android.app.popularmovies.constants.PopularMovieConstants;
import com.example.android.app.popularmovies.dto.MovieDetails;
import com.example.android.app.popularmovies.dto.MovieResultsDTO;
import com.example.android.app.popularmovies.network.RemoteCaller;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * PopularMovieTask endpoint calls most popular movies by order and page
 *
 * @Author Rupesh Padhye
 */
public class PopularMovieTask extends AsyncTask<String, Void, String> {

    private ArrayAdapter<MovieDetails> adapter;
    private MovieResultsDTO resultsDTO;
    private static final String LOG_TAG = PopularMovieTask.class.getSimpleName();

    public PopularMovieTask(ArrayAdapter<MovieDetails> adapter) {
        this.adapter = adapter;
    }

    private String getPopularMovies(String orderBy, String pageNo) {
        String result = null;
        Uri builtUri = Uri.parse(PopularMovieConstants.BASE_URL)
                .buildUpon()
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
        if (params == null) {
            return null;
        }
        Log.d(LOG_TAG, "doInBackground Called");
        String result = getPopularMovies(params[0], params[1]);
        return result;
    }


    @Override
    protected void onPostExecute(String result) {
       // Log.d(LOG_TAG + " onPostExecute-> ", result);
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-DD").create();
        resultsDTO = gson.fromJson(result, MovieResultsDTO.class);
        ((MovieGridViewAdapter) adapter).setGridData(resultsDTO.getResults());
    }
}


