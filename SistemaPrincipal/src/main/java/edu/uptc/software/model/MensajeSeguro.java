package edu.uptc.software.model;

public class MensajeSeguro {
    private String contenido;
    private String hash;

    // Constructor
    public MensajeSeguro() {}

    // Getters and Setters
    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}
