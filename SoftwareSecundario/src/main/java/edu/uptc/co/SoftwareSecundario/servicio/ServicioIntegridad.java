package edu.uptc.co.SoftwareSecundario.servicio;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import org.springframework.stereotype.Service;

@Service
public class ServicioIntegridad {

    public String generarHash(String mensaje){
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(mensaje.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashBytes);
        }catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al caclular SHA-256: " + e.getMessage());
        }
    }
    
}

