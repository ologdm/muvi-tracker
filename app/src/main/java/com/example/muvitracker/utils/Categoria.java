package com.example.muvitracker.utils;

// non utilizzato XXXXXXX

public enum Categoria {

    POPULAR ("Popular"),
    BOXOFFICE ("Box Office");


    private final String categoria;

    Categoria (String c){
        this.categoria =c;
    }

    public String getCategoria() {
        return categoria;
    }
}
