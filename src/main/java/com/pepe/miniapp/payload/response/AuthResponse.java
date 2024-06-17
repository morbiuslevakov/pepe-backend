package com.pepe.miniapp.payload.response;

public class AuthResponse {
    private boolean isNewUser;
    private int leader;
    private boolean authenticated;

    public AuthResponse() {
    }

    public AuthResponse(boolean isNewUser, int leader) {
        this.isNewUser = isNewUser;
        this.leader = leader;
    }

    public boolean isNewUser() {
        return isNewUser;
    }

    public void setNewUser(boolean newUser) {
        isNewUser = newUser;
    }

    public int getLeader() {
        return leader;
    }

    public void setLeader(int leader) {
        this.leader = leader;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }
}
