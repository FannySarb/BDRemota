package estefania.com.bdremota.ui.consultarUsuarios;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;



import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

//Esto tambien para mostrar los datos de la bd
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import estefania.com.bdremota.R;
import estefania.com.bdremota.model.Usuario;

public class ConsultarUsuariosFragment extends Fragment implements Response.Listener<JSONObject>,Response.ErrorListener {

    private ConsultarUsuariosViewModel toolsViewModel;



    EditText etId, etNombre, etApellidos, etEdad;
    ImageButton btnConsultar;
    Button btnActualizar, btnEliminar;
    ProgressDialog progreso;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    StringRequest stringRequest;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        toolsViewModel =
                ViewModelProviders.of(this).get(ConsultarUsuariosViewModel.class);
        View root = inflater.inflate(R.layout.fragment_consultar_usuarios, container, false);

        //referencia a cada elemento en la interfaz
        etId=root.findViewById(R.id.etId);
        etNombre=root.findViewById(R.id.etNombre);
        etApellidos=root.findViewById(R.id.etApellidos);
        etEdad=root.findViewById(R.id.etEdad);
        request= Volley.newRequestQueue(getContext());
        btnActualizar=root.findViewById(R.id.btnActualizar);
        btnConsultar=root.findViewById(R.id.btnConsultar);
        btnEliminar=root.findViewById(R.id.btnEliminar);

        //eventos para botones
        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eliminar();
            }
        });
        btnConsultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                consultar();
            }
        });
        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actualizar();
            }
        });
        return root;
    }

    private void eliminar() {
        progreso=new ProgressDialog(getContext());
        progreso.setMessage("Cargando");
        progreso.hide();
        //se manda a llamar al servicio en php y se le pasa el valor del Edit text  correspondiente al Id para
        // que regrese el json que tiene almacenado en la BD
        String url="https://appsmoviles2020.000webhostapp.com/eliminar.php?id="+etId.getText();

        // solicitud get
        stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            //en caso de que si haya una respuesta
            public void onResponse(String response) {
                progreso.hide();
                if (response.trim().equalsIgnoreCase("elimina")) {
                    etId.setText("");
                    etNombre.setText("");
                    etApellidos.setText("");
                    etEdad.setText("");
                    Toast.makeText(getContext(), "Se ha eliminado con exito", Toast.LENGTH_SHORT).show();
                } else {
                    if (response.trim().equalsIgnoreCase("noExiste")) {
                        Toast.makeText(getContext(), "No se encuentra el registro", Toast.LENGTH_SHORT).show();
                        Log.i("Respuesta:", "" + response);
                    } else {
                        Toast.makeText(getContext(), "NO se ha eliminado", Toast.LENGTH_SHORT).show();
                        Log.i("Respuesta:", "" + response);
                    }
                }
            }  //no respuesta
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"No se pudo conectar", Toast.LENGTH_SHORT).show();
                progreso.hide();
            }
        });
        request.add(stringRequest);
    }

    private void actualizar() {
        progreso=new ProgressDialog(getContext());
        progreso.setMessage("Cargando...");
        progreso.show();

        //Servicio
        String url="https://appsmoviles2020.000webhostapp.com/actualizar.php";

        //solicitud
        stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progreso.hide();
                if (response.trim().equalsIgnoreCase("actualiza")) {
                    Toast.makeText(getContext(), "Se actuaizo correctamente", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "No se actualizo", Toast.LENGTH_SHORT).show();
                    Log.i("Respuesta", "" + response);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"Error de conexion", Toast.LENGTH_SHORT).show();
                progreso.hide();
            }
        }){
            @Override
            protected Map<String, String> getParams()throws AuthFailureError{
                String id=etId.getText().toString();
                String nombre=etNombre.getText().toString();
                String apellidos=etApellidos.getText().toString();
                String edad=etEdad.getText().toString();
                Map<String, String> parametros=new HashMap<>();
                parametros.put("id", id);
                parametros.put("nombre",nombre);
                parametros.put("apellidos", apellidos);
                parametros.put("edad",edad);
                return parametros;
            }
        };
        request.add(stringRequest);
    }

    //busca el registro por id
    private void consultar() {
        progreso=new ProgressDialog(getContext());
        progreso.setMessage("Consultando");
        progreso.show();
        String url="https://appsmoviles2020.000webhostapp.com/consultar_usuario.php?id="+
                etId.getText().toString();
        jsonObjectRequest =new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        progreso.hide();
        Toast.makeText(getContext(),"NO se pudo consultar"+error.toString(),Toast.LENGTH_SHORT).show();
        Log.i("Error", error.toString());

    }

    @Override
    public void onResponse(JSONObject response) {
        progreso.hide();
        //Toast.makeText(getContext(),"Mensaje"+response,Toast.LENGTH_SHORT).show();
        Usuario user=new Usuario();
        JSONArray jsonArray=response.optJSONArray("usuario");
        JSONObject jsonObject=null;
        try {
            jsonObject=jsonArray.getJSONObject(0);
            user.setNombre(jsonObject.optString("nombre"));
            user.setApellidos(jsonObject.optString("apellidos"));
            user.setEdad(jsonObject.optString("edad"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        etNombre.setText(user.getNombre());
        etApellidos.setText(user.getApellidos());
        etEdad.setText(user.getEdad());
    }
}
