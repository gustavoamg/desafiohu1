package br.com.hotelurbano.desafio.domain;

/**
 * Created by gustavoamg on 11/02/17.
 */

public class Hotel {

    private long id;
    private String name;
    private City city;

    public Hotel() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }
}
