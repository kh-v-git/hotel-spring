package com.khomenkovadym.hotelspring.model;

import com.khomenkovadym.hotelspring.common.FieldMatch;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
    @FieldMatch.List({
            @FieldMatch(first = "password", second = "confirmPassword", message = "The password fields must match")
    })
public class UserRegistrationDTO {

    @NotEmpty(message = "First name can not be empty")
    private String firstName;

    @NotEmpty(message = "Last name can not be empty")
    @Length(min = 1 ,max = 12)
    private String lastName;

    @NotEmpty(message = "Phone can not be empty")
    @Pattern(regexp = "^[0-9]{12}$")
    private String phone;

    @NotEmpty(message = "Email can not be empty")
    @Email(message = "Please provide a valid email id")
    private String email;

    @NotEmpty(message = "Password can not be empty")
    //@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$")
    private String password;

    @NotEmpty(message = "Password can not be empty")
   // @Pattern(regexp = "^(.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$")
    private String confirmPassword;

    @NotEmpty(message = "About can not be empty")
    @Length(max = 12)
    private String about;
}
