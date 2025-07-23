package com.jadeilton.microservicos.delivery.tracking.domain.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum DeliveryStatus {



    DRAFT,
    WAITING_FOR_COURIER(DRAFT),
    IN_TRANSIT(WAITING_FOR_COURIER),
    DELIVERY(IN_TRANSIT);

    private final List<DeliveryStatus> previousStatuses;


    DeliveryStatus(DeliveryStatus... previousStatuses) {

        this.previousStatuses = Arrays.asList(previousStatuses);

    }

    public boolean canNotChange(DeliveryStatus newStatus) {
        DeliveryStatus current = this;
        return !newStatus.previousStatuses.contains(current);
    }

    public boolean canChangeTo(DeliveryStatus newStatus) {
        return !canNotChange(newStatus);
    }

}
