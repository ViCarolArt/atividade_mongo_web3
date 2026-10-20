package com.example.mongoapp.service;

import java.util.Locale;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.mongoapp.dto.UsuarioRequest;
import com.example.mongoapp.exception.EmailJaCadastradoException;
import com.example.mongoapp.exception.UsuarioNaoEncontradoException;
import com.example.mongoapp.model.Usuario;
import com.example.mongoapp.repository.UsuarioRepository;

@Service
public class UsuarioService {

    private static final Logger log = LoggerFactory.getLogger(UsuarioService.class);
    static final int TAMANHO_MAXIMO = 50;

    private final UsuarioRepository repository;

    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }

    public Page<Usuario> listar(int pagina, int tamanho) {
        int p = Math.max(pagina, 0);
        int t = Math.min(Math.max(tamanho, 1), TAMANHO_MAXIMO);
        return repository.findAll(PageRequest.of(p, t, Sort.by("nome")));
    }

    public Usuario buscarPorId(String id) {
        return repository.findById(validarId(id)).orElseThrow(UsuarioNaoEncontradoException::new);
    }

    public Usuario criar(UsuarioRequest req) {
        Usuario u = new Usuario(req.nome().trim(), normalizar(req.email()));
        Usuario salvo = salvar(u);
        log.info("Usuário criado: id={}", salvo.getId());
        return salvo;
    }

    public Usuario atualizar(String id, UsuarioRequest req) {
        Usuario u = buscarPorId(id);
        u.setNome(req.nome().trim());
        u.setEmail(normalizar(req.email()));
        Usuario salvo = salvar(u);
        log.info("Usuário atualizado: id={}", id);
        return salvo;
    }

    public void deletar(String id) {
        String idValido = validarId(id);
        if (!repository.existsById(idValido)) {
            throw new UsuarioNaoEncontradoException();
        }
        repository.deleteById(idValido);
        log.info("Usuário removido: id={}", idValido);
    }

    private Usuario salvar(Usuario u) {
        try {
            return repository.save(u);
        } catch (DuplicateKeyException e) { // indice unico de e-mail
            throw new EmailJaCadastradoException();
        }
    }

    private static String normalizar(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }

    private static String validarId(String id) {
        if (id == null || !ObjectId.isValid(id)) {
            throw new UsuarioNaoEncontradoException();
        }
        return id;
    }
}
