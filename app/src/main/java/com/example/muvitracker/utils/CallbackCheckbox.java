package com.example.muvitracker.utils;

import java.util.List;

// invia all'indietro una lista
// adapter - fragment

public interface CallbackCheckbox <T> {
        public void play (List<T> listAdapter);
    }


