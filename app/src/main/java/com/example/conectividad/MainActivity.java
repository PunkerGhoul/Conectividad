package com.example.conectividad;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String protocol = "http://";
    //private static final String IP = "192.168.0.28";
    private static final String IP = "192.168.104.75";
    //private static final String IP = "127.0.0.1";
    private static final String sitio = "practicaMovil";


    private EditText edtId;
    private EditText edtNombres;
    private EditText edtApellidos;
    private Button btnAgregar;
    private Button btnBuscar;
    private Button btnModificar;
    private Button btnEliminar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtId = (EditText) findViewById(R.id.editTextNumber);
        edtNombres = (EditText) findViewById(R.id.editTextTextPersonName);
        edtApellidos = (EditText) findViewById(R.id.editTextTextPersonLastName);
        btnAgregar = (Button) findViewById(R.id.buttonAgregar);
        btnBuscar = (Button) findViewById(R.id.buttonBuscar);
        btnModificar = (Button) findViewById(R.id.buttonModificar);
        btnEliminar = (Button) findViewById(R.id.buttonEliminar);

        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = edtId.getText().toString();
                String nombres = edtNombres.getText().toString();
                String apellidos = edtApellidos.getText().toString();

                ejecutarDatos(protocol + IP + "/" + sitio + "/insertar.php?cidentificacion=" + id + "&cnombre=" + nombres +"&capellido="+ apellidos,
                        id,
                        nombres,
                        apellidos);
            }
        });

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = edtId.getText().toString();
                buscarDatos(protocol + IP + "/" + sitio + "/consultar.php?cidentificacion=" + id);
            }
        });

        btnModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = edtId.getText().toString();
                String nombres = edtNombres.getText().toString();
                String apellidos = edtApellidos.getText().toString();
                ejecutarDatos(protocol + IP + "/" + sitio + "/modificar.php?cidentificacion=" + id + "&cnombre=" + nombres +"&capellido="+ apellidos,
                        id,
                        nombres,
                        apellidos);
            }
        });

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = edtId.getText().toString();
                eliminarDatos(protocol + IP + "/" + sitio + "/eliminar.php?cidentificacion=" + id);
            }
        });
    }

    public void ejecutarDatos(String URL, String id, String nombres, String apellidos){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "OPERACIÓN ÉXITOSA", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("id", id);
                parametros.put("nombres", nombres);
                parametros.put("apellidos", apellidos);
                return parametros;
            }
        };
        //Describe como envíar los datos a la base de datos
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        requestQueue.start();
    }

    public void buscarDatos(String URL){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, response -> {
            JSONObject jsonObject;
            for (int i = 0; i < response.length(); i++) {
                try {
                    jsonObject = response.getJSONObject(i);
                    edtId.setText(jsonObject.getString("identificacion"));
                    edtNombres.setText(jsonObject.getString("nombre"));
                    edtApellidos.setText(jsonObject.getString("apellido"));
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        }, error -> Toast.makeText(getApplicationContext(), "ERROR DE CONEXION",
                Toast.LENGTH_SHORT).show()
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
        requestQueue.start();
    }

    public void eliminarDatos(String URL){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, response -> {
            Toast.makeText(getApplicationContext(), "DATOS ELIMINADOS", Toast.LENGTH_SHORT).show();
        }, error -> Toast.makeText(getApplicationContext(), "ERROR DE CONEXION",
                Toast.LENGTH_SHORT).show()
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
        requestQueue.start();
    }
}