package com.example.restaurant.Gerentes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.restaurant.Helpers.ServerAddress;
import com.example.restaurant.Modelos.Reportes;
import com.example.restaurant.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.widget.DatePicker;
import android.app.DatePickerDialog;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class ReportesRangosActivity extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {


    ListView lstReporteDiario;
    private JsonObjectRequest jsonObjectRequest;
    private MyArrayAdapter adapter;
    private RequestQueue request;
    EditText txtFecha1;
    EditText txtFecha2;
    private Button btnBuscarfecha;
    final Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportes_rangos);
        lstReporteDiario = (ListView) findViewById(R.id.lstReporteRango);
        request = Volley.newRequestQueue(this);
        btnBuscarfecha = (Button) findViewById(R.id.btnBuscar);
        txtFecha1 = (EditText) findViewById(R.id.txtFecha1ReporteRango);
        txtFecha2 = (EditText) findViewById(R.id.txtFecha2ReporteRango);
        final DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String formateoFecha = "yyyy-MM-dd";
                SimpleDateFormat dateFormat = new SimpleDateFormat(formateoFecha, Locale.US);
                txtFecha1.setText(dateFormat.format(calendar.getTime()));
            }
        };
        final DatePickerDialog.OnDateSetListener date2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String formateoFecha = "yyyy-MM-dd";
                SimpleDateFormat dateFormat = new SimpleDateFormat(formateoFecha, Locale.US);
                txtFecha2.setText(dateFormat.format(calendar.getTime()));
            }
        };
        txtFecha1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*actualizarInput1();*/
                new DatePickerDialog(ReportesRangosActivity.this, date1, calendar.get(Calendar.YEAR)
                        ,calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        txtFecha2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*actualizarInput2();*/
                new DatePickerDialog(ReportesRangosActivity.this, date2, calendar.get(Calendar.YEAR)
                        ,calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btnBuscarfecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!txtFecha1.getText().toString().equals("") && !txtFecha2.getText().toString().equals("")){
                    consultarReporteDiario(txtFecha1.getText().toString(),txtFecha2.getText().toString());
                }
                else
                Toast.makeText(ReportesRangosActivity.this,"no a seleccionado fechas",Toast.LENGTH_SHORT).show();

            }
        });

    }

    public void consultarReporteDiario(String fecha1,String fecha2) {
        String url = ServerAddress.SERVER_IP + "wsJSONCargarReportesRango.php?fechaInicio="+fecha1+"&fechaFin="+fecha2;
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);
    }

    class MyArrayAdapter extends ArrayAdapter<Reportes> {
        Context context;
        int textViewRecursoId;
        ArrayList<Reportes> objects;

        public MyArrayAdapter(Context context, int textViewResourceId, ArrayList<Reportes> objects) {
            super(context, textViewResourceId, objects);
            this.context = context;
            this.textViewRecursoId = textViewResourceId;
            this.objects = objects;
        }

        public View getView(final int position, View convertView, ViewGroup viewGroup) {

            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(this.textViewRecursoId, null);

            TextView lblId = (TextView) view.findViewById(R.id.lblIdReporte);
            TextView lblMesa = (TextView) view.findViewById(R.id.lblMesaReporte);
            TextView lblTotal = (TextView) view.findViewById(R.id.lblTotalReporte);
            TextView lblObservaciones = (TextView) view.findViewById(R.id.lblObservacionesReporte);
            TextView lblMesero = (TextView) view.findViewById(R.id.lblMeseroReporte);
            TextView lblFechaHora = (TextView) view.findViewById(R.id.lblFechaHoraReporte);
            TextView lblStatus = (TextView) view.findViewById(R.id.lblStatusReporte);

            lblId.setText("#"+objects.get(position).getId());
            lblMesa.setText("Mesa:"+objects.get(position).getMesa());
            lblTotal.setText("Total:$"+objects.get(position).getTotal());
            lblObservaciones.setText("Observaciones:"+objects.get(position).getObservaciones());
            lblMesero.setText("Mesero:"+objects.get(position).getMesero());
            lblFechaHora.setText("Fecha y Hora:"+objects.get(position).getFecha()+" "+objects.get(position).getHora());
            lblStatus.setText("Status:"+objects.get(position).getStatus());
            return view;
        }

    }


    @Override
    public void onErrorResponse(VolleyError error) {
    }

    @Override
    public void onResponse(JSONObject response) {
        Reportes reportes = null;
        ArrayList<Reportes> lista = new ArrayList<>();
        JSONObject jsonObject;
        JSONArray json = response.optJSONArray("reportes");
        try {
            for (int i=0;i<json.length();i++){
                reportes = new Reportes();
                jsonObject = json.getJSONObject(i);
                reportes.setId(jsonObject.optString("id"));
                reportes.setMesa(jsonObject.optString("mesa"));
                reportes.setTotal(jsonObject.optString("total"));
                reportes.setObservaciones(jsonObject.optString("observaciones"));
                reportes.setMesero(jsonObject.optString("mesero"));
                reportes.setFecha(jsonObject.optString("fecha"));
                reportes.setHora(jsonObject.optString("hora"));
                reportes.setStatus(jsonObject.optString("status"));
                lista.add(reportes);
            }
            adapter = new MyArrayAdapter(this,R.layout.layout_reporte,lista);
            lstReporteDiario.setAdapter(adapter);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }
}

