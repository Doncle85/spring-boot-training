package com.example.demo.food;


public class Restaurant {

    private String name;
    private String specialities;

    public Restaurant(String name, String specialities) {
        this.name = name;
        this.specialities = specialities;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecialities() {
        return specialities;
    }

    public void setSpecialities(String specialities) {
        this.specialities = specialities;
    }


}
