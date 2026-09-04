package com.example.userprofile.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "Payload for registering a new user including full profile details")
public class RegisterRequest {

    // Core fields
    @NotBlank(message = "Name is required")
    @Schema(description = "Full name of the user", example = "Jane Doe")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Schema(description = "Email address for login", example = "jane.doe@example.com")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    @Schema(description = "Password (min 6 characters)", example = "secureP@ss123")
    private String password;

    // Profile fields
    @Positive(message = "Age must be positive")
    @Schema(description = "Age in years", example = "28")
    private Integer age;

    @Positive(message = "Weight must be positive")
    @Schema(description = "Weight in kilograms", example = "65.5")
    private Double weight;

    @Positive(message = "Height must be positive")
    @Schema(description = "Height in centimeters", example = "170.0")
    private Double height;

    @Schema(description = "Activity level of the user (e.g., Sedentary, Active)", example = "Active")
    private String activityLevel;

    // Dietary preferences
    @Schema(description = "Is the user vegan?", example = "true")
    private Boolean vegan;
    @Schema(description = "Is the user vegetarian?", example = "false")
    private Boolean vegetarian;
    @Schema(description = "Is the user keto?", example = "false")
    private Boolean keto;
    @Schema(description = "Is the user gluten-free?", example = "true")
    private Boolean glutenFree;
    @Schema(description = "Is the user dairy-free?", example = "false")
    private Boolean dairyFree;

    // Allergies
    @Schema(description = "List of allergy names", example = "[\"Peanuts\", \"Shellfish\"]")
    private List<@NotBlank(message = "Allergy name cannot be blank") String> allergies;
}
