package com.jadeilton.microservicos.courier.management.domain.model;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.*;
import org.hibernate.id.Assigned;
import org.hibernate.validator.constraints.Normalized;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Setter(AccessLevel.PRIVATE)
public class Courier {



    @Id
    @EqualsAndHashCode.Include
    private UUID id;


    @Setter(AccessLevel.PUBLIC)
    private String name;

    @Setter(AccessLevel.PUBLIC)
    private String phone;


    private Integer fulfilledDeliveriesQuantity;

    private Integer pendingDeliveriesQuantity;


    private OffsetDateTime lastFulfilledDeliveryAt;




    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "courier")
    private List<AssignedDelivery> pendingDelivery = new ArrayList<>();


    public List<AssignedDelivery> getPendingDeliveries(){

        return Collections.unmodifiableList(this.pendingDelivery);
    }
    public static Courier brandNew(String name, String phone) {


        Courier courier = new Courier();
        courier.setId(UUID.randomUUID());

        courier.setName(name);
        courier.setPendingDeliveriesQuantity(0);
        courier.setFulfilledDeliveriesQuantity(0);
        return courier;

    }


    public void assign(UUID deliveryId){
        this.pendingDelivery.add(
                AssignedDelivery.pending(deliveryId, this)
        );
        this.pendingDeliveriesQuantity ++;
    }

    public void fulfill(UUID deliveryId){

        AssignedDelivery delivery = this.pendingDelivery.stream().filter(
                d -> d.getId().equals(deliveryId)
        ).findFirst().orElseThrow();

        this.pendingDelivery.remove(delivery);
        this.pendingDeliveriesQuantity--;

        this.fulfilledDeliveriesQuantity++;

        this.lastFulfilledDeliveryAt = OffsetDateTime.now();
    }
}
