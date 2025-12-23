package ru.mentee.power.crm.domain;

import java.util.UUID;
import java.util.Objects;

public record Lead(UUID id, Contact contact, String company, String status) {
    public Lead(UUID id, Contact contact, String company, String status) {
        if (id == null || contact == null || status == null) {
            throw new IllegalArgumentException();
        }
        if (!(status.equals("NEW") || status.equals("QUALIFIED") || status.equals("CONVERTED"))) {
            throw new IllegalArgumentException();
        }
        this.id = id;
        this.contact = contact;
        this.company = company;
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Lead lead = (Lead) o;
        return Objects.equals(id, lead.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
