package com.pepe.miniapp.pojo;

import java.math.BigInteger;

public class ReffPJ {
    private String name;
    private BigInteger score;

    public ReffPJ(String name, BigInteger score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigInteger getScore() {
        return score;
    }

    public void setScore(BigInteger score) {
        this.score = score;
    }
}
