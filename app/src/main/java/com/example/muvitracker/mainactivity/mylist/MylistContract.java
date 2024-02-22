package com.example.muvitracker.mainactivity.mylist;


import java.util.List;

public interface MylistContract {

    public interface View {

        public void updateUi (List<MylistDto> myList);

    }


    public interface Presenter {

        public void loadData ();

        public void setList(List<MylistDto> prefList);

    }

}
