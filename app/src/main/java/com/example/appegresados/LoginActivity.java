package com.example.appegresados;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    EditText etDni, etPassword;
    Button btnLogin, btnRegister;
    ImageView logoFacebook, logoYoutube, logoInstagram;
    String dni, password;
    LocalStorage localStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setTitle("LOGIN");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        localStorage = new LocalStorage(com.example.appegresados.LoginActivity.this);

        etDni = findViewById(R.id.etDni);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        //btnRegister = findViewById(R.id.btnRegister);

        ImageView fb = (ImageView) findViewById(R.id.logoFacebook);
        ImageView yt = (ImageView) findViewById(R.id.logoYoutube);
        ImageView ig = (ImageView) findViewById(R.id.logoInstagram);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLogin();
            }
        });


        fb.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                abrirUrl("https://www.facebook.com/untelsperu");
            }
        });

        yt.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                abrirUrl("https://www.youtube.com/c/UniversidadNacionalTecnológicadeLimaSur");
            }
        });

        ig.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                abrirUrl("https://www.instagram.com/untelsoficial/");
            }
        });
    }

    private void checkLogin() {
        dni = etDni.getText().toString();
        password = etPassword.getText().toString();
        if (dni.isEmpty() || password.isEmpty()) {
            alertFail("Debe ingresar el código de matrícula y contraseña.");
        }
        else{
            sendLogin();
        }
    }

    private void sendLogin() {
        JSONObject params = new JSONObject();
        try {
            params.put("dni", dni);
            params.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String data = params.toString();
        String url = getString(R.string.api_server)+"/login";

        new Thread(new Runnable() {
            @Override
            public void run() {
                Http http = new Http(LoginActivity.this, url);
                http.setMethod("post");
                http.setData(data);
                http.send();
                Log.d("TAG", "*********************************"+ http.getResponse());


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Integer code = http.getStatusCode();
                        Log.d("TAG", "*********************************"+ code);
                        if(code == 200){
                            try{
                                JSONObject response = new JSONObject(http.getResponse());
                                String token = response.getString("token");
                                localStorage.setToken(token);
                                Intent intent = new Intent(LoginActivity.this,DrawerNav.class);
                                Bundle bundle = new Bundle();
                                //Add your data to bundle
                                bundle.putString("token", token);
                                //Add the bundle to the intent
                                intent.putExtras(bundle);
                                startActivity(intent);
                                finish();
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }
                        else if(code == 422){
                            try {
                                JSONObject response = new JSONObject(http.getResponse());
                                String msg = response.getString("message");
                                alertFail(msg);
                            }catch(JSONException e){
                                e.printStackTrace();
                            }
                        }
                        else if(code == 401){
                            try {
                                JSONObject response = new JSONObject(http.getResponse());
                                String msg = response.getString("message");
                                alertFail(msg);
                            }catch(JSONException e){
                                e.printStackTrace();
                            }
                        }
                        else{
                            Toast.makeText(com.example.appegresados.LoginActivity.this, "Error aaa"+code, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }).start();

    }

    public void abrirUrl(String url){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);

        //pass the url to intent data
        intent.setData(Uri.parse(url));

        startActivity(intent);
    }


    private void alertFail(String s) {
        new AlertDialog.Builder(this)
            .setTitle("Failed")
            .setIcon(R.drawable.ic_warning)
            .setMessage(s)
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).show();
    }
}