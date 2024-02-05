package com.example.muvitracker.repository.dto;


// DTO = data transfer object
// serialized serve? - no, solo per passare una classe complessa da un activity fragment ad un altro



/* JSON - Popular (lista standard)
  {
    "title": "The Dark Knight",
    "year": 2008,
    "ids": {
      "trakt": 16,
      "slug": "the-dark-knight-2008",
      "imdb": "tt0468569",
      "tmdb": 155
    }
  },
  */


// Returns the most popular movies.
// Popularity is calculated using the rating percentage and the number of ratings.


@SuppressWarnings("unused")
public class MovieDto {

    // Attributi
    String title;
    int year;
    Ids ids; // null

    // Get, Set
    public String getTitle() {
        return title;
    }
    public int getYear() {
        return year;
    }
    public Ids getIds() {
        return ids;
    }


    // Image URL - metodo che prende url da (OMDb id == IMDb id)
    public String getImageUrl() {
        return "http://img.omdbapi.com/" +"?apikey=ef6d3d4c" + "&i=" + ids.getImdb();
    }



    public void setTitle(String title) {
        this.title = title;
    }
    public void setYear(int year) {
        this.year = year;
    }
    public void setIds(Ids ids) {
        this.ids = ids;
    }



    // Inner Class 'Ids'
    public static class Ids {
        int trakt;
        String slug;
        String imdb; // utilizzata per immagini
        int tmdb;


        public int getTrakt() {
            return trakt;
        }
        public String getSlug() {
            return slug;
        }
        public String getImdb() {
            return imdb;
        }
        public int getTmdb() {
            return tmdb;
        }


        public void setTrakt(int trakt) {
            this.trakt = trakt;
        }
        public void setSlug(String slug) {
            this.slug = slug;
        }
        public void setImdb(String imdb) {
            this.imdb = imdb;
        }
        public void setTmdb(int tmdb) {
            this.tmdb = tmdb;
        }
    }



}
