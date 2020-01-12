package com.example.ud7_ejemplo2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.ud7_ejemplo2.conexion.ApiTrabajadores;
import com.example.ud7_ejemplo2.conexion.Cliente;
import com.example.ud7_ejemplo2.modelo.Trabajador;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Retrofit retrofit;
    private RecyclerView recyclerView;
    private TrabajadorAdapter trabajadorAdapter;
    private int posisicionPulsada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerview);

        recyclerView.setHasFixedSize(true);

        // Añadimos la línea de separación de elementos de la lista
        // 0 para horizontal y 1 para vertical
        recyclerView.addItemDecoration(new DividerItemDecoration(MainActivity.this, 1));

        // Creamos un LinearLayout que contendrá cada elemento del RecyclerView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        trabajadorAdapter = new TrabajadorAdapter(this);

        trabajadorAdapter.setOnItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Obtenemos la posición pulsada del recyclerView para actualizarla o eliminarla.
                posisicionPulsada = recyclerView.getChildAdapterPosition(v);

                Trabajador t = trabajadorAdapter.lista.get(posisicionPulsada);

                Intent intent = new Intent(MainActivity.this, ActualizarBorrar.class);

                intent.putExtra("id", t.getId());
                intent.putExtra("nombre", t.getNombre());
                intent.putExtra("apellido", t.getApellido());
                intent.putExtra("email", t.getEmail());

                startActivityForResult(intent, 1);
            }
        });

        recyclerView.setAdapter(trabajadorAdapter);

        // Buscamos el FAB y configuramos su onClickListener
        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Insertar.class);

                startActivityForResult(intent, 0);
            }
        });

        retrofit = Cliente.obtenerCliente();

        obtenerDatos();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Insertar
        if (requestCode == 0 && resultCode == RESULT_OK) {
            Trabajador t = new Trabajador();
            t.setNombre(data.getStringExtra("nombre"));
            t.setApellido(data.getStringExtra("apellido"));
            t.setEmail(data.getStringExtra("email"));

            insertarDatos(t);

        }
        else{
            if (requestCode == 1 && resultCode == RESULT_OK) {
                // Borrar
                if(data.getBooleanExtra("Borrar", true)){ // Eliminamos
                    borrarDatos(data.getIntExtra("id", 0));
                }
                else { // Actualizar
                    Trabajador t = new Trabajador();
                    t.setId(data.getIntExtra("id", 0));

                    t.setNombre(data.getStringExtra("nombre"));
                    t.setApellido(data.getStringExtra("apellido"));
                    t.setEmail(data.getStringExtra("email"));

                    actualizarDatos(t);
                }
            }
        }

    }

    // Método para obtener todos los trabajadores del servidor.
    private void obtenerDatos(){
        ApiTrabajadores api = retrofit.create(ApiTrabajadores.class);

        Call<ArrayList<Trabajador>> respuesta = api.obtenerTrabajador();

        respuesta.enqueue(new Callback<ArrayList<Trabajador>>() {
            @Override
            public void onResponse(Call<ArrayList<Trabajador>> call, Response<ArrayList<Trabajador>> response) {
                if(response.isSuccessful()){
                    ArrayList<Trabajador> listapersonajes = response.body();

                    trabajadorAdapter.anyadirALista(listapersonajes);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Fallo en la respuesta", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Trabajador>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Método para insertar un nuevo trabajador en el servidor.
    private void insertarDatos(Trabajador trab){
        ApiTrabajadores api = retrofit.create(ApiTrabajadores.class);

        Call<Trabajador> t = api.guardaTrabajador(trab.getNombre(), trab.getApellido(), trab.getEmail());

        t.enqueue(new Callback<Trabajador>() {
            @Override
            public void onResponse(Call<Trabajador> call, Response<Trabajador> response) {
                Trabajador t = response.body();

                trabajadorAdapter.anyadirALista(t);

            }

            @Override
            public void onFailure(Call<Trabajador> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Fallo en la respuesta", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Método para actualizar un trabajador del servidor.
    private void actualizarDatos(Trabajador trab) {
        ApiTrabajadores api = retrofit.create(ApiTrabajadores.class);

        Call<Trabajador> t = api.actualizaTrabajador(trab.getId(), trab);

        t.enqueue(new Callback<Trabajador>() {
            @Override
            public void onResponse(Call<Trabajador> call, Response<Trabajador> response) {
                Trabajador t = response.body();

                trabajadorAdapter.actualizarLista(posisicionPulsada, t);

            }

            @Override
            public void onFailure(Call<Trabajador> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Fallo en la respuesta", Toast.LENGTH_SHORT).show();
            }
        });

    }

    // Método para borrar un trabajador del servidor.
    private void borrarDatos(int id) {
        ApiTrabajadores api = retrofit.create(ApiTrabajadores.class);

        Call<Trabajador> t = api.borraTrabajador(id);

        t.enqueue(new Callback<Trabajador>() {
            @Override
            public void onResponse(Call<Trabajador> call, Response<Trabajador> response) {
                Trabajador t = response.body();

                trabajadorAdapter.borrarDeLista(posisicionPulsada);
            }

            @Override
            public void onFailure(Call<Trabajador> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Fallo en la respuesta", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
