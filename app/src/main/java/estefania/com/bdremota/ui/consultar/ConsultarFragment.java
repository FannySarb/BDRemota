package estefania.com.bdremota.ui.consultar;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import estefania.com.bdremota.R;
import estefania.com.bdremota.adapter.UsuarioAdapter;
import estefania.com.bdremota.model.Usuario;

public class ConsultarFragment extends Fragment implements Response.ErrorListener, Response.Listener<JSONObject> {
    RecyclerView recyclerUsuarios;
    ArrayList<Usuario> listaUsuarios;
    ProgressDialog progreso;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

    private SlideshowViewModel slideshowViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                ViewModelProviders.of(this).get(SlideshowViewModel.class);
        View root = inflater.inflate(R.layout.fragment_consultar, container, false);
        listaUsuarios=new ArrayList<>();
        recyclerUsuarios=root.findViewById(R.id.rvUsuarios);
        recyclerUsuarios.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerUsuarios.setHasFixedSize(true);
        request= Volley.newRequestQueue(getContext());
        cargarWebService();
        return root;
    }

    public void cargarWebService()
    {
        String url="https://appsmoviles2020.000webhostapp.com/consultar.php";
        jsonObjectRequest= new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        request.add(jsonObjectRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getContext(), "No se puede conectar", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(JSONObject response) {
        Usuario usuario=null;
        JSONArray json=response.optJSONArray("usuario");

        //recorrer json
            try {
                for (int i = 0; i < json.length(); i++) {
                    usuario = new Usuario();
                    JSONObject jsonObject = null;
                    jsonObject = json.getJSONObject(i);

                    //obtener los datos de la bd al objeto usuario ("atributos en comillas" corresponden al campo de la tabla en la bd)
                    usuario.setNombre(jsonObject.optString("nombre"));
                    usuario.setApellidos(jsonObject.optString("apellidos"));
                    usuario.setEdad(jsonObject.optString("edad"));

                    //Agregar a la lista
                    listaUsuarios.add(usuario);
                }
                UsuarioAdapter adapter=new UsuarioAdapter(listaUsuarios);
                recyclerUsuarios.setAdapter(adapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }


    }
}