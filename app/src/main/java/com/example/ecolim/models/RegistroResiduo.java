package com.example.ecolim.models;

public class RegistroResiduo {
    public String empleado;
    public String tipoResiduo;
    public double cantidad;
    public String fechaRegistro;
    public String observaciones;

    public RegistroResiduo(String empleado, String tipoResiduo, double cantidad, String fechaRegistro, String observaciones) {
        this.empleado = empleado;
        this.tipoResiduo = tipoResiduo;
        this.cantidad = cantidad;
        this.fechaRegistro = fechaRegistro;
        this.observaciones = observaciones;
    }
}
