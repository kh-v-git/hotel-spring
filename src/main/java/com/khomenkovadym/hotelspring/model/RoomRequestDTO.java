package com.khomenkovadym.hotelspring.model;

import com.khomenkovadym.hotelspring.utils.RoomBedSize;
import com.khomenkovadym.hotelspring.utils.RoomRequestStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomRequestDTO {
    private Integer requestId;

    @NotEmpty(message = "Status can not be empty")
    private Integer userId;

    @NotEmpty(message = "Status can not be empty")
    private RoomRequestStatus status;

    @NotEmpty(message = "Bed size can not be empty")
    private RoomBedSize bedSize;

    @NotEmpty(message = "Capacity can not be empty")
    private Integer adultsCapacity;

    @NotEmpty(message = "Capacity can not be empty")
    private Integer childrenCapacity;

    @NotEmpty(message = "Date can not be empty")
    private LocalDate arrivalDate;

    @NotEmpty(message = "Date can not be empty")
    private LocalDate departureDate;
}
