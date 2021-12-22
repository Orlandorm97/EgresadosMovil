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

import androidx.annotation.DrawableRes;
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
import java.lang.reflect.Array;

public class TrayectoriaAcaFragment extends Fragment {
    TextView perfilGrado, perfilFechaI, perfilFechaF, maestriaCarrera, maestriaGrado, maestriaInstitucion, maestriaPais, maestriaFechaI, maestriaFechaF, doctoradoCarrera, doctoradoGrado, doctoradoInstitucion, doctoradoPais, doctoradoFechaI, doctoradoFechaF;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_trayectoriaaca, container, false);

        perfilGrado = (TextView) v.findViewById(R.id.perfilGrado);
        perfilFechaI = (TextView) v.findViewById(R.id.perfilFechaI);
        perfilFechaF = (TextView) v.findViewById(R.id.perfilFechaF);

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

                                LinearLayout linearLayoutM = (LinearLayout) getActivity().findViewById(R.id.linearlayout2);

                                LinearLayout.LayoutParams lLparams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1);

                                LinearLayout.LayoutParams fechafParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1);
                                fechafParams.setMargins(0,0,0,40);

                                Drawable line = getResources().getDrawable(R.drawable.custom_line);
                                Drawable borde = getResources().getDrawable(R.drawable.custom_border);

                                int[] lL = new int[countMa];
                                for( int i = 0; i < countMa; i++)
                                {
                                    JSONObject responseM = new JSONObject(responseArr2.getString(i));

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
                                    lL6.setLayoutParams(fechafParams);


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

                                    String id_maestria = responseM.getString("id_maestria");
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

                                    lLAll.addView(lL1);
                                    lLAll.addView(lL2);
                                    lLAll.addView(lL3);
                                    lLAll.addView(lL4);
                                    lLAll.addView(lL5);
                                    lLAll.addView(lL6);

                                    lLAll.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            // ADD your action here

                                            Toast.makeText(getContext(), "Id_Maestria  "+ id_maestria, Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                    linearLayoutM.addView(lLAll);
                                }


                                LinearLayout linearLayoutD = (LinearLayout) getActivity().findViewById(R.id.linearlayout3);

                                for( int i = 0; i < countDo; i++)
                                {
                                    JSONObject responseM = new JSONObject(responseArr3.getString(i));

                                    LinearLayout lLAll = new LinearLayout(getContext());
                                    lLAll.setId(i);
                                    lLAll.setOrientation(LinearLayout.VERTICAL);
                                    lLAll.setBackground(borde);
                                    lLAll.isClickable();
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
                                    lL6.setLayoutParams(fechafParams);


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

                                    String id_doctorado = responseM.getString("id_doctorado");
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

                                    lLAll.addView(lL1);
                                    lLAll.addView(lL2);
                                    lLAll.addView(lL3);
                                    lLAll.addView(lL4);
                                    lLAll.addView(lL5);
                                    lLAll.addView(lL6);

                                    lLAll.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            // ADD your action here

                                            Toast.makeText(getContext(), "Id_Doctorado  "+ id_doctorado, Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                    linearLayoutD.addView(lLAll);

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