package com.example.muvitracker.repo.dto.search.secondarie;

public class Ids {

    // attributi (5)
    private int trakt;
    private String slug;
    private String imdb;
    private Integer tvdb; // Usiamo Integer invece di int per consentire null
    private int tmdb;


    // getters
    public int getTrakt() {
        return trakt;
    }

    public String getSlug() {
        return slug;
    }

    public String getImdb() {
        return imdb;
    }

    public Integer getTvdb() {
        return tvdb;
    }

    public int getTmdb() {
        return tmdb;
    }


    // setters
    public void setTrakt(int trakt) {
        this.trakt = trakt;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public void setImdb(String imdb) {
        this.imdb = imdb;
    }

    public void setTvdb(Integer tvdb) {
        this.tvdb = tvdb;
    }

    public void setTmdb(int tmdb) {
        this.tmdb = tmdb;
    }
}
