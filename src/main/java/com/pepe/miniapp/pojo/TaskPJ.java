package com.pepe.miniapp.pojo;

public class TaskPJ {
    private String id;
    private String title;
    private int reward;
    private String link;
    private boolean isCompleted;

    public TaskPJ(String id, String title, int reward, String link, boolean isCompleted) {
        this.id = id;
        this.title = title;
        this.reward = reward;
        this.link = link;
        this.isCompleted = isCompleted;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getReward() {
        return reward;
    }

    public void setReward(int reward) {
        this.reward = reward;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
}
