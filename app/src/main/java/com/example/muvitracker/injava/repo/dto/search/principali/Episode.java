package com.example.muvitracker.injava.repo.dto.search.principali;

import com.example.muvitracker.injava.repo.dto.search.secondarie.Ids;


public class Episode {

    // 1. Attributi
    private int season;
    private int number;
    private String title;
    private Ids ids;
    private Show show; // ??? e da mettere


    // 2. getters
    public int getSeason() {
        return season;
    }

    public int getNumber() {
        return number;
    }

    public String getTitle() {
        return title;
    }

    public Ids getIds() {
        return ids;
    }

    public Show getShow() {
        return show;
    }


    // 3. setters
    public void setSeason(int season) {
        this.season = season;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setIds(Ids ids) {
        this.ids = ids;
    }

    public void setShow(Show show) {
        this.show = show;
    }
}
