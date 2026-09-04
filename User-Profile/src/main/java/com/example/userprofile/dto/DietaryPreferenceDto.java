package com.example.userprofile.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DietaryPreferenceDto {
    private Boolean vegan;
    private Boolean vegetarian;
    private Boolean keto;
    private Boolean glutenFree;
    private Boolean dairyFree;
}
