package edu.uptc.software.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.uptc.software.model.RespuestaSecreto;
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

    @GetMapping("/login")
    public String login(@RequestParam String nombre, 
                    @RequestParam String clave, 
                    @RequestParam int codigo) {
    
    if (servicio.validarCredenciales(nombre, clave)) {
        
        if (servicio.verificarCodigo2FA(nombre, codigo)) {
            
            return servicioComunicacion.enviarDatos("Acceso 2FA concedido a: " + nombre);
            
        } else {
            return "Error: Código de segundo factor (2FA) incorrecto.";
        }
    }
    return "Error: Usuario o contraseña incorrectos.";
}

    @GetMapping("/test-envio")
    public String testEnvio() {
    return servicioComunicacion.enviarDatos("Mensaje de prueba desde Sistema Principal");
}

    @GetMapping("/usuarios")
    public List<Usuario> listarUsuarios() {
        return servicio.obtenerTodosLosUsuarios();
    }

    @GetMapping("/activar-mfa")
    public ResponseEntity<RespuestaSecreto> activar(@RequestParam String nombre) {
    String secreto = servicio.habilitar2FA(nombre);
    
    RespuestaSecreto respuesta = new RespuestaSecreto(nombre, secreto);
    
    return ResponseEntity.ok(respuesta);
    }
}