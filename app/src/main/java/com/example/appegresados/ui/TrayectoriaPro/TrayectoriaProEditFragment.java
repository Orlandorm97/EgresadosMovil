package com.example.appegresados.ui.TrayectoriaPro;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Spinner;
import android.widget.Toast;

import com.example.appegresados.DataCommunication;
import com.example.appegresados.Http;
import com.example.appegresados.LocalStorage;
import com.example.appegresados.R;
import com.example.appegresados.ui.TrayectoriaAca.TrayectoriaAcaFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class TrayectoriaProEditFragment extends Fragment {
    Button bt_volver, bt_eliminar, bt_editar;
    EditText etPais, etDescripcion, etEmpresa, etActividad, etArea, etPuesto, etSubarea, etFechai, etFechaf;
    String pais, descripcion, empresa, actividad, nivel, area, puesto, subarea, fechai, fechaf, token, matricula, id_profesion;
    LocalStorage localStorage;
    DataCommunication mCallback;
    Spinner dropdown, etNivel;
    ArrayAdapter adapter;
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
        View v = inflater.inflate(R.layout.fragment_trayectoria_pro_edit, container, false);

        etActividad = (EditText) v.findViewById(R.id.etActividad);
        etPais = (EditText) v.findViewById(R.id.etPais);
        etDescripcion = (EditText) v.findViewById(R.id.etDescripcion);
        etArea = (EditText) v.findViewById(R.id.etArea);
        etNivel = (Spinner) v.findViewById(R.id.spinnetNivel);
        etSubarea = (EditText) v.findViewById(R.id.etSubarea);
        etEmpresa = (EditText) v.findViewById(R.id.etEmpresa);
        etPuesto = (EditText) v.findViewById(R.id.etPuesto);
        etFechai = (EditText) v.findViewById(R.id.etFechai);
        etFechaf = (EditText) v.findViewById(R.id.etFechaf);

        bt_eliminar = (Button) v.findViewById(R.id.bt_eliminar);
        bt_volver = (Button) v.findViewById(R.id.bt_volver);
        bt_editar = (Button) v.findViewById(R.id.bt_editar);

        try {
            mCallback = (DataCommunication) mActivity;
            id_profesion = mCallback.getMyVariableZ();
            Log.d("ID EN EDIT ---- ",""+id_profesion);
            getTrayPro();
        } catch (ClassCastException e) {
            throw new ClassCastException(mActivity.toString()
                    + " must implement DataCommunication");
        }

        bt_volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fra = getFragmentManager().beginTransaction();
                fra.replace(R.id.nav_host_fragment, new TrayectoriaProFragment());
                fra.commit();
            }
        });

        bt_editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRegistros();
            }
        });

        bt_eliminar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                deleteRegistro();
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.nav_host_fragment, new TrayectoriaProFragment());
                fr.commit();
            }
        });

        dropdown  = v.findViewById(R.id.spinnetNivel);
        //create a list of items for the spinner.
        String[] items = new String[]{"Junior", "Senior"};
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        adapter = new ArrayAdapter<String>(mActivity, android.R.layout.simple_spinner_dropdown_item, items);
        //set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);

        return v;
    }

    private void getTrayPro() {
        String url = getString(R.string.api_server)+"/trayectoriapro";
        new Thread(new Runnable() {
            @Override
            public void run() {
                com.example.appegresados.Http http = new com.example.appegresados.Http(mActivity,url);
                http.setMethod("GET");
                http.setToken(true);
                http.send();

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Integer code = http.getStatusCode();
                        if(code == 200){
                            try {
                                JSONObject response= new JSONObject(http.getResponse());
                                JSONArray responseArr2 =  response.getJSONArray("egresados");

                                Integer countMa = responseArr2.length();

                                Log.d("CANTIDAD DE MAESTRIAS","---"+countMa);

                                for (int i=0; i<=countMa; i++){
                                    JSONObject responseT = new JSONObject(responseArr2.getString(i));
                                    Log.d("RESPONSE AAAA ","---"+responseT);
                                    String id = responseT.getString("id_profesion");
                                    Log.d("RECORRIDO DEL ID","---"+id +"="+id_profesion );

                                    if(id==id_profesion){
                                        Log.d("RESPONSE DEL IF","---"+id + " = "+ id_profesion);

                                        String empresa = responseT.getString("empresa");
                                        String actividad = responseT.getString("actividad_empresa");
                                        String puesto = responseT.getString("puesto");
                                        String nivel = responseT.getString("nivel_experiencia");
                                        String area = responseT.getString("area_puesto");
                                        String subarea = responseT.getString("subarea");
                                        String pais = responseT.getString("pais");
                                        String fechai = responseT.getString("fecha_inicio");
                                        String fechaf = responseT.getString("fecha_finalizacion");
                                        String descripcion = responseT.getString("descripcion_responsabilidades");

                                        etActividad.setText(actividad);
                                        etPais.setText(pais);
                                        etDescripcion.setText(descripcion);
                                        dropdown.setSelection(adapter.getPosition(nivel));
                                        etArea.setText(area);
                                        etSubarea.setText(subarea);
                                        etEmpresa.setText(empresa);
                                        etPuesto.setText(puesto);
                                        etFechai.setText(fechai);
                                        etFechaf.setText(fechaf);

                                        break;
                                    }
                                }

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
        etActividad = (EditText) mActivity.findViewById(R.id.etActividad);
        etPais = (EditText) mActivity.findViewById(R.id.etPais);
        etDescripcion = (EditText) mActivity.findViewById(R.id.etDescripcion);
        etArea = (EditText) mActivity.findViewById(R.id.etArea);
        etSubarea = (EditText) mActivity.findViewById(R.id.etSubarea);
        etEmpresa = (EditText) mActivity.findViewById(R.id.etEmpresa);
        etPuesto = (EditText) mActivity.findViewById(R.id.etPuesto);
        etFechai = (EditText) mActivity.findViewById(R.id.etFechai);
        etFechaf = (EditText) mActivity.findViewById(R.id.etFechaf);

        actividad = etActividad.getText().toString();
        pais = etPais.getText().toString();
        descripcion = etDescripcion.getText().toString();
        area = etArea.getText().toString();
        subarea = etSubarea.getText().toString();
        empresa = etEmpresa.getText().toString();
        puesto = etPuesto.getText().toString();
        fechai = etFechai.getText().toString();
        fechaf = etFechaf.getText().toString();

        JSONObject params = new JSONObject();
        try {
            params.put( "actividad_empresa", actividad);
            params.put( "pais", pais);
            params.put( "descripcion_responsabilidades", descripcion);
            params.put( "nivel_experiencia", nivel);
            params.put( "area_puesto", area);
            params.put( "subarea", subarea);
            params.put( "empresa", empresa);
            params.put( "puesto", puesto);
            params.put( "fecha_inicio", fechai);
            params.put( "fecha_finalizacion", fechaf);
        } catch (JSONException e){
            e.printStackTrace();
        }
        String data = params.toString();
        String urlSend = getString(R.string.api_server)+"/updatetraypro/"+ id_profesion;
        Log.d("URL SEND", "********"+ data);

        new Thread(new Runnable() {
            @Override
            public void run() {
                Http http = new Http(mActivity, urlSend);
                http.setMethod("put");
                http.setData(data);
                http.setToken(true);
                Log.d("DATA UPDATE", "********"+ data);
                http.send();

                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Integer code = http.getStatusCode();
                        Log.d("TAG", "*********************************"+ code);
                        if(code == 200){
                            try{
                                JSONObject response = new JSONObject(http.getResponse());
                                Fragment fragment = new Fragment();
                                Bundle bundle = new Bundle();
                                //Add your data to bundle
                                fragment.setArguments(bundle);
                                FragmentTransaction fr = getFragmentManager().beginTransaction();
                                fr.replace(R.id.nav_host_fragment, fragment);
                                fr.replace(R.id.nav_host_fragment, new TrayectoriaProFragment());
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

    private void deleteRegistro() {
        String urlSend = getString(R.string.api_server)+"/deletetraypro/"+ id_profesion;
        Log.d("URL SEND", "********"+ urlSend);

        new Thread(new Runnable() {
            @Override
            public void run() {
                Http http = new Http(mActivity, urlSend);
                http.setMethod("delete");
                http.setToken(true);
                http.send();

                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Integer code = http.getStatusCode();
                        if (code == 200) {
                        } else if (code == 422) {
                            try {
                                JSONObject response = new JSONObject(http.getResponse());
                                String msg = response.getString("message");
                                alertFail(msg);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(mActivity, "Error" + code, Toast.LENGTH_SHORT).show();

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