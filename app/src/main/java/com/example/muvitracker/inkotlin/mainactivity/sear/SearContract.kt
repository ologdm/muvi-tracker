package com.example.muvitracker.inkotlin.mainactivity.sear

import com.example.muvitracker.inkotlin.repo.dto.search.SearDto


interface SearContract {

     interface View {

         fun updateUi(list:List<SearDto>)

         fun startDetailsFragment(traktMovieId: Int)

     }



     interface Presenter {

         fun getNetworkResult(text:String)

         fun onVHolderClick(traktMovieId: Int)


     }




}