package com.example.muvitracker.repo.java.dto;


// JSON Details - Trakt Summary -> https://trakt.docs.apiary.io/#reference/sync/update-favorite-item/get-a-movie

// API GET:  https://private-anon-9505e163f3-trakt.apiary-mock.com/movies/ (id)
// (id) --> Trakt ID, Trakt , or IMDB ID Example: tron-legacy-2010.

// ** titolo: TRON: Legacy /-> slug - tron-legacy-2010

// #######################################################################
/* /movies/tron-legacy-2010
{
  "title": "TRON: Legacy",
  "year": 2010,
  "ids": {
    "trakt": 1,
    "slug": "tron-legacy-2010",
    "imdb": "tt1104001",
    "tmdb": 20526
  }
}
 */


// #######################################################################
/* /movies/tron-legacy-2010?extended=full
{
    "title": "TRON: Legacy",
    "year": 2010,
    "ids": {
        "trakt": 343,
        "slug": "tron-legacy-2010",
        "imdb": "tt1104001",
        "tmdb": 20526
    },
    "tagline": "The Game Has Changed.",
    "overview": "Sam Flynn, the tech-savvy and daring son of Kevin Flynn, investigates his father's disappearance and is pulled into The Grid. With the help of  a mysterious program named Quorra, Sam quests to stop evil dictator Clu from crossing into the real world.",
    "released": "2010-12-16",
    "runtime": 125,
    "country": "us",
    "updated_at": "2014-07-23T03:21:46.000Z",
    "trailer": null,
    "homepage": "http://disney.go.com/tron/",
    "status": "released",
    "rating": 8,
    "votes": 111,
    "comment_count": 92,
    "language": "en",
    "available_translations": [
        "en"
    ],
    "genres": [
        "action"
    ],
    "certification": "PG-13"
}
 */


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

// utilizzato in Details e Prefs

public class DetailsDto implements Serializable {

    // *watched status - parametro di prefList
    private boolean watched;
    private boolean liked;

    // JSON
    // movie
    private String title = "";
    private int year;
    private Ids ids;


    // altro
    private String tagline;
    private String overview;
    private String released;
    private int runtime;
    private String country;

    @SerializedName("updated_at")
    private String updatedAt;

    //trailer = null
    private String homepage;
    private String status;
    private float rating;
    private int votes;

    @SerializedName("comment_count")
    private int commentCount;

    private String language;
    private List<String> availableTranslations;
    private List<String> genres;
    private String certification;


    // ImageUrl - metodo che prende url da (OMDb id == IMDb id)
    // == movieDto
    public String getImageUrl() {
        return "http://img.omdbapi.com/" +"?apikey=ef6d3d4c" + "&i=" + ids.getImdb();
    }


    // SETTERS


    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public void setWatched(boolean watched) {
        this.watched = watched;
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

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }
    public void setOverview(String overview) {
        this.overview = overview;
    }
    public void setReleased(String released) {
        this.released = released;
    }
    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }
    public void setCountry(String country) {
        this.country = country;
    }
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public void setRating(float rating) {
        this.rating = rating;
    }
    public void setVotes(int votes) {
        this.votes = votes;
    }
    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }
    public void setLanguage(String language) {
        this.language = language;
    }
    public void setAvailableTranslations(List<String> availableTranslations) {
        this.availableTranslations = availableTranslations;
    }
    public void setGenres(List<String> genres) {
        this.genres = genres;
    }
    public void setCertification(String certification) {
        this.certification = certification;
    }


    // GETTERS


    public boolean isLiked() {
        return liked;
    }

    public boolean isWatched() {
        return watched;
    }

    public String getTitle() {
        return title;
    }
    public int getYear() {
        return year;
    }
    public Ids getIds() {
        return ids;
    }

    public String getTagline() {
        return tagline;
    }
    public String getOverview() {
        return overview;
    }
    public String getReleased() {
        return released;
    }
    public int getRuntime() {
        return runtime;
    }
    public String getCountry() {
        return country;
    }
    public String getUpdatedAt() {
        return updatedAt;
    }
    public String getHomepage() {
        return homepage;
    }
    public String getStatus() {
        return status;
    }
    public float getRating() {
        return rating;
    }
    public int getVotes() {
        return votes;
    }
    public int getCommentCount() {
        return commentCount;
    }
    public String getLanguage() {
        return language;
    }
    public List<String> getAvailableTranslations() {
        return availableTranslations;
    }
    public List<String> getGenres() {
        return genres;
    }
    public String getCertification() {
        return certification;
    }


    // Inner Class 'Ids'
    public static class Ids {
        int trakt;
        String slug;
        String imdb = "x"; // utilizzata per immagini
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
