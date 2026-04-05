package edu.uptc.software.model;

public class RespuestaSecreto{
    private String usuario;
    private String secreto;
    private String instrucciones;

    public RespuestaSecreto(String usuario, String secreto) {
        this.usuario = usuario;
        this.secreto = secreto;
        this.instrucciones = "Ingrese este secreto en Google Authenticator para sincronizar su cuenta.";
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getSecreto() {
        return secreto;
    }

    public void setSecreto(String secreto) {
        this.secreto = secreto;
    }

    public String getInstrucciones() {
        return instrucciones;
    }

    public void setInstrucciones(String instrucciones) {
        this.instrucciones = instrucciones;
    }


    

}

