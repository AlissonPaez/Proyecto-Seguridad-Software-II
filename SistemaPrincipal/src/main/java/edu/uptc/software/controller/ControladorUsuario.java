package edu.uptc.software.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.uptc.software.model.Usuario;
import edu.uptc.software.servicio.ServicioComunicacion;
import edu.uptc.software.servicio.ServicioUsuario;

@RestController // la clase es un componente web y las respuestas que se envíen se mandarán al navegador como texto o datos
@RequestMapping("/auth") // las URL empezarán con /auth
public class ControladorUsuario {

    @Autowired
    private ServicioUsuario servicio; //se conecta el servicio del usuario para la logica

    @Autowired
    private ServicioComunicacion servicioComunicacion;

    // para crear un nuevo usuario.
    @PostMapping("/registrar")
    public String registrar(@RequestBody Usuario usuario) { // el request body es para convertir el JSON en un objeto usuario
        return servicio.registrarUsuario(usuario);
    }

    // para pedir o consultar usuarios
    @GetMapping("/login")
    public String login(@RequestParam String nombre, @RequestParam String clave) { // todos los datos van en la misma URL
        boolean esValido = servicio.validarCredenciales(nombre, clave);
        if (esValido) {
            return "¡Bienvenido, " + nombre + "! Ha ingresado al sistema.";
        } else {
            return "Error: Usuario o contraseña incorrectos.";
        }
    }

    @GetMapping("/test-envio")
    public String testEnvio() {
    return servicioComunicacion.enviarDatos("Mensaje de prueba desde Sistema Principal");
}

    @GetMapping("/usuarios")
    public List<Usuario> listarUsuarios() {
        return servicio.obtenerTodosLosUsuarios();
    }
}