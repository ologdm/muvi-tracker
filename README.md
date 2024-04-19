# MuviTracker

App that allows you to quickly save a movie you want to watch in your favorites list 

It's based on <u>[Trakt APIs](https://trakt.docs.apiary.io/#)</u> (TMDB - TVDB - Fanart.tv - OMDB)

allows you to:
- navigate between popular and box office movies list
- look at the movie details
- put a movie in your favorite list, both as liked and watched
- search for a movie title in trakt Db

## Screenshots

<div class="row">
  <img src="app/app-screenshots/base.png" width="24%"/>
  <img src="app/app-screenshots/details.png" width="24%"/>
  <img src="app/app-screenshots/favorite.png" width="24%"/>
  <img src="app/app-screenshots/search.png" width="24%"/>
</div>

## Download

Go to the <u>[Releases](https://github.com/ologdm/muvi-tracker/releases)</u> to download the latest APK.

## Tech stack & Open-source libraries

- Minimum SDK level 30
- [Kotlin](https://kotlinlang.org/) based project, callbacks for asynchronous
- Architecture
    - MVP (Model - View - Presenter) 
      - Structure Layers: (View - Presenter - Repository - DataSource)
    - MVVM for test

- [Retrofit](https://github.com/square/retrofit) - Construct the REST APIs
- [Gson](https://github.com/google/gson) - convert Java Objects into their JSON representation
- SharedPreferences - Storage and Caching  

- [Glide](https://github.com/bumptech/glide) - Loading images from network
- [Material Design 3](https://m3.material.io/) - view components, theme
- Animations - android default



## What's next?
- Dependency Injection with Dagger
- Coroutines for asynchronous
- Paging3 network data with coroutines
- Animations and other graphic features
- Add also the TV series. 