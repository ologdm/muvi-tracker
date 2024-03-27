package com.example.muvitracker.mainactivity.kotlin.sear

import com.example.muvitracker.repo.kotlin.dto.search.SearDto


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