package com.project.backend.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class CandidatureStatusUpdateRequest {
    private String statut;
    private String motifRefus;
}
