package com.example.muvitracker.mainactivity.java.boxoffice;

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
import com.example.muvitracker.mainactivity.java.MainNavigator;
import com.example.muvitracker.mainactivity.java.details.DetailsFragment;
import com.example.muvitracker.repo.java.dto.BoxofficeDto;
import com.example.muvitracker.utils.java.UiUtils;
import com.example.muvitracker.utils.java.Visibility;

import java.util.List;

//             2°STEP
// = Popular


//             3°STEP
// = Popular
//      - passa dati a details - clickVH
//      - setErrorMessage



public class BoxofficeFragment extends Fragment implements BoxofficeContract.View {

    // 1. ATTRIBUTI
    RecyclerView recyclerView;
    BoxofficeAdapter adapter = new BoxofficeAdapter();

    ProgressBar progressBar;
    Button retryButton;
    TextView tvErrorMessage;

    BoxofficeContract.Presenter presenter = new BoxofficePresenter(this); // passo questa view al presenter
    SwipeRefreshLayout swipeRefreshLayout;
    MainNavigator navigator = new MainNavigator();


    // 2. METODI FRAGMENT
    // 2.1 Creazione
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragm_boxo, container, false);
    }

    // 2.2 Logica
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        // 1. Identificazione
        recyclerView = view.findViewById(R.id.boxofficeFragmentRV);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 3));

        progressBar = view.findViewById(R.id.progressBar);
        retryButton = view.findViewById(R.id.retryButton);
        tvErrorMessage = view.findViewById(R.id.errorMsgTextview);

        // 3. SwipeRefresh
        swipeRefreshLayout = view.findViewById(R.id.swipeToRefresh);
        swipeRefreshLayout.setOnRefreshListener(() -> {

            presenter.serverCallAndUpdateUi(true);


        });

        // 4. Default Loading Data
        presenter.serverCallAndUpdateUi(false);



        // 4. RetryButton
        retryButton.setOnClickListener(v -> {
            presenter.serverCallAndUpdateUi(false);
        });


        //      3°STEP
        // 6. Click VHolder OK
        adapter.setCallbackVH(movieId -> {
            presenter.onVHolderClick(movieId);
        });




    }


    // 3 CONTRACT METHODS

    // 3.1 Caricamento Dati
    @Override
    public void updateUi(List<BoxofficeDto> list) {

        adapter.updateAdapter(list);

        swipeRefreshLayout.setRefreshing(false); // ferma barra refreshing
    }


    // 3.2 Progresssion
    @Override
    public void setProgressBar(Visibility visibility) {
        UiUtils.setSingleViewVisibility(
            progressBar, visibility);
    }


    // 3.3 ErrorPage
    @Override
    public void setErrorPage(Visibility visibility, String errorMsg) {

        UiUtils.setDoubleViewVisibility(retryButton, tvErrorMessage, visibility);

        swipeRefreshLayout.setRefreshing(false);  // ferma barra refreshing

        tvErrorMessage.setText(errorMsg);
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



    // 3.5  Crea DetailsFragment + pass Id
    @Override
    public void startDetailsFragment(int movieId) {
        navigator.addToBackstackFragment(
            requireActivity(),
            DetailsFragment.create(movieId));
    }


}
