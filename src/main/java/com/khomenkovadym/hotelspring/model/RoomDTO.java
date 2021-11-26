package com.khomenkovadym.hotelspring.model;

import com.khomenkovadym.hotelspring.utils.RoomBedSize;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.NumberFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomDTO {

    @NumberFormat()
    private Integer roomId;

    @NotEmpty(message = "Room number can not be empty")
    @NumberFormat()
    private Integer number;

    @NotEmpty(message = "Capacity can not be empty")
    @Max(value = 10, message = "Large number")
    @NumberFormat()
    private Integer adultCapacity;

    @NotEmpty(message = "Capacity can not be empty")
    @Max(value = 10, message = "Large number")
    @NumberFormat()
    private Integer childCapacity;

    @NotEmpty(message = "Price can not be empty")
    @Max(value = 10000, message = "Large number")
    @NumberFormat()
    private Double price;

    @NotEmpty(message = "Price can not be empty")
    @Length(max = 10)
    private RoomBedSize bedSize;

    @NotEmpty(message = "Price can not be empty")
    @Length(max = 50)
    private String about;

}
