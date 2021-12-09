package com.khomenkovadym.hotelspring.model;

import com.khomenkovadym.hotelspring.utils.RoomBedSize;
import com.khomenkovadym.hotelspring.utils.RoomRequestStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomRequestDTO {
    @NotNull(message = "ID can not be empty")
    private Integer requestId;

    private Integer roomID;

    @NotNull
    private Integer userId;

    @NotNull
    @Enumerated(EnumType.STRING)
    private RoomRequestStatus status;

    @NotNull
    @Enumerated(EnumType.STRING)
    private RoomBedSize bedSize;

    @NotNull(message = "Capacity can not be empty")
    private Integer adultsCapacity;

    @NotNull(message = "Capacity can not be empty")
    private Integer childrenCapacity;

    @NotNull(message = "Date can not be empty")
    private LocalDate arrivalDate;

    @NotNull(message = "Date can not be empty")
    private LocalDate departureDate;
}
