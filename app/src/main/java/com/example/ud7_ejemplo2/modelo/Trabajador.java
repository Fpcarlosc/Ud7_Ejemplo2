package com.example.ud7_ejemplo2.modelo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/* Usamos la anotación "SerializedName" para indicar que queremos obtener el valor de la
   clave indicada, así podemos usar otro nombre para el atributo del POJO. En otro caso
   el atributo se debe llamar igual a la clave.
   La anotación "Expose" es para indicar que ese atributo debe ser expuesto para serializar o
   deserializarse en JSON. */
public class Trabajador {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("nombre")
    @Expose
    private String nombre;
    @SerializedName("apellido")
    @Expose
    private String apellido;
    @SerializedName("email")
    @Expose
    private String email;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
