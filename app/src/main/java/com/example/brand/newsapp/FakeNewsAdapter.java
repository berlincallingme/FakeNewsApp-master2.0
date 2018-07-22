package com.example.brand.newsapp;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;


public class FakeNewsAdapter extends ArrayAdapter<FakeNews> {


    private static final String DATETIME_SEPARATOR = "T";


    public FakeNewsAdapter(Context context, List<FakeNews> Newsfeeds) {
        super(context, 0, Newsfeeds);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.fake_news_list_item, parent, false);
        }
        FakeNews currentNewsfeed = getItem(position);
        TextView sectionView = listItemView.findViewById(R.id.fake_section);
        sectionView.setText(currentNewsfeed.getSectionName());

        TextView titleView = (TextView) listItemView.findViewById(R.id.fake_news_title);
        titleView.setText(currentNewsfeed.getfakeNewsTitle());
        String author = currentNewsfeed.getfakeAuthorLastName();
        TextView authorView = (TextView) listItemView.findViewById(R.id.fake_author);
        authorView.setText(author);

        String originalDateTime = currentNewsfeed.getfakeNewsDate();

        String theDate = "";

        if (originalDateTime.contains(DATETIME_SEPARATOR)) {

            String[] parts = originalDateTime.split(DATETIME_SEPARATOR);
            theDate = parts[0];
        }
        TextView dateView = (TextView) listItemView.findViewById(R.id.date);
        dateView.setText(theDate);

        return listItemView;
    }


}