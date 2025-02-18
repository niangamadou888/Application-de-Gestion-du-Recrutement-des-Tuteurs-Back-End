package com.project.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.backend.entity.Candidature;
import com.project.backend.entity.CandidatureStatusUpdateRequest;
import com.project.backend.service.CandidatureService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/candidatures")
public class CandidatureController {

    private final CandidatureService candidatureService;

    public CandidatureController(CandidatureService candidatureService) {
        this.candidatureService = candidatureService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('Admin', 'User')")
    public List<Candidature> getAllCandidatures() {
        return candidatureService.getAllCandidatures();
    }

    @PostMapping(value = "/soumettre", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Candidature> soumettreCandidature(
            @RequestPart("candidature") String candidatureJson,
            @RequestParam("userId") String userId,
            @RequestParam("annonceId") Long annonceId,
            @RequestPart(value = "cvFile", required = false) MultipartFile cvFile,
            @RequestPart(value = "lettreFile", required = false) MultipartFile lettreFile,
            @RequestPart(value = "diplomeFile", required = false) MultipartFile diplomeFile
    ) throws IOException {

        // Convert JSON String en Objet Candidature
        ObjectMapper objectMapper = new ObjectMapper();
        Candidature candidature = objectMapper.readValue(candidatureJson, Candidature.class);

        // Appel du service pour enregistrer la candidature
        Candidature savedCandidature = candidatureService.soumettreCandidature(userId, annonceId, candidature, cvFile, lettreFile, diplomeFile);

        return ResponseEntity.ok(savedCandidature);
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<Candidature> updateCandidatureStatus(
            @PathVariable Long id,
            @RequestBody CandidatureStatusUpdateRequest request) {

        Candidature updatedCandidature = candidatureService.updateCandidatureStatus(id, request.getStatut(), request.getMotifRefus());

        return ResponseEntity.ok(updatedCandidature);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<Void> deleteCandidature(@PathVariable Long id) {
        candidatureService.deleteCandidature(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/download/{type}/{id}")
    @PreAuthorize("hasAnyRole('Admin', 'User')")
    public ResponseEntity<byte[]> downloadFile(@PathVariable Long id, @PathVariable String type) throws IOException {
        Optional<Candidature> candidatureOptional = candidatureService.getCandidatureById(id);

        if (candidatureOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Candidature candidature = candidatureOptional.get();
        String filePath = switch (type) {
            case "cv" -> candidature.getCvPath();
            case "lettreMotivation" -> candidature.getLettreMotivationPath();
            case "diplome" -> candidature.getDiplomePath();
            default -> null;
        };

        if (filePath == null || filePath.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Path path = Path.of(filePath);
        byte[] fileContent = Files.readAllBytes(path);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + path.getFileName() + "\"")
                .header(HttpHeaders.CONTENT_TYPE, "application/pdf")
                .body(fileContent);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('Admin', 'User')")
    public ResponseEntity<Candidature> getCandidatureById(@PathVariable Long id) {
        Optional<Candidature> candidature = candidatureService.getCandidatureById(id);
        return candidature.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
