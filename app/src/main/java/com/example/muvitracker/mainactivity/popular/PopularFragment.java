package com.example.muvitracker.mainactivity.popular;

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
import com.example.muvitracker.mainactivity.MainNavigator;
import com.example.muvitracker.mainactivity.details.DetailsFragment;
import com.example.muvitracker.utils.UiUtils;
import com.example.muvitracker.utils.Visibility;
import com.example.muvitracker.repository.dto.MovieDto;

import java.util.List;

//        2°STEP->
// 1. Rappresenta la lista da API
// 2. metodi EmptyStates
// 3. metodi aggiornam Adapter MVP
// 4. SwipeRefresh
//          1- setOnRefreshListener()
//          2- setRefreshing(false) ->
//             hide loading bar per caso success and fail

//        3°STEP
// 1. Passa dati ad altre fragment -> details
// 2. metodo Click VH() -> startDetailsFragment()
//                      -> chiama funzione -> @Presenter-> @Fragment MVP



public class PopularFragment extends Fragment implements PopularContract.View {

    // 1. ATTRIBUTI
    // 1.1 RView
    RecyclerView recyclerView;
    PopularAdapter adapter = new PopularAdapter();

    // 1.2 Empty states, include_layout
    ProgressBar progressBar;
    Button retryButton;
    TextView noInternetText;

    // 1.3 Refresh
    SwipeRefreshLayout swipeRefreshLayout;

    // 1.4
    PopularContract.Presenter presenter = new PopularPresenter(this);


    // 1.5 3°STEP OK
    MainNavigator navigator = new MainNavigator();


    // 2. FUNZIONI FRAGMENT
    // 2.1 Creazione
    @Nullable
    @Override
    public View onCreateView(
        @NonNull LayoutInflater inflater,
        @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_popular, container, false);
    }


    // 2.2 Logica
    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        //presenter = new PopularPresenter(this);

        // 1. OK
        recyclerView = view.findViewById(R.id.popularFragmentRV);
        recyclerView.setAdapter(adapter);
        //recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        recyclerView.setLayoutManager(new GridLayoutManager(requireActivity(), 3));


        // 2. Empty States OK
        progressBar = view.findViewById(R.id.progressBar);
        retryButton = view.findViewById(R.id.retryButton);
        noInternetText = view.findViewById(R.id.noInternetText);


        // 3. SwipeRefresh OK
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(
            () -> presenter.serverCallAndUpdateUi(true));
        //  poi nascondi caricamento di swipeRefresh: .setRefreshing(false); '
        // caso success - in .updateUi ()
        // caso error - in caso .setErrorPage(nointernet)


        // 4. Default Loading Data OK
        presenter.serverCallAndUpdateUi(false);


        // 5. Retry Data OK
        retryButton.setOnClickListener(v -> {
            presenter.serverCallAndUpdateUi(false);
        });


        // 6.       3°STEP
        // - Click VHolder OK
        adapter.setCallbackVH(movieId -> {
            presenter.onVHolderClick(movieId);
        });
    }





    // 3. CONTRACT METHODS
    // !!! utilizzati in presenter

    // 3.1 Caricamento Dati
    @Override
    public void updateUi(List<MovieDto> list) {
        adapter.updateList(list);
        setRvVisibility(Visibility.SHOW);

        // Caso Success
        swipeRefreshLayout.setRefreshing(false);
    }



    // 3.2 Progresssion
    // -> default .GONE
    @Override
    public void setProgressBar(Visibility visibility) {
        UiUtils.setSingleViewVisibility(progressBar, visibility);
    }


    // 3.3 ErrorPage
    // (default .GONE)
    @Override
    public void setErrorPage(Visibility visibility) {
        UiUtils.setDoubleViewVisibility(retryButton, noInternetText, visibility);
        // Caso2 - fail

        swipeRefreshLayout.setRefreshing(false);
    }


    // 3.4 RView
    @Override
    public void setRvVisibility(Visibility visibility) {
        if (visibility == Visibility.SHOW) {
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.GONE);
        }
    }


    //          3°STEP
    // 3.5  Crea DetailsFragment + pass Id
    @Override
    public void startDetailsFragment(int movieId) {
        navigator.addToBackstackFragment(
            requireActivity(),
            DetailsFragment.create(movieId));
    }


}




