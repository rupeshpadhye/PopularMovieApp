package com.example.android.app.popularmovies.fragmetns;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import com.example.android.app.popularmovies.BuildConfig;
import com.example.android.app.popularmovies.R;
import com.example.android.app.popularmovies.adapter.MovieReviewListAdapter;
import com.example.android.app.popularmovies.adapter.MovieTrailerListAdapter;
import com.example.android.app.popularmovies.constants.PopularMovieConstants;
import com.example.android.app.popularmovies.dto.MovieDetails;
import com.example.android.app.popularmovies.dto.MovieReview;
import com.example.android.app.popularmovies.dto.MovieReviewsDTO;
import com.example.android.app.popularmovies.dto.MovieTrailer;
import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by RUPESH on 12/25/2015.
 */
public class MovieReviewFragment extends Fragment {

    private ProgressBar progressBar;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static final String LOG_TAG = MovieReviewFragment.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.movie_review, container, false);
        Intent intent = getActivity().getIntent();
       if (intent != null && intent.hasExtra(PopularMovieConstants.MOVIE_DATA)) {
            MovieDetails movieDetails = intent.getParcelableExtra(PopularMovieConstants.MOVIE_DATA);
            progressBar = ((ProgressBar) rootView.findViewById(R.id.reviewProgessBar));
            mRecyclerView = (RecyclerView) rootView.findViewById(R.id.MovieReviewView);
            mRecyclerView.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(getContext());
            mRecyclerView.setLayoutManager(mLayoutManager);
           mAdapter = new MovieReviewListAdapter(new ArrayList<MovieReview>());
           mRecyclerView.setAdapter(mAdapter);
           updateView(movieDetails);
       }
      return rootView;
    }

        public void updateView(MovieDetails movieDetails) {
        Uri builtUri = Uri.parse(PopularMovieConstants.BASE_URL)
                .buildUpon()
                .appendPath(PopularMovieConstants.MOVIE_APPENDER)
                .appendPath(PopularMovieConstants.MOVIE_PATH)
                .appendPath(Integer.toString(movieDetails.getId()))
                .appendPath(PopularMovieConstants.REVIEWS)
                .appendQueryParameter(PopularMovieConstants.API_KEY_PARAM, BuildConfig.THE_MOVIE_DB_API_KEY)
                .build();

        Log.d(LOG_TAG, builtUri.toString());
        Request request = new Request.Builder()
                .url(builtUri.toString())
                .build();

        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException throwable) {
                throwable.printStackTrace();

            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (!response.isSuccessful())
                    throw new IOException("Unexpected code " + response);
                Gson gson = new Gson();
                final MovieReviewsDTO reviews = gson.fromJson(response.body().charStream(), MovieReviewsDTO.class);
                Log.d(LOG_TAG, reviews.getResults().toString());
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((MovieReviewListAdapter) mAdapter).setGridData(reviews.getResults());
                        progressBar.setVisibility(View.GONE);

                    }

                });

            }
        });

    }

}
