package com.example.muvitracker.mainactivity.mylist;

import com.example.muvitracker.repository.dto.DetailsDto;

import java.util.ArrayList;
import java.util.List;

// 1. add list OK
// 2. delete from list
// 3. TODO set watched OK


public class MylistRepository {


    // 1. ATTRIBUTI

    // 1.1 Lista salvata - Cache RAM
    List<MylistDto> prefList = new ArrayList<>();


    // 1.2 Singleton
    private static MylistRepository istance;
    private MylistRepository() {
    }
    public static MylistRepository getInstance() {
        if (istance == null) {
            istance = new MylistRepository();
        }
        return istance;
    }



    // 2. METODI -> DA DETAILS FRAGMENT (add,remove)

    // 2.1 Aggiungi nuovo elemento lista - like click
    public void addToPref(DetailsDto detailsDto) {
        MylistDto dto = new MylistDto(detailsDto);
        prefList.add(dto);
    }


    // 2.2 Elimina dalla lista - like click
    public void removeFromPref(int traktId) {

        prefList.remove(traktId);
    }



    // 3. METODI -> DA MYLIST
    // 3.1  SetList (per lista modificata da adapter)
    public void setPrefList(List<MylistDto> list) {
        prefList = list;
    }



    // 4. ALTRI
    // 4.1 Getter
    public List<MylistDto> getPrefList() {
        return prefList;
    }



}







/*
    public void aggiungiElementiPerTest(){

        DetailsDto detailsDto = new DetailsDto();
        detailsDto.setTitle("Ciao Dima");
        detailsDto.setIds(new DetailsDto.Ids());
        myPrefList.add(new MylistDto(detailsDto));
        myPrefList.add(new MylistDto(detailsDto));
    }

 */

