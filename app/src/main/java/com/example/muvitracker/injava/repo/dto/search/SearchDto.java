package com.example.muvitracker.injava.repo.dto.search;


// JSON COMPLETO SEARCH ELEMENT ->
// classi principali:
// 1. movie (type, score, movie)
// 2. show, (type, score, show)

// TODO - implementare solo se serve
//  3. episode(type, score, episode, show)
//  4. person (type, score,person)
//  5. list(type, score, list(*,*,...., ids, user)


import com.example.muvitracker.injava.repo.dto.search.principali.Movie;
import com.example.muvitracker.injava.repo.dto.search.principali.Show;


public class SearchDto {


    // 1. ATTRIBUTI (4)

    // Uguali per tutte categorie
    private String type;
    private Double score; // float

    // 1,2 - i tipi che mi servono
    private Movie movie;
    private Show show;


    // 3,4,5 implementare se serve !!!!
    //private Episode episode;
    //private Person person;
    //private TraktList list;


    // 2. GETTERS
    public String getType() {
        return type;
    }

    public Double getScore() {
        return score;
    }

    public Movie getMovie() {
        return movie;
    }

    public Show getShow() {
        return show;
    }


    // 3. SETTERS
    public void setType(String type) {
        this.type = type;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public void setShow(Show show) {
        this.show = show;
    }
}


// ###########################################

// JSON COMPLETO SEARCH ELEMENT -> Struttura
// movie (type, score, movie)
// show, (type, score, show
// episode(type, score, episode, show)
// person (type, score,person)
// list(type, score, list(1,2,...., ids, )

/*


[
    {
        "type": "movie",
        "score": 26.019499,
        "movie": {
            "title": "TRON: Legacy",
            "year": 2010,
            "ids": {
                "trakt": 12601,
                "slug": "tron-legacy-2010",
                "imdb": "tt1104001",
                "tmdb": 20526
            }
        }
    },


    {
        "type": "show",
        "score": 19.533358,
        "show": {
            "title": "Tron: Uprising",
            "year": 2012,
            "ids": {
                "trakt": 34209,
                "slug": "tron-uprising",
                "tvdb": 258480,
                "imdb": "tt1812523",
                "tmdb": 34356
            }
        }
    },


    {
        "type": "episode",
        "score": 42.50835,
        "episode": {
            "season": 1,
            "number": 1,
            "title": "The Renegade (1)",
            "ids": {
                "trakt": 793693,
                "tvdb": 4318713,
                "imdb": null,
                "tmdb": 786460
            }
        },
        "show": {
            "title": "Tron: Uprising",
            "year": 2012,
            "ids": {
                "trakt": 34209,
                "slug": "tron-uprising",
                "tvdb": 258480,
                "imdb": "tt1812523",
                "tmdb": 34356
            }
        }
    },


    {
        "type": "person",
        "score": 53.421608,
        "person": {
            "name": "Jeff Bridges",
            "ids": {
                "trakt": 4173,
                "slug": "jeff-bridges",
                "imdb": "nm0000313",
                "tmdb": 1229
            }
        }
    },


    {
        "type": "list",
        "score": 38.643196,
        "list": {
            "name": "Open Your Eyes",
            "description": "Let food be thy medicine and medicine be thy food.",
            "privacy": "public",
            "share_link": "https://trakt.tv/lists/2180135",
            "type": "personal",
            "display_numbers": true,
            "allow_comments": true,
            "sort_by": "rank",
            "sort_how": "asc",
            "created_at": "2016-04-22T05:54:55.000Z",
            "updated_at": "2016-06-29T09:47:40.000Z",
            "item_count": 22,
            "comment_count": 0,
            "likes": 6,
            "ids": {
                "trakt": 2180135,
                "slug": "open-your-eyes"
            },
            "user": {
                "username": "justin",
                "private": false,
                "name": "Justin Nemeth",
                "vip": true,
                "vip_ep": true,
                "ids": {
                    "slug": "justin"
                }
            }
        }
    }
]
 */
