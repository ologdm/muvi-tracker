# MuviTracker

Movie tracking android application based on [Trakt API](https://trakt.docs.apiary.io/#)

## Features
- Explore popular and box-office movies
- Search any movie from Trakt database
- My list: add a movie to favourites to see it later, and mark it as viewed when done

## Screenshots

<div class="row">
  <img src="app/app-screenshots/base.png" width="24%"/>
  <img src="app/app-screenshots/details.png" width="24%"/>
  <img src="app/app-screenshots/favorite.png" width="24%"/>
  <img src="app/app-screenshots/search.png" width="24%"/>
</div>

## Tech stack & open-source libraries
- Min SDK level 24
- Entirely written in [Kotlin](https://kotlinlang.org/) (previously Java)
- MVP Architecture (also MVVM for test)
- [Retrofit](https://github.com/square/retrofit) - - Construct the REST APIs
- [Gson](https://github.com/google/gson) - convert Java Objects into their JSON representation
- SharedPreferences - Storage and Caching
- [Glide](https://github.com/bumptech/glide) - Loading images 
- [Material Design 3](https://m3.material.io/)
- Androidx
    - Swipe refresh layout
    - Constratint layout
    - Appcompat



## What's next?

- Dependency Injection using [Dagger](https://github.com/google/dagger)
- [Coroutines](https://github.com/Kotlin/kotlinx.coroutines) for asynchronous programming
- Pagination using androidx [Paging3](https://developer.android.com/topic/libraries/architecture/paging/v3-overview)
- Animations and other graphic features
- Features
    - TV Series support

## Download

Go to the <u>[Releases](https://github.com/ologdm/muvi-tracker/releases)</u> to download the latest APK.

