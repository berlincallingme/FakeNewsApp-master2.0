package com.example.brand.newsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

public class FakeNewsLoader extends AsyncTaskLoader<List<FakeNews>> {

    private String mUrl;


    public FakeNewsLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<FakeNews> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        List<FakeNews> fakenewsfeeds = QueryUtils.fetchFakeNewsData(mUrl);
        return fakenewsfeeds;
    }
}
