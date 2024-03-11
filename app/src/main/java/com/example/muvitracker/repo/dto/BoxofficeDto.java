package com.example.muvitracker.repo.dto;


// revenue + datamodelmovie



/* JSON Boxoffice
  {
    "revenue": 48464322,
    "movie": {
      "title": "Hotel Transylvania 2",
      "year": 2015,
      "ids": {
        "trakt": 103449,
        "slug": "hotel-transylvania-2-2015",
        "imdb": "tt2510894",
        "tmdb": 159824
      }
    }
  },
 */

// Returns the top 10 grossing movies in the U.S. box office last weekend. (10 con piu incassi)
// Updated every Monday morning.


@SuppressWarnings("unused")
public class BoxofficeDto {

    // Attributi
    int revenue;  // specifico boxoffice (incassi)
    PopularDto movie; // dati standard


    // Image URL - metodo che prende url da (OMDb id == IMDb id)
    public String getUrlImage (){
        return "http://img.omdbapi.com/" +"?apikey=ef6d3d4c" + "&i=" + movie.getIds().getImdb();

    }


    // Getters
    public int getRevenue() {
        return revenue;
    }
    public PopularDto getMovie() {
        return movie;
    }


    // Setters - servono per creazione Gson da JSON
    public void setRevenue(int revenue) {
        this.revenue = revenue;
    }
    public void setMovie(PopularDto movie) {
        this.movie = movie;
    }


}