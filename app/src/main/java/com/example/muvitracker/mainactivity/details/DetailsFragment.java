package com.example.muvitracker.mainactivity.details;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
// 1. add /remove
// 2. check element in prefList
// 3.  TODO cambia icona (vuota,piena) in base a presenza elemento nella mia lista
// -> metodo updateDto() chiamato su presenter -> eliminato OK
// -> ScrollView non scrolla!!! -> debug -> non devo vincolarla in lunghezza


// TODO
//      1 -> metodi contract - mettere caricamento prima di apertura fragment -> su presenter
//      2 -> arrotondare valori rating(float)
//      3 -> cache-ing elementi aperti su ram (poi room)
//      4 -> aggiungere banner per add/remove



public class DetailsFragment extends Fragment implements DetailsContract.View {

    // 1. ATTRIBUTI

    // 1.1
    private int traktMovieId;
    //private DetailsDto detailsDto = new DetailsDto();

    // 1.2 Views
    private TextView title;
    private ImageView image;
    private TextView released;
    private TextView runtimeFilm;
    private TextView country;
    private TextView rating;
    private TextView overview;
    private ImageButton buttonBack;


    // 1.3
    // ??? perchè final
    private final DetailsPresenter presenter = new DetailsPresenter(this);
    //MainNavigator navigator = new MainNavigator();

    // TODO     5°STEP -> addIcon
    boolean isFavorite = false; // leggi da presenter
    ImageButton addIcon;


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
        @Nullable Bundle savedInstanceState) {

        // 1 Assegnazione
        title = view.findViewById(R.id.titoloTitle);  //string
        image = view.findViewById(R.id.imageFilm); // glide
        released = view.findViewById(R.id.uscitaReleased); // string
        runtimeFilm = view.findViewById(R.id.durataRuntime); // int
        country = view.findViewById(R.id.paeseCountry); // string
        rating = view.findViewById(R.id.rating); // float
        overview = view.findViewById(R.id.descrizioneOverview); // string
        buttonBack = view.findViewById(R.id.buttonBack);


        // 2. Get Arguments
        Bundle bundle = getArguments();
        if (bundle != null) {
            traktMovieId = bundle.getInt("key_id");
        }


        // 3. BackButton
        buttonBack.setOnClickListener(v -> {
            // torna indietro nello stack
            // !!! metodo dell'activity (meglio consiglio eugi)
            requireActivity().onBackPressed();
        });


        //   TODO   5°STEP -> addIcon
        // Aggiungi elemento OK
        addIcon = view.findViewById(R.id.addToMylistButton);
        addIcon.setOnClickListener(v -> {
            presenter.addItem(); // TODO aggiungere dentro questo metodo view.statoIcona()
        });


        /*
        // TODO eugi spiegfazione -  addIcon - cambia Icona
        addToMylist.setOnClickListener(v -> {
            Drawable drawable;
            if (isFavorite) {
                drawable = getContext().getDrawable(R.drawable.baseline_favorite_24);
            } else {
                drawable = getContext().getDrawable(R.drawable.baseline_favorite_border_24);
            }
            addToMylist.setImageDrawable(drawable);

            isFavorite = !isFavorite;
        });

         */



        // 4. Load Data ( da cache o server) OK
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

    // 4.2   TODO   5°STEP -> addIcon
    public void setAddIcon (){}




    // TODO 1

}

