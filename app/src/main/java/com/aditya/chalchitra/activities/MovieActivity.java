package com.aditya.chalchitra.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.aditya.chalchitra.R;
import com.aditya.chalchitra.base.BaseAppCompat;
import com.aditya.chalchitra.concurrency.ExecutorUtils;
import com.aditya.chalchitra.constants.AppAPI;
import com.aditya.chalchitra.models.Genres;
import com.aditya.chalchitra.models.Movie;
import com.aditya.chalchitra.models.ProductionCompanies;
import com.aditya.chalchitra.models.ProductionCountries;
import com.aditya.chalchitra.models.Video;
import com.aditya.chalchitra.network.Factory;
import com.aditya.chalchitra.uicomponents.ScrollViewX;
import com.aditya.chalchitra.utils.ExceptionUtils;
import com.aditya.chalchitra.utils.StringUtils;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MovieActivity extends BaseAppCompat {

    private ActionBar actionBar;
    private Bundle bundle;
    private Movie movie;
    private ProgressBar progressBar;
    private VideoView videoView;
    private ImageView poster;
    private TextView name;
    private TextView popularity;
    private TextView date;
    private TextView overview;
    private TextView languages;
    private CardView seeVideo;
    private CardView comment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        progressBar=(ProgressBar)findViewById(R.id.progress_bar);
        progressBar.setIndeterminate(true);
        poster=(ImageView)findViewById(R.id.movie_poster);
        name=(TextView)findViewById(R.id.movie_name);
        popularity=(TextView)findViewById(R.id.movie_popularity);
        date=(TextView)findViewById(R.id.movie_date);
        overview=(TextView)findViewById(R.id.movie_overview);
        seeVideo=(CardView)findViewById(R.id.card_view);
        seeVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getURL();
            }
        });
        comment=(CardView)findViewById(R.id.comment);
        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openComment=new Intent(getApplicationContext(),CommentActivity.class);
                startActivity(openComment);
            }
        });

        bundle=getIntent().getExtras();
        movie=bundle.getParcelable(AppAPI.MOVIE);
        getMovieDetail();

    }

    private void getMovieDetail(){
        final ListenableFuture<JSONObject> movieDetail=Factory.getMoviesService().getMovieDetail(movie.getId()+"");
        Futures.addCallback(movieDetail, new FutureCallback<JSONObject>() {
            @Override
            public void onSuccess(@NonNull JSONObject result) {
                try {
                    Gson gson = new Gson();
                    movie = gson.fromJson(result.toString(), Movie.class);
                    if (!result.isNull(AppAPI.BUDGET)) {
                        movie.setBudget(result.getLong(AppAPI.BUDGET));
                    }
                    if (!result.isNull(AppAPI.GENRES)) {
                        JSONArray array=new JSONArray(result.getJSONArray(AppAPI.GENRES).toString());
                        List<Genres> genresList=new ArrayList<Genres>();
                        for(int i=0;i<array.length();i++){
                            Genres genres=gson.fromJson(array.get(i).toString(),Genres.class);
                            genresList.add(genres);
                        }
                        movie.setGenres(genresList);
                    }
                    if (!result.isNull(AppAPI.PRODUCTIONCOMPANIES)) {
                        JSONArray array=new JSONArray(result.getJSONArray(AppAPI.PRODUCTIONCOMPANIES).toString());
                        List<ProductionCompanies> productionCompaniesList=new ArrayList<ProductionCompanies>();
                        for(int i=0;i<array.length();i++){
                            ProductionCompanies productionCompanies=gson.fromJson(array.get(i).toString(),ProductionCompanies.class);
                            productionCompaniesList.add(productionCompanies);
                        }
                        movie.setProduction_companies(productionCompaniesList);
                    }
                    if (!result.isNull(AppAPI.PRODUCTIONCOUNTRIES)) {
                        JSONArray array=new JSONArray(result.getJSONArray(AppAPI.PRODUCTIONCOUNTRIES).toString());
                        List<ProductionCountries> productionCountriesList=new ArrayList<ProductionCountries>();
                        for(int i=0;i<array.length();i++){
                            ProductionCountries productionCountries=gson.fromJson(array.get(i).toString(),ProductionCountries.class);
                            productionCountriesList.add(productionCountries);
                        }
                        movie.setProduction_countries(productionCountriesList);
                    }
                    if (!result.isNull(AppAPI.HOMEPAGE)) {
                        movie.setHomepage(result.getString(AppAPI.HOMEPAGE));
                    }
                    if (!result.isNull(AppAPI.IMDBID)) {
                        movie.setImdb_id(result.getString(AppAPI.IMDBID));
                    }
                    if (!result.isNull(AppAPI.REVENUE)) {
                        movie.setRevenue(result.getLong(AppAPI.REVENUE));
                    }
                    if (!result.isNull(AppAPI.RUNTIME)) {
                        movie.setRuntime(result.getLong(AppAPI.RUNTIME));
                    }
                    if (!result.isNull(AppAPI.STATUS)) {
                        movie.setStatus(result.getString(AppAPI.STATUS));
                    }
                    if (!result.isNull(AppAPI.TAGLINE)) {
                        movie.setTagline(result.getString(AppAPI.TAGLINE));
                    }
                    if(movie!=null){
                        fillData(movie);
                        progressBar.setVisibility(View.GONE);
                        Log.d(getTAG(), movie.toString());
                    }
                }catch (JSONException jsonException){
                    progressBar.setVisibility(View.GONE);
                    ExceptionUtils.exceptionMessage(jsonException,getTAG());
                }catch (Exception exception){
                    progressBar.setVisibility(View.GONE);
                    ExceptionUtils.exceptionMessage(exception,getTAG());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                ExceptionUtils.throwableMessage(t,getTAG());
            }
        }, ExecutorUtils.getUIThread());
    }

    private void fillData(@NonNull Movie movie){
        if(StringUtils.isNotEmptyOrNull(movie.getTitle())){
            name.setText(movie.getTitle());
        }
        if(StringUtils.isNotEmptyOrNull(movie.getPoster_path())){
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
                Picasso.with(getApplicationContext()).load(AppAPI.IMAGE_URL+movie.getPoster_path())
                        .resize(size.x,size.x)
                        .into(poster);
        }
        if(StringUtils.isNotEmptyOrNull(movie.getRelease_date())){
            date.setText(movie.getRelease_date()+"   "+movie.getStatus());
        }
        popularity.setText(movie.getVote_average()+"/10 "+"  By "+movie.getVote_count()+" people ");
        if(StringUtils.isNotEmptyOrNull(movie.getOverview())){
            overview.setText(movie.getOverview());
        }
    }

    private void getURL(){
        ListenableFuture<JSONObject> getMovieKey= Factory.getMoviesService().getVideoUrl(movie.getId()+"");
        Futures.addCallback(getMovieKey, new FutureCallback<JSONObject>() {
            @Override
            public void onSuccess(@NonNull JSONObject result) {
                Log.d(getTAG(), "OnSuccess: " + result.toString());
                try {
                    JSONArray results=new JSONArray();
                    if(!result.isNull(AppAPI.RESULTS)){
                        results=new JSONArray(result.getJSONArray(AppAPI.RESULTS).toString());
                        Gson gson=new Gson();
                        if(!results.isNull(0)){
                            Video video=gson.fromJson(results.get(0).toString(),Video.class);
                            Log.d(getTAG(),"URL: "+AppAPI.VIDEO_URL+video.getKey());
                            if((video!=null)&&(StringUtils.isNotEmptyOrNull(video.getKey()))){
                            Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(AppAPI.VIDEO_URL+video.getKey().toString()));
                            startActivity(intent);
                            }else{
                                Toast.makeText(getApplicationContext(),"URL not found!!",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                } catch (Exception exception){
                    ExceptionUtils.exceptionMessage(exception,getTAG());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                ExceptionUtils.throwableMessage(t,getTAG());
            }
        },ExecutorUtils.getUIThread());
    }


}
