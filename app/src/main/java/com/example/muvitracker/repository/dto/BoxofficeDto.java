package com.example.muvitracker.repository.dto;


// revenue + datamodelmovie

@SuppressWarnings("unused")
public class BoxofficeDto { // DTO = data transfer object

    // Attributi
    int revenue;
    MovieDto movieDto;


    // get + set
    public int getRevenue() {
        return revenue;
    }
    public MovieDto getDataModelMovie() {
        return movieDto;
    }

    public void setRevenue(int revenue) {
        this.revenue = revenue;
    }
    public void setDataModelMovie(MovieDto movieDto) {
        this.movieDto = movieDto;
    }
}
