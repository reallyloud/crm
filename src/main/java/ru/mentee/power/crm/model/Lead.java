package ru.mentee.power.crm.model;

import ru.mentee.power.crm.domain.Address;
import ru.mentee.power.crm.domain.Contact;

import java.util.UUID;
import java.util.Objects;

public record Lead(UUID id, Contact contact, String company, LeadStatus status, String email) {
    public Lead (UUID id, Contact contact, String company, LeadStatus status){
        if (status == null || company == null) {
            throw new IllegalArgumentException();
        }
        if (!(status == LeadStatus.NEW ||
                status == LeadStatus.QUALIFIED ||
                status == LeadStatus.CONVERTED ||
                status == LeadStatus.CONTACTED)) {
            throw new IllegalArgumentException();
        }

        this(id, contact,company, status, "");
    }

    public Lead (UUID uuid, String email, String company, LeadStatus status) {
        if (email == null || company == null || status == null) {
            throw new IllegalArgumentException();
        }
        Address address = new Address("","","");
        Contact contact = new Contact("","",address);

        this(uuid,contact,company,status, email);
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
