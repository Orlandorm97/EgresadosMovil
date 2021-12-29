package com.example.appegresados.ui.DatosPer;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appegresados.DrawerNav;
import com.example.appegresados.Http;
import com.example.appegresados.LocalStorage;
import com.example.appegresados.LoginActivity;
import com.example.appegresados.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class DatosPerEditFragment extends Fragment {
    EditText etName, etGenero, etFecha, etCelular, etDni, etApPaterno, etApMaterno;
    String nombres, genero, fecha, celular, ap_materno, ap_paterno, matricula,telefono, dni, nombre_completo, token;
    Button bt_volver, bt_editar;
    Spinner dropdown;
    LocalStorage localStorage;

    Activity mActivity;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_datos_per_edit, container, false);
        localStorage = new LocalStorage(getContext());

        bt_volver = (Button) v.findViewById(R.id.bt_volver);
        bt_editar = (Button) v.findViewById(R.id.bt_editar);
        etName = (EditText) v.findViewById(R.id.etName);
        etFecha = (EditText) v.findViewById(R.id.etFecha);
        etCelular = (EditText) v.findViewById(R.id.etCelular);
        etDni = (EditText) v.findViewById(R.id.etDni);
        etApPaterno = (EditText) v.findViewById(R.id.etApPaterno);
        etApMaterno = (EditText) v.findViewById(R.id.etApMaterno);

        Bundle bundle = mActivity.getIntent().getExtras();

        token = bundle.getString("token");

        bundle.putString("token en fragment edit", token);


        bt_volver.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    etApPaterno = (EditText) v.findViewById(R.id.etApPaterno);
                    etApMaterno = (EditText) v.findViewById(R.id.etApMaterno);
                    etName = (EditText) v.findViewById(R.id.etName);
                    etFecha = (EditText) v.findViewById(R.id.etFecha);
                    etCelular = (EditText) v.findViewById(R.id.etCelular);
                    etDni = (EditText) v.findViewById(R.id.etDni);

                    FragmentTransaction fr = getFragmentManager().beginTransaction();
                    fr.replace(R.id.nav_host_fragment, new DatosPerFragment());
                    fr.commit();
                }
            });

            dropdown  = v.findViewById(R.id.spinnerGenero);
            //create a list of items for the spinner.
            String[] items = new String[]{"Masculino", "Femenino"};
            //create an adapter to describe how the items are displayed, adapters are used in several places in android.
            //There are multiple variations of this, but this is the basic variant.
            ArrayAdapter adapter = new ArrayAdapter<String>(mActivity, android.R.layout.simple_spinner_dropdown_item, items);
            //set the spinners adapter to the previously created one.
            dropdown.setAdapter(adapter);


        Log.d("response", "***"+ dropdown.getSelectedItem());

            getUser();

            bt_editar.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    sendRegistros();

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


    public void getUser() {
        String url = getString(R.string.api_server)+"/datospersonales";
        new Thread(new Runnable() {
            @Override
            public void run() {
                Http http = new Http(getContext(),url);
                http.setMethod("GET");
                http.setToken(true);
                http.send();

                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Integer code = http.getStatusCode();
                        if(code == 200){
                            try {
                                JSONObject response = new JSONObject(http.getResponse());
                                JSONArray responseArr =  response.getJSONArray("egresados");
                                JSONObject responseDP = new JSONObject(responseArr.getString(0));


                                Log.d("RESPONSE GETUSER", "***"+ response);

                                matricula = responseDP.getString("matricula");
                                ap_paterno = capitalize(responseDP.getString("ap_paterno"));
                                ap_materno = capitalize(responseDP.getString("ap_materno"));
                                nombres = capitalize(responseDP.getString("nombres"));
                                genero = capitalize(responseDP.getString("genero"));
                                fecha = responseDP.getString("fecha_nacimiento");
                                celular = responseDP.getString("celular");

                                nombre_completo = ap_paterno+ " "+ ap_materno+ ", "+ nombres;

                                etApMaterno.setText(ap_materno);
                                etApPaterno.setText(ap_paterno);
                                etName.setText(nombres);
                                etFecha.setText(fecha);
                                etCelular.setText(celular);
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


    private void sendRegistros() {
        String generoDD = String.valueOf(dropdown.getSelectedItem());
        etName = (EditText) mActivity.findViewById(R.id.etName);
        etFecha = (EditText) mActivity.findViewById(R.id.etFecha);
        etCelular = (EditText) mActivity.findViewById(R.id.etCelular);
        etApPaterno = (EditText) mActivity.findViewById(R.id.etApPaterno);
        etApMaterno = (EditText) mActivity.findViewById(R.id.etApMaterno);

        nombres = etName.getText().toString();
        fecha = etFecha.getText().toString();
        ap_paterno = etApPaterno.getText().toString();
        ap_paterno = etApPaterno.getText().toString();
        ap_materno = etApMaterno.getText().toString();
        celular = etCelular.getText().toString();

        Log.d("GENERO", "********"+ generoDD);

        JSONObject params = new JSONObject();
        try {
            params.put( "matricula", matricula);
            params.put( "ap_paterno", ap_paterno);
            params.put( "ap_materno", ap_materno);
            params.put( "nombres", nombres);
            params.put( "genero", generoDD);
            params.put( "fecha_nacimiento", fecha);
            params.put( "celular", celular);
        } catch (JSONException e){
            e.printStackTrace();
        }
        String data = params.toString();
        String urlSend = getString(R.string.api_server)+"/updatedp/"+ matricula;
        Log.d("URL SEND", "********"+ data);

        new Thread(new Runnable() {
            @Override
            public void run() {
                Http http = new Http(mActivity, urlSend);
                http.setMethod("put");
                http.setData(data);
                http.setToken(true);
                Log.d("DATA UPDATE", "********"+ data);
                Log.d("TOKEN", "********"+ token);
                http.send();

                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Integer code = http.getStatusCode();
                        Log.d("TAG", "*********************************"+ code);
                        if(code == 200){
                            try{
                                JSONObject response = new JSONObject(http.getResponse());
                                localStorage.setToken(token);
                                Fragment fragment = new Fragment();
                                Bundle bundle = new Bundle();
                                //Add your data to bundle
                                bundle.putString("token", token);
                                fragment.setArguments(bundle);
                                FragmentTransaction fr = getFragmentManager().beginTransaction();
                                fr.replace(R.id.nav_host_fragment, fragment);
                                fr.replace(R.id.nav_host_fragment, new DatosPerFragment());
                                fr.commit();

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
                            Toast.makeText(mActivity, "Error aaa"+code, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }).start();

    }


    private void alertSuccess(String s) {
        new AlertDialog.Builder(mActivity)
                .setTitle("Success")
                .setIcon(R.drawable.ic_success)
                .setMessage(s)
                .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mActivity.onBackPressed();
                    }
                }).show();
    }

    private void alertFail(String s) {
        new AlertDialog.Builder(mActivity)
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