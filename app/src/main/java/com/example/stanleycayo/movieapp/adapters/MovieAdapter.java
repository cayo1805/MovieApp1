package com.example.stanleycayo.movieapp.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.stanleycayo.movieapp.R;
import com.example.stanleycayo.movieapp.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Created by Stanley CAYO on 08/02/2018.
 */


public class MovieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static final String TAG = MovieAdapter.class.getName();

    private static int POPULAR_VOTE_AVERAGE = 5;

    private static List<Movie> movies;

    // View types
    private final int SHOW_BACKDROP_IMAGE = 0;
    private final int SHOW_POSTER_IMAGE = 1;

    public static class PosterImageViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvTitle)
        TextView tvTitle;

        @BindView(R.id.tvOverview)
        TextView tvOverview;

        @BindView(R.id.ivPoster)
        ImageView ivPoster;

        public PosterImageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public static class BackdropImageViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ivBackdrop)
        ImageView ivBackdrop;

        public BackdropImageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    public MovieAdapter(List<Movie> movieList) {
        movies = movieList;
    }

    public void update(List<Movie> movieList) {
        movies = movieList;
        notifyDataSetChanged();
    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        View movieView;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (parent.getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            movieView = inflater.inflate(R.layout.item_movie, parent, false);
            viewHolder = new PosterImageViewHolder(movieView);
        } else {
            // Portrait layout
            switch (viewType) {
                case SHOW_BACKDROP_IMAGE:
                    movieView = inflater.inflate(R.layout.item_movie_backdrop, parent, false);
                    viewHolder = new BackdropImageViewHolder(movieView);
                    break;

                case SHOW_POSTER_IMAGE:
                    movieView = inflater.inflate(R.layout.item_movie, parent, false);
                    viewHolder = new PosterImageViewHolder(movieView);
            }
        }

        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        // If a context is needed, it can be retrieved
        // from the ViewHolder's root view.
        final Context context = viewHolder.itemView.getContext();
        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            configurePosterImageView(context, (PosterImageViewHolder) viewHolder, position);
        } else {
            // Portrait layout
            switch (viewHolder.getItemViewType()) {
                case SHOW_BACKDROP_IMAGE:
                    configureBackdropImageView(context, (BackdropImageViewHolder) viewHolder, position);
                    break;

                case SHOW_POSTER_IMAGE:
                    configurePosterImageView(context, (PosterImageViewHolder) viewHolder, position);
                    break;

                default:
                    break;
            }
        }

    }

    private void configureBackdropImageView(Context context, BackdropImageViewHolder viewHolder, int position) {
        Movie movie = movies.get(position);

        // Show backdrop image for landscape orientation.
        String imageURL;
        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            imageURL = movie.getBackdropPath();
        } else {
            imageURL = movie.getPosterPath();
        }

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();

        Picasso.with(context).load(imageURL)
                .placeholder(R.drawable.ic_launcher_background)
                .resize(displayMetrics.widthPixels, 0)
                .transform(new RoundedCornersTransformation(10, 10))
                .into(viewHolder.ivBackdrop);

    }


    private void configurePosterImageView(Context context, PosterImageViewHolder viewHolder, int position) {
        Movie movie = movies.get(position);

        viewHolder.tvOverview.setText(movie.getOverview());
        viewHolder.tvTitle.setText(movie.getTitle());

        // Show backdrop image for landscape orientation.
        String imageURL;
        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            imageURL = movie.getBackdropPath();
        } else {
            imageURL = movie.getPosterPath();
        }

        // use placeholder & rounded corners transformation
        Picasso.with(context).load(imageURL)
                .placeholder(R.drawable.ic_launcher_background)
                .transform(new RoundedCornersTransformation(10, 10))
                .into(viewHolder.ivPoster);
    }

    @Override
    public int getItemViewType(int position) {
        Movie movie = movies.get(position);
        Log.d(TAG, "getItemViewType: Movie: "+ movie.getTitle() + " vote: "+ movie.getVoteAverage());

        if (movie.getVoteAverage() > POPULAR_VOTE_AVERAGE) {
            Log.d(TAG, "Movie: "+ movie.getTitle() + " vote: "+ movie.getVoteAverage());
            return SHOW_BACKDROP_IMAGE;
        }

        return SHOW_POSTER_IMAGE;
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return movies.size();
    }
}

