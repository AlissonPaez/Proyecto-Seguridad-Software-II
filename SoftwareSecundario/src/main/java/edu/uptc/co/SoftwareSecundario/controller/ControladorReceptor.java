package edu.uptc.co.SoftwareSecundario.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.uptc.co.SoftwareSecundario.model.MensajeSeguro;

@RestController
@RequestMapping("/receptor")
public class ControladorReceptor {

    @PostMapping("/recibir")
    public String recibirMensaje(@RequestBody MensajeSeguro mensaje) {
        
        System.out.println("Nuevo mensaje recibido en el receptor:");
        System.out.println("Mensaje recibido: " + mensaje.getContenido());
        System.out.println("Hash recibido: " + mensaje.getHash());
        return "Mensaje recibido por el sistema secundario";
    }
    
}
