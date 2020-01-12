package com.example.ud7_ejemplo2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ActualizarBorrar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_borrar);

        EditText idEdit = findViewById(R.id.idEditAct);
        EditText nombre = findViewById(R.id.nombreEditAct);
        EditText ap = findViewById(R.id.apellidoEditAct);
        EditText email = findViewById(R.id.emailEditAct);

        int id = getIntent().getIntExtra("id", 0);

        idEdit.setText(String.valueOf(id));
        nombre.setText(getIntent().getStringExtra("nombre"));
        ap.setText(getIntent().getStringExtra("apellido"));
        email.setText(getIntent().getStringExtra("email"));

        Button botonAct = findViewById(R.id.actualizarBoton);
        Button botonBor = findViewById(R.id.borrarBoton);

        botonAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();

                intent.putExtra("Borrar", false);
                intent.putExtra("id", id);
                intent.putExtra("nombre", nombre.getText().toString());
                intent.putExtra("apellido", ap.getText().toString());
                intent.putExtra("email", email.getText().toString());


                // Devolvemos un código de RESULT_OK
                setResult(RESULT_OK, intent);

                // Cerramos la actividad y volvemos a atrás.
                finish();
            }
        });

        botonBor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();

                intent.putExtra("Borrar", true);
                intent.putExtra("id", id);

                // Devolvemos un código de RESULT_OK
                setResult(RESULT_OK, intent);

                // Cerramos la actividad y volvemos a atrás.
                finish();
            }
        });
    }
}
