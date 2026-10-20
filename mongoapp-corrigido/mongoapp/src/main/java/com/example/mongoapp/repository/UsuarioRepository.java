package com.example.mongoapp.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.mongoapp.model.Usuario;

public interface UsuarioRepository extends MongoRepository<Usuario, String> {
}
