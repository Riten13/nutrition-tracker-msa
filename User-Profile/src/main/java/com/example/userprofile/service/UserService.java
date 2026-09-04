package com.example.userprofile.service;

import com.example.userprofile.dto.*;
import com.example.userprofile.entity.Allergy;
import com.example.userprofile.entity.DietaryPreference;
import com.example.userprofile.entity.Profile;
import com.example.userprofile.entity.User;
import com.example.userprofile.exception.BadRequestException;
import com.example.userprofile.exception.ResourceNotFoundException;
import com.example.userprofile.exception.UnauthorizedAccessException;
import com.example.userprofile.repository.AllergyRepository;
import com.example.userprofile.repository.DietaryPreferenceRepository;
import com.example.userprofile.repository.ProfileRepository;
import com.example.userprofile.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final AllergyRepository allergyRepository;
    private final DietaryPreferenceRepository preferenceRepository;

    private User getAuthenticatedUserAndVerifyOwnership(Long userId, String authEmail) {
        User user = userRepository.findByEmail(authEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found"));

        if (!user.getId().equals(userId)) {
            throw new UnauthorizedAccessException("Access Denied: You are not authorized to access user ID " + userId);
        }
        return user;
    }

    @Transactional(readOnly = true)
    public UserProfileResponse getUserProfile(Long userId, String authEmail) {
        User user = getAuthenticatedUserAndVerifyOwnership(userId, authEmail);

        ProfileDto profileDto = user.getProfile() != null ? ProfileDto.builder()
                .age(user.getProfile().getAge())
                .weight(user.getProfile().getWeight())
                .height(user.getProfile().getHeight())
                .activityLevel(user.getProfile().getActivityLevel())
                .build() : null;

        List<AllergyDto> allergyDtos = user.getAllergies() != null ? user.getAllergies().stream()
                .map(a -> AllergyDto.builder().id(a.getId()).name(a.getName()).build())
                .collect(Collectors.toList()) : Collections.emptyList();

        DietaryPreferenceDto prefDto = user.getDietaryPreference() != null ? DietaryPreferenceDto.builder()
                .vegan(user.getDietaryPreference().getVegan())
                .vegetarian(user.getDietaryPreference().getVegetarian())
                .keto(user.getDietaryPreference().getKeto())
                .glutenFree(user.getDietaryPreference().getGlutenFree())
                .dairyFree(user.getDietaryPreference().getDairyFree())
                .build() : null;

        return UserProfileResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .profile(profileDto)
                .allergies(allergyDtos)
                .dietaryPreference(prefDto)
                .build();
    }

    @Transactional
    public ProfileDto updateProfile(Long userId, ProfileDto profileDto, String authEmail) {
        User user = getAuthenticatedUserAndVerifyOwnership(userId, authEmail);

        Profile profile = user.getProfile();
        if (profile == null) {
            profile = Profile.builder().user(user).build();
            user.setProfile(profile);
        }

        profile.setAge(profileDto.getAge());
        profile.setWeight(profileDto.getWeight());
        profile.setHeight(profileDto.getHeight());
        profile.setActivityLevel(profileDto.getActivityLevel());

        Profile savedProfile = profileRepository.save(profile);

        return ProfileDto.builder()
                .age(savedProfile.getAge())
                .weight(savedProfile.getWeight())
                .height(savedProfile.getHeight())
                .activityLevel(savedProfile.getActivityLevel())
                .build();
    }

    @Transactional
    public AllergyDto addAllergy(Long userId, AllergyDto allergyDto, String authEmail) {
        User user = getAuthenticatedUserAndVerifyOwnership(userId, authEmail);

        Allergy allergy = Allergy.builder()
                .name(allergyDto.getName())
                .user(user)
                .build();

        Allergy savedAllergy = allergyRepository.save(allergy);

        return AllergyDto.builder()
                .id(savedAllergy.getId())
                .name(savedAllergy.getName())
                .build();
    }

    @Transactional(readOnly = true)
    public List<AllergyDto> getAllergies(Long userId, String authEmail) {
        getAuthenticatedUserAndVerifyOwnership(userId, authEmail);

        return allergyRepository.findByUserId(userId).stream()
                .map(a -> AllergyDto.builder().id(a.getId()).name(a.getName()).build())
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteAllergy(Long userId, Long allergyId, String authEmail) {
        getAuthenticatedUserAndVerifyOwnership(userId, authEmail);

        Allergy allergy = allergyRepository.findById(allergyId)
                .orElseThrow(() -> new ResourceNotFoundException("Allergy not found with ID: " + allergyId));

        if (!allergy.getUser().getId().equals(userId)) {
            throw new BadRequestException("Allergy ID " + allergyId + " does not belong to user ID " + userId);
        }

        allergyRepository.delete(allergy);
    }

    @Transactional
    public DietaryPreferenceDto updateDietaryPreference(Long userId, DietaryPreferenceDto preferenceDto, String authEmail) {
        User user = getAuthenticatedUserAndVerifyOwnership(userId, authEmail);

        DietaryPreference preference = user.getDietaryPreference();
        if (preference == null) {
            preference = DietaryPreference.builder().user(user).build();
            user.setDietaryPreference(preference);
        }

        preference.setVegan(preferenceDto.getVegan());
        preference.setVegetarian(preferenceDto.getVegetarian());
        preference.setKeto(preferenceDto.getKeto());
        preference.setGlutenFree(preferenceDto.getGlutenFree());
        preference.setDairyFree(preferenceDto.getDairyFree());

        DietaryPreference saved = preferenceRepository.save(preference);

        return DietaryPreferenceDto.builder()
                .vegan(saved.getVegan())
                .vegetarian(saved.getVegetarian())
                .keto(saved.getKeto())
                .glutenFree(saved.getGlutenFree())
                .dairyFree(saved.getDairyFree())
                .build();
    }
}
