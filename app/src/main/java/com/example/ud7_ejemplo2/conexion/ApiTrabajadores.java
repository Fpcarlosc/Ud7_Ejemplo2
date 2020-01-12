package com.example.ud7_ejemplo2.conexion;

import com.example.ud7_ejemplo2.modelo.Trabajador;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiTrabajadores {
    // Obtener todos los trabajadores
    @GET("trabajadores")
    Call<ArrayList<Trabajador>> obtenerTrabajador();

    // Añadir nuevos trabajadores. El id es autoincremental.
    // Con la anotación "FormUrlEncoded" los valores de la url son codificados en tuplas clave/valor
    // separadas por '&' y con un '='  entre la clave y el valor.
    @FormUrlEncoded
    @POST("trabajadores")
    Call<Trabajador> guardaTrabajador(
            @Field("nombre") String nombre,
            @Field("apellido") String ap,
            @Field("email") String email
    );

    // Actualizar el trabajador asociado al id indicado.
    @PUT("trabajadores/{id}")
    Call<Trabajador> actualizaTrabajador(@Path("id") int id, @Body Trabajador trabajador);

    // Borrar el trabajador asociado al id indicado.
    @DELETE("trabajadores/{id}")
    Call<Trabajador> borraTrabajador(@Path("id") int id);
}
