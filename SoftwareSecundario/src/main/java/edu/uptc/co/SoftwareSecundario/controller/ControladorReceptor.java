package edu.uptc.co.SoftwareSecundario.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.uptc.co.SoftwareSecundario.model.MensajeSeguro;
import edu.uptc.co.SoftwareSecundario.servicio.ServicioIntegridad;

@RestController
@RequestMapping("/receptor")
public class ControladorReceptor {

    @Autowired
    private ServicioIntegridad servicioIntegridad;

    @PostMapping("/recibir")
    public String recibirMensaje(@RequestBody MensajeSeguro mensaje) {
        
        String hashCalculado = servicioIntegridad.generarHash(mensaje.getContenido());
        
        if (hashCalculado.equals(mensaje.getHash())) {
            System.out.println("INTEGRIDAD OK: El mensaje es auténtico.");
            return "Mensaje verificado y aceptado por el sistema secundario";
        } else {
            System.out.println("ALERTA: El mensaje fue manipulado en el camino.");
            return "ERROR: Integridad comprometida. Mensaje rechazado.";
        }
    }
}
