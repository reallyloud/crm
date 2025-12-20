package ru.mentee.power.crm.storage;

import ru.mentee.power.crm.domain.Lead;

public class LeadStorage {
    private Lead[] leads = new Lead[100];

    public boolean add(Lead lead) {
        for (Lead current : leads) {
            if (current != null && current.getEmail().equals(lead.getEmail())) {
                return false;
            }
        }
        for (int i = 0; i < leads.length; i++) {
            if (leads[i] == null) {
                leads[i] = lead;
                return true;
            }
        }
        throw new IllegalStateException("Storage is full");
    }

    public Lead[] findAll() {
        int count = 0;
        for (Lead current : leads) {
            if (current != null) {
                count++;
            }
        }

        Lead[] result = new Lead[count];
        int resultIndex = 0;
        for (int i = 0; i < leads.length; i++) {
            if (leads[i] != null) {
                result[resultIndex] = leads[i];
                resultIndex++;
            }
        }
        return result;
    }

    public int size() {
        int count = 0;
        for (Lead lead : leads) {
            if (lead != null) {
                count++;
            }
        }
        return count;
    }
}
