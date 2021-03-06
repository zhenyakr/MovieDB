package com.example.moviedb.retrofit;

import com.example.moviedb.model.MovieResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("discover/movie?page=")
    Call<MovieResponse> getPopularyMovies(@Query("page")Integer numberPage,@Query("api_key") String apiKey);

    @GET("movie/top_rated?page=")
    Call<MovieResponse> getTopRatedMovies(@Query("page")Integer numberPage, @Query("api_key") String apiKey);

    @GET("movie/upcoming?page=")
    Call<MovieResponse> getUpcomingMovies(@Query("page") Integer numberPage, @Query("api_key") String apiKey);

}
