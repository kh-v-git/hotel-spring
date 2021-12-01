package com.khomenkovadym.hotelspring.entities;

import com.khomenkovadym.hotelspring.utils.RoomOrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "room_order")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_order_id")
    private Integer orderId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @ManyToOne
    @JoinColumn(name = "room_request_id")
    private RoomRequest roomRequest;

    @Column(name = "order_status")
    @Enumerated(EnumType.STRING)
    private RoomOrderStatus status;

    @Column(name = "order_date")
    private LocalDateTime orderDate;

    @Column(name = "arrival_date")
    private LocalDate arrivalDate;

    @Column(name = "departure_date")
    private LocalDate departureDate;

    @Column(name = "price")
    private Double price;
}
