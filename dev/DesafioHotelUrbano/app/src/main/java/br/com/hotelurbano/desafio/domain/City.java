package br.com.hotelurbano.desafio.domain;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by gustavoamg on 11/02/17.
 */

public class City implements Parcelable {

    String name;

    public City() {
    }

    protected City(Parcel in) {
        name = in.readString();
    }

    public static final Creator<City> CREATOR = new Creator<City>() {
        @Override
        public City createFromParcel(Parcel in) {
            return new City(in);
        }

        @Override
        public City[] newArray(int size) {
            return new City[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
    }
}
