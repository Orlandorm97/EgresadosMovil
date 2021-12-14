package com.example.appegresados;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {
    EditText etName, etEmail, etPassword, etConfirmation, etMatricula;
    Button btnRegister;
    String name, matricula, email, password, confirmation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setTitle("REGISTER");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etMatricula = findViewById(R.id.etMatricula);
        etPassword = findViewById(R.id.etPassword);
        etConfirmation = findViewById(R.id.etConfirmation);
        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkRegister();
            }
        });

    }

    private void checkRegister(){
        name = etName.getText().toString();
        matricula = etMatricula.getText().toString();
        email = etEmail.getText().toString();
        password = etPassword.getText().toString();
        confirmation = etConfirmation.getText().toString();

        if(name.isEmpty() || email.isEmpty() || password.isEmpty() || matricula.isEmpty()){
            alertFail("Nombre, Correo, Username and Contraseña es requerido");
        }
        else if(!password.equals(confirmation)){
            alertFail("Las contraseñas no coinciden");
        }
        else{
            sendRegister();
        }
    }

    private void sendRegister() {
        JSONObject params = new JSONObject();
        try {
            params.put( "name", name);
            params.put( "egresado_matricula", matricula);
            params.put( "email", email);
            params.put( "password", password);
            params.put( "password_confirmation", confirmation);
        } catch (JSONException e){
            e.printStackTrace();
        }
        String data = params.toString();
        String url = getString(R.string.api_server)+"/register";

        new Thread(new Runnable() {
            @Override
            public void run() {
                Http http = new Http(com.example.appegresados.RegisterActivity.this, url);
                http.setMethod("post");
                http.setData(data);
                http.send();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Integer code = http.getStatusCode();
                        if (code == 201 || code == 208) {
                            alertSuccess("Register Successfuly. ");
                        } else if (code == 422) {
                            try {
                                JSONObject response = new JSONObject(http.getResponse());
                                String msg = response.getString("message");
                                alertFail(msg);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(com.example.appegresados.RegisterActivity.this, "Error" + code, Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        }).start();
    }

    private void alertSuccess(String s) {
        new AlertDialog.Builder(this)
                .setTitle("Success")
                .setIcon(R.drawable.ic_success)
                .setMessage(s)
                .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onBackPressed();
                    }
                }).show();
    }

    private void alertFail(String s) {
        new AlertDialog.Builder(this)
            .setTitle("Failed")
            .setIcon(R.drawable.ic_warning)
            .setMessage(s)
            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).show();
    }

}