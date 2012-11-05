package com.ermes.controlGasto;

import java.util.Date;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ControlGastoActivity extends Activity implements OnClickListener, OnKeyListener {

    EditText gasto;
    Button ingresar;
    TextView total, cantidad;
    Spinner concepto;
    ArrayAdapter<CharSequence> adaptador;
    Date fecha = new Date();
    String fechaFormat = (String) DateFormat.format("dd-MM-yy hh:mm", fecha);
    private static final int MES = 10;

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

                /*
                 * Dialog d = new Dialog(this); d.setTitle("OK !!!"); TextView
                 * tv = new TextView(this); tv.setText("el concepto es " +
                 * concepto.getSelectedItem() + " la fecha es " + fechaFormat);
                 * d.setContentView(tv); d.show();
                 */
                Apunte entrada = new Apunte(ControlGastoActivity.this);
                entrada.abrir();
                total.setText(entrada.listarApuntes());
                cantidad.setText("Total acumulado: " + entrada.CANTIDAD);
                entrada.cerrar();

            }
        }

    }

    @Override
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
        total = (TextView) findViewById(R.id.txtTotal);
        cantidad = (TextView) findViewById(R.id.txtCantidad);

        adaptador = ArrayAdapter.createFromResource(this, R.array.arrayConcepto, android.R.layout.simple_spinner_item);
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        concepto.setAdapter(adaptador);

        ingresar.setGravity(Gravity.LEFT);
        ingresar.setOnClickListener(this);
        gasto.setOnKeyListener(this);
        cantidad.setGravity(Gravity.RIGHT);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_control_gasto, menu);
        return true;
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
            accion();
        }
        return false;
    }
}
