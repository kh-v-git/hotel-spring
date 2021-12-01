package com.khomenkovadym.hotelspring.model;

import com.khomenkovadym.hotelspring.utils.RoomOrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomOrderDTO {
    @NotNull
    private Integer orderId;

    @NotNull
    private Integer userId;

    @NotNull
    private Integer roomId;


    private Integer requestId;

    @NotNull
    private RoomOrderStatus status;

    @NotNull(message = "Date can not be empty")
    private LocalDate arrivalDate;

    @NotNull(message = "Date can not be empty")
    private LocalDate departureDate;

    @NotNull
    private LocalDateTime orderDate;

    @NotNull
    private Double price;

}
