package com.example.brand.newsapp;


import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class FakeNewsActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<FakeNews>> {

    private static final String GAURDIAN_API_URL = "http://content.guardianapis.com/search?show-tags=contributor&order-by=newest&page-size=10&api-key=1a6c86d0-7270-4bc3-bd92-11c8c0d9db02";
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
    public Loader<List<FakeNews>> onCreateLoader(int id, Bundle args) {
        return new FakeNewsLoader(this, GAURDIAN_API_URL);
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
}

