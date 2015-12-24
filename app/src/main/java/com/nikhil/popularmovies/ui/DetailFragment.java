package com.nikhil.popularmovies.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nikhil.popularmovies.R;
import com.nikhil.popularmovies.io.ApiRequests;
import com.nikhil.popularmovies.io.AppRequest;
import com.nikhil.popularmovies.io.BaseTask;
import com.nikhil.popularmovies.utils.Constants;
import com.nikhil.popularmovies.utils.Utility;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

/**
 * Created by nikhil on 22/12/15.
 */
public class DetailFragment extends Fragment implements AppRequest {


    private long id;
    private View view;
    private ImageView moviePoster;
    private String imageUrlSuffix;
    private LinearLayout progress;
    private TextView movie_title;
    private Button markAsFavourate;
    private TextView movieDuration;
    private TextView movieYear;
    private TextView movieRating;
    private TextView movieOverview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            id = bundle.getLong("id");
        }
        imageUrlSuffix = Utility.getImageUrlSuffix(getActivity());
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.detail_fragmnet, container, false);
        progress = (LinearLayout) view.findViewById(R.id.progress);
        progress.setVisibility(View.VISIBLE);
        moviePoster = (ImageView) view.findViewById(R.id.moviePoster);
        moviePoster.setVisibility(View.GONE);
        movie_title = (TextView) view.findViewById(R.id.movie_title);
        movieYear = (TextView) view.findViewById(R.id.movieYear);
        movieDuration = (TextView) view.findViewById(R.id.movieDuration);
        movieRating = (TextView) view.findViewById(R.id.movieRating);
        markAsFavourate = (Button) view.findViewById(R.id.markAsFavourate);
        markAsFavourate.setVisibility(View.GONE);
        movieOverview = (TextView) view.findViewById(R.id.movieOverview);
        updateView();
        return view;
    }


    private void updateView() {
        StringBuilder stringBuilder = new StringBuilder(Constants.BASE_URL_DETAIL);
        stringBuilder.append(String.valueOf(id));
        stringBuilder.append(Constants.API_TAG);
        stringBuilder.append(Constants.MOVIE_DB_API_KEY);
        ApiRequests.getInstance().fetchMovies(getActivity(), this, stringBuilder.toString());
    }

    @Override
    public <T> void onRequestStarted(BaseTask<T> listener, String url) {
        progress.setVisibility(View.VISIBLE);

    }

    @Override
    public <T> void onRequestCompleted(BaseTask<T> listener, String url) {
        progress.setVisibility(View.GONE);

        JSONObject respose = listener.getJsonResponse();
        StringBuilder stringBuilder = new StringBuilder(Constants.BASE_IMAGE_URL_PATH);
        stringBuilder.append(imageUrlSuffix);
        stringBuilder.append(respose.optString("poster_path"));
        moviePoster.setVisibility(View.VISIBLE);
        Picasso.with(getActivity()).load(stringBuilder.toString()).into(moviePoster);

        movie_title.setText(respose.optString("title"));
        String formatedDate = respose.optString("release_date").substring(0, 4);

        movieYear.setText(formatedDate);
        movieDuration.setText(getActivity().getString(R.string.movie_duration, respose.optString("runtime")));
        movieRating.setText(getActivity().getString(R.string.movie_rating, respose.optString("vote_average")));
        movieOverview.setText(respose.optString("overview"));
        markAsFavourate.setText(R.string.mark_as_fav);
        markAsFavourate.setVisibility(View.VISIBLE);


    }

    @Override
    public <T> void onRequestFailed(BaseTask<T> listener, String url) {
        progress.setVisibility(View.GONE);

    }
}
