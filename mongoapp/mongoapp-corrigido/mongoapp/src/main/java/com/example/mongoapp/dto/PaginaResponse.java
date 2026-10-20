package com.example.mongoapp.dto;

import java.util.List;

public record PaginaResponse<T>(List<T> conteudo, int pagina, int tamanho, long total, int totalPaginas) {
}
