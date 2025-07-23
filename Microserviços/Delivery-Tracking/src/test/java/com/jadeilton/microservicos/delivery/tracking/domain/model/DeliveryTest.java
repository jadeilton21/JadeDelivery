package com.jadeilton.microservicos.delivery.tracking.domain.model;

import com.jadeilton.microservicos.delivery.tracking.domain.exception.DomainException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class DeliveryTest {



    @Test
    public void shouldChangeToPlaced(){

        Delivery delivery = Delivery.draft();

        delivery.editPreparationDetails(createdValidPreparationDetails());

        delivery.place();
        assertEquals(DeliveryStatus.WAITING_FOR_COURIER, delivery.getStatus());
        assertNotNull(delivery.getPlacedAt());


    }

    @Test
    public void shouldNotPlace(){
        Delivery delivery = Delivery.draft();

        assertThrows(DomainException.class, () -> delivery.place());


        assertEquals(DeliveryStatus.DRAFT, delivery.getStatus());
        assertNull(delivery.getPlacedAt());
    }

    private Delivery.PreparationDetails createdValidPreparationDetails() {

        ContactPoint sender = ContactPoint.builder()
                .zipCode("00000-000")
                .street("Rua Campo Grande")
                .number("333")
                .complement("Sala 24")
                .name("Jadeilton")
                .phone("(82) 988994066")
                .build();


        ContactPoint recipient = ContactPoint.builder()
                .zipCode("3333-3333")
                .street("Rua Maximiana")
                .number("33")
                .complement("Casa")
                .name("Adriza")
                .phone("82 988994066")
                .build();



        return Delivery.PreparationDetails.builder()
                .sender(sender)
                .recipient(recipient)
                .distanceFee(new BigDecimal("100"))
                .courierPayout(new BigDecimal("3333"))
                .expectedDeliveryTime(Duration.ofHours(5))
                .build();
    }






}