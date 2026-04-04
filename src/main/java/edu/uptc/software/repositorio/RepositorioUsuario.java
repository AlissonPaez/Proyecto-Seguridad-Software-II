package edu.uptc.software.repositorio;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.uptc.software.model.Usuario;

// JpaRepository es para guardar, eliminar y en general hacer operaciones con la base de datos 
public interface RepositorioUsuario extends JpaRepository<Usuario, Long> {
    
    // con este metodo se busca el nombre del usuario para ver si existe o no
    Optional<Usuario> findByNombreUsuario(String nombreUsuario);
}