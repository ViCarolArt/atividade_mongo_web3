package com.example.mongoapp.controller;

import java.net.URI;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.mongoapp.dto.PaginaResponse;
import com.example.mongoapp.dto.UsuarioRequest;
import com.example.mongoapp.dto.UsuarioResponse;
import com.example.mongoapp.model.Usuario;
import com.example.mongoapp.service.UsuarioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @GetMapping
    public PaginaResponse<UsuarioResponse> listar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<Usuario> p = service.listar(page, size);
        return new PaginaResponse<>(
                p.getContent().stream().map(UsuarioResponse::de).toList(),
                p.getNumber(), p.getSize(), p.getTotalElements(), p.getTotalPages());
    }

    @GetMapping("/{id}")
    public UsuarioResponse buscar(@PathVariable String id) {
        return UsuarioResponse.de(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<UsuarioResponse> criar(@Valid @RequestBody UsuarioRequest req) {
        Usuario criado = service.criar(req);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(criado.getId()).toUri();
        return ResponseEntity.created(location).body(UsuarioResponse.de(criado));
    }

    @PutMapping("/{id}")
    public UsuarioResponse atualizar(@PathVariable String id, @Valid @RequestBody UsuarioRequest req) {
        return UsuarioResponse.de(service.atualizar(id, req));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable String id) {
        service.deletar(id);
    }
}
