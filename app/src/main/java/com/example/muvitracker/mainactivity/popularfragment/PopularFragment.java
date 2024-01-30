package com.example.muvitracker.mainactivity.popularfragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.muvitracker.R;
import com.example.muvitracker.mainactivity.UiUtils;
import com.example.muvitracker.mainactivity.VisibilityEnum;
import com.example.muvitracker.repository.dto.MovieDto;

import java.util.List;

// 1. Rappresenta la lista da API
// 2. passa dati ad altre fragment - esempio details;  ma forse non serve !!!!


public class PopularFragment extends Fragment implements PopularContract.View {

    // ATTRIBUTI
    RecyclerView recyclerView;
    PopularAdapter popularAdapter = new PopularAdapter();

    PopularContract.Presenter presenter = new PopularPresenter(this);

    // Empty states, include_layout
    ProgressBar progressBar;
    Button buttonRetry;
    TextView textNoInternet;




    // 1. CREAZIONE VIEW
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_popular, container, false);
    }


    // 2. LOGICA
    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        //presenter = new PopularPresenter(this);

        // OK
        recyclerView = view.findViewById(R.id.popularFragmentRV);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        recyclerView.setAdapter(popularAdapter);

        // TODO empty states
        //progressBar = view.findViewById(R.id.progressBar);
        //buttonRetry = view.findViewById(R.id.retryButton);
        //tvNoInternet = view.findViewById(R.id.noInternetConnection);

        presenter.serverCallAndUpdateUi();
        System.out.println("prova");
    }


    // CONTRACT METHODS - utilizzati in presenter
    // 1.
    @Override
    public void updateUi(List<MovieDto> list) {
        popularAdapter.updateList(list);
    }


    // 2. progresssion  + error OK
    @Override
    public void setProgressBar(VisibilityEnum visibility) {
        UiUtils.setSingleViewVisibility(progressBar, visibility);
        /* prova
        if (visibility==VisibilityEnum.SHOW){
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }

         */

    }


    @Override
    public void setErrorPage(VisibilityEnum visibility) {
        UiUtils.setDoubleViewVisibility(buttonRetry, textNoInternet, visibility);
        // prova
        /*
        if (visibility==VisibilityEnum.SHOW){
            buttonRetry.setVisibility(View.VISIBLE);
            tvNoInternet.setVisibility(View.VISIBLE);
        } else {
            buttonRetry.setVisibility(View.GONE);
            tvNoInternet.setVisibility(View.GONE);
        }

         */
    }


}




