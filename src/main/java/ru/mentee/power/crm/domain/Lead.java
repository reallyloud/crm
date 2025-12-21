package ru.mentee.power.crm.domain;

import java.util.UUID;
import java.util.Objects;

public class Lead {
    UUID id;
    String email;
    String phone;
    String company;
    String status;

    public Lead(UUID id, String email, String phone, String company, String status) {
        this.id = id;
        this.email = email;
        this.phone = phone;
        this.company = company;
        this.status = status;
    }

    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getCompany() {
        return company;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "Lead{id = %s, email = %s, phone = %s, company = %s, status = %s }".
                formatted(id, email, phone, company, status);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Lead lead = (Lead) o;
        return Objects.equals(id, lead.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
