package com.feedback.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class SwitcherDto {
    private String description;
    private boolean isActive;
}
