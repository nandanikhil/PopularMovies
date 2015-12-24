package com.nikhil.popularmovies.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.nikhil.popularmovies.R;

public class MainActivity extends AppCompatActivity {


    private FrameLayout mainContainer;
    private boolean mTwoPane;
    private MoviesListFragment moviesListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.detailContainer) == null) {
            mTwoPane = false;
        } else {
            mTwoPane = true;
        }
        mainContainer = (FrameLayout) findViewById(R.id.mainContainer);
        getFragmentManager().beginTransaction().add(R.id.mainContainer,  getFragment(), MoviesListFragment.class.getSimpleName()).addToBackStack(null).commitAllowingStateLoss();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            if (mTwoPane) {
                Fragment fragment = getFragmentManager().findFragmentByTag(DetailFragment.class.getSimpleName());
                if (fragment != null)
                    getFragmentManager().beginTransaction().remove(fragment).commitAllowingStateLoss();
            }

            getFragmentManager().beginTransaction().add(R.id.mainContainer, new SettingsFragment(), SettingsFragment.class.getSimpleName()).addToBackStack(null).commitAllowingStateLoss();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public Fragment getCurrentFragment() {
        return getFragmentManager().findFragmentById(R.id.mainContainer);
    }

    @Override
    public void onBackPressed() {

        if (getCurrentFragment() instanceof SettingsFragment || getCurrentFragment() instanceof DetailFragment) {
            getFragmentManager().beginTransaction().add(R.id.mainContainer, getFragment(), MoviesListFragment.class.getSimpleName()).addToBackStack(null).commitAllowingStateLoss();
        } else {
            super.onBackPressed();
        }
    }

    private MoviesListFragment getFragment() {


        moviesListFragment = new MoviesListFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("isTwoPane", mTwoPane);
        moviesListFragment.setArguments(bundle);
        return moviesListFragment;
    }

}
