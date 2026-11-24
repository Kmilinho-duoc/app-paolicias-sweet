package com.paolicias.usuarios.services;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.paolicias.usuarios.entities.Usuario;
import com.paolicias.usuarios.repository.UsuarioRepository;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepo;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepo = usuarioRepository;
    }

    @Override
    public List<Usuario> obtenerTodos() {
        return usuarioRepo.findAll();
    }

    @Override
    public Optional<Usuario> obtenerPorId(Long id) {
        return usuarioRepo.findById(id);
    }

    @Override
    public Optional<Usuario> obtenerPorEmail(String email) {
        return usuarioRepo.findByEmail(email);
    }

    @Override
    public Optional<Usuario> crear(Usuario usuario) {
        validar(usuario);
        if (usuarioRepo.existsByEmail(usuario.getEmail())) {
            return Optional.empty();
        }
        usuario.setId(null);
        return Optional.of(usuarioRepo.save(usuario));
    }

    @Override
    public Optional<Usuario> actualizar(Long id, Usuario usuario) {
        validar(usuario);
        return usuarioRepo.findById(id).map(existente -> {
            usuario.setId(id);
            return usuarioRepo.save(usuario);
        });
    }

    @Override
    public boolean eliminar(Long id) {
        if (!usuarioRepo.existsById(id)) {
            return false;
        }
        usuarioRepo.deleteById(id);
        return true;
    }

    // nombre y correo son obligatorios; si no viene rol, es Cliente
    private void validar(Usuario usuario) {
        if (usuario.getNombre() == null || usuario.getNombre().isBlank()
                || usuario.getEmail() == null || usuario.getEmail().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre y el correo son obligatorios.");
        }
        if (usuario.getRol() == null || usuario.getRol().isBlank()) {
            usuario.setRol("Cliente");
        }
    }
}
