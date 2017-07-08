package com.example.cr.recyclerviewcaching;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.cr.recyclerviewcaching.adapter.CustomAdapter;
import com.example.cr.recyclerviewcaching.app.AppController;
import com.example.cr.recyclerviewcaching.model.Movie;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String url = "http://api.androidhive.info/json/movies.json";
    private ProgressDialog progressDialog;
    private List<Movie> movieList = new ArrayList<Movie>();
    private ListView listView;
    private CustomAdapter customAdapter;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.list);
        customAdapter = new CustomAdapter(this, movieList, getApplicationContext());
        listView.setAdapter(customAdapter);

        progressDialog = new ProgressDialog(this);

        progressDialog.setMessage("Loading.......");
        progressDialog.show();

       // getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1b1b1b")));

        JsonArrayRequest movieReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>()
        {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());
                hideDialog();

                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject obj = response.getJSONObject(i);
                        Movie movie = new Movie();
                        movie.setTitle(obj.getString("title"));

                        movie.setThumbnailUrl(obj.getString("image"));
                        movie.setRating(((Number) obj.get("rating")).doubleValue());
                        movie.setYear(obj.getInt("releaseYear"));

                        JSONArray genreArray = obj.getJSONArray("genre");
                        ArrayList<String> genre = new ArrayList<String>();
                        for (int j = 0; j < genreArray.length(); j++) {
                            genre.add((String) genreArray.get(j));
                        }

                        movie.setGenre(genre);

                        movieList.add(movie);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                customAdapter.notifyDataSetChanged();
            }
            }, new Response.ErrorListener()
            {
                public void onErrorResponse(VolleyError error)
                {
                    VolleyLog.d(TAG, "Error: "+error.getMessage());
                    hideDialog();
                }
        });
        AppController.getInstance().addToRequestQueue(movieReq);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        hideDialog();
    }

    private void hideDialog()
    {
        if(progressDialog != null)
        {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
}
