package com.example.kukala.mykukalafitness;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;


public class Fragment_Consulta extends Fragment {
    Button botonfragment;
    ListView lstCalorias;

    public Fragment_Consulta(){

    }
    private Button btnListar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //Nos creamos la vista
       View view=inflater.inflate(R.layout.fragment_consulta_calorias, container, false);
        //Incorporamos los elementos
        lstCalorias = (ListView) view.findViewById(R.id.lstCalorias);
        btnListar = (Button) view.findViewById(R.id.btnListar);
        botonfragment = (Button) view.findViewById(R.id.botonfragment);
        //Fecha=(EditText) view.findViewById(R.id.Fecha);
        botonfragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Texto en el fragment", Toast.LENGTH_SHORT).show();
            }
        });
        btnListar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment_Consulta.TareaWSListar tarea = new Fragment_Consulta.TareaWSListar();
                tarea.execute();
            }
        });
        return view;
    }
    //Tarea As�ncrona para llamar al WS de listado en segundo plano
    private class TareaWSListar extends AsyncTask<String,Integer,Boolean> {

        private String[] calorias;

        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();
//http://192.168.1.190:82/ServiceWebRest/Api/Calorias
            HttpGet del =
                    //new HttpGet("http://10.111.8.141:82/ServiceWebRest/Api/Calorias");
                    new HttpGet("http://192.168.1.190:82/ServiceWebRest/Api/Calorias");
            //new HttpGet("http://10.41.1.245:82/ServiceWebRest/Api/Calorias");
            del.setHeader("content-type", "application/json");

            try
            {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());

                JSONArray respJSON = new JSONArray(respStr);

                calorias = new String[respJSON.length()];

                for(int i=0; i<respJSON.length(); i++)
                {
                    JSONObject obj = respJSON.getJSONObject(i);

                    String email = obj.getString("EmailUsu");
                    String tipoComida=obj.getString("TipoComida");
                    int cantidad = obj.getInt("Cantidad");

                    calorias[i] = "" + email + "-" + tipoComida + "-" + cantidad;
                }
            }
            catch(Exception ex)
            {
                Log.e("ServicioRest","Error!", ex);
                resul = false;
            }

            return resul;
        }

        protected void onPostExecute(Boolean result) {

            if (result)
            {
                //Rellenamos la lista con los nombres de los calorias
                //Rellenamos la lista con los resultados
                ArrayAdapter<String> adaptador =
                        new ArrayAdapter<String>(getContext(),
                                android.R.layout.simple_list_item_1, calorias);

                lstCalorias.setAdapter(adaptador);
            }
        }
    }




};









