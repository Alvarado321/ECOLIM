package com.example.ecolim.api.responses;

import com.example.ecolim.models.UsuarioModel;
import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("status")
    private String status;
    
    @SerializedName("message")
    private String message;
    
    @SerializedName("data")
    private UsuarioModel data;

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

    public UsuarioModel getData() {
        return data;
    }

    public void setData(UsuarioModel data) {
        this.data = data;
    }
}
