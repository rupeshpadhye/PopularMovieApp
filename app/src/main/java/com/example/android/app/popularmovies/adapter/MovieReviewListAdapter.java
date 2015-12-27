package com.example.android.app.popularmovies.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.android.app.popularmovies.R;
import com.example.android.app.popularmovies.dto.MovieReview;
import java.util.List;

/**
 * Created by RUPESH on 12/26/2015.
 */
public class MovieReviewListAdapter extends RecyclerView.Adapter<MovieReviewListAdapter.DataObjectHolder> {

    private List<MovieReview> mMovieReviewArrayList;
    public MovieReviewListAdapter(List<MovieReview> movieReviewArrayList) {
        mMovieReviewArrayList = movieReviewArrayList;
    }

    public static class DataObjectHolder extends RecyclerView.ViewHolder {
        TextView author;
        TextView content;
        public DataObjectHolder(View itemView) {
            super(itemView);
            author = (TextView) itemView.findViewById(R.id.author);
            content = (TextView) itemView.findViewById(R.id.content);
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
                .inflate(R.layout.review_item, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        holder.author.setText(mMovieReviewArrayList.get(position).getAuthor());
        holder.content.setText(mMovieReviewArrayList.get(position).getContent());
    }

    public void setGridData(List<MovieReview> movieReviewListData) {
        this.mMovieReviewArrayList.clear();
        this.mMovieReviewArrayList.addAll(movieReviewListData);
        this.notifyDataSetChanged();
    }

}

