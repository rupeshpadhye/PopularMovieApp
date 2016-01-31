package com.example.android.app.popularmovies.fragmetns;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.android.app.popularmovies.BuildConfig;
import com.example.android.app.popularmovies.R;
import com.example.android.app.popularmovies.adapter.MovieTrailerListAdapter;
import com.example.android.app.popularmovies.constants.PopularMovieConstants;
import com.example.android.app.popularmovies.dto.MovieDetails;
import com.example.android.app.popularmovies.dto.MovieTrailer;
import com.example.android.app.popularmovies.dto.MovieTrailersDTO;
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
public class MovieTrailerFragment extends Fragment {


    private ProgressBar progressBar;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<MovieTrailer> movieTrailerList;
    private static final String LOG_TAG = MovieTrailerFragment.class.getSimpleName();
    private ShareActionProvider mShareActionProvider;
    private MovieTrailer mTrailerShare;

    public MovieTrailerFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.share_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.action_share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        if (mTrailerShare != null) {
            mShareActionProvider.setShareIntent(createShareTrailerIntent());
        }
    }


    public Intent createShareTrailerIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        String data="http://www.youtube.com/watch?v=" +mTrailerShare.getKey()+"#PopularMoviesTrailers-"+mTrailerShare.getName();
        shareIntent.putExtra(Intent.EXTRA_TEXT, data);
        return shareIntent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.containsKey("TRAILER_DATA")) {
            movieTrailerList = savedInstanceState.getParcelableArrayList("TRAILER_DATA");
            mTrailerShare=movieTrailerList.get(0);
        } else {
            movieTrailerList = new ArrayList<>();
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.movie_trailer, container, false);
        boolean dualPane = getResources().getBoolean(R.bool.dual_pane);
        if (dualPane) {
            rootView.setVisibility(View.INVISIBLE);
        }
        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra(PopularMovieConstants.MOVIE_DATA)) {
            MovieDetails movieDetails = intent.getParcelableExtra(PopularMovieConstants.MOVIE_DATA);
            initUI(rootView);

            if (mAdapter.getItemCount() == 0) {
                getTrailers(movieDetails);
            } else {
                progressBar.setVisibility(View.GONE);
            }
        }
        return rootView;
    }

    public void initUI(View view) {
        progressBar = ((ProgressBar) view.findViewById(R.id.trailerProgessBar));
        mRecyclerView = (RecyclerView) view.findViewById(R.id.movieTrailerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MovieTrailerListAdapter(movieTrailerList);
        mRecyclerView.setAdapter(mAdapter);

        ((MovieTrailerListAdapter) mAdapter).setOnItemClickListener(new MovieTrailerListAdapter
                .itemClickInterface() {
            @Override
            public void onClick(int pos, View view) {
                MovieTrailer trailer = ((MovieTrailerListAdapter) mAdapter).getItem(pos);
                Log.d(LOG_TAG, "calling youtube intent");
                Intent intent = new Intent(Intent.ACTION_VIEW
                        , Uri.parse("http://www.youtube.com/watch?v=" + trailer.getKey()));
                startActivity(intent);
            }
        });
    }

    public void updateView(MovieDetails movieDetails) {
        getView().setVisibility(View.VISIBLE);
        initUI(getView());
        getTrailers(movieDetails);
    }

    public void getTrailers(MovieDetails movieDetails) {
        Log.d(LOG_TAG, "updateView MovieTrailerFragment callded");
        Uri builtUri = Uri.parse(PopularMovieConstants.BASE_URL)
                .buildUpon()
                .appendPath(PopularMovieConstants.MOVIE_APPENDER)
                .appendPath(PopularMovieConstants.MOVIE_PATH)
                .appendPath(Integer.toString(movieDetails.getId()))
                .appendPath(PopularMovieConstants.VIDEOS)
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
                final MovieTrailersDTO trailersDTO = gson.fromJson(response.body().charStream(), MovieTrailersDTO.class);
                Log.d(LOG_TAG, trailersDTO.getResults().toString());
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((MovieTrailerListAdapter) mAdapter).setGridData(trailersDTO.getResults());
                        if(mAdapter.getItemCount() >0) {
                            mTrailerShare = ((MovieTrailerListAdapter) mAdapter).getItem(0);
                        }
                        if (mShareActionProvider != null) {
                            mShareActionProvider.setShareIntent(createShareTrailerIntent());
                        }
                        progressBar.setVisibility(View.GONE);
                    }

                });

            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        if(mAdapter.getItemCount()>0) {
            outState.putParcelableArrayList("TRAILER_DATA"
                    , (ArrayList<? extends Parcelable>) ((MovieTrailerListAdapter) mAdapter).getGridData());

        }
        super.onSaveInstanceState(outState);
    }

}
