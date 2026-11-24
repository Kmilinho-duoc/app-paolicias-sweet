package com.paolicias.usuarios.services;

import java.util.List;
import java.util.Optional;

import com.paolicias.usuarios.entities.Usuario;

public interface UsuarioService {

    List<Usuario> obtenerTodos();

    Optional<Usuario> obtenerPorId(Long id);

    Optional<Usuario> obtenerPorEmail(String email);

    // devuelve vacío si el correo ya está registrado
    Optional<Usuario> crear(Usuario usuario);

    Optional<Usuario> actualizar(Long id, Usuario usuario);

    boolean eliminar(Long id);
}
