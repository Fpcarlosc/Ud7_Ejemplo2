package com.example.ud7_ejemplo2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Insertar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insertar);

        EditText nombre = findViewById(R.id.nombreEdit);
        EditText ap = findViewById(R.id.apellidoEdit);
        EditText email = findViewById(R.id.emailEdit);

        Button boton = findViewById(R.id.insertarBoton);

        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();

                intent.putExtra("nombre", nombre.getText().toString());
                intent.putExtra("apellido", ap.getText().toString());
                intent.putExtra("email", email.getText().toString());

                // Devolvemos un código de RESULT_OK
                setResult(RESULT_OK, intent);

                // Cerramos la actividad y volvemos a atrás.
                finish();
            }
        });
    }
}
