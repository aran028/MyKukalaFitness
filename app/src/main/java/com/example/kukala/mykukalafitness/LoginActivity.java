package com.example.kukala.mykukalafitness;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    //private static final  IP_con_PUERTO_y_URL ruta = new IP_con_PUERTO_y_URL();
    //private static final IP_con_PUERTO_y_URL rutaPrevio = new IP_con_PUERTO_y_URL();
   //private static final String ruta = (String)rutaPrevio.getRuta();
    private SharedPreferences prefs;
    EditText txtemail;
    EditText password;
    Button botonlogin;
    TextView lblResultado;
    String foto;
    private static final String BARRA = "/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Inflate the layout for this fragment
        cargarCampos();
        prefs=getSharedPreferences("Preferencias",Context.MODE_PRIVATE);
        botonlogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean vpass = validatePassword(password.getText().toString());
                boolean vemail = validateEmail(txtemail.getText().toString());
                if (vpass && vemail) {
                    TareaWSObtener tarea = new TareaWSObtener();
                    tarea.execute(txtemail.getText().toString());
                }
            }
        });

       // return view;
    }

    private void cargarCampos() {
        txtemail = (EditText) findViewById(R.id.txtemail);
        txtemail.setText("rbm030@gmail.com");
        password = (EditText)findViewById(R.id.password);
        password.setText("rbm030");
        botonlogin = (Button)findViewById(R.id.botonlogin);
    }

    private boolean validatePassword(String pass) {
        String password = pass;
        if (password.isEmpty()) {
            Toast.makeText(this, "el password no puede estar vacio", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private boolean validateEmail(String email) {
        String emailInput = email;
        if (emailInput.isEmpty()) {
            Toast.makeText(this, "email no puede estar vacio", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            Toast.makeText(this, "introduce email valido", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }
    //escribimos la clase de json para buscar
//Tarea Asincrona para llamar al WS de consulta en segundo plano
    private class TareaWSObtener extends AsyncTask<String, Integer, Boolean> {
        private String email;
        private String contrasena;
        private String foto;

        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            String id = params[0];

            HttpGet del =
                   // new HttpGet("http://192.168.1.190:82/ServiceWebRest/Api/Usuarios/Usuario/" + id + BARRA);
                   new HttpGet("http://10.111.8.141:82/ServiceWebRest/Api/Usuarios/Usuario/" + id + BARRA);
            //new HttpGet(ruta+"Usuarios/Usuario/" + id + BARRA);
            //new HttpGet("http://192.168.1.106:82/ServiceWebRest/Api/Usuarios/Usuario/" + id + BARRA);
            del.setHeader("content-type", "application/json");
            try {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());
                JSONObject respJSON = new JSONObject(respStr);
                email = respJSON.getString("Email");
                contrasena = respJSON.getString("Contrasena");
                foto = respJSON.getString("Foto");
            } catch (Exception ex) {
                Log.e("ServicioRest", "Error!", ex);
                resul = false;
            }
            return resul;
        }

        protected void onPostExecute(Boolean result) {

            if (result) {
                String passw = (String) password.getText().toString();
                contrasena = contrasena.trim();
                if (contrasena.equals(passw)) {
                    int objetivoCalo=1000;
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);//no va hacia atrás
                    startActivity(intent);
                    GuardarPreferences(email,foto,objetivoCalo);

                } else {
                    Toast.makeText(LoginActivity.this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(LoginActivity.this, "Usuario no existe en la base de datos", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void GuardarPreferences(String email,String foto, int objetivoCalo) {
        String miEmail = txtemail.getText().toString();
        String miFoto = foto;
        Toast.makeText(LoginActivity.this, "Estoy en saveOnPreferences", Toast.LENGTH_SHORT).show();
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("pemail", miEmail);
        editor.putString("pfoto", miFoto);
        editor.putInt("pobjetivoCalorias",objetivoCalo);
        editor.commit();
        //Toast.makeText(this, "SESION CREADA: "+miEmail + "foto"+miFoto, Toast.LENGTH_LONG).show();
        Toast.makeText(this, "SU SESIÓN SE HA CREADO: "+miEmail , Toast.LENGTH_LONG).show();
    }

}




