package com.example.muvitracker.repository;


// serealized serve?

@SuppressWarnings("unused")
public class DataModel {

    // Attributi
    String title;
    int year;
    Ids ids;


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

    public void setTitle(String title) {
        this.title = title;
    }
    public void setYear(int year) {
        this.year = year;
    }
    public void setIds(Ids ids) {
        this.ids = ids;
    }




    // CLasse Nuova
    public static class Ids {
        int trakt;
        String slug;
        String imdb;
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
