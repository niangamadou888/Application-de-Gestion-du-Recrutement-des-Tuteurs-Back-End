package com.project.backend.service;

import com.project.backend.dao.AnnonceDAO;
import com.project.backend.entity.Annonce;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AnnonceService {

    private final AnnonceDAO annonceDAO;

    public AnnonceService(AnnonceDAO annonceDAO) {
        this.annonceDAO = annonceDAO;
    }

    public List<Annonce> getAllAnnonces() {
        return (List<Annonce>) annonceDAO.findAll();
    }

    public Optional<Annonce> getAnnonceById(Long id) {
        return annonceDAO.findById(id);
    }

    public Annonce saveAnnonce(Annonce annonce) {
        return annonceDAO.save(annonce);
    }

    public void deleteAnnonce(Long id) {
        annonceDAO.deleteById(id);
    }
}
