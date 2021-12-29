package com.example.appegresados.ui.TrayectoriaPro;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.appegresados.Http;
import com.example.appegresados.LocalStorage;
import com.example.appegresados.R;
import com.example.appegresados.ui.TrayectoriaAca.TrayectoriaAcaFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class TrayectoriaProAddFragment extends Fragment {
    Button bt_volver, bt_agregar;
    EditText etPais, etDescripcion, etEmpresa, etActividad, etNivel, etArea, etPuesto, etSubarea, etFechai, etFechaf;
    String pais, descripcion, empresa, actividad, nivel, area, puesto, subarea, fechai, fechaf, token, matricula;
    LocalStorage localStorage;
    Spinner dropdown;
    final Calendar myCalendar= Calendar.getInstance();

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
        View v = inflater.inflate(R.layout.fragment_trayectoria_pro_add, container, false);

        etActividad = (EditText) v.findViewById(R.id.etActividad);
        etPais = (EditText) v.findViewById(R.id.etPais);
        etDescripcion = (EditText) v.findViewById(R.id.etDescripcion);
        etArea = (EditText) v.findViewById(R.id.etArea);
        etSubarea = (EditText) v.findViewById(R.id.etSubarea);
        etEmpresa = (EditText) v.findViewById(R.id.etEmpresa);
        etPuesto = (EditText) v.findViewById(R.id.etPuesto);

        etFechai = (EditText) v.findViewById(R.id.etFechai);
        etFechaf = (EditText) v.findViewById(R.id.etFechaf);

        bt_agregar = (Button) v.findViewById(R.id.bt_agregar);
        bt_volver = (Button) v.findViewById(R.id.bt_volver);

        bt_volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.nav_host_fragment, new TrayectoriaProFragment());
                fr.commit();
            }
        });

        etFechai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // TODO Auto-generated method stub
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        String myFormat = "yyyy-MM-dd"; // your format
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());

                        etFechai.setText(sdf.format(myCalendar.getTime()));
                    }
                };

                new DatePickerDialog(mActivity, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        etFechaf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // TODO Auto-generated method stub
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        String myFormat = "yyyy-MM-dd"; // your format
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());

                        etFechaf.setText(sdf.format(myCalendar.getTime()));
                    }
                };
                new DatePickerDialog(mActivity, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        dropdown  = v.findViewById(R.id.spinnetNivel);
        //create a list of items for the spinner.
        String[] items = new String[]{"Junior", "Senior"};
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter adapter = new ArrayAdapter<String>(mActivity, android.R.layout.simple_spinner_dropdown_item, items);
        //set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);

        getUser();

        bt_agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRegistros();
                final FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.nav_host_fragment, new TrayectoriaProFragment());
                fr.commit();
            }
        });

        return v;
    }


    private void getUser() {
        String url = getString(R.string.api_server)+"/datospersonales";
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

                                matricula = response.getString("matricula");

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
        nivel = String.valueOf(dropdown.getSelectedItem());
        etEmpresa = (EditText) mActivity.findViewById(R.id.etEmpresa);
        etDescripcion = (EditText) mActivity.findViewById(R.id.etDescripcion);
        etPuesto = (EditText) mActivity.findViewById(R.id.etPuesto);
        etSubarea = (EditText) mActivity.findViewById(R.id.etSubarea);
        etArea = (EditText) mActivity.findViewById(R.id.etArea);
        etPais = (EditText) mActivity.findViewById(R.id.etPais);
        etActividad = (EditText) mActivity.findViewById(R.id.etActividad);
        etFechai = (EditText) mActivity.findViewById(R.id.etFechai);
        etFechaf = (EditText) mActivity.findViewById(R.id.etFechaf);

        empresa = etEmpresa.getText().toString();
        descripcion = etDescripcion.getText().toString();
        puesto = etPuesto.getText().toString();
        subarea = etSubarea.getText().toString();
        area = etArea.getText().toString();
        actividad = etActividad.getText().toString();
        pais = etPais.getText().toString();
        fechai = etFechai.getText().toString();
        fechaf = etFechaf.getText().toString();


        JSONObject params = new JSONObject();
        try {
            params.put( "empresa", empresa);
            params.put( "actividad_empresa", actividad);
            params.put( "puesto", puesto);
            params.put( "nivel_experiencia", nivel);
            params.put( "area_puesto", area);
            params.put( "subarea", subarea);
            params.put( "descripcion_responsabilidades", descripcion);
            params.put( "pais", pais);
            params.put( "fecha_inicio", fechai);
            params.put( "fecha_finalizacion", fechaf);
            params.put( "matricula", matricula);

        } catch (JSONException e){
            e.printStackTrace();
        }
        String data = params.toString();
        String urlSend = getString(R.string.api_server)+"/createtraypro";
        Log.d("URL SEND", "********"+ data);

        new Thread(new Runnable() {
            @Override
            public void run() {
                Http http = new Http(mActivity, urlSend);
                http.setMethod("post");
                http.setData(data);
                http.setToken(true);
                Log.d("DATA UPDATE", "********"+ data);
                http.send();

                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Integer code = http.getStatusCode();
                        Log.d("TAG", "*********************************"+ code);
                        if (code == 201 || code == 208) {
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