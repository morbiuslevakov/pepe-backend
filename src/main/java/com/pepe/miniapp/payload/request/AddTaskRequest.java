package com.pepe.miniapp.payload.request;

public class AddTaskRequest {
    private String title;
    private int reward;
    private String link;

    public AddTaskRequest(String title, int reward, String link) {
        this.title = title;
        this.reward = reward;
        this.link = link;
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
}
