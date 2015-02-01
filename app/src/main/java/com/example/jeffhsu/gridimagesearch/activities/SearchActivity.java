package com.example.jeffhsu.gridimagesearch.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.example.jeffhsu.gridimagesearch.Listeners.EndlessScrollListener;
import com.example.jeffhsu.gridimagesearch.adapters.ImageResultsAdapter;
import com.example.jeffhsu.gridimagesearch.models.ImageResult;
import com.example.jeffhsu.gridimagesearch.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class SearchActivity extends ActionBarActivity {
    private EditText etQuery;
    private GridView gvResults;
    private ArrayList<ImageResult> imageResults;
    private ImageResultsAdapter aImageResults;
    private String[] sizes;
    private String[] colors;
    private String[] types;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        // setting up the views first
        setupViews();
        // Attach the listener to the AdapterView onCreate
        gvResults.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemCount) {
                Log.i("DEBUG","page in onLoadMore "+page);
                Log.i("DEBUG", "totalItemCount in onLoadMore "+totalItemCount);
            // Triggers only when new data needs to be appended to the list
            // Add whatever code is needed to append new items to your AdapterView
                customLoadMoreDataFromApi(page);
            }
        });
        imageResults = new ArrayList<ImageResult>();
        // Attaches the data source to an adapter
        aImageResults = new ImageResultsAdapter(this, imageResults);
        gvResults.setAdapter(aImageResults);
        sizes = getResources().getStringArray(R.array.size);
        colors = getResources().getStringArray(R.array.color);
        types = getResources().getStringArray(R.array.type);

    }

    private void customLoadMoreDataFromApi(int page) {
                Log.i("DEBUG","FIRING API CALL!");
                Log.i("DEBUG","page in api firing call is "+ page);
//        Log.i("DEBUG","Calling onLoad more!");
        String query = etQuery.getText().toString();
//        Toast.makeText(this,query, Toast.LENGTH_LONG).show();
        String searchUrl = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=" + query + "&rsz=8";
        String advOptions = "";
        String startIndex = Integer.toString((page-1)*8);

        SharedPreferences prefs = getSharedPreferences("MyPref",0);
        int size = prefs.getInt("size",0);
        int color = prefs.getInt("color", 0);
        int type = prefs.getInt("type", 0);
        String site = prefs.getString("site", null);

        if (size!=0) {
            advOptions += "&imgsz=" + sizes[size];
        }
        if (color!=0) {
            advOptions += "&imgcolor=" + colors[color];
        }
        if (type!=0) {
            advOptions += "&imgtype=" + types[type];
        }
        if (site!=null && !site.equals("")) {
            advOptions += "&as_sitesearch" + site;
        }

        searchUrl += advOptions;
        searchUrl += "&start=" + startIndex;

        Log.i("DEBUG", "search url is "+searchUrl);
        AsyncHttpClient client = new AsyncHttpClient();

        client.get(searchUrl, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray imageResultsJson = null;
                try{
                    imageResultsJson = response.getJSONObject("responseData").getJSONArray("results");
//                    imageResults.clear();
                    // When you make changes to adapter, it does modify the underlying data
                    aImageResults.addAll(ImageResult.fromJSONArray(imageResultsJson));
                    // do notify or another method
//                    aImageResults.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.i("INFO", imageResults.toString());
            }
        });
    }

    private void setupViews(){
        etQuery = (EditText) findViewById(R.id.etQuery);
        gvResults = (GridView) findViewById(R.id.gvResults);
        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Launch the image display activity
                // Creating an intent
                // this is anonymous class, so we need to specify more clearly (this won't work in intent)
                Intent i = new Intent(SearchActivity.this, ImageDisplayActivity.class);
                // Get the image result to display
                ImageResult result = imageResults.get(position);
                // Pass image result into the intent
                // Need to either be serializable or parceable
                i.putExtra("result", result);
                // Launch the new activity
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.barSetting) {
            Toast.makeText(this, "Clicked!", Toast.LENGTH_LONG).show();
            Intent i = new Intent(this, ImageSearchSetting.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    // Fire whenever the button is pressed (android:onClick)
    public void onImageSearch(View view) {
        String query = etQuery.getText().toString();
        Toast.makeText(this,query, Toast.LENGTH_LONG).show();
        String searchUrl = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=" + query + "&rsz=8";
        String advOptions = "";

        SharedPreferences prefs = getSharedPreferences("MyPref",0);
        int size = prefs.getInt("size",0);
        int color = prefs.getInt("color", 0);
        int type = prefs.getInt("type", 0);
        String site = prefs.getString("site", null);

        if (size!=0) {
            advOptions += "&imgsz=" + sizes[size];
        }
        if (color!=0) {
            advOptions += "&imgcolor=" + colors[color];
        }
        if (type!=0) {
            advOptions += "&imgtype=" + types[type];
        }
        if (site!=null && !site.equals("")) {
            advOptions += "&as_sitesearch" + site;
        }

        searchUrl += advOptions;
        Log.i("DEBUG", "search url is "+searchUrl);
        AsyncHttpClient client = new AsyncHttpClient();

        client.get(searchUrl, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray imageResultsJson = null;
                try{
                    imageResultsJson = response.getJSONObject("responseData").getJSONArray("results");
                    imageResults.clear();
                    // When you make changes to adapter, it does modify the underlying data
                    aImageResults.addAll(ImageResult.fromJSONArray(imageResultsJson));
                    // do notify or another method

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.i("INFO", imageResults.toString());
            }
        });
    }
}
