package ru.mentee.power.crm.domain;

import java.util.UUID;

public record Customer(UUID id, Contact contact, Address billingAddress, String loyaltyTier) {
  public Customer(UUID id, Contact contact, Address billingAddress, String loyaltyTier) {
    if (contact == null) {
      throw new IllegalArgumentException();
    }
    if (!(loyaltyTier.equals("BRONZE")
        || loyaltyTier.equals("SILVER")
        || loyaltyTier.equals("GOLD"))) {
      throw new IllegalArgumentException();
    }
    this.contact = contact;
    this.id = id;
    this.billingAddress = billingAddress;
    this.loyaltyTier = loyaltyTier;
  }
}
