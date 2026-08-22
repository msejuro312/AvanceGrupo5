package com.serfagab.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.serfagab.dto.LoginRequest;
import com.serfagab.entities.Usuario;
import com.serfagab.repository.UsuarioRepository;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Usuario usuario = usuarioRepository.findByLogin(loginRequest.getLogin()).orElse(null);
        if (usuario == null || !passwordEncoder.matches(loginRequest.getClave(), usuario.getClave())) {
            return ResponseEntity.status(401).body("Credenciales invalidas");
        }
        return ResponseEntity.ok(usuario);
    }
}
