package com.feedback.dto;

import com.feedback.repo.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class BriefUserDto {
    private Long id;
    private String firstName;
    private String lastName;

    public static BriefUserDto map(User user) {
        return BriefUserDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }
}
