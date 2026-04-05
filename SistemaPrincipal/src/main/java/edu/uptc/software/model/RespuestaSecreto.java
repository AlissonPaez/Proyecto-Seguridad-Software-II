package edu.uptc.software.model;

public class RespuestaSecreto{
    private String usuario;
    private String secreto;

    public RespuestaSecreto(String usuario, String secreto) {
        this.usuario = usuario;
        this.secreto = secreto;
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


    

}

