package com.example.muvitracker.repository;

// *** https://trakt.docs.apiary.io/#introduction/create-an-app

// API
// production -> https://api.trakt.tv/
// stagging -> https://api-staging.trakt.tv/ - ????
// mocky -> https://private-anon-5a4b7269e2-trakt.apiary-mock.com/ + seconda parte


// PRODUCTION
// GET https://api.trakt.tv/movies/popular
// GET https://api.trakt.tv/movies/trending - Returns all movies being watched right now. Movies with the most users are returned first.
// GET https://api.trakt.tv/movies/boxoffice - Returns the most popular movies. Popularity is calculated using the rating percentage and the number of ratings.
// BoxOffice  - Il botteghino del cinema
// GET https://api.trakt.tv/movies/id - Returns a single movie's details.
// GET https://api.trakt.tv/movies/id/aliases - nomi in altre lingue



import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface TraktApi {

    // https://api.trakt.tv/

    @GET("movies/popular") // @GET fa riferimento al metodo sotto
    Call<List<DataModel>> getPopularMovies();

    @GET("movies/trending")
    Call<List<DataModel>> getTrendingMovies();

    @GET("movies/boxoffice")
    Call<List<DataModel>> getBoxOfficeMovies();



}
