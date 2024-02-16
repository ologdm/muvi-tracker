package com.example.muvitracker.repository.dto.search.principali;

import com.example.muvitracker.repository.dto.search.secondarie.ListIds;
import com.example.muvitracker.repository.dto.search.secondarie.User;



public class TraktList {

    // Attributi (16)
    private String name;
    private String description;
    private String privacy;
    private String share_link;
    private String type;
    private boolean display_numbers;
    private boolean allow_comments;
    private String sort_by;
    private String sort_how;
    private String created_at;
    private String updated_at;
    private int item_count;
    private int comment_count;
    private int likes;
    private ListIds ids;
    private User user;

    // getters and setters

}
