package com.example.ecolim.api;

import com.example.ecolim.models.Usuario;
import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    private String status;
    @SerializedName("message")
    private Usuario usuario;

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
}
