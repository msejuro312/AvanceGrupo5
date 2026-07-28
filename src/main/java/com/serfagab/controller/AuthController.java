package com.serfagab.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.serfagab.dto.LoginRequest;
import com.serfagab.entities.Usuario;
import com.serfagab.repository.UsuarioRepository;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UsuarioRepository usuarioRepository;

    public AuthController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Usuario usuario = usuarioRepository.findByLoginAndClave(loginRequest.getLogin(), loginRequest.getClave()).orElse(null);
        if (usuario == null) {
            return ResponseEntity.status(401).body("Credenciales invalidas");
        }
        return ResponseEntity.ok(usuario);
    }
}
