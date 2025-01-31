package com.project.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "annee_academique")
@Data
@NoArgsConstructor
public class AnneeAcademique {

    @Setter
    @Getter
    @Id
    @Column(unique = true, nullable = false)
    private String annee; // Ex: "2023-2024"

    @OneToMany(mappedBy = "anneeAcademique", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Annonce> annonces;

    public AnneeAcademique(String annee) {
        this.annee = annee;
    }

}
