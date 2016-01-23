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
    private List<MovieReview> movieReviewList;
    private static final String LOG_TAG = MovieReviewFragment.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG,"onCreate called");
        if (savedInstanceState != null && savedInstanceState.containsKey("REVIEW_DATA")) {
            movieReviewList = savedInstanceState.getParcelableArrayList("REVIEW_DATA");
        } else {
            movieReviewList = new ArrayList<>();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.movie_review, container, false);

        boolean dualPane = getResources().getBoolean(R.bool.dual_pane);
        if (dualPane) {
            rootView.setVisibility(View.INVISIBLE);
        }
        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra(PopularMovieConstants.MOVIE_DATA)) {
            MovieDetails movieDetails = intent.getParcelableExtra(PopularMovieConstants.MOVIE_DATA);
           // initUI(rootView);
            progressBar = ((ProgressBar) rootView.findViewById(R.id.reviewProgessBar));
            mRecyclerView = (RecyclerView) rootView.findViewById(R.id.MovieReviewView);
            mRecyclerView.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(getContext());
            mRecyclerView.setLayoutManager(mLayoutManager);
            mAdapter = new MovieReviewListAdapter(movieReviewList);
            mRecyclerView.setAdapter(mAdapter);
            if (mAdapter.getItemCount() == 0) {
                getReview(movieDetails);
            } else {
                progressBar.setVisibility(View.GONE);
            }
        }
        return rootView;
    }

    public void initUI(View view) {
        Log.d(LOG_TAG,"start initView");
        progressBar = ((ProgressBar) view.findViewById(R.id.reviewProgessBar));
        mRecyclerView = (RecyclerView) view.findViewById(R.id.MovieReviewView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MovieReviewListAdapter(movieReviewList);
        mRecyclerView.setAdapter(mAdapter);
    }


    public void updateView(MovieDetails MovieDetails) {
        //initUI(getView());
        getView().setVisibility(View.VISIBLE);
        progressBar=(ProgressBar)getView().findViewById(R.id.reviewProgessBar);
        mRecyclerView=(RecyclerView) getView().findViewById(R.id.MovieReviewView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MovieReviewListAdapter(new ArrayList<MovieReview>());
        mRecyclerView.setAdapter(mAdapter);

        getReview(MovieDetails);
        Log.d(LOG_TAG,"update View Completed");
    }
    public void getReview(MovieDetails MovieDetails) {
        Log.d(LOG_TAG, "updateView MovieReviewFragment");
        Uri builtUri = Uri.parse(PopularMovieConstants.BASE_URL)
                .buildUpon()
                .appendPath(PopularMovieConstants.MOVIE_APPENDER)
                .appendPath(PopularMovieConstants.MOVIE_PATH)
                .appendPath(Integer.toString(MovieDetails.getId()))
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
                Log.d(LOG_TAG, "success");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((MovieReviewListAdapter) mAdapter).setGridData(reviews.getResults());
                        Log.d(LOG_TAG, "ui" + ((MovieReviewListAdapter) mAdapter).getItemCount());
                        progressBar.setVisibility(View.GONE);

                    }

                });

            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("REVIEW_DATA"
                , (ArrayList<? extends Parcelable>) ((MovieReviewListAdapter) mAdapter).getGridData());
        super.onSaveInstanceState(outState);
    }

}
