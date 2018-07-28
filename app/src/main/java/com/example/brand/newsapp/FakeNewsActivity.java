package com.example.brand.newsapp;


import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class FakeNewsActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<FakeNews>> {

    private static final String GAURDIAN_API_URL = "https://content.guardianapis.com/search?api-key=7e333feb-30bb-423d-9836-80d0459d9b25";
    private static final int FAKENEWSFEED_LOADER_ID = 1;

    private FakeNewsAdapter mFakeAdapter;
    private TextView mEmptyStateTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fake_news_activity);
        initFakeNewsList();
        if (isInternetAvailable()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(FAKENEWSFEED_LOADER_ID, null, this);
        } else {
            mEmptyStateTextView.setText(R.string.internet_connection_error);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(FAKENEWSFEED_LOADER_ID, null, this);
    }


    private boolean isInternetAvailable() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private void initFakeNewsList() {
        ListView fakeNewsListView = (ListView) findViewById(R.id.list);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        fakeNewsListView.setEmptyView(mEmptyStateTextView);

        mFakeAdapter = new FakeNewsAdapter(this, new ArrayList<FakeNews>());
        fakeNewsListView.setAdapter(mFakeAdapter);

        fakeNewsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                FakeNews currentFakeNews = mFakeAdapter.getItem(position);
                Uri newsfeedUri = Uri.parse(currentFakeNews.getfakeNewsUrl());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsfeedUri);
                startActivity(websiteIntent);
            }
        });
    }


    @Override
    // onCreateLoader instantiates and returns a new Loader for the given ID
    public Loader<List<FakeNews>> onCreateLoader(int i, Bundle bundle) {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        // getString retrieves a String value from the preferences; the second parameter is the default value for this preference
        String topic = sharedPrefs.getString(
                getString(R.string.settings_section_category_key),
                getString(R.string.settings_section_category_default));


        // parse breaks apart the URI string that's passed into its parameter
        Uri baseUri = Uri.parse(GAURDIAN_API_URL);
        // buildUpon prepares the baseUri that we just parsed so we can add query parameters to it
        Uri.Builder uriBuilder = baseUri.buildUpon();

        //uriBuilder.appendQueryParameter("order-by", orderBy);
        uriBuilder.appendQueryParameter("show-fields", "all");
        uriBuilder.appendQueryParameter("q", topic);

        String urlAddress = uriBuilder.toString();

        Log.v("MainActivity", "addressUrl: " + urlAddress);

        // Create a new loader for the given URL
        return new FakeNewsLoader(this, urlAddress);
    }
    @Override
    public void onLoadFinished(Loader<List<FakeNews>> loader, List<FakeNews> data) {
        mEmptyStateTextView.setText(R.string.no_fake_news);
        mFakeAdapter.clear();
        if (data != null && !data.isEmpty()) {
            mFakeAdapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<FakeNews>> loader) {
        mFakeAdapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

