package com.example.appegresados.ui.TrayectoriaAca;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.appegresados.Http;
import com.example.appegresados.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class TrayectoriaAcaFragment extends Fragment {
    TextView perfilGrado, perfilFechaI, perfilFechaF, maestriaCarrera, maestriaGrado, maestriaInstitucion, maestriaPais, maestriaFechaI, maestriaFechaF, doctoradoCarrera, doctoradoGrado, doctoradoInstitucion, doctoradoPais, doctoradoFechaI, doctoradoFechaF;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_trayectoriaaca, container, false);

        perfilGrado = (TextView) v.findViewById(R.id.perfilGrado);
        perfilFechaI = (TextView) v.findViewById(R.id.perfilFechaI);
        perfilFechaF = (TextView) v.findViewById(R.id.perfilFechaF);
        /*maestriaCarrera = (TextView) v.findViewById(R.id.maestriaCarrera);
        maestriaGrado = (TextView) v.findViewById(R.id.maestriaGrado);
        maestriaInstitucion = (TextView) v.findViewById(R.id.maestriaInstitucion);
        maestriaPais = (TextView) v.findViewById(R.id.maestriaPais);
        maestriaFechaI = (TextView) v.findViewById(R.id.maestriaFechaI);
        maestriaFechaF = (TextView) v.findViewById(R.id.maestriaFechaF);
        doctoradoCarrera = (TextView) v.findViewById(R.id.doctoradoCarrera);
        doctoradoGrado = (TextView) v.findViewById(R.id.doctoradoGrado);
        doctoradoInstitucion = (TextView) v.findViewById(R.id.doctoradoInstitucion);
        doctoradoPais = (TextView) v.findViewById(R.id.doctoradoPais);
        doctoradoFechaI = (TextView) v.findViewById(R.id.doctoradoFechaI);
        doctoradoFechaF = (TextView) v.findViewById(R.id.doctoradoFechaF);
        */
        getTrayectoriaAca();

        return v;
    }


    private void getTrayectoriaAca() {
        String url = getString(R.string.api_server)+"/trayectoriaaca";
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
                                JSONObject response= new JSONObject(http.getResponse());
                                JSONArray responseArr1 =  response.getJSONArray("egresados0");
                                JSONObject responseP = new JSONObject(responseArr1.getString(0));

                                JSONArray responseArr2 =  response.getJSONArray("egresados");
                                JSONObject responseMa = new JSONObject(responseArr2.getString(0));
                                Integer countMa = responseArr2.length();

                                JSONArray responseArr3 =  response.getJSONArray("egresados1");
                                JSONObject responseDo = new JSONObject(responseArr2.getString(0));
                                Integer countDo = responseArr3.length();

                                //Log.d("response", "***"+ responseDo);
                                Log.d("response", "***"+ responseArr3.length());

                                /*
                                LinearLayout2
                                android:layout_width="match_parent"
                                android:layout_height="0dp"
                                android:layout_weight="1"
                                android:orientation="horizontal">*/

                                LinearLayout linearLayoutM = (LinearLayout) getActivity().findViewById(R.id.linearlayout2);

                                LinearLayout.LayoutParams lLparams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1);

                                LinearLayout.LayoutParams fechafParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1);
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

                                for( int i = 0; i < countMa; i++)
                                {
                                    JSONObject responseM = new JSONObject(responseArr2.getString(i));

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
                                    //lL6.setBackground(line);
                                    lL6.setLayoutParams(fechafParams);

                                    linearLayoutM.addView(lL1);
                                    linearLayoutM.addView(lL2);
                                    linearLayoutM.addView(lL3);
                                    linearLayoutM.addView(lL4);
                                    linearLayoutM.addView(lL5);
                                    linearLayoutM.addView(lL6);

                                    LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
                                    tvParams.setMargins(40,0,0,0);

                                    TextView tvCarr1 = new TextView(getContext());
                                    tvCarr1.setTextColor(Color.parseColor("#000000"));
                                    tvCarr1.setTypeface(Typeface.DEFAULT_BOLD);
                                    tvCarr1.setText("CARRERA PROFESIONAL");
                                    tvCarr1.setLayoutParams(tvParams);

                                    TextView tvCarr2 = new TextView(getContext());
                                    tvCarr2.setTextColor(Color.parseColor("#000000"));
                                    tvCarr2.setLayoutParams(tvParams);

                                    TextView tvGrado1 = new TextView(getContext());
                                    tvGrado1.setTextColor(Color.parseColor("#000000"));
                                    tvGrado1.setText("CARRERA PROFESIONAL");
                                    tvGrado1.setTypeface(Typeface.DEFAULT_BOLD);
                                    tvGrado1.setLayoutParams(tvParams);

                                    TextView tvGrado2 = new TextView(getContext());
                                    tvGrado2.setTextColor(Color.parseColor("#000000"));
                                    tvGrado2.setGravity(Gravity.LEFT);
                                    tvGrado2.setLayoutParams(tvParams);

                                    TextView tvInst1 = new TextView(getContext());
                                    tvInst1.setTextColor(Color.parseColor("#000000"));
                                    tvInst1.setText("GRADO ACADÉMICO");
                                    tvInst1.setTypeface(Typeface.DEFAULT_BOLD);
                                    tvInst1.setLayoutParams(tvParams);

                                    TextView tvInst2 = new TextView(getContext());
                                    tvInst2.setTextColor(Color.parseColor("#000000"));
                                    tvInst2.setGravity(Gravity.LEFT);
                                    tvInst2.setLayoutParams(tvParams);

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
                                    tvFechai1.setText("FECHA INICIAL");
                                    tvFechai1.setTypeface(Typeface.DEFAULT_BOLD);
                                    tvFechai1.setLayoutParams(tvParams);

                                    TextView tvFechai2 = new TextView(getContext());
                                    tvFechai2.setTextColor(Color.parseColor("#000000"));
                                    tvFechai2.setLayoutParams(tvParams);

                                    TextView tvFechaf1 = new TextView(getContext());
                                    tvFechaf1.setTextColor(Color.parseColor("#000000"));
                                    tvFechaf1.setText("FECHA FINAL");
                                    tvFechaf1.setTypeface(Typeface.DEFAULT_BOLD);
                                    tvFechaf1.setLayoutParams(tvParams);

                                    TextView tvFechaf2 = new TextView(getContext());
                                    tvFechaf2.setTextColor(Color.parseColor("#000000"));
                                    tvFechaf2.setLayoutParams(tvParams);


                                    tvCarr2.setText(responseM.getString("carr_profesional"));
                                    tvGrado2.setText(responseM.getString("maestria_grado_academico"));
                                    tvInst2.setText(responseM.getString("maestria_institución"));
                                    tvPais2.setText(responseM.getString("maestria_pais"));
                                    tvFechai2.setText(responseM.getString("maestria_fecha_inicial"));
                                    tvFechaf2.setText(responseM.getString("maestria_fecha_final"));

                                    lL1.addView(tvCarr1);
                                    lL2.addView(tvGrado1);
                                    lL3.addView(tvInst1);
                                    lL4.addView(tvPais1);
                                    lL5.addView(tvFechai1);
                                    lL6.addView(tvFechaf1);

                                    lL1.addView(tvCarr2);
                                    lL2.addView(tvGrado2);
                                    lL3.addView(tvInst2);
                                    lL4.addView(tvPais2);
                                    lL5.addView(tvFechai2);
                                    lL6.addView(tvFechaf2);

                                }


                                LinearLayout linearLayoutD = (LinearLayout) getActivity().findViewById(R.id.linearlayout3);

                                for( int i = 0; i < countDo; i++)
                                {
                                    JSONObject responseM = new JSONObject(responseArr3.getString(i));

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
                                    lL6.setLayoutParams(fechafParams);

                                    linearLayoutD.addView(lL1);
                                    linearLayoutD.addView(lL2);
                                    linearLayoutD.addView(lL3);
                                    linearLayoutD.addView(lL4);
                                    linearLayoutD.addView(lL5);
                                    linearLayoutD.addView(lL6);

                                    LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
                                    tvParams.setMargins(40,0,0,0);

                                    TextView tvCarr1 = new TextView(getContext());
                                    tvCarr1.setTextColor(Color.parseColor("#000000"));
                                    tvCarr1.setTypeface(Typeface.DEFAULT_BOLD);
                                    tvCarr1.setText("CARRERA PROFESIONAL");
                                    tvCarr1.setLayoutParams(tvParams);

                                    TextView tvCarr2 = new TextView(getContext());
                                    tvCarr2.setTextColor(Color.parseColor("#000000"));
                                    tvCarr2.setLayoutParams(tvParams);

                                    TextView tvGrado1 = new TextView(getContext());
                                    tvGrado1.setTextColor(Color.parseColor("#000000"));
                                    tvGrado1.setText("CARRERA PROFESIONAL");
                                    tvGrado1.setTypeface(Typeface.DEFAULT_BOLD);
                                    tvGrado1.setLayoutParams(tvParams);

                                    TextView tvGrado2 = new TextView(getContext());
                                    tvGrado2.setTextColor(Color.parseColor("#000000"));
                                    tvGrado2.setGravity(Gravity.LEFT);
                                    tvGrado2.setLayoutParams(tvParams);

                                    TextView tvInst1 = new TextView(getContext());
                                    tvInst1.setTextColor(Color.parseColor("#000000"));
                                    tvInst1.setText("GRADO ACADÉMICO");
                                    tvInst1.setTypeface(Typeface.DEFAULT_BOLD);
                                    tvInst1.setLayoutParams(tvParams);

                                    TextView tvInst2 = new TextView(getContext());
                                    tvInst2.setTextColor(Color.parseColor("#000000"));
                                    tvInst2.setGravity(Gravity.LEFT);
                                    tvInst2.setLayoutParams(tvParams);

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
                                    tvFechai1.setText("FECHA INICIAL");
                                    tvFechai1.setTypeface(Typeface.DEFAULT_BOLD);
                                    tvFechai1.setLayoutParams(tvParams);

                                    TextView tvFechai2 = new TextView(getContext());
                                    tvFechai2.setTextColor(Color.parseColor("#000000"));
                                    tvFechai2.setLayoutParams(tvParams);

                                    TextView tvFechaf1 = new TextView(getContext());
                                    tvFechaf1.setTextColor(Color.parseColor("#000000"));
                                    tvFechaf1.setText("FECHA FINAL");
                                    tvFechaf1.setTypeface(Typeface.DEFAULT_BOLD);
                                    tvFechaf1.setLayoutParams(tvParams);

                                    TextView tvFechaf2 = new TextView(getContext());
                                    tvFechaf2.setTextColor(Color.parseColor("#000000"));
                                    tvFechaf2.setLayoutParams(tvParams);


                                    tvCarr2.setText(responseM.getString("carr_profesional"));
                                    tvGrado2.setText(responseM.getString("doctorado_grado_academico"));
                                    tvInst2.setText(responseM.getString("doctorado_institución"));
                                    tvPais2.setText(responseM.getString("doctorado_pais"));
                                    tvFechai2.setText(responseM.getString("doctorado_fecha_inicial"));
                                    tvFechaf2.setText(responseM.getString("doctorado_fecha_final"));

                                    lL1.addView(tvCarr1);
                                    lL2.addView(tvGrado1);
                                    lL3.addView(tvInst1);
                                    lL4.addView(tvPais1);
                                    lL5.addView(tvFechai1);
                                    lL6.addView(tvFechaf1);

                                    lL1.addView(tvCarr2);
                                    lL2.addView(tvGrado2);
                                    lL3.addView(tvInst2);
                                    lL4.addView(tvPais2);
                                    lL5.addView(tvFechai2);
                                    lL6.addView(tvFechaf2);

                                }


                                String grado = responseP.getString("grado_academico");
                                String fechai = responseP.getString("semestre_ingreso");
                                String fechaf = responseP.getString("semestre_egreso");


                                perfilGrado.setText(grado);
                                perfilFechaI.setText(fechai);
                                perfilFechaF.setText(fechaf);
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