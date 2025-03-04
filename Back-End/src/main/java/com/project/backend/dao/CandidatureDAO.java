package com.project.backend.dao;

import com.project.backend.entity.Annonce;
import com.project.backend.entity.Candidature;
import com.project.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CandidatureDAO extends JpaRepository<Candidature, Long> {
    List<Candidature> findByCandidat_UserEmail(String userEmail);
}
