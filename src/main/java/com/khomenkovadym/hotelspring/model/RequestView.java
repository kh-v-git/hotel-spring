package com.khomenkovadym.hotelspring.model;

import com.khomenkovadym.hotelspring.utils.RoomBedSize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestView {
    @NotEmpty(message = "Bed size can not be empty")
    @Length(max = 20)
    private RoomBedSize bedSize;

    @NotEmpty(message = "Capacity can not be empty")
    @Pattern(regexp = "^[0-9]*$")
    private Integer adultsCapacity;

    @NotEmpty(message = "Capacity can not be empty")
    @Pattern(regexp = "^[0-9]*$")
    private Integer childrenCapacity;

    @NotEmpty(message = "Date can not be empty")
    //@DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate arrivalDate;

    @NotEmpty(message = "Date can not be empty")
    //@DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate departureDate;
}
