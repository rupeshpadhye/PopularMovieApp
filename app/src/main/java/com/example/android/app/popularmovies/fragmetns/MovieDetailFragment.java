//--------------------------------------------------------------------------------------------------
package com.example.android.app.popularmovies.fragmetns;
//--------------------------------------------------------------------------------------------------

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.app.popularmovies.R;
import com.example.android.app.popularmovies.constants.PopularMovieConstants;
import com.example.android.app.popularmovies.dto.MovieDetails;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
//--------------------------------------------------------------------------------------------------

/**
 * @Author Rupesh Padhye
 */
//--------------------------------------------------------------------------------------------------
public class MovieDetailFragment extends Fragment {

    private static String LOG_TAG = MovieDetailFragment.class.getSimpleName();

    public MovieDetailFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreateView Started");
      boolean dualPane= getResources().getBoolean(R.bool.dual_pane);
        View rootView = inflater.inflate(R.layout.movie_detail_fragment, container, false);
        if (dualPane)
        {
            rootView.setVisibility(View.INVISIBLE);
        }
        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra(PopularMovieConstants.MOVIE_DATA)) {
            MovieDetails movieDetails = intent.getParcelableExtra(PopularMovieConstants.MOVIE_DATA);
            Log.d(LOG_TAG, movieDetails.toString());
            ((TextView) rootView.findViewById(R.id.movieTitle)).setText(
                    movieDetails.getOriginal_title());
            ((TextView) rootView.findViewById(R.id.rating)).setText(
                    Float.toString(movieDetails.getVote_average()));
            ((TextView) rootView.findViewById(R.id.description)).setText(
                    movieDetails.getOverview());
            ((TextView) rootView.findViewById(R.id.releaseDate)).setText(movieDetails.getRelease_date());
            ((TextView) rootView.findViewById(R.id.language)).setText(movieDetails.getOriginal_language());
            if (movieDetails.getPoster_path() != null && !movieDetails.getPoster_path().isEmpty()) {
                Picasso.with(getContext())
                        .load(PopularMovieConstants.IMG_URL_W185 + movieDetails.getPoster_path())
                        .into(((ImageView) rootView.findViewById(R.id.imageView)));

            }

        }

        return rootView;
    }

    public void updateView(MovieDetails movieDetails) {
        Log.d(LOG_TAG, movieDetails.toString());
        getView().setVisibility(View.VISIBLE);
        ((TextView) getView().findViewById(R.id.movieTitle)).setText(
                movieDetails.getOriginal_title());
        ((TextView) getView().findViewById(R.id.rating)).setText(
                Float.toString(movieDetails.getVote_average()));
        ((TextView) getView().findViewById(R.id.description)).setText(
                movieDetails.getOverview());
        ((TextView) getView().findViewById(R.id.releaseDate)).setText(movieDetails.getRelease_date());
        ((TextView) getView().findViewById(R.id.language)).setText(movieDetails.getOriginal_language());
        if (movieDetails.getPoster_path() != null && !movieDetails.getPoster_path().isEmpty()) {
            Picasso.with(getContext())
                    .load(PopularMovieConstants.IMG_URL_W185 + movieDetails.getPoster_path())
                    .into(((ImageView) getView().findViewById(R.id.imageView)));

        }
        else {
            ((ImageView) getView().findViewById(R.id.imageView))
                    .setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.no_image));
        }
    }

}
//--------------------------------------------------------------------------------------------------
//
//--------------------------------------------------------------------------------------------------