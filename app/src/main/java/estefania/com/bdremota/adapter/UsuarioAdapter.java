package estefania.com.bdremota.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import estefania.com.bdremota.R;
import estefania.com.bdremota.model.Usuario;


// puente entre los datos y el list view
public class UsuarioAdapter extends RecyclerView.Adapter<UsuarioAdapter.UsuariosHolder> {

    //Lista de usuarios
    List<Usuario> listaUsuarios;

    //constructor
    public UsuarioAdapter(List<Usuario> listaUsuarios)
    {
        this.listaUsuarios=listaUsuarios;
    }

    @NonNull
    @Override
    public UsuariosHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View vista= LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_usuarios,parent,false);
       RecyclerView.LayoutParams layoutParams= new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
       vista.setLayoutParams(layoutParams);
       return new UsuariosHolder(vista);
    }


    //para recorrer la lista
    @Override
    public void onBindViewHolder(@NonNull UsuariosHolder holder, int position) {
        holder.txtNombre.setText(listaUsuarios.get(position).getNombre());
        holder.txtApellidos.setText(listaUsuarios.get(position).getApellidos());
        holder.txtEdad.setText(listaUsuarios.get(position).getEdad());
    }

    @Override
    public int getItemCount() {
        return listaUsuarios.size();
    }


    //para mostrar los datos de la BD
    public class UsuariosHolder extends RecyclerView.ViewHolder {
        TextView txtNombre, txtApellidos, txtEdad;
        public UsuariosHolder(@NonNull View itemView) {
            super(itemView);
            txtNombre= itemView.findViewById(R.id.txtNombre2);
            txtApellidos= itemView.findViewById(R.id.txtApellidos2);
            txtEdad= itemView.findViewById(R.id.txtEdad2);
        }
    }
}
