# Ud7_Ejemplo2
_Ejemplo 2 de la Unidad 7._

Vamos a implementar una aplicación _CRUD_ que se conecte a nuestro servidor y permita crear, leer, modificar y eliminar trabajadores que tendrá los atributos id, nombre, apellido e email. 

Para ello crearemos nuestra propia API REST haciendo uso de un servidor _json-server_, que está implementado en _Node.js_ y permite trabajar con archivos _JSON_ para almacenar información.

Una forma fácil de instalarlo es hacerlo a través de una imagen de _Docker_.
Usaremos la siguiente: https://hub.docker.com/r/williamyeh/json-server/


Los pasos serán los siguientes:


## Paso 1: Modificar el fichero _build.gradle(Module:app)_
Añadiremos las siguientes dependencias para poder hacer uso de la librería _Retrofit_, del convertidor de _JSON_ _GSON_ y del _RecyclerView_
(Podemos ver la versión actual en _https://github.com/square/retrofit_):
```
    implementation 'com.squareup.retrofit2:retrofit:2.7.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.7.0'
    implementation 'androidx.recyclerview:recyclerview:1.1.0'
    implementation 'com.google.android.material:material:1.0.0'
```

Además, para su correcto funcionamiento, deberemos añadir las siguientes líneas en el bloque _android_:
```
android {
    ...
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
    ...
}
```

## Paso 2: Dar permisos a la aplicación
Para poder realizar conexiones con servidores deberemos darle permisos a la aplicación. Para ello se debe añadir la siguiente línea en el fichero _AndrodManifest.xml_:
```
<uses-permission android:name="android.permission.INTERNET"/>
```

## Paso 3: Creación de la interfaz _ApiTrabajadores_
Creamos una interfaz con los métodos HTTP a utilizar en nuestra aplicación. 
```
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

```

## Paso 4: Creación de la clase _Cliente_
Creamos una clase en la que construiremos el objeto _Retrofit_. En la constante _URL_ deberemos asignar la dirección _IP_ de 
nuestro servidor.
```
public class Cliente {
    // Dirección URL del servidor json server.
    private static final String URL = "http://192.168.1.100:3000";
    private static Retrofit retrofit = null;

    public static Retrofit obtenerCliente(){
        if(retrofit == null){
            // Construimos el objeto Retrofit asociando la URL del servidor y el convertidor Gson
            // para formatear la respuesta JSON.
            retrofit = new Retrofit.Builder()
                    .baseUrl(URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }
}
```

## Paso 5: Creación de la clase _Trabajador_
Creamos un _POJO_ con los datos que queremos obtener del trabajador.
```
public class Trabajador {
    @SerializedName("_id")
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
```

## Paso 6: Creación de los _layouts_
Creamos los _layouts_ de la aplicación con el _RecyclerView_ y el _FAB_.

### _activity_main.xml_
```
<androidx.constraintlayout.widget.ConstraintLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">

    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/recyclerview"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

    <com.google.android.material.floatingactionbutton.FloatingActionButton
        android:id="@+id/fab"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="end|bottom"
        android:src="@drawable/insertar"
        android:layout_marginEnd="32dp"
        android:layout_marginRight="32dp"
        android:layout_marginBottom="32dp"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent" />

</androidx.constraintlayout.widget.ConstraintLayout>
```
### _elementos_lista.xml_
```
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:orientation="vertical"
    android:layout_width="match_parent"
    android:layout_height="wrap_content">

    <TextView
        android:id="@+id/idtextView"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        tools:text="id"/>
    <TextView
        android:id="@+id/nombretextView"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        tools:text="nombre"/>
    <TextView
        android:id="@+id/apellidotextView"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        tools:text="apellido"/>
    <TextView
        android:id="@+id/emailtextView"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        tools:text="email"/>

</LinearLayout>
```
### _activity_insertar.xml_
```
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".Insertar">

    <TextView
        android:id="@+id/nombretextView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Nombre:"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.207"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintVertical_bias="0.341" />

    <EditText
        android:id="@+id/nombreEdit"
        android:layout_width="200dp"
        android:layout_height="wrap_content"
        android:hint="Nombre"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.53"
        app:layout_constraintStart_toEndOf="@+id/nombretextView"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintVertical_bias="0.333" />

    <TextView
        android:id="@+id/apellidotextView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Apellido:"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.207"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintVertical_bias="0.425" />

    <EditText
        android:id="@+id/apellidoEdit"
        android:layout_width="200dp"
        android:layout_height="wrap_content"
        android:hint="Apellido"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.542"
        app:layout_constraintStart_toEndOf="@+id/apellidotextView"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintVertical_bias="0.424" />

    <TextView
        android:id="@+id/emailtextView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Email:"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.241"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

    <EditText
        android:id="@+id/emailEdit"
        android:layout_width="200dp"
        android:layout_height="wrap_content"
        android:hint="Email"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.542"
        app:layout_constraintStart_toEndOf="@+id/emailtextView"
        app:layout_constraintTop_toTopOf="parent" />

    <Button
        android:id="@+id/insertarBoton"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Insertar"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/emailEdit" />

</androidx.constraintlayout.widget.ConstraintLayout>
```
### _activity_actualizar_borrar.xml_
```
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".ActualizarBorrar">

    <TextView
        android:id="@+id/idAct"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Id:"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.285"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintVertical_bias="0.273" />

    <EditText
        android:id="@+id/idEditAct"
        android:layout_width="200dp"
        android:layout_height="wrap_content"
        android:enabled="false"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.542"
        app:layout_constraintStart_toEndOf="@+id/idAct"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintVertical_bias="0.268" />

    <TextView
        android:id="@+id/nombreAct"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Nombre:"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.207"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintVertical_bias="0.341" />

    <EditText
        android:id="@+id/nombreEditAct"
        android:layout_width="200dp"
        android:layout_height="wrap_content"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.53"
        app:layout_constraintStart_toEndOf="@+id/nombreAct"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintVertical_bias="0.333" />

    <TextView
        android:id="@+id/apellidoAct"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Apellido:"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.207"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintVertical_bias="0.425" />

    <EditText
        android:id="@+id/apellidoEditAct"
        android:layout_width="200dp"
        android:layout_height="wrap_content"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.542"
        app:layout_constraintStart_toEndOf="@+id/apellidoAct"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintVertical_bias="0.424" />

    <TextView
        android:id="@+id/emailAct"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Email:"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.241"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

    <EditText
        android:id="@+id/emailEditAct"
        android:layout_width="200dp"
        android:layout_height="wrap_content"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.542"
        app:layout_constraintStart_toEndOf="@+id/emailAct"
        app:layout_constraintTop_toTopOf="parent" />

    <Button
        android:id="@+id/actualizarBoton"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Actualizar"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.297"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/emailEditAct"
        app:layout_constraintVertical_bias="0.501" />

    <Button
        android:id="@+id/borrarBoton"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Borrar"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.544"
        app:layout_constraintStart_toEndOf="@+id/actualizarBoton"
        app:layout_constraintTop_toBottomOf="@+id/emailEditAct"
        app:layout_constraintVertical_bias="0.501" />

</androidx.constraintlayout.widget.ConstraintLayout>
```
## Paso 7: Creación del adaptador _TrabajadorAdapter_
Creamos el adaptador para trabajar con el _RecyclerView_.

```
public class TrabajadorAdapter extends RecyclerView.Adapter<TrabajadorAdapter.TrabajadorViewHolder>{
    private Context context;
    ArrayList<Trabajador> lista;
    private View.OnClickListener onClickListener; // Atributo para el evento

    public TrabajadorAdapter(Context context) {
        this.context = context;
        lista = new ArrayList<>();
    }

    @NonNull
    @Override
    public TrabajadorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());

        View view=inflater.inflate(R.layout.elementos_lista,parent,false);

        view.setOnClickListener(this.onClickListener);

        TrabajadorViewHolder miViewHolder=new TrabajadorViewHolder(view);

        return miViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TrabajadorViewHolder holder, int position) {
        Trabajador t = lista.get(position);

        holder.idtextView.setText(String.valueOf(t.getId()));
        holder.nombretextView.setText(t.getNombre());
        holder.aptextView.setText(t.getApellido());
        holder.emailtextView.setText(t.getEmail());
    }

    @Override
    public int getItemCount() {
        if(lista == null)
            return 0;
        else
            return lista.size();
    }
    public void setOnItemClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    // Método para añadir una lista de trabajadores al recyclerView. (Llamado para GET)
    public void anyadirALista(ArrayList<Trabajador> lista){
        this.lista.addAll(lista);
        notifyDataSetChanged(); // Actualizamos el recyclerView
    }

    // Método para añadir un trabajador al recyclerView. (Llamado para POST)
    public void anyadirALista(Trabajador trabajador){
        this.lista.add(trabajador);
        notifyDataSetChanged(); // Actualizamos el recyclerView
    }

    // Método para actualizar una posición del recyclerView. (Llamado para PUT)
    public void actualizarLista(int pos, Trabajador trabajador){
        this.lista.set(pos, trabajador);
        notifyItemChanged(pos); // Actualizamos el elemento de la posicion "pos".
    }

    // Método para borrar una posición del recyclerView. (Llamado para DELETE)
    public void borrarDeLista(int pos){
        this.lista.remove(pos);
        notifyDataSetChanged(); // Actualizamos el recyclerView
    }

    class TrabajadorViewHolder extends RecyclerView.ViewHolder {

        TextView idtextView;
        TextView nombretextView;
        TextView aptextView;
        TextView emailtextView;

        public TrabajadorViewHolder(View itemView) {
            super(itemView);

            idtextView = itemView.findViewById(R.id.idtextView);
            nombretextView = itemView.findViewById(R.id.nombretextView);
            aptextView = itemView.findViewById(R.id.apellidotextView);
            emailtextView = itemView.findViewById(R.id.emailtextView);
        }
    }
}
```
## Paso 8: Creación de las actividades
A continuación, creamos las actividades para insertar, actualizar y eliminar trabajadores.

### Insertar.java
```
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
```
### ActualizarBorrar.java
```
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
```

## Paso 9: Creación de la clase _MainActivity_
Por último, en la clase _MainActivity_ haremos uso de todas las clases creadas y podremos conectar con el servidor.
```
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
```

