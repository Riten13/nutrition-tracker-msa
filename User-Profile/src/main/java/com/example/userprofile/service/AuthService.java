package com.example.userprofile.service;

import com.example.userprofile.dto.AuthResponse;
import com.example.userprofile.dto.LoginRequest;
import com.example.userprofile.dto.RegisterRequest;
import com.example.userprofile.dto.UserProfileResponse;
import com.example.userprofile.entity.Allergy;
import com.example.userprofile.entity.DietaryPreference;
import com.example.userprofile.entity.Profile;
import com.example.userprofile.entity.User;
import com.example.userprofile.exception.BadRequestException;
import com.example.userprofile.repository.UserRepository;
import com.example.userprofile.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserService userService;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email is already in use: " + request.getEmail());
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        // Create profile from request
        Profile profile = Profile.builder()
                .user(user)
                .age(request.getAge())
                .weight(request.getWeight())
                .height(request.getHeight())
                .activityLevel(request.getActivityLevel())
                .build();
        user.setProfile(profile);

        // Create dietary preferences from request
        DietaryPreference preference = DietaryPreference.builder()
                .vegan(request.getVegan() != null ? request.getVegan() : false)
                .vegetarian(request.getVegetarian() != null ? request.getVegetarian() : false)
                .keto(request.getKeto() != null ? request.getKeto() : false)
                .glutenFree(request.getGlutenFree() != null ? request.getGlutenFree() : false)
                .dairyFree(request.getDairyFree() != null ? request.getDairyFree() : false)
                .user(user)
                .build();
        user.setDietaryPreference(preference);

        // Create allergies from request
        if (request.getAllergies() != null && !request.getAllergies().isEmpty()) {
            List<Allergy> allergyList = new ArrayList<>();
            for (String allergyName : request.getAllergies()) {
                Allergy allergy = Allergy.builder()
                        .name(allergyName)
                        .user(user)
                        .build();
                allergyList.add(allergy);
            }
            user.setAllergies(allergyList);
        }

        User savedUser = userRepository.save(user);

        String token = jwtUtils.generateTokenFromEmail(savedUser.getEmail());
        
        UserProfileResponse userProfile = userService.getUserProfile(savedUser.getId(), savedUser.getEmail());

        return AuthResponse.builder()
                .token(token)
                .type("Bearer")
                .user(userProfile)
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtUtils.generateToken(userDetails);

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadRequestException("User not found"));

        UserProfileResponse userProfile = userService.getUserProfile(user.getId(), user.getEmail());

        return AuthResponse.builder()
                .token(token)
                .type("Bearer")
                .user(userProfile)
                .build();
    }
}
