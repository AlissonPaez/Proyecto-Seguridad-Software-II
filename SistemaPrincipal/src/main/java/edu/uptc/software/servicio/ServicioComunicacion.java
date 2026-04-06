package edu.uptc.software.servicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import edu.uptc.software.model.MensajeSeguro;

@Service
public class ServicioComunicacion {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ServicioIntegridad servicioIntegridad;

    public String enviarDatos(String texto){
        String url = "http://localhost:8081/receptor/recibir";
        

        MensajeSeguro mensajeSeguro = new MensajeSeguro();
        mensajeSeguro.setContenido(texto);

        String hash = servicioIntegridad.generarHash(texto);
        mensajeSeguro.setHash(hash);
        
        return restTemplate.postForObject(url, mensajeSeguro, String.class);
    }
}
