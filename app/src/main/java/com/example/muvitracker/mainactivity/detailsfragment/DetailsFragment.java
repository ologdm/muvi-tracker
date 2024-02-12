package com.example.muvitracker.mainactivity.detailsfragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.muvitracker.R;
import com.example.muvitracker.repository.dto.DetailsDto;


// Details film si apre grazie al IdFilm,
// Poi cerca sul DB necessario i dati da mostrare
// Scroll view
// Aggiorna caselle view solo quando ho i dati
// TODO -> mettere caricamento prima di apertura fragment -> su presenter
// TODO -> arrotondare valori rating(float)


// TODO 3°STEP

public class DetailsFragment extends Fragment implements DetailsContract.View {

    // 1. ATTRIBUTI
    // 1.1 Dto
    private int movieId;
    private DetailsDto detailsDto = new DetailsDto();

    // 1.2 Views
    private TextView title;
    private ImageView image;
    private TextView released;
    private TextView runtime;
    private TextView country;
    private TextView rating;
    private TextView overview;
    private Button buttonBack;

    private DetailsPresenter presenter = new DetailsPresenter(this);


    // COSTRUTTORE OK
    // 2.1 privato - dobbiamo sempre avere un Id
    private DetailsFragment() {
    }

    // 2.2 Factory OK
    // Crea Fragment con Id - SetArguments -> usare
    public static DetailsFragment create(int id) {
        DetailsFragment detailsFragment = new DetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("key_id", id);
        detailsFragment.setArguments(bundle);
        return detailsFragment;
    }


    // 3. FUNZIONI FRAGMENT
    // 3.1.Creazione
    @Nullable
    @Override
    public View onCreateView(
        @NonNull LayoutInflater inflater,
        @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    // 3.2. Logica
    @Override
    public void onViewCreated(
        @NonNull View view,
        @Nullable Bundle savedInstanceState) {

        // 1 Assegnazione View OK
        title = view.findViewById(R.id.titoloTitle);  //string
        image = view.findViewById(R.id.imageFilm); // glide
        released = view.findViewById(R.id.uscitaReleased); // string
        runtime = view.findViewById(R.id.durataRuntime); // int
        country = view.findViewById(R.id.paeseCountry); // string
        rating = view.findViewById(R.id.rating); // float
        overview = view.findViewById(R.id.descrizioneOverview); // string

        buttonBack = view.findViewById(R.id.buttonBack);


        // 2. Get Arguments
        Bundle bundle = getArguments();
        if (bundle != null) {
            movieId = bundle.getInt("key_id");
        }
        // 3. Passa Id
        presenter.passIdToCall(movieId);

        // 4. Updata Data
        presenter.getDataFromCall();


        // 5. TODO 1
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO -> torna indietro nello stack
                // getActivity().onBackPressed(); - > esce dal programma

            }
        });

    }


    // 4. METODO CONTRACT
    // 4.1 OK
    @Override
    public void updateDetailsDto(DetailsDto detailsDto) {
        this.detailsDto = detailsDto;
        System.out.println("dto");
    }

    // 4.2 OK
    // devo eseguire .setText() solo quando ho la risposta dal server
    @Override
    public void updateUi() {
        title.setText(detailsDto.getTitle());
        released.setText(detailsDto.getReleased());
        runtime.setText(String.valueOf(detailsDto.getRuntime()) + " min");
        country.setText(detailsDto.getCountry());
        rating.setText(String.valueOf(detailsDto.getRating()) + " stars");
        overview.setText(detailsDto.getOverview());

        //4.3 OK
        Glide.with(getContext())
            .load(detailsDto.getImageUrl())  //Url
            .into(image);

    }


}

