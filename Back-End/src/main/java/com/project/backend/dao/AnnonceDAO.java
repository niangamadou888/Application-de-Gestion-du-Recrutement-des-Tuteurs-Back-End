package com.project.backend.dao;

import com.project.backend.entity.Annonce;
import org.springframework.data.repository.CrudRepository;

public interface AnnonceDAO extends CrudRepository<Annonce, Long> {
}
