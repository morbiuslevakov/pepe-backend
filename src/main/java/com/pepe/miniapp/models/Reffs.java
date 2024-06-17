package com.pepe.miniapp.models;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "reffs")
public class Reffs {
    private String id;
    @DBRef
    private User user;
    @DBRef
    private List<User> reffs;

    public Reffs(User user) {
        this.user = user;
        this.reffs = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<User> getReffs() {
        return reffs;
    }

    public void setReffs(List<User> reffs) {
        this.reffs = reffs;
    }
}
