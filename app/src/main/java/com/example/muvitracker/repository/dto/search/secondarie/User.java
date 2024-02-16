package com.example.muvitracker.repository.dto.search.secondarie;

public class User {

    // attributi 6
    private String username;
    private String name;
    // !!! boolean private; non funzione,  nome=keyword
    private boolean vip;
    private boolean vip_ep;
    private UserId ids;


    // getter
    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public boolean isVip() {
        return vip;
    }

    public boolean isVip_ep() {
        return vip_ep;
    }

    public UserId getIds() {
        return ids;
    }


    // setter
    public void setUsername(String username) {
        this.username = username;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setVip(boolean vip) {
        this.vip = vip;
    }

    public void setVip_ep(boolean vip_ep) {
        this.vip_ep = vip_ep;
    }

    public void setIds(UserId ids) {
        this.ids = ids;
    }
}

