package com.example.ud7_ejemplo2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ud7_ejemplo2.modelo.Trabajador;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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

