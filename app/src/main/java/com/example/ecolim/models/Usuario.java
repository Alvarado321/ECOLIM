package com.example.ecolim.models;

public class Usuario {
    private int idUsuario;
    private String nombre;
    private String email;
    private String password;
    private String rol; // Para distinguir diferentes tipos de usuarios

    public Usuario() {
    }

    public Usuario(int idUsuario, String nombre, String email, String password, String rol) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.rol = rol;
    }

    // Getters
    public int getIdUsuario() { return idUsuario; }
    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getRol() { return rol; }

    // Setters
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setRol(String rol) { this.rol = rol; }
}
