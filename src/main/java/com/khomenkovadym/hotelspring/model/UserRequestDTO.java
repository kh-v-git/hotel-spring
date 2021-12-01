package com.khomenkovadym.hotelspring.model;

import com.khomenkovadym.hotelspring.utils.RoomBedSize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Digits;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequestDTO {

    @Enumerated(EnumType.STRING)
    private RoomBedSize bedSize;

    @Digits(integer=1, fraction=0, message = "Not more them 1 digit")
    private Integer adultsCapacity;

    @Digits(integer=1, fraction=0, message = "Not more them 1 digit")
    private Integer childrenCapacity;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate arrivalDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate departureDate;
}
