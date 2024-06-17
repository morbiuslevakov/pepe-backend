package com.pepe.miniapp.payload.response;

import com.pepe.miniapp.pojo.ReffPJ;

import java.math.BigInteger;
import java.util.List;

public class UserResponse {
    private BigInteger score;
    private List<ReffPJ> reffs;

    public UserResponse(BigInteger score, List<ReffPJ> reffs) {
        this.score = score;
        this.reffs = reffs;
    }

    public BigInteger getScore() {
        return score;
    }

    public void setScore(BigInteger score) {
        this.score = score;
    }

    public List<ReffPJ> getReffs() {
        return reffs;
    }

    public void setReffs(List<ReffPJ> reffs) {
        this.reffs = reffs;
    }
}
