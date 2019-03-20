package com.example.kukala.mykukalafitness;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.Calendar;

import static android.content.Context.*;

public class Fragment_Registro_Calorias extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String CERO = "0";
    private static final String BARRA = "-";
    //Calendario para obtener fecha & hora
    public final Calendar c = Calendar.getInstance();
    //Variables para obtener la fecha
    final int mes = c.get(Calendar.MONTH);
    final int dia = c.get(Calendar.DAY_OF_MONTH);
    final int anio = c.get(Calendar.YEAR);
    //Widgets
    EditText etFecha;
    ImageButton btnObtenerFecha;
    Spinner cmbAlimentos;
    EditText textCA;
    Spinner cmbOpciones;
    EditText textTC;
    EditText etCantidad;
    Button btnAgregarAlimento;
   //pruebas
    EditText edTextPemail;
    //incorporamos el fichero SharedPreferences
    //accedemos al fichero de referencias
    private SharedPreferences prefs;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Nos creamos la vista
        View view = inflater.inflate(R.layout.fragment__registro__calorias, container, false);
        //Incorporamos el elemento de prueba de email
        edTextPemail = (EditText) view.findViewById(R.id.edTextPemail);
        SharedPreferences prefs = this.getActivity().getSharedPreferences("Preferencias", Context.MODE_PRIVATE);
        String capturaemail=prefs.getString("pemail","");
        edTextPemail.setText(capturaemail);
        edTextPemail.setVisibility(View.INVISIBLE);
        //Incorporamos los elementos
        etFecha = (EditText) view.findViewById(R.id.etFecha);
        //Widget ImageButton del cual usaremos el evento clic para obtener la fecha
        btnObtenerFecha = (ImageButton) view.findViewById(R.id.btnObtenerFecha);
        //BOTON CARGAR CALENDARIO Y CARGAR LOS VALORES DE LOS ALIMENTOS EN SPINNER cmbAlimentos
        btnObtenerFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getContext(), "Hola guapa no te desanimes", Toast.LENGTH_SHORT).show();
                Fragment_Registro_Calorias.TareaWSListarAlimentos tarea = new Fragment_Registro_Calorias.TareaWSListarAlimentos();
                tarea.execute();
                obtenerFecha();
            }
        });
        cmbOpciones = (Spinner) view.findViewById(R.id.cmbOpciones);
        String[] letra = {"D", "A", "C"};
        //texto donde se ve que tipo de comida se ha seleccionado
        textTC = (EditText) view.findViewById(R.id.textTC);
        textTC.setVisibility(View.INVISIBLE);
        //spinner de tipo de comida D desayuno, A almuerzo C cena
        cmbOpciones.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, letra));
        cmbOpciones.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               textTC.setText(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        //spinner de alimentos
        cmbAlimentos = (Spinner) view.findViewById(R.id.cmbAlimentos);
        textCA = (EditText) view.findViewById(R.id.textCA);
        textCA.setVisibility(View.INVISIBLE);
        cmbAlimentos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                textCA.setText(parent.getItemAtPosition(position).toString().substring(0,1));
                                       }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        etCantidad = (EditText) view.findViewById(R.id.etCantidad);
        //Insertamos el botón para agregar alimento
        btnAgregarAlimento = (Button) view.findViewById(R.id.btnAgregarAlimento);
        //vamos definir el evento onclick
        btnAgregarAlimento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Hola guapa HAS PULSADO EN BOTON AGREGAR ALIMENTO", Toast.LENGTH_SHORT).show();
                Fragment_Registro_Calorias.TareaWSInsertarAlimentos tarea2 = new TareaWSInsertarAlimentos();
                tarea2.execute(
                        edTextPemail.getText().toString(),//0
                        etFecha.getText().toString(),//1
                        textTC.getText().toString(),//2
                        textCA.getText().toString(),//3
                        etCantidad.getText().toString());//4
            }

        });

       /* btnInicio = (Button) view.findViewById(R.id.btnInicio);
        btnInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //me voy al inicio de la aplicación
                Intent intent = new Intent(getActivity(), MainActivity.class);
                getActivity().startActivity(intent);
           }
        });*/
                return view;
    }

      private void obtenerFecha() {
        DatePickerDialog recogerFecha = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //Esta variable lo que realiza es aumentar en uno el mes ya que comienza desde 0 = enero
                final int mesActual = month + 1;
                //Formateo el día obtenido: antepone el 0 si son menores de 10
                String diaFormateado = (dayOfMonth < 10) ? CERO + String.valueOf(dayOfMonth) : String.valueOf(dayOfMonth);
                //Formateo el mes obtenido: antepone el 0 si son menores de 10
                String mesFormateado = (mesActual < 10) ? CERO + String.valueOf(mesActual) : String.valueOf(mesActual);
                //Muestro la fecha con el formato deseado
                etFecha.setText(year + BARRA + mesFormateado + BARRA + diaFormateado);
            }
            //Estos valores deben ir en ese orden, de lo contrario no mostrara la fecha actual
            /**
             *También puede cargar los valores que usted desee
             */
            //https://luismasdev.com/implementar-time-picker-y-date-picker-android/
        }, anio, mes, dia);
        //Muestro el widget
        recogerFecha.show();
    }

    //Tarea As�ncrona para llamar al WS de listado en segundo plano
    private class TareaWSListarAlimentos extends AsyncTask<String, Integer, Boolean> {

        private String[] alimentos;

        protected Boolean doInBackground(String... params) {

            boolean resul = true;
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet del =
                    //new HttpGet("http://10.41.1.245:82/ServiceWebRest/Api/Alimentos");
                     new HttpGet("http://10.111.8.141:82/ServiceWebRest/Api/Alimentos");
                    //new HttpGet("http://192.168.1.190:82/ServiceWebRest/Api/Alimentos");
                    //new HttpGet("http://192.168.1.179:82/ServiceWebRest/Api/Alimentos");
            del.setHeader("content-type", "application/json");

            try {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());

                JSONArray respJSON = new JSONArray(respStr);

                alimentos = new String[respJSON.length()];

                for (int i = 0; i < respJSON.length(); i++) {
                    JSONObject obj = respJSON.getJSONObject(i);
                    Integer codalimento = obj.getInt("CodAl");
                    String denomalimento = obj.getString("Denominacion");
                    int numcalalimento = obj.getInt("Calorias");
                    alimentos[i] = codalimento + "-" + denomalimento + "-" + numcalalimento;
                }
            } catch (Exception ex) {
                Log.e("ServicioRest", "Error!", ex);
                resul = false;
            }

            return resul;
        }

        protected void onPostExecute(Boolean result) {

            if (result) {  //Rellenamos el spinner combAlimentos con los alimentos de la tabla
                cmbAlimentos.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, alimentos));
            }
        }

    }//fin de TAREAWSLISTARALIMENTOS

    //TAREA PARA GRABAR EL REGISTRO DE CALORIAS
    //insertamos la clase TareaWSInsertarAlimentos para poder INSERTAR ALIMENTOS CONSUMIDOS POR EL USUARIO EN LA TABLA CALORIAS
//Tarea Asï¿½ncrona para llamar al WS de inserciï¿½n en segundo plano
    private class TareaWSInsertarAlimentos extends AsyncTask<String, Integer, Boolean> {

        protected Boolean doInBackground(String... params) {
            boolean resul = true;
            HttpClient httpClient = new DefaultHttpClient();
            //HttpPost post = new HttpPost("http://192.168.1.190:82/ServiceWebRest/Api/Calorias/Caloria");            //
             HttpPost post = new HttpPost("http://10.111.8.141:82/ServiceWebRest/Api/calorias/Caloria");
            //HttpPost post = new HttpPost("http://192.168.1.179:82/ServiceWebRest/Api/calorias/Caloria");
            post.setHeader("content-type", "application/json");
            try {  //Construimos el objeto cliente en formato JSON
                JSONObject dato = new JSONObject();
                //dato.put("nro2", nro2.getText().toString());
                //ORDEN DE CAMPOS
                //0 la fecha, 2 me falta alimento seleccionado, 4 tipo comida  , 5 cantidad
                dato.put("emailUsu", edTextPemail.getText().toString());
                dato.put("Fecha", params[1]);
                //dato.put("etFecha",etFecha.getText().toString());
                dato.put("TipoComida", params[2]);
                //dato.put("textTC",textTC.getText().toString());
                dato.put("CAl", Integer.parseInt(params[3]));
                //dato.put("textCA",textCA.getText().toString());
                dato.put("Cantidad", Integer.parseInt(params[4]));
                //https://www.programacion.com.py/moviles/android/acceso-a-web-service-rest-en-android
                StringEntity entity = new StringEntity(dato.toString());
                post.setEntity(entity);
                //Una vez creada nuestra peticiÃ³n HTTP y asociado el dato de entrada, tan sÃ³lo nos queda realizar
                // la llamada al servicio mediante el mtodo execute() del objeto HttpClient y
                // recuperar el resultado mediante getEntity().
                // Este resultado lo recibimos en forma de objeto HttpEntity, pero lo podemos convertir
                // fÃ¡cilmente en una cadena de texto mediante el mtodo estÃ¡tico EntityUtils.toString().
                HttpResponse resp = httpClient.execute(post);
                String respStr = EntityUtils.toString(resp.getEntity());
                //El resultado de la insercciÃ³n devuelve valor booleano o true si hay exito o false sino
                //El resultado de la operacion lo mostramos en etiqueta de texto llamada lblResultado.
                //System.out.println("Nos llega: " + respStr.toString() + "");
                if (!respStr.equals("true"))
                    resul = false;
            } catch (Exception ex) {
                Log.e("ServicioRest", "Error!", ex);
                resul = false;
            }
            return resul;
        }
        protected void onPostExecute(Boolean result) {
            if (result) {
                Toast.makeText(getContext(), "REGISTRO INSERTADO", Toast.LENGTH_SHORT).show();

                //Ponemos campos en blanco


            }

        }
    }
}










