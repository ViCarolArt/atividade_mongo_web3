package com.example.mongoapp.dto;

import com.example.mongoapp.model.Usuario;

public record UsuarioResponse(String id, String nome, String email) {
    public static UsuarioResponse de(Usuario u) {
        return new UsuarioResponse(u.getId(), u.getNome(), u.getEmail());
    }
}
