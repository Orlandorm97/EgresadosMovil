package com.example.appegresados;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UserActivity extends AppCompatActivity {
    TextView tvName, tvGenero, tvFecha, tvTelefono, tvProvincia, tvDistrito;
    Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        tvName = findViewById(R.id.tvName);
        tvGenero = findViewById(R.id.tvGenero);
        tvFecha = findViewById(R.id.tvFecha);
        tvTelefono = findViewById(R.id.tvTelefono);
        tvFecha = findViewById(R.id.tvFecha);
        tvProvincia = findViewById(R.id.tvProvincia);
        tvDistrito = findViewById(R.id.tvDistrito);
        btnLogout = findViewById(R.id.btnLogout);
        getUser();

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }

    public static String capitalize(String str) {
        if(str == null || str.isEmpty()) {
            return str;
        }

        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    private void getUser() {
        String url = getString(R.string.api_server)+"/user";
        new Thread(new Runnable() {
            @Override
            public void run() {
                com.example.appegresados.Http http = new com.example.appegresados.Http(com.example.appegresados.UserActivity.this,url);
                http.setMethod("GET");
                http.setToken(true);
                http.send();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Integer code = http.getStatusCode();
                        if(code == 200){
                            try {
                                JSONArray responseArray = new JSONArray(http.getResponse());
                                JSONObject response= new JSONObject(responseArray.getString(0));

                                String ap_paterno = capitalize(response.getString("ap_paterno"));
                                String ap_materno = capitalize(response.getString("ap_materno"));
                                String nombres = capitalize(response.getString("nombres"));
                                String genero = capitalize(response.getString("genero"));
                                String fecha = response.getString("fecha_nacimiento");
                                String telefono = response.getString("telefono");
                                String provincia = capitalize(response.getString("Provincia"));
                                String distrito = capitalize(response.getString("Distrito"));

                                String nombre_completo = ap_paterno+ " "+ ap_materno+ ", "+ nombres;

                                tvName.append(nombre_completo);
                                tvGenero.append(genero);
                                tvFecha.append(fecha);
                                tvTelefono.append(telefono);
                                tvProvincia.append(provincia);
                                tvDistrito.append(distrito);
                            }catch(JSONException e){
                                e.printStackTrace();
                            }
                        }
                        else{
                            Toast.makeText(com.example.appegresados.UserActivity.this, "Error "+code, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }).start();
    }

    private void logout() {
        String url = getString(R.string.api_server)+"/logout";
        new Thread(new Runnable() {
            @Override
            public void run() {
                com.example.appegresados.Http http = new com.example.appegresados.Http(com.example.appegresados.UserActivity.this, url);
                http.setMethod("post");
                http.setToken(true);
                http.send();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Integer code = http.getStatusCode();
                        if (code == 200){
                            Intent intent = new Intent(com.example.appegresados.UserActivity.this, com.example.appegresados.LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else{
                            Toast.makeText(com.example.appegresados.UserActivity.this, "Error "+code, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }).start();

    }


}