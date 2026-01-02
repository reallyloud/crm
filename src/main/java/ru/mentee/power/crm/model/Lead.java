package ru.mentee.power.crm.model;

import ru.mentee.power.crm.domain.Contact;

import java.util.UUID;
import java.util.Objects;

public record Lead(UUID id, Contact contact, String company, LeadStatus status) {
    public Lead {
        if (id == null || contact == null || status == null) {
            throw new IllegalArgumentException();
        }
        if (!(status == LeadStatus.NEW ||
                status == LeadStatus.QUALIFIED ||
                status == LeadStatus.CONVERTED ||
                status == LeadStatus.CONTACTED)) {
            throw new IllegalArgumentException();
        }
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

    public String email() {
        return this.contact.email();
    }

}
