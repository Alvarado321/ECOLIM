package com.example.ecolim.models;

public class EmpleadoModel {
    private int idEmpleado;
    private String nombre;
    private String email;
    private String cargo;
    private String departamento;
    private boolean activo;

    public EmpleadoModel() {
    }

    public EmpleadoModel(int idEmpleado, String nombre, String email, String cargo, String departamento, boolean activo) {
        this.idEmpleado = idEmpleado;
        this.nombre = nombre;
        this.email = email;
        this.cargo = cargo;
        this.departamento = departamento;
        this.activo = activo;
    }

    // Getters
    public int getIdEmpleado() { return idEmpleado; }
    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
    public String getCargo() { return cargo; }
    public String getDepartamento() { return departamento; }
    public boolean isActivo() { return activo; }

    // Setters
    public void setIdEmpleado(int idEmpleado) { this.idEmpleado = idEmpleado; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setEmail(String email) { this.email = email; }
    public void setCargo(String cargo) { this.cargo = cargo; }
    public void setDepartamento(String departamento) { this.departamento = departamento; }
    public void setActivo(boolean activo) { this.activo = activo; }
}
