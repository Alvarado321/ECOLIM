package com.example.ecolim.api;

import com.example.ecolim.models.Usuario;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.*;

public interface ApiService {
    @POST("usuarios")
    Call<Usuario> registrarUsuario(@Body Usuario usuario);

    @GET("usuarios")
    Call<List<Usuario>> obtenerUsuarios();

    @PUT("usuarios/{id}")
    Call<Usuario> actualizarUsuario(@Path("id") int id, @Body Usuario usuario);

    @DELETE("usuarios/{id}")
    Call<Void> eliminarUsuario(@Path("id") int id);

    @POST("usuarios/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);
}
