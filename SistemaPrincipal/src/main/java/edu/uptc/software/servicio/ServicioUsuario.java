package edu.uptc.software.servicio;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorConfig;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;

import edu.uptc.software.model.Usuario;
import edu.uptc.software.repositorio.RepositorioUsuario;

@Service // para que spring boot sepa que esta clase tiene la logica de la autenticacion
public class ServicioUsuario {

    private final GoogleAuthenticator gAuth;

    @Autowired // para conectar con el repositorio de la bd y hacer operaciones
    private RepositorioUsuario repositorio;

    public ServicioUsuario() {
        GoogleAuthenticatorConfig config = new GoogleAuthenticatorConfig.GoogleAuthenticatorConfigBuilder()
            .setTimeStepSizeInMillis(30000) // 30 segundos estándar
            .setWindowSize(3) 
            .build();
        this.gAuth = new GoogleAuthenticator(config);
    }   
    

    public String registrarUsuario(Usuario usuario) { // se busca el usuario en el repositorio y si ya esta da error
        if (usuario.getNombreUsuario() == null || usuario.getNombreUsuario().isBlank() || 
        usuario.getContraseña() == null || usuario.getContraseña().isBlank()) {
        return "Error: Los campos no pueden estar vacíos.";
    }

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
        if (nombre == null || nombre.isBlank()) {
        return "Error: Debe ingresar un nombre de usuario.";
    }
    Optional<Usuario> usuarioOpt = repositorio.findByNombreUsuario(nombre); //Busca en el repositorio o bd el usuario.
    if (usuarioOpt.isPresent()) {
        GoogleAuthenticatorKey credentials = gAuth.createCredentials(); // la librería de google genera el código secreto para el usuario en ga.
        String secreto = credentials.getKey();
        
        Usuario u = usuarioOpt.get();
        u.setSecreto2fa(secreto); // guarda esa llave en la base de datos para ese usuario.
        u.setMfaHabilitado(true); // y obliga a que tenga que ponerse el codigo temporal.
        repositorio.save(u);
        return secreto; // Este código secreto es el que se mete en el celular para sacar el otro.
    }
    return "Usuario no encontrado";
}

// Método para validar el código temporal del login
    public boolean verificarCodigo2FA(String nombre, int codigo) {
    Optional<Usuario> usuarioOpt = repositorio.findByNombreUsuario(nombre); //Busca el nombre en la base de datos para ver el secreto y comparar.
    if (usuarioOpt.isPresent()) {
        Usuario u = usuarioOpt.get();
        if (!u.isMfaHabilitado() || u.getSecreto2fa() == null)  // Ve si el usuario tiene que de verdad ingresar ese codigo temporal y si tiene el codigo secreto.
            return false;
        
        // se trae el codigo secreto de la bd, y con la librería de google se hace el calculo y se compara que si sean los 6 digitos que el usuario ingreso.
        return gAuth.authorize(u.getSecreto2fa(), codigo);
    }
    return false;
}
}