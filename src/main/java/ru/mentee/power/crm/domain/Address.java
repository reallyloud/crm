package ru.mentee.power.crm.domain;

public record Address(String city,String street, String zip) {
public Address(String city,String street, String zip) {
    if (city == null || city.isEmpty()) {
        throw new IllegalArgumentException();
    } else if (zip == null || zip.isEmpty()) {
        throw new IllegalArgumentException();
    }
    this.city = city;
    this.street = street;
    this.zip = zip;
}
}
