package com.example.cr.recyclerviewcaching.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.cr.recyclerviewcaching.R;
import com.example.cr.recyclerviewcaching.app.AppController;
import com.example.cr.recyclerviewcaching.model.Movie;

import java.util.List;

public class CustomAdapter extends BaseAdapter
{
    public Activity activity;
    private LayoutInflater inflator;
    private List<Movie> movieList;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public CustomAdapter(Activity activity, List<Movie> movieList)
    {
        this.activity = activity;
        this.movieList = movieList;
    }

    @Override
    public int getCount()
    {
        return movieList.size();
    }

    @Override
    public Object getItem(int position)
    {
        return movieList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if(inflator == null)
        {
            inflator = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if(convertView == null)
            {
                convertView = inflator.inflate(R.layout.list_row, null);
            }

            if(imageLoader == null)
            {
                imageLoader = AppController.getInstance().getImageLoader();
            }
                NetworkImageView networkImageView = (NetworkImageView) convertView.findViewById(R.id.thumbnail);
                TextView title = (TextView) convertView.findViewById(R.id.title);
                TextView rating = (TextView) convertView.findViewById(R.id.rating);
                TextView genre = (TextView) convertView.findViewById(R.id.genre);
                TextView year = (TextView) convertView.findViewById(R.id.releaseYear);

                Movie m= movieList.get(position);
                networkImageView.setImageUrl(m.getThumbnailUrl(), imageLoader);

                title.setText(m.getTitle());
                rating.setText("Rating: "+String.valueOf(m.getRating()));

                String genreStr = "";

                for(String str : m.getGenre())
                {
                    genreStr +=str + "";
                }

                genreStr = genreStr.length() > 0 ? genreStr.substring(0, genreStr.length() - 2) : genreStr;
                genre.setText(genreStr);

                year.setText(String.valueOf(m.getYear()));
        }
        return convertView;
    }
}
