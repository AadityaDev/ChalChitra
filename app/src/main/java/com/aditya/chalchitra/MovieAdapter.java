package com.aditya.chalchitra;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Size;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.aditya.chalchitra.constants.AppAPI;
import com.aditya.chalchitra.constants.AppConstant;
import com.aditya.chalchitra.models.Movie;
import com.aditya.chalchitra.utils.StringUtils;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHelper>{

    private List<Movie> movies;
    private Context context;
    private Point point;

    public MovieAdapter(Context context,List<Movie> movies,Point point){
        this.context=context;
        this.movies=movies;
        this.point=point;
    }

    @Override
    public MovieViewHelper onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.movie_item,parent,false);
        return new MovieViewHelper(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHelper holder, int position) {
        final Movie movie=movies.get(position);
        if(movie!=null){
//            Bitmap myImage = getBitmapFromURL(AppAPI.IMAGE_URL+movie.getPoster_path());
//            Drawable drawable = new BitmapDrawable(myImage);
//            holder.background.setImageBitmap(myImage);

//            final int horizontal=;
            if(StringUtils.isNotEmptyOrNull(movie.getPoster_path())){
                Picasso.with(context).load(AppAPI.IMAGE_URL+movie.getPoster_path())
                        .resize(point.x,point.x)
                        .into(holder.background);
            }
            if (StringUtils.isNotEmptyOrNull(movie.getTitle())){
                holder.title.setText(movie.getTitle());
            }
            holder.vote.setText("Vote: "+movie.getVote_count());
            holder.popularity.setText("Popularity: "+movie.getPopularity());
            if(StringUtils.isNotEmptyOrNull(movie.getOriginal_language())){
                holder.originalLanguage.setText("Language: "+movie.getOriginal_language());
            }
        }
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    @Override
    public long getItemId(int position) {
        return movies.get(position).getId();
    }

    public Movie getMovie(int position){
        return movies.get(position);
    }

    public Bitmap getBitmapFromURL(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static class MovieViewHelper extends RecyclerView.ViewHolder{

        private ImageView background;
        private TextView title;
        private TextView popularity;
        private TextView vote;
        private TextView originalLanguage;
        private RelativeLayout detail;

        public MovieViewHelper(@NonNull View item){
            super(item);
            background=(ImageView) item.findViewById(R.id.background);
            title=(TextView)item.findViewById(R.id.movie_title);
            popularity=(TextView)item.findViewById(R.id.movie_popularity);
            vote=(TextView)item.findViewById(R.id.movie_vote);
            originalLanguage=(TextView)item.findViewById(R.id.movie_original_language);
        }

    }

}
