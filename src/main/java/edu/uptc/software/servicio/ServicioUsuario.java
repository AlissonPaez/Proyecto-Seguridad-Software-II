package edu.uptc.software.servicio;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.uptc.software.model.Usuario;
import edu.uptc.software.repositorio.RepositorioUsuario;

@Service // para que spring boot sepa que esta clase tiene la logica de la autenticacion
public class ServicioUsuario {

    @Autowired // para conectar con el repositorio de la bd y hacer operaciones
    private RepositorioUsuario repositorio;


    public String registrarUsuario(Usuario usuario) { // se busca el usuario en el repositorio y si ya esta da error
        if (repositorio.findByNombreUsuario(usuario.getNombreUsuario()).isPresent()) {
            return "Error: El nombre de usuario ya existe.";
        }
        repositorio.save(usuario);
        return "Usuario registrado exitosamente.";
    }

    public boolean validarCredenciales(String nombre, String clave) {
        Optional<Usuario> usuarioEncontrado = repositorio.findByNombreUsuario(nombre); // primero verifica que el usuario si exista
        
        if (usuarioEncontrado.isPresent()) {
            return usuarioEncontrado.get().getContraseña().equals(clave); // si existe compara la contraseña con la que ingresó y si son iguales devuelve true, sino false
        }
        return false;
    }
}