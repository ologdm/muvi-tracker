package com.example.muvitracker.mainactivity.mylist;

import com.example.muvitracker.repository.dto.DetailsDto;

import java.util.ArrayList;
import java.util.List;

// 1. add list OK
// 2. remove from list OK
// 3. set watched  - aggiorno direttamente tutta la lista da Adapter OK
// 4.


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

    // 2.1 Aggiungi nuovo dto
    public void addToPref(DetailsDto detailsDto) {
        MylistDto dto = new MylistDto(detailsDto);
        prefList.add(dto);
    }


    // 2.2 Elimina dto dalla lista
    // devo fare il check dell'id
    public void removeFromPreflist(int traktId) {

        int intputId = traktId;

        for (int i = 0; i < prefList.size(); i++) {

            MylistDto dto = prefList.get(i);
            int listId = dto.getDetailsDto().getIds().getTrakt();

            if (intputId == listId) {
                prefList.remove(i);
                // break; se non metto break gli elimina tutti se si ripetono
            }
        }
    }


    // LISTA
    // Setter
    // - per lista modificata da adapter
    public void setPrefList(List<MylistDto> list) {
        prefList = list;
    }


    // Getter
    // per tutto
    public List<MylistDto> getPrefList() {
        return prefList;
    }


    // MOVIE
    // restituisci film
    // - usato in details
    public MylistDto getMovie(int traktId) {
        int inputId = traktId;

        for (int i = 0; i < prefList.size(); i++) {
            MylistDto prefDto = prefList.get(i);
            int prefsId = prefDto.getDetailsDto().getIds().getTrakt();
            if (inputId == prefsId) {
                return prefDto; // dto completo
            }
        }
        return null; // ritorno null se non trovo niente
    }


    // TODO utilizzare in details
    public void setMovie(MylistDto inputPrefDto) {
        // id elemento
        int inputId = inputPrefDto.getDetailsDto().getIds().getTrakt();

        // trovo elemento for + setto
        for (int i = 0; i < prefList.size(); i++) {
            MylistDto prefDto = prefList.get(i);
            int prefsId = prefDto.getDetailsDto().getIds().getTrakt();
            // se id item corrispondem setto !!!
            if (inputId == prefsId) {
                prefList.set(i, inputPrefDto);
            }
        }

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

