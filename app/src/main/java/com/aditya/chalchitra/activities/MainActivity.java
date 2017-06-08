package com.aditya.chalchitra.activities;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ProgressBar;

import com.aditya.chalchitra.MovieAdapter;
import com.aditya.chalchitra.R;
import com.aditya.chalchitra.base.BaseAppCompat;
import com.aditya.chalchitra.concurrency.ExecutorUtils;
import com.aditya.chalchitra.constants.AppAPI;
import com.aditya.chalchitra.models.Movie;
import com.aditya.chalchitra.network.Factory;
import com.aditya.chalchitra.uicomponents.ItemClickSupport;
import com.aditya.chalchitra.utils.ExceptionUtils;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseAppCompat {

    private MovieAdapter movieAdapter;
    private List<Movie> movies;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        recyclerView=(RecyclerView)findViewById(R.id.movies);
        linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        movies=new ArrayList<>();
        movieAdapter=new MovieAdapter(getApplicationContext(),movies,size);
        getMovies();
        recyclerView.setAdapter(movieAdapter);
        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Bundle bundle=new Bundle();
                bundle.putParcelable(AppAPI.MOVIE,movies.get(position));
                Intent intent=new Intent(getApplicationContext(),MovieActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        progressBar=(ProgressBar)findViewById(R.id.progress_bar);
        progressBar.setIndeterminate(true);

    }

    public void getMovies(){
        ListenableFuture<JSONArray> getMovies= Factory.getMoviesService().getMovies();
        Futures.addCallback(getMovies, new FutureCallback<JSONArray>() {
            @Override
            public void onSuccess(@NonNull JSONArray result) {
                try{
                Log.d(getTAG(), "OnSuccess: " + result.toString());
                for(int i=0;i<result.length();i++){
                    Gson gson=new Gson();
                    final Movie movie=gson.fromJson(result.get(i).toString(),Movie.class);
                    movies.add(movie);
                }
                }catch (JSONException jsonException){
                    ExceptionUtils.exceptionMessage(jsonException,getTAG());
                }catch (Exception exception){
                    ExceptionUtils.exceptionMessage(exception,getTAG());
                }
                movieAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d(getTAG(),t.getMessage());
                progressBar.setVisibility(View.GONE);
            }
        }, ExecutorUtils.getUIThread());
    }
}
