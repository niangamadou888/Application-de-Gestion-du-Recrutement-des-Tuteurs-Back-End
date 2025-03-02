package com.project.backend.service;

import com.project.backend.dao.RoleDAO;
import com.project.backend.dao.UserDao;
import com.project.backend.entity.Role;
import com.project.backend.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDAO roleDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerNewUser(User user) {
        Role role = roleDao.findById("User").get();

        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRole(roles);

        user.setUserPassword(getEncodedPassword(user.getUserPassword()));
        return userDao.save(user);
    }

    // Method to update user information (only firstName, lastName, and email)
    public User updateUserInfo(User updatedUser, String userEmail) {
        Optional<User> userOptional = userDao.findById(userEmail);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Update the fields
            if (updatedUser.getUserFirstName() != null) {
                user.setUserFirstName(updatedUser.getUserFirstName());
            }
            if (updatedUser.getUserLastName() != null) {
                user.setUserLastName(updatedUser.getUserLastName());
            }
            if (updatedUser.getUserEmail() != null) {
                user.setUserEmail(updatedUser.getUserEmail());
            }

            return userDao.save(user);
        } else {
            throw new RuntimeException("User not found with email: " + userEmail);
        }
    }

    public void initRolesAndUser(){
        Role adminRole = new Role();
        adminRole.setRoleName("Admin");
        adminRole.setRoleDescription("Admin role");
        roleDao.save(adminRole);

        Role userRole = new Role();
        userRole.setRoleName("User");
        userRole.setRoleDescription("Default role for newly create record");
        roleDao.save(userRole);

        User adminUser = new User();
        adminUser.setUserFirstName("admin");
        adminUser.setUserLastName("admin");
        adminUser.setUserEmail("admin@admin.com");
        adminUser.setUserPassword(getEncodedPassword("admin123"));
        Set<Role> adminRoles = new HashSet<>();
        adminRoles.add(adminRole);
        adminUser.setRole(adminRoles);
        userDao.save(adminUser);

        /*
        User user = new User();
        user.setUserFirstName("user");
        user.setUserLastName("user");
        user.setUserEmail("user@user.com");
        user.setUserPassword(getEncodedPassword("user123"));
        Set<Role> userRoles = new HashSet<>();
        userRoles.add(userRole);
        user.setRole(userRoles);
        userDao.save(user);

         */
    }

    public String getEncodedPassword(String password) {
        return passwordEncoder.encode(password);
    }
}
