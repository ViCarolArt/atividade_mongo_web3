package com.example.mongoapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DuplicateKeyException;

import com.example.mongoapp.dto.UsuarioRequest;
import com.example.mongoapp.exception.EmailJaCadastradoException;
import com.example.mongoapp.exception.UsuarioNaoEncontradoException;
import com.example.mongoapp.model.Usuario;
import com.example.mongoapp.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    UsuarioRepository repository;

    @InjectMocks
    UsuarioService service;

    @Test
    void criarNormalizaEmailEIgnoraEspacos() {
        when(repository.save(any(Usuario.class))).thenAnswer(i -> i.getArgument(0));
        service.criar(new UsuarioRequest("  Ana  ", "  ANA@Mail.COM "));
        ArgumentCaptor<Usuario> cap = ArgumentCaptor.forClass(Usuario.class);
        verify(repository).save(cap.capture());
        assertEquals("Ana", cap.getValue().getNome());
        assertEquals("ana@mail.com", cap.getValue().getEmail());
    }

    @Test
    void criarComEmailDuplicadoLancaConflito() {
        when(repository.save(any(Usuario.class))).thenThrow(new DuplicateKeyException("dup"));
        assertThrows(EmailJaCadastradoException.class,
                () -> service.criar(new UsuarioRequest("Ana", "ana@mail.com")));
    }

    @Test
    void idMalformadoRetornaNaoEncontradoSemConsultarOBanco() {
        assertThrows(UsuarioNaoEncontradoException.class, () -> service.buscarPorId("{\"$ne\":null}"));
        assertThrows(UsuarioNaoEncontradoException.class, () -> service.deletar("abc"));
        verify(repository, never()).findById(any());
        verify(repository, never()).deleteById(any());
    }
}
