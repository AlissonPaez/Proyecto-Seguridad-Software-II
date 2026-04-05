package edu.uptc.software.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "usuarios") // Esta clase va a ser una tabla en la base de datos para guardar usuarios

public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // para el id como clave primaria y que se va a ir incrementando

    @Column(unique = true, nullable = false)
    private String nombreUsuario; // para que no hayan dos usuarios con el mismo nombre

    @Column(nullable = false)
    private String contraseña;

    private String secreto2fa;  // es el secreto que se le dará al usuario para que genere el 2FA con google authenticator.

    private boolean mfaHabilitado = false; // sirve para pedir o no el codigo temporal, si está en true tiene que si o si poner el codigo de los 6 digitos

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public String getSecreto2fa() {
        return secreto2fa;
    }

    public void setSecreto2fa(String secreto2fa) {
        this.secreto2fa = secreto2fa;
    }

    public boolean isMfaHabilitado() {
        return mfaHabilitado;
    }

    public void setMfaHabilitado(boolean mfaHabilitado) {
        this.mfaHabilitado = mfaHabilitado;
    }

    
    

    
}
