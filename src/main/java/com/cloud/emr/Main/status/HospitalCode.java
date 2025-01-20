package com.cloud.emr.Main.status;

public enum HospitalCode {

    A001("서울병원"),
    B002("부산병원"),
    C003("대구병원");

    private final String name;

    HospitalCode(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
