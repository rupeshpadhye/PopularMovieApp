//--------------------------------------------------------------------------------------------------
package com.example.android.app.popularmovies.fragmetns;
//--------------------------------------------------------------------------------------------------

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

    private ImageButton favButton;

    private static String LOG_TAG = MovieDetailFragment.class.getSimpleName();


    public MovieDetailFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreateView Started");
        boolean dualPane = getResources().getBoolean(R.bool.dual_pane);
        View rootView = inflater.inflate(R.layout.movie_detail_fragment, container, false);
       /* if (dualPane) {
            rootView.setVisibility(View.INVISIBLE);
        }*/
        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra(PopularMovieConstants.MOVIE_DATA)) {
           final MovieDetails movieDetails = intent.getParcelableExtra(PopularMovieConstants.MOVIE_DATA);
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

            favButton = (ImageButton) rootView.findViewById(R.id.favButton);
            favButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    Drawable drawable;
                    if(isFavMovie(movieDetails)) {

                        drawable= ContextCompat.getDrawable(getContext(), android.R.drawable.star_on);
                    }
                    else{
                        drawable= ContextCompat.getDrawable(getContext(), android.R.drawable.star_off);
                    }
                    favButton.setImageDrawable(drawable);
                }
            });
        }
        return rootView;
    }

    public boolean isFavMovie(MovieDetails movieDetails)
    {
        return false;
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

        } else {
            ((ImageView) getView().findViewById(R.id.imageView))
                    .setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.no_image));
        }
    }

}
//--------------------------------------------------------------------------------------------------
//
//--------------------------------------------------------------------------------------------------