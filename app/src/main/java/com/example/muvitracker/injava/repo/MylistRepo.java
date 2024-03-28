package com.example.muvitracker.injava.repo;


import com.example.muvitracker.injava.repo.dto.DetailsDto;
import java.util.ArrayList;
import java.util.List;

// 1. add list OK
// 2. remove from list OK
// 3. set watched  - aggiorno direttamente tutta la lista da Adapter OK
// 4.


public class MylistRepo {

    // 1. ATTRIBUTI

    // lista cache ram
    List<DetailsDto> prefList = new ArrayList<>();


    // 1.2 singleton
    private static MylistRepo istance;

    private MylistRepo() {
    }

    public static MylistRepo getInstance() {
        if (istance == null) {
            istance = new MylistRepo();
        }
        return istance;
    }


    // 2 LISTA get set
    public void setPrefList(List<DetailsDto> list) {
        // !!! copia lista quando passo su repository
        List<DetailsDto> lista = new ArrayList<>(list);
        prefList = lista;
    }

    public List<DetailsDto> getPrefList() {
        return prefList;
    }


    // 3 METODI ITEM

    // check id
    public int getItemIndex(DetailsDto inputDto) {
        int index = -1;

        for (int i = 0; i < prefList.size(); i++) {
            DetailsDto prefDto = prefList.get(i);
            if (prefDto.getIds().getTrakt() == inputDto.getIds().getTrakt()) {
                index = i;
                break;
            }
        }
        return index;
        // se trova restituisce index, altrimenti -1
    }

    public int getItemIndex(int traktId) {
        int index = -1;

        for (int i = 0; i < prefList.size(); i++) {
            DetailsDto prefDto = prefList.get(i);
            if (prefDto.getIds().getTrakt() == traktId) {
                index = i;
                break;
            }
        }
        return index;
    }



    // remove
    private void removeItemFromList(DetailsDto inputDto) {
        int index = getItemIndex(inputDto); // check se elemento nella lista

        // se in lista -> remove
        if (index != -1) {
            prefList.remove(index);
        }
    }


    // add or edit
    //  add ->  if (no lista) 00->01
    //  edit -> if (in lista) 11->01
    private void addOrEditItemFromList(DetailsDto inputDto) {
        int index = getItemIndex(inputDto);

        if (index != -1) { // nella lista
            prefList.set(index, inputDto);
        } else {           // non nella lista
            prefList.add(inputDto);
        }
    }


    // non serve il check
    public DetailsDto getMovie(int traktId) {
        int index = getItemIndex(traktId);
            return prefList.get(index);
    }


    // 4 LOGICA FAVORITE BUTTON (eugi)
    /* gestisce tutti e 4 gli stati in cui mi ci metto
    // in // out
    // 01->  (00) - remove
    // 01->  (11) - edit
    // 11->(01)<-00 - edit/add
     */


    public boolean toggleFavouriteItem(DetailsDto inputDto) {
        // inverti stato liked + gestisci salvataggio correttamente

        // 1 inverto valore liked sul click (fragm -> pres- > here)
        inputDto.setLiked(!inputDto.isLiked());

        // 2 prendo valori i valori aggiornati
        boolean isLiked = inputDto.isLiked(); // invertito
        boolean isWatched = inputDto.isWatched(); // fisso

        // 3 gestire modifica/rimozione
        // caso 11 - edit
        // 01->11
        if (isLiked && isWatched) {
            // edit
            addOrEditItemFromList(inputDto);

            // caso 00 - remove
            // 01->00
        } else if (!isLiked && !isWatched) {
            // remove
            removeItemFromList(inputDto);

            // caso 01
            // 11-01 || 00->01
        } else {
            // add/edit
            addOrEditItemFromList(inputDto);
        }

        // ritorna stato dto
        return inputDto.isLiked();
    }


    // 5 LOGICA WATCHED CHECKBOX

    // logica:
    // 1 passo lo stato
    // 2 lo aggiorno correttamente (add, remove, edit)
    // boolean non ne ho bisogno

    public void updateWatchedItem(DetailsDto inputDto) {
        // prendo variabili gia settata
        boolean isWatched = inputDto.isWatched();
        boolean isLiked = inputDto.isLiked();

        // gestione 4 stati:
        // 11
        if (isWatched && isLiked) {
            // edit
            addOrEditItemFromList(inputDto);

            // 00
        } else if (!isWatched && !isLiked) {
            // remove
            removeItemFromList(inputDto);

        } else { //  11->01<-00
            // edit or add
            addOrEditItemFromList(inputDto);
        }

        // return - non c'e bisogno
    }


}




