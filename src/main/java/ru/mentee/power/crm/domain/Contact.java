package ru.mentee.power.crm.domain;

public record Contact(String email, String phone, Address address) {
  public Contact(String email, String phone, Address address) {
    if (address == null) {
      throw new IllegalArgumentException();
    }
    this.email = email;
    this.phone = phone;
    this.address = address;
  }
}
