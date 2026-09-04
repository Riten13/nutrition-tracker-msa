package com.example.userprofile.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "dietary_preferences")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DietaryPreference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean vegan;

    private Boolean vegetarian;

    private Boolean keto;

    private Boolean glutenFree;

    private Boolean dairyFree;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
