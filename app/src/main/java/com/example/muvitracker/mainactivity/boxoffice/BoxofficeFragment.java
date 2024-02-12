package com.example.muvitracker.mainactivity.boxoffice;

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
import com.example.muvitracker.repository.dto.BoxofficeDto;
import com.example.muvitracker.utils.UiUtils;
import com.example.muvitracker.utils.Visibility;

import java.util.List;


// al click, MainActivity crea BoxofficeFragment

public class BoxofficeFragment extends Fragment implements BoxofficeContract.View {

    // ATTRIBUTI
    // base
    RecyclerView recyclerView;
    BoxofficeAdapter adapter = new BoxofficeAdapter();

    BoxofficeContract.Presenter presenter = new BoxofficePresenter(this); // passo questa view al presenter

    // empty states
    ProgressBar progressBar;
    Button retryButton;
    TextView noInternetText;

    SwipeRefreshLayout swipeRefreshLayout;


    // METODI FRAGMENT
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_boxoffice, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        // Identificazione View
        recyclerView = view.findViewById(R.id.boxofficeFragmentRV);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 3));

        progressBar = view.findViewById(R.id.progressBar);
        retryButton = view.findViewById(R.id.retryButton);
        noInternetText = view.findViewById(R.id.noInternetText);

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);


        // Codice Funzionamento

        // click retry
        // click swipe
        swipeRefreshLayout.setOnRefreshListener(()
            -> presenter.serverCallAndUpdateUi(true));


        // chiama funzione
        presenter.serverCallAndUpdateUi(false);


    }


    // CONTRACT METHODS
    @Override
    public void updateUi(List<BoxofficeDto> list) {
        //adapter
        adapter.updateAdapter(list);
        swipeRefreshLayout.setRefreshing(false); // ferma barra refreshing
    }

    @Override
    public void setProgressBar(Visibility visibility) {
        UiUtils.setSingleViewVisibility(progressBar, visibility);
    }


    @Override
    public void setErrorPage(Visibility visibility) {
        UiUtils.setDoubleViewVisibility(retryButton, noInternetText, visibility);
        swipeRefreshLayout.setRefreshing(false); // ferma barra refreshing
    }


    @Override
    public void setRvVisibility(Visibility visibility) {
        if (visibility == Visibility.SHOW) {
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.GONE);
        }
    }
}
