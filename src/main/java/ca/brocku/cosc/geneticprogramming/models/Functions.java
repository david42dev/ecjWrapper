package ca.brocku.cosc.geneticprogramming.models;

import lombok.Getter;

public enum Functions {
    ADD("Add",2),
    EULEREXPONENT("EulerExponential",1),
    INVERT("Invert",1),
    LOG("Log",1),
    MUL("Mul",2),
    SQROOT("SqRoot",1),
    SUB("Sub",2);

    Functions(String classname, int numberOfChildren){
        this.classname = classname;
        this.numberOfChildren = numberOfChildren;
    }

    @Getter
    private final String classname;

    @Getter
    private final int numberOfChildren;

    public static Functions valueOfLabel(String classname) {
        for (Functions f : values()) {
            if (f.classname.equals(classname)) {
                return f;
            }
        }
        return null;
    }
}
