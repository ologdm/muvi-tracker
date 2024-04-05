package com.example.muvitracker.injava.mainactivity.details;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.muvitracker.R;
import com.example.muvitracker.injava.repo.dto.DetailsDto;

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

    private final DetailsPresenter presenter = new DetailsPresenter(this);
    ImageButton likeButton;
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
    private CheckBox watchedCheckbox;


    // 2. COSTRUTTORE

    // 2.1 Costruttore privato -> perché dobbiamo sempre avere un Id
    private DetailsFragment() {
    }

    // 2.2 Factory
    // crea Fragment con Id - SetArguments -> usare
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
        @Nullable Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragm_deta, container, false);
    }


    // 3.2 Logica
    @Override
    public void onViewCreated(
        @NonNull View view,
        @Nullable Bundle savedInstanceState
    ) {


        // assegnazione
        title = view.findViewById(R.id.title);  //string
        image = view.findViewById(R.id.imageVertical); // glide
        released = view.findViewById(R.id.released); // string
        runtimeFilm = view.findViewById(R.id.runtime); // int
        country = view.findViewById(R.id.country); // string
        rating = view.findViewById(R.id.rating); // float
        overview = view.findViewById(R.id.overview); // string
        buttonBack = view.findViewById(R.id.buttonBack);

        likeButton = view.findViewById(R.id.likedButton);
        watchedCheckbox = view.findViewById(R.id.watchedCkbox);


        // get arguments - assegnazione id
        Bundle bundle = getArguments();
        if (bundle != null) {
            traktMovieId = bundle.getInt("key_id");
        }

        // load data ( da cache o server)
        presenter.getMovie(traktMovieId);


        // back
        buttonBack.setOnClickListener(v -> {
            // torna indietro nello stack
            // !!! metodo dell'activity (meglio consiglio eugi)
            requireActivity().onBackPressed();
        });




        // (eugi)
        //  click like icon
        likeButton.setOnClickListener(v -> {
            presenter.toggleFavourite();
        });


        // TODO (5°STEP) - 2. click watched
        // visibilità checkbox + scritta -> appari se premi like
        watchedCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // funzione contiene set
            // e ri-aggiornamento da repo (non serve)
            presenter.updateWatched(isChecked);
        });
    }


    // 4. METODO CONTRACT - aggiornamento Ui

    // 4.1 aggiorna views
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

        updateFavoriteIcon(detailsDto.isLiked());
        updateWatchedCheckbox(detailsDto.isWatched());
    }


    // cambia l'icona in base allo state preferito dal presenter
    @Override
    public void updateFavoriteIcon(boolean isFavourite) {
        // !!! CAPITAL_CASE - solo per variabile 'final static', qua c'e context che e dell'istanza
        final Drawable iconFilled = getContext().getDrawable(R.drawable.baseline_favorite_24);
        final Drawable iconEmpty = getContext().getDrawable(R.drawable.baseline_favorite_border_24);
        if (isFavourite) {
            likeButton.setImageDrawable(iconFilled); // icona piena
        } else {
            likeButton.setImageDrawable(iconEmpty); // icona vuota
        }
    }


    // lo uso per
    @Override
    public void updateWatchedCheckbox(boolean isWatched) {
        watchedCheckbox.setChecked(isWatched);
    }

}





