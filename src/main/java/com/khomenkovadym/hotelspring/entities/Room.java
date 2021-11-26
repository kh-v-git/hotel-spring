package com.khomenkovadym.hotelspring.entities;

import com.khomenkovadym.hotelspring.utils.RoomBedSize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "room")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Integer roomId;

    @Column(name = "number", unique = true)
    private Integer number;

    @Column(name = "adult_capacity")
    private Integer adultCapacity;

    @Column(name = "child_capacity")
    private Integer childCapacity;

    @Column(name = "price")
    private Double price;

    @Column(name = "bed_size")
    @Enumerated(EnumType.STRING)
    private RoomBedSize bedSize;

    @Column(name = "about")
    private String about;

}
