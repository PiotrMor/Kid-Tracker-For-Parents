package com.example.android.kidtrackerparent.Enums;

public enum AccountType {
    PARENT(1), KID(2);

    private int numVal;

    AccountType(int numVal) {
        this.numVal = numVal;
    }

    public int getNumVal() {
        return numVal;
    }
}
