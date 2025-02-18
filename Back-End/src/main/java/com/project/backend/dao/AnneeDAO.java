package com.project.backend.dao;

import com.project.backend.entity.AnneeAcademique;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnneeDAO extends CrudRepository<AnneeAcademique, String> {
}
