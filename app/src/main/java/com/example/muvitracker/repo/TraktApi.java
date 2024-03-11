package com.example.muvitracker.repo;

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


//-> /movies/tron-legacy-2010?extended=full


import com.example.muvitracker.repo.dto.BoxofficeDto;
import com.example.muvitracker.repo.dto.DetailsDto;
import com.example.muvitracker.repo.dto.PopularDto;
import com.example.muvitracker.repo.dto.search.SearchDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

// @GET("movies/boxoffice") - le annotazioni sono metadati


public interface TraktApi {


    //              1°STEP
    @GET("movies/popular")
    // @GET fa riferimento al metodo sotto
    Call<List<PopularDto>> getPopularMovies();


    @GET("movies/boxoffice")
        // !!! sono metadati
    Call<List<BoxofficeDto>> getBoxofficeMovies();


    //               3°STEP
    // path mobile (da mettere nei parametri) - OK
    @GET("movies/{movie_id}?extended=full")
    Call<DetailsDto> getDetailsDto(@Path("movie_id") int movieId);


    //               4°STEP
    // query mobile - OK
    @GET("search/movie,show")
    Call<List<SearchDto>> getSearch(@Query("query") String searchString);


}
