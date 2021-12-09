package com.khomenkovadym.hotelspring.model;

import com.khomenkovadym.hotelspring.utils.RoomBedSize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class UserRequestDTO implements Serializable {

    @Enumerated(EnumType.STRING)
    private RoomBedSize bedSize;

    @Digits(integer=1, fraction=0, message = "Not more them 1 digit")
    @Min(value = 1, message = "Capacity should not be less than 1")
    @Max(value = 9, message = "Capacity should not be greater than 9")
    private Integer adultsCapacity;

    @Digits(integer=1, fraction=0, message = "Not more them 1 digit")
    @Min(value = 0, message = "Capacity should not be less than 0")
    @Max(value = 9, message = "Capacity should not be greater than 9")
    private Integer childrenCapacity;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @FutureOrPresent
    private LocalDate arrivalDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Future
    private LocalDate departureDate;
}
