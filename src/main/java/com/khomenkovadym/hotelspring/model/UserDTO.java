package com.khomenkovadym.hotelspring.model;

import com.khomenkovadym.hotelspring.utils.UserRoleEnum;
import com.khomenkovadym.hotelspring.utils.UserStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Integer userId;

    @NotEmpty(message = "First name can not be empty")
    @Pattern(regexp = "^[A-Za-z\u0400-\u04ff]{1,32}$", message = "Must contain max 32 letters only")
    private String firstName;

    @NotEmpty(message = "Last name can not be empty")
    @Pattern(regexp = "^[A-Za-z\u0400-\u04ff]{1,32}$", message = "Must contain max 32 letters only")
    private String lastName;

    @NotEmpty(message = "Phone can not be empty")
    @Pattern(regexp = "^[0-9]{12}$", message = "Must contain 12 digits")
    private String phone;

    @NotEmpty(message = "Email can not be empty")
    @Email(message = "Please provide a valid email id")
    private String email;

    @Enumerated(EnumType.STRING)
    private UserRoleEnum role;

    @Enumerated(EnumType.STRING)
    private UserStatusEnum status;

    @NotEmpty(message = "About can not be empty")
    @Length(max = 12, message = "About can not be empty. Max 12 symbols")
    private String about;
}
