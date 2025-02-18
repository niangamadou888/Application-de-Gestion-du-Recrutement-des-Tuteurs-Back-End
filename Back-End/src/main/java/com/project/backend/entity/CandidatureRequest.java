package com.project.backend.entity;

import lombok.Data;

@Data
public class CandidatureRequest {

    private String nomComplet;
    private String email;
    private String telephone;
    private String adresse;
    private String niveauEtude;
    private String domaineSpecialisation;
    private String experiencePro;
    private String universiteFrequente;
    private Long nbHeureDisponible;
}
