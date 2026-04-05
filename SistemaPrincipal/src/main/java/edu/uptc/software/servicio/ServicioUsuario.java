package edu.uptc.software.servicio;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;

import edu.uptc.software.model.Usuario;
import edu.uptc.software.repositorio.RepositorioUsuario;

@Service // para que spring boot sepa que esta clase tiene la logica de la autenticacion
public class ServicioUsuario {

    private final GoogleAuthenticator gAuth = new GoogleAuthenticator();

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

    public List<Usuario> obtenerTodosLosUsuarios() {
    return repositorio.findAll(); 
    }

    // Método para generar el secreto con google authenticator
    public String habilitar2FA(String nombre) {
    Optional<Usuario> usuarioOpt = repositorio.findByNombreUsuario(nombre);
    if (usuarioOpt.isPresent()) {
        GoogleAuthenticatorKey credentials = gAuth.createCredentials();
        String secreto = credentials.getKey();
        
        Usuario u = usuarioOpt.get();
        u.setSecreto2fa(secreto);
        u.setMfaHabilitado(true);
        repositorio.save(u);
        return secreto; // Este código es el que se mete en el celular
    }
    return "Usuario no encontrado";
}

// Método para validar el código que el usuario escribe en el login
    public boolean verificarCodigo2FA(String nombre, int codigo) {
    Optional<Usuario> usuarioOpt = repositorio.findByNombreUsuario(nombre);
    if (usuarioOpt.isPresent()) {
        Usuario u = usuarioOpt.get();
        if (!u.isMfaHabilitado() || u.getSecreto2fa() == null) return false;
        
        // Compara el código del celular con el secreto guardado
        return gAuth.authorize(u.getSecreto2fa(), codigo);
    }
    return false;
}
}