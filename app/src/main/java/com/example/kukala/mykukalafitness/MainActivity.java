package com.example.kukala.mykukalafitness;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import static android.widget.Toast.makeText;
import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
        private SharedPreferences prefs;//accedemos al fichero de referencias

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        prefs=getSharedPreferences("Preferencias",Context.MODE_PRIVATE);
        String prueba=prefs.getString("pemail", "no existe");
        String fotografia=prefs.getString("pfoto","no existe");
        //Toast.makeText(this, "Prueba vale: "+prueba, Toast.LENGTH_LONG).show();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Obtenemos referencia de navigationView
        ImageView imageView=(ImageView)navigationView.getHeaderView(0).findViewById(R.id.imageView);
        TextView txtname = (TextView)navigationView.getHeaderView(0).findViewById(R.id.txtViewHola);
        //String urlimage="https://airquee.com/images/Staff/Anna%20Maria.jpg";
        Picasso.with(this)
                .load(fotografia)
                .into(imageView);
        txtname.setText("Hola "+prueba);



         }

        //Toast.makeText(this, "pemail", Toast.LENGTH_SHORT).show();

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            removeSharedPreferences();
            logOut();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager fragmentManager=getSupportFragmentManager();
         if (id == R.id.nav_objetivo) {
            //fragmentManager.beginTransaction().replace(R.id.contenedor, new Fragment_Objetivo()).commit();
            //getSupportActionBar().setTitle("Objetivo");
        } else if (id == R.id.nav_registro_calorias) {
            fragmentManager.beginTransaction().replace(R.id.contenedor, new Fragment_Registro_Calorias()).commit();
            getSupportActionBar().setTitle("Registro Calorias");
         } else if (id == R.id.nav_consulta_calorias) {
            fragmentManager.beginTransaction().replace(R.id.contenedor, new Fragment_Consulta()).commit();
            getSupportActionBar().setTitle("Consulta");
         } else if (id == R.id.nav_logout) {
            removeSharedPreferences();
           logOut();
             }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void logOut(){
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);//no va hacia atrás
        startActivity(intent);
    }
    private void removeSharedPreferences() {
        prefs.edit().clear().apply();//borra todo de preferencias
    }
    /*
    private String getUserMailPrefs(){
       return prefs.getString("pemail","");

    }*/

}
