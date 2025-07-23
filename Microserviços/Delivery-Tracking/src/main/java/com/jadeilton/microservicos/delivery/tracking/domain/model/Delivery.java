package com.jadeilton.microservicos.delivery.tracking.domain.model;

import jakarta.persistence.*;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;




@Entity
@NoArgsConstructor(access = AccessLevel.PACKAGE )
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Setter(AccessLevel.PRIVATE)
@Getter
public class Delivery {

        @Id
        @EqualsAndHashCode.Include
        private UUID id;
        private UUID courierId;


        private DeliveryStatus status;

        private OffsetDateTime placedAt;
        private OffsetDateTime assignedAt;
        private OffsetDateTime expectdDeliveryAt;
        private OffsetDateTime fulfilledAt;

        private BigDecimal distanceFee;
        private BigDecimal courierPayout;
        private BigDecimal totalCost;

        private Integer totalItems;


        @Embedded
        @AttributeOverride(
                {
                        @AttributeOverride(name = "zipCode", column = @Column(name = "sender_zip_code")),
                        @AttributeOverride(name = "street", column = @Column(name = "sender_street")),
                        @AttributeOverride(name = "number" , column = @Column(name ="sender_number")),
                        @AttributeOverride(name = "complement" , column = @Column(name ="sender_complement")),
                        @AttributeOverride(name = "name" , column = @Column(name ="sender_name")),
                        @AttributeOverride(name = "phone" , column = @Column(name ="sender_phone"))
                }
        )

        private ContactPoint sender;



        @Embedded
        @AttributeOverride(
                {
                        @AttributeOverride(name = "zipCode", column = @Column(name = "sender_zip_code")),
                        @AttributeOverride(name = "street", column = @Column(name = "sender_street")),
                        @AttributeOverride(name = "number" , column = @Column(name ="sender_number")),
                        @AttributeOverride(name = "complement" , column = @Column(name ="sender_complement")),
                        @AttributeOverride(name = "name" , column = @Column(name ="sender_name")),
                        @AttributeOverride(name = "phone" , column = @Column(name ="sender_phone"))
                }
        )

        private ContactPoint sender;



        private List<Item> items = new ArrayList<>();


        public static Delivery draft(){
                Delivery delivery = new Delivery();

                delivery.setId(UUID.randomUUID());
                delivery.setStatus(DeliveryStatus.DRAFT);
                delivery.setTotalItems(0);
                delivery.setTotalCost(BigDecimal.ZERO);
                delivery.setTotalCost(BigDecimal.ZERO);
                delivery.setDistanceFee(BigDecimal.ZERO);
                return delivery;

        }




        public UUID addItem(String name, int quantity)  {
                Item item = Item.brandNew(name, quantity, this);
                items.add(item);
                calculateTotalItems();
                return item.getId();
        }


        public void removeItem(UUID itemId){
                items.removeIf(item -> item.getId().equals(itemId));
                calculateTotalItems();
        }

        public void changeItemQuantity(UUID itemId, int quantity) {
                Item item = getItems().stream().filter(i -> i.getId().equals(itemId))
                        .findFirst().orElseThrow();


                item.setQuantity(quantity);
                calculateTotalItems();
        }




        public void removeItems(){

                items,clear();
                calculateTotalItems();

        }


        public void editPreparationDelails(PreparationDetails details){
                verifyIfCanBeEdited();

                setSender(details.getSender());
                setRecipient(details.getRecipient());
                setDistanceFee(details.getDistanceFee());
                setCourierPayout(details.getCourierPayout());
                setExpecteDeliveryAt(OffsetDateTime.now().plus(details.getExpectedDeliveyTime()));

                setTotalCost(this.getDistanceFee().add(this.getCourierPayout()));
        }



        public void place(){
                verifyIfCanBePlaced();
                this.changeStatusTo(DeliveryStatus.WAITING_FOR_COURIER);
                this.setPlacedAt(OffsetDateTime.now());
        }



}
