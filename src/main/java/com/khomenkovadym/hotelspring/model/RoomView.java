package com.khomenkovadym.hotelspring.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomView {
    private String bedSize;
    private double minPrice;
    private double maxPrice;
    private int maxAdults;
    private int minAdults;
    private int maxChildren;
    private int minChildren;
}
