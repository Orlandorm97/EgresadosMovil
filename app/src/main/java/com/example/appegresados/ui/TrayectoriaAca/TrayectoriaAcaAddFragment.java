package com.example.appegresados.ui.TrayectoriaAca;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appegresados.Http;
import com.example.appegresados.LocalStorage;
import com.example.appegresados.R;
import com.example.appegresados.ui.DatosPer.DatosPerFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class TrayectoriaAcaAddFragment extends Fragment {
    EditText etInst, etGrado, etPais, etFechai, etFechaf;
    Button bt_volver, bt_agregar;
    String grado, pais, institucion, fechai, fechaf, matricula;
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
        View v = inflater.inflate(R.layout.fragment_trayectoria_aca_add, container, false);
        localStorage = new LocalStorage(getContext());

        bt_volver = (Button) v.findViewById(R.id.bt_volver);
        bt_agregar = (Button) v.findViewById(R.id.bt_agregar);

        etInst = (EditText) v.findViewById(R.id.etInst);
        etPais = (EditText) v.findViewById(R.id.etPais);
        etFechai = (EditText) v.findViewById(R.id.etFechai);
        etFechaf = (EditText) v.findViewById(R.id.etFechaf);

        bt_volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.nav_host_fragment, new TrayectoriaAcaFragment());
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

        dropdown  = v.findViewById(R.id.spinnetGrado);
        //create a list of items for the spinner.
        String[] items = new String[]{"Maestro", "Doctor"};
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter adapter = new ArrayAdapter<String>(mActivity, android.R.layout.simple_spinner_dropdown_item, items);
        //set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);


        bt_agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRegistros();
                final FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.nav_host_fragment, new TrayectoriaAcaFragment());
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
        grado = String.valueOf(dropdown.getSelectedItem());
        etInst = (EditText) mActivity.findViewById(R.id.etInst);
        etPais = (EditText) mActivity.findViewById(R.id.etPais);
        etFechai = (EditText) mActivity.findViewById(R.id.etFechai);
        etFechaf = (EditText) mActivity.findViewById(R.id.etFechaf);

        institucion = etInst.getText().toString();
        pais = etPais.getText().toString();
        fechai = etFechai.getText().toString();
        fechaf = etFechaf.getText().toString();

        Log.d("FECHA INICIAL", "********"+ fechai);

        JSONObject params = new JSONObject();
        try {
            params.put( "grado_academico", grado);
            params.put( "pais", pais);
            params.put( "institución", institucion);
            params.put( "fecha_inicial", fechai);
            params.put( "fecha_final", fechaf);
            params.put( "matricula", matricula);
        } catch (JSONException e){
            e.printStackTrace();
        }
        String data = params.toString();
        String urlSend = getString(R.string.api_server)+"/createtrayaca";
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
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
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