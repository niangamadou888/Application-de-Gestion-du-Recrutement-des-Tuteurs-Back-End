package com.project.backend.dao;

import com.project.backend.entity.AnneeAcademique;
import org.springframework.data.repository.CrudRepository;

public interface AnneeDAO extends CrudRepository<AnneeAcademique, String> {
}
