package com.feedback.dto;

import com.feedback.repo.entity.Role;
import com.feedback.repo.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private Long id;
    @NotBlank(message = "Name shouldn't be empty")
    private String firstName;
    @NotBlank(message = "Lastname shouldn't be empty")
    private String lastName;
    @Email(message = "Please enter valid email")
    private String email;
    @NotBlank(message = "Password shouldn't be empty")
    private String password;
    @Pattern(regexp = "^\\+38\\(0[0-9]{2}\\)[0-9]{3}\\-[0-9]{4}$", message = "Phone Number should have format +38(0XX)XXX-XXXX")
    private String phoneNumber;
    private String role;
    private boolean active;

    public static User map(UserDto userDto, String password) {
        return User.builder()
                .id(userDto.getId())
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .email(userDto.getEmail())
                .password(password)
                .phoneNumber(userDto.getPhoneNumber())
                .role(Role.valueOf(userDto.getRole()))
                .build();
    }

    public static UserDto map(User user) {
        return UserDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .role(String.valueOf(user.getRole()))
                .active(user.isActive())
                .build();
    }
}
