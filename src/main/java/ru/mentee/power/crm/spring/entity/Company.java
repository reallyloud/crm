package ru.mentee.power.crm.spring.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;
import java.util.UUID;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "companies")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column
    private String industry;

    @OneToMany(mappedBy = "company", cascade = CascadeType.PERSIST)
    private List<Lead> leads = new ArrayList<>();

    public void addLead(Lead lead) {
        leads.add(lead);
        lead.setCompany(this);
    }

    public void removeLead(Lead lead) {
        leads.remove(lead);
        lead.setCompany(null);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Company company)) return false;
        return Objects.equals(name, company.name) && Objects.equals(industry, company.industry);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, industry);
    }
}
