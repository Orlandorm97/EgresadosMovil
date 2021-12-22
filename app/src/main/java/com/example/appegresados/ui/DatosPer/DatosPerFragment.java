package com.example.appegresados.ui.DatosPer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.appegresados.Http;
import com.example.appegresados.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class DatosPerFragment extends Fragment {
    TextView tvName, tvGenero, tvFecha, tvTelefono, tvUrl, tvDni;
    ImageView IvPerfil;
    Button btEdit_datosper;
    String token;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_datosper, container, false);
        tvName = (TextView) v.findViewById(R.id.tvName);
        tvGenero = (TextView) v.findViewById(R.id.tvGenero);
        tvFecha = (TextView) v.findViewById(R.id.tvFecha);
        tvTelefono = (TextView) v.findViewById(R.id.tvTelefono);
        tvFecha = (TextView) v.findViewById(R.id.tvFecha);
        tvUrl = (TextView) v.findViewById(R.id.tvUrl);
        tvDni = (TextView) v.findViewById(R.id.tvDni);
        getUser();

        btEdit_datosper = (Button) v.findViewById(R.id.btEdit_datosper);


        Fragment fragment = new Fragment();

        Bundle bundle = getActivity().getIntent().getExtras();

        token = bundle.getString("token");

        bundle.putString("token", token);

        fragment.setArguments(bundle);

        //Log.d("token", "------"+ token);



        btEdit_datosper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.nav_host_fragment, fragment);
                fr.replace(R.id.nav_host_fragment, new DatosPerEditFragment());
                fr.commit();
            }
        });


       return v;
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
                Http http = new Http(getContext(),url);
                http.setMethod("GET");
                http.setToken(true);
                http.send();

                getActivity().runOnUiThread(new Runnable() {
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
                                String telefono = response.getString("celular");
                                String dni = capitalize(response.getString("dni"));
                                String url = capitalize(response.getString("url"));


                                String nombre_completo = ap_paterno+ " "+ ap_materno+ ", "+ nombres;



                                File imgFile = new  File("C:/Users/Orlando/Desktop/Egresados/Sprint-I/Login/public/storage/imagenes/subfolder/2016200186/915d865c231e7dbf0ba3e43a67870d7d.jpg");//Your file path
                                Log.d("Imagen", "*************"+ imgFile);

                                if(imgFile.exists()){
                                    ImageView myImage = new ImageView(getContext());
                                    myImage.setImageURI(Uri.fromFile(imgFile));

                                    Log.d("Imagen", "***********");
                                }



                                tvName.append(nombre_completo);
                                tvGenero.append(genero);
                                tvFecha.append(fecha);
                                tvTelefono.append(telefono);
                                tvDni.append(dni);
                            }catch(JSONException e){
                                e.printStackTrace();
                            }
                        }
                        else{
                            Toast.makeText(getContext(), "Error "+code, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }).start();
    }

}