package com.example.userprofile.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AllergyDto {
    private Long id;

    @NotBlank(message = "Allergy name is required")
    private String name;
}
