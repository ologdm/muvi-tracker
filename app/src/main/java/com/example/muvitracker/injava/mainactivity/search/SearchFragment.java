package com.example.muvitracker.injava.mainactivity.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.muvitracker.R;
import com.example.muvitracker.injava.mainactivity.MainNavigator;
import com.example.muvitracker.injava.mainactivity.details.DetailsFragment;
import com.example.muvitracker.injava.repo.dto.search.SearchDto;

import java.util.List;

//          4°STEP
// 1. mostra film e show
// 2. search ->EditText + ImmageButton

// TODO
//  1. debounce su presenter
//  2. empty states
//  3. swipe to refresh

public class SearchFragment extends Fragment implements SearchContract.View {


    // 1. ATTRIBUTI

    // 1.1 Rv
    private RecyclerView recyclerView;
    private SearchAdapter adapter = new SearchAdapter();

    // 1.2 Search Bar
    EditText searchEt;
    ImageButton searchButton;

    // 1.3
    private SearchContract.Presenter presenter = new SearchPresenter(this);
    private MainNavigator navigator = new MainNavigator();


    // 2. COSTRUTTORE - no


    // 3. METODI
    // 3.1 Creazione
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragm_sear, container, false);
    }


    // 3.2 Logica
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        // 1. idendificazione
        recyclerView = view.findViewById(R.id.recycleView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(),3));

        searchEt = view.findViewById(R.id.searchEditText);
        searchButton = view.findViewById(R.id.searchImageButton);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.setSearchText(searchEt.getText().toString());
                presenter.callServerAndUpdateUi();
            }
        });



        // 2. crea Details
        // -> presenter -> view.startDetails
        adapter.setCallbackVH(movieId -> {
            presenter.onVHolderClick(movieId);
        });


        presenter.callServerAndUpdateUi();

    }



    // 4. CONTRACT METHODS OK
    // 4.1 Aggiorna
    @Override
    public void updateUi(List<SearchDto> list) {
        adapter.updateList(list);
    }


    // 4.2 Crea details
    @Override
    public void startDetailsFragment(int id) {
        navigator.addToBackstackFragment(requireActivity(),
            DetailsFragment.create(id));
    }

}
