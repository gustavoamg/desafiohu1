package br.com.hotelurbano.desafio.domain;

import java.util.Date;

/**
 * Created by gustavoamg on 13/02/17.
 */

public class Availability {

    private Date date;
    private int available;
    private int minimumNights;
    private Hotel hotel;

    public Availability() {
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getMinimumNights() {
        return minimumNights;
    }

    public void setMinimumNights(int minimumNights) {
        this.minimumNights = minimumNights;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public int getAvailable() {
        return available;
    }

    public void setAvailable(int available) {
        this.available = available;
    }
}
