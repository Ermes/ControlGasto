package com.ermes.controlGasto;

import java.util.Date;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class ControlGastoActivity extends Activity implements OnClickListener, OnKeyListener {

    EditText                   gasto;
    Button                     ingresar;
    TextView                   total;
    Spinner                    concepto;
    ArrayAdapter<CharSequence> arrConcepto;
    Date                       fecha       = new Date();
    String                     fechaFormat = (String) DateFormat.format("dd-MM-yy hh:mm", fecha);
    private static final int   MES         = 10;

    public void accion() {
        boolean funciona = true;
        // oculto el teclado cuando le doy al enter
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        try {
            Apunte entrada = new Apunte(ControlGastoActivity.this);
            entrada.abrir();
            entrada.crearApunte(gasto.getText().toString(), fecha.getTime(), concepto.getSelectedItem().toString());
            entrada.cerrar();

        } catch (Exception e) {
            funciona = false;
            // creo un msg view sobre la marcha agregando el textview
            String error = e.toString();
            Dialog d = new Dialog(this);
            d.setTitle("error");
            TextView tv = new TextView(this);
            tv.setText(error);
            d.setContentView(tv);
            d.show();

        } finally {
            if (funciona) {

                listar_movimientos();
            }
        }
    }

    public void listar_movimientos() {
        Apunte entrada = new Apunte(ControlGastoActivity.this);
        TableLayout tl = (TableLayout) findViewById(R.id.tbCantidad);
        entrada.abrir();
        String contenido = entrada.tablaApuntes();
        String[] cFilas = contenido.split("\n");
        tl.removeAllViewsInLayout();

        TableRow th = new TableRow(this);
        th.setId(10);
        th.setBackgroundColor(Color.parseColor("#33b5e5"));
        th.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

        TextView label_id = new TextView(this);
        label_id.setText("id");
        label_id.setTextColor(Color.WHITE);
        label_id.setPadding(20, 1, 50, 20);
        th.addView(label_id);
        TextView label_fecha = new TextView(this);
        label_fecha.setText("fecha");
        label_fecha.setTextColor(Color.WHITE);
        label_fecha.setPadding(20, 1, 50, 20);
        th.addView(label_fecha);
        TextView label_concepto = new TextView(this);
        label_concepto.setText("concepto");
        label_concepto.setTextColor(Color.WHITE);
        label_concepto.setPadding(20, 1, 50, 20);
        th.addView(label_concepto);
        TextView label_cantidad = new TextView(this);
        label_cantidad.setText("€");
        label_cantidad.setTextColor(Color.WHITE);
        label_cantidad.setPadding(20, 1, 50, 20);
        th.addView(label_cantidad);

        tl.addView(th);

        for (String cFila : cFilas) {

            TableRow tr = new TableRow(this);
            tr.setLayoutParams(new LayoutParams(20, LayoutParams.FILL_PARENT));
            String[] cColumnas = cFila.split(",");

            for (String columna : cColumnas) {
                TextView cantidad = new TextView(this);
                cantidad.setPadding(20, 1, 50, 20);
                cantidad.setText(columna.toString());

                tr.addView(cantidad);
            }

            tl.addView(tr, new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.FILL_PARENT));

        }

        // total.setText("Total acumulado: " + entrada.CANTIDAD);

        entrada.cerrar();

    }

    public void onClick(View arg0) {
        if (gasto.getText().toString().equals("")) {
            Toast.makeText(this, "Debes rellenar la cantidad del gasto", Toast.LENGTH_SHORT).show();
        } else {
            accion();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_gasto);

        concepto = (Spinner) findViewById(R.id.spinner);
        gasto = (EditText) findViewById(R.id.txtGasto);
        ingresar = (Button) findViewById(R.id.btnIngresar);
        // total = (TextView) findViewById(R.id.txtTotal);

        arrConcepto = ArrayAdapter
                .createFromResource(this, R.array.arrayConcepto, android.R.layout.simple_spinner_item);
        arrConcepto.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        concepto.setAdapter(arrConcepto);

        ingresar.setGravity(Gravity.LEFT);
        ingresar.setOnClickListener(this);
        gasto.setOnKeyListener(this);

        listar_movimientos();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_control_gasto, menu);
        return true;
    }

    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
            accion();
        }
        return false;
    }
}
