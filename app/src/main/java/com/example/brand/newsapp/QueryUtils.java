package com.example.brand.newsapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public final class QueryUtils {
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();
    private QueryUtils() {
    }
    public static List<FakeNews> fetchFakeNewsData(String requestUrl) {
        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Unable to make the HTTP connection to get your Fake News", e);
        }
        List<FakeNews> FakeNewsfeeds = extractFeatureFromJson(jsonResponse);
        return FakeNewsfeeds;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Unable to get URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(R.string.read_timeout);
            urlConnection.setConnectTimeout(R.string.connect_timeout);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Unable to get JSON data.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }


    private static List<FakeNews> extractFeatureFromJson(String FakeNewsJSON) {
        if (TextUtils.isEmpty(FakeNewsJSON)) {
            return null;
        }

        List<FakeNews> FakeNewsObject = new ArrayList<>();

        try {

            JSONObject baseJsonResponse = new JSONObject(FakeNewsJSON);

            JSONObject response = baseJsonResponse.getJSONObject("response");

            JSONArray NewsfeedArray = response.getJSONArray("results");
            for (int i = 0; i < NewsfeedArray.length(); i++) {
                JSONObject currentNewsfeed = NewsfeedArray.getJSONObject(i);
                String section = currentNewsfeed.getString("sectionName");
                String title = currentNewsfeed.getString("webTitle");
                JSONArray tags = currentNewsfeed.getJSONArray("tags");

                String authorName = null;
                String authorSurname = null;
                for (int j = 0; j < tags.length(); j++) {
                    JSONObject currentAuthor = tags.getJSONObject(j);
                    if (currentAuthor.has("firstName")) {
                        authorName = currentAuthor.getString("firstName");
                    }
                    if (currentAuthor.has("lastName")) {
                        authorSurname = currentAuthor.getString("lastName");
                    }
                }

                String time = currentNewsfeed.getString("webPublicationDate");
                String url = currentNewsfeed.getString("webUrl");
                FakeNews Newsfeed = new FakeNews(section, title, authorName + " "+ authorSurname, time, url);
                FakeNewsObject.add(Newsfeed);
            }

        } catch (JSONException e) {
            Log.e("QueryUtils", "Unable to parse JSON results.", e);
        }

        return FakeNewsObject;
    }
}