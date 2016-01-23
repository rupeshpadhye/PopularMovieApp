//--------------------------------------------------------------------------------------------------
package com.example.android.app.popularmovies.fragmetns;
//--------------------------------------------------------------------------------------------------

import android.content.Intent;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
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
import com.example.android.app.popularmovies.helpers.DatabaseHelper;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.squareup.picasso.Picasso;

import java.sql.SQLException;
import java.util.Calendar;
//--------------------------------------------------------------------------------------------------

/**
 * @Author Rupesh Padhye
 */
//--------------------------------------------------------------------------------------------------
public class MovieDetailFragment extends Fragment {

    private ImageButton favButton;
    private static String LOG_TAG = MovieDetailFragment.class.getSimpleName();
    private Dao<MovieDetails, Integer> movieDetailsLongDAO;

    public MovieDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            movieDetailsLongDAO = OpenHelperManager.getHelper(getContext(),
                    DatabaseHelper.class).getMovieDetailsDAO();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreateView Started");
        View rootView = inflater.inflate(R.layout.movie_detail_fragment, container, false);
        boolean dualPane = getResources().getBoolean(R.bool.dual_pane);
        if (dualPane) {
            rootView.setVisibility(View.INVISIBLE);
        }
        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra(PopularMovieConstants.MOVIE_DATA)) {
            final MovieDetails movieDetails = intent.getParcelableExtra(PopularMovieConstants.MOVIE_DATA);
            updateUI(movieDetails, rootView);
        }
        return rootView;
    }


    public void updateView(final MovieDetails movieDetails) {
        updateUI(movieDetails, getView());
    }

    private boolean isFavMovie(MovieDetails movieDetails) {
        boolean isFav = false;
        try {
            isFav = movieDetailsLongDAO.idExists(movieDetails.getId());
            Log.d(LOG_TAG, "movie is fav" + isFav);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isFav;
    }

    private void updateUI(final MovieDetails movieDetails, View view) {
        Log.d(LOG_TAG, movieDetails.toString());
        view.setVisibility(View.VISIBLE);
        ((TextView) view.findViewById(R.id.movieTitle)).setText(
                movieDetails.getOriginal_title());
        ((TextView) view.findViewById(R.id.rating)).setText(
                Float.toString(movieDetails.getVote_average()));
        ((TextView) view.findViewById(R.id.description)).setText(
                movieDetails.getOverview());
        ((TextView) view.findViewById(R.id.releaseDate)).setText(movieDetails.getRelease_date());
        ((TextView) view.findViewById(R.id.language)).setText(movieDetails.getOriginal_language());
        if (movieDetails.getPoster_path() != null && !movieDetails.getPoster_path().isEmpty()) {
            Picasso.with(getContext())
                    .load(PopularMovieConstants.IMG_URL_W185 + movieDetails.getPoster_path())
                    .into(((ImageView) view.findViewById(R.id.imageView)));

        } else {
            ((ImageView) view.findViewById(R.id.imageView))
                    .setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.no_image));
        }

        favButton = (ImageButton) view.findViewById(R.id.favButton);
        Drawable drawable;
        if (isFavMovie(movieDetails)) {
            drawable = ContextCompat.getDrawable(getContext(), R.drawable.ic_favorite);
        }
        else {
            drawable = ContextCompat.getDrawable(getContext(), R.drawable.ic_favorite_outline);
        }

        favButton.setImageDrawable(drawable);
        favButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Drawable drawable;
                try {
                    if (!isFavMovie(movieDetails)) {
                        movieDetailsLongDAO.create(movieDetails);
                        drawable = ContextCompat.getDrawable(getContext(), R.drawable.ic_favorite);
                        Log.d(LOG_TAG, "new row inserted");
                    } else {
                        movieDetailsLongDAO.deleteById(movieDetails.getId());
                        drawable = ContextCompat.getDrawable(getContext(), R.drawable.ic_favorite_outline);
                        Log.d(LOG_TAG, "movie deleted");
                    }
                    favButton.setImageDrawable(drawable);
                } catch (SQLException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Something Went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
//--------------------------------------------------------------------------------------------------
//
//--------------------------------------------------------------------------------------------------