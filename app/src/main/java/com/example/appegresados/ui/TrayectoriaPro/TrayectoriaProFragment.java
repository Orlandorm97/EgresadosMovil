package com.example.appegresados.ui.TrayectoriaPro;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.appegresados.Http;
import com.example.appegresados.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TrayectoriaProFragment extends Fragment {
    TextView perfilGrado, perfilFechaI, perfilFechaF, maestriaCarrera, maestriaGrado, maestriaInstitucion, maestriaPais, maestriaFechaI, maestriaFechaF, doctoradoCarrera, doctoradoGrado, doctoradoInstitucion, doctoradoPais, doctoradoFechaI, doctoradoFechaF;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_trayectoriapro, container, false);

        getTrayectoriaPro();

        return v;
    }


    private void getTrayectoriaPro() {
        String url = getString(R.string.api_server)+"/trayectoriapro";
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
                                JSONObject response = new JSONObject(http.getResponse());
                                JSONArray responseArr1 =  response.getJSONArray("egresados");
                                JSONObject responseP = new JSONObject(responseArr1.getString(0));

                                Integer countMa = responseArr1.length();

                                //Log.d("response", "***"+ responseDo);
                                Log.d("response", "***"+ responseP);

                                /*
                                LinearLayout2
                                android:layout_width="match_parent"
                                android:layout_height="0dp"
                                android:layout_weight="1"
                                android:orientation="horizontal">*/

                                LinearLayout linearLayoutM = (LinearLayout) getActivity().findViewById(R.id.linearlayout1);

                                LinearLayout.LayoutParams lLparams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
                                lLparams.setMargins(0,0,0,15);

                                LinearLayout.LayoutParams fechafParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
                                fechafParams.setMargins(0,0,0,40);


                                Drawable line = getResources().getDrawable(R.drawable.custom_line);
                                /*
                                TextView
                                <TextView
                                    android:text="CARRERA PROFESIONAL   "
                                    android:textStyle="bold"
                                    android:textColor="@color/black"
                                    android:layout_marginLeft="20dp"
                                    android:layout_width="0dp"
                                    android:layout_height="wrap_content"
                                    android:layout_weight="1"/>

                                <TextView
                                    android:id="@+id/doctoradoCarrera"
                                    android:gravity="left"
                                    android:textColor="@color/black"
                                    android:layout_marginLeft="20dp"
                                    android:layout_width="0dp"
                                    android:layout_height="wrap_content"
                                    android:layout_weight="1"/>
                                 */

                                Drawable borde = getResources().getDrawable(R.drawable.custom_border);


                                for( int i = 0; i < countMa; i++)
                                {
                                    JSONObject responseM = new JSONObject(responseArr1.getString(i));

                                    LinearLayout lLAll = new LinearLayout(getContext());
                                    lLAll.setOrientation(LinearLayout.VERTICAL);
                                    lLAll.setBackground(borde);
                                    lLAll.setLayoutParams(lLparams);


                                    LinearLayout lL1 = new LinearLayout(getContext());
                                    lL1.setOrientation(LinearLayout.HORIZONTAL);
                                    lL1.setLayoutParams(lLparams);

                                    LinearLayout lL2 = new LinearLayout(getContext());
                                    lL2.setOrientation(LinearLayout.HORIZONTAL);
                                    lL2.setLayoutParams(lLparams);

                                    LinearLayout lL3 = new LinearLayout(getContext());
                                    lL3.setOrientation(LinearLayout.HORIZONTAL);
                                    lL3.setLayoutParams(lLparams);

                                    LinearLayout lL4 = new LinearLayout(getContext());
                                    lL4.setOrientation(LinearLayout.HORIZONTAL);
                                    lL4.setLayoutParams(lLparams);

                                    LinearLayout lL5 = new LinearLayout(getContext());
                                    lL5.setOrientation(LinearLayout.HORIZONTAL);
                                    lL5.setLayoutParams(lLparams);

                                    LinearLayout lL6 = new LinearLayout(getContext());
                                    lL6.setOrientation(LinearLayout.HORIZONTAL);

                                    LinearLayout lL7 = new LinearLayout(getContext());
                                    lL7.setOrientation(LinearLayout.HORIZONTAL);
                                    lL7.setLayoutParams(lLparams);

                                    LinearLayout lL8 = new LinearLayout(getContext());
                                    lL8.setOrientation(LinearLayout.HORIZONTAL);
                                    lL8.setLayoutParams(lLparams);

                                    LinearLayout lL9 = new LinearLayout(getContext());
                                    lL9.setOrientation(LinearLayout.HORIZONTAL);
                                    lL9.setLayoutParams(lLparams);

                                    LinearLayout lL10 = new LinearLayout(getContext());
                                    lL10.setOrientation(LinearLayout.HORIZONTAL);
                                    lL10.setLayoutParams(fechafParams);

                                    //lL6.setBackground(line);

                                                                        LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);
                                    tvParams.setMargins(40,0,0,0);

                                    TextView tvEmpresa1 = new TextView(getContext());
                                    tvEmpresa1.setTextColor(Color.parseColor("#000000"));
                                    tvEmpresa1.setTypeface(Typeface.DEFAULT_BOLD);
                                    tvEmpresa1.setText("EMPRESA");
                                    tvEmpresa1.setLayoutParams(tvParams);

                                    TextView tvEmpresa2 = new TextView(getContext());
                                    tvEmpresa2.setTextColor(Color.parseColor("#000000"));
                                    tvEmpresa2.setLayoutParams(tvParams);

                                    TextView tvActividad1 = new TextView(getContext());
                                    tvActividad1.setTextColor(Color.parseColor("#000000"));
                                    tvActividad1.setText("ACTIVIDAD DE LA EMPRESA");
                                    tvActividad1.setTypeface(Typeface.DEFAULT_BOLD);
                                    tvActividad1.setLayoutParams(tvParams);

                                    TextView tvActividad2 = new TextView(getContext());
                                    tvActividad2.setTextColor(Color.parseColor("#000000"));
                                    tvActividad2.setLayoutParams(tvParams);

                                    TextView tvPuesto1 = new TextView(getContext());
                                    tvPuesto1.setTextColor(Color.parseColor("#000000"));
                                    tvPuesto1.setText("PUESTO");
                                    tvPuesto1.setTypeface(Typeface.DEFAULT_BOLD);
                                    tvPuesto1.setLayoutParams(tvParams);

                                    TextView tvPuesto2 = new TextView(getContext());
                                    tvPuesto2.setTextColor(Color.parseColor("#000000"));
                                    tvPuesto2.setLayoutParams(tvParams);

                                    TextView tvNivel1 = new TextView(getContext());
                                    tvNivel1.setTextColor(Color.parseColor("#000000"));
                                    tvNivel1.setText("NIVEL DE EXPERIENCIA");
                                    tvNivel1.setTypeface(Typeface.DEFAULT_BOLD);
                                    tvNivel1.setLayoutParams(tvParams);

                                    TextView tvNivel2 = new TextView(getContext());
                                    tvNivel2.setTextColor(Color.parseColor("#000000"));
                                    tvNivel2.setLayoutParams(tvParams);

                                    TextView tvArea1 = new TextView(getContext());
                                    tvArea1.setTextColor(Color.parseColor("#000000"));
                                    tvArea1.setText("ÁREA DE PUESTO\t");
                                    tvArea1.setTypeface(Typeface.DEFAULT_BOLD);
                                    tvArea1.setLayoutParams(tvParams);

                                    TextView tvArea2 = new TextView(getContext());
                                    tvArea2.setTextColor(Color.parseColor("#000000"));
                                    tvArea2.setLayoutParams(tvParams);

                                    TextView tvSubrea1 = new TextView(getContext());
                                    tvSubrea1.setTextColor(Color.parseColor("#000000"));
                                    tvSubrea1.setText("SUBÁREA");
                                    tvSubrea1.setTypeface(Typeface.DEFAULT_BOLD);
                                    tvSubrea1.setLayoutParams(tvParams);

                                    TextView tvSubrea2 = new TextView(getContext());
                                    tvSubrea2.setTextColor(Color.parseColor("#000000"));
                                    tvSubrea2.setLayoutParams(tvParams);

                                    TextView tvPais1 = new TextView(getContext());
                                    tvPais1.setTextColor(Color.parseColor("#000000"));
                                    tvPais1.setText("PAÍS");
                                    tvPais1.setTypeface(Typeface.DEFAULT_BOLD);
                                    tvPais1.setLayoutParams(tvParams);

                                    TextView tvPais2 = new TextView(getContext());
                                    tvPais2.setTextColor(Color.parseColor("#000000"));
                                    tvPais2.setLayoutParams(tvParams);

                                    TextView tvFechai1 = new TextView(getContext());
                                    tvFechai1.setTextColor(Color.parseColor("#000000"));
                                    tvFechai1.setText("FECHA DE INICIO");
                                    tvFechai1.setTypeface(Typeface.DEFAULT_BOLD);
                                    tvFechai1.setLayoutParams(tvParams);

                                    TextView tvFechai2 = new TextView(getContext());
                                    tvFechai2.setTextColor(Color.parseColor("#000000"));
                                    tvFechai2.setLayoutParams(tvParams);

                                    TextView tvFechaf1 = new TextView(getContext());
                                    tvFechaf1.setTextColor(Color.parseColor("#000000"));
                                    tvFechaf1.setText("FECHA DE FINALIZACIÓN");
                                    tvFechaf1.setTypeface(Typeface.DEFAULT_BOLD);
                                    tvFechaf1.setLayoutParams(tvParams);

                                    TextView tvFechaf2 = new TextView(getContext());
                                    tvFechaf2.setTextColor(Color.parseColor("#000000"));
                                    tvFechaf2.setLayoutParams(tvParams);

                                    TextView tvRespon1 = new TextView(getContext());
                                    tvRespon1.setTextColor(Color.parseColor("#000000"));
                                    tvRespon1.setText("DESCRIPCIÓN DE RESPONSABILIDADES");
                                    tvRespon1.setTypeface(Typeface.DEFAULT_BOLD);
                                    tvRespon1.setLayoutParams(tvParams);

                                    TextView tvRespon2 = new TextView(getContext());
                                    tvRespon2.setTextColor(Color.parseColor("#000000"));
                                    tvRespon2.setLayoutParams(tvParams);


                                    tvEmpresa2.setText(responseM.getString("empresa"));
                                    tvActividad2.setText(responseM.getString("actividad_empresa"));
                                    tvPuesto2.setText(responseM.getString("puesto"));
                                    tvNivel2.setText(responseM.getString("nivel_experiencia"));
                                    tvArea2.setText(responseM.getString("area_puesto"));
                                    tvSubrea2.setText(responseM.getString("subarea"));
                                    tvPais2.setText(responseM.getString("pais"));
                                    tvFechai2.setText(responseM.getString("fecha_inicio"));
                                    tvFechaf2.setText(responseM.getString("fecha_finalizacion"));
                                    tvRespon2.setText(responseM.getString("descripcion_responsabilidades"));

                                    lL1.addView(tvEmpresa1);
                                    lL2.addView(tvActividad1);
                                    lL3.addView(tvPuesto1);
                                    lL4.addView(tvNivel1);
                                    lL5.addView(tvArea1);
                                    lL6.addView(tvSubrea1);
                                    lL7.addView(tvPais1);
                                    lL8.addView(tvFechai1);
                                    lL9.addView(tvFechaf1);
                                    lL10.addView(tvRespon1);

                                    lL1.addView(tvEmpresa2);
                                    lL2.addView(tvActividad2);
                                    lL3.addView(tvPuesto2);
                                    lL4.addView(tvNivel2);
                                    lL5.addView(tvArea2);
                                    lL6.addView(tvSubrea2);
                                    lL7.addView(tvPais2);
                                    lL8.addView(tvFechai2);
                                    lL9.addView(tvFechaf2);
                                    lL10.addView(tvRespon2);

                                    lLAll.addView(lL1);
                                    lLAll.addView(lL2);
                                    lLAll.addView(lL3);
                                    lLAll.addView(lL4);
                                    lLAll.addView(lL5);
                                    lLAll.addView(lL6);
                                    lLAll.addView(lL7);
                                    lLAll.addView(lL8);
                                    lLAll.addView(lL9);
                                    lLAll.addView(lL10);

                                    linearLayoutM.addView(lLAll);
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



}