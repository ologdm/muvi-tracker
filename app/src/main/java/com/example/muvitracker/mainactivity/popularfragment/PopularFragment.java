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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.muvitracker.R;
import com.example.muvitracker.utils.UiUtils;
import com.example.muvitracker.utils.Visibility;
import com.example.muvitracker.repository.dto.MovieDto;

import java.util.List;

// 1. Rappresenta la lista da API
// 2. passa dati ad altre fragment - esempio details;  ma forse non serve !!!!


public class PopularFragment extends Fragment implements PopularContract.View {

    // ATTRIBUTI
    RecyclerView recyclerView;
    PopularAdapter adapter = new PopularAdapter();
    PopularContract.Presenter presenter = new PopularPresenter(this);

    // Empty states, include_layout
    ProgressBar progressBar;
    Button retryButton;
    TextView noInternetText;

    // refresh
    SwipeRefreshLayout swipeRefreshLayout;



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
        recyclerView.setAdapter(adapter);
        //recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        recyclerView.setLayoutManager(new GridLayoutManager(requireActivity(),3));


        // Empty States OK
        progressBar = view.findViewById(R.id.progressBar);
        retryButton = view.findViewById(R.id.retryButton);
        noInternetText = view.findViewById(R.id.noInternetText);


        // SwipeRefresh OK

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(
            () -> presenter.serverCallAndUpdateUi(true));

        //  poi nascondi caricamento di swipeRefresh: .setRefreshing(false); '
        // caso success - in .updateUi ()
        // caso error - in caso .setErrorPage(nointernet)



        // Caricamento Default OK
        presenter.serverCallAndUpdateUi(false);


        // Retry OK
        retryButton.setOnClickListener(v ->
            presenter.serverCallAndUpdateUi(false));


    }


    // CONTRACT METHODS - utilizzati in presenter
    // 1. caricamento dati
    @Override
    public void updateUi(List<MovieDto> list) {
        adapter.updateList(list);

        // in caso di success,
        swipeRefreshLayout.setRefreshing(false);
        setRvVisibility(Visibility.SHOW);
    }


    // 2. Progresssion  + Error OK
    // di default -> entrambi GONE
    @Override
    public void setProgressBar(Visibility visibility) {
        UiUtils.setSingleViewVisibility(progressBar, visibility);
    }

    // 3.
    @Override
    public void setErrorPage(Visibility visibility) {
        UiUtils.setDoubleViewVisibility(retryButton, noInternetText, visibility);
        // nascondere barra caricamento
        swipeRefreshLayout.setRefreshing(false);
    }


    // 4.
    @Override
    public void setRvVisibility(Visibility visibility) {
        if (visibility == Visibility.SHOW) {
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.GONE);
        }
    }


}




