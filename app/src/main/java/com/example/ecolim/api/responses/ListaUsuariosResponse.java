package com.example.ecolim.api.responses;

import com.example.ecolim.models.UsuarioModel;
import java.util.List;

public class ListaUsuariosResponse {
    private String status;
    private String message;
    private List<UsuarioModel> data;

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

    public List<UsuarioModel> getData() {
        return data;
    }

    public void setData(List<UsuarioModel> data) {
        this.data = data;
    }
}
