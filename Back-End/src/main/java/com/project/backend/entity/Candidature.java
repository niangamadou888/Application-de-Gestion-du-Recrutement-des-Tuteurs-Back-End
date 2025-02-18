package com.project.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "candidatures")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Candidature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomComplet;

    private String statut; // "En cours de traitement", "Acceptée", "Refusée"

    private String motifRefus; // Raison du refus si refusée

    private Date dateSoumission;

    private String email;

    private String telephone;

    private Date dateNaissance;

    private String adresse;

    private String niveauEtude;

    private String domaineSpecialisation;

    private String experiencePro;

    private String universiteFrequente;

    private Long nbHeureDisponible;


    private String cvPath;
    private String lettreMotivationPath;
    private String diplomePath;

    @ManyToOne
    @JoinColumn(name = "candidat_id")
    private User candidat;

    @ManyToOne
    @JoinColumn(name = "annonce_id")
    private Annonce annonce;
}
