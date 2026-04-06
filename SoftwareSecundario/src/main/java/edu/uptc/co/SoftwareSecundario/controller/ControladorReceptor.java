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
        // Recalculamos el hash del contenido recibido
        String hashCalculado = servicioIntegridad.generarHash(mensaje.getContenido());

        // Comparamos el hash que llegó con el que acabamos de calcular
        if (hashCalculado.equals(mensaje.getHash())) {
            System.out.println("✅ INTEGRIDAD VERIFICADA: El mensaje no ha sido alterado");
            return "Mensaje verificado";
        } else {
            System.out.println("❌ ALERTA: Error de integridad detectado");
            return "ERROR: Integridad comprometida";
        }
    }
}
