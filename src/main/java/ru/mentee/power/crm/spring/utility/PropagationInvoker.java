package ru.mentee.power.crm.spring.utility;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PropagationInvoker {

    public final Propagation propagationTester;

    @Transactional(rollbackFor = IllegalArgumentException.class)
    public void propagationRequired(List<UUID> ids) {
        int i = 0;
        for (UUID id: ids) {
                propagationTester.propagationRequired(id, i);
            i++;
        }
    }

    public void propagationRequiresNew(List<UUID> ids) {
        int i = 0;
        for (UUID id: ids) {
            try {
                propagationTester.propagationRequiresNew(id,i);
            } catch (IllegalArgumentException _) {}
            i++;
        }
    }

    public void propagationMandatory(List<UUID> ids) {
        int i = 0;
        for (UUID id: ids) {
                propagationTester.propagationMandatory(id,i);
            i++;
        }
    }
}
