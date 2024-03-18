package com.example.muvitracker.mainactivity.java.mylist;


import com.example.muvitracker.repo.java.dto.DetailsDto;

import java.util.List;

public interface PrefsContract {

    public interface View {

        public void updateUi (List<DetailsDto> prefsList);



    }


    public interface Presenter {

        public void loadData ();

        public void setList(List<DetailsDto> prefList); // ->fragm ->adapter

        public void toggleFavorite(DetailsDto dto);

    }

}
