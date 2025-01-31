package com.project.backend.service;

import com.project.backend.dao.AnneeDAO;
import com.project.backend.entity.AnneeAcademique;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AnneeAcademiqueService {

    private final AnneeDAO anneeDAO;

    public AnneeAcademiqueService(AnneeDAO anneeDAO) {
        this.anneeDAO = anneeDAO;
    }

    public List<AnneeAcademique> getAllAnnees() {
        return (List<AnneeAcademique>) anneeDAO.findAll();
    }

    public Optional<AnneeAcademique> getAnneeById(String id) {
        return anneeDAO.findById(id);
    }

    public AnneeAcademique saveAnnee(AnneeAcademique annee) {
        return anneeDAO.save(annee);
    }

    public void deleteAnnee(String id) {
        anneeDAO.deleteById(id);
    }
}
