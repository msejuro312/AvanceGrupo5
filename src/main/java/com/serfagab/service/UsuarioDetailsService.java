package com.serfagab.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.serfagab.entities.Usuario;
import com.serfagab.repository.UsuarioRepository;

@Service
public class UsuarioDetailsService implements UserDetailsService {
    private final UsuarioRepository usuarioRepository;

    public UsuarioDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + login));

        if (!Boolean.TRUE.equals(usuario.getActivo())) {
            throw new UsernameNotFoundException("Usuario inactivo: " + login);
        }

        String rol = "administrador".equalsIgnoreCase(usuario.getTipo().getDescripcion())
                ? "ADMIN"
                : "USER";

        return org.springframework.security.core.userdetails.User.builder()
                .username(usuario.getLogin())
                .password(usuario.getClave())
                .roles(rol)
                .build();
    }
}
