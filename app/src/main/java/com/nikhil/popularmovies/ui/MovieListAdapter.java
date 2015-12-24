package com.nikhil.popularmovies.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nikhil.popularmovies.R;
import com.nikhil.popularmovies.utils.Constants;
import com.nikhil.popularmovies.utils.Utility;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by nikhil on 21/12/15.
 */
public class MovieListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private DetailFragment detailFragment = null;
    private Bundle bundle = null;
    private ArrayList<String> posterUrls;
    private ArrayList<Long> movieIds = null;
    private Activity activity;
    private String imageUrlSuffix = null;
    private boolean isTwoPane;
    private int container;

    public MovieListAdapter(ArrayList<String> posterUrls, ArrayList<Long> movieIds, Activity activity, boolean isTwoPane) {

        this.posterUrls = posterUrls;
        this.activity = activity;
        this.movieIds = movieIds;
        this.isTwoPane = isTwoPane;
        imageUrlSuffix = Utility.getImageUrlSuffix(activity);

        bundle = new Bundle();
        if (isTwoPane) {
            container = R.id.detailContainer;
            detailFragment = new DetailFragment();
            bundle.putLong("id", movieIds.get(0));
            detailFragment.setArguments(bundle);
            activity.getFragmentManager().beginTransaction().add(container, detailFragment, DetailFragment.class.getSimpleName()).addToBackStack(null).commitAllowingStateLoss();
        } else {
            container = R.id.mainContainer;
        }


    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = activity.getLayoutInflater().inflate(R.layout.list_item, parent, false);
        return new ListItemViewHolder(view);


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        ((ListItemViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                detailFragment = new DetailFragment();
                bundle.putLong("id", movieIds.get(position));
                detailFragment.setArguments(bundle);
                activity.getFragmentManager().beginTransaction().add(container, detailFragment, DetailFragment.class.getSimpleName()).addToBackStack(null).commitAllowingStateLoss();
            }
        });

        StringBuilder stringBuilder = new StringBuilder(Constants.BASE_IMAGE_URL_PATH);
        stringBuilder.append(imageUrlSuffix);
        stringBuilder.append(posterUrls.get(position));
        Picasso.with(activity).load(stringBuilder.toString()).into(((ListItemViewHolder) holder).moviePoster);


    }

    @Override
    public int getItemCount() {
        return posterUrls.size();
    }


    public class ListItemViewHolder extends RecyclerView.ViewHolder {

        public ImageView moviePoster;


        public ListItemViewHolder(View itemView) {
            super(itemView);
            moviePoster = (ImageView) itemView.findViewById(R.id.moviePoster);

        }
    }

    private DetailFragment getDetailFragment() {


        return detailFragment;

    }
}
