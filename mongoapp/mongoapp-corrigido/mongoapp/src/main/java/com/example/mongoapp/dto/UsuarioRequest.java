package com.example.mongoapp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** Entrada da API: so aceita nome e e-mail (o "id" enviado pelo cliente e ignorado). */
public record UsuarioRequest(
        @NotBlank(message = "O nome é obrigatório")
        @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
        String nome,

        @NotBlank(message = "O e-mail é obrigatório")
        @Email(message = "E-mail inválido")
        @Size(max = 254, message = "O e-mail deve ter no máximo 254 caracteres")
        String email) {
}
