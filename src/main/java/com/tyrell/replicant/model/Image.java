package com.tyrell.replicant.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;

// TODO add co-pilot shortcuts to README.md - https://docs.github.com/en/copilot/getting-started-with-github-copilot?tool=jetbrains
// TODO add spring boot starter data jpa dependency to build.gradle
// which is best java.x.persistence or spring boot starter data jpa?
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Image {
    @Id
    @GeneratedValue
    private Long id;
    private String title;
    private String description;
    private String imagePath;
    private String imageFileName;

}
