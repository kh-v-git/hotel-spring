package com.khomenkovadym.hotelspring.model;

import com.khomenkovadym.hotelspring.utils.RoomBedSize;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomDTO {

    @Digits(integer = 10, fraction = 0)
    private Integer roomId;

    @NotNull(message = "Room number can not be empty")
    @Digits(integer = 10, fraction = 0)
    private Integer number;

    @NotNull(message = "Capacity can not be empty")
    @Max(value = 10, message = "Large number")
    @Digits(integer = 2, fraction = 0)
    private Integer adultCapacity;

    @NotNull(message = "Capacity can not be empty")
    @Max(value = 10, message = "Large number")
    @Digits(integer = 2, fraction = 0)
    private Integer childCapacity;

    @NotNull(message = "Price can not be empty")
    @Max(value = 10000, message = "Large number")
    @Digits(integer = 5, fraction = 2)
    private Double price;

    @Enumerated(EnumType.STRING)
    private RoomBedSize bedSize;

    @Length(max = 50)
    private String about;

}
