package com.khomenkovadym.hotelspring.entities;

import com.khomenkovadym.hotelspring.utils.RoomBedSize;
import com.khomenkovadym.hotelspring.utils.RoomRequestStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "room_request")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_request_id")
    private Integer requestId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "status")
    private RoomRequestStatus status;

    @Column(name = "bed_size")
    private RoomBedSize bedSize;

    @Column(name = "adult_capacity")
    private Integer adultsCapacity;

    @Column(name = "child_capacity")
    private Integer childrenCapacity;

    @Column(name = "arrival_date")
    private LocalDate arrivalDate;

    @Column(name = "departure_date")
    private LocalDate departureDate;

}
