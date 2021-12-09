package com.khomenkovadym.hotelspring.utils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum UserRoleEnum {
    USER,
    MANAGER,
    ADMIN;

    public static List<String> getUserRoleList () {
        return  Arrays.stream(UserRoleEnum.values())
                .map(UserRoleEnum::name)
                .collect(Collectors.toList());
    }
}
