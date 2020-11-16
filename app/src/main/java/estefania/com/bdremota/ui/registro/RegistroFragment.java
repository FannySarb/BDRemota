package estefania.com.bdremota.ui.registro;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;


//libreria volley tambien debe de ponerse en el segundo build.gradle y dar permisos en el manifest
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import estefania.com.bdremota.R;

//Agregar las interfaces para los objetos JSON
public class RegistroFragment extends Fragment implements Response.Listener<JSONObject>,Response.ErrorListener {


    //referenciar los elementos de la interfaz
    private GalleryViewModel galleryViewModel;
    EditText tnombre, tapellidos, tedad;
    Button registrar;
    ProgressDialog progreso;

    //permite la conexion directa con la BD remota
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_registro, container, false);
        tnombre =root.findViewById(R.id.txtNombre);
        tapellidos=root.findViewById(R.id.etApellidos);
        tedad=root.findViewById(R.id.etEdad);
        registrar=root.findViewById(R.id.btnGuardar);


        request= Volley.newRequestQueue(getContext());

        //boton para el registro
        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //llama al servicio php
                cargarWebService();
            }
        });
        return root;
    }

    //Servicio con el que se obtienen los datos de los campos y se envian al la bd
    public void cargarWebService()
    {

        progreso=new ProgressDialog(getContext());
        progreso.setMessage("Cargando");
        progreso.show();

        //url del host y pasamos parametros de los campos para que se suban a la bd
        String url="https://appsmoviles2020.000webhostapp.com/registro.php?nombre="+tnombre.getText().toString()
                +"&apellidos="+tapellidos.getText().toString()+"&edad="+tedad.getText().toString();

        //remplazar espacios
        url=url.replace(" ","%20");

        //mandamos la url del servicio a volley para que la procese
        jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url,null,this,this);
        request.add(jsonObjectRequest);
    }

    //En caso de que la conexión falle
    @Override
    public void onErrorResponse(VolleyError error) {
        progreso.hide();
        Toast.makeText(getContext(),"No se pudo guardar el registro", Toast.LENGTH_SHORT).show();
        Log.i("Error",error.toString());
    }

    //Cuando la conexión funciona correctamente
    @Override
    public void onResponse(JSONObject response) {
        Toast.makeText(getContext(), "Registro exitoso", Toast.LENGTH_SHORT).show();
        progreso.hide();
        tnombre.setText("");
        tapellidos.setText("");
        tedad.setText("");
    }
}