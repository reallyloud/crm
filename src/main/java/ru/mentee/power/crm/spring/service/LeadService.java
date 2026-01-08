package ru.mentee.power.crm.spring.service;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import ru.mentee.power.crm.domain.Address;
import ru.mentee.power.crm.domain.Contact;
import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.spring.repository.LeadRepository;

@Service
@RequiredArgsConstructor
public class LeadService {

    private final LeadRepository<Lead> repository;

    /**
     * Создаёт нового лида с проверкой уникальности email.
     *
     * @throws IllegalStateException если лид с таким email уже существует
     */
    public Lead addLead(String email, String company, LeadStatus status) {
        if (repository.findByEmail(email).isPresent()) {
            throw new IllegalStateException("Лид с таким Email уже существует!");
        }
        Address address = new Address("Moscow", "Lubyanka", "zip");
        Contact contact = new Contact(email, "899436964", address);
        Lead lead = new Lead(UUID.randomUUID(), contact, company, status);
        repository.save(lead);
        return lead;
    }

    public List<Lead> findAll() {
        return repository.findAll();
    }

    public Optional<Lead> findById(UUID id) {
        return repository.findById(id);
    }

    public Optional<Lead> findByEmail(String email) {
        return repository.findByEmail(email);
    }

}