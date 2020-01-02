package ca.brocku.cosc.geneticprogramming.models;

import lombok.Getter;

public enum Terminals {
    X("X"),
    Y("Y");

    Terminals(String classname){
        this.classname = classname;
    }

    @Getter
    private String classname;
}
