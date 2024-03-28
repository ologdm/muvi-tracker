package com.example.muvitracker.injava.repo.dto.search.principali;

import com.example.muvitracker.injava.repo.dto.search.secondarie.Ids;


public class Movie {

    // atttributi (3)
    private String title;
    private int year;
    private Ids ids; // in secondarie


    // Image URL - metodo che prende url da (OMDb id == IMDb id)
    public String getImageUrl() {
        return "http://img.omdbapi.com/" + "?apikey=ef6d3d4c" + "&i=" + ids.getImdb();
    }


    // getters
    public String getTitle() {
        return title;
    }

    public int getYear() {
        return year;
    }

    public Ids getIds() {
        return ids;
    }


    // setters
    public void setTitle(String title) {
        this.title = title;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setIds(Ids ids) {
        this.ids = ids;
    }
}
