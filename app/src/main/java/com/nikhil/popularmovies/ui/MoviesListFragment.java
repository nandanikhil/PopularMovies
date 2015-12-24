package com.nikhil.popularmovies.ui;

import android.app.Activity;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.nikhil.popularmovies.R;
import com.nikhil.popularmovies.io.ApiRequests;
import com.nikhil.popularmovies.io.AppRequest;
import com.nikhil.popularmovies.io.BaseTask;
import com.nikhil.popularmovies.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by nikhil on 20/12/15.
 */
public class MoviesListFragment extends Fragment implements AppRequest {

    private View view;
    private RecyclerView moviesList;
    private GridLayoutManager gridLayoutManager;
    private MovieListAdapter movieListAdapter = null;


    private LinearLayout progress;
    private ArrayList<String> posterUrls;
    private ArrayList<Long> movieIds;
    private Activity activity;
    private boolean mTwoPane;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        activity = getActivity();
        if (getArguments() != null) {
            mTwoPane = getArguments().getBoolean("isTwoPane");
        }

    }


    public MoviesListFragment() {

    }

    @Override
    public void onResume() {


        super.onResume();
        updateView();

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.list_fragment, container, false);
        progress = (LinearLayout) view.findViewById(R.id.progress);
        progress.setVisibility(View.VISIBLE);
        moviesList = (RecyclerView) view.findViewById(R.id.moviesList);
        if (mTwoPane) {
            gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        } else {
            gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        }

        moviesList.setLayoutManager(gridLayoutManager);

        return view;
    }

    @Override
    public <T> void onRequestStarted(BaseTask<T> listener, String url) {
        progress.setVisibility(View.VISIBLE);
    }

    @Override
    public <T> void onRequestCompleted(BaseTask<T> listener, String url) {
        progress.setVisibility(View.GONE);
        posterUrls = new ArrayList<String>();
        movieIds = new ArrayList<Long>();
        if (listener.getJsonResponse() != null && listener.getJsonResponse() instanceof JSONObject) {

            JSONArray jsonArray = listener.getJsonResponse().optJSONArray("results");
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    movieIds.add(jsonObject.optLong("id"));
                    posterUrls.add(jsonObject.optString("poster_path"));
                } catch (JSONException e) {

                    e.printStackTrace();
                }

            }
            movieListAdapter = new MovieListAdapter(posterUrls, movieIds, getActivity(), mTwoPane);
            moviesList.setAdapter(movieListAdapter);
            movieListAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public <T> void onRequestFailed(BaseTask<T> listener, String url) {
        progress.setVisibility(View.GONE);
        Toast.makeText(getActivity(), R.string.request_failed, Toast.LENGTH_SHORT).show();
    }


    public void updateView() {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        String sortType = preferences.getString(getString(R.string.pref_key), getString(R.string.most_popular));

        if (sortType.equalsIgnoreCase(getString(R.string.most_popular))) {
            ApiRequests.getInstance().fetchMovies(getActivity(), MoviesListFragment.this, Constants.BASE_URL_POPULAR_MOVIES + Constants.MOVIE_DB_API_KEY);
        } else {
            ApiRequests.getInstance().fetchMovies(getActivity(), MoviesListFragment.this, Constants.BASE_URL_HIGHEST_RATED_MOVIES + Constants.MOVIE_DB_API_KEY);
        }


    }
}
