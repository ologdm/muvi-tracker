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


//-> /movies/tron-legacy-2010?extended=full


import androidx.annotation.Nullable;

import com.example.muvitracker.repository.dto.BoxofficeDto;
import com.example.muvitracker.repository.dto.DetailsDto;
import com.example.muvitracker.repository.dto.MovieDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TraktApi {


    @GET("movies/popular")
        // @GET fa riferimento al metodo sotto
    Call<List<MovieDto>> getPopularMovies();

    // metadati
    @GET("movies/boxoffice")
    Call<List<BoxofficeDto>> getBoxofficeMovies();



    @GET("movies/{movie_id}?extended=full")
    Call<DetailsDto> getDetailsDto(
        @Path("movie_id") int movieId
    );



}
