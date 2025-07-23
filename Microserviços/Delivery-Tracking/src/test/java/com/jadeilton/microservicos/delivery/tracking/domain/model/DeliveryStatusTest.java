package com.jadeilton.microservicos.delivery.tracking.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeliveryStatusTest {




    @Test
    void draft_canChangeTowaitingForCourier()
    {


        assertTrue(
          DeliveryStatus.DRAFT.canChangeTo(DeliveryStatus.WAITING_FOR_COURIER)
        );
    }

}