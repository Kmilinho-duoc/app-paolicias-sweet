package com.paolicias.usuarios.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import com.paolicias.usuarios.entities.Usuario;
import com.paolicias.usuarios.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepo;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    private Usuario usuario(String nombre, String email) {
        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setEmail(email);
        return usuario;
    }

    @Test
    void crearGuardaElUsuarioComoCliente() {
        when(usuarioRepo.existsByEmail("ana@correo.cl")).thenReturn(false);
        when(usuarioRepo.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));

        Optional<Usuario> creado = usuarioService.crear(usuario("Ana", "ana@correo.cl"));

        assertTrue(creado.isPresent());
        assertEquals("Cliente", creado.get().getRol());
    }

    @Test
    void crearConUnCorreoRepetidoDevuelveVacio() {
        when(usuarioRepo.existsByEmail("ana@correo.cl")).thenReturn(true);

        assertTrue(usuarioService.crear(usuario("Ana", "ana@correo.cl")).isEmpty());
        verify(usuarioRepo, never()).save(any(Usuario.class));
    }

    @Test
    void crearSinNombreOSinCorreoDevuelveError() {
        assertThrows(ResponseStatusException.class, () -> usuarioService.crear(usuario("", "ana@correo.cl")));
        assertThrows(ResponseStatusException.class, () -> usuarioService.crear(usuario("Ana", " ")));
    }

    @Test
    void actualizarConservaElIdDelUsuarioExistente() {
        when(usuarioRepo.findById(3L)).thenReturn(Optional.of(usuario("Ana", "ana@correo.cl")));
        when(usuarioRepo.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));

        Optional<Usuario> resultado = usuarioService.actualizar(3L, usuario("Ana María", "ana@correo.cl"));

        assertTrue(resultado.isPresent());
        assertEquals(3L, resultado.get().getId());
        assertEquals("Ana María", resultado.get().getNombre());
    }

    @Test
    void eliminarUnUsuarioInexistenteDevuelveFalso() {
        when(usuarioRepo.existsById(8L)).thenReturn(false);

        assertEquals(false, usuarioService.eliminar(8L));
        verify(usuarioRepo, never()).deleteById(8L);
    }
}
