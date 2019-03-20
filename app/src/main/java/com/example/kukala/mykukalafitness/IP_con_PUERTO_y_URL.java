package com.example.kukala.mykukalafitness;

public class IP_con_PUERTO_y_URL {

    // Variable final de la IP --> Solo se modificaría esto
    private static final String IP = "10.111.8.141" ;

    // Variable para el puerto
    private static final String Puerto = "82" ;

    // Variable para la ruta del servicio
    private static final String WebApi = "/ServiceWebRest/Api/";

    private String ruta;


    public IP_con_PUERTO_y_URL(){

        ruta = IP+":"+Puerto+WebApi;

    }

    /* Metodo getter para obtener la url completa */
    public String getRuta(){
        return ruta;
    }

}
