package com.example.appegresados.ui.TrayectoriaAca;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.appegresados.DataCommunication;
import com.example.appegresados.Http;
import com.example.appegresados.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TrayectoriaAcaEditDoctoradoFragment extends Fragment {
    EditText etPais, etFechai, etFechaf, etInstitucion;
    String id_doctorado, pais, fechai, fechaf, institucion;
    Button bt_volver, bt_editar, bt_eliminar;
    DataCommunication mCallback;
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
        View v = inflater.inflate(R.layout.fragment_trayectoria_aca_edit_doctorado, container, false);


        etPais = (EditText) v.findViewById(R.id.etPais);
        etInstitucion = (EditText) v.findViewById(R.id.etInst);
        etFechai = (EditText) v.findViewById(R.id.etFechai);
        etFechaf = (EditText) v.findViewById(R.id.etFechaf);

        try {
            mCallback = (DataCommunication) mActivity;
            id_doctorado = mCallback.getMyVariableY();
            getTrayAca();
        } catch (ClassCastException e) {
            throw new ClassCastException(mActivity.toString()
                    + " must implement DataCommunication");
        }

        Log.d("ID POR FAVOR", "---" + id_doctorado);
        bt_volver = (Button) v.findViewById(R.id.bt_volver);
        bt_editar = (Button) v.findViewById(R.id.bt_editar);
        bt_eliminar = (Button) v.findViewById(R.id.bt_eliminar);

        bt_volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fra = getFragmentManager().beginTransaction();
                fra.replace(R.id.nav_host_fragment, new TrayectoriaAcaFragment());
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
                fr.replace(R.id.nav_host_fragment, new TrayectoriaAcaFragment());
                fr.commit();
            }
        });

        return v;
    }

    private void getTrayAca() {
        String url = getString(R.string.api_server) + "/trayectoriaaca";
        new Thread(new Runnable() {
            @Override
            public void run() {
                com.example.appegresados.Http http = new com.example.appegresados.Http(mActivity, url);
                http.setMethod("GET");
                http.setToken(true);
                http.send();

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Integer code = http.getStatusCode();
                        if (code == 200) {
                            try {
                                JSONObject response = new JSONObject(http.getResponse());
                                JSONArray responseArr3 =  response.getJSONArray("egresados1");

                                Integer countMa = responseArr3.length();

                                Log.d("CANTIDAD DE doctoradoS", "---" + countMa);

                                for (int i = 0; i <= countMa; i++) {
                                    JSONObject responseMa = new JSONObject(responseArr3.getString(i));
                                    Log.d("RESPONSE AAAA ", "---" + responseMa);
                                    String id = responseMa.getString("id_doctorado");
                                    Log.d("RECORRIDO DEL ID", "---" + id + "=" + id_doctorado);

                                    if (id == id_doctorado) {
                                        Log.d("RESPONSE DEL IF", "---" + id + " = " + id_doctorado);

                                        String pais = responseMa.getString("doctorado_pais");
                                        String institucion = responseMa.getString("doctorado_institución");
                                        String fechai = responseMa.getString("doctorado_fecha_inicial");
                                        String fechaf = responseMa.getString("doctorado_fecha_final");

                                        etPais.setText(pais);
                                        etInstitucion.setText(institucion);
                                        etFechai.setText(fechai);
                                        etFechaf.setText(fechaf);

                                        break;
                                    }
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(getContext(), "Error " + code, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }).start();
    }

    private void sendRegistros() {
        etPais = (EditText) mActivity.findViewById(R.id.etPais);
        etInstitucion = (EditText) mActivity.findViewById(R.id.etInst);
        etFechai = (EditText) mActivity.findViewById(R.id.etFechai);
        etFechaf = (EditText) mActivity.findViewById(R.id.etFechaf);

        pais = etPais.getText().toString();
        institucion = etInstitucion.getText().toString();
        fechai = etFechai.getText().toString();
        fechaf = etFechaf.getText().toString();

        JSONObject params = new JSONObject();
        try {
            params.put("doctorado_pais", pais);
            params.put("doctorado_institución", institucion);
            params.put("doctorado_fecha_inicial", fechai);
            params.put("doctorado_fecha_final", fechaf);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String data = params.toString();
        String urlSend = getString(R.string.api_server) + "/updatedoc/" + id_doctorado;
        Log.d("URL SEND", "********" + data);

        new Thread(new Runnable() {
            @Override
            public void run() {
                Http http = new Http(mActivity, urlSend);
                http.setMethod("put");
                http.setData(data);
                http.setToken(true);
                Log.d("DATA UPDATE", "********" + data);
                http.send();

                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Integer code = http.getStatusCode();
                        Log.d("TAG", "*********************************" + code);
                        if (code == 200) {
                            try {
                                JSONObject response = new JSONObject(http.getResponse());
                                Fragment fragment = new Fragment();
                                Bundle bundle = new Bundle();
                                //Add your data to bundle
                                fragment.setArguments(bundle);
                                FragmentTransaction fr = getFragmentManager().beginTransaction();
                                fr.replace(R.id.nav_host_fragment, fragment);
                                fr.replace(R.id.nav_host_fragment, new TrayectoriaAcaFragment());
                                fr.commit();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else if (code == 422) {
                            try {
                                JSONObject response = new JSONObject(http.getResponse());
                                String msg = response.getString("message");
                                alertFail(msg);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else if (code == 401) {
                            try {
                                JSONObject response = new JSONObject(http.getResponse());
                                String msg = response.getString("message");
                                alertFail(msg);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(mActivity, "Error aaa" + code, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }).start();

    }


    private void deleteRegistro() {
        String urlSend = getString(R.string.api_server)+"/deletedoc/"+ id_doctorado;
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