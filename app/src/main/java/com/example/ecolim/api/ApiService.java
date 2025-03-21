package com.example.ecolim.api;

import com.example.ecolim.models.UsuarioModel;
import com.example.ecolim.api.responses.ListaUsuariosResponse;
import com.example.ecolim.api.responses.LoginResponse;
import com.example.ecolim.api.requests.LoginRequest;

import retrofit2.Call;
import retrofit2.http.*;

public interface ApiService {
    @POST("usuarios")
    Call<UsuarioModel> registrarUsuario(@Body UsuarioModel usuario);

    @GET("usuarios")
    Call<ListaUsuariosResponse> obtenerUsuarios();

    @GET("usuarios/search")
    Call<ListaUsuariosResponse> buscarUsuarios(@Query("query") String query);

    @PUT("usuarios/{id}")
    Call<UsuarioModel> actualizarUsuario(@Path("id") int id, @Body UsuarioModel usuario);

    @DELETE("usuarios/{id}")
    Call<Void> eliminarUsuario(@Path("id") int id);

    @POST("usuarios/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);
}
