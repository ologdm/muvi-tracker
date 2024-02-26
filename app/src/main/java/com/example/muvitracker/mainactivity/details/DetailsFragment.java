package com.example.muvitracker.mainactivity.details;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.muvitracker.R;
import com.example.muvitracker.repository.dto.DetailsDto;

//      3°STEP-> inizio
// 1. Details film si apre grazie al IdFilm,
// 2. Poi cerca sul DB necessario i dati da mostrare
// 3. Scroll view
// 4. Aggiorna caselle view solo quando ho i dati
// 5. back Button

//      5°STEP - aggiungi elemento a Mylist
// 1. add /remove OK
// 2. checkId in presenter e prefRepo OK
// 3 gestione cambio icona ->
//               1.apertura  Details OK
//               2. setClick OK
// 4 aggiungere watched checkbox
//              (oppure disegno diverso)


// TODO
//      1 -> metodi contract - mettere caricamento prima di apertura fragment -> su presenter
//      2 -> arrotondare valori rating(float)
//      3 -> cache-ing elementi aperti su ram (poi room)


// -> ScrollView non scrolla!!! -> debug -> non devo vincolarla in lunghezza OK


public class DetailsFragment extends Fragment implements DetailsContract.View {


    // 1. ATTRIBUTI

    // 1.1
    private int traktMovieId;

    // 1.2 Views
    private TextView title;
    private ImageView image;
    private TextView released;
    private TextView runtimeFilm;
    private TextView country;
    private TextView rating;
    private TextView overview;
    private ImageButton buttonBack;


    //  AddIcon + Checkbox  (5°STEP)
    ImageButton addIcon; // OK
    private CheckBox watchedCheckbox;


    // 1.3
    // ??? perchè final
    private final DetailsPresenter presenter = new DetailsPresenter(this);
    //MainNavigator navigator = new MainNavigator();




    // 2. COSTRUTTORE

    // 2.1 Costruttore privato -> perché dobbiamo sempre avere un Id
    private DetailsFragment() {
    }

    // 2.2 Factory
    //     crea Fragment con Id - SetArguments -> usare
    public static DetailsFragment create(int traktId) {
        DetailsFragment detailsFragment = new DetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("key_id", traktId);
        detailsFragment.setArguments(bundle);
        return detailsFragment;
    }


    // 3. FUNZIONI FRAGMENT

    // 3.1 Crea
    @Nullable
    @Override
    public View onCreateView(
        @NonNull LayoutInflater inflater,
        @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_details, container, false);
    }


    // 3.2 Logica
    @Override
    public void onViewCreated(
        @NonNull View view,
        @Nullable Bundle savedInstanceState
    ) {

        Drawable FAVORITE_DRAW = getContext().getDrawable(R.drawable.baseline_favorite_24);
        Drawable FAVORITE_BORDER_DRAW = getContext().getDrawable(R.drawable.baseline_favorite_border_24);


        // Assegnazione
        title = view.findViewById(R.id.titoloTitle);  //string
        image = view.findViewById(R.id.imageFilm); // glide
        released = view.findViewById(R.id.uscitaReleased); // string
        runtimeFilm = view.findViewById(R.id.durataRuntime); // int
        country = view.findViewById(R.id.paeseCountry); // string
        rating = view.findViewById(R.id.rating); // float
        overview = view.findViewById(R.id.descrizioneOverview); // string
        buttonBack = view.findViewById(R.id.buttonBack);

        // addIcon + watched
        addIcon = view.findViewById(R.id.addToMylistButton);

        watchedCheckbox = view.findViewById(R.id.checkBox);


        // Get Arguments - assegnazione Id
        Bundle bundle = getArguments();
        if (bundle != null) {
            traktMovieId = bundle.getInt("key_id");
        }

        //   TODO (5°STEP) - 1. addButton
        //  - gestione addIcon drawable status
        // 1. checkId
        boolean isIdIntoPref = presenter.checkMovieId(traktMovieId);
        // 2.
        if (isIdIntoPref){
            addIcon.setImageDrawable(FAVORITE_DRAW); // icona piena
        } else {
            addIcon.setImageDrawable(FAVORITE_BORDER_DRAW); // icona vuota
        }


        //  click addIcon
        addIcon.setOnClickListener(v -> {

            if (!isIdIntoPref) {
                presenter.addItem();
                addIcon.setImageDrawable(FAVORITE_DRAW); // cambia in piena
            } else {
                presenter.removeItem(traktMovieId);
                addIcon.setImageDrawable(FAVORITE_BORDER_DRAW); // cambia in vuota
            }

        });



        // TODO (5°STEP) - 2. click watched

        // visibilità checkbox + scritta -> appari se premi like

        //
        watchedCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // funzione contiene set e ri-aggiornamento da repo
            presenter.setWatchedStatus(isChecked);
        });






        // 3. BackButton
        buttonBack.setOnClickListener(v -> {
            // torna indietro nello stack
            // !!! metodo dell'activity (meglio consiglio eugi)
            requireActivity().onBackPressed();
        });


        // Load Data ( da cache o server) OK
        presenter.getMovie(traktMovieId);
    }


    // 4. METODO CONTRACT

    // 4.1 Aggiorna Dto e Views
    // !!! devo eseguire .setText() solo quando ho la risposta dal server
    @Override
    public void updateUi(DetailsDto detailsDto) {

        title.setText(detailsDto.getTitle());
        released.setText(detailsDto.getReleased());
        // ??? come fare +"min" TODO 2
        runtimeFilm.setText(String.valueOf(detailsDto.getRuntime()) + " min");
        country.setText(detailsDto.getCountry());
        // ??? come fare +"min" TODO 2
        rating.setText(String.valueOf(detailsDto.getRating()) + " stars");
        overview.setText(detailsDto.getOverview());

        //4.3 OK
        Glide.with(getContext())
            .load(detailsDto.getImageUrl())  //Url
            .into(image);

    }


    // TODO 1

}





