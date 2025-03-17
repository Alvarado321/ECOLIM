package com.example.ecolim.api;

import com.example.ecolim.models.Usuario;
import java.util.List;

public class ListaUsuariosResponse {
    private String status;
    private String message;
    private List<Usuario> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Usuario> getData() {
        return data;
    }

    public void setData(List<Usuario> data) {
        this.data = data;
    }
}
