package com.example.muvitracker.mainactivity.mylist;

import androidx.annotation.Nullable;

import com.example.muvitracker.repository.dto.DetailsDto;


public class MylistDto {

    // 1. ATTRIBUTI
    private boolean watched; // valore default false
    private DetailsDto detailsDto;


    // 2. COSTRUTTORE
    public MylistDto(DetailsDto detailsDto) {
        this.detailsDto = detailsDto;
    }


    // 3. GETTER E SETTER
    public boolean isWatched() {
        return watched;
    }

    public void setWatched(boolean watched) {
        this.watched = watched;
    }


    @Nullable
    public DetailsDto getDetailsDto() {
        return detailsDto;
    }

    public void setDetailsDto(DetailsDto detailsDto) {
        this.detailsDto = detailsDto;
    }
}
