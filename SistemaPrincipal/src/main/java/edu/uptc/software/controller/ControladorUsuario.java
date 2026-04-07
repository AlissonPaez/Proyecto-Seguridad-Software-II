package edu.uptc.software.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.uptc.software.model.LoginRequest;
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

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request) {
        // 1. Validar Credenciales
        if (servicio.validarCredenciales(request.getNombre(), request.getClave())) {
            
            // 2. Validar el 2FA
            if (servicio.verificarCodigo2FA(request.getNombre(), request.getCodigo())) {
                
                // 3. Si todo es correcto, enviamos al Sistema Secundario con INTEGRIDAD (Tu parte)
                return servicioComunicacion.enviarDatos("Acceso exitoso para: " + request.getNombre());
            } else {
                return "Error: Código 2FA incorrecto o expirado.";
            }
        }
        return "Error: Usuario o contraseña incorrectos.";
    }

    @GetMapping("/test-envio")
    public String testEnvio() {
    return servicioComunicacion.enviarDatos("Mensaje de prueba desde Sistema Principal");
}

    //@GetMapping("/usuarios")
    //public List<Usuario> listarUsuarios() {
    //    return servicio.obtenerTodosLosUsuarios();
    //}

    @GetMapping("/activar-mfa")
    public ResponseEntity<RespuestaSecreto> activar(@RequestParam String nombre) {
    String secreto = servicio.habilitar2FA(nombre); // genera la cadena del código secreto y la guarda en la base de datos para ese usuario.
    
    RespuestaSecreto respuesta = new RespuestaSecreto(nombre, secreto); // aquí solo mostramos el nombre y el código secreto.
    
    return ResponseEntity.ok(respuesta); // si todo sale bien se pasa esa respuesta que es la que se muestra en el index
    }
}