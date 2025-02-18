package com.project.backend.service;

import com.project.backend.dao.AnnonceDAO;
import com.project.backend.dao.CandidatureDAO;
import com.project.backend.dao.UserDao;
import com.project.backend.entity.Annonce;
import com.project.backend.entity.Candidature;
import com.project.backend.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CandidatureService {

    private final CandidatureDAO candidatureRepository;
    private final UserDao userRepository;
    private final AnnonceDAO annonceRepository;

    private static final String UPLOAD_DIR = "uploads/candidatures/";

    public CandidatureService(CandidatureDAO candidatureRepository,
                              UserDao userRepository,
                              AnnonceDAO annonceRepository) {
        this.candidatureRepository = candidatureRepository;
        this.userRepository = userRepository;
        this.annonceRepository = annonceRepository;
    }

    public List<Candidature> getAllCandidatures() {
        return candidatureRepository.findAll();
    }

    public Optional<Candidature> getCandidatureById(Long id) {
        return candidatureRepository.findById(id);
    }

    public Candidature soumettreCandidature(String userId, Long annonceId, Candidature candidature,
                                            MultipartFile cvFile, MultipartFile lettreFile, MultipartFile diplomeFile) throws IOException {
        User candidat = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Candidat non trouvé"));
        Annonce annonce = annonceRepository.findById(annonceId)
                .orElseThrow(() -> new RuntimeException("Annonce non trouvée"));

        candidature.setCandidat(candidat);
        candidature.setAnnonce(annonce);
        candidature.setStatut("En cours de traitement");
        candidature.setDateSoumission(new Date());

        // Save Files
        if (cvFile != null && !cvFile.isEmpty()) {
            candidature.setCvPath(saveFile(cvFile, userId, "CV"));
        }
        if (lettreFile != null && !lettreFile.isEmpty()) {
            candidature.setLettreMotivationPath(saveFile(lettreFile, userId, "LettreMotivation"));
        }
        if (diplomeFile != null && !diplomeFile.isEmpty()) {
            candidature.setDiplomePath(saveFile(diplomeFile, userId, "Diplome"));
        }

        return candidatureRepository.save(candidature);
    }

    private String saveFile(MultipartFile file, String userId, String type) throws IOException {
        String filename = userId + "_" + type + "_" + System.currentTimeMillis() + ".pdf";
        Path filePath = Paths.get(UPLOAD_DIR + filename);

        Files.createDirectories(filePath.getParent());
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return filePath.toString();
    }

    @Transactional
    public synchronized Candidature updateCandidatureStatus(Long id, String statut, String motifRefus) {
        Candidature candidature = candidatureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Candidature non trouvée"));

        candidature.setStatut(statut);
        if ("Refusée".equalsIgnoreCase(statut) && motifRefus != null) {
            candidature.setMotifRefus(motifRefus);
        } else {
            candidature.setMotifRefus(null); // Réinitialiser le motif en cas d'acceptation
        }

        return candidatureRepository.save(candidature);
    }

    public void deleteCandidature(Long id) {
        Optional<Candidature> candidatureOptional = candidatureRepository.findById(id);
        if (candidatureOptional.isEmpty()) {
            throw new RuntimeException("Candidature non trouvée");
        }

        // Supprimer les fichiers associés
        Candidature candidature = candidatureOptional.get();
        deleteFile(candidature.getCvPath());
        deleteFile(candidature.getLettreMotivationPath());
        deleteFile(candidature.getDiplomePath());

        candidatureRepository.deleteById(id);
    }

    private void deleteFile(String filePath) {
        if (filePath != null && !filePath.isEmpty()) {
            try {
                Files.deleteIfExists(Path.of(filePath));
            } catch (IOException e) {
                throw new RuntimeException("Erreur lors de la suppression du fichier: " + filePath);
            }
        }
    }
}
