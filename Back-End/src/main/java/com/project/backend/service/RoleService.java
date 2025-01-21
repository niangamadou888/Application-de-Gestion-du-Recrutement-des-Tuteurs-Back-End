package com.project.backend.service;

import com.project.backend.dao.RoleDAO;
import com.project.backend.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    @Autowired
    private RoleDAO roleDAO;

    public Role createNewRole(Role role){
        return roleDAO.save(role);
    }
}
