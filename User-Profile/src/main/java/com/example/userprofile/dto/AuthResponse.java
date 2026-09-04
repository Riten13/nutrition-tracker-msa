package com.example.userprofile.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Response containing the JWT token and the complete user profile")
public class AuthResponse {
    @Schema(description = "JWT Bearer token for authentication")
    private String token;
    
    @Builder.Default
    @Schema(description = "Token type", example = "Bearer")
    private String type = "Bearer";
    
    @Schema(description = "The fully populated user profile")
    private UserProfileResponse user;
}
