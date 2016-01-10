package com.example.android.app.popularmovies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.app.popularmovies.R;
import com.example.android.app.popularmovies.dto.MovieTrailer;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by RUPESH on 12/27/2015.
 */
public class MovieTrailerListAdapter extends RecyclerView.Adapter<MovieTrailerListAdapter.DataObjectHolder> {

    private List<MovieTrailer> mMovieReviewArrayList;
    private static itemClickInterface itemClickListener;

    public MovieTrailerListAdapter(List<MovieTrailer> movieReviewArrayList) {
        mMovieReviewArrayList = movieReviewArrayList;
    }

    public static void setOnItemClickListener(itemClickInterface listener) {
        itemClickListener = listener;
    }

    public MovieTrailer getItem(int pos) {
        return mMovieReviewArrayList.get(pos);
    }

    public List<MovieTrailer> getGridData() {
        return mMovieReviewArrayList;
    }


    public static class DataObjectHolder
            extends     RecyclerView.ViewHolder
            implements  View.OnClickListener {
                    TextView trailerName;
                    ImageView videoImage;

                    public DataObjectHolder(View itemView) {
                        super(itemView);
                        trailerName = (TextView) itemView.findViewById(R.id.trailername);
                        videoImage=(ImageView)itemView.findViewById(R.id.videoImage);
                        itemView.setOnClickListener(this);

                    }

                    @Override
                    public void onClick(View v) {
                        itemClickListener.onClick(getAdapterPosition(), v);
                    }
            }

    @Override
    public int getItemCount() {
        return mMovieReviewArrayList.size();
    }


    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.treailer_item, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        holder.trailerName.setText(mMovieReviewArrayList.get(position).getName());
        Context context=holder.videoImage.getContext();
        String key=mMovieReviewArrayList.get(position).getKey();
        String url="http://img.youtube.com/vi/"+key+"/default.jpg";
        Picasso.with(context).load(url).into(holder.videoImage);
    }

    public void setGridData(List<MovieTrailer> movieReviewListData) {
        this.mMovieReviewArrayList.clear();
        this.mMovieReviewArrayList.addAll(movieReviewListData);
        this.notifyDataSetChanged();
    }


    public interface itemClickInterface {
        void onClick(int pos, View view);
    }
}
