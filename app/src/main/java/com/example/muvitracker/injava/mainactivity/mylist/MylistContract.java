package com.example.muvitracker.injava.mainactivity.mylist;


import com.example.muvitracker.injava.repo.dto.DetailsDto;

import java.util.List;

public interface MylistContract {

    public interface View {

        public void updateUi (List<DetailsDto> prefsList);



    }


    public interface Presenter {

        public void loadData ();

        public void setList(List<DetailsDto> prefList); // ->fragm ->adapter

        public void toggleFavorite(DetailsDto dto);

    }

}
