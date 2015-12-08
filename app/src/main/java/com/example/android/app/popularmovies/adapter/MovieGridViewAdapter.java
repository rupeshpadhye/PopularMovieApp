package com.example.android.app.popularmovies.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.android.app.popularmovies.R;
import com.example.android.app.popularmovies.dto.MovieDetails;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by RUPESH on 12/3/2015.
 */
public final class MovieGridViewAdapter extends ArrayAdapter<MovieDetails> {

    private Context mContext;
    private int layoutResourceId;
    private List<MovieDetails> mGridData;
    private static final String LOG_TAG = MovieGridViewAdapter.class.getSimpleName();

    public MovieGridViewAdapter(Context mContext, int layoutResourceId, List<MovieDetails> mGridData) {
        super(mContext, layoutResourceId, mGridData);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.mGridData = mGridData;
    }

    public void setGridData(List<MovieDetails> mGridData) {
        this.mGridData.clear();
        this.mGridData.addAll(mGridData);
        this.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;
        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.imageView = (ImageView) row.findViewById(R.id.movieImageItem);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        MovieDetails item = mGridData.get(position);
        String imagePath = "http://image.tmdb.org/t/p/w185/" + item.getPoster_path();
        //  Log.d(LOG_TAG,"imagePath ->"+imagePath);
        Picasso.with(mContext).load(imagePath).into(holder.imageView);
        return row;
    }

    static class ViewHolder {
        ImageView imageView;
    }
}
//--------------------------------------------------------------------------------------------------
//
//--------------------------------------------------------------------------------------------------


