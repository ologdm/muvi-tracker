package com.example.muvitracker.mainactivity.boxofficefragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.muvitracker.R;
import com.example.muvitracker.repository.dto.BoxofficeDto;
import java.util.List;


// al click, MainActivity crea BoxofficeFragment

public class BoxofficeFragment extends Fragment implements BoxofficeContract.View {




    // METODI FRAGMENT
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_boxoffice,container,false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {



    }



    // CONTRACT METHODS
    @Override
    public void UpdateUi(List<BoxofficeDto> boxofficeList) {
        //adapter
    }
}
