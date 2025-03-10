package com.project.backend.controller;

import com.project.backend.dao.UserDao;
import com.project.backend.entity.JwtRequest;
import com.project.backend.entity.JwtResponse;
import com.project.backend.entity.User;
import com.project.backend.service.EmailService;
import com.project.backend.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@CrossOrigin
public class JwtController {

    @Lazy
    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Lazy
    @Autowired
    private EmailService emailService;

    @Autowired
    private UserDao userDao;

    @PostMapping({"/authenticate"})
    public JwtResponse createJwtToken(@RequestBody JwtRequest jwtRequest) throws Exception {
        return jwtService.createJwtToken(jwtRequest);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, String>> forgotPassword(@RequestBody Map<String, String> request) {
        String userEmail = request.get("userEmail");
        User user = userDao.findByUserEmail(userEmail).orElseThrow(() -> new RuntimeException("User not found"));

        // Générer un token unique
        String resetToken = UUID.randomUUID().toString();
        user.setResetToken(resetToken);
        userDao.save(user);

        // Envoyer l'email avec un lien contenant le token
        String resetLink = "https://recrutementtuteur.netlify.app/modifier-mdp/" + resetToken;
        emailService.sendEmail(user.getUserEmail(), "Réinitialisation de votre mot de passe",
                "Cliquez sur ce lien pour réinitialiser votre mot de passe : " + resetLink);

        // Return a JSON response with a proper Map
        Map<String, String> response = new HashMap<>();
        response.put("message", "Email de réinitialisation envoyé.");

        return ResponseEntity.ok(response); // Return as JSON automatically serialized
    }


    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(
            @RequestParam("token") String token,
            @RequestBody Map<String, String> request) {

        String newPassword = request.get("newPassword");

        List<User> users = userDao.findByResetToken(token);
        if (users.isEmpty()) {
            // Invalid token, return a JSON response with an error message
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Token invalide.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } else if (users.size() > 1) {
            // Multiple users found with the same token, return an error message
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Problème de sécurité : Token en double.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }

        User user = users.get(0);

        // Hash the password before saving
        user.setUserPassword(getEncodedPassword(newPassword));
        user.setResetToken(null); // Remove the token after use
        userDao.save(user);

        // Successful password reset response
        Map<String, String> successResponse = new HashMap<>();
        successResponse.put("message", "Mot de passe réinitialisé avec succès.");
        return ResponseEntity.ok(successResponse);
    }

    public String getEncodedPassword(String password) {
        return passwordEncoder.encode(password);
    }
}
