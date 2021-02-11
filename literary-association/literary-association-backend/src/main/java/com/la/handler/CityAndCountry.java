package com.la.handler;


import java.io.Serializable;

public class CityAndCountry implements Serializable {
    private Long id;
    private String value;

    public CityAndCountry(Long id, String cityAndCountry) {
        this.id = id;
        this.value = cityAndCountry;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "CityAndCountry{" +
                "id=" + id +
                ", value='" + value + '\'' +
                '}';
    }
}
