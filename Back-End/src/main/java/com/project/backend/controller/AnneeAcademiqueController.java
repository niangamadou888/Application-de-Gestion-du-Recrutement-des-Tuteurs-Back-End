package com.project.backend.controller;

import com.project.backend.entity.AnneeAcademique;
import com.project.backend.service.AnneeAcademiqueService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/annee-academique")
public class AnneeAcademiqueController {
    private final AnneeAcademiqueService anneeAcademiqueService;

    public AnneeAcademiqueController(AnneeAcademiqueService anneeAcademiqueService) {
        this.anneeAcademiqueService = anneeAcademiqueService;
    }

    @GetMapping
    @PreAuthorize("hasRole('Admin')")
    public List<AnneeAcademique> getAllAnnees() {
        return anneeAcademiqueService.getAllAnnees();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<AnneeAcademique> getAnneeById(@PathVariable String id) {
        return anneeAcademiqueService.getAnneeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('Admin')")
    public AnneeAcademique createAnnee(@RequestBody AnneeAcademique annee) {
        return anneeAcademiqueService.saveAnnee(annee);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<Void> deleteAnnee(@PathVariable String id) {
        anneeAcademiqueService.deleteAnnee(id);
        return ResponseEntity.noContent().build();
    }
}
